package com.bookwrms.model.stage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="deletedsubscriptions")
public class DeletedSubscriptions {

	@Id
	@Column(name="id")
	String id;

	public DeletedSubscriptions() {
	}

	public DeletedSubscriptions(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
