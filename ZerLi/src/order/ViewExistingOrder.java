package order;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import catalog.Item;
import catalog.Product;
import client.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import order.Order.orderStatus;

/**
 * this class presents the details of an order that marked by a customer
 * (customer can cancel an order)
 * 
 * @param o
 *            a static instance of Order
 * @param selectedListIndex
 *            saves the index of clicked Item in table
 * @param oItemsList
 *            a list of Items that is shown in a table
 * @param selfItemsList
 *            list of self made products that is in the customer's order
 * @param productsList
 *            list of items from catalog that is in the customer's order
 * @param flowers
 *            String details of the self made product in order pressed.
 * 
 */
public class ViewExistingOrder implements Initializable {
	public static Order o;
	public static int selectedListIndex;
	private ObservableList<Item> oItemsList = FXCollections.observableArrayList();
	private ObservableList<SelfMadeProduct> selfItemsList;
	private ObservableList<Product> productsList;
	private ObservableList<String> flowers;

	@FXML
	private TableView<Item> tblOrderedProd;

	@FXML
	private TableColumn<Item, String> clmType;

	@FXML
	private TableColumn<Item, String> clmID;

	@FXML
	private TableColumn<Item, String> clmItemName;

	@FXML
	private TableColumn<Item, Integer> clmQuantity;

	@FXML
	private TableColumn<Item, Float> clmPrice;

	@FXML
	private Button btnViewOrders;

	@FXML
	private Button btnCancelOrder;

	@FXML
	private Button btnCustomerMenu;

	@FXML
	private ListView<String> lstFlowers;

	@FXML
	private Text lblPrice;

	@FXML
	private Text refundMsg_txt;

	@FXML
	private Text valueRefundMsg_txt;

	/**
	 * checks if a product is instance of self made product
	 * 
	 * @param event
	 *            clicked on a product on the table
	 * @throws Exception
	 */
	@FXML
	private void checkIfSelfMadeSelected(MouseEvent event) throws Exception {
		if (tblOrderedProd.getSelectionModel().getSelectedItem() instanceof SelfMadeProduct)
			showFlowersList();
		else
			lstFlowers.setVisible(false);
	}

	/**
	 * this method set the details of the self made product
	 */
	private void showFlowersList() {
		ArrayList<String> arr = new ArrayList<String>();
		flowers = FXCollections.observableArrayList(new ArrayList<String>());
		lstFlowers.setItems(flowers);
		SelfMadeProduct self = (SelfMadeProduct) tblOrderedProd.getSelectionModel().getSelectedItem();
		for (Flower f : self.getFlowers()) {
			arr.add(f.toStringtoOrder());
		}
		flowers = FXCollections.observableArrayList(arr);
		lstFlowers.setItems(flowers);
		lstFlowers.refresh();
		lstFlowers.setVisible(true);
	}

	/**
	 * sets the table of products and checks if order status is "cancelled" or
	 * "picked up" to disable the cancel order button
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setCellTable();
		o = OrderController.customer.getCustomerOrders().get(selectedListIndex);
		o = OrderController.getOrder(o);
		oItemsList = FXCollections.observableArrayList(new ArrayList<Item>());
		setItems(o.getSelfMadeProducts());
		setProducts(o.getOrderedProducts());
		tblOrderedProd.setItems(oItemsList);
		lblPrice.setText(Float.toString(o.getPrice()));
		tblOrderedProd.refresh();
		if (o.getOrderStatusOption().equals(orderStatus.cancelled)) {
			valueRefundMsg_txt.setText("" + o.getRefund());
			valueRefundMsg_txt.setVisible(true);
			refundMsg_txt.setVisible(true);
		}
		if (!(o.getOrderStatusOption().equals(orderStatus.ordered)))
			btnCancelOrder.setDisable(true);
	}

	/**
	 * defines the columns of table
	 */
	private void setCellTable() {
		clmType.setCellValueFactory(new PropertyValueFactory<>("type"));
		clmID.setCellValueFactory(new PropertyValueFactory<>("id"));
		clmItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
		clmQuantity.setCellValueFactory(new PropertyValueFactory<>("quant"));
		clmPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
	}

	/**
	 * sets the self made products of the order inside the table
	 * 
	 * @param customerOrderItems
	 *            self made products ordered of this customer
	 */
	private void setItems(ArrayList<SelfMadeProduct> customerOrderItems) {
		SelfMadeProduct selfToAdd;
		if (!customerOrderItems.isEmpty()) {
			selfItemsList = FXCollections.observableArrayList();
			for (SelfMadeProduct singleItem : customerOrderItems) {
				if (singleItem instanceof SelfMadeProduct) {
					selfToAdd = new SelfMadeProduct(singleItem.getId(), singleItem.get_productType(),
							singleItem.getQuant(), singleItem.getPrice(), singleItem.getFlowers());
					selfToAdd.set_price(selfToAdd.getQuant() * selfToAdd.get_price());
					selfItemsList.add(selfToAdd);
				}
			}
			oItemsList.addAll(selfItemsList);
		}
	}

	/**
	 * sets the catalog item of the order inside the table
	 * 
	 * @param orderedProducts
	 *            catalog items ordered of this customer
	 */
	private void setProducts(ArrayList<Product> orderedProducts) {
		if (!orderedProducts.isEmpty()) {
			Product prodToAdd;
			productsList = FXCollections.observableArrayList();
			for (Product singleItem : orderedProducts) {
				if (singleItem instanceof Product) {
					prodToAdd = new Product(singleItem.getId(), singleItem.getName(), singleItem.get_product_Type(),
							singleItem.getPrice());
					prodToAdd.setQuant(singleItem.getQuant());
					prodToAdd.setPrice(singleItem.getQuant() * singleItem.getPrice());
					productsList.add(prodToAdd);
				}
			}
			oItemsList.addAll(productsList);
		}
	}

	/**
	 * this button runs the procedure of canceling an order and brings the customer
	 * back to his view order gui
	 * 
	 * @param event
	 *            clicked on button "Cancel Order"
	 * @throws IOException
	 * @throws ParseException
	 */
	@FXML
	private void cancelOrderPress(ActionEvent event) throws IOException, ParseException {
		OrderController.cancelOrder(o);
		o = OrderController.getOrder(o);
		valueRefundMsg_txt.setText("" + o.getRefund());
		btnCancelOrder.setDisable(true);
		valueRefundMsg_txt.setVisible(true);
		refundMsg_txt.setVisible(true);
	}

	/**
	 * brings customer back to "customer menu" gui
	 * 
	 * @param event
	 *            clicked on "Return to customer menu" button
	 * @throws Exception
	 */
	@FXML
	private void customerMenuListenner(ActionEvent event) throws Exception {
		OrderController.order=new Order();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		LoginController.setStageTitle("customer menu window");
		Pane root = loader.load(getClass().getResource("/client/CustomerMenu.fxml").openStream());
		OrderController.changeWindow(root);
	}

	/**
	 * brings customer back to his "view orders gui"
	 * 
	 * @param event
	 *            clicked on "Return to view orders" button
	 * @throws Exception
	 */
	@FXML
	private void viewOrdersListenner(ActionEvent event) throws Exception {
		OrderController.order=new Order();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		LoginController.setStageTitle("view orders window");
		Pane root = loader.load(getClass().getResource("/order/ViewOrdersGui.fxml").openStream());
		OrderController.changeWindow(root);
	}

}
