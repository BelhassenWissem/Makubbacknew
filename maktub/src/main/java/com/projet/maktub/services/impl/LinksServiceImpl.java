package com.projet.maktub.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projet.maktub.model.Links;
import com.projet.maktub.model.OrderClient;
import com.projet.maktub.model.Person;
import com.projet.maktub.model.Product;
import com.projet.maktub.repository.PersonRepository;
import com.projet.maktub.services.LinksService;


@Service
public class LinksServiceImpl implements LinksService {


	@Autowired
	PersonRepository personRepository;
	
	
    @Override
    public Person getPersonById(Integer id) {
        return personRepository.findById(id).orElse(null);
    }
	
  
/*
    @Override
    public void removeLinkFromUser(Integer userId, Integer itemId) {
    	  Person person = getPersonById(userId);
          if (person != null) {
        	  person.getLinks().removeIf(item -> item.getId().equals(itemId));
            personRepository.save(person);
        }
    }
*/
	
	
	
	
	
	
	@Override
	public List<Links> getAlluserLinks(String mail) {
		Person person;
		person =  this.personRepository.findByMail(mail).get();
		 List<Links> liste = new ArrayList<>();
		 if(person!=null) {
			liste = person.getLinks();

					}
		return liste;
	}
	
	
	
	
	
	

	
	
	
	
	
	
	
}
