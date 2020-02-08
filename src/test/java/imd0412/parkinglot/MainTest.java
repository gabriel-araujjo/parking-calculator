package imd0412.parkinglot;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ParserProperties;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


@RunWith(Parameterized.class)
public class MainTest {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> parameters() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return Arrays.asList(new Object[][]{
                // args, stdout, stderr, exit code
                {"[NO ARGS]", new String[]{}, "", usage(), 1},
                {
                   "-t VIP \"2017.08.20 20:10\"",
                   new String[]{"-t", "VIP", "2017.08.20 20:10"},
                   "", usage(), 1
                },
                {
                    "-t VIP \"2017.08.20 20:10\" \"2017.09.01 10:10\"",
                    new String[]{"-t", "VIP", "2017.08.20 20:10", "2017.09.01 10:10"},
                    "Parking total price = 1000.00 BRL\n", "", 0
                },
                {
                        "-t vip \"2017.08.20 20:10\" \"2017.09.01 10:10\"",
                        new String[]{"-t", "vip", "2017.08.20 20:10", "2017.09.01 10:10"},
                        "Parking total price = 1000.00 BRL\n", "", 0
                },
                {
                        "-t vip \"2000.08.20 20:10\" \"2000.09.01 10:10\"",
                        new String[]{"-t", "vip", "2000.08.20 20:10", "2000.09.01 10:10"},
                        "Parking total price = 1000.00 BRL\n", "", 0
                },
                {
                        "-t vip \"2000.02.30 20:10\" \"2000.09.01 10:10\"",
                        new String[]{"-t", "vip", "2000.02.30 20:10", "2000.09.01 10:10"},
                        "", "2000.2.30 does not exists\n\n"+usage(), 1
                },
                {
                        "-t longterm \"2000.01.16 20:00\" \"2000.02.15 20:01\"",
                        new String[]{"-t", "longterm", "2000.01.16 20:00", "2000.02.15 20:01"},
                        "Parking total price = 1590.00 BRL\n", "", 0
                },
                {
                        "-t vip \"2004.08.20 20:10\" \"2004.09.01 10:10\"",
                        new String[]{"-t", "vip", "2004.08.20 20:10", "2004.09.01 10:10"},
                        "Parking total price = 1000.00 BRL\n", "", 0
                },
                {
                        "-t vip \"1900.08.20 20:10\" \"1900.09.01 10:10\"",
                        new String[]{"-t", "vip", "1900.08.20 20:10", "1900.09.01 10:10"},
                        "", "Check in year (1900) out of valid range [1970, 2017].\n\n"+usage(),
                        1
                },
                {
                        "-t vip \"1900.00.20 20:10\" \"1900.09.01 10:10\"",
                        new String[]{"-t", "vip", "1900.00.20 20:10", "1900.09.01 10:10"},
                        "", "Month 0 out of range\n\n"+usage(),
                        1
                },
                {
                        "-t LongTerm \"1998.01.16 20:00\" \"1998.02.15 20:01\"",
                        new String[]{"-t", "LongTerm", "1998.01.16 20:00", "1998.02.15 20:01"},
                        "Parking total price = 1590.00 BRL\n", "", 0
                },
                {
                        "-t longterm \"1998.01.16 20:00\" \"1998.02.15 20:01\"",
                        new String[]{"-t", "longterm", "1998.01.16 20:00", "1998.02.15 20:01"},
                        "Parking total price = 1590.00 BRL\n", "", 0
                },
                {
                        "[expect exit 1]-t long-term \"1998.01.16 20:00\" \"1998.02.15 20:01\"",
                        new String[]{"-t", "long-term", "1998.01.16 20:00", "1998.02.15 20:01"},
                        "", usage(), 1
                },
                {
                        "-t ShortTerm \"1998.01.16 20:00\" \"1998.01.23 20:00\"",
                        new String[]{"-t", "ShortTerm", "1998.01.16 20:00", "1998.01.23 20:00"},
                        "Parking total price = 642.00 BRL\n", "", 0
                },
                {
                        "-t shortterm \"1998.01.16 20:00\" \"1998.01.23 20:00\"",
                        new String[]{"-t", "shortterm", "1998.01.16 20:00", "1998.01.23 20:00"},
                        "Parking total price = 642.00 BRL\n", "", 0
                },
                {
                        "-t shortterm \"1998.02.29 20:00\" \"1998.03.01 20:00\"",
                        new String[]{"-t", "shortterm", "1998.02.29 20:00", "1998.03.01 20:00"},
                        "", "1998.2.29 does not exists\n\n"+usage(), 1
                },
                {
                        "-t shortterm \"1998.02.34 20:00\" \"1998.03.01 20:00\"",
                        new String[]{"-t", "shortterm", "1998.02.34 20:00", "1998.03.01 20:00"},
                        "", "Day 34 out of valid range\n\n"+usage(), 1
                },
                {
                        "-t shortterm \"1998.02.28 20:00\" \"1998-03-01 20:00\"",
                        new String[]{"-t", "shortterm", "1998.02.28 20:00", "1998-03-01 20:00"},
                        "", "Check out date is not in a valid format\n\n"+usage(), 1
                },
                {
                        "-t shortterm \"1998-02-28 20:00\" \"1998-03-01 20:00\"",
                        new String[]{"-t", "shortterm", "1998-02-28 20:00", "1998-03-01 20:00"},
                        "", "Check in date is not in the valid format\nCheck out date is not in a valid format\n\n"+usage(), 1
                },
        });
    }

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Parameter(/*0*/)
    public String testName;

    @Parameter(1)
    public String[] args;

    @Parameter(2)
    public String expectedOut;

    @Parameter(3)
    public String expectedErr;

    @Parameter(4)
    public int expectedExitCode;


    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void checkAndCleanUpStreams() {
        assertThat(new String(errContent.toByteArray()), is(expectedErr));
        assertThat(new String(outContent.toByteArray()), is(expectedOut));
        System.setErr(null);
        System.setOut(null);
    }

    @Test
    public void main() throws Exception {
        exit.expectSystemExitWithStatus(expectedExitCode);
        Main.main(args);
        // If main exit normally, but not call exit(0), that is not an error
        // Because this should happens inplicitly.
        // However exit takes the absent of a System.exit call as an error.
        // To fix that, we call a System.exit(0) if main nether threw any
        // exception nor call System.exit 
        System.exit(0);
    }

    private static String usage() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Main main = new Main();

        ParserProperties props = ParserProperties.defaults().withUsageWidth(200);
        CmdLineParser parser = new CmdLineParser(main, props);

        Method method = Main.class.getDeclaredMethod("usage", CmdLineParser.class);
        method.setAccessible(true);

        return (String) method.invoke(main, parser);
    }
}
