package auction.dao;

import auction.domain.Item;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Alexander
 */
public class ItemDAOJPAImpl implements ItemDAO {
    
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("auctionPU");
    private EntityManager em;
    
    public ItemDAOJPAImpl() {
        em = emf.createEntityManager();
    }

    @Override
    public int count() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        
        Query q = null;
        int i = 0;
        
        try {
            q = em.createNamedQuery("Item.count");
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
    public void create(Item item) {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        
        try {
            em.persist(item);
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
    public void edit(Item item) {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        
        try {
            em.merge(item);
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
    public Item find(Long id) {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        
        Item i = null;
        
        try {
            i = em.find(Item.class, id);
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
    public List<Item> findAll() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        
        Query q = null;
        List<Item> items = new ArrayList<Item>();
        
        try {
            q = em.createNamedQuery("Item.getAll");
            if (q.getResultList().size() > 0) { 
                items = new ArrayList<Item>(q.getResultList());  
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

        return items;
    }

    @Override
    public List<Item> findByDescription(String description) {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        
        Query q = null;
        List<Item> items = new ArrayList<Item>();
        
        try {
            q = em.createNamedQuery("Item.findByDescription");
            q.setParameter("description", description);
            if (q.getResultList().size() > 0) { 
                items = new ArrayList<Item>(q.getResultList()); 
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

        return items;
    }

    @Override
    public void remove(Item item) {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        
        try {
            em.remove(em.merge(item));
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
