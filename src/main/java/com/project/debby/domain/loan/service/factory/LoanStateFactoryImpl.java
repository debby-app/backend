package com.project.debby.domain.loan.service.factory;

import com.project.debby.domain.loan.model.Loan;
import com.project.debby.domain.loan.model.LoanState;
import com.project.debby.domain.loan.model.LoanStatus;
import com.project.debby.domain.user.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LoanStateFactoryImpl implements LoanStateFactory {
    @Override
    public LoanState create(Loan loan, User user) {
        return LoanState.builder()
                .loan(loan)
                .borrower(user)
                .status(LoanStatus.PENDING_ACCEPTANCE)
                .paidPart(BigDecimal.ZERO)
                .paidPartOnConfirmation(BigDecimal.ZERO)
                .requestedMaturityDate(loan.getMaturityDate())
                .updatedMaturityDate(loan.getMaturityDate())
                .build();
    }
}
