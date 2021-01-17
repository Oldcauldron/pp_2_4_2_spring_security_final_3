package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import web.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class RoleDaoImpl implements RoleDao {

//    public static boolean initRoles = false;

    @PersistenceContext
    EntityManager entityManager;

//    public void addDefaultRoles() {
////        if (!initRoles) {
////            initRoles = true;
//            String query = "select r from Role r where r.role = :role";
//            Role role = entityManager.createQuery(query, Role.class).setParameter("role", "ROLE_ADMIN").getSingleResult();
//            if (role == null) {
//                addRole("ROLE_ADMIN");
//                addRole("ROLE_USER");
//            }
////        }
//    }

    @Override
    public void addRole(String role) {
        Role roleAdd = new Role(role);
        entityManager.persist(roleAdd);
    }

    @Override
    public Role getRole(String role) {
//        addDefaultRoles();
        String query = "select r from Role r where r.role = :role";
        Role thisRole = entityManager.createQuery(query, Role.class).setParameter("role", role).getSingleResult();
        return thisRole;
    }

    @Override
    public Set<Role> getAllRoles() {
        String query = "select r from Role r";
        return new HashSet<Role>(entityManager.createQuery(query).getResultList());
    }
}
