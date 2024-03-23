package io.github.reconsolidated.tempowaiter.authentication.appUser;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(generator = "app_users")
    @SequenceGenerator(name = "app_users", allocationSize = 1)
    private Long id;
    private String password;
    @Column(unique = true)
    private String email = "";
    private String phoneNumber = "";
    private Long companyId = null;
    @Column(columnDefinition = "boolean default false")
    private boolean enabled = true;
    @Enumerated(EnumType.STRING)
    private AppUserRole role = AppUserRole.USER;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AppUser appUser = (AppUser) o;
        return id != null && Objects.equals(id, appUser.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
