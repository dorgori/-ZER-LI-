package compensate_customer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import catalog.CatalogController;
import client.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * This class represent the customer service employee gui and provides all the
 * functionality that customer service employee can perform
 * 
 * @author dorgori
 *
 */
public class CustomerServiceEmployeeGui implements Initializable {

	@FXML
	private Button addComplaintBtn;

	@FXML
	private Button saveBtn;

	@FXML
	private Button survey_results;

	@FXML
	private Button btnLogout;

	@FXML
	private Text amountAlert;

	@FXML
	private TextField txtAmount;

	@FXML
	private Text complaintAlert;

	@FXML
	private ListView<String> openComplaintList = new ListView<String>();

	private ObservableList<String> List;

	/**
	 * This method call when the add complaint
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void addComplaintlistener(ActionEvent event) throws Exception // open new gui
	{
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/compensate_customer/addComplaintGui.fxml").openStream());
		LoginController.setStageTitle("addComplaintGui");
		CompensateController.changeWindow(root);
	}

	/**
	 * This method initialize the list of open complaints
	 * 
	 * @param cse
	 *            the active customer service employee
	 */
	private void initOpenComplaintList(CustomerServiceEmployee cse) {
		ArrayList<String> toAdd = new ArrayList<String>();
		for (Complaint c : cse.getArrayListComplaints()) {
			toAdd.add(c.get_complaintID() + "\t\t" + c.get_customerID() + "\t\t" + c.get_storeID() + "\t\t"
					+ c.get_time());
		}
		List = FXCollections.observableArrayList(toAdd);
		openComplaintList.getItems().addAll(List);
		openComplaintList.refresh();
	}

	/**
	 * This method responsible to ask from the server for the complaints
	 * @param cse the active user
	 */
	private void getOpenComplaintFromDB(CustomerServiceEmployee cse) {
		cse.setMyOpenComplaints(CompensateController.getComplaintFromDB(cse));
		if (cse.getArrayListComplaints().get(0).get_complaintID() == -1)
			cse.getArrayListComplaints().remove(0);
		else
			initOpenComplaintList(cse);
	}

	/**
	 * This method occur when the customer service employee press on resolve button 
	 * with amount
	 * @param event the event button
	 * @throws Exception
	 */
	@FXML
	void resolveWithCompensatetlistener(ActionEvent event) throws Exception {
		float amount = 0;
		if (openComplaintList.getSelectionModel().getSelectedIndex() == -1)// have to pick from the list
		{
			complaintAlert.setVisible(true);
			return;
		}
		try {
			amount = Integer.parseInt(txtAmount.getText());
		} catch (Exception e) {
			amountAlert.setVisible(true);
			return;
		}
		handleCompesnate(amount, event);
	}

	/**
	 * This method occur when the customer service employee press on resolve button 
	 * without amount
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void resolveWithoutCompensatetlistener(ActionEvent event) throws Exception {
		if (openComplaintList.getSelectionModel().getSelectedIndex() == -1)// have to pick from the list
		{
			complaintAlert.setVisible(true);
			return;
		}
		handleCompesnate(0, event);
	}

	/**
	 * This method send to server the relevant info about resolving a complaint 
	 * @param CompensateAmount amount number
	 * @param event the event
	 * @throws IOException
	 */
	private void handleCompesnate(float CompensateAmount, ActionEvent event) throws IOException {
		String[] list = openComplaintList.getSelectionModel().getSelectedItem().split("\t\t");
		String complaintID = list[0];
		String customerID = list[1];
		String storeID = list[2];
		System.out.println(complaintID);
		CompensateController.updateComplaintStatus(complaintID, CompensateAmount, customerID, storeID);
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader
				.load(getClass().getResource("/compensate_customer/customerServiceEmployeeGui.fxml").openStream());
		LoginController.setStageTitle("CustomerServiceEmployeeMenu");
		CompensateController.changeWindow(root);
	}

	/**
	 * open new window when
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void add_pro_comment(ActionEvent event) throws IOException {

		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/compensate_customer/survey_result.fxml").openStream());
		LoginController.setStageTitle("survey_result");
		CompensateController.changeWindow(root);
	}
	/**
	 * This method occur when user press logout
	 * @param event the press
	 * @throws IOException
	 */
	@FXML
	void logout_btn(ActionEvent event) throws IOException {
		LoginController.userLogOut();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/client/loginGui.fxml").openStream());
		LoginController.setStageTitle("login window");
		CatalogController.changeWindow(root);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		getOpenComplaintFromDB(CompensateController.cse);
	}

}
