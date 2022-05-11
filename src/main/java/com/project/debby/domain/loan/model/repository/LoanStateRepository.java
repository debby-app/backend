package com.project.debby.domain.loan.model.repository;

import com.project.debby.domain.loan.model.LoanState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanStateRepository extends JpaRepository<LoanState, Long> {
    List<LoanState> getAllByBorrower_UserDetails_Credentials_ExternalId(String externalID);
}
