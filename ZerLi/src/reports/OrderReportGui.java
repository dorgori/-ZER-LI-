package reports;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
/**
 * This class describe Order report gui window
 * @author Sagi
 *
 */
public class OrderReportGui implements Initializable{

	ObservableList<ItemInOrderReport> items;
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
    private Button cmp_brn;

    @FXML
    private BarChart<?, ?> productsChart;

    /**
     * This method is compare listener , transfer to compare menu gui by press this button.
     * @param event
     * @throws IOException
     */
    @FXML
    void cmp_btn_hndler(ActionEvent event) throws IOException {
    	ReportController.type="ReportOrder";
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

		if(ReportController.client.user.getLogin()==User.permission.network_mng)
		{
			System.out.println(arr);
			cmp_brn.setVisible(true);
			
		}
		pType_col.setCellValueFactory(new PropertyValueFactory<>("type"));
		ordered_col.setCellValueFactory(new PropertyValueFactory<>("orderCount"));
		profit_col.setCellValueFactory(new PropertyValueFactory<>("profit"));
		if(arr!=null)
		{
			items=FXCollections.observableArrayList(arr);
			report_tbl.setItems(items);
			report_tbl.refresh();
		}
		XYChart.Series s= new XYChart.Series<>();
		for(ItemInOrderReport q: arr)
		s.getData().add(new XYChart.Data(q.getType(),q.getProfit()));
		s.setName(ReportController.getStoreName(ReportController.storeID));
		productsChart.getData().addAll(s);

		
	}
}
