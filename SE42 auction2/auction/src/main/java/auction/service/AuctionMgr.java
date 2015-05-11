package auction.service;

import auction.dao.*;
import nl.fontys.util.Money;
import auction.domain.*;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

public class AuctionMgr  {
    private EntityManagerFactory emf;
    
    public AuctionMgr() {
        emf = Persistence.createEntityManagerFactory("auctionPU");
    }

   /**
     * @param id
     * @return het item met deze id; als dit item niet bekend is wordt er null
     *         geretourneerd
     */
    public Item getItem(Long id) {
        EntityManager em = emf.createEntityManager();
        ItemDAO itemDAO = new ItemDAOJPAImpl(em);
        
        em.getTransaction().begin();
        Item item = null;
        
        try {
            item = itemDAO.find(id);
            em.getTransaction().commit();
        } catch(Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        
        return item;
    }

  
   /**
     * @param description
     * @return een lijst met items met @desciption. Eventueel lege lijst.
     */
    public List<Item> findItemByDescription(String description) {
        EntityManager em = emf.createEntityManager();
        ItemDAO itemDAO = new ItemDAOJPAImpl(em);
        
        em.getTransaction().begin();
        List<Item> items = new ArrayList<>();
        
        try {
            items = itemDAO.findByDescription(description);
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        
        return items;
    }

    /**
     * @param item
     * @param buyer
     * @param amount
     * @return het nieuwe bod ter hoogte van amount op item door buyer, tenzij
     *         amount niet hoger was dan het laatste bod, dan null
     */
    public Bid newBid(Item item, User buyer, Money amount) {
        EntityManager em = emf.createEntityManager();
        ItemDAO itemDAO = new ItemDAOJPAImpl(em);
        
        //??
        Bid bid = item.newBid(buyer, amount);
        
        if(bid == null) {
            em.getTransaction().begin();
            
            try {
                itemDAO.edit(item);
                em.getTransaction().commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                em.getTransaction().rollback();
            } finally {
                em.close();
            }
        }
        
        return bid;
    }
}