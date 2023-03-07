package com.project.debby.domain.loan.dto.response;

import com.project.debby.domain.integrations.minio.service.MinioService;
import com.project.debby.domain.loan.model.Loan;
import com.project.debby.domain.user.dto.response.UserDTO;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class LoanDTO {
    private Long id;
    private UserDTO owner;
    private String name;
    private BigDecimal price;
    private LocalDateTime creationDate;
    private LocalDateTime maturityDate;
    private boolean isGroup;
    private List<LoanStateDTO> states;

    public static LoanDTO create(Loan loan){
        return LoanDTO.builder()
                .id(loan.getId())
                .owner(UserDTO.create(loan.getOwner()))
                .name(loan.getName())
                .price(loan.getPrice())
                .creationDate(loan.getCreationDate())
                .maturityDate(loan.getMaturityDate())
                .isGroup(loan.isGroup())
                .states(loan.getStates().stream().map(LoanStateDTO::create).collect(Collectors.toList()))
                .build();
    }
}
