package reports;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
/**
 * This class describe compare order report gui window
 * @author Sagi
 * @param items list of items in order report
 * @param items2 list of items in order report of another store
 *
 */
public class OrderReportComapredGui implements Initializable{

	ObservableList<ItemInOrderReport> items;
	ObservableList<ItemInOrderReport> items2;
    @FXML
    private TableView<ItemInOrderReport> report_tbl;
    @FXML
    private TableColumn<ItemInOrderReport,String> pType_col;

    @FXML
    private TableColumn<ItemInOrderReport, Float> profit_col;

    @FXML
    private TableColumn<ItemInOrderReport, Integer> ordered_col;

    @FXML
    private Button back_btn;

    @FXML
    private Text sid_txt;

    @FXML
    private Text year_txt;

    @FXML
    private Text q_txt;

    @FXML
    private Text sid_txt1;

    @FXML
    private Text q_txt1;

    @FXML
    private Text year_txt1;

    @FXML
    private  TableView<ItemInOrderReport> report_tbl1;

    @FXML
    private TableColumn<ItemInOrderReport,String> pType_col1;

    @FXML
    private TableColumn<ItemInOrderReport, Float> profit_col1;

    @FXML
    private  TableColumn<ItemInOrderReport, Integer> ordered_col1;

    @FXML
    private BarChart<?, ?> compare_chart;

    @FXML
    private CategoryAxis x_axi;

    @FXML
    private NumberAxis y_axi;

    /**
     * This method is return listener, return to View report menu gui window.
     * @param event
     * @throws IOException
     */
    @FXML
    void back_btn_hndler(ActionEvent event) throws IOException {
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
	public void initialize(URL arg0, ResourceBundle arg1) {
		ArrayList<ItemInOrderReport> arr= ReportController.getOrderReport(ReportController.storeID,ReportController.quarter,ReportController.year);
		ArrayList<ItemInOrderReport> arr2= ReportController.getOrderReport(ReportController.storeID2,ReportController.quarter2,ReportController.year2);

		q_txt.setText(ReportController.quarter);
		q_txt1.setText(ReportController.quarter2);
		year_txt.setText(""+ReportController.year);
		year_txt1.setText(""+ReportController.year2);
		sid_txt.setText(ReportController.storeID);
		sid_txt1.setText(ReportController.storeID2);
		
		pType_col.setCellValueFactory(new PropertyValueFactory<>("type"));
		ordered_col.setCellValueFactory(new PropertyValueFactory<>("orderCount"));
		profit_col.setCellValueFactory(new PropertyValueFactory<>("profit"));
		
		pType_col1.setCellValueFactory(new PropertyValueFactory<>("type"));
		ordered_col1.setCellValueFactory(new PropertyValueFactory<>("orderCount"));
		profit_col1.setCellValueFactory(new PropertyValueFactory<>("profit"));
		if(arr2!=null)
			items2=FXCollections.observableArrayList(arr2);
		if(arr!=null)
			items=FXCollections.observableArrayList(arr);
		if(arr!=null)// make sure its not empty
			report_tbl.setItems(items);
		report_tbl.refresh();
		if (arr2!=null)
			report_tbl1.setItems(items2);
		report_tbl1.refresh();
		
		XYChart.Series s2 = new XYChart.Series();
		XYChart.Series s= new XYChart.Series<>();
		for(ItemInOrderReport i: arr)
		{
			s.getData().add(new XYChart.Data(i.getType(),i.getProfit()));
		}
		for(ItemInOrderReport i: arr2)
		{
			s2.getData().add(new XYChart.Data(i.getType(),i.getProfit()));
		}
		s.setName(ReportController.getStoreName(ReportController.storeID));
		s2.setName(ReportController.getStoreName(ReportController.storeID2));
		compare_chart.getData().addAll(s,s2);
		
	}
}
