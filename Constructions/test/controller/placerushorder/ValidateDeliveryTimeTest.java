package controller.placerushorder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import controller.PlaceRushOrderController;

class ValidateDeliveryTimeTest {
	
	PlaceRushOrderController testInstance;

	@BeforeEach
	void setUp() throws Exception {
		testInstance = new PlaceRushOrderController();
	}

	@ParameterizedTest
	@CsvSource({
		"02/28/2019, true",
		"02/31/2019, false",
		"02/2a/2019, false",
		"2019/01/01, false"
	})
	void test(String deliveryTime, boolean expected) {
		boolean isValided = testInstance.validateDeliveryTime(deliveryTime);
		assertEquals(expected, isValided);
	}
}
