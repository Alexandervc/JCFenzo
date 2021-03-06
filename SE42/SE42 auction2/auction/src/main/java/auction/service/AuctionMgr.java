package auction.service;

import auction.dao.*;
import nl.fontys.util.Money;
import auction.domain.*;
import java.util.List;

public class AuctionMgr  {
    
    private ItemDAO itemDAO;
    
    public AuctionMgr() {
        itemDAO = new ItemDAOJPAImpl();   
    }

   /**
     * @param id
     * @return het item met deze id; als dit item niet bekend is wordt er null
     *         geretourneerd
     */
    public Item getItem(Long id) {             
        return itemDAO.find(id);
    }

  
   /**
     * @param description
     * @return een lijst met items met @desciption. Eventueel lege lijst.
     */
    public List<Item> findItemByDescription(String description) {
        return itemDAO.findByDescription(description);
    }

    /**
     * @param item
     * @param buyer
     * @param amount
     * @return het nieuwe bod ter hoogte van amount op item door buyer, tenzij
     *         amount niet hoger was dan het laatste bod, dan null
     */
    public Bid newBid(Item item, User buyer, Money amount) {
        Bid bid = item.newBid(buyer, amount);
        
        if(bid != null) {
            itemDAO.edit(item);
        }
        
        return bid;
    }
}
