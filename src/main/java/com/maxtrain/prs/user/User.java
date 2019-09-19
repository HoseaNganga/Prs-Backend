package com.maxtrain.prs.user;

import javax.persistence.*;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	
	private String username;
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	
	private String password;
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	
	private String firstname;
	public String getFirstname() { return firstname; }
	public void setFirstname(String firstname) { this.firstname = firstname; }
	
	private String lastname;
	public String getLastname() { return lastname; }
	public void setLastname(String lastname) { this.lastname = lastname; }
	
	private String phone;
	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }

	private String email;
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	
	private boolean isAdmin;
	public boolean getIsAdmin() { return isAdmin; }
	public void setIsAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }

	private boolean isReviewer;
	public boolean getIsReviewer() { return isReviewer; }
	public void setIsReviewer(boolean isReviewer) { this.isReviewer = isReviewer; }
}
