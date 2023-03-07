package com.project.debby.domain.loan.dto.response;

import com.project.debby.domain.loan.model.LoanState;
import com.project.debby.domain.loan.model.LoanStatus;
import com.project.debby.domain.user.dto.response.UserDTO;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class LoanStateDTO {
    private Long id;
    private LoanStatus status;
    private UserDTO borrower;
    private LocalDateTime updatedMaturityDate;
    private LocalDateTime requestedMaturityDate;
    private BigDecimal paidPart;
    private BigDecimal paidPartOnConfirmation;
    private String file;

    public static LoanStateDTO create(LoanState state){
        return LoanStateDTO.builder()
                .id(state.getId())
                .status(state.getStatus())
                .borrower(UserDTO.create(state.getBorrower()))
                .updatedMaturityDate(state.getUpdatedMaturityDate())
                .requestedMaturityDate(state.getRequestedMaturityDate())
                .paidPart(state.getPaidPart())
                .paidPartOnConfirmation(state.getPaidPartOnConfirmation())
                .build();
    }
}
