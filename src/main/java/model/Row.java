package model;

import java.util.ArrayList;
import java.util.List;

/**
 * хранит места в ряду
 */
public class Row implements Comparable{
    /**
     * список мест
     */
    private List<Seat> seatList;

    public Row() {
    }

    /**
     * добавление мест в ряд
     *
     * @param seat место
     */
    public void addSeat(Seat seat) {
        if (seatList == null) {
            seatList = new ArrayList<>();
        }
        seatList.add(seat);
    }

    /**
     * получения списка мест
     *
     * @return список мест в ряду
     */
    public List<Seat> getSeatList() {
        return seatList;
    }

    @Override
    public int compareTo(Object o) {
        int result = 0;
        int thisRow = this.getSeatList().get(0).getRow();
        int objectORow = ((Row) o).getSeatList().get(0).getRow();
        if (thisRow > objectORow) result = 1;
        if (thisRow < objectORow) result = -1;
        return result;
    }
}
