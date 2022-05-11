package com.project.debby.domain.loan.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoanChangeTermsDTO {
    private Long stateID;
    private LocalDateTime newMaturityDate;
}
