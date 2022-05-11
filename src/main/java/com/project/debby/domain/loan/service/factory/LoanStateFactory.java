package com.project.debby.domain.loan.service.factory;

import com.project.debby.domain.loan.model.Loan;
import com.project.debby.domain.loan.model.LoanState;
import com.project.debby.domain.user.model.User;

public interface LoanStateFactory {
    LoanState create(Loan loan, User user);
}
