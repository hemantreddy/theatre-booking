# Movie Theatre Seating

This algorithm attempts to fulfil the reservation requests, taking into account the size and buffer of seats required 
between each booking.

### Assumptions

* Rows are labelled from A - J and, and screen is closest to the A row. 
* If the system gets a request where the number of seats requested is greater than any contiguous available seats, the 
request is denied, and we move on to the next request. 
* Best seats are defined by the distance between screen and seats. Higher the distance, better the seats. 

### Approach
* Always fill from the J left corner, and go towards right for seats and bottom for rows, making 
sure you utilise all the seats properly. 
* Every row class has the ``maxiumContinuousAvailable`` count and the ``firstAvailableSeat`` pointer at which that count starts. This helps
us evaluate which row would work out best for which seat

### Steps to run the project
To compile the project, open your terminal and 
go to the root directory of the project, and run the following command

```
mvn clean compile assembly:single
```

And to execute the driver code
```
java -jar target/movieTheater-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### Sample input, this can be placed in ``input.txt`` file.
```
R001 2
R002 4
R003 4
R004 3
```
``ROOX`` corresponds to request ID and number after space corresponds to the number of seats requested. 

### Further Improvements
* Cancellations
    * Methods to handle the complete and partial reservations. 
* Ability to handle multiple movies on the same screen  in a single day