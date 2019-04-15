package order;

import java.io.Serializable;
import java.util.ArrayList;

import catalog.Item;
import catalog.Product;

/**
 * this class describes an order
 * 
 * @param deliveryType
 *            shows if customer chose self pickup or a delivery for an order
 * @param orderStatus
 *            the status of the order: ORDERED=not completed yet. PICKED_UP=
 *            order completed. CANCELLED= order cancelled
 * @param paymentType
 *            0=cash 1=credit card 2=refund
 *
 */
@SuppressWarnings("serial")
public class Order implements Serializable {
	private int orderID;
	private String storeID;
	private String customerID;
	private String orderDate = "";
	private ArrayList<Product> orderedProducts;
	private ArrayList<SelfMadeProduct> selfMadeProducts;
	private ArrayList<Item> items;
	private String greetingCard;

	public enum deliveryType {
		pickup, delivery
	};

	private deliveryType deliveryOption;
	private String stringOfDeliveryOp;
	private String deliveryRecipient;
	private String deliveryAddress;
	private String requirementDate = "";
	private String requirementTime = "";
	private float price;

	public enum orderStatus {
		ordered, picked_up, cancelled
	}

	private orderStatus orderStatusOption;
	private String stringOfOrderStat;
	private int selfMadeCounter = 0;
	private float refund = 0;
	private String tel = "";
	private int paymentType;

	public Order() {
		orderID = -1;
		orderedProducts = new ArrayList<Product>();
		deliveryRecipient = null;
		deliveryAddress = null;
		selfMadeProducts = new ArrayList<SelfMadeProduct>();
		items = new ArrayList<Item>();
		deliveryRecipient = null;
		deliveryAddress = null;
		paymentType = 0;
	}

	public Order(int _orderID, String _storeID, String _customerID, String _orderDate, String _greetingCard,
			deliveryType _deliveryOption, String _deliveryRecipient, String _deliveryAddress, String _deliveryDate,
			String _deliveryTime, float _price, orderStatus _orderStatusOption, float ref, String telephone,
			int payType) {
		orderID = _orderID;
		storeID = _storeID;
		customerID = _customerID;
		orderDate = _orderDate;
		orderedProducts = new ArrayList<Product>();
		greetingCard = _greetingCard;
		deliveryOption = _deliveryOption;
		deliveryRecipient = _deliveryRecipient;
		deliveryAddress = _deliveryAddress;
		requirementDate = _deliveryDate;
		requirementTime = _deliveryTime;
		price = _price;
		orderStatusOption = _orderStatusOption;
		refund = ref;
		tel = telephone;
		paymentType = payType;
	}

	public Order(int orderID, String orderDate, deliveryType deliveryOption, String deliveryDate, float price,
			orderStatus orderStat) {
		this.orderID = orderID;
		this.orderDate = orderDate;
		this.deliveryOption = deliveryOption;
		this.requirementDate = deliveryDate;
		this.price = price;
		this.orderStatusOption = orderStat;
		deliveryRecipient = null;
		deliveryAddress = null;
	}
	// check

	public Order(int orderID, String orderDate, deliveryType deliveryOption, float price,
			orderStatus orderStatusOption) {
		this.orderID = orderID;
		this.orderDate = orderDate;
		this.deliveryOption = deliveryOption;
		this.price = price;
		this.orderStatusOption = orderStatusOption;
		deliveryRecipient = null;
		deliveryAddress = null;
	}

	public String getGreetingCard() {
		return greetingCard;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public String getStoreID() {
		return storeID;
	}

	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public void setGreetingCard(String greetingCard) {
		this.greetingCard = greetingCard;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public deliveryType getDeliveryOption() {
		return deliveryOption;
	}

	public void setDeliveryOption(deliveryType deliveryOption) {
		this.deliveryOption = deliveryOption;
	}

	public String getRequirementDate() {
		return requirementDate;
	}

	public void setRequirementDate(String deliveryDate) {
		this.requirementDate = deliveryDate;
	}

	public String getRequirementTime() {
		return requirementTime;
	}

	public void setRequirementTime(String deliveryTime) {
		this.requirementTime = deliveryTime;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public orderStatus getOrderStatusOption() {
		return orderStatusOption;
	}

	public void setOrderStatusOption(orderStatus orderStatusOption) {
		this.orderStatusOption = orderStatusOption;
	}

	public ArrayList<Product> getOrderedProducts() {
		return orderedProducts;
	}

	public void setOrderedProducts(ArrayList<Product> orderedProducts) {
		this.orderedProducts = orderedProducts;
	}

	public String getDeliveryRecipient() {
		return deliveryRecipient;
	}

	public void setDeliveryRecipient(String deliveryRecipient) {
		this.deliveryRecipient = deliveryRecipient;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public ArrayList<SelfMadeProduct> getSelfMadeProducts() {
		return selfMadeProducts;
	}

	public void setSelfMadeProducts(ArrayList<SelfMadeProduct> selfMadeProducts) {
		this.selfMadeProducts = selfMadeProducts;
	}

	public void addSelfMade(SelfMadeProduct selfMadeProduct) {
		selfMadeProducts.add(selfMadeProduct);

	}

	public void refreshItems() {
		items = new ArrayList<Item>();
		items.addAll(selfMadeProducts);
		items.addAll(orderedProducts);
	}

	public String getStringOfOrderStat() {
		return stringOfOrderStat;
	}

	public void setStringOfOrderStat(orderStatus orderStatusOption) {
		switch (orderStatusOption) {
		case ordered:
			stringOfOrderStat = "Ordered";
			break;
		case picked_up:
			stringOfOrderStat = "Picked up";
			break;
		case cancelled:
			stringOfOrderStat = "Cancelled";
			break;
		}
	}

	public int getSelfMadeCounter() {
		return selfMadeCounter;
	}

	public void setSelfMadeCounter(int selfMadeCounter) {
		this.selfMadeCounter = selfMadeCounter;
	}

	public String getStringOfDeliveryOp() {
		return stringOfDeliveryOp;
	}

	public void setStringOfDeliveryOp(deliveryType delivery) {
		switch (delivery) {
		case pickup:
			stringOfDeliveryOp = "Pick-Up";
			break;
		case delivery:
			stringOfDeliveryOp = "Delivery";
			break;
		}
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public float getRefund() {
		return refund;
	}

	public void setRefund(float refund) {
		this.refund = refund;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public int getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

}
