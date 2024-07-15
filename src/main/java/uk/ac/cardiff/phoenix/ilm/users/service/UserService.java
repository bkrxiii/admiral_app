package uk.ac.cardiff.phoenix.ilm.users.service;


import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.users.model.User;
import uk.ac.cardiff.phoenix.ilm.users.model.RolePermission;
import uk.ac.cardiff.phoenix.ilm.users.model.UserRole;

import java.util.List;
import java.util.Set;

@Service
public interface UserService {
    User getUserById(Long userID);
    User addUser(User user);
    User updateUser(User user);
    void deleteUser(Long userID);
    List<User> getUsersByRole(String role);

    //get all permissions for all roles which a user has
    Set<RolePermission> getUserPermissions(User user);

    boolean authenticate(String Email, String password);
    void changePassword(User user, String newPassword);

    void addRole(User user, UserRole role);
    void removeRole(User user, UserRole role);

}
