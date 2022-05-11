package com.project.debby.domain.loan.service.factory;

import com.project.debby.domain.loan.dto.request.LoanRegisterDTO;
import com.project.debby.domain.loan.model.Loan;
import com.project.debby.domain.user.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LoanFactoryImpl implements LoanFactory {
    @Override
    public Loan create(LoanRegisterDTO registerDTO, User owner) {
        return Loan.builder()
                .owner(owner)
                .creationDate(registerDTO.getCreationDate())
                .maturityDate(registerDTO.getMaturityDate())
                .isGroup(registerDTO.isGroup())
                .name(registerDTO.getName())
                .price(registerDTO.getPrice())
                .states(new ArrayList<>())
                .build();
    }
}
