package web.service;

import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.RoleDao;
import web.dao.UserDao;
import web.model.Role;
import web.model.User;

import java.util.*;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService, UserService, RoleService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    Environment env;

    public UserDetailsServiceImpl(UserDao userDao, RoleDao roleDao,
                                  Environment env) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.env = env;
//        if (userDao.getUserByName("admin") == null) {
//            Role roleAdmin = new Role("ROLE_ADMIN");
//            Role roleUser = new Role("ROLE_USER");
//            Set<Role> roles = new HashSet<>(Arrays.asList(roleAdmin, roleUser));
//            String passCrypt = bCryptPasswordEncoder.encode("admin");
//            User user = new User(1L, "admin", passCrypt, roles);
//            userDao.addUser(user);
//        }
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDao.getUserByName(s);
    }

    @Override
    public User showById(long id) {
        return userDao.showById(id);
    }

    @Override
    public boolean isExistingUser(User user) {
        return userDao.isExistingUser(user);
    }

    @Override
    public boolean isExistingUserByName(String name) {
        return userDao.isExistingUserByName(name);
    }

    @Override
    public List<User> allUsers() {
        return userDao.allUsers();
    }

    @Override
    public void addUser(User user) {
        Set<Role> defaultRoles = Collections.singleton(getRole(env.getProperty("role.default")));
        user.setRoles(defaultRoles);
        userDao.addUser(user);
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    public void deleteUserById(long id) {
        userDao.deleteUserById(id);
    }

    @Override
    public Role getRole(String role) {
        return roleDao.getRole(role);
    }

    @Override
    public Set<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }
}
