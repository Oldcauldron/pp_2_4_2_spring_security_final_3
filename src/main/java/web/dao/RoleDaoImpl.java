package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import web.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Role getRole(String role) {
        String query = "select r from Role r where r.role = :role";
        Role thisRole = entityManager.createQuery(query, Role.class).setParameter("role", role).getSingleResult();
        return thisRole;
    }
}
