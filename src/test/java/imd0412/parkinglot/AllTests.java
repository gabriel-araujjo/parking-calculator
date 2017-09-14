package imd0412.parkinglot;

import imd0412.parkinglot.calculator.CalculatorExceptionsTest;
import imd0412.parkinglot.calculator.CalculatorPublicInterfaceTest;
import imd0412.parkinglot.calculator.CalculatorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
        CalculatorTest.class,
        CalculatorExceptionsTest.class,
        CalculatorPublicInterfaceTest.class,
        MainTest.class
})
@RunWith(Suite.class)
public class AllTests {
}
