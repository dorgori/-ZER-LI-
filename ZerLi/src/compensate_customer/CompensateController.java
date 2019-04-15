package compensate_customer;

import java.util.ArrayList;

import client.CustomerinStore;
import client.LoginController;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import survey.Survey;

/**
 * 
 * @author dorgori This controler class provide the connection between gui
 *         client and server The class store the customer service employee that
 *         active
 */
public class CompensateController {
	public static ArrayList<Survey> survey_answers;
	public static ArrayList<String> pro_comment_arr;
	public static CustomerServiceEmployee cse;
	public static ArrayList<CustomerinStore> cArr;

	public static void changeWindow(Pane root) {
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				LoginController.client.user_exit();
			}
		});
	}

	/**
	 * This method initialize the active customer service employee
	 * 
	 * @param cuServEmp
	 */
	public static void setCustomerServiceEmployee(CustomerServiceEmployee cuServEmp) {
		cse = cuServEmp;
	}

	/**
	 * This method responsible for sending new complaint to the server
	 * 
	 * @param comp
	 *            the complaint to send
	 */
	public static void sendNewComplaint(Complaint comp) {
		LoginController.client.sendComplaintToDB(comp);
	}

	/**
	 * This method responsible to receive from the server the active customer
	 * service employee and pull his open complaints from the server
	 * 
	 * @param cse1
	 * @return arraylist of complaints
	 */
	public static ArrayList<Complaint> getComplaintFromDB(CustomerServiceEmployee cse1) {
		return LoginController.client.getComplaints(cse1);
	}

	/**
	 * This method responsible to receive all the registered cutomers in stores
	 * 
	 * @return arraylist of customer in stores
	 */
	public static ArrayList<CustomerinStore> getCustomersInStore() {
		cArr = LoginController.client.getCustomerInStore();
		return cArr;
	}

	/**
	 * This method responsible to send to the server update complaint status and
	 * compensate amount
	 * 
	 * @param complaintID
	 *            id of the complaint
	 * @param compensateAmount
	 *            amount
	 * @param customerID
	 *            customer id
	 * @param storeID
	 *            store id
	 */
	public static void updateComplaintStatus(String complaintID, float compensateAmount, String customerID,
			String storeID) {
		LoginController.client.updateComplaint(complaintID, compensateAmount, customerID, storeID);
	}

	/**
	 * This method responsible of sending the expert opinion on the survey
	 * 
	 * @param selectedListIndex
	 *            survey id
	 * @param text
	 *            the expert opinion
	 */
	public static void add_pro_comment(int selectedListIndex, String text) {
		LoginController.client.add_pro_comment(selectedListIndex, text);

	}

	/**
	 * This method responsible to receive surveys from the server
	 *
	 * @return arraylist of survey
	 */
	public static ArrayList<Survey> ask_for_survey() {

		survey_answers = LoginController.client.ask_for_survey();
		return survey_answers;
	}

	/**
	 * This method responsible to receive expert opinion from server
	 * 
	 * @return arraylist contain the opinions
	 */
	public static ArrayList<String> ask_for_pro_comment_arr() {
		pro_comment_arr = LoginController.client.ask_for_pro_comment_arr();
		return pro_comment_arr;
	}
}
