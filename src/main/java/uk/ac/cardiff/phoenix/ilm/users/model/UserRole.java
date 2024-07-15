package uk.ac.cardiff.phoenix.ilm.users.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Audited
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)  // Make the 'role' attribute unique
    private String role;
    @ManyToMany(fetch = FetchType.EAGER)
    Set<RolePermission> permissions = new HashSet<>();

    public UserRole(String role) {
        this.role = role;
    }


}
