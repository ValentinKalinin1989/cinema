package model;

import java.util.ArrayList;
import java.util.List;

/**
 * создает зрительный зал, который используется для передачи в качестве json объекта на html страницу
 */

public class Hall {
    /**
     * число рядов
     */
    private int numberOfRows = 0;
    /**
     * список с рядами мест
     */
    private List<Row> rowList;

    public Hall() {
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public List<Row> getRowQueue() {
        return rowList;
    }

    public void addRow (Row row) {
        if(rowList == null) {
            rowList = new ArrayList<>();
        }
        rowList.add(row);
    }

    public void incrementNumbOfRows() {
        numberOfRows++;
    }
}
