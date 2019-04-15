package client;

import java.net.URL;
import java.util.ResourceBundle;

import compensate_customer.CompensateController;
import compensate_customer.CustomerServiceEmployee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import reports.ReportController;

/**
 * This class responsible for displaying the relevant interface according to the 
 * connecteded user
 * Describes the login gui menu
 */
public class LoginGui implements Initializable {

	@FXML
	private Text lbl_username;

	@FXML
	private Text lbll_pass;

	@FXML
	private Text lbl_wrong_login;

	@FXML
	private TextField txt_user_name;

	@FXML
	private TextField txt_pass;

	@FXML
	private Button btn_login;

	@FXML
	private Text active_txt;

	@FXML
	private Text blockedtxt_txt;

	@FXML
	private Button logout_btn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	/**
	 * trying to login a user given user name and password. checks his permission to
	 * decide what next window to show to the user
	 * 
	 * @param event
	 *            clicked on "Login" button
	 * @throws Exception
	 */
	@FXML
	public void btnListener(ActionEvent event) throws Exception {
		User user = new User(txt_user_name.getText(), txt_pass.getText());
		user = LoginController.commit_log_in(user);
		if (user.getLogin() == User.permission.rejected) {
			active_txt.setVisible(false);
			blockedtxt_txt.setVisible(false);
			lbl_wrong_login.setVisible(true);
			return;
		}
		if (user.getBlocked() == 1) {
			active_txt.setVisible(false);
			lbl_wrong_login.setVisible(false);
			blockedtxt_txt.setVisible(true);
			return;
		}
		if (user.getActive() == 1) {
			lbl_wrong_login.setVisible(false);
			blockedtxt_txt.setVisible(false);
			active_txt.setVisible(true);
			return;
		}
		//user recognized as customer "permission=1"
		if (user.getLogin() == User.permission.customer) {
			Customer customer = new Customer(user.getName(), user.getPass());
			customer = LoginController.findCustomer(customer);
		//opens customer store selection menu
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			LoginController.setStageTitle("customer store pick");
			Pane root = loader.load(getClass().getResource("CustomerStoreSelectionMenu.fxml").openStream());
			LoginController.changeWindow(root);
		}
		//user recognized as store employee "permission=2"
		 else if (user.getLogin() == User.permission.store_emp) {
			StoreEmployee s = new StoreEmployee(user.getName(), user.getPass());
			s = LoginController.findSEmplyee(s);

			System.out.println(s.getS_ID());
			//opens store employee menu
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/client/StoreEmployeeMenu.fxml").openStream());
			LoginController.changeWindow(root);
		}
		//user recognized as store manager "permission=3"
		 else if (user.getLogin() == User.permission.store_mng) {
			StoreManager s = new StoreManager(user.getName(), user.getPass());
			s = LoginController.findSmngr(s);
			System.out.println("store ID:" + s.getS_ID());
			ReportController.storeID = s.getS_ID();
			//opens store manager menu
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/client/StoreMngMenu.fxml").openStream());
			LoginController.changeWindow(root);	
		}
		//user recognized as network employee "permission=4"
		 else if (user.getLogin() == User.permission.network_emp) {
			User net_emp = user;
			System.out.println(net_emp.getName());
			//opens network employee menu
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/catalog/NetWork_Employee_Gui.fxml").openStream());
			LoginController.changeWindow(root);
		}
		//user recognized as network manager "permission=5"
		 else if (user.getLogin() == User.permission.network_mng) {
			CustomerServiceEmployee cust_serv_emp = new CustomerServiceEmployee(user.getName(), user.getPass());
			CompensateController.setCustomerServiceEmployee(cust_serv_emp);
			System.out.println(cust_serv_emp.getName());
			//opens report menu
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/reports/ViewReportMenu.fxml").openStream());
			LoginController.changeWindow(root);
		}
		//user recognized as customer service employee "permission=6"
		 else if (user.getLogin() == User.permission.customer_service_emp) {
			// User net_emp= new User (user.getName(),user.getPass());
			CustomerServiceEmployee cust_serv_emp = new CustomerServiceEmployee(user.getName(), user.getPass());
			CompensateController.setCustomerServiceEmployee(cust_serv_emp);
			System.out.println(cust_serv_emp.getName());
			//opens customer service employee menu
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/compensate_customer/customerServiceEmployeeGui.fxml").openStream());
			LoginController.changeWindow(root);
		}
		//user recognized as system manager "permission=7"
		 else if (user.getLogin() == User.permission.sys_mng) {
			User sys_mng = user;
			System.out.println(sys_mng.getName());
			//opens system manager menu
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/client/ChangePermissionGui.fxml").openStream());
			LoginController.changeWindow(root);	
		}	
	}

}
