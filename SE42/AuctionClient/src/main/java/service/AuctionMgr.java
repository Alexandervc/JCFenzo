package service;

import auction.web.*;
import java.util.List;

public class AuctionMgr  {
    private AuctionService service;
    private Auction port;
    
    public AuctionMgr() {
        service = new AuctionService();
        port = service.getAuctionPort();
    }    
    
   /**
     * @param id
     * @return het item met deze id; als dit item niet bekend is wordt er null
     *         geretourneerd
     */
    public Item getItem(Long id) {
        try {
            Item result = port.getItem(id);
            System.out.println("Result = "+result);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
  
   /**
     * @param description
     * @return een lijst met items met @desciption. Eventueel lege lijst.
     */
    public List<Item> findItemByDescription(String description) {
        try {
            List<Item> result = port.findItemByDescription(description);
            System.out.println("Result = "+result);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * @param item
     * @param buyer
     * @param amount
     * @return het nieuwe bod ter hoogte van amount op item door buyer, tenzij
     *         amount niet hoger was dan het laatste bod, dan null
     */
    public Bid newBid(Item item, User buyer, Money amount) {
        try {
            Bid result = port.newBid(item, buyer, amount);
            System.out.println("Result = "+result);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
