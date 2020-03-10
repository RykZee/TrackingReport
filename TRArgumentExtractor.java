import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Date;

public class TRArgumentExtractor {
    private String[] args;

    private String filePath;
    private Date startDate;
    private Date endDate;

    public TRArgumentExtractor(String[] args) {
        this.args = args;
    }

    public boolean extract() {
        try {
            filePath = extractFilePath();
            if(filePath == null) {
                return false;
            }
        } catch (IOException e) {
            System.err.println("File must have .txt extension with full filepath or in current filepath");
            System.exit(1);
        }

        try {
            ArrayList<Date> dateList = extractDates();
            if(dateList == null) {
                System.err.println("Start and end date must be entered as arguments");
                System.err.println("Usage: TrackingReportMain <file path> <\"beginning time range\"> <\"ending time range\"> ");
                System.exit(1);
            }
            startDate = dateList.get(0);
            endDate = dateList.get(1);
        } catch (ParseException e) {
            System.err.println("Date entered couldn't be parsed must be entered with yyy-MM-dd HH:mm:ss formatting");
            System.exit(1);
        } catch (DateTimeException e) {
            System.err.println("End date must be after start date");
            System.exit(1);
        }

        return true;
    }

    protected String extractFilePath() throws IOException {
        if(args[0] == null) {
            return null;
        }

        if(args[0].matches("/.*.txt")) { // Absolute filepath entered
            return args[0];
        } else if(args[0].matches(".*.txt")) { // filepath in current directory
            System.out.println("Found value: " + args[0]);
            return new File(".").getCanonicalPath() + "/" + args[0];
        }
        throw new IOException();
    }

    protected ArrayList<Date> extractDates() throws ParseException {
        if(args[1] == null || args[2] == null) {
            return null;
        }

        ArrayList<Date> list = new ArrayList<>();
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:sszzz");
        list.add(parser.parse(args[1] + "UTC"));

        // Matches HH:mm:ss (no date entered)
        if(args[2].matches("([0-9]|[0-2][0-9]):[0-5][0-9]:[0-5][0-9]")) {
            String endTime = args[1].substring(0, 10) + " " + args[2] + "UTC";
            list.add(parser.parse(endTime));
            return list;
        }
        list.add(parser.parse(args[2] + "UTC"));
        if(list.get(1).before(list.get(0))) {
            // Some error has occurred, end date is before start date
            throw new DateTimeException("End date is before start date");
        }
        return list;
    }

    public String getFilePath() {
        return filePath;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "TRArgumentExtractor{" +
                "filePath='" + filePath + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
