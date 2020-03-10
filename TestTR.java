import java.io.IOException;
import java.text.ParseException;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Date;

public class TestTR {
    public static void main(String[] args) {
        RunTests tests = new RunTests();
        tests.run();
    }
}

class RunTests {
    public RunTests() {

    }

    public void run() {
        testMain();
        testNullFilePath();
        testNoTextDocFilePath();
        testNullDates();
        testInvalidStartDate();
        testEndDateEarlierThanStart();

        System.out.println("All tests ran correctly if there were no error messages received");
    }

    private void testMain() {
        String[] args = {"/Users/sebastian/Desktop/Tracking Report/trackingreport.txt", "2013-09-01 09:00:00", "10:59:59"};
        TRMain.main(args);
    }

    private void testNullFilePath() {
        String[] args = {null, "2013-09-01 09:00:00", "10:59:59"};
        TRArgumentExtractor argExtractor = new TRArgumentExtractor(args);
        boolean result = argExtractor.extract();

        if(result == true) {
            System.err.println("Unexpected value in testNullFilePath method");
        }
    }

    private void testNoTextDocFilePath() {
        String[] args = {"/Users/sebastian/Desktop/Tracking Report/", "2013-09-01 09:00:00", "10:59:59"};
        TRArgumentExtractor argExtractor = new TRArgumentExtractor(args);
        try {
            argExtractor.extractFilePath();
        } catch (IOException e) {
            // Correct behaviour
            return;
        }

        System.err.println("Unexpected value in testNoTextDocFilePath method");
    }

    private void testNullDates() {
        String[] args = {"/Users/sebastian/Desktop/Tracking Report/trackingreport.txt", null, "10:59:59"};
        TRArgumentExtractor argExtractor = new TRArgumentExtractor(args);
        ArrayList<Date> result;
        try {
            result = argExtractor.extractDates();
            if(result != null) {
                System.err.println("Null value expected in testNullDates method, received not null");
            }
        } catch (Exception e) {
            System.err.println("Unexpected value in testNullDates method");
        }

        String[] args2 = {"/Users/sebastian/Desktop/Tracking Report/trackingreport.txt", "2013-09-01 09:00:00", null};
        argExtractor = new TRArgumentExtractor(args2);
        try {
            result = argExtractor.extractDates();
            if(result != null) {
                System.err.println("Null value expected in testNullDates method, received not null");
            }
        } catch (Exception e) {
            System.err.println("Unexpected value in testNullDates method");
        }
    }

    private void testInvalidStartDate() {
        String[] args = {"/Users/sebastian/Desktop/Tracking Report/trackingreport.txt", "2013/09/01", "10:59:59"};
        TRArgumentExtractor argExtractor = new TRArgumentExtractor(args);
        try {
            argExtractor.extractDates();
        } catch (ParseException e) {
            // Correct behaviour
            return;
        }

        System.err.println("Unexpected value in testInvalidStartDate method");
    }

    private void testEndDateEarlierThanStart() {
        String[] args = {"/Users/sebastian/Desktop/Tracking Report/trackingreport.txt", "2013-09-01 09:00:00", "2013-09-01 08:00:00"};
        TRArgumentExtractor argExtractor = new TRArgumentExtractor(args);
        try {
            argExtractor.extractDates();
        } catch (ParseException e) {
            System.err.println("Unexpected value in testEndDateEarlierThanStart method");
        } catch (DateTimeException e) {
            // Correct behaviour
            return;
        }

        System.err.println("Unexpected value in testEndDateEarlierThanStart method");
    }

    // TODO: Test TRLogger methods
}