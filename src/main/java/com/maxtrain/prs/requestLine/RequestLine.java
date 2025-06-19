package com.maxtrain.prs.requestLine;

import javax.persistence.*;

import com.maxtrain.prs.product.Product;
import com.maxtrain.prs.request.Request;

@Entity
public class RequestLine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(columnDefinition = "int default 1")
	private int quantity;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "productId", nullable = false)
	private Product product;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "requestId", nullable = false)
	private Request request;
		
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
}
