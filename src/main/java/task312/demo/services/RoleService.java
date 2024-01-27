package task312.demo.services;

import task312.demo.models.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role createRole(Role role);
    void deleteRole(Long roleId);
    Role findById(Long roleId);
    Role findByName(String name);
    void assignRoleToUser(Long userId, Long roleId);
    void removeAllUsersFromRole(Long roleId);
    void removeAllRolesFromUser(Long userId);
}
