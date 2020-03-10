import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
        //testMain();
        testNullFilePath();
        testNoTextDocFilePath();
        testNullDates();
        testInvalidStartDate();
        testEndDateEarlierThanStart();
        testAddUrl();
        testAddUserId();
        testGetVisitors();

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

    private void testAddUrl() {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:sszzz");
        Date start = null, end = null;
        try {
            start = parser.parse("2013-09-01 09:00:00UTC");
            end = parser.parse("2013-09-01 10:59:59UTC");
        } catch (ParseException e) {
            System.err.println("Parse error in testAddUrl");
        }

        TRLogger logger = new TRLogger("/Users/sebastian/Desktop/Tracking Report/trackingreport.txt", start, end);
        String url1 = "/contact.html";
        String url2 = "/index.html";

        logger.addUrl(url1);
        logger.addUrl(url1);
        logger.addUrl(url2);

        HashMap<String, Integer> hm = logger.getPageViews();
        if(hm.get(url1) != 2 || hm.get(url2) != 1) {
            System.err.println("Unexpected value in testAddUrl method");
        }
    }

    private void testAddUserId() {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:sszzz");
        Date start = null, end = null;
        try {
            start = parser.parse("2013-09-01 09:00:00UTC");
            end = parser.parse("2013-09-01 10:59:59UTC");
        } catch (ParseException e) {
            System.err.println("Parse error in testAddUrl");
        }

        TRLogger logger = new TRLogger("/Users/sebastian/Desktop/Tracking Report/trackingreport.txt", start, end);
        String url1 = "/contact.html";
        String url2 = "/index.html";
        String userId1 = "Tony Stark";
        String userId2 = "Steve Rogers";

        logger.addUrl(url1);
        logger.addUserId(url1, userId1);
        logger.addUrl(url2);
        logger.addUserId(url2, userId2);

        String[][] table = logger.getTable();

        if(!table[0][0].equals(url1) || !table[1][0].equals(userId1)) {
            System.err.println("Unexpected value in testAddUserId method");
        }

        if(!table[0][1].equals(url2) || !table[1][1].equals(userId2)) {
            System.err.println("Unexpected value in testAddUserId method");
        }
    }

    private void testGetVisitors() {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:sszzz");
        Date start = null, end = null;
        try {
            start = parser.parse("2013-09-01 09:00:00UTC");
            end = parser.parse("2013-09-01 10:59:59UTC");
        } catch (ParseException e) {
            System.err.println("Parse error in testAddUrl");
        }

        TRLogger logger = new TRLogger("/Users/sebastian/Desktop/Tracking Report/trackingreport.txt", start, end);
        String url1 = "/contact.html";
        String url2 = "/index.html";
        String userId1 = "Tony Stark";
        String userId2 = "Steve Rogers";

        logger.addUrl(url1);
        logger.addUserId(url1, userId1);
        logger.addUserId(url1, userId1);
        logger.addUserId(url1, userId2);
        logger.addUrl(url2);
        logger.addUserId(url2, userId2);

        if(logger.getVisitors(url1) != 2 || logger.getVisitors(url2) != 1) {
            System.err.println("Unexpected value in testGetVisitors method");
        }
    }
}