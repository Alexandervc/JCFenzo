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
    private EntityManager em;
    
    public ItemDAOJPAImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public int count() {
        Query q = em.createNamedQuery("Item.count");
        return((Long) q.getSingleResult()).intValue();
    }

    @Override
    public void create(Item item) {
        em.persist(item);
    }

    @Override
    public void edit(Item item) {
        em.merge(item);
    }

    @Override
    public Item find(Long id) {
        return em.find(Item.class, id);
    }

    @Override
    public List<Item> findAll() {
        Query q = em.createNamedQuery("Item.getAll");
        return (List<Item>) q.getResultList();
    }

    @Override
    public List<Item> findByDescription(String description) {
        Query q = em.createNamedQuery("Item.findByDescription");
        q.setParameter("description", description);
        return (List<Item>) q.getResultList();
    }

    @Override
    public void remove(Item item) {
        em.remove(em.merge(item));
    }
}
