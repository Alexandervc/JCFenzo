package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import auction.web.*;
import service.*;

public class SellerMgrTest {

    private AuctionMgr auctionMgr;
    private JPARegistrationMgr registrationMgr;
    private SellerMgr sellerMgr;
    
    public SellerMgrTest() {
        registrationMgr = new JPARegistrationMgr();
        auctionMgr = new AuctionMgr();
        sellerMgr = new SellerMgr();
    }

    @Before
    public void setUp() throws Exception {
        registrationMgr.cleanDB();
    }

    /**
     * Test of offerItem method, of class SellerMgr.
     */
    @Test
    public void testOfferItem() {
        String omsch = "omsch";

        User user1 = registrationMgr.registerUser("xx@nl");
        Category cat = new Category();
        cat.setDescription("cat1");
        Item item1 = sellerMgr.offerItem(user1, cat, omsch);
        assertEquals(omsch, item1.getDescription());
        assertNotNull(item1.getId());
    }

    /**
     * Test of revokeItem method, of class SellerMgr.
     */
    @Test
    public void testRevokeItem() {
        String omsch = "omsch";
        String omsch2 = "omsch2";
        
    
        User seller = registrationMgr.registerUser("sel@nl");
        User buyer = registrationMgr.registerUser("buy@nl");
        Category cat = new Category();
        cat.setDescription("cat1");
        
            // revoke before bidding
        Item item1 = sellerMgr.offerItem(seller, cat, omsch);
        boolean res = sellerMgr.revokeItem(item1);
        assertTrue(res);
        int count = auctionMgr.findItemByDescription(omsch).size();
        assertEquals(0, count);
        
            // revoke after bid has been made
        Item item2 = sellerMgr.offerItem(seller, cat, omsch2);
        Money money = new Money();
        money.setCurrency("Euro");
        money.setCents(100);
        auctionMgr.newBid(item2, buyer, money);
        boolean res2 = sellerMgr.revokeItem(item2);
        assertFalse(res2);
        int count2 = auctionMgr.findItemByDescription(omsch2).size();
        assertEquals(1, count2);
    }
}
