package task312.demo.services;

import task312.demo.models.Role;
import task312.demo.repositories.RoleRepository;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role createRole(Role role);
    void deleteRole(Long roleId);
    Role findById(Long roleId);
    Role findByName(String name);
    RoleRepository getRoleRepository();
}
