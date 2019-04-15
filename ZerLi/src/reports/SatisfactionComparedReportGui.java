package reports;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import catalog.CatalogController;
import client.LoginController;
import client.User;
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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
/**
 * This class describe the Compared Satisfaction report between 2 stores gui window.
 * @author Oren
 * @param arr1 - the survey answers of store 1 according to input year and quarter.
 * @param arr2 - the survey answers of store 2 according to input year and quarter.
 * @param storeID- store1 ID, quarter- store1 quarter choice, year- store 1 year choice.
 * @param storeID2- store2 ID, quarter2- store2 quarter choice, year2- store 2 year choice.
 * @param ans1 - the report of store 1 according quarter and year.
 * @param ans2 - the report of store 2 according quarter and year.
 */
public class SatisfactionComparedReportGui implements Initializable {
	private float []arr1=new float[7];
	private float []arr2=new float[7];
	private ArrayList<Float> ans1,ans2;
	private String storeID=ReportController.storeID,storeID2=ReportController.storeID2,quarter=ReportController.quarter,quarter2=ReportController.quarter2;
	int year=ReportController.year,year2=ReportController.year2;

	@FXML
    private Text txtYear;

    @FXML
    private Text txtQuater;

    @FXML
    private Text txtStore;

    @FXML
    private Text txtQ1;

    @FXML
    private Text txtQ2;

    @FXML
    private Text txtQ3;

    @FXML
    private Text txtQ4;

    @FXML
    private Text txtQ5;

    @FXML
    private Text txtQ6;

    @FXML
    private BarChart<?, ?> satisfactionChart;

    @FXML
    private CategoryAxis questions_chart_x;

    @FXML
    private NumberAxis satisfy_chart_y;

    @FXML
    private Button btnReturn;

    @FXML
    private Text txtYear1;

    @FXML
    private Text txtQuater1;

    @FXML
    private Text txtStore1;

    @FXML
    private Text txtQ11;

    @FXML
    private Text txtQ21;

    @FXML
    private Text txtQ31;

    @FXML
    private Text txtQ41;

    @FXML
    private Text txtQ51;

    @FXML
    private Text txtQ61;

    @FXML
    private Button btnCompare;

    @FXML
    private Text txtStoreAvg;

    @FXML
    private Text txtStoreAvg1;


    /**
     * This method open a gui window to choose store to compare with store 1.
     * @author Oren 
     * @param event
     */    
    	@FXML
	    void cmpPress(ActionEvent event) throws IOException {
	    	ReportController.type="Satisfaction Report";
	    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			Pane root= null;
			root = loader.load(getClass().getResource("/reports/CompareMenu.fxml").openStream());
			ReportController.changeWindow(root);
	    }
    /**
     * This method return to View report menu gui window to choose any report to choose.
     * @author Oren
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
	  * This method initialize all the parameters before this gui window appearance.
	  * Sets all the reports, texts, and charts.
	  * @author Oren
	  */
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			ans1=ReportController.getStoreSatisfaction(storeID, quarter, year);
			ans2=ReportController.getStoreSatisfaction(storeID2, quarter2, year2);
			int i=0;
			for(float q:ans1) {
				arr1[i]=q;
				i++;
			}
			txtStoreAvg.setText(""+arr1[0]);
			i=0;
			for(float q:ans2) {
				arr2[i]=q;
				i++;
			}
			txtStoreAvg1.setText(""+arr2[0]);
			
			
			setChart();
			setGuiTexts();

			if(ReportController.client.user.getLogin()==User.permission.network_mng)
			{
				btnCompare.setVisible(true);
			}
	}
		/**
		 * This method set all the text values according to the parameters.
		 * @author Oren
		 */
		private void setGuiTexts() {
			txtYear.setText(""+year);
			txtQuater.setText(quarter);
			txtStore.setText(storeID);	
			txtYear1.setText(""+year2);
			txtQuater1.setText(quarter2);
			txtStore1.setText(storeID2);
			txtQ1.setText(""+arr1[1]);
			txtQ2.setText(""+arr1[2]);
			txtQ3.setText(""+arr1[3]);
			txtQ4.setText(""+arr1[4]);
			txtQ5.setText(""+arr1[5]);
			txtQ6.setText(""+arr1[6]);
			txtQ11.setText(""+arr2[1]);
			txtQ21.setText(""+arr2[2]);
			txtQ31.setText(""+arr2[3]);
			txtQ41.setText(""+arr2[4]);
			txtQ51.setText(""+arr2[5]);
			txtQ61.setText(""+arr2[6]);
		}
		/**
		 * This method set the chart with the details of 1'st store and 2'nd store.
		 * @author Oren
		 */
		private void setChart() {
			XYChart.Series s= new XYChart.Series<>();
			XYChart.Series s2= new XYChart.Series<>();
			s.getData().add(new XYChart.Data("Q1",arr1[1]));
			s.getData().add(new XYChart.Data("Q2",arr1[2]));
			s.getData().add(new XYChart.Data("Q3",arr1[3]));
			s.getData().add(new XYChart.Data("Q4",arr1[4]));
			s.getData().add(new XYChart.Data("Q5",arr1[5]));
			s.getData().add(new XYChart.Data("Q6",arr1[6]));
			s2.getData().add(new XYChart.Data("Q1",arr2[1]));
			s2.getData().add(new XYChart.Data("Q2",arr2[2]));
			s2.getData().add(new XYChart.Data("Q3",arr2[3]));
			s2.getData().add(new XYChart.Data("Q4",arr2[4]));
			s2.getData().add(new XYChart.Data("Q5",arr2[5]));
			s2.getData().add(new XYChart.Data("Q6",arr2[6]));
			s.setName(ReportController.getStoreName(ReportController.storeID));
			s2.setName(ReportController.getStoreName(ReportController.storeID2));
			satisfactionChart.getData().addAll(s,s2);
		}
		
		/**
		 * This method is logout listener, set the user not active and return to login gui window.
		 * @author Oren
		 * @param event
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
