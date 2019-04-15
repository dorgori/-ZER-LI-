package client;

import java.io.Serializable;

/**
 * Describes the Customer in Store class, extends Store
 * 
 * @param refund
 *            refund amount of customer in store
 * @param customerName
 *            ID of the customer
 *
 */
@SuppressWarnings("serial")
public class CustomerinStore extends Store implements Serializable {

	private float refund;
	private String customerName;

	public CustomerinStore(String s_ID, String s_name) {
		super(s_ID, s_name);
	}

	public CustomerinStore(String s_ID, float f) {
		super(s_ID);
		refund = f;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public float getRefund() {
		return refund;
	}

	public void setRefund(float refund) {
		this.refund = refund;
	}

}
