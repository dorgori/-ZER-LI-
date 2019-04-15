package client;

import java.io.Serializable;

/**
 * Describes the Store Class
 * 
 * @param s_ID
 *            the store ID (PK)
 * @param s_name
 *            the store name
 *
 */
@SuppressWarnings("serial")
public class Store implements Serializable {
	private String s_ID;
	private String s_name;

	public String getS_ID() {
		return s_ID;
	}

	public void setS_ID(String s_ID) {
		this.s_ID = s_ID;
	}

	public String getS_name() {
		return s_name;
	}

	public void setS_name(String s_name) {
		this.s_name = s_name;
	}

	public Store(String s_ID, String s_name) {
		super();
		this.s_ID = s_ID;
		this.s_name = s_name;
	}

	public Store(String s_ID) {
		this.s_ID = s_ID;
	}

	@Override
	public String toString() {
		return (s_ID + "," + s_name);
	}

}
