package reports;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
 * This class describe compare income report between more then 2 stores. 
 * @author Sagi
 *
 */
public class IncomeReportMulty implements Initializable{


    @FXML
    private Text incomeReportTitle_txt;

    @FXML
    private Button backBtn_btn;
    
    @FXML
    private Button cmp_btn;
    
    @FXML
    private BarChart<?, ?> income_chart;
    @FXML
    private CategoryAxis stores_chart_x;

    @FXML
    private NumberAxis income_chart_y;

    /**
     * will open a gui to choose another report 
     * @param event
     */
    @FXML
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
    	ReportController.multiIncomeReport=false;
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
		XYChart.Series s= new XYChart.Series<>();
		for(IncomeRep i:ReportController.incomes)
		{
			s.getData().add(new XYChart.Data(""+i.getYear()+","+i.getQ()+","+i.getId(),i.getIncome()));
		}
		
		income_chart.getData().addAll(s);
}


}
