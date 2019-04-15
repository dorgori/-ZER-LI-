package client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import catalog.CatalogController;
import client.User.permission;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * Describes the a gui that system admin can have access to and change permissions of different users
 * @param userList an observable list of users to be shown in the table
 * @param userPermission an observable list of strings of the user permissions that is shown in the table
 * @param  
 *
 */
public class ChangePermissionGui implements Initializable{

	private ObservableList<User> userList;
	private ObservableList<String> userPermission;
	private ObservableList<String> storesList;
	private User selectedUser;
	private int selectedPermission=-1;
	private int selectedStore=-1;
	private ArrayList<Store> stores;

    @FXML
    private TableView<User> tblUsers;
    @FXML
	private TableColumn<User, String> clmUsername;
	@FXML
	private TableColumn<User, String> clmPermission;
    @FXML
    private TableColumn<User, String> clmBlocked;
	@FXML
    private Button btnSave;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnEdit;
    @FXML
    private TextField txtUsername;
    @FXML
    private Text lblUsername;
    @FXML
    private Text lblPermission;
    @FXML
    private Text txtErrMess;
    @FXML
    private ComboBox<String> cmbPermission;
    @FXML
    private Text lblStores;
    @FXML
    private ComboBox<String> cmbStores;
    @FXML
    private Text lblException;
    
    /**
     * This method gets store ArrayList from server by calling getStoresList() in the client, set the static stores ArrayList
     * of the class. After that the method initialize the values of stores id to list that shows in the store combo box. 
     * @author Oren
     */
    private void setStores(){
    	stores= LoginController.getStoresList();
    	ArrayList<String> arr= new ArrayList<String>();
    	for(Store st:stores)
    		arr.add(st.getS_name());
    	storesList=FXCollections.observableArrayList(arr);
    	cmbStores.setItems(storesList);
    }
    /**
     * This method is a listener of store combo box, saves the selected index in selectedStore to handle case that 
     * the user didn't select anything.
     * @author Oren
     * @param event
     * @throws Exception
     */
    @FXML
    private void choosingStore(ActionEvent event)throws Exception{
    	selectedStore=cmbStores.getSelectionModel().getSelectedIndex();
    }
    /**
     * This method is a listener of new permission combo box, saves the selected index in selectedPermission to handle case that 
     * the user didn't select anything. If the user chose store Employee/Manager the relevant Combo box will show.
     * @author Oren
     * @param event
     * @throws Exception
     */
    @FXML
    private void choosingNewPermission(ActionEvent event)throws Exception{
    	selectedPermission=cmbPermission.getSelectionModel().getSelectedIndex();
    	if(selectedPermission==0 || selectedPermission==1) {//store employee/manager
    		cmbStores.setVisible(true);
    		lblStores.setVisible(true);
    	}
    	else {
    		cmbStores.setVisible(false);
    		lblStores.setVisible(false);
    	}
    }
    /**
     * This method allocating all the details of the combo boxes and send it to the next method in LoginController then next to the client
     * and server to update the changes in the DB.
     * If the chosen user is store employee or store manager the method remove the specific user from the table in the db and update
     * according to new choise. If the new choose is store employee or store manager the method add the specific user to the relevant table in the db. 
     * @author Oren
     * @param event
     * @throws Exception
     */
    @FXML
    public void btnSaveListenner(ActionEvent event)throws Exception{
    	if(selectedPermission==-1) {
    		return;
    	}
    	String newStLogin=cmbPermission.getSelectionModel().getSelectedItem();
    	permission newPer = LoginController.activeUser.converteStringToPermission(newStLogin);
    	if(cmbPermission.getSelectionModel().getSelectedItem().equals(permission.store_emp) 
    			||cmbPermission.getSelectionModel().getSelectedItem().equals(permission.store_mng)) {
    		if(selectedStore==-1) {
        		lblException.setVisible(true);
        		return;
        	}
    	}
    	if(selectedUser.getStLogin().equals("store_emp")) {
    		LoginController.deleteStoreEmpFromDB(selectedUser.getName());
    	}
    	else if(selectedUser.getStLogin().equals("store_mng")) {
    		LoginController.deleteStoreMngFromDB(selectedUser.getName());
    	}
    	else if(selectedUser.getStLogin().equals("customer")) {
    		LoginController.deleteCustomerFromDB(selectedUser.getName());
    	}
    	if(newPer.equals(permission.store_emp)){
    		LoginController.addStoreEmpToDB(selectedUser.getName(),stores.get(cmbStores.getSelectionModel().getSelectedIndex()).getS_ID());
    	}
    	else if(newPer.equals(permission.store_mng)){
    		LoginController.addStoreMngToDB(selectedUser.getName(),stores.get(cmbStores.getSelectionModel().getSelectedIndex()).getS_ID());
    	}
    	else if(newPer.equals(permission.blocked_user)){
    		selectedUser.setBlocked(1);
    	}
    	else if(newPer.equals(permission.unblock_user)){
    		selectedUser.setBlocked(0);
    	}
    	selectedUser.setStLogin(newPer);
    	LoginController.updatePermission(selectedUser);
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/client/ChangePermissionGui.fxml").openStream());
    	CatalogController.changeWindow(root);
    }
	/**
	 * The function checks if the user that we want to edit his permission is logged in, yes - show error message / no- shows the details.
	 * @author Oren 
	 * @param event
	 * @throws Exception
	 */
    @FXML
    public void chooseUserToEdit(ActionEvent event)throws Exception{
		selectedUser=tblUsers.getSelectionModel().getSelectedItem();
		txtErrMess.setVisible(false);
    	if(LoginController.checkActiveUser(selectedUser.getName())==0) {
        	txtUsername.setText(selectedUser.getName());
        	lblPermission.setVisible(true);
        	lblUsername.setVisible(true);
        	txtUsername.setVisible(true);
        	cmbPermission.setVisible(true);
        	btnSave.setVisible(true);
    	}
    	else {
    		txtErrMess.setVisible(true);
    	}
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ArrayList<User> arr= LoginController.getUsersList();
		setStores();
		setUsers(arr);
		setCellTable();
		setComboBox();
		tblUsers.setItems(userList);
		tblUsers.refresh();
	}
	/**
	 * This method is logout listener - exit from the gui to the first login gui.
	 * @author Oren
	 * @param event
	 * @throws IOException
	 */
	@FXML
    void btnLogoutListenner(ActionEvent event) throws IOException {
		LoginController.userLogOut();
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/client/loginGui.fxml").openStream());
    	CatalogController.changeWindow(root);
    }
	/**
	 * This method initialize the permission combo box according to user permissions.
	 * @author Oren
	 */
	private void setComboBox()
	{
		ArrayList<String> permissions= new ArrayList<String>();
		permission[] arr=permission.values();
		for(int i=2;i<arr.length;i++) {
			permissions.add(LoginController.client.user.convertePermissionToString(arr[i]));
		}
		userPermission=FXCollections.observableArrayList(permissions);
		cmbPermission.setItems(userPermission);
	}
	/**
	 * This method set the cells of the table.
	 * @author Oren
	 */
	private void setCellTable()
	{
		clmUsername.setCellValueFactory(new PropertyValueFactory<>("name"));
		clmPermission.setCellValueFactory(new PropertyValueFactory<>("stLogin"));
		clmBlocked.setCellValueFactory(new PropertyValueFactory<>("blocked"));
	}
	/**
	 * This method set the static list userLsit by calling the method in LoginController and client to get all the users in the system from server.
	 * @author Oren
	 * @param actualUsers - List of the registered users of the system, from all the roles.
	 */
	private void setUsers(ArrayList<User> actualUsers)
	{
		userList=FXCollections.observableArrayList();
		for(User singleUser : actualUsers){
			userList.add(new User(singleUser.getName(),singleUser.getPass(),singleUser.getLogin(),singleUser.getBlocked()));
		}
		
	}

}