package catalog;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
/**
 * this gui is in charge of product details editing
 *
 *
 */
public class edit_product_in_catalog_gui implements Initializable {

	public static Product p;

	@FXML
	private TextField product_name;

	@FXML
	private TextField product_type;

	@FXML
	private TextField product_price;

	@FXML
	private TextField product_description;

	@FXML
	private Button submit;
	@FXML
	private Button close;
	@FXML
	private Label id_txt;

	@FXML
	private Label error_price_txt;
	@FXML
	private TextField path_text;
	@FXML
	private Text txtPathError;

	/**
	 * submit button handler.
	 * will check all fields and try to update the products details
	 * @param event submit pressed
	 * @throws IOException
	 */
	@FXML
	void up_date_product(ActionEvent event) throws IOException {
		p.set_product_ID(id_txt.getText());
		p.set_product_Name(product_name.getText());
		p.set_product_Type(product_type.getText());
		p.setProductDesc(product_description.getText());

		if (check_price(product_price.getText())) {
			error_price_txt.setVisible(false);
			p.setPrice(Float.parseFloat(product_price.getText()));
			if (!CatalogController.editProduct(p, path_text.getText())) {
				txtPathError.setVisible(true);
				return;
			}
			
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/catalog/NetWork_Employee_Gui.fxml").openStream());
			LoginController.setStageTitle("Network_Employee_Gui");
			CatalogController.changeWindow(root);
		}

		else {
			error_price_txt.setVisible(true);
		}
	}

	/**
	 * validate the price field
	 * @param text price
	 * @return
	 */
	private boolean check_price(String text) {
		boolean flag = true;

		System.out.println("the price is " + product_price.getText());
		for (int i = 0; i < product_price.getText().length(); i++) {
			if (text.charAt(i) == '.')
				;
			else if (text.charAt(i) < '0' || text.charAt(i) > '9')
				flag = false;

		}
		return flag;
	}

	/**
	 * set current item's details
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		error_price_txt.setVisible(false);
		System.out.println(p.get_product_ID());
		id_txt.setText(p.get_product_ID());

		product_name.setText(p.get_product_Name());
		product_type.setText(p.get_product_Type());
		product_description.setText(p.getProductDesc());
		product_price.setText("" + p.getPrice());

	}

	/**
	 * back button handler 
	 * go back to Network employee main menu
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void return_back(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/catalog/NetWork_Employee_Gui.fxml").openStream());
		LoginController.setStageTitle("Network_Employee_Gui");
		CatalogController.changeWindow(root);
	}

	
	/**
	 * sets the product to edit
	 * @param p2
	 */
	public static void get_product_to_edit(Product p2) {
		p = p2;
	}

}