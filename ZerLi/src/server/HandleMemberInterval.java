package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.TimerTask;

/**
 * This class responsible to check if a member is expired The test will be every
 * day and if a member expired he will be change to regular member
 * 
 * @author dorgori
 *
 */
public class HandleMemberInterval extends TimerTask {

	@Override
	public void run() {
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + Server.path, Server.username, Server.password);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try { // handle monthly member
			ps = conn.prepareStatement("SELECT * FROM sys.customerinstore WHERE `memberType`=?;");
			ps.setInt(1, 1);
			rs = ps.executeQuery();
			while (rs.next()) {
				String strCreatedDate = rs.getString(5);// member created date
				if (checkExpiredMonthly(strCreatedDate) == true)// member expired , update in db
				{
					deleteMember(rs.getString(1),rs.getString(2));
				}
			}
			ps = conn.prepareStatement("SELECT * FROM sys.customerinstore WHERE `memberType`=?;");
			ps.setInt(1, 2);
			rs = ps.executeQuery();
			while (rs.next()) {
				String strCreatedDate = rs.getString(5);// member created date
				if (checkYearlyExpired(strCreatedDate) == true)// member expired , update in db
				{
					deleteMember(rs.getString(1),rs.getString(2));
				}
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * This method responsible for checking if a monthly member expired
	 * 
	 * @param strCreatedDate
	 *            - the created date of the member
	 * @return - true if expired ,false otherwise
	 */
	private boolean checkExpiredMonthly(String strCreatedDate) {
		boolean toRet = false;
		LocalDate toComp = LocalDate.now();
		String[] dates = strCreatedDate.toString().split("-");
		String createdYear = dates[0];
		String createdMonth = dates[1];
		String createdDay = dates[2];
		int year = Integer.parseInt(createdYear);
		int month = Integer.parseInt(createdMonth);
		int day = Integer.parseInt(createdDay);
		if (toComp.getMonthValue() > month)// month is passed now check for the day
		{
			if (toComp.getDayOfMonth() >= day)// day is passed need to return true
			{
				toRet = true;
			}
		} else if (toComp.getYear() > year && toComp.getDayOfMonth() >= day)// a year is passed only in january - december
		{
			if (toComp.getDayOfMonth() >= day)// day is passed need to return true
			{
				toRet = true;
			}
		}
		return toRet;
	}

	/**
	 * This method responsible for checking if a yearly member is expired
	 * @param strCreatedDate represent the register date
	 * @return true if expired false otherwise
	 */
	private boolean checkYearlyExpired(String strCreatedDate) {
		boolean toRet = false;
		LocalDate toComp = LocalDate.now();
		String[] dates = strCreatedDate.toString().split("-");
		String createdYear = dates[0];
		String createdMonth = dates[1];
		String createdDay = dates[2];
		int year = Integer.parseInt(createdYear);
		int month = Integer.parseInt(createdMonth);
		int day = Integer.parseInt(createdDay);
		if (toComp.getYear() > year && toComp.getMonthValue() >= month && toComp.getDayOfMonth() >= day)
			toRet = true;
		return toRet;
	}

	/**
	 * This method responsible for delete a member and change him to regular customer
	 * @param customerID
	 * @param storeID
	 */
	private void deleteMember(String customerID,String storeID) {
		PreparedStatement prs = null;
		LocalDate toComp = LocalDate.now();
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://" + Server.path, Server.username,
					Server.password);
			prs = conn.prepareStatement("UPDATE customerinstore SET `memberType`=?, `createdDate`=? WHERE `c_ID`=? AND `s_ID`=? ;");
			prs.setInt(1, 0);
			prs.setString(2, toComp.toString());
			prs.setString(3, customerID);
			prs.setString(4, storeID);
			prs.executeUpdate();
			conn.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
