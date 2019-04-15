package client;

import java.io.Serializable;

/**
 * Describes the User Class
 * 
 * @param name
 *            the user name (PK)
 * @param pass
 *            password of the user
 * @param login
 *            the "enum" permission of the user in zer-li
 * @param stLogin
 *            the String of permission
 * @param active
 *            the current user status logged in/logged out
 * @param blocked
 *            if blocked==1 user is blocked if blocked==0 user isn't blocked
 */
@SuppressWarnings("serial")
public class User implements Serializable {
	private String name;
	private String pass;
	private permission login;
	private String stLogin = "";
	private int active = 0;
	private int blocked;

	public enum permission {
		rejected, customer, store_emp, store_mng, network_emp, network_mng, customer_service_emp, sys_mng, blocked_user, unblock_user
	};

	public User(String Name, String Pass) {
		this.name = Name;
		this.pass = Pass;
	}

	public User(String name, String pass, permission per, int blocked) {
		this.name = name;
		this.pass = pass;
		this.login = per;
		this.blocked = blocked;
		this.setStLogin(this.login);
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public permission getLogin() {
		return login;
	}

	public void setLogin(permission login) {
		this.login = login;
	}

	public String getStLogin() {
		return stLogin;
	}

	public int getBlocked() {
		return blocked;
	}

	public void setBlocked(int blocked) {
		this.blocked = blocked;
	}

	public void setStLogin(permission per) {
		switch (per) {
		case rejected: {
			stLogin = "rejected";
			break;
		}
		case customer: {
			stLogin = "customer";
			break;
		}
		case store_emp: {
			stLogin = "store_emp";
			break;
		}
		case store_mng: {
			stLogin = "store_mng";
			break;
		}
		case network_emp: {
			stLogin = "network_emp";
			break;
		}
		case network_mng: {
			stLogin = "network_mng";
			break;
		}
		case customer_service_emp: {
			stLogin = "customer_service_emp";
			break;
		}
		case sys_mng: {
			stLogin = "sys_mng";
			break;
		}
		case blocked_user: {
			stLogin = "blocked_user";
			break;
		}
		case unblock_user: {
			stLogin = "unblock_user";
			break;
		}
		default:
			stLogin = "customer";
		}
	}

	public String convertePermissionToString(permission per) {
		switch (per) {
		case rejected: {
			return "rejected";
		}
		case customer: {
			return "customer";
		}
		case store_emp: {
			return "store_emp";
		}
		case store_mng: {
			return "store_mng";
		}
		case network_emp: {
			return "network_emp";
		}
		case network_mng: {
			return "network_mng";
		}
		case customer_service_emp: {
			return "customer_service_emp";
		}
		case sys_mng: {
			return "sys_mng";
		}
		case blocked_user: {
			return "blocked_user";
		}
		case unblock_user: {
			return "unblock_user";
		}
		default:
			return "customer";
		}
	}

	public permission converteStringToPermission(String per) {
		switch (per) {
		case "rejected": {
			return permission.rejected;
		}
		case "customer": {
			return permission.customer;
		}
		case "store_emp": {
			return permission.store_emp;
		}
		case "store_mng": {
			return permission.store_mng;
		}
		case "network_emp": {
			return permission.network_emp;
		}
		case "network_mng": {
			return permission.network_mng;
		}
		case "customer_service_emp": {
			return permission.customer_service_emp;
		}
		case "sys_mng": {
			return permission.sys_mng;
		}
		case "blocked_user": {
			return permission.blocked_user;
		}
		case "unblock_user": {
			return permission.unblock_user;
		}
		default:
			return permission.customer;
		}
	}

}
