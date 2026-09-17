package com.jkweg.smartwallet.investment;

import org.springframework.data.jpa.repository.JpaRepository;

interface InvestmentRepository extends JpaRepository<InvestmentTransaction, Long> {
}
