package auction.service;

import java.util.*;
import auction.domain.User;
import auction.dao.UserDAO;
import auction.dao.UserDAOJPAImpl;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.EntityType;

public class JPARegistrationMgr {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("auctionPU");
    private EntityManager em;
    private UserDAO userDAO;
    
    private static final Class<?>[] ENTITY_TYPES = {
        User.class
    };

    public JPARegistrationMgr() {      
    }
    
    public void cleanDatabase() {
        em = emf.createEntityManager();
        em.getTransaction().begin();

        for (Class<?> entityType : ENTITY_TYPES) {
            EntityType et = em.getMetamodel().entity(entityType);
            if (et != null) {
                em.createQuery("delete from " + et.getName()).executeUpdate();
            }
        }
        
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Registreert een gebruiker met het als parameter gegeven e-mailadres, mits
     * zo'n gebruiker nog niet bestaat.
     * @param email
     * @return Een Userobject dat geïdentificeerd wordt door het gegeven
     * e-mailadres (nieuw aangemaakt of reeds bestaand). Als het e-mailadres
     * onjuist is ( het bevat geen '@'-teken) wordt null teruggegeven.
     */
    public User registerUser(String email) {
        em = emf.createEntityManager();
        userDAO = new UserDAOJPAImpl(em);
        
        if (!email.contains("@")) {
            return null;
        }
        
        User user = null;        
        em.getTransaction().begin();
        
        try {
            user = userDAO.findByEmail(email);  
            if (user != null) {
                return user;
            }
            user = new User(email);
            userDAO.create(user);            
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return user;
    }

    /**
     *
     * @param email een e-mailadres
     * @return Het Userobject dat geïdentificeerd wordt door het gegeven
     * e-mailadres of null als zo'n User niet bestaat.
     */
    public User getUser(String email) {
        em = emf.createEntityManager();
        userDAO = new UserDAOJPAImpl(em);
        
        User user = null;
        em.getTransaction().begin();
        
        try {
            user = userDAO.findByEmail(email);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return user;
    }

    /**
     * @return Een iterator over alle geregistreerde gebruikers
     */
    public List<User> getUsers() {
        em = emf.createEntityManager();
        userDAO = new UserDAOJPAImpl(em);
        
        List<User> users = null;
        em.getTransaction().begin();
        
        try {
            users = userDAO.findAll();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return users;
    }
}
