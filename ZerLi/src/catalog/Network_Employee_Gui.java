package catalog;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * 
 * a gui that holds all network emplyee's actions add delete and edit of
 * products in the catalog in this gui the employee can choose a product and
 * choose an action on it
 * 
 *
 */
public class Network_Employee_Gui implements Initializable {
	ObservableList<String> list;
	ArrayList<String> arr;
	private static int selectedListIndex = -1;
	public Product p;

	private ArrayList<Product> products = new ArrayList<Product>();
	public Image productImage;
	@FXML
	private TextArea txtProductsDesc;
	@FXML
	private Button add_btn;
	@FXML
	private Text txtProductPrice;
	@FXML
	private Button delete_product_from_list;

	@FXML
	private Button edit_product_from_list;

	@FXML
	private ListView<String> lstProducts = new ListView<String>();

	@FXML
	private ImageView product_image;

	@FXML
	private Label info_lbl;

	@FXML
	void pressed_on_list(MouseEvent event) {

	}

	/**
	 * click on add handler will change window
	 * 
	 * @param event
	 *            add clocked
	 * @throws IOException
	 */
	@FXML
	void add_product(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/catalog/add_product_Gui.fxml").openStream());
		LoginController.setStageTitle("add_product_Gui");
		CatalogController.changeWindow(root);
		lstProducts.refresh();
	}

	/**
	 * click on delete handler
	 * 
	 * @param event
	 */
	@FXML
	void delete_product_from_list(ActionEvent event) {
		selectedListIndex = lstProducts.getSelectionModel().getSelectedIndex();// save index of picked product

		if (selectedListIndex < 0)
			System.out.println("no product chosen");
		else {
			p = CatalogController.getProducts().get(selectedListIndex);
			CatalogController.removeProduct(p);
			lstProducts.getItems().remove(selectedListIndex);
			lstProducts.refresh();

			product_image.setImage(null);
		}
	}

	/**
	 * edit product handler will change window to edit window
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void edit_product_from_list(ActionEvent event) throws IOException {
		selectedListIndex = lstProducts.getSelectionModel().getSelectedIndex();// save index of picked product

		if (selectedListIndex < 0)
			System.out.println("no product chosen");
		else {
			this.p = products.get(selectedListIndex);
			System.out.println(p.get_product_ID());
			edit_product_in_catalog_gui.get_product_to_edit(this.p);

			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/catalog/edit_product_gui.fxml").openStream());
			LoginController.setStageTitle("edit_product_gui");
			CatalogController.changeWindow(root);
		}
	}

	/**
	 * initialize the catalog product list
	 * 
	 * @param _products
	 */
	void set_Show_products(ArrayList<Product> _products) {

		arr = new ArrayList<String>();
		this.products = _products;
		for (Product prod : _products) {
			arr.add(prod.get_product_ID() + " " + prod.get_product_Name());
		}
		list = FXCollections.observableArrayList(arr);
		lstProducts.getItems().addAll(list);
	}

	/**
	 * click on list handler
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void chooseProductFromList1(MouseEvent event) throws Exception {
		selectedListIndex = lstProducts.getSelectionModel().getSelectedIndex(); // save index of picked product
		product_image.setImage(null);
		p = setProductDetails();

	}

	/**
	 * log out button listener
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void logout_btn(ActionEvent event) throws IOException {
		LoginController.userLogOut();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/client/loginGui.fxml").openStream());
		LoginController.setStageTitle("loginGui");
		CatalogController.changeWindow(root);
	}

	/**
	 * initialize the gui
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		set_Show_products(CatalogController.getProducts());
		lstProducts.refresh();
	}

	/**
	 * when an item on the list is clicked
	 * this method will set the details of the product to be shown on the gui
	 * @return
	 * @throws IOException
	 */
	Product setProductDetails() throws IOException {
		if (selectedListIndex >= 0) {
			this.p = products.get(selectedListIndex);// find the right product yay

			txtProductsDesc.setText(p.getProductDesc());
			txtProductPrice.setText(Float.toString(p.getPrice()));

			if (p.getProductImage() != null)// there is picture
			{
				productImage = new Image(new ByteArrayInputStream(p.getProductImage()));
				product_image.setImage(productImage);
			}

			return p;
		}
		return null;
	}
}