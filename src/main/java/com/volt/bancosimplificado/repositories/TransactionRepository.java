package com.volt.bancosimplificado.repositories;

import com.volt.bancosimplificado.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
