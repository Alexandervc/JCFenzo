package auction.service;

import auction.dao.ItemDAO;
import auction.dao.ItemDAOJPAImpl;
import auction.domain.Category;
import auction.domain.Furniture;
import auction.domain.Item;
import auction.domain.Painting;
import auction.domain.User;

public class SellerMgr {

    private ItemDAO itemDAO;
    
    public SellerMgr() {
        itemDAO = new ItemDAOJPAImpl();   
    }
    
    /**
     * @param seller
     * @param cat
     * @param description
     * @return het item aangeboden door seller, behorende tot de categorie cat
     *         en met de beschrijving description
     */
    public Item offerItem(User seller, Category cat, String description) {
        Item item = new Item(seller, cat, description);
        itemDAO.create(item);
        return item;
    }
    
    /**
     * @param seller
     * @param cat
     * @param description
     * @param title
     * @param painter
     * @return het schilderij aangeboden door seller, behorende tot de categorie cat
     *         en met de beschrijving description en met titel title en schilder painter
     */
    public Item offerPainting(User seller, Category cat, String description, String title, String painter) {
        Painting painting = new Painting(title, painter, seller, cat, description);
        itemDAO.create(painting);
        return painting;
    }
    
    /**
     * @param seller
     * @param cat
     * @param description
     * @param material
     * @return het meubelstuk aangeboden door seller, behorende tot de categorie cat
     *         en met de beschrijving description en van het materiaal material
     */
    public Item offerFurniture(User seller, Category cat, String description, String material) {
        Furniture furniture = new Furniture(material, seller, cat, description);
        itemDAO.create(furniture);
        return furniture;
    }
    
     /**
     * @param item
     * @return true als er nog niet geboden is op het item. Het item wordt verwijderd.
     *         false als er al geboden was op het item.
     */
    public boolean revokeItem(Item item) {
        if (item.getHighestBid() == null) {
            itemDAO.remove(item);
            return true;
        }
        
        return false;
    }
}
