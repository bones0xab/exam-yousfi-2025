package org.example.commercantservice.repo;

import org.example.commercantservice.entities.Cheque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChequeRepository extends JpaRepository<Cheque, Long> {
}
