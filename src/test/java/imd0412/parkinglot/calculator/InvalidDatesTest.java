package imd0412.parkinglot.calculator;

import imd0412.parkinglot.exception.DateFormatException;
import imd0412.parkinglot.exception.InvalidDateException;
import imd0412.parkinglot.exception.InvalidDateType;
import org.hamcrest.CoreMatchers;
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
import static imd0412.parkinglot.exception.InvalidDateType.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class InvalidDatesTest {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Parameters(name = "{0}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][]{
                {"check in before then 1/1/1970", "1969.12.31 23:59", "2018.12.31 23:59", InvalidYear}, // TC_018 - check in before then 1/1/1970
                {"check out before then 1/1/1970", "1970.01.01 00:00", "1969.12.31 23:59", InvalidYear}, // TC_019 - check out before then 1/1/1970
                {"check in after then 12/31/2017", "2018.01.01 00:00", "2018.12.31 23:59", InvalidYear}, // TC_020 - check in after then 12/31/2017
                {"check out after then 12/31/2018", "1970.01.01 00:00", "2019.01.01 00:00", InvalidYear}, // TC_021 - check out after then 12/31/2018
                {"check in = 2001.02.29", "2001.02.29 00:00", "2018.12.31 23:59", NonexistentDate}, // TC_022 - Nonexistent check in
                {"check in = 2001.02.30", "2001.02.30 00:00", "2018.12.31 23:59", NonexistentDate}, // TC_022 - Nonexistent check in
                {"check in = 2001.02.31", "2001.02.31 00:00", "2018.12.31 23:59", NonexistentDate}, // TC_022 - Nonexistent check in
                {"check in = 2001.04.31", "2001.04.31 00:00", "2018.12.31 23:59", NonexistentDate}, // TC_022 - Nonexistent check in
                {"check in = 2001.06.31", "2001.06.31 00:00", "2018.12.31 23:59", NonexistentDate}, // TC_022 - Nonexistent check in
                {"check in = 2001.09.31", "2001.09.31 00:00", "2018.12.31 23:59", NonexistentDate}, // TC_022 - Nonexistent check in
                {"check in = 2001.11.31", "2001.11.31 00:00", "2018.12.31 23:59", NonexistentDate}, // TC_022 - Nonexistent check in
                {"check out = 2001.02.29", "1970.01.01 00:00", "1970.02.29 00:00", NonexistentDate}, // TC_023 - Nonexistent check out
                {"check out = 2001.02.30", "1970.01.01 00:00", "1970.02.30 00:00", NonexistentDate}, // TC_023 - Nonexistent check out
                {"check out = 2001.02.31", "1970.01.01 00:00", "1970.02.31 00:00", NonexistentDate}, // TC_023 - Nonexistent check out
                {"check out = 2001.04.31", "1970.01.01 00:00", "1970.04.31 00:00", NonexistentDate}, // TC_023 - Nonexistent check out
                {"check out = 2001.06.31", "1970.01.01 00:00", "1970.06.31 00:00", NonexistentDate}, // TC_023 - Nonexistent check out
                {"check out = 2001.09.31", "1970.01.01 00:00", "1970.09.31 00:00", NonexistentDate}, // TC_023 - Nonexistent check out
                {"check out = 2001.11.31", "1970.11.31 00:00", "1970.09.31 00:00", NonexistentDate}, // TC_023 - Nonexistent check out
                {"check in = 1970.13.31", "1970.13.31 00:00", "1970.09.31 00:00", InvalidMonth}, // TC_025 - Input invalid
                {"check in = 1970.12.00", "1970.12.00 00:00", "1970.09.31 00:00", InvalidDay}, // TC_025 - Input invalid
                {"check in = 1970.12.32", "1970.12.32 00:00", "1970.09.31 00:00", InvalidDay}, // TC_025 - Input invalid
                {"check in = 1970.12.31 24:00", "1970.12.31 24:00", "1970.09.31 00:00", NonexistentDate}, // TC_025 - Input invalid
                {"check in = 1970.12.31 23:60", "1970.12.31 23:60", "1970.09.31 00:00", NonexistentDate}, // TC_025 - Input invalid
                {"Check out before then check in", "2017.09.05 00:00", "2017.08.05 00:00", InvalidDay}, // TC_024 - Check out before then check in

        });
    }

    @Parameter
    public String message;

    @Parameter(1)
    public String checkIn;

    @Parameter(2)
    public String checkOut;

    @Parameter(3)
    public InvalidDateType reason;

    private Calculator calculator;

    @Before
    public void setUp(){
        calculator = new Calculator();
    }

    @Test
    public void testInvalidDate() throws InvalidDateException, DateFormatException {
        thrown.expect(InvalidDateException.class);
        InvalidDateException ex = null;
        try {
            calculator.calculateParkingCost(checkIn, checkOut, VIP);
        } catch (InvalidDateException e) {
            ex = e;
        }
        assertNotNull(ex);
        assertThat(ex.getReason(), CoreMatchers.is(reason));
        throw ex;
    }

}
