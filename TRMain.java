public class TRMain {
    public static void main(String[] args) {
        if(args.length < 3) {
            System.out.println("Usage: TrackingReportMain <file path> <\"beginning time range\"> <\"ending time range\"> ");
            System.exit(1);
        }

        TRArgumentExtractor extractor = new TRArgumentExtractor(args);
        if(!extractor.extract()) {
            System.err.println("Could not determine filepath or dates invalid");
            System.exit(1);
        }

        TRLogger logger = new TRLogger(extractor.getFilePath(), extractor.getStartDate(), extractor.getEndDate());
        logger.run();
    }
}