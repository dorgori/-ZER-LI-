package catalog;

import java.io.Serializable;
/**
 * item is a collective to both catalog product and self made item
 * it hold the needed attributes to show the item on tables
 * including id, name, price, type and quant if it was ordered
 * 
 *
 */
public abstract class Item implements Serializable {
	protected String type;
	protected String id;
	protected String name;
	protected float price;
	protected int quant;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getQuant() {
		return quant;
	}

	public void setQuant(int quant) {
		this.quant = quant;
	}

	abstract public String cartString();
}
