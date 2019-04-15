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
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
/**
 * This class describe income report gui window
 * @author Sagi
 */
public class IncomeReportGui implements Initializable {

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
    private Button backBtn_btn;
    
    @FXML
    private Button cmp_btn;
    @FXML
    private Text q_txt;

    @FXML
    private Text year_txt;
    
    @FXML
    private BarChart<?, ?> incomeChart;

    @FXML
    /**
     * will open a gui to choose another report 
     * @param event
     */
    void cmp_listener(ActionEvent event) throws IOException {
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
		ReportController.multiIncomeReport=false;
		ReportController.incomes=new ArrayList<IncomeRep>();
		String storeID=ReportController.getStoreID();
		storeIDvalue_txt.setText(storeID);
		float income=ReportController.getStoreIncome(storeID,ReportController.quarter,ReportController.year);
		incomeValue_txt.setText(""+income);
		q_txt.setText(ReportController.quarter);
		year_txt.setText(""+ReportController.year);
		if(ReportController.client.user.getLogin()==User.permission.network_mng)
		{
			cmp_btn.setVisible(true);
		}
		
		XYChart.Series s= new XYChart.Series<>();
		s.getData().add(new XYChart.Data(""+ReportController.year+","+ReportController.quarter+","+storeID,income));
		s.setName(ReportController.getStoreName(ReportController.storeID));
		incomeChart.getData().addAll(s);
		
	}
	
}



