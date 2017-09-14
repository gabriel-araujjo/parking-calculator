package imd0412.parkinglot.calculator;

import imd0412.parkinglot.ParkingLotType;
import imd0412.parkinglot.exception.DateFormatException;
import imd0412.parkinglot.exception.InvalidDateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static imd0412.parkinglot.ParkingLotType.LongTerm;
import static imd0412.parkinglot.ParkingLotType.ShortTerm;
import static imd0412.parkinglot.ParkingLotType.VIP;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class CalculatorTest
{

	@Parameters(name = "{0}")
	public static Collection<Object[]> parameters() {
		return Arrays.asList(new Object[][]{
		        // Testing data limits
                {"inferior limits", "1970.01.01 00:00", "1970.01.01 00:00", VIP, 0.00f}, // TC_017 - inferior limits
                {"superior limits", "2017.12.31 23:59", "2018.12.31 23:59", VIP, 28080.00f + 500.00f + 700.00f}, // TC_017 - superior limits

                // ShortTerm parking lot cases
                {"ShortTerm - nothing", "1998.01.16 20:00", "1998.01.16 20:00", ShortTerm, 0.00f}, // TC_001 - nothing
                {"ShortTerm - a minute", "1998.01.16 20:00", "1998.01.16 20:01", ShortTerm, 8.00f}, // TC_002 - a minute
                {"ShortTerm - an hour", "1998.01.16 20:00", "1998.01.16 21:00", ShortTerm, 8.00f}, // TC_002 - an hour
                {"ShortTerm - an hour and a minute", "1998.01.16 20:00", "1998.01.16 21:01", ShortTerm, 10.00f}, // TC_003 - an hour and a minute
                {"ShortTerm - a day", "1998.01.16 20:00", "1998.01.17 20:00", ShortTerm, 54.00f}, // TC_003 - a day
                {"ShortTerm - a day and a minute", "1998.01.16 20:00", "1998.01.17 20:01", ShortTerm, 106.00f}, // TC_004 - a day and a minute
                {"ShortTerm - seven days", "1998.01.16 20:00", "1998.01.23 20:00", ShortTerm, 642.00f}, // TC_004 - seven days
                {"ShortTerm - seven days and a minute", "1998.01.16 20:00", "1998.01.23 20:01", ShortTerm, 674.00f}, // TC_005 - seven days and a minute

                // LongTerm parking lot cases
                {"LongTerm - nothing", "1998.01.16 20:00", "1998.01.16 20:00", LongTerm, 0.00f}, // TC_006 - nothing
                {"LongTerm - one minute", "1998.01.16 20:00", "1998.01.16 20:01", LongTerm, 70.00f}, // TC_007 - one minute
                {"LongTerm - a day", "1998.01.16 20:00", "1998.01.17 20:00", LongTerm, 70.00f}, // TC_007 - a day
                {"LongTerm - a day and a minute", "1998.01.16 20:00", "1998.01.17 20:01", LongTerm, 120.00f}, // TC_008 - a day and a minute
                {"LongTerm - a week", "1998.01.16 20:00", "1998.01.23 20:00", LongTerm, 370.00f}, // TC_008 - a week
                {"LongTerm - a week and a minute", "1998.01.16 20:00", "1998.01.23 20:01", LongTerm, 400.00f}, // TC_009 - a week and a minute
                {"LongTerm - a month", "1998.01.16 20:00", "1998.02.15 20:00", LongTerm, 1060.00f}, // TC_009 - a month
                {"LongTerm - a month and a minute", "1998.01.16 20:00", "1998.02.15 20:01", LongTerm, 1590.00f}, // TC_010 - a month and a minute

                // VIP parking lot cases
                {"VIP - nothing", "1998.01.16 20:00", "1998.01.16 20:00", VIP, 0.00f}, // TC_011 - nothing
                {"VIP - a minute", "1998.01.16 20:00", "1998.01.16 20:01", VIP, 500.00f}, // TC_012 - a minute
                {"VIP - a week", "1998.01.16 20:00", "1998.01.23 20:00", VIP, 500.00f}, // TC_012 - a week
                {"VIP - a week and a minute", "1998.01.16 20:00", "1998.01.23 20:01", VIP, 600.00f}, // TC_013 - a week and a minute
                {"VIP - two weeks", "1998.01.16 20:00", "1998.01.30 20:00", VIP, 1200.00f}, // TC_013 - two weeks
                {"VIP - two weeks and a minute", "1998.01.16 20:00", "1998.01.30 20:01", VIP, 1280.00f}, // TC_014 - two weeks and a minute

        });
    }

    @Parameter
    public String message;

    @Parameter(1)
    public String checkIn;

    @Parameter(2)
    public String checkOut;

    @Parameter(3)
    public ParkingLotType parking;

    @Parameter(4)
    public float expectedPrice;

    private Calculator calculator;

    @Before
    public void setUp(){
        calculator = new Calculator();
    }

	@Test
	public void testCalcParkingCost() throws InvalidDateException, DateFormatException {
		assertThat(calculator.calculateParkingCost(checkIn, checkOut, parking), is(expectedPrice));
	}
}
