
package survey;

import java.util.ArrayList;

import client.Client;
import client.Store;
import client.StoreEmployee;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * make connection between the survey gui to the client - to reach the database
 * @author koral zakai
 *
 */
public class SurveyController  {

	public static StoreEmployee sEmployeee;
	public static Client client;
	/**
	 * Getting  the client 
	 * @param c
	 */
	public static void setClient(Client c)
	{
		client =c;
	}
	/**
	 * change to new window
	 * @param root
	 */
	public static void changeWindow(Pane root)
	{
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);		 	
		primaryStage.setScene(scene);		
		primaryStage.show();	
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	  client.user_exit();
	          }});
		
	}
/**
 * adding new survey to data base
 * @param survey
 * @return
 */
	public static boolean addSurvey(Survey survey )
	{
		
	client.add_Survey(survey);
	return true;//
	}
/**
 * ask from, server for stores id list
 * @return
 */
	public static ArrayList<Store> get_stores() {
		return client.getStores();
	}
	/**
	 * Calculate the date and sent it to survey gui
	 * @return the local date
	 */

	public static String getDate() {
		java.util.Date dt = new java.util.Date();

		java.text.SimpleDateFormat sdf = 
		     new java.text.SimpleDateFormat("yyyy-MM-dd");

		String currentTime = sdf.format(dt);
		return currentTime;
	}

	
	
}
