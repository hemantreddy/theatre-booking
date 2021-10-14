package com.movietheater.code.model;

import com.movietheater.code.conf.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MovieTheater {

    ArrayList<Row> rows = new ArrayList<Row>();


    Map<String, List<String>> requestTicketMap = new HashMap<>();


    public MovieTheater() {
        for (int i = 0; i < Config.NUM_ROWS; i++) {
            rows.add(new Row());
        }
    }

    /**
     * n -> numRows
     * O(n) impl to get the row with min continuousVacantSeats greater than required seats
     * <p>
     * Alternatively use PriorityQueue (but updates for the next and previous rows would then be non-trivial)
     *
     * @param requiredSeats
     * @return
     */
    public Integer getBestFitRowForBooking(int requiredSeats) {
        // returns the row with the min continuousVacantSeats that is greater than requiredSeats

        int resultingRowIndex = -1;
        int min = Config.NUM_COLS + 1;

        for (int i = 0; i < Config.NUM_ROWS; i++) {
            if (rows.get(i).getMaxContinuousVacant() >= requiredSeats && rows.get(i).getMaxContinuousVacant() < min) {
                resultingRowIndex = i;
                min = rows.get(i).getMaxContinuousVacant();
            }
        }
        return resultingRowIndex;

    }

    // O(m)
    public void book(int numSeats, int rowIndex, String requestId) {

        // book numSeats seats starting from firstAvailable of rowIndex
        Row row = rows.get(rowIndex);

        List<String> seatIds = new ArrayList<>();
        int pointerToFirstEmptySeat = row.getFirstAvailable();

        // O(m); m -> number of seats / number of columns
        for (int i = 0; i < numSeats; i++) {
            row.getSeats().get((i + pointerToFirstEmptySeat)).setOccupied(true);
            char rowId = (char) ('J' - rowIndex);
            seatIds.add(new StringBuilder().append(rowId).append(i + pointerToFirstEmptySeat +1).toString());
        }
        // add buffer -> O(m)
        if (rowIndex > 0) {
            for (int i = 0; i < numSeats; i++) {
                rows.get(rowIndex - 1).getSeats().get((i + pointerToFirstEmptySeat)).setReserved(true);
            }
            //recalculate maxContinuousAvailable
            rows.get(rowIndex - 1).recomputeAvailability();
        }

        //  O(m)
        if (rowIndex < Config.NUM_ROWS - 1) {
            for (int i = 0; i < numSeats; i++) {
                rows.get(rowIndex + 1).getSeats().get((i + pointerToFirstEmptySeat)).setReserved(true);
            }
            //recalculate maxContinuousAvailable
            rows.get(rowIndex + 1).recomputeAvailability();
        }


        pointerToFirstEmptySeat += numSeats;


        int c = 0;
        for (int i = 0; i < 3; i++) {
            if ((pointerToFirstEmptySeat + i) < Config.NUM_COLS) {
                row.getSeats().get(pointerToFirstEmptySeat + i).setReserved(true);
                c++;
            }
        }
        pointerToFirstEmptySeat+=c;


        // O(m)
        requestTicketMap.put(requestId, seatIds);

        row.setFirstAvailable(pointerToFirstEmptySeat);

        row.setMaxContinuousVacant(Config.NUM_COLS - pointerToFirstEmptySeat);
        // O(m)s
        row.recomputeAvailability();
    }


    public List<String> getSeatIds(String requestId) {
        return requestTicketMap.get(requestId);
    }

}
