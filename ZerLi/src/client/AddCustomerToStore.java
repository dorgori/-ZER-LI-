package client;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * Describes a gui that store manager has access to. adding a customer to his
 * store
 * 
 * @param customersList
 *            an observable list of available customer to show in a combo box
 * @param memberList
 *            an observable list of type of membership that are shown in a combo
 *            box
 * @param selectedCustomer
 *            is the selected customer from a combo box
 * @param selectedMemberType
 *            the type of the membership selected
 * @param customerToAdd
 *            the customer itself that should be added to the store after
 *            process is complete
 */
public class AddCustomerToStore implements Initializable {

	private ObservableList<String> customersList;
	private ObservableList<String> memberList;
	private String selectedCustomer = "";
	private int selectedMemberType = -1;
	private Customer customerToAdd;

	@FXML
	private ComboBox<String> cmbCustomers;
	@FXML
	private ComboBox<String> cmbMemberType;
	@FXML
	private Button btnAddCustomer;
	@FXML
	private Button btnReturnMenu;
	@FXML
	private Text lblCustException;
	@FXML
	private Text lblMemException;

	/**
	 * initialize this gui
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setLists();
	}

	/**
	 * get the customer that are not in this store and initialize the Member-type
	 * combo box with the types
	 */
	private void setLists() {
		ArrayList<String> customers = LoginController.getCustomers(LoginController.sMngr.getS_ID());
		ArrayList<String> mem = new ArrayList<String>();
		mem.add("Regular");
		mem.add("Monthly");
		mem.add("Yearly");
		customersList = FXCollections.observableArrayList(customers);
		memberList = FXCollections.observableArrayList(mem);
		cmbCustomers.setItems(customersList);
		cmbMemberType.setItems(memberList);
	}

	/**
	 * adds a customer to the store of the manager
	 * 
	 * @param event
	 *            clicked on "Add"
	 * @throws Exception
	 */
	@FXML
	private void addListenner(ActionEvent event) throws Exception {
		if (selectedCustomer.equals("")) {
			lblCustException.setVisible(true);
			return;
		} else
			lblCustException.setVisible(false);
		if (selectedMemberType == -1) {
			lblMemException.setVisible(true);
			return;
		} else
			lblMemException.setVisible(false);
		LocalDate localdt = LocalDate.now();
		customerToAdd = new Customer(selectedCustomer, LoginController.sMngr.getS_ID());
		customerToAdd.setStoreID(LoginController.sMngr.getS_ID());
		customerToAdd.intToMemType(selectedMemberType);
		customerToAdd.setMemberOption(customerToAdd.intToMemType(selectedMemberType));
		customerToAdd.setCreatedDate(localdt.toString());
		System.out.println("CUSTOMER : " + customerToAdd.getName() + " " + customerToAdd.getStoreID() + " "
				+ customerToAdd.getMemberOption().toString());
		LoginController.sendCustomerToAdd(customerToAdd);
		chooseReturn(event);
	}

	/**
	 * saves the selected customer in a variable
	 * 
	 * @param event
	 *            clicked on Customer combo box
	 * @throws Exception
	 */
	@FXML
	private void customerCmbListenner(ActionEvent event) throws Exception {
		selectedCustomer = cmbCustomers.getSelectionModel().getSelectedItem();
	}

	/**
	 * saves the selected Membership in a variable
	 * 
	 * @param event
	 *            clicked on Member-Type combo box
	 * @throws Exception
	 */
	@FXML
	private void memberCmbListenner(ActionEvent event) throws Exception {
		selectedMemberType = cmbMemberType.getSelectionModel().getSelectedIndex();
	}

	/**
	 * changes window to store manager menu
	 * 
	 * @param event
	 *            clicked on "Return to Menu"
	 * @throws Exception
	 */
	@FXML
	private void chooseReturn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/client/StoreMngMenu.fxml").openStream());
		LoginController.setStageTitle("StoreMngMenu");
		LoginController.changeWindow(root);
	}

}
