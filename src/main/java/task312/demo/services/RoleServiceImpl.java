package task312.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task312.demo.exceptions.RoleAlreadyExistException;
import task312.demo.exceptions.RoleNotFoundException;
import task312.demo.models.Role;
import task312.demo.models.User;
import task312.demo.repositories.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final UserServiceImpl userService;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, UserServiceImpl userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public Role createRole(Role role) {
        Optional<Role> checkIfRoleExists = roleRepository.findByName(role.getName());
        if (checkIfRoleExists.isPresent()) {
            throw new RoleAlreadyExistException(role.getName());
        }
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        roleRepository.deleteById(roleId);
    }

    @Override
    public Role findById(Long roleId) {
        Optional<Role> foundRole = roleRepository.findById(roleId);

        if (foundRole.isPresent()) {
            return foundRole.get();
        } else {
            throw new RoleNotFoundException(roleId);
        }
    }

    @Override
    public Role findByName(String name) {
        Optional<Role> foundRole = roleRepository.findByName(name);

        if (foundRole.isPresent()) {
            return foundRole.get();
        } else {
            throw new RoleNotFoundException(name);
        }
    }

    @Override
    @Transactional
    public User removeRoleFromUser(Long userId, Long roleId) {
        User user = userService.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);

        if (role.isPresent()) {
            Role roleToRemove = role.get();
            user.getRoles().remove(roleToRemove);
            return user;
        } else {
            throw new RoleNotFoundException(roleId);
        }

    }

    @Override
    @Transactional
    public User assignRoleToUser(Long userId, Long roleId) {
        User user = userService.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);

        if (role.isPresent()) {
            Role roleToAdd = role.get();
            user.getRoles().add(roleToAdd);
            return user;
        } else {
            throw new RoleNotFoundException(roleId);
        }
    }

    @Override
    @Transactional
    public void removeAllUsersFromRole(Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);

        if (role.isPresent()) {
            Role roleToAdd = role.get();
            roleToAdd.getUsers().clear();
        }
    }

    @Override
    public void removeAllRolesFromUser(Long userId) {
        User user = userService.findById(userId);

        user.getRoles().clear();
    }
}
