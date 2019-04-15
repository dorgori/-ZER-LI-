package catalog;

import java.io.Serializable;

/**
 * 
 * this class represents a catalog product
 * also it can represent an item on discount 
 *
 */
@SuppressWarnings("serial")
public class Product extends Item implements Serializable, Comparable{

	private String _product_Type;
	private boolean isOnDiscount=false;
	private float discount;
	private int priority=1; // 1= nor in discount, 0= in discount...
	private String productDesc;
	private byte[] productImage;
	private String pType;
	
	public Product(String id,String name,String type, float price )
	{
		this.type="Catalog";
		this.id=id;
		this.name=name;
		this.price=price;
		this.quant=1;
		_product_Type=type;
		productDesc=null;
		priority =1;
		discount=0;
		isOnDiscount=false;
		productImage=null;
	}
	
	public Product(String productID, String productName, float price, int quant) {
		this.type="Catalog";
		this.id=productID;
		this.name=productName;
		this.price=price;
		this.quant=quant;
	}

	public int getQuant() {
		return quant;
	}

	public void setQuant(int quant) {
		this.quant = quant;
		super.quant=this.quant;
	}

	public byte[] getProductImage() {
		return productImage;
	}

	public void setProductImage(byte[] productImage) {
		this.productImage = productImage;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public boolean isOnDiscount() {
		return isOnDiscount;
	}

	public void setOnDiscount(boolean isOnDiscount) {
		this.isOnDiscount = isOnDiscount;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	
	public float getPrice() {
		return super.getPrice();
	}

	public void setPrice(float price) {
		super.setPrice(price);
	}

	public String get_product_ID() {
		return super.getId();
	}
	
	public void set_product_ID(String _product_ID) {
		super.setId(_product_ID);
	}
	
	public String get_product_Name() {
		return super.getName();
	}
	
	public void set_product_Name(String _product_Name) {
		super.setName(_product_Name);
	}
	
	public String get_product_Type() {
		return _product_Type;
	}
	
	public void set_product_Type(String _product_Type) {
		this._product_Type = _product_Type;
	}
	public String toString()
	{
		if(!isOnDiscount)
			return(this.get_product_Name()+" "+this.getPrice());
		else
			return(this.get_product_Name()+" "+this.getPrice()+" "+"***DISCOUNT***"+this.discount +"%");	
	}


	public String getpType() {
		return pType;
	}

	public void setpType(String pType) {
		this.pType = pType;
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return  this.priority-((Product) arg0).getPriority();
	}

	public String cartString() {
		String ret = "Catalog "+this.get_product_ID()+" "+this.get_product_Name();
		if(quant>1)ret= ret + " quantity:"+quant;
		return ret;
		
	}

}
