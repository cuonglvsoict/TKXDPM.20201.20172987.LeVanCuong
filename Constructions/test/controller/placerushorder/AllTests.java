package controller.placerushorder;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ValidateDeliveryTimeTest.class, ValidateNameTest.class, ValidateAddressTest.class,
		ValidatePhoneNumberTest.class })
public class AllTests {

}
