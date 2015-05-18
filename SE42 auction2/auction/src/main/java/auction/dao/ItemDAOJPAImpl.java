/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auction.dao;

import auction.domain.Item;
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
        this.em = emf.createEntityManager();
    }

    @Override
    public int count() {
        this.em = emf.createEntityManager();
        
        Query q = em.createNamedQuery("Item.count");
        return((Long) q.getSingleResult()).intValue();
    }

    @Override
    public void create(Item item) {
        this.em = emf.createEntityManager();
        em.persist(item);
    }

    @Override
    public void edit(Item item) {
        this.em = emf.createEntityManager();
        em.merge(item);
    }

    @Override
    public Item find(Long id) {
        this.em = emf.createEntityManager();
        return em.find(Item.class, id);
    }

    @Override
    public List<Item> findAll() {
        this.em = emf.createEntityManager();
        Query q = em.createNamedQuery("Item.getAll");
        return (List<Item>) q.getResultList();
    }

    @Override
    public List<Item> findByDescription(String description) {
        this.em = emf.createEntityManager();
        Query q = em.createNamedQuery("Item.findByDescription");
        q.setParameter("description", description);
        return (List<Item>) q.getResultList();
    }

    @Override
    public void remove(Item item) {
        this.em = emf.createEntityManager();
        em.remove(em.merge(item));
    }
}
