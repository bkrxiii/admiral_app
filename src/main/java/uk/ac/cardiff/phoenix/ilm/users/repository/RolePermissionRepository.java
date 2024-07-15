package uk.ac.cardiff.phoenix.ilm.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.cardiff.phoenix.ilm.users.model.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    RolePermission findByPermission(String permission);
}
