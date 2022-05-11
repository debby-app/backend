package com.project.debby.domain.auth.model.repository;

import com.project.debby.domain.auth.model.ConfirmationMail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationMailRepository extends JpaRepository<ConfirmationMail, Long> {
}
