package database;

import model.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InteractionWithPostgresDb {
    private static final BasicDataSource SOURCE = new BasicDataSource();
    private static final InteractionWithPostgresDb INSTANCE = new InteractionWithPostgresDb();
    private static final Logger LOG = LogManager.getLogger(InteractionWithPostgresDb.class.getName());

    private InteractionWithPostgresDb() {
        SOURCE.setDriverClassName("org.postgresql.Driver");
        SOURCE.setUrl("jdbc:postgresql://127.0.0.1:5432/cinema_ticket");
        SOURCE.setUsername("postgres");
        SOURCE.setPassword("root");
        SOURCE.setMinIdle(5);
        SOURCE.setMaxIdle(10);
        SOURCE.setMaxOpenPreparedStatements(100);
    }

    public static InteractionWithPostgresDb getInstance() {
        return INSTANCE;
    }

    /**
     * Возвращает зрительный зал с местами
     * переделать
     *
     * @return зрительный зал с местами
     */
    public Hall getHall() {
        Hall hall = new Hall();
        try (Connection connection = SOURCE.getConnection();
        ) {
            ResultSet rs = connection.createStatement().executeQuery("SELECT row FROM halls AS h ORDER BY row DESC LIMIT 1;");
            rs.next();
            int countOfRow = rs.getInt("row");
            rs.close();
            for (int i = 1; i <= countOfRow; i++) {
                PreparedStatement st = connection.prepareStatement("SELECT * FROM halls AS h WHERE h.row = ?");
                st.setInt(1, i);
                ResultSet rsIn = st.executeQuery();
                Row row = new Row();
                while (rsIn.next()) {
                    row.addSeat(new Seat(
                            rsIn.getInt("row"),
                            rsIn.getInt("seat"),
                            rsIn.getInt("price"),
                            StatusOfPlace.valueOf(rsIn.getString("status"))
                    ));
                }
                hall.addRow(row);
                hall.incrementNumbOfRows();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hall;
    }

    /**
     * Возвращает сатус места
     * true - свободно
     * false - занято
     *
     * @param row    номер ряда места
     * @param column номер места в ряду
     * @return статус места в залле
     */
    public boolean placeIsFree(int row, int column) {
        boolean placeStatus = false;
        try (Connection connection = SOURCE.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "SELECT * FROM halls AS h WHERE h.row = ? AND h.seat = ?"
             );
        ) {
            st.setInt(1, row);
            st.setInt(2, column);
            ResultSet rs = st.executeQuery();
            rs.next();
            if (rs.getString("status").equals("FREE")) {
                placeStatus = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return placeStatus;
    }

    /**
     * блокирует место на время покупки
     *
     * @param row    номер ряда места
     * @param column номер места в ряду
     */
    public void blockedPlace(int row, int column) {
        try (Connection connection = SOURCE.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "UPDATE halls AS h SET status = ? WHERE h.row = ? AND h.seat = ?"
             );
        ) {
            st.setString(1, "BLOCKED");
            st.setInt(2, row);
            st.setInt(3, column);
            st.executeUpdate();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * изменяет статус места на куплено, после покупки билета
     *
     * @param row    номер ряда места
     * @param column номер места в ряду
     */
    public void buyPlace(int row, int column) {
        try (Connection connection = SOURCE.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "UPDATE halls AS h SET status = ? WHERE h.row = ? AND h.seat = ?"
             );
        ) {
            st.setString(1, "SALES");
            st.setInt(2, row);
            st.setInt(3, column);
            st.executeUpdate();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * сохраняет информацию о покупателе в базу данных
     * переделать
     *
     * @param buyer - покупатель
     */
    public void addBuyer(Buyer buyer) {
        try (Connection connection = SOURCE.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "INSERT INTO account (full_name, phone_number, number_of_place) VALUES (?, ?, ?)"
             );
        ) {
            st.setString(1, buyer.getFullName());
            st.setString(2, buyer.getPhoneNumber());
            st.setInt(3, buyer.getNumberOfPlace());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * поиск информации о покупателе по ФИО
     * переделать
     *
     * @param fullName - ФИО покупателя
     * @return buyer - покупатель
     */
    public Buyer getBuyer(String fullName) {
        Buyer buyer = new Buyer();
        try (Connection connection = SOURCE.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "SELECT * FROM account AS a WHERE a.full_name = ?"
             );
        ) {
            st.setString(1, fullName);
            ResultSet rs = st.executeQuery();
            rs.next();
            buyer.setFullName(rs.getString("full_name"));
            buyer.setPhoneNumber(rs.getString("phone_number"));
            buyer.setNumberOfPlace(rs.getInt("number_of_place"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buyer;
    }

    /**
     * Определяет id места в кинозале по номеру ряда и номеру места в этом ряде
     *
     * @param row    номер ряда
     * @param column номер места в ряде
     * @return id места в БД
     */
    public int findIdPlaceByRowAndColumn(int row, int column) {
        int id = -1;
        try (Connection connection = SOURCE.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "SELECT id FROM halls AS h WHERE h.row = ? AND h.seat = ?"
             );
        ) {
            st.setInt(1, row);
            st.setInt(2, column);
            ResultSet rs = st.executeQuery();
            rs.next();
            id = rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * проводит атомарную операцию изменеия статуса места на SALES
     * и добавляет в ДБ информацию покупателе,
     * если на одном из этапов происходит ошибка, то все выполненые операции отменяются.
     *
     * @param row         - ряд места
     * @param column      - номер места в ряду
     * @param userName    - полное имя покупателя
     * @param phoneNumber - телефонный номер покупателя
     * @return true - если операция проведена успешно
     */
    public boolean buyPlaceAndAddBuyerToDB(int row, int column, String userName, String phoneNumber) {
        int seatID = -1;
        boolean resultOperation = false;
        try {
            Connection connection = SOURCE.getConnection();
            try {
                connection.setAutoCommit(false);
                if (!seatISFree(connection, row, column)) {
                    throw new SQLException("Место уже куплено");
                }
                seatID = getSeatId(connection, row, column);
                changeStatusSeatToSales(connection, seatID);
                insertAccountIntoDB(connection, userName, phoneNumber, seatID);
                connection.commit();
                resultOperation = true;
                connection.close();
            } catch (SQLException e) {
                connection.rollback();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return resultOperation;
    }

    /**
     * возвращает true - если место свободно
     *
     * @param connection - соединение с БД
     * @param row        - ряд места
     * @param column     - номер места в ряду
     * @return - состояние места (куплено или нет)
     */
    private boolean seatISFree(Connection connection, int row, int column) {
        boolean result = false;
        PreparedStatement getSeatStatus = null;
        try {
            getSeatStatus = connection.prepareStatement(
                    "SELECT status FROM halls AS h WHERE h.row = ? AND h.seat = ?"
            );
            getSeatStatus.setInt(1, row);
            getSeatStatus.setInt(2, column);
            ResultSet rs = getSeatStatus.executeQuery();
            rs.next();
            String status = rs.getString("status");
            if (status.equals("FREE")) {
                result = true;
            }
            getSeatStatus.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * получает id места из БД
     *
     * @param connection - соединение с БД
     * @param row        - ряд места
     * @param column     - номер места в ряду
     * @return - id места
     */
    private int getSeatId(Connection connection, int row, int column) {
        int seatId = -1;
        PreparedStatement getSeatIdStatement = null;
        try {
            getSeatIdStatement = connection.prepareStatement(
                    "SELECT id FROM halls AS h WHERE h.row = ? AND h.seat = ?"
            );
            getSeatIdStatement.setInt(1, row);
            getSeatIdStatement.setInt(2, column);
            ResultSet resultSetFindingId = getSeatIdStatement.executeQuery();
            resultSetFindingId.next();
            seatId = resultSetFindingId.getInt("id");
            getSeatIdStatement.close();
            resultSetFindingId.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seatId;
    }

    /**
     * изменение статуса места на SALES
     *
     * @param connection - соединение с БД
     * @param id         - id места в БД
     */
    private void changeStatusSeatToSales(Connection connection, int id) {
        PreparedStatement changeStatusToSalesStatement = null;
        try {
            changeStatusToSalesStatement = connection.prepareStatement(
                    "UPDATE halls AS h SET status = ? WHERE h.id = ?"
            );
            changeStatusToSalesStatement.setString(1, "SALES");
            changeStatusToSalesStatement.setInt(2, id);
            changeStatusToSalesStatement.executeUpdate();
            changeStatusToSalesStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * добавление информации о покупателе в базу данных
     *
     * @param connection  - соединение с БД
     * @param userName    - полное имя покупателя
     * @param phoneNumber - телефонный номер покупателя
     * @param id          - id места в БД
     */
    private void insertAccountIntoDB(Connection connection, String userName, String phoneNumber, int id) {
        PreparedStatement insertIntoAccountStatement = null;
        try {
            insertIntoAccountStatement = connection.prepareStatement(
                    "INSERT INTO accounts (fullname, phonemunber, halls_id) VALUES (?, ?, ?)"
            );
            insertIntoAccountStatement.setString(1, userName);
            insertIntoAccountStatement.setString(2, phoneNumber);
            insertIntoAccountStatement.setInt(3, id);
            insertIntoAccountStatement.execute();
            insertIntoAccountStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
