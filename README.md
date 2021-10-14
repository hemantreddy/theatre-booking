# Movie Theatre Seating

This algorithm attempts to fulfil the reservation requests, taking into account the size and buffer required 
between each booking.

## Steps to run the project
To compile the project, open your terminal and 
go to the root directory of the project, and run the following command

```
mvn clean compile assembly:single
```

And to build the jar file
```
java -jar target/movieTheater-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### Sample input, this can be placed in `input.txt` file.
```
R001 2
R002 4
R003 4
R004 3
```