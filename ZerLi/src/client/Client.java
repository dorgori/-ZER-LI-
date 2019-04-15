package client;

import java.io.IOException;
import java.util.ArrayList;

import catalog.CatalogController;
import catalog.Discount;
import catalog.Product;
import compensate_customer.Complaint;
import compensate_customer.CustomerServiceEmployee;
import javafx.application.Application;
import javafx.stage.Stage;
import ocsf.client.AbstractClient;
import order.Flower;
import order.Order;
import order.OrderController;
import order.SelfMadeProduct;
import reports.ItemInOrderReport;
import reports.ReportController;
import survey.Survey;
import survey.SurveyController;

/**
 * Describes the Client Class!
 * 
 * @param DEFAULT_PORT
 * @param user
 *            instance of a user
 * @param mail_box
 *            a boolean that switches when the server sends the data to client
 * @param isActive
 *            current status of a user: logged on/off
 * @param host
 *            host ip
 * @param customer
 * @param products
 *            catalog products of an order
 * @param discounts
 *            catalog products in discount
 * @param orders
 *            the orders
 * @param users
 *            array list of users
 * @param flowers
 *            array list of flowers
 * @param orderedSMProd
 *            array list of SelfMadeProduct of an order
 * @param compArr
 *            array list of complaints
 * @param customerInStores
 *            array list of customers in a store
 * @param customersToAdd
 *            Strings of customers that do not belong to a store
 * @param survey_answers
 *            surveys
 * @param complaintReport
 *            reports of complaint
 * @param pro_comment_arr
 *            professional comment
 * @param satisfactionRep
 *            total satisfaction of a store
 * @param stores
 *            list of stores of a customer he enlists to
 * @param allStores
 *            array list of stores
 * @param sEmplyee
 *            instance of Store Employee
 * @param sMngr
 *            instance of Store Manager
 * @param income
 *            total income of a store
 * @param itemsInOrderReport
 *            array list of items ordered for a store
 * @param order
 *            instance of order
 */
public class Client extends AbstractClient {
	final public static int DEFAULT_PORT = 5555;
	public static User user;
	private static Client client;
	private static boolean mail_box = false;
	private static int isActive;
	private static String host = "";
	private static Customer customer;
	private ArrayList<Product> products;
	private ArrayList<Discount> discounts;
	private ArrayList<Order> orders;
	private ArrayList<User> users;
	private ArrayList<Flower> flowers;
	private ArrayList<SelfMadeProduct> orderedSMProd;
	private ArrayList<Complaint> compArr;
	private ArrayList<CustomerinStore> customerInStores;
	private ArrayList<String> customersToAdd;
	private ArrayList<Survey> survey_answers;
	private ArrayList<String> complaintReport;
	private ArrayList<String> pro_comment_arr;
	private ArrayList<Float> satisfactionRep;
	private static ArrayList<CustomerinStore> stores;
	private static ArrayList<Store> allStores;
	private static StoreEmployee sEmplyee;
	private static StoreManager sMngr;
	private static float income = 0;
	private static ArrayList<ItemInOrderReport> itemsInOrderReport;
	public static ArrayList<String> regularCustomers;
	private static Order order;

	/**
	 * The constructor responsible to connect the server
	 * 
	 * @param host
	 *            host name
	 * @param port
	 *            host port
	 */
	public Client(String host, int port) {
		super(host, port); // Call the superclass constructor
		try {
			openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Client getClient() {
		return client;
	}

	public static void setClient(Client client) {
		Client.client = client;
	}

	/**
	 * main method, invokes the sub-class GuiOpener to initiate first launch of gui
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		GuiOpener.init_launch();
	}

	/**
	 * defines a new client and sets the client in the controllers
	 * 
	 * @param ip
	 *            the ip of the host
	 */
	public static void connectToServer(String ip) {
		host = "localhost";
		if (!ip.equals(""))
			host = ip;
		client = new Client(host, DEFAULT_PORT);
		LoginController.setClient(client);
		CatalogController.setClient(client);
		SurveyController.setClient(client);
		OrderController.setClient(client);
		ReportController.setClient(client);
	}

	/**
	 * all messages the sever sends back to the client are handled in this method
	 */
	@SuppressWarnings("unchecked")
	public void handleMessageFromServer(Object msg) {
		// checks all the messages that are instance of ArrayList<?>
		if (msg instanceof ArrayList<?>) {
			// checks all the messages that the first object in the array list is a string
			if (((ArrayList<?>) msg).get(0) instanceof String) {

				if (((ArrayList<?>) msg).get(0).equals("receive customers in store")) {
					((ArrayList<?>) msg).remove(0);
					customerInStores = (((((ArrayList<ArrayList<CustomerinStore>>) msg)).get(0)));
				}

				else if (((ArrayList<?>) msg).get(0).equals("get complaints to report")) {
					((ArrayList<?>) msg).remove(0);
					complaintReport = (((ArrayList<String>) msg));
				}
				
				else if (((ArrayList<?>) msg).get(0).equals("ask for regular customers")) {
					((ArrayList<?>) msg).remove(0);
					regularCustomers = (((ArrayList<String>) msg));
				}
				
				else if (((ArrayList<?>) msg).get(0).equals("recive list of customers to add")) {
					((ArrayList<?>) msg).remove(0);
					customersToAdd = (ArrayList<String>) msg;
				}

				else if (((ArrayList<?>) msg).get(0).equals("pro comment arr")) {
					(((ArrayList<?>) msg)).remove(0);
					pro_comment_arr = ((((ArrayList<String>) msg)));
					System.out.println("array of comments : " + pro_comment_arr);
				}

				else if (((ArrayList<?>) msg).get(0).equals("up date pro comment arr")) {
					(((ArrayList<?>) msg)).remove(0);
					pro_comment_arr = ((((ArrayList<String>) msg)));
					System.out.println("array of comments after update  : " + pro_comment_arr);
				}
			}

			// checks all the messages that the first message of the array list is an*
			// instance of a Class

			else if (((ArrayList<?>) msg).get(0) instanceof Product) {
				products = (ArrayList<Product>) msg;
			}

			else if (((ArrayList<?>) msg).get(0) instanceof SelfMadeProduct) {
				orderedSMProd = (ArrayList<SelfMadeProduct>) msg;
			}

			else if (((ArrayList<?>) msg).get(0) instanceof ItemInOrderReport) {
				if (((ArrayList<ItemInOrderReport>) msg).get(0).getType().equals("error"))
					itemsInOrderReport = null;
				else
					itemsInOrderReport = ((ArrayList<ItemInOrderReport>) msg);
			}

			else if (((ArrayList<?>) msg).get(0) instanceof Discount) {
				if (((ArrayList<Discount>) msg).get(0).getStoreId().equals("-999"))
					discounts = null;
				else
					discounts = (ArrayList<Discount>) msg;
			}

			else if (((ArrayList<?>) msg).get(0) instanceof Order)
				orders = (ArrayList<Order>) msg;

			else if (((ArrayList<?>) msg).get(0) instanceof User)
				users = (ArrayList<User>) msg;

			else if (((ArrayList<?>) msg).get(0) instanceof CustomerinStore) {
				if (((ArrayList<Store>) msg).get(0).getS_ID().equals("-999"))
					stores = null;
				else
					stores = (ArrayList<CustomerinStore>) msg;
			}

			else if (((ArrayList<?>) msg).get(0) instanceof Flower)
				flowers = (ArrayList<Flower>) msg;

			else if (((ArrayList<?>) msg).get(0) instanceof Complaint) {
				compArr = (ArrayList<Complaint>) msg;
			}

			else if (((ArrayList<?>) msg).get(0) instanceof Store) {
				allStores = (ArrayList<Store>) msg;
			}

			else if (((ArrayList<?>) msg).get(0) instanceof Float) {
				satisfactionRep = (ArrayList<Float>) msg;
			}

			else if (((ArrayList<?>) msg).get(0) instanceof Survey) {
				survey_answers = ((((ArrayList<Survey>) msg)));
				System.out.println("array aray: " + survey_answers);
			}
		}

		// checks the messages which aren't an array list but an instance of a Class

		else if (msg instanceof Customer) {
			customer = (Customer) msg;
			System.out.println((Customer) msg);
		}

		else if (msg instanceof StoreEmployee) {
			sEmplyee = (StoreEmployee) msg;
		}

		else if (msg instanceof Order) {
			order = (Order) msg;
		}

		else if (msg instanceof StoreManager) {
			sMngr = (StoreManager) msg;
		}

		else if (msg instanceof User) {
			user = (User) msg;
		}

		else if (msg instanceof Integer) {
			isActive = (Integer) msg;
		}

		else if (msg instanceof Float) {
			income = (Float) msg;
		}

		else if (msg instanceof String) {
			if (((String) msg).equals("no income report")) {
				income = -99999;
			}
		}
		// after all the checks are done and handled. the mail box can be true again and
		// send the message
		// onwards to the controller
		mail_box = true;
	}

	/**
	 * tries to sends a message to server
	 * 
	 * @param message
	 *            a message by client
	 */
	public void handleMessageFromClientUI(Object message) {
		try {
			sendToServer(message);
		} catch (IOException e) {
			System.out.println("Could not send message to server.  Terminating client.");
			quit();
		}
	}

	/**
	 * this method close connection between client and server if needed
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * sends the user to the server
	 * 
	 * @param func_user
	 *            user sent from controller
	 * @return user
	 */
	public User sendLogin(User func_user) {
		handleMessageFromClientUI(func_user);
		waitForMailBox();
		return user;
	}

	/**
	 * sends the customer to the server
	 * 
	 * @param func_customer
	 *            customer sent from controller
	 * @return customer
	 */
	public Customer sendCustomer(Customer func_customer) {
		handleMessageFromClientUI(func_customer);
		waitForMailBox();
		return customer;
	}

	/**
	 * sends the store employee to the server
	 * 
	 * @param s
	 *            store employee sent from controller
	 * @return store employee
	 */
	public StoreEmployee getSE(StoreEmployee s) {
		handleMessageFromClientUI(s);
		waitForMailBox();
		return sEmplyee;
	}

	/**
	 * sends the store manager to the server
	 * 
	 * @param s
	 *            store manager sent from controller
	 * @return store manager
	 */
	public StoreManager getSmngr(StoreManager s) {
		handleMessageFromClientUI(s);
		System.out.println(mail_box);
		waitForMailBox();
		return sMngr;
	}

	/**
	 * send a complaint to the server
	 * 
	 * @param comp
	 *            a complaint sent from controller
	 */
	public void sendComplaintToServer(Complaint comp) {
		System.out.println("CLIENT " + comp.get_customerServiceEmployeeID());
		handleMessageFromClientUI(comp);
	}

	/**
	 * send an order to the server
	 * 
	 * @param order
	 *            an order sent from controller
	 */
	public void sendCompletedOrderToServer(Order order) {
		System.out.println("CLIENT " + order.getCustomerID());
		handleMessageFromClientUI(order);
		waitForMailBox();
	}

	/**
	 * send a product to the server with a message to delete it
	 * 
	 * @param product
	 *            the product to be deleted
	 */
	public void sendDeleteProductFromCatalogDB(Product product) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("deleteProd");
		arr.add(product);
		client.handleMessageFromClientUI(arr);
	}

	/**
	 * send a created product to the server with a message to add it
	 * 
	 * @param p
	 *            a new product created by network employee
	 */
	public void sendAddProductFromCatalogDB(Product p) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("addPro");
		arr.add(p);
		client.handleMessageFromClientUI(arr);

	}

	/***
	 * sent the array of answers of the survey to data base
	 * 
	 * @param survey
	 */

	public void add_Survey(Survey survey) {

		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("addSurvey");
		arr.add(survey);
		client.handleMessageFromClientUI(arr);
	}

	/**
	 * this function activate when someone asks for product arraylist we send
	 * receive to the function handleMessageFromClientUI that sends this signal to
	 * the function sendToServer which leads to the function handleMessageFromClient
	 * , then it return the updated arraylist which will return to the
	 * ProductFrameController
	 */
	public ArrayList<Product> getProducts() {
		System.out.println(mail_box);
		client.handleMessageFromClientUI("recive");
		waitForMailBox();
		System.out.println(products);
		return products;
	}

	/**
	 * sends store ID and a message to receive discounts to the server
	 * 
	 * @param storeID
	 *            the store ID (PK)
	 * @return array list of products in discount in a store
	 */
	public ArrayList<Discount> getDiscounts(String storeID) {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("recive discounts");
		arr.add(storeID);
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
		return discounts;
	}

	/**
	 * sends a customer id , store id and a message to receive customer orders to
	 * the server
	 * 
	 * @param customerName
	 *            customer ID (PK)
	 * @param storeID
	 *            store ID (PK)
	 * @return customer orders in a store
	 */
	public ArrayList<Order> getCustomerOrders(String customerName, String storeID) {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("recive customer orders");
		arr.add(customerName);
		arr.add(storeID);
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
		return orders;
	}

	/**
	 * sends a message to the server to receive users
	 * 
	 * @return array list of users
	 */
	public ArrayList<User> getUsersList() {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("recive users");
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
		System.out.println("CLIENT :" + users);
		return users;
	}

	/**
	 * sends a message to the server to receive flowers
	 * 
	 * @return array list of flowers
	 */
	public ArrayList<Flower> getFlowers() {

		ArrayList<String> arr = new ArrayList<String>();
		arr.add("receive flowers");
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
		return flowers;
	}

	/**
	 * sends a user name and a message to receive if a user is active
	 * 
	 * @param username
	 *            user id (PK)
	 * @return 1=user is active. 0=user is offline
	 */
	public int checkIfActive(String username) {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("recieve isActive");
		arr.add(username);
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
		return isActive;
	}

	/**
	 * sends a user id, user permission in string and a message to change a
	 * permission of a user
	 * 
	 * @param selectedUser
	 *            a user sent by a controller
	 */
	public void updatePermissionToExistUser(User selectedUser) {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("recive user to change permission");
		arr.add(selectedUser.getName());
		arr.add(selectedUser.getStLogin());
		client.handleMessageFromClientUI(arr);
	}

	/**
	 * @author sagi arieli
	 * @param name
	 *            = costumer id (user name)
	 * @return all the stores the customer is enlisted to
	 */
	public static ArrayList<CustomerinStore> getStores(String name) {

		ArrayList<String> arr = new ArrayList<String>();
		arr.add("findStores");
		arr.add(name);
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
		return stores;
	}

	/**
	 * sends to server a store id, product id and a message to remove discount
	 * 
	 * @param s_ID
	 *            the store id (PK)
	 * @param get_product_ID
	 *            the product id (PK)
	 */
	public void removeDiscount(String s_ID, String get_product_ID) {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("remove discount");
		arr.add(s_ID);
		arr.add(get_product_ID);
		handleMessageFromClientUI(arr);
		waitForMailBox();
	}

	/**
	 * send the product to the server and a message to add discount
	 * 
	 * @param d
	 *            a catalog product
	 */
	public void addDiscount(Discount d) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("add discount");
		arr.add(d);
		handleMessageFromClientUI(arr);
		waitForMailBox();
	}

	/**
	 * send discount to edit to the server
	 * 
	 * @author sagi arieli
	 * @param d
	 *            discount
	 */
	public void editDiscount(Discount d) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("edit discount");
		arr.add(d);
		handleMessageFromClientUI(arr);
		waitForMailBox();
	}

	/**
	 * sends to server a product and a message to edit product
	 * 
	 * @param p
	 *            a catalog product
	 */
	public void sendEditedProductFromCatalogDB(Product p) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("Edit_pro");
		arr.add(p);
		client.handleMessageFromClientUI(arr);
	}

	/**
	 * sends to server the store id of the order, the order id and a message to get
	 * catalog products of order
	 * 
	 * @param selectedOrder
	 *            an order sent by a controller
	 * @return array list of product of an order
	 */
	public ArrayList<Product> getProductsOfOrderFromDB(Order selectedOrder) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("Get products of order");
		arr.add(selectedOrder.getStoreID());
		arr.add(selectedOrder.getOrderID());
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
		return products;
	}

	/**
	 * sends to server store id of the order, the order id and a message to get self
	 * made product of order
	 * 
	 * @param selectedOrder
	 *            an order sent by controller
	 * @return array list of self made products of an order
	 */
	public ArrayList<SelfMadeProduct> getSelfMadeOfOrderfromDB(Order selectedOrder) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("Get Self-Made products of order");
		arr.add(selectedOrder.getStoreID());
		arr.add(selectedOrder.getOrderID());
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
		return orderedSMProd;
	}

	/**
	 * sends to server the store id, quarter, year and a message to get store income
	 * report
	 * 
	 * @param storeID
	 *            the store id (PK)
	 * @param quarter
	 *            the quarter
	 * @param year
	 *            year
	 * @return total income(float) of a store in a selected quarter and year
	 */
	public float getStoreIncome(String storeID, String quarter, int year) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("get store income report");
		arr.add(storeID);
		arr.add(quarter);
		arr.add(year);
		handleMessageFromClientUI(arr);
		waitForMailBox();
		return income;
	}

	/**
	 * sends a Complaint object to the server
	 * 
	 * @param comp
	 *            a complaint sent by controller
	 */
	public void sendComplaintToDB(Complaint comp) {
		handleMessageFromClientUI(comp);
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Store> getStores() {
		client.handleMessageFromClientUI("GetStores");
		waitForMailBox();
		System.out.println(allStores);
		return allStores;
	}

	/**
	 * send to server a customer service employee
	 * 
	 * @param cse
	 *            a customer service employee
	 * @return array list of complaints
	 */
	public ArrayList<Complaint> getComplaints(CustomerServiceEmployee cse) {
		handleMessageFromClientUI(cse);
		waitForMailBox();
		return compArr;
	}

	/**
	 * 
	 * @return cus
	 */
	public ArrayList<CustomerinStore> getCustomerInStore() {
		ArrayList<String> toSend = new ArrayList<String>();
		toSend.add("ask for customers in stores");
		handleMessageFromClientUI(toSend);
		waitForMailBox();
		return customerInStores;
	}

	/**
	 * sends to server the order,the refund ratio and a message to cancel order
	 * 
	 * @param o
	 *            instance of order
	 * @param the
	 *            ratio refund
	 */
	public void cancelOrder(Order o, float i) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("cancel order");
		arr.add(o);
		arr.add(i);
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
	}

	/**
	 * sends to server the store id, customer id,the new refund to update and a
	 * message to update customer refund
	 * 
	 * @param storeID
	 *            the store id (PK)
	 * @param customerID
	 *            the customer user name (PK)
	 * @param refundAmount
	 *            the new customer refund amount in store to update
	 */
	public void updateCustomerRefund(String storeID, String customerID, float refundAmount) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("Update customer refund");
		arr.add(storeID);
		arr.add(customerID);
		arr.add(refundAmount);
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
	}

	/**
	 * sends to servers a bunch of parameters and send the message compensate
	 * 
	 * @param complaintID
	 *            complaint ID (PK)
	 * @param compensateAmount
	 *            amount to compensate
	 * @param customerID
	 *            customer user name
	 * @param storeID
	 *            the store id
	 */
	public void updateComplaint(String complaintID, float compensateAmount, String customerID, String storeID) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("compensate");
		arr.add(complaintID);
		arr.add(compensateAmount);
		arr.add(customerID);
		arr.add(storeID);
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
	}

	/**
	 * This method invoke by the system in each quarter responsible for pulling data
	 * from the data base
	 * 
	 * @param storeID3
	 *            store id
	 * @param quarter3
	 *            the quarter
	 * @param year3
	 *            the year
	 * @return arraylist of items in order
	 */
	public ArrayList<ItemInOrderReport> getOrderReport(String storeID3, String quarter3, int year3) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("getOrderReport");
		arr.add(storeID3);
		arr.add(quarter3);
		arr.add(year3);
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
		return itemsInOrderReport;
	}

	/**
	 * This method invoke by the system in each quarter responsible for pulling data
	 * from the data base
	 * 
	 * @param storeID
	 *            - specific store id
	 * @param year
	 *            - specific year
	 * @param quarter
	 *            requested quarter
	 * @return arraylist of strings represent complaint report data
	 */
	public ArrayList<String> getComplaintsToReport(String storeID, int year, String quarter) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("get complaints to report");
		arr.add(storeID);
		arr.add(year);
		arr.add(quarter);
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
		return complaintReport;
	}

	/**
	 * This method responsible for deleting store employee by the system manager
	 * 
	 * @param username
	 *            - employee user name
	 */
	public void deleteStoreEmpFromdb(String username) {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("Delete Store-Employee");
		arr.add(username);
		client.handleMessageFromClientUI(arr);
	}

	/**
	 * This method responsible for deleting store manager by the system manager
	 * 
	 * @param username
	 *            - store manager user name
	 */
	public void deleteStoreMngFromdb(String username) {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("Delete Store-Manager");
		arr.add(username);
		client.handleMessageFromClientUI(arr);
	}

	/**
	 * This method responsible to attach store employee to specific store by the
	 * system manager
	 * 
	 * @param username
	 *            - store emloyee id
	 * @param storeID
	 *            - store id
	 */
	public void addStoreEmpTodb(String username, String storeID) {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("Add new Store-Emp");
		arr.add(username);
		arr.add(storeID);
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
	}

	/**
	 * This method responsible for adding store manager to db by the system manager
	 * 
	 * @param username
	 *            - store manager id
	 * @param storeID
	 *            - store id
	 */
	public void addStoreMngTodb(String username, String storeID) {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("Add new Store-Mng");
		arr.add(username);
		arr.add(storeID);
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
	}

	/**
	 * This method responsible for pulling orders of a specific store from the db
	 * 
	 * @param storeID
	 *            - store id
	 * @return - arraylist of orders of a specific store
	 */
	public ArrayList<Order> getStoreOrders(String storeID) {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("recieve store orders");
		arr.add(storeID);
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
		return orders;
	}

	/**
	 * This method responsible for mark a specific order as pickup on db by the
	 * store employee
	 * 
	 * @param orderID
	 *            - order id
	 * @param storeID
	 *            - store id
	 */
	public void orderHandlingCompleted(int orderID, String storeID) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("order handlig over");
		arr.add(orderID);
		arr.add(storeID);
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
	}

	/**
	 * This method responsible for pulling order data about a specific order
	 * 
	 * @param o
	 *            - the order
	 * @return - the updated order from db
	 */
	public Order getOrder(Order o) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("get order");
		arr.add(o);
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
		return order;
	}

	/**
	 * This method responsible to get all customers that can be added to a specific
	 * store
	 * 
	 * @param storeID
	 *            - store id
	 * @return - arraylist of customer that can be added
	 */
	public ArrayList<String> getCustomersToAdd(String storeID) {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("get customers that not from that store");
		arr.add(storeID);
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
		return customersToAdd;
	}

	/**
	 * This method responsible to add a specific customer to a store
	 * 
	 * @param customerToAdd
	 *            - the customer
	 */
	public void sendCustomerToAdd(Customer customerToAdd) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("receive complete customer in store to add");
		arr.add(customerToAdd);
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
	}

	/**
	 * This method responsible to attach expert comment to survey
	 * 
	 * @param selectedListIndex
	 *            - survey id
	 * @param text
	 *            - the expert comment
	 */
	public void add_pro_comment(int selectedListIndex, String text) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("add_pro_comment");
		arr.add(selectedListIndex);
		arr.add(text);
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
	}

	/**
	 * This method responsible to pull a survey answers from the db
	 * 
	 * @return survey answer
	 */
	public ArrayList<Survey> ask_for_survey() {

		ArrayList<String> toSend = new ArrayList<String>();
		toSend.add("get answers");
		client.handleMessageFromClientUI(toSend);
		waitForMailBox();
		return survey_answers;
	}

	/**
	 * This method responsible for pulling expert comments on survey
	 * 
	 * @return arraylist represent expert comments
	 */
	public ArrayList<String> ask_for_pro_comment_arr() {

		ArrayList<String> toSend = new ArrayList<String>();
		toSend.add("get pro comment arr");
		client.handleMessageFromClientUI(toSend);
		waitForMailBox();
		return pro_comment_arr;
	}

	/**
	 * This method responsible to return arraylisty of satisfaction statistic about
	 * a store in a specific quarter
	 * 
	 * @param storeID
	 *            - store id
	 * @param year
	 *            - year
	 * @param quarter
	 *            - quarter
	 * @return - arraylist of floats represent satisfaction
	 */
	public ArrayList<Float> getSatisfactionToReport(String storeID, int year, String quarter) {
		ArrayList<Object> arr = new ArrayList<Object>();
		arr.add("get Satisfaction Report");
		arr.add(storeID);
		arr.add(quarter);
		arr.add(year);
		client.handleMessageFromClientUI(arr);
		waitForMailBox();
		return satisfactionRep;
	}

	/**
	 * sends to server the user and a message "exit"
	 */
	public void user_exit() {
		if (user != null) {
			ArrayList<Object> arr = new ArrayList<Object>();
			arr.add("exit");
			arr.add(user);
			client.handleMessageFromClientUI(arr);
		}
	}

	/**
	 * this method locks the client for a moment so the message can be handled and
	 * sent to the user without any issues
	 */
	private static void waitForMailBox() {
		while (!mail_box) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		mail_box = false;
	}

	/**
	 * sub-class of Client. this Class handles the opening of the first gui when
	 * launching the main method. extends Application to use the start method.
	 * 
	 * @param gui
	 *            an instance of a gui class
	 * @param parameters
	 *            required for the Launch method
	 */
	public static class GuiOpener extends Application {

		private ClientConnectToServerGui gui;
		private static String[] parameters = null;

		/**
		 * invoked by client Main method. this method calls the start method in this
		 * class
		 */
		public static void init_launch() {
			launch(parameters);
		}

		/**
		 * called by init_Launch in the class and starts the progress of launching the gui
		 */
		public void start(Stage primaryStage) throws Exception {
			gui = new ClientConnectToServerGui();
			gui.start(primaryStage);
		}

	}
	/**
	 * Called by deleteCustomerFromDB in LoginController an send to server the customer id that we want to delete from db.
	 * @author Oren 
	 * @param username
	 */
	public void deleteCustomerFromdb(String username) {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("Remove customer from tables");
		arr.add(username);
		client.handleMessageFromClientUI(arr);		
	}

	/**
	 * This method responsible for ask from the server regular customer members
	 * @param storeID - store id
	 * @return -  arraylist of regular customers
	 */
	public ArrayList<String> getRegularCustomerFromDB(String storeID) {
		ArrayList<String> toSend = new ArrayList<String>();
		String str = "ask for regular customers";
		toSend.add(str);
		toSend.add(storeID);
		client.handleMessageFromClientUI(toSend);
		waitForMailBox();
		return regularCustomers;
	}

	/**
	 * This method sends to the server cutomer details
	 * @param cutomerID
	 * @param memberType
	 */
	public void changeRegularCustomer(String cutomerID,String storeID,int memberType) {
		ArrayList<String> toSend = new ArrayList<String>();
		String str = "send cutomer to change";
		toSend.add(str);
		toSend.add(cutomerID);
		toSend.add(storeID);
		toSend.add(""+memberType);
		client.handleMessageFromClientUI(toSend);

	}

}
