package reports;




import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Store;
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
 * This class describe the compare menu gui window.
 * @param stores - list of all the stores in the system.
 * @param quarters - list of all the quarters.
 */
public class CompareMenuGui  implements Initializable {

    @FXML
    private Button ViewReport_BTN;

    @FXML
    private Text wrongDateTxt;

    @FXML
    private TextField year_txt;

    @FXML
    private ComboBox<String> Q_combo= new ComboBox<String>();

    @FXML
    private ComboBox<String> store_combo= new ComboBox<String>();

    @FXML
    private Text year_prompt_txt;
    
    @FXML
    private Text prompt_combo_txt;
    
    @FXML
    private Button btnReturnViewReportMenu;

    
    ObservableList<String> stores;
    ObservableList<String> quarters;

    /**
     * This method is return listener , by pressing this button the window will replaced by view report menu. 
     * @param event
     * @throws Exception
     */
    @FXML
    void returnPressed(ActionEvent event)throws Exception{
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root= null;
		root = loader.load(getClass().getResource("/reports/ViewReportMenu.fxml").openStream());
		ReportController.changeWindow(root);
    }
    /**
     * This method checks all the user input.
     * If the input valid and there is details of the choosen report - transfer to the choosen compare report.
     * Either- shows an appropriate error message.
     * @param event
     */
    @FXML
    void ViewReportPressed(ActionEvent event){
    	try {
    	if(Integer.parseInt(year_txt.getText())>LocalDate.now().getYear())
    	{
    		year_prompt_txt.setVisible(true);
    	}
    	ReportController.year2=Integer.parseInt(year_txt.getText());
    	}
    	catch(Exception e)
    	{
    		year_prompt_txt.setVisible(true);
    	}
    	try {
    		ReportController.quarter2 =Q_combo.getSelectionModel().getSelectedItem();
    		ReportController.storeID2=ReportController.stores.get(store_combo.getSelectionModel().getSelectedIndex()).getS_ID();
    		if (store_combo.getSelectionModel().getSelectedIndex() == -1)
				return;
    		if(ReportController.multiIncomeReport)
    		{
    			
    			if(ReportController.getStoreIncome(ReportController.storeID2,ReportController.quarter2,ReportController.year2)==-9999)
    			{
    				wrongDateTxt.setVisible(true);
    				return;
    			}
    			try {
    				// ADD THE NEW INCOME REPORT TO THE ARRAYLIST
    				ReportController.incomes.add(new IncomeRep(ReportController.storeID2,ReportController.quarter2,"" + ReportController.year2));
    				
    				
    	    		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    				FXMLLoader loader = new FXMLLoader();
    				Pane root;
    				root = loader.load(getClass().getResource("/reports/IncomeReportMulty.fxml").openStream());
    				ReportController.changeWindow(root);
    	    	} catch (IOException e) {
    				e.printStackTrace();
    			}
    			
    		}
    		else if(ReportController.type.equals("IncomeReport"))
    		{
    			if(ReportController.getStoreIncome(ReportController.storeID2,ReportController.quarter2,ReportController.year2)==-9999)
    			{
    				wrongDateTxt.setVisible(true);
    				return;
    			}
    			try {
    	    		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    				FXMLLoader loader = new FXMLLoader();
    				Pane root;
    				root = loader.load(getClass().getResource("/reports/IncomeReportsCompered.fxml").openStream());
    				ReportController.changeWindow(root);
    	    	} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    		if(ReportController.type.equals("ReportOrder"))
    		{
    			if(ReportController.getOrderReport(ReportController.storeID2, ReportController.quarter2, ReportController.year2).get(0).getType().equals("ERROR"))
    			{
    				wrongDateTxt.setVisible(true);
    				return;
    			}
    			try {
    	    		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    				FXMLLoader loader = new FXMLLoader();
    				Pane root;
    				root = loader.load(getClass().getResource("/reports/OrdersReportCompared.fxml").openStream());
    				ReportController.changeWindow(root);
    	    	} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    		else if(ReportController.type.equals("Satisfaction Report"))
    		{
    			if(ReportController.getStoreSatisfaction(ReportController.storeID2, ReportController.quarter2, ReportController.year2).get(0)==-9999)
    			{
    				wrongDateTxt.setVisible(true);
    				return;
    			}
    			try {
    	    		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    				FXMLLoader loader = new FXMLLoader();
    				Pane root;
    				root = loader.load(getClass().getResource("/reports/SatisfactionComparedReport.fxml").openStream());
    				ReportController.changeWindow(root);
    	    	} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    		else if(ReportController.type.equals("Complaint Report"))
    		{
    			ReportController.getComplaintsComp(ReportController.storeID2, ReportController.year2, ReportController.quarter2);
    			if(ReportController.complaintReportComp.get(0).equals("Error"))
    			{
    				wrongDateTxt.setVisible(true);
    				return;
    			}
    			try {
    	    		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    				FXMLLoader loader = new FXMLLoader();
    				Pane root;
    				root = loader.load(getClass().getResource("/reports/ComplaintReportCompare.fxml").openStream());
    				ReportController.changeWindow(root);
    	    	} catch (IOException e) {//
    				e.printStackTrace();
    			}
    		}
    		
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		prompt_combo_txt.setVisible(true);
    	}

    }
    /**
     * This method initialize all the combo boxes and parameters before the gui window appearance.
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		ArrayList<String> al = new ArrayList<String>();	
		for(Store s: ReportController.getStores()){
			al.add(s.getS_name());
		}
		stores = FXCollections.observableArrayList(al);
		store_combo.setItems(stores);
		al= new ArrayList<String>();
		al.add("Q4");
		al.add("Q3");
		al.add("Q2");
		al.add("Q1");
		quarters = FXCollections.observableArrayList(al);
		Q_combo.setItems(quarters);


		
	}

}
