package server;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;

import catalog.Discount;
import catalog.Product;
import client.Customer;
import client.CustomerinStore;
import client.Store;
import client.StoreEmployee;
import client.StoreManager;
import client.User;
import compensate_customer.Complaint;
import compensate_customer.CustomerServiceEmployee;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import order.Flower;
import order.Order;
import order.Order.orderStatus;
import order.SelfMadeProduct;
import reports.ItemInOrderReport;
import survey.Survey;

/**
 * This class responsible to communicate with the data base We receive requests
 * from the client asking for information from the data base and Sending back
 * the data This class also store the data base register info
 */
public class Server extends AbstractServer {
	final public static int DEFAULT_PORT = 5555;
	static String path;
	static String username;
	static String password;
	private static int numOfOrders;
	public static int numOfSurvey;
	public static Timer reportTimer;
	public static Timer memberTimer;
	public static Timer complaintTimer;
	final static int day = 86400000;
	public static String nextquarterdate = "2016-01-01";
	public static ArrayList<String> pro_comment_arr;
	public static int port = 0;

	public Server(int port) {
		super(port);
	}

	public static void main(String[] args) throws ParseException {
		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}
		ServerThread t = new ServerThread();
		t.start();
		System.out.println("Before server connection:" + "\t path= \t" + path + "\t Username= \t" + username
				+ "\t Password= \t" + password);
	
		 setNextQarterDate();
		 System.out.println("next quarter date is : "+nextquarterdate);

	}

	/**
	 * This method responsible to check if the gui of the server is up and only then
	 * we want to ask for information from the database
	 */
	public static void tryConnectingToServer() {
		System.out.println("After server connection:" + "\t path= \t" + path + "\t Username= \t" + username
				+ "\t Password= \t" + password);
		Server sv = new Server(port);
		set_up();
		initializeNumOfOrders();
		initializeNumOfSurvey();
		complaintTimer = new Timer();
		complaintTimer.schedule(new HandleComplaintInterval(),0,day);
		reportTimer = new Timer();
		reportTimer.schedule(new ReportMaker(), 0,day);
		memberTimer = new Timer();
		memberTimer.schedule(new HandleMemberInterval(), 0, day);
		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}

	/**
	 * This method responsible to set all the users as disconnected when the server
	 * is up
	 */
	private static void set_up() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("UPDATE users SET is_Active=0");
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * This method occur when the client send message to the server and asks for
	 * data from the data base
	 * 
	 * @param msg
	 *            : the object represent the client needed , according to the msg
	 *            the server knows which ask is it
	 * @param client
	 *            : the active client this function check whether the msg is a
	 *            string or a products that needs to be update
	 *
	 */
	@SuppressWarnings("unchecked")
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		System.out.println(msg);
		if (msg instanceof ArrayList<?>) {
			if (((ArrayList<?>) msg).get(0) instanceof String) {
				if (((ArrayList<String>) msg).get(0).equals("recive discounts")) {
					try {
						client.sendToClient(getDiscounts(((ArrayList<String>) msg).get(1)));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (((ArrayList<Object>) msg).get(0).equals("compensate")) {
					try {
						((ArrayList<Object>) msg).remove(0);
						client.sendToClient(updateComplaintInDB(msg));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (((ArrayList<Object>) msg).get(0).equals("ask for regular customers")) {
					try {
						client.sendToClient(getRegularMemberFromDB(msg));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (((ArrayList<Object>) msg).get(0).equals("send cutomer to change")) {
						((ArrayList<Object>) msg).remove(0);
						updateCutomerMemebershipInDB(msg);
				} else if (((ArrayList<Object>) msg).get(0).equals("order handlig over")) {
					int orderID = (int) ((ArrayList<Object>) msg).get(1);
					String storeID = (String) ((ArrayList<Object>) msg).get(2);
					try {
						client.sendToClient(updateCompletedOrder(orderID, storeID));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				else if (((ArrayList<String>) msg).get(0).equals("recive customer orders")) {
					try {
						client.sendToClient(getCustomerOrders(((ArrayList<String>) msg).get(1),
								(((ArrayList<String>) msg)).get(2)));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (((ArrayList<String>) msg).get(0).equals("remove discount")) {
					System.out.println("chcek");
					removeDiscount(((ArrayList<String>) msg).get(1), ((ArrayList<String>) msg).get(2));
					try {
						client.sendToClient("hi"); // to make him stop waiting
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (((ArrayList<String>) msg).get(0).equals("add discount")) {

					addDiscount(((ArrayList<Discount>) msg).get(1));
					try {
						client.sendToClient("hi"); // to make him stop waiting
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (((ArrayList<String>) msg).get(0).equals("edit discount")) {

					editDiscount(((ArrayList<Discount>) msg).get(1));
					try {
						client.sendToClient("hi"); // to make him stop waiting
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (((ArrayList<String>) msg).get(0).equals("get complaints to report")) {
					try {
						client.sendToClient(getComplaintToReportFromDB(msg));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (((ArrayList<String>) msg).get(0).equals("recive users")) {
					try {
						client.sendToClient(getUsersList());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (((ArrayList<String>) msg).get(0).equals("recieve isActive")) {
					try {
						client.sendToClient(findIsActive(((ArrayList<String>) msg).get(1)));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (((ArrayList<String>) msg).get(0).equals("receive flowers")) {
					try {
						client.sendToClient(getFlowerFromDB());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (((ArrayList<String>) msg).get(0).equals("getOrderReport")) {
					try {
						client.sendToClient(getOrderReport(((ArrayList<String>) msg).get(1),
								((ArrayList<String>) msg).get(2), ((ArrayList<Integer>) msg).get(3)));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (((ArrayList<String>) msg).get(0).equals("recive user to change permission")) {
					ArrayList<String> arr = new ArrayList<String>();
					arr.add(((ArrayList<String>) msg).get(1));
					arr.add(((ArrayList<String>) msg).get(2));
					changePermission(arr.get(0), arr.get(1));
				} else if (((ArrayList<String>) msg).get(0).equals("get customers that not from that store")) {
					try {
						client.sendToClient(getCustomersToAdd(((ArrayList<String>) msg).get(1)));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else if (((ArrayList<String>) msg).get(0).equals("receive complete customer in store to add")) {
					try {
						client.sendToClient(sendCustomerinStoreToDB(((ArrayList<Customer>) msg).get(1)));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				else if (((ArrayList<String>) msg).get(0).equals("Update customer refund")) {
					String storeID = ((ArrayList<String>) msg).get(1);
					String customerID = ((ArrayList<String>) msg).get(2);
					float refundAmount = ((ArrayList<Float>) msg).get(3);
					try {
						client.sendToClient(updateCustomerRefund(storeID, customerID, refundAmount));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (((ArrayList<String>) msg).get(0).equals("get order")) {
					try {
						client.sendToClient(getOrder(((ArrayList<Order>) msg).get(1)));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						client.sendToClient("bye");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				else if (((ArrayList<String>) msg).get(0).equals("exit"))
					user_inactive(((ArrayList<User>) msg).get(1));

				else if (((ArrayList<String>) msg).get(0).equals("deleteProd"))
					removeProductFromCatalogDB(((ArrayList<Product>) msg).get(1));

				else if (((ArrayList<String>) msg).get(0).equals("Edit_pro"))
					updateProduct(((ArrayList<Product>) msg).get(1));

				else if (((ArrayList<String>) msg).get(0).equals("addPro"))
					AddProductFromCatalogDB(((ArrayList<Product>) msg).get(1));

				else if (((ArrayList<String>) msg).get(0).equals("addSurvey"))
					AddSurveyToDB(((ArrayList<Survey>) msg).get(1));

				else if (((ArrayList<String>) msg).get(0).equals("get answers")) {
					try {

						try {
							client.sendToClient(getSurveyAnswersFromDB());
						} catch (IOException e) {// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (SQLException e1) {

						e1.printStackTrace();
					}
				} else if (((ArrayList<String>) msg).get(0).equals("findStores")) {
					try {
						client.sendToClient(getCustomerStores(((ArrayList<String>) msg).get(1)));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (((ArrayList<String>) msg).get(0).equals("get store income report")) {
					System.out.println("check1");
					String storeID = ((ArrayList<String>) msg).get(1);
					String q = ((ArrayList<String>) msg).get(2);
					String year = "" + ((ArrayList<Integer>) msg).get(3);
					try {
						System.out.println("check2");
						client.sendToClient(getIncomeReport(storeID, q, year));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (((ArrayList<String>) msg).get(0).equals("get Satisfaction Report")) {
					System.out.println("check3");
					String storeID = ((ArrayList<String>) msg).get(1);
					String q = ((ArrayList<String>) msg).get(2);
					String year = "" + ((ArrayList<Integer>) msg).get(3);
					try {
						System.out.println("check4");
						client.sendToClient(getSatisfactionReport(storeID, q, year));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (((ArrayList<String>) msg).get(0).equals("cancel order")) {
					try {
						client.sendToClient(
								cancelOrder(((ArrayList<Order>) msg).get(1), ((ArrayList<Float>) msg).get(2)));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (((ArrayList<String>) msg).get(0).equals("ask for customers in stores")) {
					try {
						client.sendToClient(getCustomersFromDB());
					} catch (Exception e) {
					}
				} else if (((ArrayList<String>) msg).get(0).equals("Delete Store-Employee")) {
					deleteStoreEmp(((ArrayList<String>) msg).get(1));
				} else if (((ArrayList<String>) msg).get(0).equals("Delete Store-Manager")) {
					deleteStoreMng(((ArrayList<String>) msg).get(1));
				} else if (((ArrayList<String>) msg).get(0).equals("Remove customer from tables")) {
					deleteCustomer(((ArrayList<String>) msg).get(1));
				}else if (((ArrayList<String>) msg).get(0).equals("Add new Store-Emp")) {
					String storeEmpID = ((ArrayList<String>) msg).get(1);
					String storeID = ((ArrayList<String>) msg).get(2);
					try {
						client.sendToClient(addNewStoreEmp(storeEmpID, storeID));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (((ArrayList<String>) msg).get(0).equals("Add new Store-Mng")) {
					String storeMngID = ((ArrayList<String>) msg).get(1);
					String storeID = ((ArrayList<String>) msg).get(2);
					try {
						client.sendToClient(addNewStoreMng(storeMngID, storeID));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (((ArrayList<String>) msg).get(0).equals("recieve store orders")) {
					try {
						client.sendToClient(getStoreOrders(((ArrayList<String>) msg).get(1)));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (((ArrayList<String>) msg).get(0).equals("get pro comment arr")) {
					try {
						client.sendToClient(up_date_pro_comment_arr());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				else if (((ArrayList<Object>) msg).get(0).equals("add_pro_comment")) {
					add_pro_comment((int) (((ArrayList) msg).get(1)), (String) (((ArrayList) msg).get(2)));
					try {
						client.sendToClient("added pro comment");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // selected index , pro comment
				}

			}
		} else if (msg instanceof String) // in case of string from client
		{
			String command = (String) msg;
			if (command.equals("recive")) {
				try {
					client.sendToClient(getProducts());
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Recived");
				System.out.println("SERVER:" + getProducts().get(0).get_product_Name());
			} else if (command.equals("GetStores")) {
				try {
					client.sendToClient(getStores());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (msg instanceof Product) // in case update product was pressed
		{
			updateProduct(msg);
		} else if (msg instanceof Complaint) {
			saveComplaintToDB((Complaint) msg);
		} else if (msg instanceof Customer) {
			try {
				client.sendToClient(find_customer((Customer) msg));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (msg instanceof StoreEmployee) {
			try {
				client.sendToClient(find_sEmployee((StoreEmployee) msg));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (msg instanceof StoreManager) {
			try {
				client.sendToClient(find_sMngr((StoreManager) msg));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (msg instanceof CustomerServiceEmployee) {
			try {
				client.sendToClient(getComplaintFromDB((CustomerServiceEmployee) msg));
			} catch (Exception e) {
			}
		} else if (msg instanceof User) {
			try {
				client.sendToClient(find_user((User) msg));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (msg instanceof Order) {
			System.out.println("Server-HandleMsgFromClient " + ((Order) msg).getCustomerID());

			try {
				client.sendToClient(addNewOrderToDB((Order) msg));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * This method update customer member in db
	 * @param msg - arraylist with customer id and membertype 
	 */
	@SuppressWarnings("unchecked")
	private void updateCutomerMemebershipInDB(Object msg) {
		ArrayList<String> toRet = new ArrayList<String>();
		String customerID = ((ArrayList<String>)msg).get(0);
		String storeID = ((ArrayList<String>)msg).get(1);
		int memberType = Integer.parseInt(((ArrayList<String>)msg).get(2));
		Connection conn = null;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			ps = conn.prepareStatement("UPDATE `sys`.`customerinstore` SET `memberType`=? WHERE `c_ID`=? and`s_ID`=?;");
			ps.setInt(1, memberType);
			ps.setString(2, customerID);
			ps.setString(3, storeID);
			ps.executeUpdate();
			conn.close();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * This method pull from the data base regular members account in specific store
	 * @param msg - arraylist with validation string and store id
	 * @return - arraylist of string represent cutomer with member regular
	 */
	@SuppressWarnings("rawtypes")
	private ArrayList<String> getRegularMemberFromDB(Object msg) {
		ArrayList<String> toRet = new ArrayList<String>();
		String storeID = (String) ((ArrayList)msg).get(1);
		toRet.add((String) ((ArrayList)msg).get(0));
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			ps = conn.prepareStatement("SELECT `c_ID` from customerinstore WHERE `s_ID`=? AND `memberType`=?;");
			ps.setString(1, storeID);
			ps.setInt(2, 0);
			rs = ps.executeQuery();
			while(rs.next())
			{
				toRet.add(rs.getString(1));
			}
			conn.close();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		if(toRet.size()==1)
			toRet.add("-999");
		return toRet;
	}

	/**
	 * This method gets Customer ID and remove this customer from customers table and from customerinstore table.
	 * @author Oren
	 * @param customerID
	 */
	private void deleteCustomer(String customerID) {
		Connection conn = null;
		PreparedStatement ps,ps1;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			ps = conn.prepareStatement("DELETE FROM `sys`.`customers` WHERE `userName`=?;");
			ps.setString(1, customerID);
			ps.execute();
			ps1 = conn.prepareStatement("DELETE FROM `sys`.`customerinstore` WHERE `c_ID`=?;");
			ps1.setString(1,customerID);
			ps1.execute();
			conn.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * this method take the report from the database the report it will return is
	 * from the time period requested by the params: (IT DOES NOT MAKE THE REPORTS)
	 * 
	 * @param storeID
	 * @param q
	 *            quarter
	 * @param year
	 * @return array list of types that were sold in said quarter and how much they
	 *         were sold + the profit
	 */
	private ArrayList<ItemInOrderReport> getOrderReport(String storeID, String q, Integer year) {
		Connection conn;
		ArrayList<ItemInOrderReport> arr = new ArrayList<ItemInOrderReport>();
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			PreparedStatement ps = conn
					.prepareStatement("Select * From orderreport WHERE " + "storeID=? AND quarter=? AND year=?");
			ResultSet rs;
			ps.setString(1, storeID);
			ps.setString(2, q);
			ps.setString(3, "" + year);
			rs = ps.executeQuery();
			while (rs.next()) {
				arr.add(new ItemInOrderReport(rs.getString(4), rs.getInt(5), rs.getFloat(6)));

			}
			conn.close();

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (arr.isEmpty())
			arr.add(new ItemInOrderReport("ERROR", -9999, 9999));
		return arr;
	}

	/**
	 * aften adding professional comment it can be editable
	 * 
	 * @return array list of comments - strings
	 */

	private ArrayList<String> up_date_pro_comment_arr() {
		pro_comment_arr = new ArrayList<String>();
		pro_comment_arr.add("up date pro comment arr");
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (int j = 1; j <= numOfSurvey; j++) {
			try {
				ps = conn.prepareStatement("Select * From survey WHERE surveyID=?");
				ps.setLong(1, j); // instead first ? = name
				rs = ps.executeQuery(); // rs=user details
				while (rs.next()) {

					if (rs.getString(10) != null)
						pro_comment_arr.add(rs.getString(10));// insert the comments or null.
					else
						pro_comment_arr.add("");//
				}

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pro_comment_arr;
	}

	/**
	 * adding new proprssional comment to survey
	 * 
	 * @param index-
	 *            number of line which were chossen
	 * @param pro_comment
	 */

	private void add_pro_comment(int index, String pro_comment) {

		index = index + 1;
		Connection conn = null;
		PreparedStatement ps;
		System.out.println("in server : index = " + index + " and comment= " + pro_comment);

		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			ps = conn.prepareStatement("UPDATE survey SET `Procomment`=? WHERE `surveyID`=?;");
			ps.setString(1, pro_comment);// name
			ps.setInt(2, index);

			ps.executeUpdate();
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	/**
	 * take from db all the answers by one survey and put it in array in size of 6
	 * for the answers
	 * 
	 * @return array of Surveys
	 * @throws SQLException
	 */
	private ArrayList<Survey> getSurveyAnswersFromDB() throws SQLException {

		pro_comment_arr = new ArrayList<String>();

		ArrayList<Survey> survey_arr = new ArrayList<Survey>();
		// survey_arr.add("receive survey from DB");
		pro_comment_arr.add("pro comment arr");
		int[] arr = new int[6];

		Survey survey;
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("num of sur : " + numOfSurvey);
		for (int j = 1; j <= numOfSurvey; j++) {
			try {
				// toRet = new String[6];

				ps = conn.prepareStatement("Select * From survey WHERE surveyID=?");
				ps.setLong(1, j); // instead first ? = name
				rs = ps.executeQuery(); // rs=user details
				while (rs.next()) {

					for (int i = 3; i <= 8; i++)
						arr[i - 3] = rs.getInt(i);// insert the questions

					if (rs.getString(9) != null) {
						pro_comment_arr.add(rs.getString(10));// insert the comments or null.

					}

					else
						pro_comment_arr.add("");

					survey = new Survey(j, rs.getString(2), arr, rs.getString(9), rs.getString(10));
					survey_arr.add(survey);

				}

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} // for end
		conn.close();

		return survey_arr;
	}

	/**
	 * This method responsible for pulling complaint details for a report
	 * 
	 * @param msg
	 *            - arraylist contain validation string,store id,year,quarter
	 * @return - arraylist of string represent complaint details
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<String> getComplaintToReportFromDB(Object msg) {
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		ArrayList<String> toRet = new ArrayList<String>();
		toRet.add((String) ((ArrayList<Object>) msg).get(0));
		String storeID, yearStr, quarter;
		storeID = (String) ((ArrayList<Object>) msg).get(1);
		int year = (int) (((ArrayList<Object>) msg).get(2));
		yearStr = Integer.toString(year);
		quarter = (String) ((ArrayList<Object>) msg).get(3);
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ps = conn.prepareStatement("SELECT * FROM complaintreport WHERE storeID=? AND year=? AND quarter=?");
			ps.setString(1, storeID);
			ps.setString(2, yearStr);
			ps.setString(3, quarter);
			rs = ps.executeQuery();
			if (rs.next()) {
				toRet.add(storeID);
				toRet.add(yearStr);
				toRet.add(quarter);
				toRet.add(Integer.toString(rs.getInt(4)));
				toRet.add(Integer.toString(rs.getInt(5)));
				toRet.add(Float.toString(rs.getFloat(6)));
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (toRet.size() == 1)
			toRet.add("Error");
		return toRet;
	}

	/**
	 * this method returns a report from the DB (Income Report) based on store
	 * quarter and year
	 * 
	 * @param storeID
	 * @param q
	 *            - quarter
	 * @param year
	 * @return income for requested time
	 */
	private Object getIncomeReport(String storeID, String q, String year) {

		Connection conn;
		float income = -9999;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			PreparedStatement ps = conn
					.prepareStatement("Select income From incomereport WHERE storeID=? AND year=? AND quarter=?");
			ResultSet rs;
			ps.setString(1, storeID);
			ps.setString(2, year);
			ps.setString(3, q);
			rs = ps.executeQuery();
			if (rs.next())

			{
				income = rs.getFloat(1);
				conn.close();
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return income;
	}

	/**
	 * This method returns from the db a report of satisfaction base store quarter
	 * and year
	 * 
	 * @param storeID
	 * @param q
	 *            - quarter
	 * @param year
	 * @return ArrayList with all the values of the report
	 */
	private Object getSatisfactionReport(String storeID, String q, String year) {

		Connection conn;
		float totalSatisfaction = -9999;
		ArrayList<Float> toRet = new ArrayList<Float>();
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			PreparedStatement ps = conn.prepareStatement(
					"Select totalSatisfaction, q1, q2, q3, q4, q5, q6 From satisfaction_report WHERE storeID=? AND year=? AND quarter=?");
			ResultSet rs;
			ps.setString(1, storeID);
			ps.setString(2, year);
			ps.setString(3, q);
			rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getFloat(1) != 0.0) {
					toRet.add(rs.getFloat(1));
					toRet.add(rs.getFloat(2));
					toRet.add(rs.getFloat(3));
					toRet.add(rs.getFloat(4));
					toRet.add(rs.getFloat(5));
					toRet.add(rs.getFloat(6));
					toRet.add(rs.getFloat(7));
				} else
					toRet.add(totalSatisfaction);
				conn.close();
			} else {
				toRet.add(totalSatisfaction);
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return toRet;
	}

	/**
	 * This method invoke when client send completed customer to add in
	 * customerinstore table to add new customer to store like open payment option
	 * in specific store.
	 * 
	 * @author Oren
	 * @param customer
	 *            - the customer that we want to save
	 * @return message that end handling the operation
	 */
	private String sendCustomerinStoreToDB(Customer customer) {
		Connection conn = null;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			ps = conn.prepareStatement(
					"INSERT INTO `sys`.`customerinstore` (`c_ID`, `s_ID`, `refund`, `memberType`, `createdDate`) "
							+ "VALUES ( ?, ?, ?, ?, ?);");
			ps.setString(1, customer.getName());
			ps.setString(2, customer.getStoreID());
			ps.setFloat(3, customer.getRefundAmount());
			ps.setInt(4, customer.MemTypetoInt(customer.getMemberOption()));
			ps.setString(5, customer.getCreatedDate());
			ps.execute();
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Insert new Customer in store completed";
	}

	/**
	 * This method extract all the customers that dont have account in the specific
	 * store
	 * 
	 * @author Oren
	 * @param storeID
	 *            the id of the specific store
	 * @return ArrayList of customers names that dont have account in this store
	 */
	private ArrayList<String> getCustomersToAdd(String storeID) {
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		ArrayList<String> customers = new ArrayList<String>();
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			ps = conn.prepareStatement("Select * From customers;");
			rs = ps.executeQuery(); // rs=user details//
			while (rs.next())
				customers.add(rs.getString(1));
			customers = deleteCustomersInStore(storeID, customers);
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("SERVER CUSTOMERS:" + customers);
		customers.add(0, "recive list of customers to add");
		return customers;
	}

	/**
	 * This method called from getCustomersToAdd to filter all the customers that
	 * have account int this store.
	 * 
	 * @param storeID
	 *            - the id of the specific store
	 * @param customers
	 *            - all the userName of all the customers
	 * @return Updated ArrayList of customers that dont have account in the specific
	 *         store.
	 */
	private ArrayList<String> deleteCustomersInStore(String storeID, ArrayList<String> customers) {
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			ps = conn.prepareStatement("Select c_ID From customerinstore WHERE s_ID=?;");
			ps.setString(1, storeID);
			rs = ps.executeQuery(); // rs=user details//
			while (rs.next())
				customers.remove(rs.getString(1));
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customers;
	}

	/**
	 * This method responsible for pull order from the data base
	 * 
	 * @author Oren
	 * @param order
	 *            - the order
	 * @return - updated order
	 */
	private Order getOrder(Order order) {
		Order toret=new Order();
		Connection conn = null;
		ResultSet rs;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			ps = conn.prepareStatement("SELECT * FROM orders  WHERE orderID=? AND storeID=? AND customerID=?");
			ps.setInt(1, order.getOrderID());
			ps.setString(2, order.getStoreID());
			ps.setString(3, order.getCustomerID());
			rs = ps.executeQuery();
			rs.next();
			toret.setOrderID(rs.getInt(1));
			toret.setStoreID(rs.getString(2));
			toret.setCustomerID(rs.getString(3));
			toret.setOrderDate(rs.getString(4));
			toret.setGreetingCard(rs.getString(5));
			toret.setDeliveryOption(findDeliveryType(rs.getInt(6)));
			toret.setDeliveryRecipient(rs.getString(7));
			toret.setDeliveryAddress(rs.getString(8));
			toret.setRequirementDate(rs.getString(9));
			toret.setRequirementTime(rs.getString(10));
			toret.setPrice(rs.getFloat(11));
			toret.setOrderStatusOption(findOrderStatus(rs.getInt(12)));
			toret.setRefund(rs.getFloat(13));
			toret.setTel(rs.getString(14));
			toret.setPaymentType(rs.getInt(15));
			conn.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return toret;
	}

	/**
	 * This method responsible to mark order as pickup in db
	 * 
	 * @author Oren
	 * @param orderID
	 *            - order id
	 * @param storeID
	 *            - store id
	 * @return - validation string
	 */
	private String updateCompletedOrder(int orderID, String storeID) {
		Connection conn = null;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			ps = conn.prepareStatement("UPDATE `sys`.`orders` SET `orderStatus`='1' WHERE `orderID`=?;");
			ps.setInt(1, orderID);
			ps.executeUpdate();
			conn.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return "order Finished!";
	}

	/**
	 * This method responsible to pull and return from the db all the orders of a
	 * specific store
	 * 
	 * @author Oren
	 * @param storeID
	 *            - store id
	 * @return - arraylist of orders
	 */
	private ArrayList<Order> getStoreOrders(String storeID) {
		ArrayList<Order> storeOrders = new ArrayList<Order>();
		Connection conn = null;
		ResultSet rs;
		int count = 0;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			System.out.println("SQL connection succeed");
			PreparedStatement ps = conn.prepareStatement("Select * From sys.orders WHERE storeID=?;");
			ps.setString(1, storeID);
			rs = ps.executeQuery(); // rs=user details
			while (rs.next()) {
				count++;
				Order singleOrder = new Order(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), findDeliveryType(rs.getInt(6)), rs.getString(7), rs.getString(8),
						rs.getString(9), rs.getString(10), rs.getFloat(11), findOrderStatus(rs.getInt(12)),
						rs.getFloat(13), rs.getString(14), rs.getInt(15));
				singleOrder.setOrderedProducts(getProductsOfOrder(singleOrder.getOrderID()));
				singleOrder.setSelfMadeProducts(getSelfMadeProductsOfOrder(singleOrder.getOrderID()));
				if (singleOrder.getOrderStatusOption().equals(orderStatus.ordered))
					storeOrders.add(singleOrder);
				else
					count--;
			}
			if (count == 0)
				storeOrders.add(new Order());

		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return storeOrders;
	}

	/**
	 * This method responsible to save complaint report of a specific quarter in db
	 * 
	 * @param storeID
	 *            - store id
	 * @param quarter
	 *            - quarter
	 * @param year
	 *            - year
	 */
	static void saveComplaintReportInDB(String storeID, String quarter, String year) {
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		Complaint c;
		ArrayList<Complaint> toSave = new ArrayList<Complaint>();
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			ps = conn.prepareStatement("SELECT * FROM complaints WHERE storeID=?");
			ps.setString(1, storeID);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (isInRange(year, quarter, rs.getString(7))) {
					c = new Complaint(rs.getString(2), rs.getString(4), rs.getString(3), rs.getString(6),
							Integer.parseInt(rs.getString(5).substring(0, 2)),
							Integer.parseInt(rs.getString(5).substring(3, 5)));
					c.setDate(rs.getString(7));
					c.set_status(rs.getBoolean(8));
					c.set_compensateAmount(rs.getInt(9));
					toSave.add(c);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		////////// NOW SAVE TO DATA BASE////////
		try {
			PreparedStatement prs = conn.prepareStatement("INSERT INTO complaintreport (`storeID`,"
					+ " `year`, `quarter`, `openedComplaint`, `numberOfCompensation`,"
					+ " `compensationAmount`) VALUES (?, ?, ?, ?, ?, ?);");
			for (Complaint ct : toSave) {
				prs.setString(1, ct.get_storeID());
				prs.setString(2, year);
				prs.setString(3, quarter);
				prs.setInt(4, toSave.size());
				prs.setInt(5, getNumOfCompensation(toSave));
				prs.setFloat(6, calculateCompensationAmount(toSave));
				prs.execute();
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method gets all the surveys of the this quarter from the survey table
	 * and collects it to report.
	 * 
	 * @param storeID
	 * @param quarter
	 * @param year
	 */
	static void saveSatisfactionReportInDB(String storeID, String quarter, String year) {
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		Survey sur;
		ArrayList<Survey> toSave = new ArrayList<Survey>();
		int[] arr = new int[6];
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			ps = conn.prepareStatement("SELECT * FROM survey WHERE storeID=?");
			ps.setString(1, storeID);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (isInRange(year, quarter, rs.getString(2))) {
					for (int i = 3; i < 9; i++)
						arr[i - 3] = rs.getInt(i);

					sur = new Survey(rs.getInt(1), rs.getNString(2), arr, rs.getNString(9), rs.getNString(10));
					toSave.add(sur);
				}
			}
			////////// NOW SAVE TO DATA BASE////////
			arr = new int[6];
			Survey currSave = new Survey(arr, storeID);
			int numofSurvey = 1;
			int sum = 0;
			for (Survey st : toSave) {
				for (int i = 3; i < 9; i++)
					arr[i - 3] += st.getAns(i-3);
				numofSurvey++;
			}
			for (int i = 0; i < 6; i++) {
				sum += arr[i];
				arr[i] /= numofSurvey;
			}
			currSave.setAnswer(arr);
			currSave.setTotalAvg(sum / numofSurvey);
			PreparedStatement prs = conn.prepareStatement(
					"INSERT INTO `sys`.`satisfaction_report` (`storeID`, `year`, `quarter`, `totalSatisfaction`, `q1`, `q2`, `q3`, `q4`, `q5`, `q6`) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
			prs.setString(1, storeID);
			prs.setString(2, year);
			prs.setString(3, quarter);
			prs.setFloat(4, currSave.getTotalAvg());
			prs.setFloat(5, currSave.getAns(0));
			prs.setFloat(6, currSave.getAns(1));
			prs.setFloat(7, currSave.getAns(2));
			prs.setFloat(8, currSave.getAns(3));
			prs.setFloat(9, currSave.getAns(4));
			prs.setFloat(10, currSave.getAns(5));
			prs.execute();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * calculate the number of complaint that resolved by compensate
	 * 
	 * @param toCheck
	 *            arraylist of complaints
	 * @return number of complaints that compensated
	 */
	private static int getNumOfCompensation(ArrayList<Complaint> toCheck) {
		int toRet = 0;
		for (Complaint c : toCheck) {
			if (c.get_compensateAmount() != 0)
				toRet++;
		}
		return toRet;
	}

	/**
	 * this method calculate total compensation amount in a quarter
	 * 
	 * @param toCheck
	 *            arraylist of complaint
	 * @return compensation amount
	 */
	private static float calculateCompensationAmount(ArrayList<Complaint> toCheck) {
		float toRet = 0;
		for (Complaint c : toCheck) {
			if (c.get_compensateAmount() != 0)
				toRet += c.get_compensateAmount();
		}
		return toRet;
	}

	/**
	 * This method responsible to save store employe in db
	 * 
	 * @param storeEmpID
	 *            - store employee id
	 * @param storeID
	 *            - store id
	 * @return - validation string
	 */
	private String addNewStoreEmp(String storeEmpID, String storeID) {
		Connection conn = null;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			ps = conn.prepareStatement("INSERT INTO `sys`.`storeemployee` (`userName`, `s_id`) VALUES (?, ?);");
			ps.setString(1, storeEmpID);
			ps.setString(2, storeID);
			ps.execute();
			conn.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return "adding new Store-Emp success";
	}

	/**
	 * This method responsible to save store manager in db
	 * 
	 * @param storeMngID
	 *            - store manager id
	 * @param storeID
	 *            - store id
	 * @return - validation string
	 */
	private String addNewStoreMng(String storeMngID, String storeID) {
		Connection conn = null;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			ps = conn.prepareStatement("INSERT INTO `sys`.`storemanager` (`userName`, `s_id`) VALUES (?, ?);");
			ps.setString(1, storeMngID);
			ps.setString(2, storeID);
			ps.execute();
			conn.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return "adding new Store-Mng success";
	}

	/**
	 * This method responsible to delete store emlpoyee from db
	 * 
	 * @author Oren
	 * @param storeEmpID
	 *            - store employee id
	 */
	private void deleteStoreEmp(String storeEmpID) {
		Connection conn = null;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			ps = conn.prepareStatement("DELETE FROM `sys`.`storeemployee` WHERE `userName`=?;");
			ps.setString(1, storeEmpID);
			ps.execute();
			conn.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * This method respsonsible to delete store manager from db
	 * 
	 * @author Oren
	 * @param storeEmpID
	 *            - store manager id
	 */
	private void deleteStoreMng(String storeEmpID) {
		Connection conn = null;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			ps = conn.prepareStatement("DELETE FROM `sys`.`storemanager` WHERE `userName`=?;");
			ps.setString(1, storeEmpID);
			ps.execute();
			conn.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * This method invoke when customer purchase new order with refund payment type
	 * and update the refund after charge in the db.
	 * 
	 * @author Oren
	 * @param storeID
	 * @param customerID
	 * @param refundAmount
	 * @return - validation string
	 */
	private String updateCustomerRefund(String storeID, String customerID, float refundAmount) {
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			ps = conn.prepareStatement("UPDATE customerinstore SET refund=? WHERE c_ID=? AND s_ID=?");
			ps.setFloat(1, refundAmount);
			ps.setString(2, customerID);
			ps.setString(3, storeID);
			ps.executeUpdate();
			conn.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return "Refund Updated";
	}

	/**
	 * update existing complaint and refund customer
	 * 
	 * @param msg-
	 *            array of the parameters to complaint - string,int,string , string
	 * @return- string if succeed
	 */
	@SuppressWarnings("unchecked")
	private String updateComplaintInDB(Object msg) {
		Connection conn = null;
		PreparedStatement ps;
		String complaintID = (String) ((ArrayList<Object>) msg).get(0);
		int compensateAmount = (int) (float) ((ArrayList<Object>) msg).get(1);
		String customerID = (String) ((ArrayList<Object>) msg).get(2);
		String storeID = (String) ((ArrayList<Object>) msg).get(3);
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			ps = conn.prepareStatement("UPDATE sys.complaints SET status=1, compensateAmount=? WHERE complaint_id=?");
			ps.setInt(1, compensateAmount);
			ps.setString(2, complaintID);
			ps.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		float refund = getCustomerRefund(customerID, storeID) + compensateAmount;
		updateRefundAfterCompensate(customerID, storeID, refund);
		return "ok";
	}

	/**
	 * updating refund to customer in store
	 * 
	 * @param customerID
	 * @param storeID
	 * @param refund
	 */
	private void updateRefundAfterCompensate(String customerID, String storeID, float refund) {
		Connection conn = null;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			ps = conn.prepareStatement("UPDATE sys.customerinstore SET refund=? WHERE c_ID=? and s_ID=?;");
			ps.setFloat(1, refund);
			ps.setString(2, customerID);
			ps.setString(3, storeID);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * this methos search from all the orders that was made in the w in the store,
	 * all the products that was order, and insert them into an array this array is
	 * the array of types for the report, then it will upload it to the data base
	 * 
	 * @author sagi arieli
	 * @param storeID
	 * @param q
	 *            quarter
	 * @param year
	 */
	static void setOrderReportToDB(String storeID, String q, int year) {
		ArrayList<ItemInOrderReport> arr = new ArrayList<ItemInOrderReport>();
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		ResultSet prs;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			ps = conn.prepareStatement("Select * From orders WHERE storeID=?");

			ps.setString(1, storeID);
			rs = ps.executeQuery();
			System.out.println("" + year);
			while (rs.next()) // for each Order
			{ // check quarter
				int oID = rs.getInt(1);
				if ((rs.getNString(4).split("-"))[0].equals("" + year)) {
					if (Integer
							.parseInt((rs.getString(4).split("-"))[1]) <= (Integer.parseInt(quarterToMonths(q).get(1)))
							&& Integer.parseInt(
									(rs.getString(4).split("-"))[1]) >= (Integer.parseInt(quarterToMonths(q).get(0)))) {
						// is it in the quarter| now search all the poroducts in the order
						ps = conn.prepareStatement("SELECT * from ordered_products WHERE orderID=?");// products
						ps.setString(1, "" + oID);
						prs = ps.executeQuery();
						while (prs.next()) {
							boolean found = false;
							for (ItemInOrderReport item : arr) // search the type in the aray
							{
								if (item.getType().equals(prs.getString(7))) {
									found = true;
									item.setOrderCount(item.getOrderCount() + prs.getInt(6));
									item.setProfit(item.getProfit() + prs.getInt(6) * prs.getFloat(5));
								}
							}
							if (!found) // if not found then ad
							{
								arr.add(new ItemInOrderReport(prs.getString(7), prs.getInt(6), prs.getFloat(5)));
							}
						}
						ps = conn.prepareStatement("SELECT * from ordered_selfmade WHERE orderID=?"); // self-made
						ps.setString(1, "" + oID);
						prs = ps.executeQuery();
						while (prs.next()) {
							boolean found = false;
							for (ItemInOrderReport item : arr) // search the type in the aray
							{
								if (item.getType().equals(prs.getString(3))) {
									found = true;
									item.setOrderCount(item.getOrderCount() + prs.getInt(5));
									item.setProfit(item.getProfit() + prs.getInt(5) * prs.getFloat(4));
								}
							}
							if (!found) // if not found then ad
							{
								arr.add(new ItemInOrderReport(prs.getString(3), prs.getInt(5), prs.getFloat(4)));
							}
						}
					}
				}
			}

			conn.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		// if(arr.isEmpty())arr.add(new ItemInOrderReport ("error",0,0));
		/*
		 * here I will add the ilst to the DB
		 */

		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			for (ItemInOrderReport i : arr) {

				ps = conn.prepareStatement(
						"INSERT INTO `sys`.`orderreport` (`storeID`, `quarter`, `year`, `type`, `count`, `profit`) VALUES (?, ?, ?, ?,?, ?)");
				ps.setString(1, storeID);
				ps.setString(2, q);
				ps.setString(3, "" + year);
				ps.setString(4, i.getType());
				ps.setInt(5, i.getOrderCount());
				ps.setFloat(6, i.getProfit());
				ps.execute();
			}
			conn.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * check if date in db is in the range od=f quater q
	 * 
	 * @param year
	 * @param q
	 * @param dateInDB
	 * @return true of false
	 */

	private static boolean isInRange(String year, String q, String dateInDB) {
		boolean flag = false;
		if ((dateInDB.split("-"))[0].equals(year)) {
			if (Integer.parseInt((dateInDB.split("-"))[1]) <= (Integer.parseInt(quarterToMonths(q).get(1)))
					&& Integer.parseInt((dateInDB.split("-"))[1]) >= (Integer.parseInt(quarterToMonths(q).get(0)))) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * update customer refund after he asked to cancle the order. and order status
	 * 
	 * @param order
	 * @param i
	 * @return string if succeeded
	 */
	private String cancelOrder(Order order, Float i) {
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		float orderRefund = order.getPrice() * i;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			ps = conn.prepareStatement("UPDATE orders SET orderStatus=2 , refund=? WHERE orderID=?");
			ps.setInt(2, order.getOrderID()); // instead first ? = name
			ps.setFloat(1, orderRefund);
			ps.executeUpdate();
			System.out.println("Server here---order ID:" + order.getOrderID());
			float currentCustomerRefund = getCustomerRefund(order.getCustomerID(), order.getStoreID());
			ps = conn.prepareStatement("UPDATE customerinstore SET refund=? WHERE c_ID=? AND s_ID=?");
			ps.setString(2, order.getCustomerID());
			ps.setString(3, order.getStoreID());
			ps.setFloat(1, currentCustomerRefund + orderRefund);
			ps.executeUpdate();
			System.out.println("Order refund=" + orderRefund);
			System.out.println("old customer refund=" + currentCustomerRefund);
			System.out.println("new customer refund=" + (currentCustomerRefund + orderRefund));
			conn.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return "Order canceled!";
	}

	/**
	 * take from db cusotmer's refund
	 * 
	 * @param customerID
	 * @param storeID
	 * @return refund- float
	 */
	private float getCustomerRefund(String customerID, String storeID) {
		Connection conn = null;
		ResultSet rs;
		PreparedStatement ps;
		float customerRefund = 0;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			ps = conn.prepareStatement("SELECT refund FROM customerinstore  WHERE c_ID=? AND s_ID=?");
			ps.setString(1, customerID);
			ps.setString(2, storeID);
			rs = ps.executeQuery();
			rs.next();
			customerRefund = rs.getFloat(1);
			conn.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		System.out.println("customer refund====" + customerRefund);
		return customerRefund;
	}

	/**
	 * get all the customer's ins stores details
	 * 
	 * @return array of customers on store
	 */
	private ArrayList<Object> getCustomersFromDB() {
		ArrayList<Object> to_ret = new ArrayList<Object>();
		to_ret.add("receive customers in store");
		ArrayList<CustomerinStore> toAdd = new ArrayList<CustomerinStore>();
		CustomerinStore c;
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ps = conn.prepareStatement("Select * From `sys`.`customerinstore`;");
			rs = ps.executeQuery();
			while (rs.next()) {
				c = new CustomerinStore(rs.getNString(1), rs.getNString(2));
				toAdd.add(c);
			}
			conn.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		to_ret.add(toAdd);
		return to_ret;
	}

	/**
	 * returns all the open complaints
	 * 
	 * @param msg
	 * @return array of complaints
	 */
	private ArrayList<Complaint> getComplaintFromDB(CustomerServiceEmployee msg) {
		ArrayList<Complaint> toRet = new ArrayList<Complaint>();
		Complaint c;
		String time;
		String[] splitTime;
		int count = 0;
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ps = conn.prepareStatement("Select * From complaints WHERE custServEmpID=? and status=0");
			ps.setString(1, msg.getName()); // instead first ? = name
			rs = ps.executeQuery(); // rs=user details
			while (rs.next()) {
				count++;
				time = rs.getNString(5);
				splitTime = time.split(":");
				c = new Complaint(rs.getNString(2), rs.getNString(4), rs.getNString(3), rs.getNString(6),
						Integer.parseInt(splitTime[0]), Integer.parseInt(splitTime[1]));
				c.set_complaintID(rs.getInt(1));
				toRet.add(c);
			}
			if (count == 0) {
				c = new Complaint();
				toRet.add(c);
			}
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return toRet;
	}

	/**
	 * @author sagi arieli return all stores from DB
	 * @return all stores
	 */
	static ArrayList<Store> getStores() {
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		ArrayList<Store> stores = new ArrayList<Store>();

		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			ps = conn.prepareStatement("Select * From stores");
			rs = ps.executeQuery();
			while (rs.next()) {
				stores.add(new Store(rs.getNString(1), rs.getNString(2)));
			}
			System.out.println(stores);
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return stores;
	}

	/**
	 * @author sagi arieli this method recive a quarter and returns the months it
	 *         aplplys to
	 * @param Q
	 *            in the form of "1" or "Q1", any thing else will return "-2""-1" as
	 *            months
	 * @return array list of 2 strings, first one is the starting month of the Q and
	 *         the second is the ending month, for example q1->"1","3"
	 * 
	 */
	private static ArrayList<String> quarterToMonths(String Q) {
		ArrayList<String> arr = new ArrayList<String>();
		switch (Q) {
		case "1": {
			arr.add("1");
			arr.add("3");
			return arr;
		}
		case "2": {
			arr.add("4");
			arr.add("6");
			return arr;
		}
		case "3": {
			arr.add("7");
			arr.add("9");
			return arr;
		}
		case "4": {
			arr.add("10");
			arr.add("12");
			return arr;
		}
		case "Q1": {
			arr.add("1");
			arr.add("3");
			return arr;
		}
		case "Q2": {
			arr.add("4");
			arr.add("6");
			return arr;
		}
		case "Q3": {
			arr.add("7");
			arr.add("9");
			return arr;
		}
		case "Q4": {
			arr.add("10");
			arr.add("12");
			return arr;
		}

		}
		arr.add("-2");
		arr.add("-1");
		return arr;
	}

	/**
	 * @author sagi arieli, ron wexler this methods receive year quarter and store
	 *         and make a new report for the requested time then save the report in
	 *         the DB
	 * @param storeID
	 *            - store id
	 * @param q
	 *            - quarter
	 * @param year
	 *
	 */
	static void setIncomeReportToDB(String storeID, String q, String year) {
		float income = 0;
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			System.out.println(storeID + " " + year + " " + q);
			ps = conn.prepareStatement("Select * From orders WHERE storeID=?");
			ps.setString(1, storeID); // instead first ? = name
			rs = ps.executeQuery(); // rs=user details
			while (rs.next()) {
				System.out.println((rs.getString(4).split("-"))[0]);
				if ((rs.getNString(4).split("-"))[0].equals(year)) {
					if (Integer
							.parseInt((rs.getString(4).split("-"))[1]) <= (Integer.parseInt(quarterToMonths(q).get(1)))
							&& Integer.parseInt(
									(rs.getString(4).split("-"))[1]) >= (Integer.parseInt(quarterToMonths(q).get(0)))) // is
																														// it
																														// in
																														// the
																														// quarter

						income += (rs.getFloat(11) - rs.getFloat(12));
				}
			}

			/*
			 * insert a report
			 */
			ps = conn.prepareStatement(
					"INSERT INTO `sys`.`incomereport` (`storeID`, `year`, `quarter`, `income`) VALUES (?, ?, ?, ?);");
			ps.setString(1, storeID); // instead first ? = name
			ps.setString(2, year);
			ps.setString(3, q);
			ps.setFloat(4, income);
			ps.execute();
			conn.close();

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * This method responsible to search for a store manager
	 * 
	 * @param s
	 *            - store manager
	 * @return - store manger details
	 */
	private StoreManager find_sMngr(StoreManager s) {
		StoreManager ret = null;
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			ps = conn.prepareStatement("Select * From storemanager WHERE userName=?;");
			ps.setString(1, s.getName()); // instead first ? = name
			rs = ps.executeQuery(); // rs=user details

			rs.next();
			ret = new StoreManager(rs.getNString(1), s.getPass());
			ret.setS_ID(rs.getNString(2));

			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return ret;
	}

	/**
	 * edit existing discount
	 * 
	 * @author sagi arieli
	 * @param discount
	 */
	private void editDiscount(Discount discount) {
		Connection conn = null;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			ps = conn.prepareStatement("UPDATE discounts SET discount=? WHERE sID=? AND pID=?");
			ps.setFloat(1, discount.getDis());
			ps.setString(2, discount.getStoreId());
			ps.setString(3, discount.getProductID());

			ps.executeUpdate();
			conn.close();

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	/**
	 * @author sagi arieli add a new discount to the BD
	 * @param discount
	 *            - yes
	 */
	private void addDiscount(Discount discount) {
		Connection conn = null;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			ps = conn.prepareStatement("INSERT INTO `sys`.`discounts` (`sID`, `pID`, `discount`) VALUES (?,?,?);");
			ps.setString(1, discount.getStoreId());
			ps.setString(2, discount.getProductID());
			ps.setFloat(3, discount.getDis());
			ps.execute();
			conn.close();

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	/**
	 * This method responsible to save complaint in db
	 * 
	 * @param c
	 *            - complaint
	 */
	private void saveComplaintToDB(Complaint c) {
		Connection conn = null;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			ps = conn.prepareStatement("INSERT INTO `sys`.`complaints` (`custServEmpID`, `storeID`,"
					+ " `customerID`, `createdTime`, `description`, `createdDate`,`status`,`compensateAmount`) "
					+ "VALUES (?, ?, ?, ?, ?,?,?,?);\r\n" + ";");
			ps.setString(1, c.get_customerServiceEmployeeID());
			ps.setString(2, c.get_storeID());
			ps.setString(3, c.get_customerID());
			ps.setString(4, c.get_time());
			ps.setString(5, c.get_complaintDesc());
			ps.setString(6, c.getDate().toString());
			ps.setBoolean(7, c.is_status());
			ps.setInt(8, c.get_compensateAmount());
			ps.execute();
			System.out.println("Complaint Stored in db");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * remove the discount from DB
	 * 
	 * @param s_id
	 *            discount's store id
	 * @param p_id
	 *            discount's products id
	 */
	private void removeDiscount(String s_id, String p_id) {
		Connection conn = null;
		PreparedStatement ps;
		try {
			System.out.println("delete " + s_id + "  " + p_id);
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			ps = conn.prepareStatement("DELETE from discounts WHERE sID=? AND pID=?;");
			ps.setString(1, s_id);
			ps.setString(2, p_id);
			ps.executeUpdate();
			conn.close();

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * find store employee in DB
	 * 
	 * @param msg
	 *            the store employee
	 * @return the store employee with its store from DB
	 */
	private StoreEmployee find_sEmployee(StoreEmployee s) {
		StoreEmployee ret = null;

		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			ps = conn.prepareStatement("Select * From storeemployee WHERE userName=?;");
			ps.setString(1, s.getName()); // instead first ? = name
			rs = ps.executeQuery(); // rs=user details

			rs.next();
			ret = new StoreEmployee(rs.getNString(1), s.getPass());
			ret.setS_ID(rs.getNString(2));

			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return ret;
	}

	/**
	 * 
	 * @param userName
	 *            - username of user that we want to change his permission.
	 * @param permission
	 *            - the new permission we want to update.
	 * @author Oren
	 */
	private void changePermission(String userName, String permission) {
		Connection conn = null;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			if ((fromStringOfPermissionToInt(permission)) == 8) {
				ps = conn.prepareStatement("UPDATE users SET is_blocked=? WHERE user_Name=?");
				ps.setInt(1, 1);
				ps.setString(2, userName);
			} else if ((fromStringOfPermissionToInt(permission)) == 9) {
				ps = conn.prepareStatement("UPDATE users SET is_blocked=? WHERE user_Name=?");
				ps.setInt(1, 0);
				ps.setString(2, userName);
			} else {
				ps = conn.prepareStatement("UPDATE users SET user_Permission=? WHERE user_Name=?");
				ps.setInt(1, fromStringOfPermissionToInt(permission)); // instead first ? = name
				ps.setString(2, userName);
			}
			ps.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param userName
	 *            - the user that we checks if is active before we change his
	 *            permission.
	 * @author Oren
	 * @return isActive - if the user that we checks is active right now.
	 */
	private int findIsActive(String userName) {
		int isActive = 0;
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			ps = conn.prepareStatement("Select users.is_Active From users WHERE user_Name=?;");
			ps.setString(1, userName); // instead first ? = name
			rs = ps.executeQuery(); // rs=user details
			rs.next();
			isActive = rs.getInt(1);
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isActive;
	}

	/**
	 * This method responsible to set user as inactive when user disconnected
	 * 
	 * @param user
	 *            - the user
	 */
	private void user_inactive(User user) {
		Connection conn = null;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			ps = conn.prepareStatement("UPDATE users SET is_Active=0 WHERE user_Name=?");
			ps.setString(1, user.getName()); // instead first ? = name
			ps.executeUpdate();
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * This method responsible to search user in db and check whether he try to
	 * connect while he is already logged in
	 * 
	 * @param user
	 *            - the user
	 * @return the user has logged in after set isActive=1 in the db
	 */
	private User find_user(User user) {
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			ps = conn.prepareStatement("Select * From users WHERE user_Name=? AND user_Password=?;");
			ps.setString(1, user.getName()); // instead first ? = name
			ps.setString(2, user.getPass());
			rs = ps.executeQuery(); // rs=user details

			if (!rs.next()) {
				user.setLogin(User.permission.rejected);
				return user;
			} else if (rs.getInt(4) == 1) {
				user.setActive(1);
			} else if (rs.getInt(5) == 1) {
				user.setBlocked(1);
				return user;
			}
			PreparedStatement pss = conn.prepareStatement("UPDATE users SET is_Active=1 WHERE user_Name=? ");
			pss.setString(1, user.getName());
			pss.executeUpdate();
			user.setLogin(enum_adapter((rs.getInt(3))));
			user.setStLogin(user.getLogin());
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return user;
	}

	/**
	 * This method gets the order that the user chose in the order's gui and insert
	 * the details to the DB.
	 * 
	 * @param order
	 *            : the new order that the customer ordered right now.
	 * @author Oren
	 */
	private Order addNewOrderToDB(Order order) {
		System.out.println("SERVER " + order.getCustomerID());
		Connection conn = null;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			if (order.getDeliveryOption() == Order.deliveryType.pickup) // if the customer chose self-pickup
			{
				ps = conn.prepareStatement(
						"INSERT INTO `sys`.`orders` (`storeID`, `customerID`, `orderDate`, `greetingCard`, `delivery`, `requirementDate`, `requirementTime`, `price`, `orderStatus`, `refund`, `paymentType`) "
								+ "VALUES ( ?, ?, ?, ?, '0', ?, ?, ?, ?, ?, ?);");

				ps.setString(1, order.getStoreID());
				ps.setString(2, order.getCustomerID());
				ps.setString(3, order.getOrderDate());
				ps.setString(4, order.getGreetingCard());
				ps.setString(5, order.getRequirementDate());
				ps.setString(6, order.getRequirementTime());
				ps.setFloat(7, order.getPrice());
				ps.setInt(8, fromOrderStatusToInteger(order.getOrderStatusOption()));
				ps.setFloat(9, order.getRefund());
				ps.setInt(10, order.getPaymentType());
			} else // if customer chose delivery
			{
				ps = conn.prepareStatement(
						"INSERT INTO `sys`.`orders` (`storeID`, `customerID`, `orderDate`, `greetingCard`, `delivery`, `deliveryRecipient`, `deliveryAddress`, `requirementDate`, `requirementTime`, `price`, `orderStatus`, `refund`,`tel`, `paymentType`) "
								+ "VALUES (?, ?, ?, ?, '1', ?, ?, ?, ?, ?, ?, ?, ?, ?);");
				ps.setString(1, order.getStoreID());
				ps.setString(2, order.getCustomerID());
				ps.setString(3, order.getOrderDate());
				ps.setString(4, order.getGreetingCard());
				ps.setString(5, order.getDeliveryRecipient());
				ps.setString(6, order.getDeliveryAddress());
				ps.setString(7, order.getRequirementDate());
				ps.setString(8, order.getRequirementTime());
				ps.setFloat(9, order.getPrice());
				ps.setInt(10, fromOrderStatusToInteger(order.getOrderStatusOption()));
				ps.setFloat(11, order.getRefund());
				ps.setString(12, order.getTel());
				ps.setInt(13, order.getPaymentType());
			}
			ps.execute();
			numOfOrders++;
			order.setOrderID(numOfOrders);
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!order.getOrderedProducts().isEmpty()) {
			insertOrderedProducts(order.getStoreID(), order.getOrderID(), order.getOrderedProducts());
		}
		if (!order.getSelfMadeProducts().isEmpty()) {
			insertOrderedSelfMade(order.getStoreID(), order.getOrderID(), order.getSelfMadeProducts());
		}
		return order;
	}

	/**
	 * This method insert to db ordered self-made products
	 * 
	 * @param orderID
	 * @param selfMadeProducts
	 * @author Oren
	 */
	private void insertOrderedSelfMade(String storeID, int orderID, ArrayList<SelfMadeProduct> selfMadeProducts) {
		Connection conn = null;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			for (SelfMadeProduct selfmade : selfMadeProducts) {
				for (Flower f : selfmade.getFlowers()) {
					ps = conn.prepareStatement("INSERT INTO ordered_selfmade values (?,?,?,?,?,?,?,?,?,?);");
					ps.setInt(1, orderID);
					ps.setString(2, selfmade.getId());
					ps.setString(3, selfmade.getName());
					ps.setFloat(4, selfmade.getPrice());
					ps.setInt(5, selfmade.getQuant());
					ps.setInt(6, f.get_fID());
					ps.setString(7, f.get_fName());
					ps.setString(8, f.get_fColor());
					ps.setInt(9, f.getQuant());
					ps.setInt(10, f.get_fPrice());
					ps.execute();
				}
			}
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Ordered selfmade save in DB");
	}

	/**
	 * This method insert to db ordered catalog products
	 * 
	 * @param orderID
	 * @param orderedProducts
	 * @author Oren
	 */
	private void insertOrderedProducts(String storeID, int orderID, ArrayList<Product> orderedProducts) {
		Connection conn = null;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			for (Product prod : orderedProducts) {
				ps = conn.prepareStatement("INSERT INTO ordered_products values (?,?,?,?,?,?,?);");
				ps.setInt(1, orderID);
				ps.setString(2, storeID);
				ps.setString(3, prod.get_product_ID());
				ps.setString(4, prod.getName());
				ps.setFloat(5, prod.getPrice());
				ps.setInt(6, prod.getQuant());
				ps.setString(7, prod.getpType());// added by sagi for reports
				ps.execute();
			}
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Ordered products save in DB");
	}

	/**
	 * This method responsible for remove product from the catalog by network
	 * employee
	 * 
	 * @author Oren
	 * @param product
	 *            - the product
	 */
	private void removeProductFromCatalogDB(Product product) {
		System.out.println("SERVER " + product.get_product_ID());
		Connection conn = null;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			ps = conn.prepareStatement("DELETE FROM `sys`.`product` WHERE Product_ID=?");

			ps.setString(1, product.get_product_ID());
			ps.executeUpdate();
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();//
		}
	}

	/***
	 * this proc add new survey to data base
	 * 
	 * @param msg-
	 *            array of strings
	 */

	private void AddSurveyToDB(Survey msg) {
		int survey_num = 0;

		System.out.println("in server: size =" + msg.getAnswer() + "sore" + msg.getStoreID());

		Connection conn = null;
		PreparedStatement ps;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {

			ps = conn.prepareStatement(
					"INSERT INTO `sys`.`survey` (`date` , `q1` , `q2`, `q3`, `q4`, `q5`,`q6`,`storeID`)"
							+ "VALUE (?,?,?,?,?,?,?,?);");
			ps.setString(1, msg.getCreatedDate());
			ps.setInt(2, msg.getAnswer()[0]);
			ps.setInt(3, msg.getAnswer()[1]);
			ps.setInt(4, msg.getAnswer()[2]);
			ps.setInt(5, msg.getAnswer()[3]);
			ps.setInt(6, msg.getAnswer()[4]);
			ps.setInt(7, msg.getAnswer()[5]);
			ps.setString(8, msg.getStoreID());

			Statement stmt;

			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT surveyID FROM survey");

			while (rs.next())
				survey_num = rs.getInt(1);
			numOfSurvey = survey_num;

			System.out.println("num of surverys= " + (++numOfSurvey));

			ps.executeUpdate();
			// ps2.executeUpdate();

			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * This method responsible for adding new product to the catalog by the network
	 * manager
	 * 
	 * @author Oren
	 * @param product
	 */
	private void AddProductFromCatalogDB(Product product) {
		System.out.println("SERVER " + product.get_product_ID());
		Connection conn = null;
		PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {

			ps = conn.prepareStatement(
					"INSERT INTO `sys`.`product` (`product_ID` , `Product_Name`, `Product_Type`, `Product_Price`, `Product_Description`,`Product_Image`)"
							+ "VALUE (?,?,?,?,?,?);");

			ps.setString(1, product.get_product_ID());
			ps.setString(2, product.get_product_Name());
			ps.setString(3, product.get_product_Type());
			ps.setFloat(4, product.getPrice());
			ps.setString(5, product.getProductDesc());
			if (product.getProductImage().length != 0) {
				InputStream is = new ByteArrayInputStream(product.getProductImage());
				ps.setBlob(6, is);
			} else
				ps.setBytes(6, null);

			ps.executeUpdate();

			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * This method responsible to find cutomer in db and return cutomer with all his
	 * details
	 * 
	 * @author Oren
	 * @param customer
	 *            - with id and password
	 * @return - customer with updated details
	 */
	private Customer find_customer(Customer customer) {
		Connection conn = null;
		PreparedStatement ps, ps1;
		ResultSet rs, rs1;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			ps = conn.prepareStatement("Select * From customerinstore WHERE c_ID=?;");
			ps.setString(1, customer.getName()); // instead first ? = name
			rs = ps.executeQuery(); // rs=user details

			rs.next();
			customer.setName(rs.getNString(1));
			customer.setStoreID(rs.getNString(2));
			customer.setRefundAmount(rs.getFloat(3));
			customer.setMemberOption(findMemberType(rs.getInt(4)));
			customer.setCreatedDate(rs.getNString(5));

			ps1 = conn.prepareStatement("Select * From customers WHERE userName=?;");
			ps1.setString(1, customer.getName()); // instead first ? = name
			rs1 = ps1.executeQuery(); // rs=user details
			rs1.next();
			customer.setCreditNumber(rs1.getNString(2));

			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return customer;
	}

	/**
	 * @author sagi arieli
	 * @param customerName
	 *            = costumer id this method returns the stores that the customer is
	 *            enlisted to
	 * @return array list of stores
	 * 
	 */
	private ArrayList<CustomerinStore> getCustomerStores(String customerName) {
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		ArrayList<CustomerinStore> stores = new ArrayList<CustomerinStore>();

		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			ps = conn.prepareStatement("Select * From customerinstore WHERE c_ID=?;");
			ps.setString(1, customerName);
			rs = ps.executeQuery();
			while (rs.next()) {
				stores.add(new CustomerinStore(rs.getNString(2), rs.getFloat(3)));
			}
			if (stores.isEmpty()) {
				stores.add(new CustomerinStore("-999", "-999"));
				return stores;
			}
			for (CustomerinStore s : stores) {
				ps = conn.prepareStatement("Select * From stores WHERE Store_ID=?;");
				ps.setString(1, s.getS_ID());
				rs = ps.executeQuery();
				rs.next();
				s.setS_name(rs.getNString(2));

			}
			System.out.println(stores);
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return stores;

	}

	/**
	 * function called from client and asks for flowers from the data base
	 * 
	 * @return array list of flower from the data base
	 */
	private ArrayList<Flower> getFlowerFromDB() {
		Connection conn = null;
		PreparedStatement ps;
		ResultSet rs;
		String name, color;
		int id, price;
		ArrayList<Flower> toRet = new ArrayList<Flower>();
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			ps = conn.prepareStatement("Select * From flowers ;");
			rs = ps.executeQuery();
			while (rs.next()) {
				name = rs.getNString(1);
				id = rs.getInt(2);
				color = rs.getNString(3);
				price = rs.getInt(4);
				toRet.add(new Flower(name, id, color, price));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toRet;
	}

	// looking for the network employee from the user table
	/**
	 * @author Oren
	 * @param i-
	 *            the order status int the DB
	 * @return the status in orderStatus enum in Order class
	 */
	private Order.orderStatus findOrderStatus(int i) {
		switch (i) {
		case 0:
			return Order.orderStatus.ordered;
		case 1:
			return Order.orderStatus.picked_up;
		case 2:
			return Order.orderStatus.cancelled;
		default:
			return Order.orderStatus.ordered;
		}
	}

	/**
	 * This method responsible to convert int to enum in delivery type
	 * 
	 * @author Oren
	 * @param i
	 *            - The delivery type in DB
	 * @return the type of deliveryType enum in Order class
	 */
	private Order.deliveryType findDeliveryType(int i) {
		switch (i) {
		case 0:
			return Order.deliveryType.pickup;
		case 1:
			return Order.deliveryType.delivery;
		default:
			return Order.deliveryType.pickup;
		}
	}

	/**
	 * This method responsible This method responsible to convert int to enum in
	 * delivery type
	 * 
	 * @param i
	 * @return
	 */
	private Customer.memberType findMemberType(int i) {
		switch (i) {
		case 0:
			return Customer.memberType.regular;
		case 1:
			return Customer.memberType.monthlyMember;
		case 2:
			return Customer.memberType.yearlyMember;
		default:
			return Customer.memberType.regular;
		}
	}

	/**
	 * This method responsible to convert enum to int in permission types
	 * 
	 * @author Oren
	 * @param i-
	 *            The permission type in DB
	 * @return The type of permission in User class
	 */
	private User.permission enum_adapter(int i) {
		switch (i) {
		case 0:
			return User.permission.rejected;
		case 1:
			return User.permission.customer;
		case 2:
			return User.permission.store_emp;
		case 3:
			return User.permission.store_mng;
		case 4:
			return User.permission.network_emp;
		case 5:
			return User.permission.network_mng;
		case 6:
			return User.permission.customer_service_emp;
		case 7:
			return User.permission.sys_mng;
		case 8:
			return User.permission.blocked_user;
		case 9:
			return User.permission.unblock_user;
		default:
			return User.permission.rejected;

		}
	}

	/**
	 * This method responsible to pull from the data base all the users
	 * 
	 * @return - arraylist of users
	 */
	private ArrayList<User> getUsersList() {
		ArrayList<User> users = new ArrayList<User>();
		Connection conn = null;
		ResultSet rs;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			System.out.println("SQL connection succeed");
			PreparedStatement ps = conn.prepareStatement("Select * From sys.users;");
			rs = ps.executeQuery(); // rs=user details
			while (rs.next()) {
				System.out.println(rs.getString(1));
				User singleUser = new User(rs.getString(1), rs.getString(2));
				singleUser.setLogin(enum_adapter(rs.getInt(3)));
				singleUser.setStLogin(singleUser.getLogin());
				singleUser.setBlocked(rs.getInt(5));
				users.add(singleUser);
			}
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}

	/**
	 * This methods responsible to pull from the database all the products in the
	 * catalog
	 * 
	 * @return - arraylist of products
	 */
	private ArrayList<Product> getProducts()// get products from data base
	{
		ArrayList<Product> to_ret = new ArrayList<Product>();
		Connection conn = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			System.out.println("SQL connection succeed");
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		Statement stmt;
		String productId, productName, productType;
		float productPrice;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("Select * From sys.product");
			Blob blob = null;
			while (rs.next())// rs has all the columns from the specify index
			{
				productId = rs.getNString(1);
				productName = rs.getNString(2);
				productType = rs.getNString(3);
				productPrice = rs.getFloat(4);
				Product p = new Product(productId, productName, productType, productPrice);
				p.setProductDesc(rs.getNString(5));
				p.setpType(productType);
				blob = rs.getBlob(6);
				if (blob == null) {
					p.setProductImage(null);
				} else {
					InputStream inputStream = blob.getBinaryStream();
					byteArrayOutputStream = new ByteArrayOutputStream();
					int inByte;

					while ((inByte = inputStream.read()) != -1) {
						byteArrayOutputStream.write(inByte);
					}

					byte[] returnBytes = byteArrayOutputStream.toByteArray();
					p.setProductImage(returnBytes);
				}
				to_ret.add(p);
			}

		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("In Fuction: " + to_ret);
		return to_ret;
	}

	/**
	 * This method gets a customer and store id and extract all the orders of the
	 * customer in this store
	 * 
	 * @author Oren
	 * @param customerID,
	 *            storeID
	 * @return array of orders of the customer in this store with their ordered
	 *         items
	 */
	private ArrayList<Order> getCustomerOrders(String customerID, String storeID) {
		ArrayList<Order> custOrders = new ArrayList<Order>();
		Connection conn = null;
		ResultSet rs;
		int count = 0;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			System.out.println("SQL connection succeed");
			PreparedStatement ps = conn.prepareStatement("Select * From sys.orders WHERE storeID=? AND customerID=?;");
			ps.setString(1, storeID);
			ps.setString(2, customerID); // instead first ? = name
			rs = ps.executeQuery(); // rs=user details
			while (rs.next()) {
				count++;
				// System.out.println(rs.getString(1));
				Order singleOrder = new Order(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), findDeliveryType(rs.getInt(6)), rs.getString(7), rs.getString(8),
						rs.getString(9), rs.getString(10), rs.getFloat(11), findOrderStatus(rs.getInt(12)),
						rs.getFloat(13), rs.getString(14), rs.getInt(15));
				singleOrder.setOrderedProducts(getProductsOfOrder(singleOrder.getOrderID()));
				singleOrder.setSelfMadeProducts(getSelfMadeProductsOfOrder(singleOrder.getOrderID()));
				custOrders.add(singleOrder);
			}
			if (count == 0)
				custOrders.add(new Order());
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("SERVER: first product name of order- " + custOrders.get(0).getOrderedProducts());
		return custOrders;
	}

	/**
	 * This method gets order ID and extract all the self-made products in the db
	 * that connect to this order ID.
	 * 
	 * @author Oren
	 * @param orderID
	 *            - the id of the order that we want to extract all it's products.
	 * @return array with all the self-made product of the specific order
	 */
	private ArrayList<SelfMadeProduct> getSelfMadeProductsOfOrder(int orderID) {
		ArrayList<SelfMadeProduct> arr = new ArrayList<SelfMadeProduct>();
		ArrayList<Flower> flowerArr = new ArrayList<Flower>();
		Flower f;
		String selfID = "";
		String selfType = "";
		float selfprice;
		int selfQuant;
		Connection conn = null;
		ResultSet rs;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			// System.out.println("SQL connection succeed");
			PreparedStatement ps = conn.prepareStatement(
					"SELECT distinct orderID, selfMadeID,name,price,quantity FROM sys.ordered_selfmade where orderID=?;");
			ps.setInt(1, orderID);
			rs = ps.executeQuery(); // rs=user details
			while (rs.next()) {
				selfID = rs.getString(2);
				selfType = rs.getString(3);
				selfprice = rs.getFloat(4);
				selfQuant = rs.getInt(5);
				flowerArr = getFlowersOfSelfMade(orderID, selfID);
				arr.add(new SelfMadeProduct(selfID, selfType, selfQuant, selfprice, flowerArr));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}

	/**
	 * This method extract all the flowers of the self-made product that we choose
	 * into ArrayList
	 * 
	 * @param orderID
	 *            - id of order
	 * @param selfID
	 *            - id of self-made product that we want to add array of his flowers
	 * @return ArrayList of flowers
	 */
	private ArrayList<Flower> getFlowersOfSelfMade(int orderID, String selfID) {
		Flower f;
		ArrayList<Flower> flowerArr = new ArrayList<Flower>();
		Connection conn = null;
		PreparedStatement psf;
		ResultSet rsf;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			// System.out.println("SQL connection succeed");
			psf = conn.prepareStatement("SELECT * FROM sys.ordered_selfmade where orderID=? AND selfMadeID=?;");
			psf.setInt(1, orderID);
			psf.setString(2, selfID);
			rsf = psf.executeQuery();
			while (rsf.next()) {
				f = new Flower(rsf.getInt(6), rsf.getString(7), rsf.getString(8), rsf.getInt(9), rsf.getInt(10));
				flowerArr.add(f);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flowerArr;
	}

	/**
	 * This method gets order ID and extract all the products in the db that connect
	 * to this order ID.
	 * 
	 * @author Oren
	 * @param orderID
	 *            - the id of the order that we want to extract all it's products.
	 * @return array with all the product of the specific order
	 */
	private ArrayList<Product> getProductsOfOrder(int orderID) {
		ArrayList<Product> arr = new ArrayList<Product>();
		Product p;
		Connection conn = null;
		ResultSet rs;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			// System.out.println("SQL connection succeed");
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ordered_products WHERE orderID=?;");
			ps.setInt(1, orderID);
			rs = ps.executeQuery(); // rs=user details
			while (rs.next()) {
				p = new Product(rs.getString(3), rs.getString(4), rs.getFloat(5), rs.getInt(6));
				arr.add(p);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}

	/**
	 * This method responsible to pull from the data base all the discount on a
	 * specific store
	 * 
	 * @param storeID
	 *            - store id
	 * @return - arraylist of discount
	 */
	private ArrayList<Discount> getDiscounts(String storeID)// get products from data base
	{
		ArrayList<Discount> to_ret = new ArrayList<Discount>();
		Connection conn = null;
		ResultSet rs;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			System.out.println("SQL connection succeed");
			PreparedStatement ps = conn.prepareStatement("Select * From sys.discounts WHERE sID=?;");
			ps.setString(1, storeID); // instead first ? = name
			rs = ps.executeQuery(); // rs=user details
			while (rs.next()) {
				System.out.println("getting discounts from store ID:" + storeID);
				System.out.println("Pid:" + rs.getString(2) + "\t discount:" + rs.getString(3) + "%");
				Discount dis = new Discount(rs.getString(1), rs.getString(2), rs.getFloat(3));
				to_ret.add(dis);
			}
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (to_ret.isEmpty())
			to_ret.add(new Discount("-999", "-999", 5));
		return to_ret;
	}

	/**
	 * This method responsible to update product details in db
	 * 
	 * @param msg
	 *            - the product
	 */
	public synchronized void updateProduct(Object msg) {
		Connection conn = null;
		Product p = (Product) msg;
		System.out.println(p);
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ArrayList<String> castedMSG = new ArrayList<String>();
		castedMSG.add(0, p.get_product_Name());
		castedMSG.add(1, p.get_product_ID());
		castedMSG.add(2, p.get_product_Type());
		castedMSG.add(3, "" + p.getPrice());
		castedMSG.add(4, p.getProductDesc());
		byte[] b = p.getProductImage();
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE product SET Product_Name=? WHERE Product_ID=?;");
			ps.setString(1, castedMSG.get(0));// name
			ps.setString(2, castedMSG.get(1));// id
			PreparedStatement ps2 = conn.prepareStatement("UPDATE product SET Product_Type=? WHERE Product_ID=?;");
			ps2.setString(1, castedMSG.get(2));// type
			ps2.setString(2, castedMSG.get(1));// id
			PreparedStatement ps3 = conn.prepareStatement("UPDATE product SET Product_Price=? WHERE Product_ID=?;");
			ps3.setString(1, castedMSG.get(3));// price
			ps3.setString(2, castedMSG.get(1));// id
			PreparedStatement ps4 = conn
					.prepareStatement("UPDATE product SET Product_Description=? WHERE Product_ID=?;");
			ps4.setString(1, castedMSG.get(4));// descrip
			ps4.setString(2, castedMSG.get(1));// id
			PreparedStatement ps5 = conn.prepareStatement("UPDATE product SET Product_Image=? WHERE Product_ID=?;");
			ps5.setBytes(1, b);// image
			ps5.setString(2, castedMSG.get(1));// id

			ps.executeUpdate();
			ps2.executeUpdate();
			ps3.executeUpdate();
			ps4.executeUpdate();
			ps5.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * a method that will set the next date for quarter report (only if today is
	 * after the "next quarter") this methods is here because we have an attribute
	 * that we check every day to see if we need to make a new report we set this
	 * attribute to be in 2016, so we can check the reports, but in the event of a
	 * live build, we done want the report maker to remake old reports
	 */
	private static void setNextQarterDate() throws ParseException {
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
			if (currentStringDate.split("-")[1].equals("10")) {
				Server.nextquarterdate = "" + (Integer.parseInt(currentStringDate.split("-")[0]) + 1) + "-01-01";
			} else {

				Server.nextquarterdate = currentStringDate.split("-")[0] + "-"
						+ (Integer.parseInt(currentStringDate.split("-")[1]) + 3) + "-01";
			}
		}

	}

	/***
	 * initolaize the num of survys that in data base
	 */
	private static void initializeNumOfSurvey() {
		Connection conn = null;
		int count = 0;
		ResultSet rs;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT surveyID FROM survey");

			while (rs.next()) {
				count = rs.getInt(1);
			}
			numOfSurvey = count;
			System.out.println("Number of surveys:" + numOfSurvey);
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * This methods responsible to initialize the num of order in the server
	 * according to num of orders in the data base - occur when the server in up
	 * 
	 * @author Oren and Dor
	 */
	private static void initializeNumOfOrders() {

		Connection conn = null;
		int count = 0;
		int survey_num = 0;
		ResultSet rs;
		ResultSet rs2;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + path, username, password);
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT orderID FROM orders");

			while (rs.next()) {
				count = rs.getInt(1);
			}
			numOfOrders = count;
			System.out.println("Num of orders: " + numOfOrders);
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * This methods sets the db attributes when up
	 * 
	 * @param pathiy
	 * @param user
	 * @param pass
	 */
	public static void setDBAtributers(String pathiy, String user, String pass) {
		path = pathiy;
		username = user;
		password = pass;
	}

	/**
	 * This method responsible to convert enum to int in order status
	 * 
	 * @author Oren
	 * @param oStatus
	 * @return
	 */
	private int fromOrderStatusToInteger(Order.orderStatus oStatus) {
		switch (oStatus) {
		case ordered:
			return 0;
		case picked_up:
			return 1;
		case cancelled:
			return 2;
		}
		return 0;
	}

	/**
	 * This method convert String of permission to Integer
	 * 
	 * @author Oren
	 * @param userPermission
	 * @return
	 */
	private int fromStringOfPermissionToInt(String userPermission) {
		switch (userPermission) {
		case "rejected":
			return 0;
		case "customer":
			return 1;
		case "store_emp":
			return 2;
		case "store_mng":
			return 3;
		case "network_emp":
			return 4;
		case "network_mng":
			return 5;
		case "customer_service_emp":
			return 6;
		case "sys_mng":
			return 7;
		case "blocked_user":
			return 8;
		case "unblock_user":
			return 9;
		default:
			return 0;
		}
	}

}
