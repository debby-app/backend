package com.project.debby.domain.loan.model.repository;

import com.project.debby.domain.loan.model.LoanState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanStateRepository extends JpaRepository<LoanState, Long> {

    @Query("SELECT s FROM LoanState s WHERE s.borrower.userDetails.credentials.externalId = :externalId")
    List<LoanState> getAllDebts(@Param("externalId") String externalId);

    @Query("SELECT s FROM LoanState s WHERE s.borrower.userDetails.credentials.externalId = :externalId AND s.status <> 'ARCHIVED'")
    List<LoanState> getAllActiveDebts(@Param("externalId") String externalId);
}
