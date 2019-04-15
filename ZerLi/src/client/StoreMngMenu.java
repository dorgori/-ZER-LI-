package client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 * Describes the Store Manager Menu gui
 */
public class StoreMngMenu implements Initializable {

	@FXML
	private Button btnCreateCustomer;
	@FXML
	private Button btnReports;
	@FXML
	private Button btnLogout;
    @FXML
    private Button btnRenew;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	/**
	 * changes window to the create customer in store gui
	 * 
	 * @param event
	 *            clicked on "Create new customer" button
	 * @throws Exception
	 */
	@FXML
	private void chooseCreateCustomer(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/client/CreateCustomerInStore.fxml").openStream());
		LoginController.changeWindow(root);
	}

	/**
	 * changes window the the view reports menu gui
	 * 
	 * @param event
	 *            clicked on "View reports" button
	 * @throws Exception
	 */
	@FXML
	private void chooseViewReports(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/reports/ViewReportMenu.fxml").openStream());
		LoginController.changeWindow(root);
	}
	/**
	 * change gui for update member for customer
	 * @param event
	 * @throws Exception
	 */
	@FXML
	private void renewMemberGui(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/client/RenewMembership.fxml").openStream());
		LoginController.changeWindow(root);
	}

	/**
	 * logout the user and changes window to the login menu
	 * 
	 * @param event
	 *            clicked on "Logout" button
	 * @throws IOException
	 */
	@FXML
	private void logout(ActionEvent event) throws Exception {
		LoginController.userLogOut();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root;
		root = loader.load(getClass().getResource("/client/loginGui.fxml").openStream());
		LoginController.changeWindow(root);
	}
	
}
