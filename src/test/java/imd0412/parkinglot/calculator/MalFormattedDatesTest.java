package imd0412.parkinglot.calculator;

import imd0412.parkinglot.exception.DateFormatException;
import imd0412.parkinglot.exception.InvalidDateException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static imd0412.parkinglot.ParkingLotType.VIP;

@RunWith(Parameterized.class)
public class MalFormattedDatesTest {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();


    @Parameters(name = "{0}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][]{
                {"1969/12/31 23:59"},
                {"Feb 10"},
                {"1969.127.31"},
                {"1969.127."},
                {"1970.01.32"},
                {"1970.13.01"},
                {"1970.12.00"}
        });
    }

    @Parameter
    public String date;

    private Calculator calculator;

    @Before
    public void setUp() {
        calculator = new Calculator();
    }

    /* TC_015 */
    @Test
    public void testMalFormattedCheckIn() throws InvalidDateException, DateFormatException {
        thrown.expect(DateFormatException.class);
        calculator.calculateParkingCost(date, "2018.12.31 23:59", VIP);
    }

    /* TC_016 */
    @Test
    public void testMalFormattedCheckOut() throws InvalidDateException, DateFormatException {
        thrown.expect(DateFormatException.class);
        calculator.calculateParkingCost("1970.01.01 00:00", date, VIP);
    }

    @Test
    public void testMalFormattedCheckInAndOut() throws InvalidDateException, DateFormatException {
        thrown.expect(DateFormatException.class);
        calculator.calculateParkingCost(date, date, VIP);
    }
}
