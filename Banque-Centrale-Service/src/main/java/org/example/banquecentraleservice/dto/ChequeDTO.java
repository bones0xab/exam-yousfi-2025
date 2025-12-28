package org.example.banquecentraleservice.dto;

import lombok.Data;

@Data
public class ChequeDTO {
    private String numeroCheque;
    private String codeBanque;
    private String numeroCompte;
    private String nomClient;
    private Double montant;
    private boolean certifie;
}