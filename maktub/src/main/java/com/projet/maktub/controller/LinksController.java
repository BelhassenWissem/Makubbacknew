package com.projet.maktub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.projet.maktub.model.Links;
import com.projet.maktub.model.OrderClient;
import com.projet.maktub.model.Person;
import com.projet.maktub.model.Product;
import com.projet.maktub.repository.PersonRepository;
import com.projet.maktub.repository.ProductRepository;
import com.projet.maktub.services.LinksService;
import com.projet.maktub.services.OrderClientService;
import com.projet.maktub.services.PersonService;


@CrossOrigin(origins = "*" )
@RestController
@RequestMapping("links")

public class LinksController {


	@Autowired 
	PersonService personService;
	

	@Autowired 
	LinksService linkService;
	
	@Autowired 
	PersonRepository       personRepository;
	

	
	 
	
	
	
	
	
	
	@GetMapping
	public ResponseEntity<List<Links>> getAllUserLinks
					(@RequestParam(value="email", required=true) String email){
		List<Links> listeLinks = this.linkService.getAlluserLinks(email);
		
		if(listeLinks!=null) {
			return new ResponseEntity<List<Links>>(listeLinks,HttpStatus.OK);
		}
		else 
			return new ResponseEntity<List<Links>>(listeLinks,HttpStatus.NOT_FOUND);
	}
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
}
