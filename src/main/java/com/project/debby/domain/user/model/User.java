package com.project.debby.domain.user.model;

import com.project.debby.domain.auth.model.UserDetails;
import com.project.debby.domain.integrations.minio.model.entity.File;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    @OneToOne(cascade = CascadeType.ALL)
    private UserDetails userDetails;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserSettings settings;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private File avatar;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
