package reports;

import java.util.ArrayList;

import client.Client;
import client.Store;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ReportController {
	
	 static Client client;
	 static String type;
	 static String quarter;
	 static int year;
	public static String storeID;
	 static String quarter2;
	 static int year2;
	 static String storeID2;
	 static  ArrayList<Store> stores =new  ArrayList<Store>();
	 static ArrayList<String> complaintReport ;
	 static ArrayList<String> complaintReportComp;
	static boolean multiIncomeReport=false;
	static ArrayList<IncomeRep> incomes;
	
	
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

	public static void setClient(Client c)
	{
		client =c;
	}
	public static String getStoreID()
	{
		return storeID;
	}
	
	public static float getStoreIncome(String storeID, String quarter, int year) {
		float ret=client.getStoreIncome(storeID,quarter,year);
		
		return ret;
	}

	public static ArrayList<Store> getStores() {
		stores = client.getStores();
		System.out.println(stores);
		return stores;
	}
	/**
	 * this is a getter method that recive the requested report vaules 
	 * @author sagi arieli
	 * @param storeID3 
	 * @param quarter3
	 * @param year3
	 * @return arraylist of types and deatils for each type (more info in ItemInOrderReport class)
	 */
	public static ArrayList<ItemInOrderReport> getOrderReport(String storeID3, String quarter3, int year3) {
		return client.getOrderReport(storeID3,quarter3,year3);
		
	}

	public static void getComplaints(String storeIDComp, int year, String quarter) {
		complaintReport = client.getComplaintsToReport(storeIDComp,year,quarter);
	}
	public static void getComplaintsComp(String storeIDComp, int year, String quarter) {
		complaintReportComp = client.getComplaintsToReport(storeIDComp,year,quarter);
	}

	public static ArrayList<Float> getStoreSatisfaction(String storeID3, String quarter3, int year3) {
		return client.getSatisfactionToReport(storeID3,year3,quarter3);
	}
	
	public static String getStoreName(String storeID) {
		for(Store s:stores) {
			if(s.getS_ID().equals(storeID))
				return s.getS_name();
		}
		return "";
	}

}
