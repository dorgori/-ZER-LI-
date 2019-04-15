package client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import catalog.CatalogController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import order.OrderController;

/**
 * Describes the gui of the customer after he select a store
 * 
 * @param customer
 *            the customer
 */
public class CustomerMenuGui implements Initializable {

	public static Customer customer;

	@FXML
	private Button btnBrowseCatalog;

	@FXML
	private Button btnCreateOrder;

	@FXML
	private Button btnViewOrders;

	@FXML
	private Text creditsValue_txt;

	@FXML
	private Label credits_lbl;

	/**
	 * initialize the window by gathering the required information of the customer
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		customer = LoginController.customer;
		creditsValue_txt.setText("" + (float) customer.getRefundAmount());
	}

	/**
	 * changes window to create order gui
	 * 
	 * @param event
	 *            clicked on "Create order" button
	 * @throws Exception
	 */
	@FXML
	void createOrderListenner(ActionEvent event) throws Exception // open new gui
	{
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		LoginController.setStageTitle("create order menu window");
		Pane root = loader.load(getClass().getResource("/order/CreateOrderGui.fxml").openStream());
		OrderController.changeWindow(root);
	}

	/**
	 * changes window to the catalog gui
	 * 
	 * @param event
	 *            clicked on "Browse Catalog" button
	 * @throws Exception
	 */
	@FXML
	void browseCatalogListenner(ActionEvent event) throws Exception // open new gui//
	{
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		LoginController.setStageTitle("catalog menu window");
		Pane root = loader.load(getClass().getResource("/catalog/CatalogGui.fxml").openStream());
		CatalogController.changeWindow(root);
	}

	/**
	 * changes window to view orders gui
	 * 
	 * @param event
	 *            clicked on ""View orders" button
	 * @throws Exception
	 */
	@FXML
	void viewOrdersListenner(ActionEvent event) throws Exception // open new gui
	{
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		LoginController.setStageTitle("view orders window");
		Pane root = loader.load(getClass().getResource("/order/ViewOrdersGui.fxml").openStream());
		OrderController.changeWindow(root);
	}

	/**
	 * changes window to the login gui
	 * 
	 * @param event
	 *            clicked on "Logout" button
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

}
