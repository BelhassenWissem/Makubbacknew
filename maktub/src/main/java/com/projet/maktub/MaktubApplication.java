package com.projet.maktub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MaktubApplication {
	
	@GetMapping("/getData")
	
		public String getMessage() {
			return "Access by ssl protocol";
		
	}

	public static void main(String[] args) {
		SpringApplication.run(MaktubApplication.class, args);
	}

}
