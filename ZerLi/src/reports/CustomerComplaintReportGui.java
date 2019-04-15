package reports;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.LoginController;
import client.User.permission;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
/**
 * This class describe Customer complaint report gui window
 * @author Dor
 * @param report - ArrayList of complaints according chosen quarter and year.
 */
public class CustomerComplaintReportGui implements Initializable {
	ArrayList<String> report;

	@FXML
	private Text txtNumComplaint;

	@FXML
	private Text txtAmount;

	@FXML
	private Button btnCompare;

	@FXML
	private BarChart<?, ?> complaintChart;

	@FXML
	private Text txtYear;

	@FXML
	private Text txtNumberCompensate;

	@FXML
	private Button btnBack;

	@FXML
	private Text txtQuarter;

	@FXML
	private Text txtStoreID;

	/**
     * This method set all the text values before the gui window appearance.
     * @author Dor
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initTextFields();
	}
	/**
	 * This method set all text and chart details.
	 * @author Dor
	 */
	private void initTextFields() { // check
		String storeID = ReportController.storeID;
		String quarter = ReportController.quarter;
		int year = ReportController.year;
		report = ReportController.complaintReport;
		txtStoreID.setText(storeID);
		txtQuarter.setText(quarter);
		txtYear.setText(Integer.toString((year)));
		txtNumComplaint.setText(report.get(3));
		txtNumberCompensate.setText(report.get(4));
		txtAmount.setText(report.get(5));
		initCharts();
		checkUser();
	}
	/**
	 * This method set chart details
	 * @author Dor
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initCharts() {
		XYChart.Series s = new XYChart.Series<>();
		s.getData().add(new XYChart.Data("Complaints In Quarter", Integer.parseInt(report.get(3))));
		s.getData().add(new XYChart.Data("Total Compensation", Float.parseFloat(report.get(5))));
		s.setName(ReportController.getStoreName(ReportController.storeID));
		complaintChart.getData().addAll(s);

	}
	/**
     * This method is return listener, return to View report menu gui window.
     * @author Dor
     * @param event
     * @throws IOException
     */
	@FXML
	void backPress(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = null;
		root = loader.load(getClass().getResource("/reports/ViewReportMenu.fxml").openStream());
		ReportController.changeWindow(root);
	}
	/**
	 * This method checks the user permission and shows up compare button if he is network manager.
	 * @author Dor
	 */
	void checkUser() {
		if (LoginController.activeUser.getLogin() == permission.network_mng)
			btnCompare.setVisible(true);
	}
	/**
	 * This method is compare listener, changed window to compare menu.
	 * @param event
	 * @throws Exception
	 */
	@FXML
	private void comparePressed(ActionEvent event) throws Exception {
		ReportController.type = "Complaint Report";
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = null;
		root = loader.load(getClass().getResource("/reports/CompareMenu.fxml").openStream());
		ReportController.changeWindow(root);
	}

}
