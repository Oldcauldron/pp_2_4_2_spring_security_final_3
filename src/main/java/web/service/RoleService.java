package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.dao.RoleDao;
import web.model.Role;
import web.model.User;

import java.util.List;
import java.util.Set;


public interface RoleService {
    public Role getRole(String role);
    Set<Role> getAllRoles();
}
