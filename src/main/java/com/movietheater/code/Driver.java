package com.movietheater.code;

import com.movietheater.code.conf.Config;
import com.movietheater.code.exceptions.BookingLimitExceededException;
import com.movietheater.code.exceptions.NotEnoughContinuousSeats;
import com.movietheater.code.model.MovieTheater;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class Driver {

    public static void main(String[] args) throws IOException {
        String inputFile;
        if(args.length < 1) {
            System.out.println("No input file passed, using the default input file");
            inputFile = Config.DEFAULT_INPUT_FILE;
        } else {
            inputFile = args[0];
        }
        File file = new File(inputFile);

        BufferedReader buf = new BufferedReader(new FileReader(file));

        StringBuilder output = new StringBuilder();

        BufferedWriter writer = new BufferedWriter(new FileWriter(Config.DEFAULT_OUTPUT_FILE));



        MovieTheater theater = new MovieTheater();

        String bookingRequest = buf.readLine();
        while (bookingRequest != null && !bookingRequest.isEmpty()) {
            // Read each request
            String[] splits = bookingRequest.split(" ");
            String reqId = splits[0];
            int numSeats = Integer.parseInt(splits[1]);

            if (numSeats > Config.NUM_COLS) {
                try {
                    throw new BookingLimitExceededException("Max booking limit is " + Config.NUM_COLS + ". Please split your booking into multiple bookings. Sorry for the inconvenience.");
                } catch (BookingLimitExceededException e) {
                    System.err.println(e.getMessage());
                    bookingRequest = buf.readLine();
                    continue;
                }
            }
            // O(n)`
            Integer bestFitRowForBooking = theater.getBestFitRowForBooking(numSeats);

            if (bestFitRowForBooking == -1) {
                try {
                    throw new NotEnoughContinuousSeats("Not enough seats available together. Sorry for the inconvenience.");
                } catch (NotEnoughContinuousSeats e) {
                    System.err.println(e.getMessage());
                    bookingRequest = buf.readLine();
                    continue;
                }
            }

            // O(m)
            theater.book(numSeats, bestFitRowForBooking, reqId);

            List<String> seatIds = theater.getSeatIds(reqId);


            if (seatIds != null && !seatIds.isEmpty()) {
                output.append(reqId).append(" ");
                for (String sId : seatIds) {
                    output.append(sId).append(" ");
                }
            }
            output.append("\n");
            bookingRequest = buf.readLine();
        }
        writer.write(output.toString());
        writer.close();
    }
}
