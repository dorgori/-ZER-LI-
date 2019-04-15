package unittest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import order.Order;
import order.Order.orderStatus;

public interface IOrder {
	public Order getOrderFromDB(int orderID,String cutomerID,String storeID,int hours, orderStatus stat);
	
	 default String handleAddDate(int hoursToAdd) {
		if (hoursToAdd == 0) {
			int min = 30;
			Calendar cal = Calendar.getInstance(); // creates calendar
			cal.setTime(new Date()); // sets calendar time/date
			cal.add(Calendar.MINUTE, min); // adds one hour
			String currentStringDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			return currentStringDate;
		} else {
			Calendar cal = Calendar.getInstance(); // creates calendar
			cal.setTime(new Date()); // sets calendar time/date
			cal.add(Calendar.HOUR_OF_DAY, hoursToAdd); // adds one hour
			String currentStringDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			return currentStringDate;
		}
	}

	default String handleAddTime(int hoursToAdd) {
		if (hoursToAdd == 0) {
			int min = 30;
			Calendar cal = Calendar.getInstance(); // creates calendar
			cal.setTime(new Date()); // sets calendar time/date
			cal.add(Calendar.MINUTE, min); // adds one hour
			String currentStringDate = new SimpleDateFormat("HH:mm").format(cal.getTime());
			return currentStringDate;
		} else {
			Calendar cal = Calendar.getInstance(); // creates calendar
			cal.setTime(new Date()); // sets calendar time/date
			cal.add(Calendar.HOUR_OF_DAY, hoursToAdd); // adds one hour
			String currentStringDate = new SimpleDateFormat("HH:mm").format(cal.getTime());
			return currentStringDate;
		}
	}
}
