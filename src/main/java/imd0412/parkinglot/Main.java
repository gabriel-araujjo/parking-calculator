package imd0412.parkinglot;

import imd0412.parkinglot.calculator.Calculator;
import org.kohsuke.args4j.*;

import java.util.List;

public class Main {

    @Option(name="-t", aliases = {"--type"}, usage = "Type of parking lot", required = true)
    private ParkingLotType type;

    @Argument
    private List<String> arguments;

    private void usage(CmdLineParser parser) {
        System.err.println("USAGE:");
        System.err.println();
        System.err.println(" java -jar ParkingLotCalculator -t <ShortTerm|LongTerm|VIP> <checkin> <checkout>");
        System.err.println();
        System.err.println("ARGS:");
        System.err.println();
        parser.printUsage(System.err);
        System.err.println();
        System.err.println(" checkin and checkout are in format \"" + Constants.VALID_DATE_FORMAT + "\"");
        System.err.println();
        System.err.println("Ex.: java -jar ParkingLotCalculator -t VIP \"2017.08.20 20:10\" \"2017.09.01 10:10\"");
        System.exit(1);
    }

    private void doMain(String[] args){
        ParserProperties props = ParserProperties.defaults().withUsageWidth(200);
        CmdLineParser parser = new CmdLineParser(this, props);

        try {
            parser.parseArgument(args);
            if (arguments.size() < 2)
                throw new IllegalArgumentException("Check in and checkout args are necessary.");
            float price = new Calculator().calcParkingCost(arguments.get(0), arguments.get(1), type);
            System.out.format("Parking total price = %.2f BRL ", price);
        } catch (Exception e) {
            usage(parser);
        }


        System.out.println();
    }

	public static void main(String[] args) throws Exception {
		new Main().doMain(args);
	}
}
