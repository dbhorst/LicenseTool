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
			License newLicense = new License("License Tool", new GregorianCalendar());
			newLicense.setExpirationDate(new GregorianCalendar(2020, 01, 01));
			repository.save(newLicense);
		};
	}
	
	public static void main(String[] args) {
		SpringApplication.run(LicenseToolsApplication.class, args);
	}
}
