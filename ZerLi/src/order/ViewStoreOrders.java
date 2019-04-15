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
import order.Order.orderStatus;

/**
 * describes the view store order gui
 * 
 * @param OrdersList
 *            an observable list of orders of the store that is shown in the
 *            table
 *
 */
public class ViewStoreOrders implements Initializable {

	private ObservableList<Order> OrdersList;

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

	@FXML
	private Button btnCompleted;

	/**
	 * initialize this gui
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		LoginController.sEmployee.setStoreOrders(OrderController.getStoreOrders(LoginController.sEmployee.getS_ID()));
		if (!(LoginController.sEmployee.getStoreOrders().get(0).getOrderID() == -1)) {
			setOrders(LoginController.sEmployee.getStoreOrders());
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
	 * set the store orders inside the table
	 * 
	 * @param storeOrders
	 *            array list of store orders
	 */
	private void setOrders(ArrayList<Order> storeOrders) {
		OrdersList = FXCollections.observableArrayList();
		for (Order singleOrder : storeOrders) {
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
	 * defines the table columns
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
	 * select order and mark it as completed="picked_up"
	 * 
	 * @param event
	 *            clicked on "Mark as completed" button
	 * @throws Exception
	 */
	@FXML
	private void sendOrder(ActionEvent event) throws Exception {
		Order o = tblOrdersTable.getSelectionModel().getSelectedItem();
		o.setOrderStatusOption(orderStatus.picked_up);
		OrderController.orderHandlingCompleted(o.getOrderID(), o.getStoreID());
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/order/ViewStoreOrders.fxml").openStream());
		OrderController.changeWindow(root);
	}

	/**
	 * changes window to store employee menu
	 * 
	 * @param event
	 *            clicked on "Return to store employee Menu" button
	 * @throws Exception
	 */
	@FXML
	private void returnStoreEmpMenuListenner(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/client/StoreEmployeeMenu.fxml").openStream());
		OrderController.changeWindow(root);
	}

}
