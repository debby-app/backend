package com.project.debby.domain.loan.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanPaidPartConfirmationDTO {
    private Long stateID;
    private BigDecimal paidPart;
}
