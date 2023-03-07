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
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    private User owner;
    private String name;
    private BigDecimal price;
    private LocalDateTime creationDate;
    private LocalDateTime maturityDate;
    private boolean isGroup;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<LoanState> states;
}
