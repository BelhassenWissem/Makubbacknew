package com.projet.maktub.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.projet.maktub.model.Links;
import com.projet.maktub.model.OrderClient;
import com.projet.maktub.model.Person;
import com.projet.maktub.model.Product;
import com.projet.maktub.repository.PersonRepository;
import com.projet.maktub.services.PersonService;

import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;



@CrossOrigin(origins = "*" )
@RestController
@RequestMapping("persons")
public class PersonController {



	@Autowired 
	PersonService personService;
	
	@Autowired
	PersonRepository  personRepository;
	
	
	 
	  @PostMapping("/signin")
	  @ApiResponses(value = {//
	      @ApiResponse(code = 400, message = "Something went wrong"), //
	      @ApiResponse(code = 422, message = "Invalid username/password supplied")})
	  public Map<String,String> login(//
	      @ApiParam("email") @RequestParam String email, //
	      @ApiParam("password") @RequestParam String password) {
	    return Collections.singletonMap("token", personService.signin(email, password));
	  }

	  @PostMapping("/signup")
	  @ApiResponses(value = {//
	      @ApiResponse(code = 400, message = "Something went wrong"), //
	      @ApiResponse(code = 403, message = "Access denied"), //
	      @ApiResponse(code = 422, message = "Username is already in use"), //
	      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
	  public ResponseEntity<Map<String,String>> signup(@ApiParam("Signup User") @RequestBody Person user) {
		  
	    return new ResponseEntity<Map<String,String>>(Collections.singletonMap("token", personService.signup(user)),HttpStatus.OK);
	  }

	
	
	
	
	
    
	@GetMapping
	public ResponseEntity<List<Person>> getAllPersons(@RequestParam(required = false) String nom) {
		try {
			List<Person> persons = new ArrayList<Person>();
				personRepository.findAll().forEach(persons::add);
				personRepository.findByNom(nom).forEach(persons::add);
			if (persons.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(persons, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping("/{idperson}")
	public ResponseEntity<Person> getPersonById(@PathVariable("idperson") Integer idperson) {
		Optional<Person> personData = personRepository.findById(idperson);

		if (personData.isPresent()) {
			return new ResponseEntity<>(personData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	
	
	
	@PostMapping("/create")
	public ResponseEntity<Person> create(@RequestParam("idperson")Integer idperson, @RequestParam("mail")String mail, @RequestParam("password")String password ) throws IOException {
		Person pers = new Person(idperson,mail,password);
		Person _person = personRepository.save(pers);
		return new ResponseEntity<>(_person, HttpStatus.CREATED);
	
	}
	
	
	
	
	
	@PostMapping(path="/new")
	public ResponseEntity<Person> create(@RequestBody Person utilisateur){
		
		try {
			this.personService.saveUser(utilisateur);
			return new ResponseEntity<Person>(utilisateur,HttpStatus.ACCEPTED);
		}
		catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

		}
	}
	
	
	@GetMapping(path="/getUser")
	public ResponseEntity<Person> getUser(@RequestParam(value="email", required=true) String email){
		Person user = this.personRepository.findByMail(email).get();
		
			return new ResponseEntity<Person>(user,HttpStatus.OK);
		}
	

	
	@PostMapping(path="/addFriendToUser")
	public ResponseEntity<Person> addFriendToUser(@RequestBody ObjectNode json){
		Person person = new Person();
		Person friend = new Person();
		
		try {
			person = new ObjectMapper().treeToValue(json.get("person"), Person.class);
			friend = new ObjectMapper().treeToValue(json.get("friend"), Person.class);
			
			boolean test2 = this.personService.addfollowing(friend , person);
			boolean test = this.personService.addFriendToUser(person, friend);
			if(test && test2)
			return new ResponseEntity<Person>(friend,HttpStatus.OK);

		} catch (JsonProcessingException e) {
			System.out.println("Parsing Exception!!");
			e.printStackTrace();
			return new ResponseEntity<Person>(HttpStatus.NOT_ACCEPTABLE);

		}
		return new ResponseEntity<Person>(HttpStatus.NOT_ACCEPTABLE);

	}
	
	

	@PostMapping(path="/addFollowing")
	public ResponseEntity<Person> addFollowing(@RequestBody ObjectNode json){
		Person person = new Person();
		Person friend = new Person();
		
		try {
			person = new ObjectMapper().treeToValue(json.get("person"), Person.class);
			friend = new ObjectMapper().treeToValue(json.get("friend"), Person.class);
			

			boolean test = this.personService.addfollowing(person, friend);
			if(test)
			return new ResponseEntity<Person>(friend,HttpStatus.OK);

		} catch (JsonProcessingException e) {
			System.out.println("Parsing Exception!!");
			e.printStackTrace();
			return new ResponseEntity<Person>(HttpStatus.NOT_ACCEPTABLE);

		}
		return new ResponseEntity<Person>(HttpStatus.NOT_ACCEPTABLE);

	}
	
	
	
	
	
	
	
	
	@PostMapping(path="/deleteFriendFromUser")
	public ResponseEntity<Boolean> deleteFriendFromUser(@RequestBody ObjectNode jso){
		Person user = new Person();
		Person friend = new Person();

		
			String email = jso.get("email").asText();
			String emailfr = jso.get("emailfr").asText();

			
			user = this.personRepository.findByMail(email).get();
			friend = this.personRepository.findByMail(emailfr).get();

			
			boolean done = this.personService.removeFriendFromUser(friend, user);
			boolean done2 = this.personService.removeFollowingFromUser(user, friend);

			
		return new ResponseEntity<Boolean>(done,HttpStatus.OK);
		
		
		
		

	}

	
	
	
	@GetMapping("/friends")
	public ResponseEntity<List<Person>> getAllUserFriends
					(@RequestParam(value="email", required=true) String email){
		
		List<Person> listeFriends = this.personService.getAlluserFriends(email);
		
		if(listeFriends!=null) {
			return new ResponseEntity<List<Person>>(listeFriends,HttpStatus.OK);
		}
		else 
			return new ResponseEntity<List<Person>>(listeFriends,HttpStatus.NOT_FOUND);
	}
	
	
	@GetMapping("/following")
	public ResponseEntity<List<Person>> getAllUserFollowing
					(@RequestParam(value="email", required=true) String email){
		
		List<Person> listeFriends = this.personService.getAlluserFollowing(email);
		
		if(listeFriends!=null) {
			return new ResponseEntity<List<Person>>(listeFriends,HttpStatus.OK);
		}
		else 
			return new ResponseEntity<List<Person>>(listeFriends,HttpStatus.NOT_FOUND);
	}
	
	

	
	 @PostMapping("/addlink")
	    public ResponseEntity<Person> addLinkToUser(@RequestBody ObjectNode json) {
		 Person person = new Person();
			try {
			   person = new ObjectMapper().treeToValue(json.get("person"), Person.class);
				String name = new ObjectMapper().treeToValue(json.get("name"), String.class);
				String url = new ObjectMapper().treeToValue(json.get("url"), String.class);

				boolean test = this.personService.addLinkToUser(person, name, url);
				if(test)
				return new ResponseEntity<Person>(person,HttpStatus.OK);

			} catch (JsonProcessingException e) {
				System.out.println("Parsing Exception!!");
				e.printStackTrace();
				return new ResponseEntity<Person>(HttpStatus.NOT_ACCEPTABLE);

			}
			return new ResponseEntity<Person>(HttpStatus.NOT_ACCEPTABLE);
	        
	        }
	    

	
	
	
	
	/*@PutMapping("/{idperson}")
	public ResponseEntity<Person> updatePerson(@RequestParam("idperson")Integer idperson, @RequestParam("mail")String mail, @RequestParam("password")String password ) throws IOException {
		Optional<Person> personData = personRepository.findById(idperson);

		if (personData.isPresent()) {
			Person _person = personData.get();
			_person.setIdperson(idperson);
			_person.setMail(mail);
			_person.setPassword(password);
			
			

			return new ResponseEntity<>(personRepository.save(_person), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	*/
	
	
	@DeleteMapping("/{idperson}")
	public ResponseEntity<HttpStatus> deletePerson(@PathVariable("idperson") Integer idperson) {
		try {
	       personRepository.deleteById(idperson);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping
	public ResponseEntity<HttpStatus> deleteAllPersons() {
		try {
			personRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	
	@PostMapping(path="/updateUser")
	public ResponseEntity<Boolean> updateUser( @RequestBody ObjectNode json){
		String email;
		String nom;
		String username;
		String tel;
		String prenom;
		String fonction;
		String adresse;

		
		try {
			email = new ObjectMapper().treeToValue(json.get("email"), String.class);
			nom = new ObjectMapper().treeToValue(json.get("nom"), String.class);
			prenom = new ObjectMapper().treeToValue(json.get("prenom"), String.class);
			adresse = new ObjectMapper().treeToValue(json.get("adresse"), String.class);
			fonction = new ObjectMapper().treeToValue(json.get("fonction"), String.class);
			username = new ObjectMapper().treeToValue(json.get("usrename"), String.class);
			tel = new ObjectMapper().treeToValue(json.get("tel"), String.class);

			fonction = new ObjectMapper().treeToValue(json.get("adresse"), String.class);

			

			boolean test2 = this.personService.updateUser(email,username,tel,nom,prenom ,  adresse,   fonction);
			if(test2)
			return new ResponseEntity<Boolean>(test2,HttpStatus.OK);

		} catch (JsonProcessingException e) {
			System.out.println("Parsing Exception!!");
			e.printStackTrace();
			return new ResponseEntity<Boolean>(false,HttpStatus.NOT_ACCEPTABLE);

		}			
		return new ResponseEntity<Boolean>(false,HttpStatus.NOT_ACCEPTABLE);

			
		}
		

	@PutMapping("/img")
	public ResponseEntity<Person> updateTutorial(@RequestParam("email")String email, @RequestParam(value = "picture", required = false) MultipartFile fichier) throws IOException {
		Optional<Person> tutorialData = personRepository.findByMail(email);

		if (tutorialData.isPresent()) {
			Person ut = new Person();
			
			Person _tutorial = tutorialData.get();
			
			_tutorial.setPhoto(fichier.getBytes());
			

			return new ResponseEntity<>(personRepository.save(_tutorial), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	
	@PutMapping("/upuserr")
	public ResponseEntity<Person> upuserr(@RequestParam("email")String email,@RequestParam("username")String username, @RequestParam("nom")String nom, @RequestParam("prenom")String prenom,@RequestParam("adresse")String adresse, @RequestParam("fonction")String fonction, @RequestParam("tel")String tel) {
		Optional<Person> tutorialData = personRepository.findByMail(email);

		if (tutorialData.isPresent()) {
			Person ut = new Person();
			
			Person _tutorial = tutorialData.get();
			
			_tutorial.setUsername(username);
			_tutorial.setNom(nom);
			_tutorial.setPrenom(prenom);
			_tutorial.setAdresse(adresse);
			_tutorial.setMail(email);
			_tutorial.setFonction(fonction);
			_tutorial.setTel(tel);
			
			

			return new ResponseEntity<>(personRepository.save(_tutorial), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	
	
	
	
	
	@PutMapping
	public ResponseEntity<Person> updatePerson(@RequestParam("email")String email,@RequestParam("nom")String nom, @RequestParam("prenom")String prenom , @RequestParam("username")String username, @RequestParam("tel")String tel ,@RequestParam("adresse")String adresse , @RequestParam("fonction")String fonction  ) throws IOException {
		Optional<Person> personData = personRepository.findByMail(email);

		if (personData.isPresent()) {
			Person _person = personData.get();
			_person.setMail(email);
			_person.setNom(nom);
			_person.setPrenom(prenom);
			_person.setUsername(username);
			_person.setTel(tel);
			_person.setAdresse(adresse);
			_person.setFonction(fonction);
			
			

			return new ResponseEntity<>(personRepository.save(_person), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	
}



