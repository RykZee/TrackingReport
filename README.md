# TrackingReport

Reads a log file for a website with certain formatting and outputs an output.txt file which contain each URLs data on nr of visitors and page views on the site.

## Formatting of log file
The log file should be formatted as this with | characters used as delimiters and timestamp being the leftmost piece of data.

|timestamp              |url           |userid|\
|2013-09-01 09:00:00UTC |/contact.html |12345 |
|2013-09-01 09:00:00UTC |/contact.html |12346 |
|2013-09-01 10:00:00UTC |/contact.html |12345 |
|2013-09-01 11:00:00UTC |/contact.html |12347 |

### Prerequisites

Only Java SDK 8 is required

### Installing and running

The program only needs to be compiled with the Java CLI compiler on the main class which is TRMain.java
```
javac TRMain.java
```

To run the application, three arguments need to be provided.
```
java TRMain <Filepath to log> <start date> <end date>
```
The filepath needs to be in absolute filepath and end with the filename with .txt extension, for instance
```
/usr/local/trackingreport.txt
```
If the filepath contains a directory with a space then the filename should be in quotes. The filepath can also simply be the filename if the log file is in the current directory.

Start and end date should have the formatting of
```
yyy-mm-dd HH:mm:ss
```
The second argument can simply be the hours, minutes and seconds if you want later the same day.

## Running the tests

Automated unit tests are compiled and run on the test class which is TestTR.java
```
javac TestTR.java
java TestTR
```
