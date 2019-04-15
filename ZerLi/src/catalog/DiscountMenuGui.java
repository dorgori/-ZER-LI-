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
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * gui for choosing which product to add/edit/remove a discount to
 * 
 * @author sagi arieli
 *
 */

public class DiscountMenuGui implements Initializable {
	ObservableList<String> list;
	ArrayList<String> arr;
	ArrayList<Product> products;
	private static int selectedListIndex;
	private static Product p;
	public Image productImage;
	@FXML
	private ListView<String> discount_list = new ListView<String>();

	@FXML
	private Button add_btn;

	@FXML
	private Button close_btn;

	@FXML
	private Button delete_btn;

	@FXML
	private ImageView image_view;

	@FXML
	private Text dis_prompt_txt;

	@FXML
	private Text dis_txt;

	@FXML
	private Text price_txt;

	@FXML
	private Text on_dis_p_txt;

	@FXML
	private Text price_dis_txt;

	@FXML
	/**
	 * when add clicked we want to move to the add/edit gui (depends if product has
	 * discount)
	 * 
	 * @param event
	 */
	void add_clicked(ActionEvent event) throws IOException {

		if (p != null) {
			CatalogController.setDiscountToADD(p);
		}
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/catalog/DiscountAddOrEdit.fxml").openStream());
		LoginController.setStageTitle("DiscountAddOrEdit");
		CatalogController.changeWindow(root);

	}

	/**
	 * close button handler return to the store employee main menu
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void close_clicked(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/client/StoreEmployeeMenu.fxml").openStream());
		LoginController.setStageTitle("StoreEmployeeMenu");
		LoginController.changeWindow(root);
	}

	/**
	 * delete button handler 
	 * will cause a refresh to the screen
	 * in order to refresh the list
	 * the delete action is still synchronized with the server - it will not do the refresh before deleting from thee database first
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void delete_clicked(ActionEvent event) throws IOException {
		CatalogController.removeDiscount(LoginController.sEmployee.getS_ID(), p.get_product_ID());

		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/catalog/DiscountMenu.fxml").openStream());
		LoginController.setStageTitle("DiscountMenu");
		CatalogController.changeWindow(root);
	}
	/**
	 * item in list pressed handler
	 * mostly use to disable/enable the needed buttons
	 * @param event
	 */
	@FXML
	void list_Mouse_Clicked(MouseEvent event) {
		selectedListIndex = discount_list.getSelectionModel().getSelectedIndex(); // save index of picked product
		try {
			p = setProductDetails();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * when an item is pressed
	 * this method will change to gui to suit the item's details
	 * such as correct buttons (if on discount then edit-on delete-on add-off) 
	 * @return
	 * @throws IOException
	 */
	Product setProductDetails() throws IOException {
		this.p = products.get(selectedListIndex);// find the right product

		if (p.isOnDiscount()) {
			dis_txt.setText("%" + p.getDiscount());
			dis_prompt_txt.setVisible(true);
			dis_txt.setVisible(true);
			add_btn.setText("EDIT");
			price_txt.setText(Float.toString((float)(int)(p.getPrice() / (1 - (p.getDiscount() / 100)))));
			on_dis_p_txt.setVisible(true);
			price_dis_txt.setText("" + p.getPrice());
			price_dis_txt.setVisible(true);
			delete_btn.setVisible(true);

		} else {
			dis_prompt_txt.setVisible(false);
			dis_txt.setVisible(false);
			add_btn.setText("ADD");
			price_txt.setText(Float.toString(p.getPrice()));
			price_dis_txt.setVisible(false);
			on_dis_p_txt.setVisible(false);
			delete_btn.setVisible(false);
		}
		price_txt.setVisible(true);
		if (p.getProductImage() != null)// there is picture
		{
			productImage = new Image(new ByteArrayInputStream(p.getProductImage()));
		} else {
			productImage = null;
		}
		image_view.setImage(productImage);
		// p.setProductImage(null);
		return p;
	}

	@Override
	/**
	 * set the list of products for the store (with up to date discounts)
	 */
	public void initialize(URL location, ResourceBundle resources) {

		products = CatalogController.getStoreProducts(LoginController.sEmployee.getS_ID());
		arr = new ArrayList<String>();
		for (Product prod : products) {
			arr.add(prod.get_product_ID() + " " + prod.get_product_Name());
		}
		list = FXCollections.observableArrayList(arr);
		discount_list.getItems().addAll(list);
		discount_list.refresh();

	}

}
