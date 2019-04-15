package client;

import java.util.ArrayList;

import catalog.CatalogController;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import order.OrderController;

/**
 * Controller that connect between gui layer and Client layer about the login
 * methods
 * 
 * @param client
 *            the client
 * @param activeUser
 *            the User
 * @param customer
 *            a possible customer that logins
 * @param sEmployee
 *            a possible store employee that logins
 * @param sMngr
 *            a possible store manager that logins
 * @param stageTitle
 *            a string to set a stage title
 */
public class LoginController {
	public static Client client;
	public static User activeUser;
	public static Customer customer;
	public static StoreEmployee sEmployee;
	public static StoreManager sMngr;
	public static String stageTitle = "";

	/**
	 * sets the client
	 * 
	 * @param c
	 *            the client from Client class
	 */
	public static void setClient(Client c) {
		client = c;
	}

	/**
	 * method for changing stages
	 * 
	 * @param root
	 *            the scene to enter
	 */
	public static void changeWindow(Pane root) {
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle(stageTitle);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				client.user_exit();
			}
		});
	}

	/**
	 * defines a string to a stage title
	 * 
	 * @param stageTitleget
	 *            string of the stage title
	 */
	public static void setStageTitle(String stageTitleget) {
		stageTitle = stageTitleget;
	}

	/**
	 * get the user
	 * 
	 * @param user
	 *            the user from the gui
	 * @return the user from the client
	 */
	public static User commit_log_in(User user) {
		activeUser = client.sendLogin(user);
		System.out.println(activeUser);
		return (activeUser);
	}

	/**
	 * get customer
	 * 
	 * @param func_customer
	 *            a customer from the gui
	 * @return the customer from the client
	 */
	public static Customer findCustomer(Customer func_customer) {
		func_customer = client.sendCustomer(func_customer);
		OrderController.set_customer(func_customer);
		System.out.println(func_customer.getName());
		customer = func_customer;
		return (customer);
	}

	/**
	 * get the list of users from the database.
	 * 
	 * @return array list of users
	 */
	public static ArrayList<User> getUsersList() {
		return client.getUsersList();
	}

	/**
	 * checks if a user is logged on/off
	 * 
	 * @param selectedUser
	 *            a user selected by a system admin
	 * @return 1=user is logged in 0=user offline
	 */
	public static int checkActiveUser(String selectedUser) {
		return client.checkIfActive(selectedUser);
	}

	/**
	 * called when a user is logging out of the server
	 */
	public static void userLogOut() {
		client.user_exit();
	}

	/**
	 * updates permission of a given user
	 * 
	 * @param selectedUser
	 *            a user selected
	 */
	public static void updatePermission(User selectedUser) {
		client.updatePermissionToExistUser(selectedUser);
	}

	/**
	 * @author sagi arieli
	 * @param name
	 *            = costumer id (user name)
	 * @return all the stores the customer is enlisted to
	 */
	public static ArrayList<CustomerinStore> getCStores(String name) {

		return Client.getStores(name);
	}

	/**
	 * @author sagi arieli this is an override to getCStores, only it take the id of
	 *         the current logged in customer.
	 * @return
	 */
	public static ArrayList<CustomerinStore> getCStores() {
		System.out.println(customer.getName());
		return getCStores(customer.getName());

	}

	/**
	 * finds the details of the store employee that logged in
	 * 
	 * @param s
	 *            a store employee given by gui
	 * @return store employee
	 */
	public static StoreEmployee findSEmplyee(StoreEmployee s) {
		StoreEmployee se = client.getSE(s);
		sEmployee = se;
		CatalogController.setSE(se);
		return se;
	}

	/**
	 * finds the details of the store manager that logged in
	 * 
	 * @param s
	 *            a store manager given by gui
	 * @return store manager
	 */
	public static StoreManager findSmngr(StoreManager s) {
		StoreManager sm = client.getSmngr(s);
		sMngr = sm;
		return sm;
	}

	/**
	 * adds a customer to a specific store
	 * 
	 * @param customerToAdd
	 *            a customer from the gui selected
	 */
	public static void sendCustomerToAdd(Customer customerToAdd) {
		client.sendCustomerToAdd(customerToAdd);
	}

	/**
	 * This method called from ChangePermission, gets all the stores in the system and send ArrayList of store.
	 * @author Oren
	 * @return ArrayList of stores.
	 */
	public static ArrayList<Store> getStoresList() {
		return client.getStores();
	}
	/**
	 * This method called from ChangePermission, gets the specific user (Store Employee) and calling to another method in client
	 * that send it to server to delete the record of this user from storeemployee table.- not store employee anymore.
	 *@author Oren 
	 *@param username - the username of the store employee that we want to remove.
	 */
	public static void deleteStoreEmpFromDB(String username) {
		client.deleteStoreEmpFromdb(username);
	}
	/**
	 * This method called from ChangePermission, gets the specific user (Store Manager) and calling to another method in client
	 * that send it to server to delete the record of this user from storemanager table.- not store manager anymore.
	 *@author Oren 
	 *@param username - the username of the store manager that we want to remove.
	 */
	public static void deleteStoreMngFromDB(String username) {
		client.deleteStoreMngFromdb(username);
	}
	/**
	 * This method called from ChangePermission, gets the specific user (Store Employee) and store ID and calling to another 
	 * method in client that send it to server to add new record of this user to add him in storeemployee table.-new store employee
	 *@author Oren 
	 *@param username - the username of the store employee that we want to add.
	 *@param storeID - the store ID of the new store employee that we add.
	 */
	public static void addStoreEmpToDB(String username, String storeID) {
		client.addStoreEmpTodb(username, storeID);
	}
	/**
	 * This method called from ChangePermission, gets the specific user (Store Manager) and store ID and calling to another 
	 * method in client that send it to server to add new record of this user to add him in storemanager table.-new store manager
	 *@author Oren 
	 *@param username - the username of the store manager that we want to add.
	 *@param storeID - the store ID of the new store manager that we add.
	 */
	public static void addStoreMngToDB(String username, String storeID) {
		client.addStoreMngTodb(username, storeID);
	}
	/**
	 * This method gets all the customers that don'thave account in the specific store ID by calling to method in client.
	 * @author Oren
	 * @param storeID -the specific store ID that we don't want her customers.- we want all the rest.
	 * @return ArrayList of customers username that don't have account in this store. 
	 */
	public static ArrayList<String> getCustomers(String storeID) {
		return client.getCustomersToAdd(storeID);
	}
	/**
	 * This method called from ChangePermission, gets the specific user (customer) and calling to another method in client
	 * that send it to server to delete the record of this user from customers and customerinstore tables.- not customer anymore.
	 *@author Oren 
	 *@param username - the username of the customer that we want to remove.
	 */
	public static void deleteCustomerFromDB(String username) {
		client.deleteCustomerFromdb(username);		
	}

	/**
	 * This method responsible for ask from the client arraylist of regular members
	 * @param storeID
	 * @return - arraylist of regular customers
	 */
	public static ArrayList<String> getRegularCustomerInStore(String storeID) {
		return client.getRegularCustomerFromDB(storeID);
	}

	/**
	 * This method send cutomer details to the client in order to update his membership
	 * @param cutomerID
	 * @param memberType
	 */
	public static void sendRegularCustomer(String cutomerID,String storeID,int memberType) {
		client.changeRegularCustomer(cutomerID,storeID,memberType);
	}

}
