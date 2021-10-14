package com.movietheater.code.model;

import com.movietheater.code.conf.Config;

import java.util.ArrayList;


public class Row {

    ArrayList<Seat> seats = new ArrayList<>();
    int maxContinuousVacant = Config.NUM_COLS;
    int firstAvailable = 0;

    public Row() {
        for (int column = 0; column < Config.NUM_COLS; column++) {
            seats.add(new Seat());
        }
    }

    public ArrayList<Seat> getSeats() {
        return seats;
    }

    public void setSeats(ArrayList<Seat> seats) {
        this.seats = seats;
    }

    public int getMaxContinuousVacant() {
        return maxContinuousVacant;
    }

    public void setMaxContinuousVacant(int maxContinuousVacant) {
        this.maxContinuousVacant = maxContinuousVacant;
    }

    public int getFirstAvailable() {
        return firstAvailable;
    }

    public void setFirstAvailable(int firstAvailable) {
        this.firstAvailable = firstAvailable;
    }

    // O (number of columns) or O(m)
    public void recomputeAvailability() {
        int cur = 0;
        int st = 0;

        int max = 0;
        int maxst = 0;
        while (st < Config.NUM_COLS && (seats.get(st).isOccupied() || seats.get(st).isReserved())) {
            ++st;
        }
        maxst = st;

        for (int i = st; i < Config.NUM_COLS; i++) {
            if (!(seats.get(i).isReserved || seats.get(i).isOccupied)) {
                cur++;
                if (cur > max) {
                    max = cur;
                    maxst = st;
                }
            } else {
                cur = 0;
                st = i + 1;
            }
        }
        maxContinuousVacant = max;
        firstAvailable = maxst;
    }
}
