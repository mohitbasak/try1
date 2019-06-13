package com.mohit.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Mohit")
public class Mohit {

	@Id
	private int aadharNo;
	
	@Column
	private String name;
	
}
