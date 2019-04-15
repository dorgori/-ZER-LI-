package client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import catalog.CatalogController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * Describes the Customer Store Selection Menu gui where the customer can select
 * a list of stores he is enlisted to and proceed to the next gui
 * 
 * @param itemIndex
 *            the index of the selected store
 * @param list
 *            an observable list of strings of store names that is shown in the
 *            combo box
 * @param stores
 *            the store selected in the combo box
 */
public class CustomerStoreSelectionMenuGui implements Initializable {

	private static int itemIndex;
	private ObservableList<String> list;
	private static ArrayList<CustomerinStore> stores;

	@FXML
	private Text storeSlctTita_txt;

	@FXML
	private Button enterStore_btn;

	@FXML
	private ComboBox<String> storSlct_combo = new ComboBox<String>();

	@FXML
	private Label selectstr_lbl;

	@FXML
	private Button logout_btn;

	/**
	 * initialize the combo box with stores available.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		ArrayList<String> al = new ArrayList<String>();
		stores = LoginController.getCStores();
		if (stores != null) {
			for (CustomerinStore s : stores) {

				al.add(s.getS_name());
			}
			list = FXCollections.observableArrayList(al);
			storSlct_combo.setItems(list);
		} else {
			enterStore_btn.setDisable(true);
		}
	}

	/**
	 * gather information about the store selected and changes to the next window
	 * 
	 * @param event
	 *            clicked on "Enter" button
	 */
	@FXML
	private void enterStoreHandle(ActionEvent event) {
		LoginController.customer.setStoreID(stores.get(itemIndex).getS_ID());
		LoginController.customer.setRefundAmount(stores.get(itemIndex).getRefund());
		System.out.println(stores.get(itemIndex).getRefund());
		try {
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			LoginController.setStageTitle("customer menu window");
			Pane root;
			root = loader.load(getClass().getResource("CustomerMenu.fxml").openStream());
			LoginController.changeWindow(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * enables the "Enter" button
	 * 
	 * @param event
	 *            clicked on a store in a combo box
	 */
	@FXML
	private void storeComboBoxSelect(ActionEvent event) {
		enterStore_btn.setDisable(false);
		itemIndex = storSlct_combo.getSelectionModel().getSelectedIndex();
	}

	/**
	 * logout the user and changes window to the login menu
	 * 
	 * @param event
	 *            clicked on "Logout" button
	 * @throws IOException
	 */
	@FXML
	private void userLogout(ActionEvent event) throws IOException {
		LoginController.userLogOut();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/client/loginGui.fxml").openStream());
		LoginController.setStageTitle("login window");
		CatalogController.changeWindow(root);
	}

}
