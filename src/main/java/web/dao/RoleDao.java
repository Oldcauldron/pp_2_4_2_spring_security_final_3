package web.dao;

import web.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleDao {
    Role getRole(String role);
    void addRole(String role);
    Set<Role> getAllRoles();
}
