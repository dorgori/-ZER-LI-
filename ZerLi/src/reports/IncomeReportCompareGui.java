package reports;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import catalog.CatalogController;
import client.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
/**
 * This method describe compared income report between 2 stores.
 * @author Sagi
 *
 */
public class IncomeReportCompareGui implements Initializable {

	    @FXML
	    private Text incomeReportTitle_txt;

	    @FXML
	    private Text storeIDvalue_txt;

	    @FXML
	    private Text incomeValue_txt;

	    @FXML
	    private Label storeidprompt_lbl;

	    @FXML
	    private Label incomePrompt_lbl;
	    @FXML
	    private Text storeIDvalue2_txt;

	    @FXML
	    private Text incomeValue2_txt;

	    @FXML
	    private Button backBtn_btn;
	    
	    @FXML
	    private Button cmp_btn;
	    @FXML
	    private Text q_txt;

	    @FXML
	    private Text year_txt;
	    @FXML
	    private Text q_txt2;

	    @FXML
	    private Text year_txt2;

	    @FXML
	    private BarChart<?, ?> income_chart;
	   
	    @FXML
	    private CategoryAxis stores_chart_x;

	    @FXML
	    private NumberAxis income_chart_y;

	    
	    @FXML
	    /**
	     * will open a gui to choose another report 
	     * @param event
	     */
	    void cmp_listener(ActionEvent event) throws IOException {
	    	ReportController.multiIncomeReport=true;
	    	ReportController.incomes.add(new IncomeRep(ReportController.storeID, ReportController.quarter, ""+ ReportController.year));
	    	ReportController.incomes.add(new IncomeRep(ReportController.storeID2, ReportController.quarter2,  ""+ReportController.year2));

	    	ReportController.type="IncomeReport";
	    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			Pane root= null;
			root = loader.load(getClass().getResource("/reports/CompareMenu.fxml").openStream());
			ReportController.changeWindow(root);
	    	return;
	    }
	    
	    /**
	     * This method is return listener, return to View report menu gui window.
	     * @param event
	     * @throws IOException
	     */
	    @FXML
	    void backPress(ActionEvent event) throws IOException {
	    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			Pane root= null;
			root = loader.load(getClass().getResource("/reports/ViewReportMenu.fxml").openStream());
			ReportController.changeWindow(root);
	    }
		
	    /**
	     * This method set all the text and chart values before the gui window appearance.
	     */
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			String storeID=ReportController.getStoreID();
			storeIDvalue_txt.setText(storeID);
			storeIDvalue2_txt.setText(ReportController.storeID2);
			float income=ReportController.getStoreIncome(storeID,ReportController.quarter,ReportController.year);
			incomeValue_txt.setText(""+income);
			float income2 = ReportController.getStoreIncome(ReportController.storeID2,ReportController.quarter2,ReportController.year2);
			incomeValue2_txt.setText(""+income2);
			q_txt.setText(ReportController.quarter);
			year_txt.setText(""+ReportController.year);
			q_txt2.setText(ReportController.quarter2);
			year_txt2.setText(""+ReportController.year2);
			XYChart.Series s= new XYChart.Series<>();
			s.getData().add(new XYChart.Data(""+ReportController.year+","+ReportController.quarter+","+storeID,income));
			s.getData().add(new XYChart.Data(""+ReportController.year2+","+ReportController.quarter2+","+ReportController.storeID2,income2));
			s.setName(ReportController.getStoreName(ReportController.storeID));
			income_chart.getData().addAll(s);// i dont know how to chart
	}
		/**
		 * This method is logout listener, by pressing this button the window replaced by login gui window. 
		 * @param event
		 * @throws Exception
		 */
	    @FXML
	    void logout(ActionEvent event) {
	    	LoginController.userLogOut();
	    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			FXMLLoader loader = new FXMLLoader();
			Pane root;
			try {
				root = loader.load(getClass().getResource("/client/loginGui.fxml").openStream());
				CatalogController.changeWindow(root);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }




}
