package compensate_customer;

import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.CustomerinStore;
import client.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * 
 * @author dorgori This class handles the add functionality and provide the user
 *         all the needed information
 */
public class AddComplaintGui implements Initializable {

	private Complaint comp;
	private ObservableList<String> customerList;
	private ObservableList<String> storeList;
	private ArrayList<CustomerinStore> cutsInStore;
	@FXML
	private TextArea complaintText;

	@FXML
	private Button saveBtn;

	@FXML
	private Text descAlert;

	@FXML
	private TextField storeTxt;

	@FXML
	private Text storeAlert;

	@FXML
	private Text customerAlert;

	@FXML
	private Button btnBack;

	@FXML
	private ComboBox<String> cmbStores = new ComboBox<String>();

	@FXML
	private ComboBox<String> cmbCustomer = new ComboBox<String>();

	@FXML
	/**
	 * save complaint to data base when save push button clicked
	 * 
	 * @param event
	 *            the click on the button
	 * @throws Exception
	 */
	void saveComplaintlistener(ActionEvent event) throws Exception {
		boolean flag = false;
		flag = false;
		if (cmbCustomer.getSelectionModel().getSelectedIndex() == -1) {
			customerAlert.setVisible(true);
			flag = true;
		}
		if (cmbStores.getSelectionModel().getSelectedIndex() == -1) {
			storeAlert.setVisible(true);
			flag = true;
		}
		if (complaintText.getText().equals("")) {
			descAlert.setVisible(true);
			flag = true;
		}
		if (!flag) {
			LocalTime time = LocalTime.now();
			int hour = time.getHour();
			int minutes = time.getHour();
			comp = new Complaint(CompensateController.cse.getName(), cmbCustomer.getValue(), cmbStores.getValue(),
					complaintText.getText(), hour, minutes);
			comp.set_compensateAmount(0);
			CompensateController.sendNewComplaint(comp);
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader
					.load(getClass().getResource("/compensate_customer/customerServiceEmployeeGui.fxml").openStream());
			LoginController.setStageTitle("CustomerServiceEmployeeMenu");
			CompensateController.changeWindow(root);
		}
	}

	/**
	 * change menu
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void backlistener(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader
				.load(getClass().getResource("/compensate_customer/customerServiceEmployeeGui.fxml").openStream());
		LoginController.setStageTitle("CustomerServiceEmployeeMenu");
		CompensateController.changeWindow(root);
	}

	/**
	 * initialize the combo box to pick stores for specific customer
	 */
	private void initCustomerComboBox() {

		ArrayList<String> customerID = new ArrayList<String>();
		cutsInStore = CompensateController.getCustomersInStore();
		for (CustomerinStore cio : cutsInStore) {
			if (!customerID.contains(cio.getS_ID()))// avoid duplicate name
				customerID.add(cio.getS_ID());
		}
		customerList = FXCollections.observableArrayList(customerID);
		cmbCustomer.setItems(customerList);
	}

	/**
	 * This method initialize the combo box of customers
	 * 
	 * @param event
	 *            the pick
	 * @throws Exception
	 */
	@FXML
	void customerComboBoxlistener(ActionEvent event) throws Exception {
		ArrayList<String> storeID = new ArrayList<String>();
		for (CustomerinStore cio : cutsInStore) {
			if (cmbCustomer.getValue().equals(cio.getS_ID()))
				storeID.add(cio.getS_name());
		}
		storeList = FXCollections.observableArrayList(storeID);
		cmbStores.setItems(storeList);
	}

	/**
	 * initialize new gui window
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initCustomerComboBox();
	}

}
