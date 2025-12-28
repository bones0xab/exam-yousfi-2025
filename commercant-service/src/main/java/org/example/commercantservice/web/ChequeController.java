package org.example.commercantservice.web;

import org.example.commercantservice.entities.Cheque;
import org.example.commercantservice.service.CommercantService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cheques")
public class ChequeController {
    private final CommercantService commercantService;

    public ChequeController(CommercantService commercantService) {
        this.commercantService = commercantService;
    }

    @PostMapping("/certifier")
    public Cheque certifier(@RequestBody Cheque cheque) {
        return commercantService.certifierCheque(cheque);
    }
}