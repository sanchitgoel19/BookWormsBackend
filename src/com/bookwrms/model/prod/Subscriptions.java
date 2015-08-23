package com.bookwrms.model.prod;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="subscriptions")
public class Subscriptions {

	@Id
	@Column(name="id")
	String id;

	@Column(name="name")
	String name;
	
	@Column(name="price")
	BigDecimal price;
	
	@Column(name="security")
	BigDecimal security;
	
	@Column(name="numberbooks")
	Long numberBooks;
	
	@Column(name="delieveries")
	Long delieveries;
	
	@Column(name="description")
	String description;
	
	@Column(name="region")
	String region;
	
	public Subscriptions() {
		
	}
	
	public Subscriptions(String id, String name, BigDecimal price, BigDecimal security,
			Long numberBooks, Long delieveries, String description,
			String region) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.security = security;
		this.numberBooks = numberBooks;
		this.delieveries = delieveries;
		this.description = description;
		this.region = region;
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getSecurity() {
		return security;
	}

	public void setSecurity(BigDecimal security) {
		this.security = security;
	}

	public Long getNumberBooks() {
		return numberBooks;
	}

	public void setNumberBooks(Long numberBooks) {
		this.numberBooks = numberBooks;
	}

	public Long getDelieveries() {
		return delieveries;
	}

	public void setDelieveries(Long delieveries) {
		this.delieveries = delieveries;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
}
