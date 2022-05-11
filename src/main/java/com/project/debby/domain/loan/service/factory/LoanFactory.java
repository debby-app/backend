package com.project.debby.domain.loan.service.factory;

import com.project.debby.domain.loan.dto.request.LoanRegisterDTO;
import com.project.debby.domain.loan.model.Loan;
import com.project.debby.domain.user.model.User;

public interface LoanFactory {
    Loan create(LoanRegisterDTO registerDTO, User owner);
}
