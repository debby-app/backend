package com.project.debby.domain.loan.model;

import com.project.debby.domain.integrations.minio.model.entity.File;
import com.project.debby.domain.user.model.User;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class LoanState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @OneToOne(fetch = FetchType.EAGER)
    private Loan loan;

    @OneToOne(fetch = FetchType.EAGER)
    private User borrower;

    private LocalDateTime updatedMaturityDate;
    private LocalDateTime requestedMaturityDate;
    private BigDecimal paidPart;
    @OneToOne(cascade = CascadeType.ALL)
    private File file;
    private BigDecimal paidPartOnConfirmation;

    @PostLoad
    public void onLoad(){
        if(getStatus() == LoanStatus.ACTIVE
                && getRequestedMaturityDate().isBefore(LocalDateTime.now())){
            setStatus(LoanStatus.OVERDUE);
        }
    }
}
