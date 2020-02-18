package model;

import java.util.Objects;

/**
 * хранит информацию о месте в зале
 */
public class Seat implements Comparable {
    /**
     * номер ряда
     */
    private int row;
    /**
     * номер места в ряду
     */
    private int column;
    /**
     * стоимость билета
     */
    private int price;
    /**
     * статус
     * FREE - свободно
     * BLOCKED - заблокировано
     * SALES - куплено
     */
    private StatusOfPlace status;

    public Seat() {
    }

    public Seat(int row, int column, int price, StatusOfPlace statusOfPlace)  {
        this.row = row;
        this.column = column;
        this.price = price;
        this.status = statusOfPlace;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public StatusOfPlace getStatus() {
        return status;
    }

    public void setStatus(StatusOfPlace status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return row == seat.row &&
                column == seat.column &&
                price == seat.price &&
                status == seat.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, price, status);
    }

    @Override
    public String toString() {
        return "Seat{" +
                "row=" + row +
                ", column=" + column +
                ", price=" + price +
                ", status=" + status +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        int result = 0;
        int numberThis = this.getColumn();
        int numberO = ((Seat) o).getColumn();
        if (numberThis > numberO) result = 1;
        if (numberThis < numberO) result = -1;
        return result;
    }
}
