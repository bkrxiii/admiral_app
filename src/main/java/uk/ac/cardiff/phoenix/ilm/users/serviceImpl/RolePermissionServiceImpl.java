package uk.ac.cardiff.phoenix.ilm.users.serviceImpl;

import org.springframework.stereotype.Service;
import uk.ac.cardiff.phoenix.ilm.users.model.RolePermission;
import uk.ac.cardiff.phoenix.ilm.users.repository.RolePermissionRepository;
import uk.ac.cardiff.phoenix.ilm.users.service.RolePermissionService;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {
    private final RolePermissionRepository rolePermissionRepository;
    public RolePermissionServiceImpl(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    /**
     * Add a permission to the system.
     * @param permission the permission to add
     * @return the permission that was added, or if the permission exists return the existing permission
     */
    @Override
    public RolePermission addPermission(String permission) {
        if (rolePermissionRepository.findByPermission(permission) != null) {
            return rolePermissionRepository.findByPermission(permission);
        }
        RolePermission rolePermission = new RolePermission(permission);
        return rolePermissionRepository.save(rolePermission);
    }

    @Override
    public RolePermission deletePermission(RolePermission rolePermission) {
        return null;
    }
}
