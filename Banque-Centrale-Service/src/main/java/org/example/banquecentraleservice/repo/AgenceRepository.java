package org.example.banquecentraleservice.repo;

import org.example.banquecentraleservice.entities.AgenceBancaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgenceRepository extends JpaRepository<AgenceBancaire, Long> {
    AgenceBancaire findByCodeBanque(String codeBanque);
}
