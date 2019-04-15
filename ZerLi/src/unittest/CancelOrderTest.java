package unittest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.sun.java.swing.plaf.motif.resources.motif;

import client.Client;
import client.Customer;
import client.LoginController;
import order.Order;
import order.Order.deliveryType;
import order.Order.orderStatus;
import order.OrderController;

public class CancelOrderTest {
	float currRefund;
	Customer customer;
	static Client client;
	IOrder mo;

	

	@Before
	public void setUp() throws Exception {
		client = new Client("localhost", 5555);
		customer = client.sendCustomer(new Customer("c1", "123"));
		try {
			Client.setClient(client);
			LoginController.customer = customer;
			LoginController.client = client;
			OrderController.client = client;
			currRefund = customer.getRefundAmount();
			mo = new StubOrder();
		} catch (Exception e) {
			System.out.println("OK");
		}

	}

	@Test
	public void cancelOrderOver3HoursSucceed() throws Exception {

		currRefund = customer.getRefundAmount();
		Order o1 = mo.getOrderFromDB(1, "c1", "02", 4, orderStatus.ordered);
		OrderController.cancelOrder(o1);
		Order o2 = client.getOrder(o1);// real from DB
		int actualStatus = fromOrderStatusToInteger(o2.getOrderStatusOption());
		float expectedRefund = o1.getPrice();
		int expectedStatus = 2;// 2= canceled
		float expectedCusTotRefund = currRefund + expectedRefund;
		
		assertTrue(actualStatus == expectedStatus);
		assertTrue(expectedRefund == o2.getRefund());
		assertTrue(customer.getRefundAmount() == expectedCusTotRefund);

	}

	@Test
	public void cancelOrderLessThen1Hour() throws Exception {

		currRefund = customer.getRefundAmount();
		Order o1 = mo.getOrderFromDB(1, "c1", "02", 0, orderStatus.ordered);
		OrderController.cancelOrder(o1);
		Order o2 = client.getOrder(o1);
		int actualStatus = fromOrderStatusToInteger(o2.getOrderStatusOption());
		float expectedRefund = 0;//
		int expectedStatus = 2;// 2= canceled
		float expectedCusTotRefund = currRefund;
		assertTrue(actualStatus == expectedStatus);
		assertTrue(expectedRefund == o2.getRefund());
		assertTrue(customer.getRefundAmount() == expectedCusTotRefund);

	}

	@Test
	public void cancelOrder1To3Hours() throws Exception {
		currRefund = customer.getRefundAmount();
		Order o1 = mo.getOrderFromDB(1, "c1", "02", 2, orderStatus.ordered);
		OrderController.cancelOrder(o1);
		Order o2 = client.getOrder(o1);
		int actualStatus = fromOrderStatusToInteger(o2.getOrderStatusOption());
		float expectedRefund = o1.getPrice() / 2;//
		int expectedStatus = 2;// 2= canceled
		float expectedCusTotRefund = currRefund + expectedRefund;
		assertTrue(actualStatus == expectedStatus);
		assertTrue(expectedRefund == o2.getRefund());
		assertTrue(customer.getRefundAmount() == expectedCusTotRefund);
	}

	@Test
	public void cancelOrderAlreadyCanceled() throws Exception {
		currRefund = customer.getRefundAmount();
		Order o1 = mo.getOrderFromDB(1, "c1", "02", 4, orderStatus.cancelled);
		OrderController.cancelOrder(o1);
		Order o2 = client.getOrder(o1);
		int actualStatus = fromOrderStatusToInteger(o2.getOrderStatusOption());
		int expectedStatus = 2;// 2= canceled
		float expectedCusTotRefund = currRefund;
		assertTrue(actualStatus == expectedStatus);
		assertTrue(customer.getRefundAmount() == expectedCusTotRefund);
	}

	@Test
	public void cancelOrderAlreadyPickedUP() throws Exception {
		currRefund = customer.getRefundAmount();
		Order o1 = mo.getOrderFromDB(3, "c1", "02", 5, orderStatus.picked_up);
		OrderController.cancelOrder(o1);
		Order o2 = client.getOrder(o1);
		int actualStatus = fromOrderStatusToInteger(o2.getOrderStatusOption());
		int expectedStatus = 1;// 1= picked up
		float expectedCusTotRefund = currRefund;
		float expectedRefund = 0;//
		assertTrue(actualStatus == expectedStatus);
		assertTrue(expectedRefund == o2.getRefund());
		assertTrue(customer.getRefundAmount() == expectedCusTotRefund);
	}

	private int fromOrderStatusToInteger(Order.orderStatus oStatus) {
		switch (oStatus) {
		case ordered:
			return 0;
		case picked_up:
			return 1;
		case cancelled:
			return 2;
		}
		return 0;
	}
}
