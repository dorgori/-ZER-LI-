package reports;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.LoginController;
import client.Store;
import client.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
/**
 * This class describe the main menu of all the report, the user can choose any report that he wants.
 * @param stores - list of all the stores in the system.
 * @param quarters - list of all the quarters.
 * @param types - list of all the report types that existed.
 */
public class ViewReportMenuGui implements Initializable {

	@FXML
	private Button ViewReport_BTN;

	@FXML
	private ComboBox<String> type_combo = new ComboBox<String>();

	@FXML
	private TextField year_txt;

	@FXML
	private ComboBox<String> Q_combo = new ComboBox<String>();

	@FXML
	private ComboBox<String> store_combo = new ComboBox<String>();

	@FXML
	private Text year_prompt_txt;

	@FXML
	private Text prompt_combo_txt;

	@FXML
	private Button btnReturnStoreMng;

	@FXML
	private Button btnLogout;

	@FXML
	private Text wrongDateTxt;
	ObservableList<String> stores;
	ObservableList<String> quarters;
	ObservableList<String> types;

	/**
	 * This method checks all the details that the user input if all valid and there is details to show, if existed transfet to the choosen report
	 * either - shows error message.
	 * @param event
	 */
	@FXML
	void ViewReportPressed(ActionEvent event) {
		try {
			if (Integer.parseInt(year_txt.getText()) > LocalDate.now().getYear()) {
				year_prompt_txt.setVisible(true);
			}
			else {
				year_prompt_txt.setVisible(false);
			}
			ReportController.year = Integer.parseInt(year_txt.getText());
		} catch (Exception e) {
			year_prompt_txt.setVisible(true);
		}

		/*
		 * validate there is a report for selected date if it works then change window
		 */

		try {
			ReportController.quarter = Q_combo.getSelectionModel().getSelectedItem();
			if (ReportController.client.user.getLogin() == User.permission.network_mng)
				ReportController.storeID = ReportController.stores
						.get(store_combo.getSelectionModel().getSelectedIndex()).getS_ID();

			if (type_combo.getSelectionModel().getSelectedItem().equals("Income Report")) {
				if (ReportController.getStoreIncome(ReportController.storeID, ReportController.quarter,
						ReportController.year) == -9999) {
					wrongDateTxt.setVisible(true);
					return;
				}
				try {
					((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
					FXMLLoader loader = new FXMLLoader();
					Pane root;
					root = loader.load(getClass().getResource("/reports/IncomeReport.fxml").openStream());
					ReportController.changeWindow(root);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (type_combo.getSelectionModel().getSelectedItem().equals("Order Report")) {
				try {
					if (ReportController
							.getOrderReport(ReportController.storeID, ReportController.quarter, ReportController.year)
							.get(0).getType().equals("ERROR")) {
						wrongDateTxt.setVisible(true);
						return;
					}
					((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
					FXMLLoader loader = new FXMLLoader();
					Pane root;
					System.out.println("almost");
					root = loader.load(getClass().getResource("/reports/OrdersReport.fxml").openStream());
					ReportController.changeWindow(root);
				} catch (IOException e) {//
					e.printStackTrace();
				}
			} else if (type_combo.getSelectionModel().getSelectedItem().equals("Customer Complaint Report")) {
				try {
					ReportController.getComplaints(ReportController.storeID, ReportController.year, ReportController.quarter);
					if (ReportController.complaintReport.get(0).equals("Error"))
					{	
						wrongDateTxt.setVisible(true);
						return;
					}
					((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
					FXMLLoader loader = new FXMLLoader();
					Pane root;
					System.out.println("almost");
					root = loader.load(getClass().getResource("/reports/CustomerComplaintReport.fxml").openStream());
					ReportController.changeWindow(root);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (type_combo.getSelectionModel().getSelectedItem().equals("Satisfaction Report")) {
				if (ReportController
						.getStoreSatisfaction(ReportController.storeID, ReportController.quarter, ReportController.year)
						.get(0) == -9999) {
					wrongDateTxt.setVisible(true);
					return;
				}
				try {
					((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
					FXMLLoader loader = new FXMLLoader();
					Pane root;
					root = loader.load(getClass().getResource("/reports/SatisfactionReport.fxml").openStream());
					ReportController.changeWindow(root);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			prompt_combo_txt.setVisible(true);
		}

	}
	/**
	 * This method initialize all the text and combo boxes in the gui window before the window appearance.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {// fix

		ArrayList<String> al = new ArrayList<String>();
		for (Store s : ReportController.getStores()) {
			al.add(s.getS_name());
		}
		stores = FXCollections.observableArrayList(al);
		store_combo.setItems(stores);
		al = new ArrayList<String>();
		al.add("Q4");
		al.add("Q3");
		al.add("Q2");
		al.add("Q1");
		quarters = FXCollections.observableArrayList(al);
		Q_combo.setItems(quarters);
		al = new ArrayList<String>();
		al.add("Income Report");
		al.add("Order Report");
		al.add("Customer Complaint Report");
		al.add("Satisfaction Report");
		types = FXCollections.observableArrayList(al);
		type_combo.setItems(types);
		if (ReportController.client.user.getLogin() == User.permission.network_mng) {
			System.out.println("nwmngr");
			store_combo.setVisible(true);
		}
		if (LoginController.client.user.getStLogin().equals("network_mng")) {
			btnReturnStoreMng.setVisible(false);
			btnLogout.setVisible(true);
		} else if (LoginController.client.user.getStLogin().equals("store_mng")) {
			btnReturnStoreMng.setVisible(true);
			btnLogout.setVisible(false);
		}
	}
	/**
	 * This method is return listener, if the user is network manager this button never shows up.
	 * If the user is store manager by pressing this button transfer him to the store manager menu.
	 * @author Oren
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void returnToStoreMng(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/client/StoreMngMenu.fxml").openStream());
		ReportController.changeWindow(root);
	}
	/**
	 * This method is logout listener, if the user is store manager this button never shows up.
	 * If the user is network manager by pressing this button transfer him to login gui window. 
	 * @param event
	 * @throws Exception
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
