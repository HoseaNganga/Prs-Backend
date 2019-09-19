package com.maxtrain.prs.request;

import java.util.ArrayList;

import javax.persistence.*;

import com.maxtrain.prs.requestLine.RequestLine;
import com.maxtrain.prs.user.User;

@Entity
public class Request {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(columnDefinition = "varchar(80)", nullable = false)
	private String description;
	@Column(columnDefinition = "varchar(80)", nullable = true)
	private String justification;
	@Column(columnDefinition = "varchar(80)", nullable = true)
	private String rejectionReason;
	@Column(columnDefinition = "varchar(20) default 'NEW'", nullable = false)
	private String status;
	@Column(columnDefinition = "double default 0", nullable = false)
	private double total;
	
	@ManyToOne
	@JoinColumn(name = "userId", nullable = false)
	private User user;
		
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getJustification() {
		return justification;
	}
	public void setJustification(String justification) {
		this.justification = justification;
	}
	public String getRejectionReason() {
		return rejectionReason;
	}
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
