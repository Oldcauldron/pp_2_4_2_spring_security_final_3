package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import web.model.Role;
import web.model.User;
import web.service.SecurityService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    EntityManager entityManager;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

//    Role roleAdmin = new Role(1L, "ROLE_ADMIN");
//    Role roleUser = new Role(2L, "ROLE_USER");
//    Set<Role> rolesAdmin = new HashSet<>(Arrays.asList(roleAdmin, roleUser));
//    Set<Role> rolesUser = new HashSet<>(Collections.singletonList(roleUser));
//    User userAdmin = new User(1L, "admin", "admin", rolesAdmin);
//    User userUser = new User(2L, "user", "user", rolesUser);
//    Map<String, User> userMap = Map.of("admin", userAdmin, "user", userUser);
//    @Override
//    public User getUserByName(String name) {
//        if (!userMap.containsKey(name)) {
//            return null;
//        }
//
//        return userMap.get(name);
//    }

    @Override
    public User getUserByName(String name) {
        String query = "select u from User u where u.username = :name";
        return entityManager.createQuery(query, User.class)
                                 .setParameter("name", name)
                                 .getSingleResult();
    }

    @Override
    public User showById(long id) {
        String query = "select u from User u where u.id = :id";
        return entityManager.createQuery(query, User.class).setParameter("id", id).getSingleResult();
    }

    @Override
    public boolean isExistingUser(User user) {
        return isExistingUserByName(user.getUsername());
    }

    @Override
    public boolean isExistingUserByName(String name) {
        try {
            getUserByName(name);
            return true;
        } catch (javax.persistence.NoResultException e) {
            return false;
        }
    }

    @Override
    public List<User> allUsers() {
        String query = "select u from User u";
        TypedQuery<User> tq = entityManager.createQuery(query, User.class);
        return tq.getResultList();
    }

    @Override
    public void addUser(User user) {
        String passCrypt = cryptPass(user.getPassword());
        user.setPassword(passCrypt);
        entityManager.persist(user);
    }

    @Override
    public void updateUser(User user) {
        String passCrypt = cryptPass(user.getPassword());
        user.setPassword(passCrypt);
        entityManager.merge(user);
    }

    @Override
    public void deleteUserById(long id) {
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public String cryptPass(String pass) {
        return bCryptPasswordEncoder.encode(pass);
    }
}
