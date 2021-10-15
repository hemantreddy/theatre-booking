package com.movietheater.code.model;

import com.movietheater.code.conf.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MovieTheater {

    ArrayList<Row> rows = new ArrayList<>();


    Map<String, List<String>> bookedTickets = new HashMap<>();


    public MovieTheater() {
        for (int row = 0; row < Config.NUM_ROWS; row++) {
            rows.add(new Row());
        }
    }

    /**
     * n -> numRows
     * O(n) impl to get the row with min continuousVacantSeats greater than required seats
     * <p>
     * Alternatively, can use PriorityQueue (but updates for the next and previous rows would then be non-trivial)
     *
     * @param requiredSeats
     * @return selectedRow
     */
    public Integer getBestFitRowForBooking(int requiredSeats) {
        // returns the row with the min continuousVacantSeats that is greater than requiredSeats

        int selectedRow = -1;
        int min = Config.NUM_COLS + 1;

        /*

         - trade off here is going through all the rows and finding the row that fits the exact number of seats without wasting any.
          - we could stop looking as soon as we find the first set of seats >= the requested seats but that would decrease the number
          of seats actually being utilized
          -  one more thing that could be done would respond with a success message as soon as we have found
          our first suitable row, and later add the confirmation email/message to a queue.
          - Just to make the user experience a tad bit faster
        */
        for (int row = 0; row < Config.NUM_ROWS; row++) {
            int continuousVacantSeats = rows.get(row).getMaxContinuousVacant();
            if (continuousVacantSeats >= requiredSeats && continuousVacantSeats < min) {
                selectedRow = row;
                min = continuousVacantSeats;
            }
        }
        return selectedRow;

    }

    // O(m)
    public void book(int numOfSeatsRequested, int rowIndex, String requestId) {

        // book numOfSeatsRequested seats starting from firstAvailable of rowIndex
        Row row = rows.get(rowIndex);

        List<String> allottedSeats = new ArrayList<>();
        int pointerToFirstEmptySeat = row.getFirstAvailable();

        // O(m); m -> number of seats / number of columns
        for (int seat = 0; seat < numOfSeatsRequested; seat++) {
            row.getSeats().get((seat + pointerToFirstEmptySeat)).setOccupied(true);
            char rowId = (char) ('J' - rowIndex);
            allottedSeats.add(new StringBuilder().append(rowId).append(seat + pointerToFirstEmptySeat + 1).toString());
        }
        // add buffer -> O(m)
        // adding buffer in currentRow - 1
        if (rowIndex > 0) {
            for (int i = 0; i < numOfSeatsRequested; i++) {
                rows.get(rowIndex - 1).getSeats().get((i + pointerToFirstEmptySeat)).setReserved(true);
            }
            //recalculate maxContinuousAvailable
            rows.get(rowIndex - 1).recomputeAvailability();
        }

        //  O(m)
        // adding buffer in currentRow + 1
        if (rowIndex < Config.NUM_ROWS - 1) {
            for (int i = 0; i < numOfSeatsRequested; i++) {
                rows.get(rowIndex + 1).getSeats().get((i + pointerToFirstEmptySeat)).setReserved(true);
            }
            //recalculate maxContinuousAvailable
            rows.get(rowIndex + 1).recomputeAvailability();
        }


        pointerToFirstEmptySeat += numOfSeatsRequested;

        // adding buffer ro the right side of the booked seats
        int finalReserved = 0;
        for (int i = 0; i < Config.BUFFER; i++) {
            if ((pointerToFirstEmptySeat + i) < Config.NUM_COLS) {
                row.getSeats().get(pointerToFirstEmptySeat + i).setReserved(true);
                finalReserved++;
            }
        }
        pointerToFirstEmptySeat += finalReserved;


        // O(m)
        bookedTickets.put(requestId, allottedSeats);

        row.setFirstAvailable(pointerToFirstEmptySeat);

        row.setMaxContinuousVacant(Config.NUM_COLS - pointerToFirstEmptySeat);
        // O(m)s
        row.recomputeAvailability();
    }


    public List<String> getBookedTickets(String requestId) {
        return bookedTickets.get(requestId);
    }

}
