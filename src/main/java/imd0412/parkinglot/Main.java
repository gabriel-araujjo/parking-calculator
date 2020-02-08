package imd0412.parkinglot;

import imd0412.parkinglot.calculator.Calculator;
import imd0412.parkinglot.exception.DateFormatException;
import imd0412.parkinglot.exception.InvalidDateException;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.ParserProperties;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

class Main {

    @Option(name="-t", aliases = {"--type"}, usage = "Type of parking lot", required = true)
    private ParkingLotType type;

    @Argument
    private List<String> arguments;

    private String usage(CmdLineParser parser) {
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuffer);
        out.println("USAGE:");
        out.println();
        out.println(" java -jar ParkingLotCalculator -t <ShortTerm|LongTerm|VIP> <checkin> <checkout>");
        out.println();
        out.println("ARGS:");
        out.println();
        parser.printUsage(out);
        out.println();
        out.println(" checkin and checkout are in format \"" + Constants.VALID_DATE_FORMAT + "\"");
        out.println();
        out.println("Ex.: java -jar ParkingLotCalculator -t VIP \"2017.08.20 20:10\" \"2017.09.01 10:10\"");
        return new String(outBuffer.toByteArray());
    }

    private void doMain(String[] args){
        ParserProperties props = ParserProperties.defaults().withUsageWidth(200);
        CmdLineParser parser = new CmdLineParser(this, props);

        try {
            parser.parseArgument(args);
            if (arguments.size() < 2) {
                throw new IllegalArgumentException("Check in and checkout args are necessary.");
            }
            float price = new Calculator().calcParkingCost(arguments.get(0), arguments.get(1), type);
            System.out.format("Parking total price = %.2f BRL", price);
            System.out.println();
        } catch (InvalidDateException |DateFormatException e) {
            System.err.println(e.getMessage()+"\n");
            System.err.print(usage(parser));
            System.exit(1);
        } catch (Exception e) {
            System.err.print(usage(parser));
            System.exit(1);
        }
    }

	public static void main(String[] args) throws Exception {
		new Main().doMain(args);
	}
}
