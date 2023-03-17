package com.project.debby.domain.user.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "_group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @OneToOne(fetch = FetchType.EAGER)
    private User owner;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> users;
}
