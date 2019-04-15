package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimerTask;

/**
 * This class responsible for handling the open complaints It will be summon
 * every minute and check if a complaint is already open for 24 hours If it does
 * the customer receive compensate amount of 30 shekel automatically
 * 
 * @author dorgori
 *
 */
public class HandleComplaintInterval extends TimerTask {

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		LocalTime nowTime = LocalTime.now();
		LocalDate toComp = LocalDate.now();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date nowDate = null;
		try {
			nowDate = Date.from(toComp.atStartOfDay(ZoneId.systemDefault()).toInstant());
			nowDate = sdf.parse(toComp.toString());
		} catch (ParseException e2) {
			// e2.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + Server.path, Server.username, Server.password);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			ps = conn.prepareStatement("SELECT * FROM sys.complaints WHERE status=?");
			ps.setInt(1, 0);
			rs = ps.executeQuery();
			while (rs.next()) {
				String strCreatedDate = rs.getString(7);
				Date createdDate = createdDate(strCreatedDate);
				String [] dates = strCreatedDate.split("-");
				int createdYear = Integer.parseInt(dates[0]);
				int createdMonth = Integer.parseInt(dates[1]);
				int createdDay = Integer.parseInt(dates[2]);
				if (nowDate.after(createdDate) == true)// day is passed now check hours
				{
					if (checkTimer(nowTime, rs.getString(5)) == true)// need to save to db
					{
						try {
							updateInDB(rs.getInt(1));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					else if(toComp.getYear()>createdYear ||toComp.getMonthValue()>createdMonth||(toComp.getDayOfMonth()-createdDay)>=2)
						updateInDB(rs.getInt(1));
				}
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * set the complaint status to 1 and compensate cutomer by 30 shekels , save in
	 * db
	 * 
	 * @param id
	 *            - the id of the complaint
	 * @throws SQLException
	 */
	private void updateInDB(int id) throws SQLException {
		Connection conn = null;
		PreparedStatement prs;
		conn = DriverManager.getConnection("jdbc:mysql://" + Server.path, Server.username, Server.password);
		prs = conn
				.prepareStatement("UPDATE sys.complaints SET status='1', compensateAmount='30' WHERE complaint_id=?;");
		prs.setInt(1, id);
		prs.executeUpdate();
		conn.close();
	}

	/**
	 * receive the complaint created date represent by string in db and convert it
	 * to date type
	 * 
	 * @param dateInDB
	 *            the string date from db
	 * @return date type
	 */
	private Date createdDate(String dateInDB) {
		Date toRet = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			toRet = sdf.parse(dateInDB);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return toRet;
	}

	/**
	 * check if there is a 24 hours difference and if it does return true , other
	 * false
	 * 
	 * @param nowTime
	 *            - the current time
	 * @param time
	 *            - the created time that store in the data base
	 * @return true if 24 hours difference other false
	 */
	private boolean checkTimer(LocalTime nowTime, String timeinDB) {
		boolean flag = false;
		String[] arrTime = timeinDB.split(":");
		int createdHour = Integer.parseInt(arrTime[0]);
		int createdMinute = Integer.parseInt(arrTime[1]);
		if (nowTime.getHour() > createdHour)// needs to compesate
			flag = true;
		else if (nowTime.getHour() == createdHour && nowTime.getMinute() >= createdMinute)
			flag = true;

		return flag;
	}
}
