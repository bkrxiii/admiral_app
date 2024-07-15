package uk.ac.cardiff.phoenix.ilm.users.service;

import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.users.model.RolePermission;
import uk.ac.cardiff.phoenix.ilm.users.model.UserRole;

import java.util.Set;
@Service
public interface UserRoleService {
    UserRole addUserRole(String role);
    UserRole deleteUserRole(UserRole userRole);
    void addPermissionToRole(UserRole userRole, Set<RolePermission> rolePermissions);
    Set<RolePermission> getPermissionsForRole(UserRole userRole);
}
