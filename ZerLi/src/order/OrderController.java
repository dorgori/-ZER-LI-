package order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import client.Client;
import client.Customer;
import client.LoginController;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import order.Order.orderStatus;

/**
 * Order Controller
 * 
 * @param client
 *            the client
 * @param customer
 *            static instance of the customer
 * @param order
 *            static instance of an order
 * @param booly
 *            a boolean for methods
 */
public class OrderController {
	public static Client client;
	public static Customer customer;
	public static Order order;
	private static Boolean booly = true;

	/**
	 * get customer details and create and empty order for him
	 * 
	 * @param customer2
	 *            the customer that the client sends to this class
	 */
	public static void set_customer(Customer customer2) {
		customer = customer2;
		order = new Order();
	}

	/**
	 * get the orders of a customer
	 * 
	 * @param customerName
	 *            the username PK of the customer
	 * @param storeID
	 *            the id PK of the store
	 */
	public static void getCustomerOrders(String customerName, String storeID) {
		customer.setCustomerOrders(client.getCustomerOrders(customerName, storeID));

	}

	/**
	 * sends to client the details of the order purchased
	 */
	public static void sendCompletedOrder() {
		client.sendCompletedOrderToServer(order);
		order = new Order();
	}

	/**
	 * a method to change scenes between classes in order package
	 * 
	 * @param root
	 *            the fxml window to be opened
	 */
	public static void changeWindow(Pane root) {
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle(LoginController.stageTitle);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				client.user_exit();
			}
		});
	}

	/**
	 * get the client details from client class
	 * 
	 * @param c
	 *            client to set
	 */
	public static void setClient(Client c) {
		client = c;
	}

	/**
	 * add a self made product to order
	 * 
	 * @param selfMadeProduct
	 *            a self made product
	 */
	public static void addSelfMade(SelfMadeProduct selfMadeProduct) {
		order.addSelfMade(selfMadeProduct);

	}

	/**
	 * Canceling an order
	 * 
	 * @param o
	 *            an order chosen to be cancelled
	 * @throws ParseException
	 */
	public static void cancelOrder(Order o) throws ParseException {
		booly=true;
		if(o.getOrderStatusOption()!=orderStatus.ordered)
			return;
		String currentStringHour = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
		String currentStringDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		String deliverStringDate = o.getRequirementDate();
		String deliveryStringHour = o.getRequirementTime();
		Date deliveryDate = stringDateToDateConvert(deliverStringDate);
		Date currentDate = stringDateToDateConvert(currentStringDate);
		compareDates(deliveryDate, currentDate, o);
		String ratiohoursMinutes = getHoursinString(deliveryStringHour, currentStringHour);
		if (booly == true)
			refundVerdict(ratiohoursMinutes, o);
	}

	/**
	 * check the hours ratio to compensate the customer accordingly
	 * 
	 * @param durationhoursMinutes
	 *            the ratio of hours between canceling an order and requirement time
	 * @param o
	 *            the order chosen
	 */
	public static void refundVerdict(String durationhoursMinutes, Order o) {
		String durationhours = (durationhoursMinutes.split(":"))[0];
		int hours = Integer.parseInt(durationhours);
		if (hours >= 3) {
			LoginController.customer.setRefundAmount(LoginController.customer.getRefundAmount() + o.getPrice());
			o.setOrderStatusOption(orderStatus.cancelled);
			o.setRefund(o.getPrice());
			client.cancelOrder(o, 1);
		} else if (hours <= 0) {
			client.cancelOrder(o, 0);
			o.setOrderStatusOption(orderStatus.cancelled);
		} else if (hours == 1 || hours == 2) {
			LoginController.customer.setRefundAmount((float) (LoginController.customer.getRefundAmount() + o.getPrice() * 0.5));
			client.cancelOrder(o, (float) 0.5);
			o.setOrderStatusOption(orderStatus.cancelled);
			//o.setRefund((float) (LoginController.customer.getRefundAmount() + o.getPrice() * 0.5));
			o.setRefund((float) ( o.getPrice() * 0.5));

		}
	}

	/**
	 * get the hours between delivery time and current hour in String
	 * 
	 * @param deliveryHour
	 *            the delivery hour specified
	 * @param currentHour
	 *            this hour
	 * @return a string of hours
	 * @throws ParseException
	 */
	public static String getHoursinString(String deliveryHour, String currentHour) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		Date deliveryHourDate = format.parse(deliveryHour);
		Date currentHourDate = format.parse(currentHour);
		long millis = deliveryHourDate.getTime() - currentHourDate.getTime();
		String hourminute = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
		return hourminute;
	}

	/**
	 * convert a String object to Date object
	 * 
	 * @param stringDate
	 *            a String date
	 * @return a date of Date object
	 * @throws ParseException
	 */
	public static Date stringDateToDateConvert(String stringDate) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date date = format.parse(stringDate);
		return date;
	}

	/**
	 * 
	 * @param deliveryDate
	 *            delivery date
	 * @param currentDate
	 *            the current date
	 * @param o
	 *            the order
	 * @throws ParseException//
	 */
	public static void compareDates(Date deliveryDate, Date currentDate, Order o) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("date1 : " + sdf.format(deliveryDate));
		System.out.println("date2 : " + sdf.format(currentDate));

		Calendar deliveryDateReal = Calendar.getInstance();
		Calendar currentDateReal = Calendar.getInstance();
		deliveryDateReal.setTime(deliveryDate);
		currentDateReal.setTime(currentDate);

		if (deliveryDateReal.equals(currentDateReal)) {
			System.out.println("deliveryDate is after currentDate or equal");
			return;
		} else if (deliveryDateReal.after(currentDateReal)) {
			LoginController.customer.setRefundAmount(LoginController.customer.getRefundAmount() + o.getPrice());
			o.setOrderStatusOption(orderStatus.cancelled);
			o.setRefund(o.getPrice());
			client.cancelOrder(o, 1);
			booly = false;
		} else {
			System.out.println("deliveryDate is BEFORE currentDate");
			o.setOrderStatusOption(orderStatus.cancelled);
			client.cancelOrder(o, 0);
			booly = false;
		}
	}

	/**
	 * updates the customer refund after making a purchase (if needed)
	 * 
	 * @param storeID
	 *            the store id PK where the purchase was made
	 * @param customerID
	 *            the customer username PK who made the purchase
	 * @param refundAmount
	 *            the refund amount to be subtracted in the customer store
	 */
	public static void updateCustomerRefund(String storeID, String customerID, float refundAmount) {
		client.updateCustomerRefund(storeID, customerID, refundAmount);
	}

	/**
	 * get an array of orders of a store
	 * 
	 * @param storeID
	 *            the store id PK
	 * @return array of orders of a store
	 */
	public static ArrayList<Order> getStoreOrders(String storeID) {
		return client.getStoreOrders(storeID);
	}

	/**
	 * store employee mark order as completed
	 * 
	 * @param orderID
	 *            the order id PK
	 * @param storeID
	 *            the store id PK
	 */
	public static void orderHandlingCompleted(int orderID, String storeID) {
		client.orderHandlingCompleted(orderID, storeID);
	}

	/**
	 * get an order from the database
	 * 
	 * @param o
	 *            the order requested
	 * @return the order updated
	 */
	public static Order getOrder(Order o) {
		order = client.getOrder(o);
		return order;
	}
}