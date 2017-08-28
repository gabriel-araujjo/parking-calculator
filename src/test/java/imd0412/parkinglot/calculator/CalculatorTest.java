package imd0412.parkinglot.calculator;

import imd0412.parkinglot.ParkingLotType;
import imd0412.parkinglot.exception.DateFormatException;
import imd0412.parkinglot.exception.InvalidDataException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static imd0412.parkinglot.ParkingLotType.LongTerm;
import static imd0412.parkinglot.ParkingLotType.ShortTerm;
import static imd0412.parkinglot.ParkingLotType.VIP;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class CalculatorTest
{

	@Parameters
	public static Collection<Object[]> parameters() {
		return Arrays.asList(new Object[][]{
		        // Testing data limits
                {"1970.01.01 00:00", "1970.01.01 00:00", VIP, 0.00f}, // TC_000
                {"1970.01.01 00:00", "1970.01.01 00:01", VIP, 500.00f}, // TC_001
                {"2017.12.31 00:00", "2018.12.31 00:01", VIP, 29280.00f}, // TC_002
                // Testing ShortTerm parking lot
                {"1998.01.16 20:00", "1998.01.16 20:00", ShortTerm, 0.00f}, // TC_003 - nothing
                {"1998.01.16 20:00", "1998.01.16 20:01", ShortTerm, 8.00f}, // TC_004 - one minute
                {"1998.01.16 20:00", "1998.01.17 20:00", ShortTerm, 54.00f}, // TC_005 - a day
                {"1998.01.16 20:00", "1998.01.17 20:01", ShortTerm, 106.00f}, // TC_006 - a day and a minute
                {"1998.01.16 20:00", "1998.01.23 20:00", ShortTerm, 642.00f}, // TC_007 - a week
                {"1998.01.16 20:00", "1998.01.23 20:01", ShortTerm, 694.00f}, // TC_008 - a week and a minute
                {"1998.01.16 20:00", "1998.01.24 20:01", ShortTerm, 772.00f}, // TC_009 - a week, a day and a minute
                // Testing LongTerm parking lot
                {"1998.01.16 20:00", "1998.01.16 20:00", LongTerm, 0.00f}, // TC_010 - nothing
                {"1998.01.16 20:00", "1998.01.16 20:01", LongTerm, 70.00f}, // TC_011 - one minute

        });
    }

    @Parameter(0)
    public String checkIn;

    @Parameter(1)
    public String checkOut;

    @Parameter(2)
    public ParkingLotType parking;

    @Parameter(3)
    public float expectedPrice;

    public Calculator calculator;

    @Before
    public void setUp(){
        calculator = new Calculator();
    }

	@Test
	public void testCalcParkingCost() throws InvalidDataException, DateFormatException {
		assertThat(calculator.calculateParkingCost(checkIn, checkOut, parking), is(expectedPrice));
	}
}
