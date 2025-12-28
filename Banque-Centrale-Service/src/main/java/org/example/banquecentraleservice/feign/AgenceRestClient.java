package org.example.banquecentraleservice.feign;

import org.example.banquecentraleservice.dto.ChequeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;

// L'URL est surchargée dynamiquement par le paramètre URI, le "name" est juste pour Eureka/LoadBalancer
@FeignClient(name = "agence-bancaire-service")
public interface AgenceRestClient {

    @PostMapping("/api/agence/operations/certifier")
    ChequeDTO certifierCheque(URI baseUrl, @RequestBody ChequeDTO chequeDTO);
}