package order;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.LoginController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * describes the self made menu gui
 * 
 * @param selectedColor
 *            the index of the color selected
 * @param selectedPrice
 *            the maximum price selected
 * @param selectedType
 *            index of the type of flower selected
 * @param slfmdprod
 *            an array list of flowers of the customer
 * @param fArr
 *            an array list of flowers from the zerli data base
 * @param flowersList
 *            an observable string of flowers from the data base
 * @param ordflowersList
 *            an observable string of flowers currently ordering
 * @param price
 *            the total price of the flowers ordered
 */
public class SelfMadeGui implements Initializable {

	private int selectedColor = -1;
	private int selectedPrice;
	private int selectedType = -1;
	private ArrayList<Flower> slfmdprod = new ArrayList<Flower>();
	private ArrayList<Flower> fArr = new ArrayList<Flower>();
	private ObservableList<String> flowersList;
	private ObservableList<String> ordflowersList;
	private float price = 0;

	@FXML
	private ComboBox<String> cmbChooseProd;
	@FXML
	private Slider sldChoosePriceRange;
	@FXML
	private Button btnOrderPage;
	@FXML
	private Button btnAddSelfMade;
	@FXML
	private ComboBox<String> cmbPickColor;
	@FXML
	private Text txtPrice;
	@FXML
	private Label lblSlider;
	@FXML
	private ListView<String> lstFlowers = new ListView<String>();
	@FXML
	private Button btnAddFlower;
	@FXML
	private ListView<String> lstOrderedFlowers = new ListView<String>();
	@FXML
	private Text lblCPrice;
	@FXML
	private Button btnRemoveFlower;
	@FXML
	private Text lblException;

	/**
	 * initialize the window with all the necessary data
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initTypeComboBox();
		initSlider();
		initColorComboBox();
		initgetFlowers();
	}

	/**
	 * Invoke by initialize Initialize the combo box of Product type
	 */
	private void initTypeComboBox() {
		ObservableList<String> list = FXCollections.observableArrayList("Bride bouquet", "Planter",
				"Flower collection");
		cmbChooseProd.setItems(list);
	}

	/**
	 * This function invoked by the initialize It sets the slider properties and
	 * sets him to listen to user slides
	 */
	private void initSlider() {
		sldChoosePriceRange.valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<?> arg0, Object arg1, Object arg2) {
				txtPrice.setText(Integer.toString((int) sldChoosePriceRange.getValue()));
			}
		});
	}

	/**
	 * Invoke by initialize Initialize the combo box of colors option the user does
	 * not have to pick color it is optional
	 */
	private void initColorComboBox() {
		ObservableList<String> list = FXCollections.observableArrayList("Yellow", "Red", "Blue", "All");
		cmbPickColor.setItems(list);
	}

	/**
	 * Invoke every time the window is open calls init functions that get properties
	 * ready
	 */
	private void initgetFlowers() {
		fArr = OrderController.client.getFlowers();
		System.out.println("SMG: Flowers- " + fArr.get(0).get_fName());
	}

	/**
	 * adds a selected flower to order
	 * 
	 * @param event
	 *            clicked on the "+" sign
	 */
	@FXML
	private void addFlower(ActionEvent event) {
		ArrayList<String> stringFlowersArr = new ArrayList<String>();
		lstOrderedFlowers.setItems(FXCollections.observableArrayList(new ArrayList<String>()));// the list is empty
		boolean found = false;
		try {
			int to_search_id = Integer.parseInt(lstFlowers.getSelectionModel().getSelectedItem().split(":")[1].split(" ")[0]);
			for (Flower singleF : slfmdprod) {
				if (singleF.get_fID() == to_search_id) {
					found = true;
					singleF.setQuant(singleF.getQuant() + 1);
					price += singleF.get_fPrice();
				}
			}
			if (!found) {
				for (Flower f : fArr) {
					if (f.get_fID() == to_search_id) {
						slfmdprod.add(f); // adding the flower to ordered flowers ArrayList//
						price += f.get_fPrice();
					}
				}
			}
			for (Flower singleF : slfmdprod) {
				stringFlowersArr.add(singleF.toStringtoOrder());
			}
			ordflowersList = FXCollections.observableArrayList(stringFlowersArr);
			lstOrderedFlowers.setItems(ordflowersList);
			lstOrderedFlowers.refresh();
			lblCPrice.setText("" + price);
		} catch (Exception e) {
		}
	}

	/**
	 * decrease 1 quantity from a flower that was added or remove it completely if
	 * quantity is 1 already
	 * 
	 * @param event
	 *            clicked on "-" sign
	 */
	@FXML
	private void decFlower(ActionEvent event) {
		ArrayList<String> stringFlowersArr = new ArrayList<String>();
		try {
			int to_search_id = Integer.parseInt(lstOrderedFlowers.getSelectionModel().getSelectedItem().split(":")[1].split(" ")[0]);
			for (Flower singleF : slfmdprod) {
				if (singleF.get_fID() == to_search_id) {
					singleF.setQuant(singleF.getQuant() - 1);
					if (singleF.getQuant() == 0) {
						singleF.setQuant(1);
						slfmdprod.remove(singleF);
						price -= singleF.get_fPrice();
						System.out.println("" + price);
						break;
					} else
						price -= singleF.get_fPrice();
				}
			}
			lstOrderedFlowers.setItems(FXCollections.observableArrayList(new ArrayList<String>()));// the list is empty
			for (Flower singleF : slfmdprod) {
				stringFlowersArr.add(singleF.toStringtoOrder());
			}

			ordflowersList = FXCollections.observableArrayList(stringFlowersArr);
			lstOrderedFlowers.setItems(ordflowersList);
			lstOrderedFlowers.refresh();
			lblCPrice.setText("" + price);
		} catch (Exception e) {
		}
	}

	/**
	 * search for the flowers filtered by the customer selection
	 * 
	 * @param event
	 *            clicked on "Search" button
	 * @throws Exception
	 */
	@FXML
	private void searchFlowers(ActionEvent event) throws Exception {
		initFlowersList(fArr);
	}

	/**
	 * Invoke when press on Add self made product change between windows and opens
	 * CreateOrderGui window
	 * 
	 * @param event
	 *            clicked on "Return to order menu"
	 * @throws Exception
	 */
	@FXML
	private void pageOrderListenner(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		FXMLLoader loader = new FXMLLoader();
		LoginController.setStageTitle("create order menu window");
		Pane root = loader.load(getClass().getResource("/order/CreateOrderGui.fxml").openStream());
		OrderController.changeWindow(root);
	}

	/**
	 * get a color of a flower to filter in the search
	 * 
	 * @param event
	 *            clicked on "Desired color" combo box
	 * @throws Exception
	 */
	@FXML
	private void chooseColor(ActionEvent event) throws Exception {
		selectedColor = cmbPickColor.getSelectionModel().getSelectedIndex();
	}

	/**
	 * listener to add button , invoke when click on add self made product adding
	 * SelfMadeProduct to array list of SelfMadeProduct in user order
	 * 
	 * @param event
	 *            clicked on "Add self-Made product"
	 * @throws Exception
	 */
	@FXML
	private void addSelfMadeListenner(ActionEvent event) throws Exception {
		selectedType = cmbChooseProd.getSelectionModel().getSelectedIndex();
		if (selectedType != -1 && sldChoosePriceRange.getValue() != 0) // only if the customer picked values
		{
			OrderController.addSelfMade(new SelfMadeProduct("S" + OrderController.order.getSelfMadeCounter(),
					cmbChooseProd.getValue(), price, slfmdprod)); // set constructor
			OrderController.order.setSelfMadeCounter(OrderController.order.getSelfMadeCounter() + 1);
			OrderController.order.setPrice(OrderController.order.getPrice() + price);
			pageOrderListenner(event);// go back to order menu
		} else {
			lblException.setVisible(true);
		}
	}

	/**
	 * shows the flowers that match the criteria the user made
	 * 
	 * @param flowersArr
	 *            array list of all the flowers from the data base
	 */
	private void initFlowersList(ArrayList<Flower> flowersArr) {
		ArrayList<String> arr = new ArrayList<String>();
		flowersList = FXCollections.observableArrayList(arr);
		if (!flowersList.isEmpty())
			flowersList.removeAll();
		lstFlowers.setItems(flowersList);
		lstFlowers.refresh();
		selectedPrice = (int) sldChoosePriceRange.getValue();
		selectedColor = cmbPickColor.getSelectionModel().getSelectedIndex();
		for (Flower singleF : flowersArr) {
			switch (selectedColor) {
			case 0:
				if (singleF.get_fColor().equals("Yellow") && singleF.get_fPrice() <= selectedPrice)
					arr.add(singleF.toString());
				break;
			case 1:
				if (singleF.get_fColor().equals("Red") && singleF.get_fPrice() <= selectedPrice)
					arr.add(singleF.toString());
				break;
			case 2:
				if (singleF.get_fColor().equals("Blue") && singleF.get_fPrice() <= selectedPrice)
					arr.add(singleF.toString());
				break;
			case 3:
				if (singleF.get_fPrice() <= selectedPrice)
					arr.add(singleF.toString());
				break;
			case -1:
				if (singleF.get_fPrice() <= selectedPrice)
					arr.add(singleF.toString());
				break;
			}
		}
		flowersList = FXCollections.observableArrayList(arr);
		System.out.println("AFTER" + arr);
		lstFlowers.getItems().addAll(flowersList);
		lstFlowers.refresh();
	}
}
