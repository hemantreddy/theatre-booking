package com.movietheater.code.model;


public class Seat {

    boolean isOccupied;
    boolean isReserved;

    public Seat() {
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public boolean isReserved() { // buffer
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }
}
