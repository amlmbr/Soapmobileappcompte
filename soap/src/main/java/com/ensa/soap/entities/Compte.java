package com.ensa.soap.entities;


import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAccessType; 
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor 
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD) 
public class Compte {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
		
	private Long id; 
	private double solde;
	@Temporal (TemporalType.DATE) 
	private Date dateCreation;
	@Enumerated (EnumType.STRING)
	private TypeCompte type;
	
}
