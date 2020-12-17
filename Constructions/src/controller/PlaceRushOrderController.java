package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;

interface DateValidator {
	   boolean isValid(String dateStr);
	}

class DateValidatorUsingDateFormat implements DateValidator {
    private String dateFormat;

    public DateValidatorUsingDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public boolean isValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

public class PlaceRushOrderController extends BaseController {

	/**
	 * logging to console
	 */
	private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

	/**
	 * checks the avalibility of product when user click PlaceOrder
	 * button
	 * 
	 * @throws SQLException
	 */
	public void placeRushOrder() throws SQLException {
		Cart.getCart().checkAvailabilityOfProduct();
	}

	/**
	 * This method creates the new Order based on the Cart
	 * 
	 * @return Order
	 * @throws SQLException
	 */
	public Order createOrder() throws SQLException {
		Order order = new Order();
		for (Object object : Cart.getCart().getListMedia()) {
			CartMedia cartMedia = (CartMedia) object;
			OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), cartMedia.getQuantity(), cartMedia.getPrice());
			order.getlstOrderMedia().add(orderMedia);
		}
		return order;
	}

	/**
	 * This method creates the new Invoice based on order
	 * 
	 * @param order
	 * @return Invoice
	 */
	public Invoice createInvoice(Order order) {
		return new Invoice(order);
	}

	/**
	 * This method takes responsibility for processing the shipping info from user
	 * 
	 * @param info
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException {
		LOGGER.info("Process Delivery Info");
		LOGGER.info(info.toString());
		validateDeliveryInfo(info);
	}

	/**
	 * The method validates the info
	 * 
	 * @param info
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException {
		
	}
	
	/**
	 * checks whether the rush order supports the address
	 * @param address: the address needs to check
	 * @return 
	 */
	public boolean checkAddressSuported(String address) {
		return false;
	}
	
	/**
	 * validate the place rush order delivery time
	 * @param value: the time of delivery (string)
	 * @return
	 */
	public boolean validateDeliveryTime(String deliveryTime) {
		DateValidator validator = new DateValidatorUsingDateFormat("MM/dd/yyyy");
		return validator.isValid(deliveryTime);
	}

	/**
	 * validate customer phone number
	 * @param phoneNumber
	 * @return
	 */
	public boolean validatePhoneNumber(String phoneNumber) {
		// TODO: your work
		if (phoneNumber.length() != 10) {
			return false;
		}

		if (!phoneNumber.startsWith("0")) {
			return false;
		}

		try {
			Integer.parseInt(phoneNumber);
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}

	/**
	 * validate customer name
	 * 
	 * @param name
	 * @return
	 */
	public boolean validateName(String name) {
		// TODO: your work
		if (name == null || name.equals(""))
			return false;
		for (char c : name.toCharArray()) {
			if (c != ' ' && (c < 65 || c > 122 || (90 < c && c < 97))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * validatea delivery address
	 * @param address
	 * @return
	 */
	public boolean validateAddress(String address) {
		if (address == null || address.equals(""))
			return false;
		for (char c : address.toCharArray()) {
			if (c != ' ' && (c < 48 || c > 122 || (57 < c && c < 65) || (90 < c && c < 97))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This method calculates the shipping fees of order
	 * 
	 * @param order
	 * @return shippingFee
	 */
	public int calculateShippingFee(Order order) {
		Random rand = new Random();
		int fees = (int) (((rand.nextFloat() * 10) / 100) * order.getAmount());
		LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
		return fees;
	}
}
