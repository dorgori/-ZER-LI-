package catalog;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * 
 * a gui class for adding a new item to the catalog
 *
 */
//
public class add_product_menuGui {
	private String _product_id;
	private String _product_name;
	private String _product_type;
	private String _product_des;
	private String _product_price = "";

	private float f = (float) 0.0;

	public ArrayList<Product> products;
	public Product p;
	@FXML
	private TextField product_name;

	@FXML
	private TextField product_id;

	@FXML
	private TextField product_type;

	@FXML
	private TextField product_price;

	@FXML
	private Button submit;

	@FXML
	private TextField product_des;
	@FXML
	private Button close_btn;

	@FXML
	private TextField image_path;
	@FXML
	private Text price_error_txt;

	@FXML
	private Text ID_exist_error_txt;

	@FXML
	private Text txtPathError;

	// submit pressed
	/**
	 * will be called when the user press add it will go over all the fields and
	 * validate them before adding the item and closing the current gui if one of
	 * the fields is incorrect, an error text indicating the source of the problem
	 * will appear
	 * 
	 * @param event
	 *            add pressed
	 * @throws IOException
	 */
	@FXML
	private void update_products_list(ActionEvent event) throws IOException {

		this._product_price = product_price.getText();
		this._product_des = product_des.getText();
		this._product_id = product_id.getText();
		this._product_name = product_name.getText();
		this._product_type = product_type.getText();
		price_error_txt.setVisible(false);
		ID_exist_error_txt.setVisible(false);

		if (check_price(this._product_price) && check_id(this._product_id)) {

			price_error_txt.setVisible(false);
			p = new Product(this._product_id, this._product_name, this._product_type,
					Float.parseFloat(this._product_price));

			try {
				f = Float.parseFloat(this._product_price);// price

				System.out.println("float f = " + f);
			} catch (NumberFormatException nfe) {
				System.out.println("NumberFormatException: " + nfe.getMessage());
			}

			p.setProductDesc(this._product_des);
			p.setPrice(f);
			// p.setProductImage(null);

			if (image_path.getText().equals(""))// if the network employee didn't enter a photo
			{
				String path = "";
				if (!CatalogController.add_new_product(p, path))
					System.out.println("error image");
			}

			else {
				byte[] b = image_path.getText().getBytes();
				p.setProductImage(b);
				if (!CatalogController.add_new_product(p, image_path.getText())) {
					txtPathError.setVisible(true);
					return;
				}
			}

			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/catalog/NetWork_Employee_Gui.fxml").openStream());
			CatalogController.changeWindow(root);

		}

	}
	/**
	 * validate the price field
	 * @param price_string
	 * @return
	 */
	private boolean check_price(String price_string) {
		boolean flag = true;

		for (int i = 0; i < price_string.length(); i++) {

			if (price_string.charAt(i) < '0' || price_string.charAt(i) > '9') {
				price_error_txt.setVisible(true);
				flag = false;
			}
		}
		return flag;
	}

	/**
	 * validate id field
	 * @param id
	 * @return
	 */
	private boolean check_id(String id) {
		boolean flag = true;
		products = CatalogController.getProducts();
		for (int i = 0; i < products.size(); i++) {

			if (products.get(i).id.equals(id)) {
				ID_exist_error_txt.setVisible(true);
				flag = false;
			}
		}
		return flag;
	}

	/**
	 * a handler to back button, will initiate a return to the main menu
	 * @param event
	 * @throws Exception
	 */
	@FXML
	private void return_toMain(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/catalog/NetWork_Employee_Gui.fxml").openStream());
		CatalogController.changeWindow(root);
	}

}
