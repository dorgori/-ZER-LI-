package catalog;

import java.io.Serializable;

/**
 * represents a discount of a single product in a single store dis = discount ,
 * by %.
 * 
 * @author sagi arieli
 *
 */
public class Discount implements Serializable {
	private String storeId;
	private String productID;
	private float dis;

	public Discount(String store_id, String product_id, float disc) {
		storeId = store_id;
		productID = product_id;
		dis = disc;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public float getDis() {
		return dis;
	}

	public void setDis(float dis) {
		this.dis = dis;
	}

	public String toString() {
		return (productID + " " + dis);
	}
}
