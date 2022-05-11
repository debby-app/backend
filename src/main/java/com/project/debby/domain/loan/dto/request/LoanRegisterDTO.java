package com.project.debby.domain.loan.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class LoanRegisterDTO {
    private String name;
    private BigDecimal price;
    private LocalDateTime creationDate;
    private LocalDateTime maturityDate;
    private boolean isGroup;
    private List<String> users;
}
