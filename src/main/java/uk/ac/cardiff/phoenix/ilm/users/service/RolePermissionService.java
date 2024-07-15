package uk.ac.cardiff.phoenix.ilm.users.service;

import uk.ac.cardiff.phoenix.ilm.users.model.RolePermission;

public interface RolePermissionService {
    RolePermission addPermission(String permission);
    RolePermission deletePermission(RolePermission rolePermission);
}