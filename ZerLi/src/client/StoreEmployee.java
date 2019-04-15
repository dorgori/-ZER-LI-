package client;

import java.io.Serializable;
import java.util.ArrayList;

import order.Order;

/**
 * Describes the store employee Class, extends User
 * 
 * @param storeOrders
 *            orders of the store that are in progress
 * @param s_ID
 *            the store id the store employee belongs to
 */
@SuppressWarnings("serial")
public class StoreEmployee extends User implements Serializable {
	
	private ArrayList<Order> storeOrders;
	private String s_ID;

	public StoreEmployee(String Name, String Pass) {
		super(Name, Pass);
		storeOrders = new ArrayList<Order>();
	}

	public String getS_ID() {
		return s_ID;
	}

	public void setS_ID(String s_ID) {
		this.s_ID = s_ID;
	}

	public ArrayList<Order> getStoreOrders() {
		return storeOrders;
	}

	public void setStoreOrders(ArrayList<Order> storeOrders) {
		this.storeOrders = storeOrders;
	}
}
