package org.example.banquecentraleservice.web;

import org.example.banquecentraleservice.dto.ChequeDTO;
import org.example.banquecentraleservice.entities.AgenceBancaire;
import org.example.banquecentraleservice.feign.AgenceRestClient;
import org.example.banquecentraleservice.repo.AgenceRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/central")
public class BanqueCentraleController {

    private final AgenceRepository agenceRepository;
    private final AgenceRestClient agenceRestClient;
    private final KafkaTemplate<String, ChequeDTO> kafkaTemplate;

    public BanqueCentraleController(AgenceRepository agenceRepository,
                                    AgenceRestClient agenceRestClient,
                                    KafkaTemplate<String, ChequeDTO> kafkaTemplate) {
        this.agenceRepository = agenceRepository;
        this.agenceRestClient = agenceRestClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/cheques/verifier")
    public ChequeDTO verifier(@RequestBody ChequeDTO chequeDTO) {
        // 1. Trouver l'agence correspondant au code banque du chèque
        AgenceBancaire agence = agenceRepository.findByCodeBanque(chequeDTO.getCodeBanque());

        if (agence == null) {
            throw new RuntimeException("Agence introuvable pour le code: " + chequeDTO.getCodeBanque());
        }

        // 2. Appel Synchrone (Feign) avec URL Dynamique
        // On construit l'URI à partir de l'URL stockée en BDD pour cette agence
        URI agenceUri = URI.create(agence.getUrlWebService());
        ChequeDTO reponseAgence = agenceRestClient.certifierCheque(agenceUri, chequeDTO);

        // 3. Envoi Asynchrone (Kafka) pour Audit/Analytics
        // On publie le résultat de la transaction dans le topic "audit-topic"
        kafkaTemplate.send("audit-topic", reponseAgence);

        return reponseAgence;
    }
}
