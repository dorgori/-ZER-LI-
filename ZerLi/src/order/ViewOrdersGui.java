package order;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import order.Order.deliveryType;

/**
 * describes the view orders menu gui
 * 
 * @param OrdersList
 *            an observable list of Orders of the customer to show in the table
 * @param selectedIndex
 *            the index of the selected order
 * @param selectedOrder
 *            the order selected
 */
public class ViewOrdersGui implements Initializable {

	private ObservableList<Order> OrdersList;
	private int selectedIndex;
	private Order selectedOrder;

	@FXML
	private TableView<Order> tblOrdersTable;

	@FXML
	private TableColumn<Order, Integer> clmOrderID;

	@FXML
	private TableColumn<Order, String> clmOrderDate;

	@FXML
	private TableColumn<Order, String> clmDelivery;

	@FXML
	private TableColumn<Order, String> clmDeliveryDate;

	@FXML
	private TableColumn<Order, Float> clmPrice;

	@FXML
	private TableColumn<Order, String> clmOrderStatus;

	@FXML
	private Button btnReturn;

	@FXML
	private Button btnOrderDetails;

	/**
	 * initialize the view existing order gui window
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		OrderController.getCustomerOrders(LoginController.customer.getName(), LoginController.customer.getStoreID());
		if (!(OrderController.customer.getCustomerOrders().get(0).getOrderID() == -1)) {
			setOrders(OrderController.customer.getCustomerOrders());
			setCellTable();
			tblOrdersTable.setItems(OrdersList);
		} else {
			setCellTable();
			OrdersList = FXCollections.observableArrayList(new ArrayList<Order>());
			tblOrdersTable.setItems(OrdersList);
		}
		tblOrdersTable.refresh();
	}

	/**
	 * This method adds the customer's orders into observable list that will shows
	 * into table.
	 * 
	 * @param customerOrders
	 *            ArrayList of customer's orders from DB.
	 * @author Oren
	 */
	private void setOrders(ArrayList<Order> customerOrders) {
		OrdersList = FXCollections.observableArrayList();
		for (Order singleOrder : customerOrders) {
			if (singleOrder.getDeliveryOption() == deliveryType.pickup) {
				OrdersList.add(new Order(singleOrder.getOrderID(), singleOrder.getOrderDate(),
						singleOrder.getDeliveryOption(), singleOrder.getPrice(), singleOrder.getOrderStatusOption()));
				OrdersList.get(OrdersList.size() - 1).setStringOfOrderStat(singleOrder.getOrderStatusOption());
				OrdersList.get(OrdersList.size() - 1).setStringOfDeliveryOp(singleOrder.getDeliveryOption());
			} else {
				OrdersList.add(new Order(singleOrder.getOrderID(), singleOrder.getOrderDate(),
						singleOrder.getDeliveryOption(), singleOrder.getRequirementDate(), singleOrder.getPrice(),
						singleOrder.getOrderStatusOption()));
				OrdersList.get(OrdersList.size() - 1).setStringOfOrderStat(singleOrder.getOrderStatusOption());
				OrdersList.get(OrdersList.size() - 1).setStringOfDeliveryOp(singleOrder.getDeliveryOption());
			}
		}
	}

	/**
	 * define the table columns
	 */
	private void setCellTable() {
		clmOrderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
		clmOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
		clmDelivery.setCellValueFactory(new PropertyValueFactory<>("stringOfDeliveryOp"));
		clmDeliveryDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
		clmPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		clmOrderStatus.setCellValueFactory(new PropertyValueFactory<>("stringOfOrderStat"));
	}

	/**
	 * shows the details of the order selected. changes to "view existing order"
	 * scene
	 * 
	 * @param event
	 *            clicked on "show order details" button
	 * @throws Exception
	 */
	@FXML
	private void showOrderListenner(ActionEvent event) throws Exception {
		selectedIndex = tblOrdersTable.getSelectionModel().getSelectedIndex();
		for (Order singleOrder : OrdersList) {
			if (singleOrder.getOrderID() == (tblOrdersTable.getSelectionModel().getSelectedItem().getOrderID())) {
				selectedOrder = tblOrdersTable.getSelectionModel().getSelectedItem();
				break;
			}
		}
		if (!selectedOrder.getStringOfOrderStat().equals(null)) {
			ViewExistingOrder.selectedListIndex = selectedIndex;
			ViewExistingOrder.o = selectedOrder;
			ViewExistingOrder.o.setOrderedProducts(selectedOrder.getOrderedProducts());
			ViewExistingOrder.o.setSelfMadeProducts(selectedOrder.getSelfMadeProducts());
			ViewExistingOrder.o.setPrice(selectedOrder.getPrice());
			System.out.println("choosing Succeseed! " + selectedOrder.getOrderID());
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			LoginController.setStageTitle("existing order window");
			Pane root = loader.load(getClass().getResource("/order/ViewExistingOrder.fxml").openStream());
			OrderController.changeWindow(root);
		}
	}

	/**
	 * changes window to customer menu gui
	 * 
	 * @param event
	 *            clicked on "Return to customer menu" button
	 * @throws Exception
	 */
	@FXML
	private void returnListenner(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		LoginController.setStageTitle("customer menu window");
		Pane root = loader.load(getClass().getResource("/client/CustomerMenu.fxml").openStream());
		OrderController.changeWindow(root);
	}

}
