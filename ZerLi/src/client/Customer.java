package client;

import java.io.Serializable;
import java.util.ArrayList;

import order.Order;

/**
 * Describes the Customer class
 * 
 * @param storeID
 *            the stores IDs that a customer belongs to
 * @param memberType
 *            type of member-ship the customer has in a given store
 * @param createdDate
 *            the date when the membership was created. no membership=0
 * @param creditNumber
 *            credit number of a customer
 * @param the
 *            amount of refund, a customer have in a store
 * @param customerOrders
 *            orders of the customer
 */
@SuppressWarnings("serial")
public class Customer extends User implements Serializable {

	private String storeID;

	public enum memberType {
		regular, monthlyMember, yearlyMember
	};

	private memberType memberOption;
	private String createdDate;
	private String creditNumber;
	private float refundAmount;
	private ArrayList<Order> customerOrders;

	public Customer(String name, String pass) {
		super(name, pass);
		customerOrders = new ArrayList<Order>();
		refundAmount = 0;
	}

	public String getStoreID() {
		return storeID;
	}

	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}

	public float getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(float refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getCreditNumber() {
		return creditNumber;
	}

	public void setCreditNumber(String creditNumber) {
		this.creditNumber = creditNumber;
	}

	public ArrayList<Order> getCustomerOrders() {
		return customerOrders;
	}

	public void setCustomerOrders(ArrayList<Order> customerOrders) {
		this.customerOrders = customerOrders;
	}

	public memberType getMemberOption() {
		return memberOption;
	}

	public void setMemberOption(memberType memberOption) {
		this.memberOption = memberOption;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public memberType intToMemType(int i) {
		switch (i) {
		case 0:
			return memberType.regular;
		case 1:
			return memberType.monthlyMember;
		case 2:
			return memberType.yearlyMember;
		}
		return memberType.regular;
	}

	public int MemTypetoInt(memberType i) {
		switch (i) {
		case regular:
			return 0;
		case monthlyMember:
			return 1;
		case yearlyMember:
			return 2;
		}
		return 0;
	}

}
