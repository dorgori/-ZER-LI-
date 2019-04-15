package catalog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import client.Client;
import client.Customer;
import client.LoginController;
import client.StoreEmployee;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * this is the main controller for all things catalog related, every time a gui
 * wants to open a new gui it will go through here every time the gui receive
 * from the user a request that involves other classes or the database it will
 * go though here and the controller will hold basic user information needed to
 * the guis.
 *
 */
public class CatalogController {
	public static String[] parameters = null;
	public static Client client;

	public CatalogController() {
	}

	public static Customer customer;
	public static StoreEmployee sEmployeee;
	public static Product PDiscountToAdd;

	public static void setClient(Client c) {
		client = c;
	}

	/**
	 * set image with or without an image (if you want no image leave the path empty
	 * "" or null) in the scenario of not valid image path, false will be returned
	 * true will be returned otherwise
	 * 
	 * @param path
	 * @return ture = correct image path. flase = not valid path
	 */
	private static boolean image_setter(Product p, String path) {
		File newFile = new File(path);
		if (path != "" && path != null && path.length() > 1) {
			try {
				// path to file
				byte[] mybytearray = new byte[(int) newFile.length()];
				FileInputStream fis = new FileInputStream(newFile);
				BufferedInputStream bis = new BufferedInputStream(fis);

				bis.read(mybytearray, 0, mybytearray.length); // 0 is the offset
				p.setProductImage(mybytearray);
				bis.close();
			} catch (IOException e) {
				return false;
			}
		} else {
			String s = "";
			byte[] b = s.getBytes();
			p.setProductImage(b);
		}
		return true;
	}

	/**
	 * the general method for gui changing, will be used when a gui want to switch
	 * to another gui
	 * 
	 * @param root
	 *            the root represents what gui to open
	 */
	public static void changeWindow(Pane root) {
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle(LoginController.stageTitle);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				client.user_exit();
			}
		});

	}

	/**
	 * this method will return the up to date catalog items from the DB
	 * 
	 * @return ArrayList of Products, containing every catalog item
	 */
	public static ArrayList<Product> getProducts() {
		return (client.getProducts());
	}

	/**
	 * used to receive all the discounts (up to date) from the data base for a
	 * specific store
	 * 
	 * @param storeID
	 * @return array list of discounts for the requested store
	 */
	public static ArrayList<Discount> getDiscounts(String storeID) {
		return (client.getDiscounts(storeID));
	}

	/**
	 * add a new product to the catalog, with or without an image (if you want no
	 * image leave the path empty "" or null) in the scenario of not valid image
	 * path, false will be returned true will be returned otherwise. this method
	 * does not validate the input other then the image!!!! if you send it an id
	 * that is in use it will not add it but no feedback will bee returned so if you
	 * use it you should first use getProducts to check if there is a product with
	 * the same id
	 * 
	 * @param p
	 *            the product to add
	 * @param path
	 *            a string containing the path to the path of the image
	 * @return ture = correct image path. flase = not valid path
	 * @throws IOException
	 */
	public static boolean add_new_product(Product p, String path) throws IOException {
		if (!image_setter(p, path))
			return false;
		client.sendAddProductFromCatalogDB(p);
		return true;
	}

	/**
	 * removes an existing catalog product from the dataBase an incorrect p.id
	 * scenario will not receive feedback from this method. the validation is up the
	 * the method caller
	 * 
	 * @param product
	 *            the product to delete form the catalog
	 */
	public static void removeProduct(Product product) {
		client.sendDeleteProductFromCatalogDB(product);
	}

	/**
	 * edits an existing catalog product can not edit if id changed Remember the
	 * product key is his id also set image
	 * 
	 * @param p
	 * @param path
	 *            the product to edit (the edited values should be pre-set in the
	 *            product)
	 */
	public static boolean editProduct(Product p, String path) {
		if (!image_setter(p, path))
			return false;
		client.sendEditedProductFromCatalogDB(p);
		return true;
	}

	/**
	 * setter of the se attribute
	 * 
	 * @param se
	 */
	public static void setSE(StoreEmployee se) {
		sEmployeee = se;

	}

	/**
	 * Receive an up to date array of the catalog items with their store discount
	 * 
	 * @param s_ID
	 *            the store the the discount will be found at
	 * @return array list of catalog items with their discounts, sorted by discount
	 *         - thee items that has discounts will be first also the items with
	 *         discounts isOnDiscount att will be set to true and the discount att
	 *         will be set to represent the % of the discount the price will be
	 *         after discount in case you want to calculate the price before
	 *         discount here is the formula :
	 *         p.getPrice()/(1-(p.getDiscount()/100)))
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Product> getStoreProducts(String s_ID) {

		ArrayList<Product> arr;
		ArrayList<Discount> dis;
		arr = CatalogController.getProducts();
		System.out.println("ORDER CONTROLLER-PRODUCTS:" + arr);
		dis = CatalogController.getDiscounts(s_ID);
		System.out.println("ORDER CONTROLLER-DISCOUNTS:" + dis);
		ArrayList<Product> newarr = new ArrayList<Product>();
		if (dis != null) {
			for (int i = 0; i < arr.size(); i++) {
				System.out.println(i);
				for (int j = 0; j < dis.size(); j++) {
					System.out.println("I:" + i + " " + "J:" + j);
					if (arr.get(i).get_product_ID().equals((dis.get(j).getProductID()))) {
						arr.get(i).setPrice( (float) ((Math.round((arr.get(i).getPrice()) - (arr.get(i).getPrice()) * ((dis.get(j).getDis() / 100.0)))*100.0)/100.0));
						arr.get(i).setPriority(0);
						arr.get(i).setOnDiscount(true);
						arr.get(i).setDiscount(dis.get(j).getDis());
					}
				}
				newarr.add(arr.get(i));
			}
			Collections.sort(newarr);
			System.out.println(newarr);
			return newarr;
		} else
			return arr;
	}

	/**
	 * remove a discount from a store
	 * 
	 * @param s_ID
	 *            the store id
	 * @param get_product_ID
	 *            the item the discount is referring to
	 */
	public static void removeDiscount(String s_ID, String get_product_ID) {
		System.out.println("check1 " + s_ID + " " + get_product_ID);
		client.removeDiscount(s_ID, get_product_ID);

	}

	public static void setDiscountToADD(Product p) {
		PDiscountToAdd = p;

	}

	/**
	 * add discount to product on a store
	 * 
	 * @param discount
	 */
	public static void addDiscount(Discount d) {
		client.addDiscount(d);

	}

	/**
	 * @author sagi arieli send to client, edit a discount
	 * @param d
	 *            discount
	 */
	public static void editDiscount(Discount d) {
		client.editDiscount(d);

	}
}
