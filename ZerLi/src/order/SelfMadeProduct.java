package order;

import java.io.Serializable;
import java.util.ArrayList;

import catalog.Item;

/**
 * describes a self made product Item. extends from Item class
 * 
 * @param _productType
 *            name of the flower
 * @param flowers
 *            array list of flowers
 */
@SuppressWarnings("serial")
public class SelfMadeProduct extends Item implements Serializable {

	private String _productType;
	private ArrayList<Flower> flowers;

	public SelfMadeProduct(String id, String productType, int quant, float price, ArrayList<Flower> flowers) {
		_productType = productType;
		super.setType("Self-Made");
		super.id = id;
		super.setName(this._productType);
		super.setQuant(quant);
		super.setPrice(price);
		this.flowers = flowers;
	}

	public SelfMadeProduct(String id, String productType, float price, ArrayList<Flower> flowers) {
		_productType = productType;
		super.setType("Self-Made");
		super.id = id;
		super.setName(this._productType);
		super.setQuant(1);
		super.setPrice(price);
		this.flowers = flowers;
	}

	public SelfMadeProduct(String id, String productType, int quant, float price) {
		_productType = productType;
		super.setType("Self-Made");
		super.id = id;
		super.setName(this._productType);
		super.setQuant(quant);
		super.setPrice(price);
	}

	public String get_productType() {
		return _productType;

	}

	public int getQuant() {
		return super.getQuant();
	}

	public void setQuant(int quant) {
		super.setQuant(quant);
	}

	public void set_productType(String _productType) {
		this._productType = _productType;
		super.setName(this._productType);
	}

	public float get_price() {
		return super.getPrice();
	}

	public void set_price(float _price) {
		super.setPrice(_price);
	}

	public ArrayList<Flower> getFlowers() {
		return flowers;
	}

	public void setFlowers(ArrayList<Flower> flowers) {
		this.flowers = flowers;
	}

	@Override
	public String cartString() {
		return null;
	}

}
