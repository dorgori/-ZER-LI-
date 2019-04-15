package reports;

import java.io.Serializable;
/**
 *  a calss for each item in the order report
 * @author sagi arieli
 *
 */
public class ItemInOrderReport implements Serializable {

	private String type;
	private int orderCount;
	private float profit;
	public ItemInOrderReport(String type,  int orderCount, float profit) {
		super();
		this.type = type;
		this.orderCount = orderCount;
		this.profit = profit;

	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public int getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}
	public float getProfit() {
		return profit;
	}
	public void setProfit(float profit) {
		this.profit = profit;
	}

	@Override
	public String toString() {
		return "ItemInOrderReport [type=" + type + ", orderCount=" + orderCount + ", profit=" + profit
				+ "]";
	}
	
	
}
