package web.dao;

import org.springframework.stereotype.Repository;
import web.model.Role;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    EntityManager entityManager;

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
        String query = "select u from User u where username = :name";
        User user = entityManager.createQuery(query, User.class).setParameter("name", name).getSingleResult();
        return user;
    }

    @Override
    public List<User> allUsers() {
        String query = "select u from User u";
        TypedQuery<User> tq = entityManager.createQuery(query, User.class);
        return tq.getResultList();
    }
}
