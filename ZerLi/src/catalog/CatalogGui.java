package catalog;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
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
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import order.Order;
import order.OrderController;

public class CatalogGui  implements Initializable {

	ObservableList<String> list;
	private static int selectedListIndex;
	private Product p;
	private ArrayList<Product> products = new ArrayList<Product>();
	public Image productImage;

	@FXML
    private ListView<String> lstProductsList = new ListView<String>();

    @FXML
    private ImageView imgProductImage;

    @FXML
    private TextArea txtProductsDesc;
    
    @FXML
    private Text txtProductPrice;
    
    @FXML
    private Button btnAddToCart;

    @FXML
    private Button btnReturnToCustomerMenu;

    @FXML
    private Button btnToOrderMenu;
    
    @FXML
    private Text dis_txt;

    @FXML
    private Text dis_price_txt;
    
    void lstSetCatalogList(ArrayList<Product> _products)
    {
       	ArrayList<String> arr = new ArrayList<String>();
       	products=_products;
       	for(Product prod:_products)
       	{
       		arr.add(prod.get_product_ID()+" "+prod.get_product_Name());
       		
       	}
    	list=FXCollections.observableArrayList(arr);
    	lstProductsList.getItems().addAll(list);
    }
    
    @FXML
	void chooseProductFromList1(MouseEvent event)throws Exception
	{
		selectedListIndex = lstProductsList.getSelectionModel().getSelectedIndex(); //save index of picked product
    	if(selectedListIndex==-1)
    		return;
		setProductDetails();
	}
    
    void setProductDetails() throws FileNotFoundException
    {
		p=products.get(selectedListIndex);
    	txtProductsDesc.setText(p.getProductDesc());
    	if (p.isOnDiscount()) {
    		txtProductPrice.setText(Float.toString((float)(int)(p.getPrice() / (1 - (p.getDiscount() / 100)))));
    		dis_txt.setVisible(true);
    		dis_price_txt.setText(Float.toString(p.getPrice()) + "	" +"("+p.getDiscount()+"% OFF)");
    		dis_price_txt.setVisible(true);
    	}
    	else {
    		txtProductPrice.setText(Float.toString(p.getPrice()));
    		dis_txt.setVisible(false);
    		dis_price_txt.setVisible(false);
    	}
    	if(p.getProductImage()!=null)
    	{
		productImage=new Image(new ByteArrayInputStream(p.getProductImage()));
    	}
    	else
    	{
    		productImage=null;
    	}
    	imgProductImage.setImage(productImage);
    }
 
    @FXML
    void addToCartListener(ActionEvent event)throws Exception
    {
    	// if quant isn't saved then make here an instance of the product array and make the changes on it, once you are done set the order's array to the one you eddited here.
    	boolean found=false;
    	for(Product pro: OrderController.order.getOrderedProducts())
    	{
    		if (p.get_product_ID().equals(pro.get_product_ID()))
    		{
    			pro.setQuant(pro.getQuant()+1);
    			found=true;
    		}
    	}
    	if(!found)
    	{
    		p.setQuant(1);
    		OrderController.order.getOrderedProducts().add(p);
    	}
    	OrderController.order.setPrice(OrderController.order.getPrice()+p.getPrice());
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		FXMLLoader loader = new FXMLLoader();
		LoginController.setStageTitle("create order menu window");
		Pane root = loader.load(getClass().getResource("/order/CreateOrderGui.fxml").openStream());
		CatalogController.changeWindow(root);
    }
    
    
	@FXML
    void ReturnToCustomerMenuListener(ActionEvent event)throws Exception
    {
	 	while(!OrderController.order.getOrderedProducts().isEmpty()) {
    		OrderController.order.getOrderedProducts().remove(0);
    	}
    	OrderController.order=new Order();
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		FXMLLoader loader = new FXMLLoader();
		LoginController.setStageTitle("customer menu window");
		Pane root = loader.load(getClass().getResource("/client/CustomerMenu.fxml").openStream());
    	CatalogController.changeWindow(root);
    }
    
	@FXML
	void ToOrderMenuListener(ActionEvent event)throws Exception
	{
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		FXMLLoader loader = new FXMLLoader();
		LoginController.setStageTitle("create order window");
		Pane root = loader.load(getClass().getResource("/order/CreateOrderGui.fxml").openStream());
		CatalogController.changeWindow(root);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lstSetCatalogList(CatalogController.getStoreProducts(LoginController.customer.getStoreID()));
		lstProductsList.refresh();
	}
	

	
	
}
