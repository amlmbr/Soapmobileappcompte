package com.ensa.soap;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.ensa.soap.entities.Compte;
import com.ensa.soap.entities.TypeCompte;
import com.ensa.soap.repositories.CompteRepository;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ensa.soap"})
public class SoapApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoapApplication.class, args);
	}
	 @Bean
	    public CommandLineRunner commandLineRunner(CompteRepository compteRepository) {
	        return args -> {
	            // Création et sauvegarde des comptes
	            Compte compte1 = new Compte(null, Math.random() * 9000, new Date(), TypeCompte.EPARGNE);
	            Compte compte2 = new Compte(null, Math.random() * 9000, new Date(), TypeCompte.COURANT);
	            Compte compte3 = new Compte(null, Math.random() * 9000, new Date(), TypeCompte.EPARGNE);
	            
	            // Sauvegarde des comptes
	            compteRepository.save(compte1);
	            compteRepository.save(compte2);
	            compteRepository.save(compte3);
	            
	            // Affichage des comptes
	            System.out.println("Liste des comptes :");
	            compteRepository.findAll().forEach(System.out::println);
	        };
	    }
}
