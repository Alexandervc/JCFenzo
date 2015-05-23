package auction.service;

import javax.persistence.*;
import util.DatabaseCleaner;
import auction.domain.Category;
import auction.domain.Item;
import auction.domain.User;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ItemsFromSellerTest {
    
    private AuctionMgr auctionMgr;
    private JPARegistrationMgr registrationMgr;
    private SellerMgr sellerMgr;
    
    private EntityManagerFactory emf;

    public ItemsFromSellerTest() {
        emf = Persistence.createEntityManagerFactory("auctionPU");
    }

    @Before
    public void setUp() throws Exception {
        EntityManager em = emf.createEntityManager();
        DatabaseCleaner dbCleaner = new DatabaseCleaner(em);
        dbCleaner.clean();
        
        registrationMgr = new JPARegistrationMgr();
        auctionMgr = new AuctionMgr();
        sellerMgr = new SellerMgr();        
    }

    @Test
    public void numberOfOfferedItems() {
        String email = "ifu1@nl";
        String omsch1 = "omsch_ifu1";
        String omsch2 = "omsch_ifu2";

        User user1 = registrationMgr.registerUser(email);
        assertEquals(0, user1.numberOfOfferedItems());

        Category cat = new Category("cat2");
        Item item1 = sellerMgr.offerItem(user1, cat, omsch1);              
        
        /**
         * QUESTION:
         *      Explain the result in terms of entity manager and persistance context.
         * EXPLANATION:
         *      Item wordt aan de lijst van offeredItems toegevoegd.
         */
        
        // test number of items belonging to user1
        assertEquals(1, user1.numberOfOfferedItems());                  
         
        assertEquals(1, item1.getSeller().numberOfOfferedItems());

        User user2 = registrationMgr.getUser(email);
        assertEquals(1, user2.numberOfOfferedItems());
        Item item2 = sellerMgr.offerItem(user2, cat, omsch2);
        assertEquals(2, user2.numberOfOfferedItems());

        User user3 = registrationMgr.getUser(email);
        assertEquals(2, user3.numberOfOfferedItems());

        User userWithItem = item2.getSeller();
        
        /**
         * QUESTION:
         *      Explain the result in terms of entity manager and persistance context.
         * EXPLANATION:
         *      Er is ondertussen maar 1 item toegevoegd.
         */ 
        assertEquals(2, userWithItem.numberOfOfferedItems());        
               
        /**
         * QUESTION:
         *      Explain the result in terms of entity manager and persistance context.
         * EXPLANATION:
         *      UserWithItem is gelijk aan user2, omdat user3 een ander object is,
         *      omdat deze later uit de database opgehaald is. Hierdoor is de verwijzing
         *      naar het object niet hetzelfde, maar de waardes van dit object wel.
         */
        assertNotSame(user3, userWithItem);
        assertSame(user2, userWithItem);
        assertEquals(user3.getId(), userWithItem.getId());
    }

    @Test
    public void getItemsFromSeller() {
        String email = "ifu1@nl";
        String omsch1 = "omsch_ifu1";
        String omsch2 = "omsch_ifu2";

        Category cat = new Category("cat2");

        User user10 = registrationMgr.registerUser(email);
        Item item10 = sellerMgr.offerItem(user10, cat, omsch1);
        Iterator<Item> it = user10.getOfferedItems();
        // testing number of items of java object
        assertTrue(it.hasNext());
        
        // now testing number of items for same user fetched from db.
        User user11 = registrationMgr.getUser(email);
        Iterator<Item> it11 = user11.getOfferedItems();
        assertTrue(it11.hasNext());
        it11.next();
        assertFalse(it11.hasNext());

        /**
         * QUESTION:
         *      Explain difference in above two tests for the iterator of 'same' user
         * EXPLANATION:
         *      Er is 1 item aan offeredItems toegevoegd. Bij de eerste test wordt
         *      er een item gevonden. Bij de tweede test haalt hij de user op uit
         *      de database. Ook hier wordt een item gevonden. Dit betekent dat
         *      de offeredItems dus ook worden weggeschreven in de database.
         */        
        
        User user20 = registrationMgr.getUser(email);
        Item item20 = sellerMgr.offerItem(user20, cat, omsch2);
        Iterator<Item> it20 = user20.getOfferedItems();
        assertTrue(it20.hasNext());
        it20.next();
        assertTrue(it20.hasNext());

        User user30 = item20.getSeller();
        Iterator<Item> it30 = user30.getOfferedItems();
        assertTrue(it30.hasNext());
        it30.next();
        assertTrue(it30.hasNext());
    } 
}
