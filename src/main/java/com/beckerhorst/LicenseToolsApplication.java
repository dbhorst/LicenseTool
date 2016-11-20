package com.beckerhorst;

import java.util.GregorianCalendar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.beckerhorst.db.License;
import com.beckerhorst.db.LicenseRepository;

/**
 * Main application with some demo licenses for the license tool.
 * 
 * @author dirk
 */
@SpringBootApplication
public class LicenseToolsApplication {
	
	@Bean
	public CommandLineRunner loadData(LicenseRepository repository) {
		return (args) -> {
			// save a couple of licenses
			License newLicense = new License("Wireframe Scetcher", new GregorianCalendar());
			newLicense.setExpirationDate(new GregorianCalendar());
			repository.save(newLicense);
			newLicense = new License("Eclipse IDE", new GregorianCalendar());
			newLicense.setExpirationDate(new GregorianCalendar(2015, 9, 13));
			repository.save(newLicense);
			newLicense = new License("iText", new GregorianCalendar());
			newLicense.setExpirationDate(new GregorianCalendar(2017, 10, 4));
			repository.save(newLicense);
		};
	}
	
	public static void main(String[] args) {
		SpringApplication.run(LicenseToolsApplication.class, args);
	}
}
