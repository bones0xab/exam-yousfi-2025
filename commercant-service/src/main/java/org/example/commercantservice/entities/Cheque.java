package org.example.commercantservice.entities;


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
public class Cheque {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numeroCheque;
    private String codeBanque;
    private String numeroCompte; // Ajouté pour cohérence métier
    private String nomClient;    // Ajouté pour cohérence métier
    private Double montant;
    private boolean certifie;
}