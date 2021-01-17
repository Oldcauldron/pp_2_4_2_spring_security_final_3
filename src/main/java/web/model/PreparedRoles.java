package web.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.dao.RoleDao;
import web.service.RoleService;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PreparedRoles {

    RoleDao roleDao;
    private Set<String> allRoles;
    private Set<String> actualRoles;

    @NotEmpty(message = "Name should not be empty!")
    private String username;

    @NotEmpty(message = "Password should not be empty!")
    private String password;

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public Set<String> getAllRoles() {
        return allRoles;
    }
    public void setAllRoles(Set<Role> allRolesInDb) {
        this.allRoles = allRolesInDb.stream().map(Role::getRole).collect(Collectors.toSet());
    }

    public Set<String> getActualRoles() {
        return actualRoles;
    }
    public void setActualRoles(Set<String> actualRoles) {
        this.actualRoles = actualRoles;
    }

    public void setUserRoles(User user) {
        this.actualRoles = user.getRoles().stream().map(Role::getRole).collect(Collectors.toSet());
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isNotErrorsTest() {
        if (this.username.isEmpty() || this.password.isEmpty()) {
            return false;
        }
        return  true;
    }

    public String getMessageIfPasswordEmpty() {
        if (this.password.isEmpty()) {
            return  "Password should not be empty!";
        }
        return null;
    }

    public String getMessageIfNameEmpty() {
        if (this.username.isEmpty()) {
            return  "Login should not be empty!";
        }
        return null;
    }
}
