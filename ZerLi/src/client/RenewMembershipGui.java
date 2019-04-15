package client;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import order.OrderController;
import javafx.fxml.Initializable;

/**
 * This class responsible for display the gui of renew membership for customer
 * and handel all the functionality of it
 * 
 * @author dorgori
 *
 */
public class RenewMembershipGui implements Initializable {

	private ObservableList<String> list;
	String storeID = LoginController.sMngr.getS_ID();

	@FXML
	private ComboBox<String> cmbCustomer = new ComboBox<String>();

	@FXML
	private ComboBox<String> cmbType = new ComboBox<String>();

	@FXML
	private Button btnSave;

	@FXML
	private Label alertCustomer;

	@FXML
	private Label alertType;

	@FXML
	private Button btnBack;

	/**
	 * This method initialize the comboBox of types
	 */
	private void initTypeCombobox() {
		ArrayList<String> toAdd = new ArrayList<String>();
		toAdd.add("Monthly member");
		toAdd.add("Yearly member");
		list = FXCollections.observableArrayList(toAdd);
		cmbType.setItems(list);
	}

	/**
	 * This method initalize the combobox of cutomer with regular membership
	 */
	private void initCustomerCombobox() {
		ArrayList<String> toAdd;
		toAdd = LoginController.getRegularCustomerInStore(storeID);
		if (toAdd.get(0).equals("-999")) {
			toAdd.remove(0);
			toAdd.add("No regular customers");
			btnSave.setDisable(true);
		}
		list = FXCollections.observableArrayList(toAdd);
		cmbCustomer.setItems(list);
	}

	/**
	 * This method occur when this gui in open , calls the initialize combobox
	 * functions
	 * 
	 * @param arg0
	 * @param arg1
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initCustomerCombobox();
		initTypeCombobox();
	}

	@FXML
	public void backListener(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/client/StoreMngMenu.fxml").openStream());
		OrderController.changeWindow(root);
	}

	@FXML
	public void saveListener(ActionEvent event) throws Exception {
		int selectCustomer = cmbCustomer.getSelectionModel().getSelectedIndex();
		int selectType = cmbType.getSelectionModel().getSelectedIndex();
		String cutomerID = cmbCustomer.getSelectionModel().getSelectedItem();
		if (selectCustomer == -1) {
			alertCustomer.setVisible(true);
			return;
		}
		if (selectType == -1) {
			alertType.setVisible(true);
			return;
		}
		LoginController.sendRegularCustomer(cutomerID,storeID,selectType+1);
		backListener(event);
	}
}
