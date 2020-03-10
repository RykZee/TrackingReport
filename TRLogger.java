import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TRLogger {
    private String filePath;
    private Date startDate;
    private Date endDate;

    private File readLog;
    private Scanner scan;

    private final int ROW = 10;
    private final int COL = 10;
    private String[][] table;
    private int urlPosition;

    HashMap<String, Integer> pageViews;

    public TRLogger(String filePath, Date startDate, Date endDate) {
        this.filePath = filePath;
        this.startDate = startDate;
        this.endDate = endDate;
        readLog = new File(filePath);

        urlPosition = 0;
        table = new String[ROW][COL];

        pageViews = new HashMap<>();
    }

    /**
     * Main worker method for this class, will scan the document in filepath and throw a FileNotFoundException if no file
     * was found. Continues to call helper methods until scanning is complete and output a resulting file.
     */
    public void run() {
        try {
            scan = new Scanner(readLog);
        } catch (FileNotFoundException e) {
            System.err.println("File not found in path " + filePath);
            System.exit(1);
        }

        // Remove headline while scanning
        scan.nextLine();
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:sszzz");

        while(scan.hasNextLine()) {
            String lines[] = scan.nextLine().split("\\|");
            String url = null;
            for(String s : lines) {
                if(s.matches("\\d+-\\d\\d-\\d\\d.*")) {
                    Date date = null;
                    try {
                        date = parser.parse(s);
                    } catch (ParseException e) {
                        System.err.println("Could not parse date");
                    }
                    if(startDate.after(date) || endDate.before(date)) {
                        break;
                    }
                } else if(s.matches("/.*\\.html.*")) {
                    url = s;
                    addUrl(s);
                } else if(s.matches("\\S.*")){
                    addUserId(url, s);
                }
            }
        }

        writeToFile();
    }

    /**
     * Final method to call when file has been read, will use the completed table to write to a file named "output.txt"
     * in the same location as the program
     */
    private void writeToFile() {
        try {
            FileWriter output = new FileWriter("output.txt");
            output.write("|url            |page views |visitors|\n");
            for(HashMap.Entry<String, Integer> hm : pageViews.entrySet()) {
                output.write("|" + hm.getKey() + " |" + hm.getValue() + "          |" + getVisitors(hm.getKey()) + "\n");
            }
            output.close();
        } catch (IOException e) {
            System.err.println("Could not output file");
            System.exit(1);
        }

        try {
            System.out.println("Output file successfully generated " + new File(".").getCanonicalPath() + "/output.txt");
        } catch (IOException e) {
            System.err.println("Could not get current file path");
        }
    }

    /**
     * Adds new URLs from String given, if the URL already exists page views will be increased by every method call.
     * @param url
     */
    private void addUrl(String url) {
        // Array needs to be bigger, doubling nr of columns
        if(urlPosition == table[0].length) {
            resize(table.length, table[0].length * 2);
        }

        // Already existing url, add one to nr of page views
        for(int i = 0; i < urlPosition; i++) {
            if(url.equals(table[0][i])) {
                pageViews.put(url, pageViews.get(url) + 1);
                return;
            }
        }

        // Completely new url, make entry in array and add one page view
        table[0][urlPosition] = url;
        pageViews.put(url, 1);
        urlPosition++;
    }

    /**
     * For every URL that exists, we will add the userId to the table even if the userId already has visited before
     * will however write error messages if the url given is null or not found.
     * @param url
     * @param userId
     */
    private void addUserId(String url, String userId) {
        if(url == null) {
            System.err.println("Found null url for userId: " + userId);
            return;
        }

        int col = Integer.MAX_VALUE;
        for(int i = 0; i < urlPosition; i++) {
            if(table[0][i].equals(url)) {
                col = i;
                break;
            }
        }

        if(col == Integer.MAX_VALUE) {
            System.err.println("Could not find userId for " + url);
            return;
        }

        // Find first available row in the column that is vacant and write it there, if everything is full the table
        // will expand
        int row;
        for(int i = 1; true; i++) {
            if(i == table.length) {
                resize(table.length * 2, table[0].length);
            }

            if(table[i][col] == null) {
                row = i;
                break;
            }
        }

        table[row][col] = userId;
    }

    /**
     * Printing the array for debugging purposes
     */
    private void printArray() {
        for(int i = 0; i < table.length; i++) {
            for(int j = 0; j < table[i].length; j++) {
                System.out.print("[" + table[i][j] + "]");
            }
            System.out.println();
        }
        System.out.println("Array size: " + table.length + " x " + table[0].length);
    }

    /**
     * Private helper method that will calculate nr of unique visitors in the table for every given URL
     * @param url
     * @return
     */
    private int getVisitors(String url) {
        ArrayList<String> list = new ArrayList<>();

        int col = Integer.MAX_VALUE;
        for(int i = 0; i < urlPosition; i++) {
            if(table[0][i].equals(url)) {
                col = i;
                break;
            }
        }

        if(col == Integer.MAX_VALUE) {
            System.err.println("Could not find url for " + url);
            return -1;
        }

        for(int i = 1; i < table.length; i++) {
            if(table[i][col] == null) {
                break;
            } else if(!list.contains(table[i][0])) {
                list.add(table[i][0]);
            }
        }

        return list.size();
    }

    /**
     * Private helper method for resizing table when needed. Can expand in either direction or both directions given it's
     * a 2D table.
     * @param rows
     * @param cols
     */
    private void resize(int rows, int cols) {
        if(rows < 0 || cols < 0) {
            throw new IllegalArgumentException("Resizing must be positive");
        } else if(rows < table.length || cols < table[0].length) {
            throw new IllegalArgumentException("Rows must be equal or bigger than " + table.length + " and columns equal or bigger than " + table[0].length);
        }

        String[][] tempArray = new String[rows][cols];
        for(int i = 0; i < table.length; i++) {
            for(int j = 0; j < table[0].length; j++) {
                tempArray[i][j] = table[i][j];
            }
        }
        table = tempArray;
    }
}
