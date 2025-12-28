package org.example.banquecentraleservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AgenceBancaire {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String ville;
    // Utilisé pour mapper le code banque du chèque vers une agence
    private String codeBanque;
    // L'URL dynamique du micro-service de cette agence spécifique
    private String urlWebService;
}
