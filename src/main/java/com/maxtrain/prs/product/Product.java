package com.maxtrain.prs.product;

import javax.persistence.*;

import com.maxtrain.prs.vendor.Vendor;

@Entity
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(columnDefinition = "varchar(30)", nullable = false)
	private String partNbr;
	@Column(columnDefinition = "varchar(30)", nullable = false)
	private String name;
	@Column(columnDefinition = "double default 10.00", nullable = false)
	private double price;
	@Column(columnDefinition = "varchar(15) default 'Each'", nullable = false)
	private String unit;
	private String photoPath;
	@ManyToOne
	@JoinColumn(name = "vendorId", nullable = false)
	private Vendor vendor;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPartNbr() {
		return partNbr;
	}
	public void setPartNbr(String partNbr) {
		this.partNbr = partNbr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getPhotoPath() {
		return photoPath;
	}
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	
	
}
