package com.projet.maktub.services;

import java.util.List;

import com.projet.maktub.model.Links;
import com.projet.maktub.model.Person;

public interface LinksService {

	
	/*    void removeLinkFromUser(Integer userId, Integer linkId);*/
	    Person getPersonById(Integer id);

	
	List<Links> getAlluserLinks(String mail) ;

}
