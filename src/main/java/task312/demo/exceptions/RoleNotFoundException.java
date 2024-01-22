package task312.demo.exceptions;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(Long roleId) {
        super("Role with ID: " + roleId + " not found.");
    }

    public RoleNotFoundException(String roleName) {
        super("Role with name: " + roleName + " not found.");
    }
}