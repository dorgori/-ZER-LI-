package survey;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.LoginController;
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
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class SurveyGui implements Initializable, Serializable{
	private ObservableList<String> answer_list;
	private ObservableList<String> store_list;
	private static int selectedIndex=-1;
	private String selectedString="";
	public int []answers = new int [6];
	
	public ArrayList<Store> arr = new ArrayList<Store>();
	public String chosen_store_id="";
    @FXML
    private Label survey;

    @FXML
    private Button submit;

    @FXML
    private ComboBox<String> answer1;

    @FXML
    private ComboBox<String> answer2;

    @FXML
    private ComboBox<String> answer3;

    @FXML
    private ComboBox<String> answer4;

    @FXML
    private ComboBox<String> answer5;

    @FXML
    private ComboBox<String> answer6;

    @FXML
    private ComboBox<String> choose_store;

    @FXML
    private Text error_not_6_ans;
    @FXML
    private Text chose_store_error_txt;
    
    @FXML
    private Button close;
/**
 * adding into array of answers the number of answer that was chosen 
 * @param event
 */
    
    @FXML
    void choose_ans1(ActionEvent event) {
    	selectedIndex = answer1.getSelectionModel().getSelectedIndex();
    	if(selectedIndex>=0)
    	{
    	answers[0]=(++selectedIndex);
    	}
    	else {
    		error_not_6_ans.setVisible(true);
    		
    	}
    }
    /**
     * adding into array of answers the number of answer that was chosen 
     * @param event
     */
    @FXML
    void choose_ans2(ActionEvent event) {
    	selectedIndex = answer2.getSelectionModel().getSelectedIndex();
    	if(selectedIndex>=0)
    	{
    	answers[1]=(++selectedIndex);
    	}
    	else {
    		error_not_6_ans.setVisible(true);
    		
    	}
    	
    }
    /**
     * adding into array of answers the number of answer that was chosen 
     * @param event
     */
    @FXML
    void choose_ans3(ActionEvent event) {
    	selectedIndex = answer3.getSelectionModel().getSelectedIndex();
    	if(selectedIndex>=0)
    	{
    	answers[2]=(++selectedIndex);
    	}
    	else {
    		error_not_6_ans.setVisible(true);
    		
    	}
    }
    /**
     * adding into array of answers the number of answer that was chosen 
     * @param event
     */
    @FXML
    void choose_ans4(ActionEvent event) {
    	selectedIndex = answer4.getSelectionModel().getSelectedIndex();
    	if(selectedIndex>=0)
    	{
    	answers[3]=(++selectedIndex);
    	}
    	else {
    		error_not_6_ans.setVisible(true);
    		
    	}
    }
    /**
     * adding into array of answers the number of answer that was chosen 
     * @param event
     */
    @FXML
    void choose_ans5(ActionEvent event) {
    	selectedIndex = answer5.getSelectionModel().getSelectedIndex();
    	if(selectedIndex>=0)
    	{
       	answers[4]=(++selectedIndex);
    	}
    	else {
    		error_not_6_ans.setVisible(true);
    		
    	}
    }
    /**
     * adding into array of answers the number of answer that was chosen 
     * @param event
     */
    @FXML
    void choose_ans6(ActionEvent event) {
    	selectedIndex = answer6.getSelectionModel().getSelectedIndex();
    	if(selectedIndex>=0)
    	{
    	answers[5]=(++selectedIndex);
    	}
    	else {
    		error_not_6_ans.setVisible(true);
    		
    	}
    }
    /**
     * Stores the store to which the customer answering the survey belongs
     * @param event
     */
    @FXML
    void chose_store(ActionEvent event) {
    	selectedString = choose_store.getSelectionModel().getSelectedItem();
    	String[] str= selectedString.split(" ");
    		chosen_store_id=str[0];
    }
    /**
     * check details and send the survey to survey comtroller- in order to save it in data  base.
     * @param event
     * @throws IOException
     */
    @FXML
    void submit(ActionEvent event) throws IOException {
    	int flag=1;
    	chose_store_error_txt.setVisible(false);
		error_not_6_ans.setVisible(false);
    	for (int i =0;i<6;i++) 
    	{
    		if ( answers[i]<=0 ||(answers[i])>11)
			{
    			error_not_6_ans.setVisible(true);
				System.out.printf("you didn't answer qustuin number %d \n ",i);
				flag=0;
			}
    	
    	}
    	 if(chosen_store_id==""||chosen_store_id==null) {
			flag=0;
			chose_store_error_txt.setVisible(true);
		}
    	if(flag==1)
    	{	
    		for (int i =0;i<6;i++) 
    			System.out.println(answers[i]);
    		
    		Survey survey= new Survey (answers,chosen_store_id);
    		survey.setCreatedDate(SurveyController.getDate());
    		
     		SurveyController.addSurvey(survey);
     		
    		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    		FXMLLoader loader = new FXMLLoader();
    		Pane root = loader.load(getClass().getResource("/client/StoreEmployeeMenu.fxml").openStream());
        	SurveyController.changeWindow(root);
         }
   }
    /**
     * insert to combo box the numbers of answers 1-10
     */
 private void   update_choose_comBox(){
	 ArrayList<String> arr = new ArrayList<String>();
		for(int i=1;i<=10;i++)
			arr.add(""+i);
		answer_list = FXCollections.observableArrayList(arr);
		answer1.setItems(answer_list);
		answer2.setItems(answer_list);
		answer3.setItems(answer_list);
		answer4.setItems(answer_list);
		answer5.setItems(answer_list);
		answer6.setItems(answer_list);
    }
 /**
  * insert to store options the stores from data base
  */
	private void update_choose_store() {	
	ArrayList<String> store_arr = new ArrayList<String>();
	arr= SurveyController.get_stores();
	for(int i=0; i<arr.size();i++) {
		if(arr.get(i).getS_ID().equals(LoginController.sEmployee.getS_ID()))
			store_arr.add(""+arr.get(i).getS_ID()+" "+arr.get(i).getS_name());
	}
	store_list = FXCollections.observableArrayList(store_arr);
	choose_store.setItems(store_list);
	}
 /**
  * go back to the prvious window
  * @param event
  * @throws IOException
  */
 @FXML
 void close(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/client/StoreEmployeeMenu.fxml").openStream());
    	SurveyController.changeWindow(root);
 }
 
 /**
  * initialize the combo boxes before upload the window
  */
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		update_choose_comBox();
		update_choose_store();
	}


}
