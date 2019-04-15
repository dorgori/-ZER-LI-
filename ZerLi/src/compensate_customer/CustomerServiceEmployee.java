package compensate_customer;

import java.io.Serializable;
import java.util.ArrayList;

import client.User;

/**
 * This class define the customer service employee user Every cse has its own
 * arraylist of complaints that he opened and when he is connecting to the
 * system this arraylist initialize with open complaint from the data base
 * 
 * @author dorgori
 */
public class CustomerServiceEmployee extends User implements Serializable {

	private ArrayList<Complaint> myOpenComplaints;

	public CustomerServiceEmployee(String name, String pass) {
		super(name, pass);
		myOpenComplaints = new ArrayList<Complaint>();
	}

	public ArrayList<Complaint> getArrayListComplaints() {
		return myOpenComplaints;
	}

	public void setMyOpenComplaints(ArrayList<Complaint> myOpenComplaints) {
		this.myOpenComplaints = myOpenComplaints;
	}

}
