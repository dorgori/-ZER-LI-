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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * a gui to add or eddit a discount
 * 
 * @author sagi arieli
 *
 */
public class DiscountAddOrEditGui implements Initializable {

	static Product p;

	@FXML
	private TextField discount_field;

	@FXML
	private Button add_btn;

	@FXML
	private Text name_txt;

	@FXML
	private Button close_btn;

	@FXML
	private Text price_txt;

	@FXML
	private Text id_txt;

	@FXML
	private Text prompt_txt;

	/**
	 * add button handler | if the product is on discount then its edit the handler
	 * can handle both cases (item is already on discount or not) it will call the
	 * appropriate method to handle either add or edit
	 * 
	 * @param event
	 *            add/edit button clicked
	 * @throws IOException
	 */
	@FXML
	void add_btn_clicked(ActionEvent event) throws IOException {

		try {
			if (discount_field.getText().length() < 1 || Float.parseFloat(discount_field.getText()) > 99
					|| Float.parseFloat(discount_field.getText()) < 1) {
				prompt_txt.setVisible(true);
			}

			else {
				if (p.isOnDiscount())// edit
				{
					Discount d = new Discount(LoginController.sEmployee.getS_ID(), p.getId(),
							Float.parseFloat(discount_field.getText()));
					CatalogController.editDiscount(d);
				} else // add
				{
					Discount d = new Discount(LoginController.sEmployee.getS_ID(), p.getId(),
							Float.parseFloat(discount_field.getText()));
					CatalogController.addDiscount(d);
				}
				System.out.println("checkeck1");
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				FXMLLoader loader = new FXMLLoader();
				Pane root = loader.load(getClass().getResource("/catalog/DiscountMenu.fxml").openStream());
				LoginController.setStageTitle("DiscountMenu");
				CatalogController.changeWindow(root);
			}
		} catch (Exception e) {
			prompt_txt.setVisible(true);
		}
	}

	/**
	 * handler for the close button, will return to the discount menu
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void close_btn_clicked(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/catalog/DiscountMenu.fxml").openStream());
		LoginController.setStageTitle("DiscountMenu");
		CatalogController.changeWindow(root);
	}

	/**
	 * Initialize the gui, set the correct add/edit button text.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		p = CatalogController.PDiscountToAdd;
		if (p.isOnDiscount()) {
			add_btn.setText("EDIT");
			discount_field.setText("" + p.getDiscount());
			price_txt.setText("" + p.getPrice() / (1 - (p.getDiscount() / 100)));
		} else {
			price_txt.setText("" + p.getPrice());
		}
		id_txt.setText(p.getId());
		name_txt.setText(p.getName());
	}

}
