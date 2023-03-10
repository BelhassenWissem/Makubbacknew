package com.projet.maktub.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.projet.maktub.security.JwtTokenProvider;

import org.springframework.stereotype.Service;

import com.projet.maktub.dto.PersonDto;
import com.projet.maktub.exception.CustomException;
import com.projet.maktub.model.Links;
import com.projet.maktub.model.OrderClient;
import com.projet.maktub.model.Person;
import com.projet.maktub.model.Product;
import com.projet.maktub.model.Role;
import com.projet.maktub.repository.OrderClientRepository;
import com.projet.maktub.repository.PersonRepository;


import com.projet.maktub.services.PersonService;



@Service
public class PersonServiceImpl implements PersonService {

  private PersonRepository personRepository;
  private OrderClientRepository orderClientRepository;

  @Autowired
  public PersonServiceImpl(PersonRepository personRepository, OrderClientRepository orderClientRepository) {
    this.personRepository = personRepository;
    this.orderClientRepository = orderClientRepository;
  }
  
  
	 @Autowired
	  private PasswordEncoder passwordEncoder;

	  @Autowired
	  private JwtTokenProvider jwtTokenProvider;

	  @Autowired
	  private AuthenticationManager authenticationManager;

	  public String signin(String email, String password) {
	    try {
	      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
	      return jwtTokenProvider.createToken(email, personRepository.findByMail(email).get().getRoles());
	    } catch (AuthenticationException e) {
	      throw new CustomException("Invalid username/password supplied",HttpStatus.UNPROCESSABLE_ENTITY);
	    }
	  }

	  public String signup(Person user) {
	    if (!personRepository.existsByMail(user.getMail())) {
	      user.setPassword(passwordEncoder.encode(user.getPassword()));
	      user.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_CLIENT)));

	      personRepository.save(user);
	      return jwtTokenProvider.createToken(user.getMail(), user.getRoles());
	    } else {
	      throw new CustomException("email is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
	    }
	  }
  
	  
	  @Override
	    public boolean addLinkToUser(Person person, String url , String name) {
		  Person persond;
			persond =  this.personRepository.findByMail(person.getMail()).get();
            Links link = new Links(name , url , persond);
	        if (persond.getLinks().add(link)) {
	            this.personRepository.save(persond);
	            return true;
	        }
	        return false;
	    }
  
  

	
	@Override
	public boolean addFriendToUser(Person person,  Person friend) {
		Person persond;
		Person friendd;
		persond =  this.personRepository.findByMail(person.getMail()).get();
		friendd = this.personRepository.findByMail(friend.getMail()).get();
	
		 List<Person> lista = new ArrayList<>();
		 if(persond!=null) {
			lista = persond.getFollowers();
				if(lista.add(friendd)) {
					Set<Person> seto = new HashSet<>(lista);
					lista.clear();
					lista.addAll(seto);
					this.personRepository.save(persond);
					return true;
				}
				}
				return false;
	}
	
	
	@Override
	public boolean addfollowing(Person persono,  Person friendo) {
		Person persondd;
		Person frienddd;
		persondd =  this.personRepository.findByMail(persono.getMail()).get();
		frienddd = this.personRepository.findByMail(friendo.getMail()).get();
		
		 List<Person> liste = new ArrayList<>();
		 if(persondd!=null) {
			liste = persondd.getFollowing();
				if(liste.add(frienddd)) {
					Set<Person> set = new HashSet<>(liste);
					liste.clear();
					liste.addAll(set);
					this.personRepository.save(persondd);
					return true;
				}
				}
				return false;
	}


	@Override
	public List<Person> getAlluserFollowing(String mail) {
		Person person;
		person =  this.personRepository.findByMail(mail).get();
		 List<Person> liste = new ArrayList<>();
		 if(person!=null) {
			liste = person.getFollowing();
			Set<Person> set = new HashSet<>(liste);
			liste.clear();
			liste.addAll(set);

					}
		return liste;
	}
	
	@Override
	public List<Person> getAlluserFriends(String mail) {
		Person person;
		person =  this.personRepository.findByMail(mail).get();
		 List<Person> liste = new ArrayList<>();
		 if(person!=null) {
			liste = person.getFollowers();
			Set<Person> set = new HashSet<>(liste);
			liste.clear();
			liste.addAll(set);
					}
		return liste;
	}

	
	
  @Override
  public PersonDto save(PersonDto dto) {
 
    return PersonDto.fromEntity(
    		personRepository.save(
    				PersonDto.toEntity(dto)
        )
    );
  }
  
  

  @Override
	public boolean removeFollowingFromUser(Person person , Person friend) {
		
			Person persond;
			Person friendd;
			persond =  this.personRepository.findByMail(person.getMail()).get();
			friendd = this.personRepository.findByMail(friend.getMail()).get();		
			if(persond!=null) {
			if(persond.getFollowing().remove(friendd)) {
				this.personRepository.save(persond);
				return true;
			}
			}
			return false;
	}
	
  
  
  
  
  
  
  @Override
	public boolean removeFriendFromUser(Person person , Person friend) {
		
			Person persond;
			Person friendd;
			persond =  this.personRepository.findByMail(person.getMail()).get();
			friendd = this.personRepository.findByMail(friend.getMail()).get();		
			if(persond!=null) {
			if(persond.getFollowers().remove(friendd)) {
				this.personRepository.save(persond);
				return true;
			}
			}
			return false;
	}
	
  
	
	@Override
	public boolean updateUser(String email,String username,String tel, String nom,String prenom , String adresse,  String fonction ) {
		Optional<Person> opt = this.personRepository.findByMail(email);
		Person user;
		if(opt.isPresent()) {
			user =  opt.get();
			user.setNom(nom);
			user.setPrenom(prenom);
			user.setAdresse(adresse);
			user.setFonction(fonction);
			user.setTel(tel);
			user.setUsername(username);
		
			
			
			
			
			 this.personRepository.save(user);
			 return true;
		}
		return false;
	}
	
  

	@Override
	public Optional<Person> findByEmail(String email) {
		return this.personRepository.findByMail(email);
	}

  @Override
  public PersonDto findById(Integer id) {
  
    return personRepository.findById(id)
        .map(PersonDto::fromEntity)
        .orElseThrow(() -> new EntityNotFoundException(
            "Aucun Client avec l'ID = " + id + " n' ete trouve dans la BDD")
        );
  }

  @Override
  public List<PersonDto> findAll() {
    return personRepository.findAll().stream()
        .map(PersonDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public void delete(Integer id) {

    personRepository.deleteById(id);
  }
  
  
  
	@Override
	public boolean saveUser(Person utilisateur) {
		utilisateur.setMail(utilisateur.getMail());
		utilisateur.setPassword(utilisateur.getPassword());

		if(this.personRepository.save(utilisateur)!=null) return true;
		else 
			return false;
	}
}
