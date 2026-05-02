package com.root.calculadoraedenred.repository;

import com.root.calculadoraedenred.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {
}