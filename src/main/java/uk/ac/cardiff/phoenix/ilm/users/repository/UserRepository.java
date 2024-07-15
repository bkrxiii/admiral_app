package uk.ac.cardiff.phoenix.ilm.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.cardiff.phoenix.ilm.users.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRoles_Role(String roleName);
    User findByEmail(String email);
}