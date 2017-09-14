package imd0412.parkinglot.calculator;

import imd0412.parkinglot.exception.DateFormatException;
import imd0412.parkinglot.exception.InvalidDateException;
import org.junit.Test;

import static imd0412.parkinglot.ParkingLotType.VIP;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class CalculatorPublicInterfaceTest {

    @Test
    public void testCalculatorPublicInterface() throws InvalidDateException, DateFormatException {
        Calculator calc = new Calculator();

        assertThat(calc.calcParkingCost("1970.01.01 00:00", "1970.01.01 00:00", VIP), is(0.00f));
    }
}
