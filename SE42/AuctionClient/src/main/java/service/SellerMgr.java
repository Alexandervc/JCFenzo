package service;

import auction.web.*;

public class SellerMgr {
    private AuctionService service;
    private Auction port;
    
    public SellerMgr() {
        service = new AuctionService();
        port = service.getAuctionPort();
    }
    
    /**
     * @param seller
     * @param cat
     * @param description
     * @return het item aangeboden door seller, behorende tot de categorie cat
     *         en met de beschrijving description
     */
    public Item offerItem(User seller, Category cat, String description) {
        try { 
            Item result = port.offerItem(seller, cat, description);
            System.out.println("Result = "+result);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
     /**
     * @param item
     * @return true als er nog niet geboden is op het item. Het item wordt verwijderd.
     *         false als er al geboden was op het item.
     */
    public boolean revokeItem(Item item) {
        try {
            boolean result = port.revokeItem(item);
            System.out.println("Result = "+result);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }
}
