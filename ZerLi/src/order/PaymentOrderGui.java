package order;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Customer;
import client.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import order.Order.deliveryType;

/**
 * This class describe the payment gui window
 * @param deliveryList - list of delivery options. (String)
 * @param paymentList - list of payment options. (String)
 * @param selectedIndex - index of selected choise in delivery combo box.
 * @param selectedHours- the selected hour in the hours combo box.
 * @param selectedMinutes- the selected minute in the minutes combo box.
 * @param selectedPayment - index of selected payment combo box.
 * @param chooseFlag - boolean of the delivery choice: self pickup or delivery, needs to show and disappear relevant details.    
 * @param delivertTypeOption - enum choice of delivery option: need to create new order.
 */
public class PaymentOrderGui implements Initializable {

	private ObservableList<String> deliveryList;
	private ObservableList<String> paymentList;
	private int selectedIndex = -1;
	private int selectedHours = -1, selectedMinutes = -1, selectedPayment = -1;
	private static int chooseFlag = 0;
	private deliveryType deliveryTypeOption;

	@FXML
	private TextArea txtGreeting;
	@FXML
	private DatePicker dtDate;
	@FXML
	private Text lblDate;
	@FXML
	private Text lblTime;
	@FXML
	private Text lblAddress;
	@FXML
	private TextField txtRecipient;
	@FXML
	private Text lblRecipient;
	@FXML
	private TextField txtAddress;
	@FXML
	private Text lblDeliveryPrice;
	@FXML
	private Text lblDeliveryPriceAmount;
	@FXML
	private Text lblTotalPriceAmount;
	@FXML
	private ComboBox<String> cmbShipment;
	@FXML
	private ComboBox<String> cmbPaymentType;
	@FXML
	private Text lblDDots;
	@FXML
	private ComboBox<String> cmbHours;
	@FXML
	private ComboBox<String> cmbMinutes;
	@FXML
	private Button btnReturn;
	@FXML
	private Button btnPurchase;
	@FXML
	private Text lblException0;
	@FXML
	private Text lblException1;
	@FXML
	private Text lblException2;
	@FXML
	private Text lblException3;
	@FXML
	private Text lblErrorTime;
	@FXML
	private Text lblTel;
	@FXML
	private TextField txtTel;
	@FXML
	private Text lblExceptionTel;
	@FXML
	private Text lblErrorDate;
	@FXML
	private Text lblExcepPay;
	/**
	 * This method is a date listener, update the requirement date of the order which user input.  
	 * @author Oren
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void chooseDeliveryDate(ActionEvent event) throws Exception {
		LocalDate localdate = dtDate.getValue();
		OrderController.order.setRequirementDate(localdate.toString());
		;
	}

	/**
	 * Invoke when pressing on Return to order change between windows and opens
	 * CreateOrderGui window
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void returnListenner(ActionEvent event) throws Exception {
		if(selectedIndex==1)
			OrderController.order.setPrice(OrderController.order.getPrice() -20);
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		LoginController.setStageTitle("create order menu window");
		Pane root = loader.load(getClass().getResource("/order/CreateOrderGui.fxml").openStream());
		OrderController.changeWindow(root);
	}
	/**
	 * This method checks the occurrences that customer have monthly member, this method checks if the monthly member is still valid. 
	 * @author Oren
	 * @param dbDate - the date of creating the monthly member
	 * @return true if valid, either false.
	 */
	boolean checkMonthlyMemTimeValid(String dbDate) {
		int year, month, day;
		String[] str = dbDate.split("-");
		year = Integer.parseInt(str[0]);
		month = Integer.parseInt(str[1]);
		day = Integer.parseInt(str[2]);
		if (LocalDate.now().getYear() == year) {
			if (LocalDate.now().getMonthValue() > month)
				return false;
			else if (LocalDate.now().getMonthValue() == month) {
				if (LocalDate.now().getDayOfMonth() <= day) {
					return true;
				} else {
					return false;
				}
			} else if (LocalDate.now().getMonthValue() < month) {
				return true;
			}
		} else if (LocalDate.now().getYear() > year) {
			if ((LocalDate.now().getMonthValue() - month) == -11) {
				if (LocalDate.now().getDayOfMonth() <= day)
					return true;
			} else {
				return false;
			}
		}
		return true;
	}
	/**
	 * This method checks the occurrences that customer have yearly member, this method checks if the yearly member is still valid. 
	 * @author Oren
	 * @param dbDate - the date of creating the yearly member
	 * @return true if valid, either false.
	 */
	private boolean checkYearlyMemTimeValid(String dbDate) {
		int year, month, day;
		String[] str = dbDate.split("-");
		year = Integer.parseInt(str[0]);
		month = Integer.parseInt(str[1]);
		day = Integer.parseInt(str[2]);
		if (LocalDate.now().getYear() == year) {
			if (LocalDate.now().getMonthValue() > month)
				return false;
			else if (LocalDate.now().getMonthValue() == month) {
				if (LocalDate.now().getDayOfMonth() <= day) {
					return true;
				} else {
					return false;
				}
			} else if (LocalDate.now().getMonthValue() < month) {
				return true;
			}
		} else if (LocalDate.now().getYear() > year) {
			if (LocalDate.now().getMonthValue() < month) {
				return true;
			} else if (LocalDate.now().getMonthValue() == month) {
				if (LocalDate.now().getDayOfMonth() <= day)
					return true;
				else {
					return false;
				}
			} else
				return false;
		}
		return true;
	}

	/**
	 * This method split string of time to 2 integers of hour and minutes.
	 * 
	 * @author Oren
	 * @param time
	 * @return
	 */
	int[] getRequirementDate(String time) {
		int[] arr = new int[2];
		String[] str = time.split(":");
		arr[0] = Integer.parseInt(str[0]);
		arr[1] = Integer.parseInt(str[1]);
		return arr;
	}

	/**
	 * Invoke when pressing on Purchase Sets all the fields the user entered in the
	 * order instance(time,date,greeting card)e.g Finally saving the order in the
	 * data base and switch window to Customer menu
	 * @author Oren
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void PurchaseListenner(ActionEvent event) throws Exception {
		boolean flag = false;
		int localHour = 0, localMinute = 0;
		int[] chooseTime = new int[2];
		OrderController.order.setStoreID(LoginController.customer.getStoreID());
		OrderController.order.setCustomerID(LoginController.customer.getName());

		LocalDate localDate = LocalDate.now();
		OrderController.order.setOrderDate(localDate.toString());
		LocalTime localTime = LocalTime.now();
		localHour = localTime.getHour();
		localMinute = localTime.getMinute();

		if (selectedIndex == -1) {
			lblException0.setVisible(true);
			flag = true;
			return;
		} else
			lblException0.setVisible(false);
		
		// need to get a new order id
		// we got customer and store id when we creating new order

		OrderController.order.setGreetingCard(txtGreeting.getText());
		OrderController.order.setDeliveryOption(deliveryTypeOption);
		selectedHours = cmbHours.getSelectionModel().getSelectedIndex();
		selectedMinutes = cmbMinutes.getSelectionModel().getSelectedIndex();

		if (OrderController.order.getRequirementDate().equals("")) {
			lblException3.setVisible(true);
			flag = true;
			return;
		} else {
			OrderController.order.setRequirementDate(dtDate.getValue().toString());
			lblException3.setVisible(false);
		}
		if (selectedHours == -1 || selectedMinutes == -1) {
			lblErrorDate.setVisible(true);
			flag = true;
			return;
		} else {
			OrderController.order.setRequirementTime(cmbHours.getSelectionModel().getSelectedItem() + ":"
					+ cmbMinutes.getSelectionModel().getSelectedItem());
			lblErrorDate.setVisible(false);
		}

		chooseTime = getRequirementDate(OrderController.order.getRequirementTime());
		if (LocalDate.now().getYear() > dtDate.getValue().getYear()
				|| LocalDate.now().getYear() < dtDate.getValue().getYear()) {
			lblErrorDate.setVisible(true);
			flag = true;
			return;
		} else if (LocalDate.now().getMonthValue() > dtDate.getValue().getMonthValue()) {
			lblErrorDate.setVisible(true);
			flag = true;
			return;

		} else if (LocalDate.now().getMonthValue() == dtDate.getValue().getMonthValue()
				&& LocalDate.now().getDayOfMonth() > dtDate.getValue().getDayOfMonth()) {
			lblErrorDate.setVisible(true);
			flag = true;
			return;
		} else if (LocalDate.now().getMonthValue() == dtDate.getValue().getMonthValue()
					&& LocalDate.now().getDayOfMonth() == dtDate.getValue().getDayOfMonth()) {
			lblErrorDate.setVisible(false);
			if(chooseTime[0]-localHour <=2) {
				lblErrorTime.setVisible(true);
				flag=true;
				return;
			}
			else {
				if (chooseTime[1] - localMinute >= 0)
					lblErrorTime.setVisible(false);
				else {
					lblErrorTime.setVisible(true);
					flag = true;
					return;
				}
			}
			
		}/* else if (LocalDate.now().equals(dtDate.getValue()) && (chooseTime[0] - localHour) < 2) {
			lblErrorTime.setVisible(true);
			flag=true;
			return;
		}*/
		if (selectedPayment == -1) {
			lblExcepPay.setVisible(true);
			flag = true;
			return;
		} else {
			OrderController.order.setPaymentType(cmbPaymentType.getSelectionModel().getSelectedIndex());
			lblExcepPay.setVisible(false);
		}
		if (OrderController.order.getDeliveryOption() == Order.deliveryType.delivery) {
			if (txtRecipient.getText().equals("")) {
				lblException1.setVisible(true);
				flag = true;// now the window wont change
				return;
			} else {
				OrderController.order.setDeliveryRecipient(txtRecipient.getText());
				lblException1.setVisible(false);
			}
			if (txtAddress.getText().equals("")) {
				lblException2.setVisible(true);
				flag = true;
				return;
			} else {
				OrderController.order.setDeliveryAddress(txtAddress.getText());
				lblException2.setVisible(false);
			}
			if (txtTel.getText().equals("")) {
				lblExceptionTel.setVisible(true);
				flag = true;
				return;
			} else {
				OrderController.order.setTel(txtTel.getText());
				lblExceptionTel.setVisible(false);
			}

		}
		if (!flag) {
			OrderController.order.setOrderStatusOption(Order.orderStatus.ordered);
			OrderController.order.setRefund(0);
			OrderController.order.setPaymentType(cmbPaymentType.getSelectionModel().getSelectedIndex());
			if (OrderController.order.getPaymentType() == 2) {
				OrderController.customer
						.setRefundAmount(OrderController.customer.getRefundAmount() - OrderController.order.getPrice());
				OrderController.updateCustomerRefund(OrderController.customer.getStoreID(),
						OrderController.customer.getName(), OrderController.customer.getRefundAmount());
			}
			System.out.println("CUSTOMER ID " + OrderController.order.getCustomerID());
			OrderController.sendCompletedOrder();

			OrderController.order = new Order();///
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			LoginController.setStageTitle("customer menu window");
			Pane root = loader.load(getClass().getResource("/client/CustomerMenu.fxml").openStream());
			LoginController.changeWindow(root);
		}
	}

	/**
	 * Invoke when customer pick from the combo box the type of shipment In case of
	 * delivery sets fields to visible and adding shipment price to the order
	 * @author Oren
	 * 
	 * @param e
	 * @throws Exception
	 */
	@FXML
	void chooseDelivery(ActionEvent e) throws Exception {
		selectedIndex = cmbShipment.getSelectionModel().getSelectedIndex();
		if (selectedIndex == 1) {
			chooseFlag = 1;
			OrderController.order.setPrice(OrderController.order.getPrice() + 20);
			lblTotalPriceAmount.setText(Float.toString(OrderController.order.getPrice()));
			deliveryTypeOption = Order.deliveryType.delivery;
			setVisibleFunc(true);
		}
		if (selectedIndex == 0) {
			if (chooseFlag == 1) {
				OrderController.order.setPrice(OrderController.order.getPrice() - 20);
				chooseFlag = 0;
				lblTotalPriceAmount.setText(Float.toString(OrderController.order.getPrice()));
			}
			deliveryTypeOption = Order.deliveryType.pickup;
			setVisibleFunc(false);
		}
	}

	/**
	 * set on/off all the hidden features on the screen by user delivery choosing
	 * @author Oren
	 * @param boolean
	 *            state - false/true depends on user choice
	 */
	private void setVisibleFunc(boolean state) {
		lblDeliveryPrice.setVisible(state);
		lblDeliveryPriceAmount.setVisible(state);
		lblRecipient.setVisible(state);
		txtRecipient.setVisible(state);
		lblAddress.setVisible(state);
		txtAddress.setVisible(state);
		lblTel.setVisible(state);
		txtTel.setVisible(state);
	}

	/**
	 * Invoke by initialize Initialize the combo box of shipment options
	 */
	private void cmbSetDeliveryType() {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("Self-Pickup");
		arr.add("Delivery");
		deliveryList = FXCollections.observableArrayList(arr);
		cmbShipment.setItems(deliveryList);
	}

	/**
	 * Invoke by initialize Initialize the combo box of Payment type options depends
	 * on customer account
	 */
	private void cmbSetPaymentType() {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("Cash");
		arr.add("Credit");
		arr.add("Refund");
		paymentList = FXCollections.observableArrayList(arr);
		cmbPaymentType.setItems(paymentList);
	}

	/**
	 * Invoke every time the window is open calls init functions that get properties
	 * ready
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lblTotalPriceAmount.setText(Float.toString(OrderController.order.getPrice()));
		if (OrderController.customer.getMemberOption() == Customer.memberType.monthlyMember
				&& checkMonthlyMemTimeValid(OrderController.customer.getCreatedDate())) {
			lblTotalPriceAmount.setText(Float
					.toString((float) (OrderController.order.getPrice() - OrderController.order.getPrice() * 0.1)));
		}
		if (OrderController.customer.getMemberOption() == Customer.memberType.yearlyMember
				&& checkYearlyMemTimeValid(OrderController.customer.getCreatedDate())) {
			lblTotalPriceAmount.setText(Float
					.toString((float) (OrderController.order.getPrice() - OrderController.order.getPrice() * 0.25)));
		}
		cmbSetDeliveryType();
		cmbSetPaymentType();
		cmbSetHours();
		cmbSetMinutes();
	}
	/**
	 * This method set the minutes in the minutes Combo box.
	 * @author Oren
	 */
	private void cmbSetMinutes() {
		ArrayList<String> arr = new ArrayList<String>();
		int i;
		for (i = 0; i < 60; i += 15)
			arr.add("" + i);
		ObservableList<String> listMinutes = FXCollections.observableArrayList(arr);
		cmbMinutes.setItems(listMinutes);
	}
	/**
	 * This method set the hours in the hours Combo box.
	 * @author Oren
	 */
	private void cmbSetHours() {
		ArrayList<String> arr = new ArrayList<String>();
		int i;
		for (i = 8; i < 19; i++) {
			if (i < 10)
				arr.add("0" + i);
			else
				arr.add("" + i);
		}
		ObservableList<String> listHours = FXCollections.observableArrayList(arr);
		cmbHours.setItems(listHours);
	}

	/**
	 * This method is payment listener, update the selected index which user choice.
	 * @author Oren
	 * @param event
	 * @throws Exception
	 */
	@FXML
	private void choosingPay(ActionEvent event) throws Exception {
		selectedPayment = cmbPaymentType.getSelectionModel().getSelectedIndex();
	}
}
