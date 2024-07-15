package uk.ac.cardiff.phoenix.ilm.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.cardiff.phoenix.ilm.users.model.UserRole;


public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    UserRole findByRole(String roleName);
}
