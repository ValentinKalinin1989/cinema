package model;

import java.util.Objects;

/**
 * информация о покупателе
 */
public class Buyer {
    /**
     * ФИО покупателя
     */
    private String fullName;
    /**
     * телефонный номер
     */
    private String phoneNumber;
    /**
     * номер купленного места
     */
    private Integer numberOfPlace;

    public Buyer() {
    }

    public Buyer(String fullName, String phoneNumber, Integer numberOfPlace) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.numberOfPlace = numberOfPlace;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getNumberOfPlace() {
        return numberOfPlace;
    }

    public void setNumberOfPlace(Integer numberOfPlace) {
        this.numberOfPlace = numberOfPlace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Buyer buyer = (Buyer) o;
        return Objects.equals(fullName, buyer.fullName) &&
                Objects.equals(phoneNumber, buyer.phoneNumber) &&
                Objects.equals(numberOfPlace, buyer.numberOfPlace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, phoneNumber, numberOfPlace);
    }

    @Override
    public String toString() {
        return "Buyer{" +
                "fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", numberOfPlace=" + numberOfPlace +
                '}';
    }

}
