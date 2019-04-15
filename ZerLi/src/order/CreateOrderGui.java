package order;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import catalog.CatalogController;
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

/**
 * this class describes the create order gui window
 * 
 * @param selectedListIndex
 *            the index on the table of an item clicked by a user
 * @param oItemsList
 *            the observable list of items a user added to this order
 * @param selfItemsList
 *            the observable list of self made product the user added to this
 *            order
 * @param productsList
 *            the observable list of catalog products the user added to this
 *            order
 * @param flowers
 *            describes the flowers in a self made product item
 */
public class CreateOrderGui implements Initializable {

	private static int selectedListIndex;
	private ObservableList<Item> oItemsList = FXCollections.observableArrayList();
	private ObservableList<SelfMadeProduct> selfItemsList;
	private ObservableList<Product> productsList;
	private ObservableList<String> flowers;

	@FXML
	private Button btnToCatalog;
	@FXML
	private Button btnMyWay;
	@FXML
	private Button btnRemove;
	@FXML
	private Button btnAddAnother;
	@FXML
	private Button btnAdvance;
	@FXML
	private Button btnReturnToCustMenu;
	@FXML
	private Text lblTotalPrice;
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
	private TableView<Item> tblOrdersProd;
	@FXML
	private ListView<String> lstFlowers;

	/**
	 * get the current order items of that customer
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		OrderController.order.setCustomerID(LoginController.customer.getName());
		OrderController.order.setStoreID(LoginController.customer.getStoreID());
		OrderController.order.refreshItems();
		setItems(OrderController.order.getSelfMadeProducts());
		setProducts(OrderController.order.getOrderedProducts());
		setCellTable();
		tblOrdersProd.setItems(oItemsList);
		lblTotalPrice.setText(Float.toString(OrderController.order.getPrice()));
		tblOrdersProd.refresh();
	}

	/**
	 * set every column of the table
	 */
	private void setCellTable() {
		clmType.setCellValueFactory(new PropertyValueFactory<>("type"));
		clmID.setCellValueFactory(new PropertyValueFactory<>("id"));
		clmItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
		clmQuantity.setCellValueFactory(new PropertyValueFactory<>("quant"));
		clmPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
	}

	/**
	 * set the self made products of this customer order inside the table
	 * 
	 * @param customerOrderItems
	 *            array list of self made products currently on this customer's
	 *            order
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
	 * set the catalog products of this customer order inside the table
	 * 
	 * @param orderedProducts
	 *            array list of catalog products currently on this customer's order
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
	 * removes 1 quantity of an item chosen or remove completely if quantity of item
	 * is 1. also subtract the price of the item
	 * 
	 * @param event
	 *            clicked on "Remove" button
	 * @throws Exception
	 */
	@FXML
	private void removeListener(ActionEvent event) throws Exception {
		selectedListIndex = tblOrdersProd.getSelectionModel().getSelectedIndex();// save index of picked Item
		Item to_rem = null;
		if (selectedListIndex == -1)
			return; // user picked nothing
		to_rem = tblOrdersProd.getItems().get(selectedListIndex);
		if (to_rem.getType().equals("Catalog")) {
			for (Product pro : OrderController.order.getOrderedProducts()) {
				if (pro.get_product_ID().equals(to_rem.getId())) {
					pro.setQuant(pro.getQuant() - 1); // decrease quantity in order
					to_rem.setQuant(to_rem.getQuant() - 1); // decrease quantity in table
					to_rem = pro;
					if (to_rem.getQuant() == 0) { // needs to remove entirely
						OrderController.order.getOrderedProducts().remove(to_rem);
					}
					break;
				}
			} // end for
		} else { // Item is selfmade item
			for (SelfMadeProduct pro : OrderController.order.getSelfMadeProducts()) {
				if (pro.getName().equals(to_rem.getName())) {
					pro.setQuant(pro.getQuant() - 1);
					to_rem.setQuant(to_rem.getQuant() - 1);
					System.out.println(pro.get_productType() + "   " + pro.get_price() + " Erased");
					to_rem = pro;
					if (to_rem.getQuant() == 0) {// needs to remove entirely from order
						OrderController.order.getSelfMadeProducts().remove(to_rem);
					}
					break;
				}
			} // end for
				// do anyway whether is product from catalog or selfmade
		}
		if (to_rem.getQuant() == 0) {
			tblOrdersProd.getItems().remove(selectedListIndex);
		} else {
			tblOrdersProd.getItems().get(selectedListIndex)
					.setPrice(tblOrdersProd.getItems().get(selectedListIndex).getPrice() - to_rem.getPrice());
		}
		OrderController.order.setPrice(OrderController.order.getPrice() - to_rem.getPrice());
		lblTotalPrice.setText(Float.toString(OrderController.order.getPrice()));
		tblOrdersProd.refresh();
	}

	/**
	 * add 1 quantity to an item chosen
	 * 
	 * @param event
	 *            clicked on "Add another" button
	 * @throws Exception
	 */
	@FXML
	private void addAnotherListener(ActionEvent event) throws Exception {
		Item to_add = null;
		selectedListIndex = tblOrdersProd.getSelectionModel().getSelectedIndex(); // save index of picked produczzzzzzt
		if (selectedListIndex == -1)// make sure user select a row
			return;
		to_add = tblOrdersProd.getItems().get(selectedListIndex);
		if (to_add.getType().equals("Catalog")) {
			for (Product pro : OrderController.order.getOrderedProducts()) {
				if (pro.get_product_ID().equals(to_add.getId())) {
					pro.setQuant(pro.getQuant() + 1);
					to_add.setQuant(to_add.getQuant() + 1);
					to_add = pro;
					break;
				}
			}
		} else {// customer picked selfmade item
			for (SelfMadeProduct pro : OrderController.order.getSelfMadeProducts()) {
				if (pro.getName().equals(to_add.getName())) {
					pro.setQuant(pro.getQuant() + 1);
					to_add.setQuant(to_add.getQuant() + 1);
					to_add = pro;
					break;
				}
			} // end for
		} // end else
			// do whether it is item from catalog or selfmade
		OrderController.order.setPrice(OrderController.order.getPrice() + to_add.getPrice());
		tblOrdersProd.getItems().get(selectedListIndex)
				.setPrice(tblOrdersProd.getItems().get(selectedListIndex).getPrice() + to_add.getPrice());
		tblOrdersProd.refresh();
		lblTotalPrice.setText(Float.toString(OrderController.order.getPrice()));
	}

	/**
	 * go to catalog menu
	 * 
	 * @param event
	 *            clicked on "To catalog" button
	 * @throws Exception
	 */
	@FXML
	private void ToCatalogListener(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		LoginController.setStageTitle("catalog menu window");
		Pane root = loader.load(getClass().getResource("/catalog/CatalogGui.fxml").openStream());
		CatalogController.changeWindow(root);
	}

	/**
	 * go to "My way" menu
	 * 
	 * @param event
	 *            clicked on "My Way" button
	 * @throws Exception
	 */
	@FXML
	private void MyWayListenner(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		LoginController.setStageTitle("my way order menu");
		Pane root = loader.load(getClass().getResource("/order/SelfMadeGui.fxml").openStream());
		OrderController.changeWindow(root);
	}

	/**
	 * enter payment menu window
	 * 
	 * @param event
	 *            clicked on "Advance to payment" button
	 * @throws Exception
	 */
	@FXML
	private void AdvanceListener(ActionEvent event) throws Exception {
		if (!OrderController.order.getOrderedProducts().isEmpty()
				|| !OrderController.order.getSelfMadeProducts().isEmpty()) {
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			LoginController.setStageTitle("payment order menu");
			Pane root = loader.load(getClass().getResource("/order/PaymentOrderGui.fxml").openStream());
			OrderController.changeWindow(root);
		}

	}

	/**
	 * checks if the item clicked is self made product
	 * 
	 * @param event
	 *            clicked on an item inside the order table
	 * @throws Exception
	 */
	@FXML
	private void checkIfSelfMadeSelected(MouseEvent event) throws Exception {
		if (tblOrdersProd.getSelectionModel().getSelectedItem() instanceof SelfMadeProduct)
			showFlowersList();
		else
			lstFlowers.setVisible(false);
	}

	/**
	 * shows the flowers of the self made item
	 */
	private void showFlowersList() {
		ArrayList<String> arr = new ArrayList<String>();
		flowers = FXCollections.observableArrayList(new ArrayList<String>());
		SelfMadeProduct self = (SelfMadeProduct) tblOrdersProd.getSelectionModel().getSelectedItem();
		for (Flower f : self.getFlowers()) {
			arr.add(f.toStringtoOrder());
		}
		flowers = FXCollections.observableArrayList(arr);
		lstFlowers.setItems(flowers);
		lstFlowers.refresh();
		lstFlowers.setVisible(true);
	}

	/**
	 * go to customer menu window
	 * 
	 * @param event
	 *            clicked on "Return to Customer Menu"
	 * @throws Exception
	 */
	@FXML
	private void btnReturnToCustMenuListener(ActionEvent event) throws Exception {
		while (!OrderController.order.getOrderedProducts().isEmpty()) {
			OrderController.order.getOrderedProducts().remove(0);
		}
		while (!OrderController.order.getSelfMadeProducts().isEmpty()) {
			OrderController.order.getSelfMadeProducts().remove(0);
		}
		OrderController.order.setPrice(0);
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		LoginController.setStageTitle("customer menu window");
		Pane root = loader.load(getClass().getResource("/client/CustomerMenu.fxml").openStream());
		LoginController.changeWindow(root);
	}
}
