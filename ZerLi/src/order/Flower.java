package order;

import java.io.Serializable;

/**
 * this class describes a Flower
 * 
 * @param _fName
 *            flower's name
 * @param fID
 *            flower's ID (PK)
 * @param fColor
 *            flower's color
 * @param _fPrice
 *            flower's price for 1 quantity
 * @param quant
 *            the quantity of the Flowers in an order
 *
 */
@SuppressWarnings("serial")
public class Flower implements Serializable {

	private String _fName;
	private int _fID;
	private String _fColor;
	private int _fPrice;
	private int quant = 1;

	public Flower(String name, int id, String color, int price) {
		_fName = name;
		_fID = id;
		_fColor = color;
		_fPrice = price;
	}

	public Flower(int id, String name, String color, int quant, int price) {
		_fName = name;
		_fID = id;
		_fColor = color;
		_fPrice = price;
		this.quant = quant;
	}

	public String get_fName() {
		return _fName;
	}

	public void set_fName(String _fName) {
		this._fName = _fName;
	}

	public int get_fID() {
		return _fID;
	}

	public void set_fID(int _fID) {
		this._fID = _fID;
	}

	public String get_fColor() {
		return _fColor;
	}

	public void set_fColor(String _fColor) {
		this._fColor = _fColor;
	}

	public int get_fPrice() {
		return _fPrice;
	}

	public void set_fPrice(int _fPrice) {
		this._fPrice = _fPrice;
	}

	public String toString() {
		String str = "";
		str +="ID:"+ _fID + " \tName:" + _fName + " \tColor:" + _fColor + " \tPrice:" + _fPrice;
		return str;

	}

	public String toStringtoOrder() {
		String str = "";
		str +="ID:"+ _fID + "     Name:" + _fName + "     Color:" + _fColor + "     Price:" + _fPrice * quant + "     Quant:" + quant;
		return str;

	}

	public int getQuant() {
		return quant;
	}

	public void setQuant(int quant) {
		this.quant = quant;
	}
}
