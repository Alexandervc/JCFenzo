package auction.dao;

import auction.domain.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

public class UserDAOJPAImpl implements UserDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("auctionPU");
    private EntityManager em;

    public UserDAOJPAImpl() {
        em = emf.createEntityManager();
    }

    @Override
    public int count() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        
        Query q = null;
        int i = 0;
        
        try {
            q = em.createNamedQuery("User.count");
            i = (int)q.getSingleResult();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        } 

        return i;
    }

    @Override
    public void create(User user) {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        
        try {
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }        
    }

    @Override
    public void edit(User user) {
        em = emf.createEntityManager();        
        em.getTransaction().begin();
        
        try {
            em.merge(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }        
    }

    @Override
    public List<User> findAll() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        
        Query q = null;
        List<User> users = new ArrayList<User>();
        
        try {
            q = em.createNamedQuery("User.getAll");
            if (q.getResultList().size() > 0) { 
                users = new ArrayList<User>(q.getResultList()); 
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
        
        return users;
    }

    @Override
    public User findByEmail(String email) {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        
        TypedQuery<User> q = null;
        User user = null;
        
        try {
            q = em.createNamedQuery("User.findByEmail", User.class);
            q.setParameter("email", email);
            user = q.getSingleResult();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }    
        
        return user;
    }

    @Override
    public void remove(User user) {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        
        try {
            em.remove(em.merge(user));
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }         
    }
}
