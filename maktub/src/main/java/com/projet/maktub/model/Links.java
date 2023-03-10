package com.projet.maktub.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="Links")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Links {
	
	  @Id
	  @Column(name = "url")
	  private String url;
	  
	  @Column(name = "name")
	  private String name;
	
	 
	 
	



	@ManyToOne(fetch = FetchType.LAZY )
	    @JoinColumn(name = "person")
	    private Person person;
	  



	
	  
	    
	  
}


