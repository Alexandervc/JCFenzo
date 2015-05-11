package auction.dao;

import auction.domain.User;
import java.util.List;
import javax.persistence.*;

public class UserDAOJPAImpl implements UserDAO {

    private final EntityManager em;

    public UserDAOJPAImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public int count() {
        Query q = em.createNamedQuery("User.count");
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public void create(User user) {
        em.persist(user);
    }

    @Override
    public void edit(User user) {
        em.merge(user);
    }

    @Override
    public List<User> findAll() {
        Query q = em.createNamedQuery("User.getAll");
        return (List<User>) q.getResultList();
    }

    @Override
    public User findByEmail(String email) {
        TypedQuery<User> q = em.createNamedQuery("User.findByEmail", User.class);
        q.setParameter("email", email);   
        User user = null;
        
        try {
            user = q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }      
        
        return user;
    }

    @Override
    public void remove(User user) {
        em.remove(em.merge(user));
    }
}
