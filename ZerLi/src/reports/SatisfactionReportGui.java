package reports;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
 * This class describe the Satisfaction report of specific store.
 * @author Oren
 * @param q1 til q6 - the store's survey answers according to input year and quarter.
 */
public class SatisfactionReportGui implements Initializable {

	private float q1,q2,q3,q4,q5,q6;
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
    private Button btnCompare;

    @FXML
    private Text txtAvg;
    
    /**
     * This method is compare listener, if the user is network manager the button shows up and the user will be able to compare
     * between 2 stores.
     * @author Oren
     * @param event
     * @throws Exception
     */
    @FXML
    private void comparePressed(ActionEvent event)throws Exception{
    	ReportController.type="Satisfaction Report";
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root= null;
		root = loader.load(getClass().getResource("/reports/CompareMenu.fxml").openStream());
		ReportController.changeWindow(root);
    	return;
    }
    /**
     * This method is return listener, return to View report menu gui window.
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
     * This method set all the text and chart values before the gui window appearance.
     * @author Oren
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initAns();
		setChart();
		txtQ1.setText(""+q1);
		txtQ2.setText(""+q2);
		txtQ3.setText(""+q3);
		txtQ4.setText(""+q4);
		txtQ5.setText(""+q5);
		txtQ6.setText(""+q6);
		txtYear.setText(""+ReportController.year);
		txtQuater.setText(ReportController.quarter);
		txtStore.setText(ReportController.storeID);
		if(ReportController.client.user.getLogin()==User.permission.network_mng)
		{
			btnCompare.setVisible(true);
		}
		
	}
	/**
	 * This method set all the avarage grade for each question that gets from the DB by calling method in Report Controller.
	 * @author Oren
	 */
	private void initAns() {
		ArrayList<Float> ans=ReportController.getStoreSatisfaction(ReportController.storeID, ReportController.quarter, ReportController.year);
		q1=ans.get(1);
		q2=ans.get(2);
		q3=ans.get(3);
		q4=ans.get(4);
		q5=ans.get(5);
		q6=ans.get(6);
		txtAvg.setText(""+ans.get(0));
	}
	/**
	 * This method set the chart with store details.
	 * @author Oren
	 */
	private void setChart() {
		XYChart.Series s= new XYChart.Series<>();
		s.getData().add(new XYChart.Data("Q1",q1));
		s.getData().add(new XYChart.Data("Q2",q2));
		s.getData().add(new XYChart.Data("Q3",q3));
		s.getData().add(new XYChart.Data("Q4",q4));
		s.getData().add(new XYChart.Data("Q5",q5));
		s.getData().add(new XYChart.Data("Q6",q6));
		s.setName(ReportController.getStoreName(ReportController.storeID));
		satisfactionChart.getData().addAll(s);
	}

}
