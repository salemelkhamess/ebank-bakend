package org.example.enbankspring.repositories;

import org.example.enbankspring.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccontRepository extends JpaRepository<BankAccount, String> {
}
