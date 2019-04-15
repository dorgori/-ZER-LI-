package compensate_customer;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 
 * @author dorgori This class define complaint and contain all the necessary
 *         data about it
 */
@SuppressWarnings("serial")
public class Complaint implements Serializable {

	private String _customerServiceEmployeeID;
	private String _customerID;
	private String _complaintDesc;
	private String _storeID;
	private int _complaintID;
	private int _hour;
	private int _minutes;
	private LocalDate date;
	private String _time;
	private String _dateString;
	private boolean _status;
	private int _compensateAmount;

	public Complaint(String customerServiceEmployeeID, String customerID, String storeID, String description, int hour,
			int minutes) {
		_customerServiceEmployeeID = customerServiceEmployeeID;
		_customerID = customerID;
		_complaintDesc = description;
		_hour = hour;
		_storeID = storeID;
		_minutes = minutes;
		date = LocalDate.now();
		_dateString = date.toString();
		_time = date.toString();
		_status = false;
		_compensateAmount = 0;
	}

	public int get_compensateAmount() {
		return _compensateAmount;
	}

	public void set_time(String _time) {
		this._time = _time;
	}

	public void set_compensateAmount(int _compensateAmount) {
		this._compensateAmount = _compensateAmount;
	}

	/**
	 * Empty constructor to handle empty arraylist return from db
	 */
	public Complaint() {
		_complaintID = -1;
	}

	public String get_complaintDesc() {
		return _complaintDesc;
	}

	public void set_complaintDesc(String _complaintDesc) {
		this._complaintDesc = _complaintDesc;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setDate(String date) {
		this._dateString = date;
	}

	public String get_customerServiceEmployeeID() {
		return _customerServiceEmployeeID;
	}

	public void set_customerServiceEmployeeID(String _customerServiceEmployeeID) {
		this._customerServiceEmployeeID = _customerServiceEmployeeID;
	}

	public String get_customerID() {
		return _customerID;
	}

	public void set_customerID(String _customerID) {
		this._customerID = _customerID;
	}

	public String get_complaint() {
		return _complaintDesc;
	}

	public void set_complaint(String _complaint) {
		this._complaintDesc = _complaint;
	}

	public String get_storeID() {
		return _storeID;
	}

	public void set_storeID(String _storeID) {
		this._storeID = _storeID;
	}

	public boolean is_status() {
		return _status;
	}

	public void set_status(boolean _status) {
		this._status = _status;
	}

	public int get_complaintID() {
		return _complaintID;
	}

	public void set_complaintID(int _complaintID) {
		this._complaintID = _complaintID;
	}

	public String get_time() {
		String toRet = "";
		toRet += _hour;
		toRet += ":";
		toRet += _minutes;
		return toRet;
	}

}
