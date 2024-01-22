package task312.demo.exceptions;

public class RoleAlreadyExistException extends RuntimeException {

    public RoleAlreadyExistException(String roleName) {
        super("Role with name " + roleName + " already exists.");
    }
}
