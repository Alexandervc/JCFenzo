package auction.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.DatabaseCleaner;
import auction.domain.*;
import java.util.Iterator;
import nl.fontys.util.Money;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FurnitureAndPaintingTest {
    private AuctionMgr auctionMgr;
    private JPARegistrationMgr registrationMgr;
    private SellerMgr sellerMgr;
    
    private EntityManagerFactory emf;
    
    public FurnitureAndPaintingTest() {
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

    /**
     * Niet abstract:
     *  Zonder gekozen strategy voor de overerving:
     *      Alles wordt samen weggeschreven in de tabel Item met een discriminator DTYPE
     *      Zelfde effect als InheritanceType.SINGLE_TABLE
     * 
     *  InheritanceType.JOINED:
     *      Specifieke eigenschappen worden weggeschreven in Painting/Furniture
     *      Algemene eigenschappen worden weggeschreven in Item, daarbij bevat item
     *      een discriminator DTYPE en de rows in de tabellen zijn gekoppeld via id
     * 
     *  InheritanceType.TABLE_PER_CLASS:
     *      Voor iedere klasse is er een eigen tabel waarin de gegevens worden opgeslagen
     *      Bijv Furniture bevat ook de eigenschappen van Item
     * 
     * Item abstract:
     *  InheritanceType.SINGLE_TABLE:
     *      Alles wordt nog op dezelfde manier weggeschreven
     * 
     *  InheritanceType.JOINED:
     *      Alles wordt nog op dezelfde manier weggeschreven
     * 
     *  InheritanceType.TABLE_PER_CLASS:
     *      Furniture en Painting worden nog steeds in een aparte tabel weggeschreven
     *      Voor Item wordt wel een tabel aangemaakt met alleen een lege kolom: seller_id
     * 
     * 
     */
    @Test
    public void newFurniture() {
        String omsch = "omsch1";
        String iemand1 = "iemand1@def";
        User u1 = registrationMgr.registerUser(iemand1);
        User u2 = registrationMgr.registerUser("iemand2@def");
        Category cat = new Category("cat2");

        Item furniture1 = sellerMgr.offerFurniture(u1, cat, "broodkast", "ijzer");
        assertEquals("seller of item correct", furniture1.getSeller(), u1);

        User foundUser = registrationMgr.getUser(iemand1);
        Iterator<Item> it = foundUser.getOfferedItems();
        Item firstItem = it.next();
   //        int xxx = 22;
        assertEquals("item added in offeredItems", furniture1, firstItem);
        Item item2 = sellerMgr.offerPainting(u1, cat, omsch, "Nachtwacht", "Rembrandt");
        it = registrationMgr.getUser(iemand1).getOfferedItems();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());

        //de volgende code verwijderen als Item abstract is
        Item item3 = sellerMgr.offerItem(u1, new Category("boek"), "The science of Discworld");
        it = registrationMgr.getUser(iemand1).getOfferedItems();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());

        assertNull(furniture1.getHighestBid());
        Bid bid = auctionMgr.newBid(furniture1, u2, new Money(150000, Money.EURO));
        assertNotNull(furniture1.getHighestBid());

        Item foundFurniture = auctionMgr.getItem(furniture1.getId());
        int i = 3;
        // Zelfde id, betekent hetzelfde
        assertEquals(foundFurniture.getHighestBid().getId(), bid.getId());
        assertTrue(foundFurniture.getClass() == Furniture.class);
    }
}
