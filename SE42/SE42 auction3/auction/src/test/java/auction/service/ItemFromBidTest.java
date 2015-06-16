package auction.service;

import auction.domain.Bid;
import auction.domain.Category;
import auction.domain.Item;
import auction.domain.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import nl.fontys.util.Money;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import util.DatabaseCleaner;

/**
 *
 * @author Melanie
 */
public class ItemFromBidTest {
    
    private AuctionMgr auctionMgr;
    private JPARegistrationMgr registrationMgr;
    private SellerMgr sellerMgr;
    
    private EntityManagerFactory emf;
    
    public ItemFromBidTest() {
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
    public void test() {        
        String omsch1 = "omsch_flup";
        Category cat = new Category("cat6");
        
        String email = "flup10@nl";
        User user5 = registrationMgr.registerUser(email);
        String email2 = "fluppy@nl";
        User user6 = registrationMgr.registerUser(email2);        
        
        Item item2 = sellerMgr.offerItem(user5, cat, omsch1);
        Bid bid = auctionMgr.newBid(item2, user6, new Money(14, "eur"));
        
        assertEquals(item2.getHighestBid(), bid);
        assertEquals(bid.getItem(), item2);
        
        //From database
        Item item3 = auctionMgr.getItem(item2.getId());
        assertEquals(bid.getId(), item3.getHighestBid().getId());
        
        Long id = bid.getItem().getId();
        Item item4 = auctionMgr.getItem(id);
        assertNotNull(item4);        
        
        //Item null
        Item item5 = null;
        Bid bid2 = auctionMgr.newBid(item5, user6, new Money(16, "eur"));
        assertNull(bid2);
    }
}
