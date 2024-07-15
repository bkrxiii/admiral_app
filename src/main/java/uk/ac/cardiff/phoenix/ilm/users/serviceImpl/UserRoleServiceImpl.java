package uk.ac.cardiff.phoenix.ilm.users.serviceImpl;

import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.users.model.RolePermission;
import uk.ac.cardiff.phoenix.ilm.users.model.UserRole;
import uk.ac.cardiff.phoenix.ilm.users.repository.UserRoleRepository;
import uk.ac.cardiff.phoenix.ilm.users.service.UserRoleService;

import java.util.Set;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository userRoleRepository;
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserRole addUserRole(String roleName) {
        // Check if a UserRole with the given role already exists
        UserRole existingUserRole = userRoleRepository.findByRole(roleName);

        if (existingUserRole != null) {
            // If it exists, return the existing UserRole
            return existingUserRole;
        } else {
            // If it doesn't exist, create a new UserRole and save it
            UserRole newUserRole = new UserRole(roleName);
            return userRoleRepository.save(newUserRole);
        }
    }

    @Override
    public UserRole deleteUserRole(UserRole userRole) {
        try {
            userRoleRepository.delete(userRole);
            return userRole;
        } catch (DataIntegrityViolationException e) {
            // If the UserRole is still referenced by a User, throw an exception
            throw new DataIntegrityViolationException("Cannot delete UserRole as it is still referenced by a User");
        }
    }

    @Override
    public void addPermissionToRole(@NotNull UserRole userRole, Set<RolePermission> rolePermissions) {
        Set<RolePermission> existingPermissions = userRole.getPermissions();
        existingPermissions.addAll(rolePermissions);
        userRole.setPermissions(existingPermissions);
        userRoleRepository.save(userRole);
    }

    @Override
    public Set<RolePermission> getPermissionsForRole(@NotNull UserRole userRole) {
        return userRole.getPermissions();
    }
}
