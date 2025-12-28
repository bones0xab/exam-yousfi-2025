package org.example.commercantservice.feign;

import org.example.commercantservice.entities.Cheque;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "banque-centrale-service")
public interface BanqueCentraleRestClient {
    @PostMapping("/api/central/cheques/verifier")
    Cheque verifierCheque(@RequestBody Cheque cheque);
}