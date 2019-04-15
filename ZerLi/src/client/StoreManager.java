package client;

import java.io.Serializable;

/**
 * Describes the Store Manager Class, extends from User
 * 
 * @param s_ID the store id which store manager belongs to
 *
 */
@SuppressWarnings("serial")
public class StoreManager extends User implements Serializable {

	private String s_ID;

	public StoreManager(String Name, String Pass) {
		super(Name, Pass);
	}

	public String getS_ID() {
		return s_ID;
	}

	public void setS_ID(String s_ID) {
		this.s_ID = s_ID;
	}

}
