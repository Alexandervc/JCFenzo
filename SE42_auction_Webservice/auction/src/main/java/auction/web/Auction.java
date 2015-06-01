/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auction.web;

import auction.domain.*;
import auction.service.*;
import java.util.List;
import javax.jws.WebService;
import nl.fontys.util.*;

/**
 *
 * @author Alexander
 */
@WebService
public class Auction {
    private AuctionMgr auctionMgr = new AuctionMgr();
    private SellerMgr sellerMgr = new SellerMgr();
    
    public Item getItem(Long id) {
        return auctionMgr.getItem(id);
    }
    
    public List<Item> findItemByDescription(String description) {
        return auctionMgr.findItemByDescription(description);
    }
    
    public Bid newBid(Item item, User buyer, Money amount) {
        return auctionMgr.newBid(item, buyer, amount);
    }
    
    public Item offerItem(User seller, Category cat, String description) {
        return sellerMgr.offerItem(seller, cat, description);
    }
    
    public boolean revokeItem(Item item) {
        return sellerMgr.revokeItem(item);
    }
}
