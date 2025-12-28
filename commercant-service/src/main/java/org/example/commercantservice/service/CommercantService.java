package org.example.commercantservice.service;

import org.example.commercantservice.entities.Cheque;
import org.example.commercantservice.feign.BanqueCentraleRestClient;
import org.example.commercantservice.repo.ChequeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommercantService {
    private final ChequeRepository chequeRepository;
    private final BanqueCentraleRestClient banqueCentraleRestClient;

    public CommercantService(ChequeRepository chequeRepository, BanqueCentraleRestClient banqueCentraleRestClient) {
        this.chequeRepository = chequeRepository;
        this.banqueCentraleRestClient = banqueCentraleRestClient;
    }

    public Cheque certifierCheque(Cheque cheque) {
        // 1. Sauvegarde initiale (statut non certifié par défaut)
        cheque.setCertifie(false);
        Cheque savedCheque = chequeRepository.save(cheque);

        // 2. Appel synchrone à la Banque Centrale
        // La banque centrale va orchestrer la vérification auprès de l'agence
        Cheque chequeVerifie = banqueCentraleRestClient.verifierCheque(savedCheque);

        // 3. Mise à jour du statut selon la réponse
        savedCheque.setCertifie(chequeVerifie.isCertifie());

        return chequeRepository.save(savedCheque);
    }
}