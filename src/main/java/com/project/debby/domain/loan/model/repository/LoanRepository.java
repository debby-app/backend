package com.project.debby.domain.loan.model.repository;

import com.project.debby.domain.loan.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT l from Loan l WHERE l.owner.userDetails.credentials.externalId = :externalId")
    List<Loan> getAllLoans(@Param("externalId") String externalId);

    @Query("SELECT l from Loan l LEFT JOIN l.states s WHERE s.status <> 'ARCHIVED' and l.owner.userDetails.credentials.externalId = :externalId")
    List<Loan> getAllActiveLoans(@Param("externalId") String externalId);
}
