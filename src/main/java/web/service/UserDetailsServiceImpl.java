package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.RoleDao;
import web.dao.UserDao;
import web.model.Role;
import web.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService, UserService, RoleService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    Environment env;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDetailsServiceImpl(UserDao userDao, RoleDao roleDao, Environment env) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.env = env;
    }


    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // «Пользователь» – это просто Object. В большинстве случаев он может быть
    //  приведен к классу UserDetails.
    // Для создания UserDetails используется интерфейс UserDetailsService, с единственным методом:
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDao.getUserByName(s);
    }

    @Override
    public List<User> allUsers() {
        return userDao.allUsers();
    }

    @Override
    public void addUser(User user) {
        Set<Role> defaultRoles = Collections.singleton(getRole(env.getProperty("role.default")));
        String passCrypt = bCryptPasswordEncoder.encode(user.getPassword());
        user.setRoles(defaultRoles);
        user.setPassword(passCrypt);
        userDao.addUser(user);
    }

    @Override
    public Role getRole(String role) {
        return roleDao.getRole(role);
    }
}
