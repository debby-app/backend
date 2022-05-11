package com.project.debby.domain.loan.model.repository;

import com.project.debby.domain.loan.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> getAllByOwner_UserDetails_Credentials_ExternalId(String externalId);
}
