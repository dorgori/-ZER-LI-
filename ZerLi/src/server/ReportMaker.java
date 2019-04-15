package server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimerTask;

import client.Store;

/**
 * 
 * This class responsible to create reports on each quarter
 * It will occur every day and check if a quarter is passed and if it does create reports
 *
 */
public class ReportMaker extends TimerTask {

	/**
	 * this method will be activated every day, and will first of all check if the
	 * current date is after the next quarter date, if so - 
	 * 1.its will set the next date to commit reports in (next quaret date)
	 * 2.it will make all the quarter reports for each store
	 * 
	 */
	@Override
	public void run() {
		try {
			String currentStringDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			Date currentDate1 = format.parse(currentStringDate);
			// Server.reportTimer.cancel();
			Date nextquadate = format.parse(Server.nextquarterdate);
			Calendar nextQuaDate = Calendar.getInstance();
			Calendar currentDate = Calendar.getInstance();
			nextQuaDate.setTime(nextquadate);
			currentDate.setTime(currentDate1);

			if (nextQuaDate.before(currentDate)) {
				int year = Integer.parseInt(Server.nextquarterdate.split("-")[0]);
				String q = null;
				if (Server.nextquarterdate.split("-")[1].equals("10")) {
					q = "Q3";
					Server.nextquarterdate = "" + (Integer.parseInt(Server.nextquarterdate.split("-")[0]) + 1)
							+ "-01-01";
				} else {
					if (Server.nextquarterdate.split("-")[1].equals("1")) {
						q = "Q4";
						year--;
					} else if (Server.nextquarterdate.split("-")[1].equals("4"))
						q = "Q1";
					else if (Server.nextquarterdate.split("-")[1].equals("7"))
						q = "Q2";
					else if (Server.nextquarterdate.split("-")[1].equals("01")) {
						q = "Q4";
						year--;
					} else if (Server.nextquarterdate.split("-")[1].equals("04"))
						q = "Q1";
					else if (Server.nextquarterdate.split("-")[1].equals("07"))
						q = "Q2";
					Server.nextquarterdate = Server.nextquarterdate.split("-")[0] + "-"
							+ (Integer.parseInt(Server.nextquarterdate.split("-")[1]) + 3) + "-01";
				}

				// here comes the code to handle the quarters report for each store
				ArrayList<Store> stores = Server.getStores();
				for (Store s : stores) {
					Server.setIncomeReportToDB(s.getS_ID(), q, "" + year);
					Server.setOrderReportToDB(s.getS_ID(), q, year);
					Server.saveComplaintReportInDB(s.getS_ID(), q, "" + year);
					Server.saveSatisfactionReportInDB(s.getS_ID(), q, "" + year);
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
