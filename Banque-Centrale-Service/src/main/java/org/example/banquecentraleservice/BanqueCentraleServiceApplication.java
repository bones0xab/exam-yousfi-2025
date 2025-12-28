package org.example.banquecentraleservice;

import org.example.banquecentraleservice.entities.AgenceBancaire;
import org.example.banquecentraleservice.repo.AgenceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class BanqueCentraleServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BanqueCentraleServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner start(AgenceRepository repo) {
        return args -> {
            repo.save(AgenceBancaire.builder()
                    .nom("Agence Mohammedia")
                    .ville("Mohammedia")
                    .codeBanque("111") // Code fictif
                    .urlWebService("http://localhost:8083") // URL de l'Agence Service
                    .build());
        };
    }
}
