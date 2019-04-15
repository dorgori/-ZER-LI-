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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import order.OrderController;
import survey.SurveyController;

/**
 * Describes the store employee menu gui
 *
 */
public class StoreEmployeeMenuGui implements Initializable {

	@FXML
	private Button discount_btn;

	@FXML
	private Button LogOut_btn;

	@FXML
	private Text StoreEmployeeMenue_txt;

	@FXML
	private Button Perform_a_survey;

	@FXML
	private Button btnStoreOrders;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	/**
	 * changes window to the view store orders gui
	 * 
	 * @param event
	 *            clicked on "View Store Orders" button
	 * @throws Exception
	 */
	@FXML
	void viewStoreOrdersListenner(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/order/ViewStoreOrders.fxml").openStream());
		OrderController.changeWindow(root);
	}

	/**
	 * changes window to the survey gui
	 * 
	 * @param event
	 *            clicked on "perform a survey" button
	 * @throws IOException
	 */
	@FXML
	void clicked_on_survey(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/survey/survey_gui.fxml").openStream());
		SurveyController.changeWindow(root);

	}

	/**
	 * changes window to the discount menu gui
	 * 
	 * @param event
	 *            clicked on "Discount Menu" button
	 */
	@FXML
	void discount_Menu_Btn(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root;
		try {
			root = loader.load(getClass().getResource("/catalog/DiscountMenu.fxml").openStream());
			CatalogController.changeWindow(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Logout the user and changes window to the login gui
	 * 
	 * @param event
	 *            clicked on "logout" button
	 * @throws IOException
	 */
	@FXML
	void log_out_handler(ActionEvent event) throws IOException {
		LoginController.userLogOut();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/client/loginGui.fxml").openStream());
		LoginController.setStageTitle("login window");
		CatalogController.changeWindow(root);
	}

}
