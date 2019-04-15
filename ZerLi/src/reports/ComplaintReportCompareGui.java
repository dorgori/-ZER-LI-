
package reports;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
 * This class describe compare complaint report between 2 stores gui window
 * @author Dor
 * @param report 1 - ArrayList of complaint: all the complaints of 1'st store in the chosen year and quarter.
 * @param report 2 - ArrayList of complaint: all the complaints of 2'nd store in the chosen year and quarter.
 */
public class ComplaintReportCompareGui implements Initializable {

	private ArrayList<String> report1;
	private ArrayList<String> report2;

	@FXML
	private BarChart<?, ?> complaintChart;

	@FXML
	private Text txtAmount;

	@FXML
	private Text txtYear;

	@FXML
	private Text txtQuarter;

	@FXML
	private Text txtStoreID;

	@FXML
	private Text txtNumComplaint;

	@FXML
	private Text txtNumberCompensate;

    @FXML
    private Button btnReturn;
    
    @FXML
    private Button btnCompareOther;

	@FXML
	private Text txtAmount1;

	@FXML
	private Text txtYear1;

	@FXML
	private Text txtQuarter1;

	@FXML
	private Text txtStoreID1;

	@FXML
	private Text txtNumComplaint1;

	@FXML
	private Text txtNumberCompensate1;

	/**
     * This method set all the text values before the gui window appearance.
     * @author Dor
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
		report1 = ReportController.complaintReport;
		txtStoreID.setText(storeID);
		txtQuarter.setText(quarter);
		txtYear.setText(Integer.toString((year)));
		txtNumComplaint.setText(report1.get(3));
		txtNumberCompensate.setText(report1.get(4));
		txtAmount.setText(report1.get(5));
		String storeID2 = ReportController.storeID2;
		String quarter2 = ReportController.quarter2;
		int year2 = ReportController.year2;
		report2 = ReportController.complaintReportComp;
		txtStoreID1.setText(storeID2);
		txtQuarter1.setText(quarter2);
		txtYear1.setText(Integer.toString((year2)));
		txtNumComplaint1.setText(report2.get(3));
		txtNumberCompensate1.setText(report2.get(4));
		txtAmount1.setText(report2.get(5));
		initCharts();
	}
	/**
	 * This method set chart details
	 * @author Dor
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initCharts() {
		XYChart.Series s = new XYChart.Series<>();
		XYChart.Series s1 = new XYChart.Series<>();
		s.getData().add(new XYChart.Data("Complaints In Quarter", Integer.parseInt(report1.get(3))));
		s.getData().add(new XYChart.Data("Total Compensation", Float.parseFloat(report1.get(5))));
		s1.getData().add(new XYChart.Data("Complaints In Quarter", Integer.parseInt(report2.get(3))));
		s1.getData().add(new XYChart.Data("Total Compensation", Float.parseFloat(report2.get(5))));
		s.setName(ReportController.getStoreName(ReportController.storeID));
		s1.setName(ReportController.getStoreName(ReportController.storeID2));
		complaintChart.getData().addAll(s, s1);
	}
	/**
	 * This method is compare listener, changed window to compare menu.
	 * @param event
	 * @throws Exception
	 */
	@FXML
    void cmpWithOther(ActionEvent event) throws IOException {
    	ReportController.type="Complaint Report";
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root= null;
		root = loader.load(getClass().getResource("/reports/CompareMenu.fxml").openStream());
		ReportController.changeWindow(root);
    }
	/**
     * This method is return listener, return to View report menu gui window.
     * @author Dor
     * @param event
     * @throws IOException
     */
	@FXML
    void btnReturnPressed(ActionEvent event) throws IOException {
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root= null;
		root = loader.load(getClass().getResource("/reports/ViewReportMenu.fxml").openStream());
		ReportController.changeWindow(root);
    }
	
}
