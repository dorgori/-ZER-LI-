package unittest;

import order.Order;
import order.Order.deliveryType;
import order.Order.orderStatus;

public class StubOrder implements IOrder {
	Order o;
	public StubOrder() {}
	public Order getOrderFromDB(int orderID, String cutomerID, String storeID,int hours, orderStatus stat) {
		String dateNow = handleAddDate(hours);
		String timeNow = handleAddTime(hours);
		Order o = new Order(orderID, storeID, cutomerID, "2018-01-29", "hello", deliveryType.pickup, "", "", dateNow, timeNow, 236,
				stat, 0, "012", 0);
		return o;
	}

}
