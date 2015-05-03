package util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import bank.dao.AccountDAO;
import bank.dao.AccountDAOJPAImpl;
import bank.domain.Account;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Alexander
 */
public class TestScript {
    private EntityManagerFactory emf;
    private EntityManager em;
    private DatabaseCleaner dbCleaner;
    
    public TestScript() {
        emf = Persistence.createEntityManagerFactory("bankPU");
        em = emf.createEntityManager();
        dbCleaner = new DatabaseCleaner(em);
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        try {
            dbCleaner.clean();
            em = emf.createEntityManager();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCleaner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
    }

    /**
     * 1. Wat is de waarde van asserties en printstatements? Corrigeer verkeerde 
     *    asserties zodat de test ‘groen’ wordt.
     * 2. Welke SQL statements worden gegenereerd?
     * 3. Wat is het eindresultaat in de database?
     * 4. Verklaring van bovenstaande drie observaties.
     */
    
    /**
     * 1. Asserties: Null (ID) & True (ID > 1)
     *    Printline: ID = 17 (gegenereerde waarde)
     * 2. Geen
     * 3. Nieuw account toegevoegd met gegenereerd ID, afhankelijk van aantal keer
     *    aaroepen binnen de klasse Account.
     * 4. Tijdens de transactie wordt het account nog niet weggeschreven. Pas na
     *    de commit wordt er een ID gegenereerd en toegekend, en komt het account
     *    in de database te staan.
     */
    @Test
    public void vraag1() {
        Account account = new Account(111L);
        em.getTransaction().begin();
        em.persist(account);
        //ID is null omdat deze nog geen GeneratedValue heeft gekregen
        assertNull(account.getId());
        em.getTransaction().commit();
        System.out.println("AccountId: " + account.getId());
        //ID is groter dan 0, want ID is gegenereerd in database
        assertTrue(account.getId() > 0L);
    }
    
    /**
     * 1. Asserties: Null (ID) & Equals (lengte lijst = 0)
     * 2. Geen
     * 3. Account is niet toegevoegd vanwege rollback. Tabel Account is in dit
     *    geval leeg.
     * 4. Door de rollback worden alle acties tussen begin en rollback niet uitgevoerd.
     *    Account is niet toegevoegd, want alleen persist wordt aangeroepen (geen commit).
     */
    @Test
    public void vraag2() {
        Account account = new Account(111L);
        em.getTransaction().begin();
        em.persist(account);
        assertNull(account.getId());
        em.getTransaction().rollback();
        //Code om te testen dat table account geen records bevat. Hint: bestudeer/gebruik AccountDAOJPAImpl
        AccountDAOJPAImpl impl = new AccountDAOJPAImpl(em);
        assertEquals(0, impl.count());        
    }
    
    /**
     * 1. Asserties: Equals (ID = -100) & NotEquals (ID = -100) & True (ID > 0)
     *    & NotEquals (ID = -100) & True (ID > 0)
     * 2. Het ingegeven ID wordt niet in de database gezet, maar deze wordt gegenereerd.
     * 3. Het account met een gegenereed ID wordt in de database gezet.
     * 4. Bij de eerste persist wordt het ingegeven ID bewaard. Door de flush wordt
     *    het ingegeven ID vervangen door een gegenereerd ID. Na de commit wordt
     *    het account pas in de database weggeschreven.
     */
    @Test
    public void vraag3() {
        Long expected = -100L;
        Account account = new Account(111L);
        account.setId(expected);
        em.getTransaction().begin();
        em.persist(account);
        //Ingegeven ID blijft behouden, want ID is nog niet in database gegenereerd
        assertEquals(expected, account.getId());
        
        em.flush();
        //Door flush wordt commando uitgevoerd, dus ook een ID gegenereerd, maar
        //nog niet in de database toegevoegd
        assertNotEquals(expected, account.getId());
        assertTrue(account.getId() > 0L);
        
        em.getTransaction().commit();
        //Account wordt aan de database toegevoegd
        assertNotEquals(expected, account.getId());
        assertTrue(account.getId() > 0L);
    }
    
    /**
     * 1. Asserties: Equals (balance = 400) & Equals (balance = 400)
     * 2. Geen
     * 3. Het account met de ingegeven balance.
     * 4. Applicatie kan Peristence Context aanpassen.
     */
    @Test
    public void vraag4() {
        Long expectedBalance = 400L;
        Account account = new Account(114L);
        em.getTransaction().begin();
        em.persist(account);
        account.setBalance(expectedBalance);
        em.getTransaction().commit();
        //Door de persist() wordt de informatie in de Persistence Context gezet. 
        //Je kunt de Persistence Context benaderen vanuit je applicatie.
        //De balance van het account wordt dan ook aangepast en in de database weggeschreven.
        assertEquals(expectedBalance, account.getBalance());
      
        Long acId = account.getId();
        account = null;
        EntityManager em2 = emf.createEntityManager();
        em2.getTransaction().begin();
        Account found = em2.find(Account.class, acId);
        //Account in de database klopt met ingegeven balance
        assertEquals(expectedBalance, found.getBalance());
    }
    
    /**
     * 1. Asserties: vraag 4 + Equals (balance = 600)
     * 2. Geen
     * 3. Balance van account is aangepast naar 600.
     * 4. Als je het ene object aanpast, dan wordt het andere object ook aangepast.
     *    Ze verwijzen naar dezelfde regel.
     */
    @Test
    public void vraag5() {
        //In de vorige opdracht verwijzen de objecten account en found naar dezelfde rij in de database. 
        //Pas een van de objecten aan, persisteer naar de database.
        //Refresh vervolgens het andere object om de veranderde state uit de database te halen.
        //Test met asserties dat dit gelukt is.
        
        Long expectedBalance = 400L;
        Long expectedBalance2 = 600L;
        Account account = new Account(114L);
        em.getTransaction().begin();
        em.persist(account);
        account.setBalance(expectedBalance);
        em.getTransaction().commit();
      
        Long acId = account.getId();
        EntityManager em2 = emf.createEntityManager();
        em2.getTransaction().begin();
        Account found = em2.find(Account.class, acId);
        
        //Beide objecten verwijzen naar dezelfde regel in de database
        //Door het ene object aan te passen en de wijzigingen weg te schrijven,
        //wordt dit bij het andere object na een Refresh ook aangepast
        account.setBalance(expectedBalance2);
        em.getTransaction().begin();
        em.persist(account);
        em.getTransaction().commit();
        em2.refresh(found);
        assertEquals(found.getBalance(), expectedBalance2);
    }
    
    /**
     * 1. Asserties: 
     *    Printline:
     * 2. -
     * 3. -
     * 4. -
     */
    @Test
    public void vraag6() {
        Account acc = new Account(1L);
        Account acc2 = new Account(2L);
        Account acc9 = new Account(9L);

        // scenario 1
        Long balance1 = 100L;
        em.getTransaction().begin();
        em.persist(acc);
        acc.setBalance(balance1);
        em.getTransaction().commit();        
        assertEquals(balance1, acc.getBalance());
      
        Long acId = acc.getId();
        EntityManager em2 = emf.createEntityManager();
        em2.getTransaction().begin();
        Account found = em2.find(Account.class, acId);
        //Dezelfde uitkomst als bij vraag 4
        assertEquals(balance1, found.getBalance());


        // scenario 2
        Long balance2a = 211L;
        acc = new Account(2L);
        em.getTransaction().begin();
        acc9 = em.merge(acc);
        acc.setBalance(balance2a);
        //Balance van acc is gewijzigd, balance van acc9 is niet gewijzigd
        assertNotEquals(acc.getBalance(), acc9.getBalance());
        assertEquals(acc.getBalance(), balance2a);
        acc9.setBalance(balance2a+balance2a);
        //Balance van acc9 is gewijzigd, balance van acc is niet gewijzigd
        assertEquals(acc9.getBalance(), Long.valueOf(balance2a + balance2a));
        assertEquals(acc.getBalance(), balance2a);
        em.getTransaction().commit();
        //Beide accs houden hun eigen balance
        assertEquals(acc9.getBalance(), Long.valueOf(balance2a + balance2a));
        assertEquals(acc.getBalance(), balance2a);
        // Balance van acc in de database is gelijk aan die van acc9
        AccountDAO accountDAO = new AccountDAOJPAImpl(em);
        Account found2 = accountDAO.findByAccountNr(acc9.getAccountNr());
        assertEquals(found2.getBalance(), acc9.getBalance());

        // scenario 3
        Long balance3b = 322L;
        Long balance3c = 333L;
        acc = new Account(3L);
        em.getTransaction().begin();
        acc2 = em.merge(acc);
        //acc is niet gekoppeld aan het object in de persistence context
        //dus contains geeft false
        assertFalse(em.contains(acc));
        //acc2 is wel gekoppeld aan het object in de persistence context
        //dus contains geeft true
        assertTrue(em.contains(acc2));
        //acc en acc2 verwijzen niet naar hetzelfde object,
        //maar acc2 is een kopie van acc, dus het id is wel gelijk
        assertNotEquals(acc,acc2);
        assertEquals(acc.getId(), acc2.getId());
        acc2.setBalance(balance3b);
        //balance van acc2 is gewijzigd naar balance3b,
        //balance van acc is niet gewijzigd naar balance3b
        assertEquals(acc2.getBalance(), balance3b);
        assertNotEquals(acc.getBalance(), balance3b);
        acc.setBalance(balance3c);
        //balance van acc2 is nog gelijk aan balance3b,
        //balance van acc is gewijzigd naar balance3c
        assertEquals(acc2.getBalance(), balance3b);
        assertEquals(acc.getBalance(), balance3c);
        em.getTransaction().commit();
        //beide accs houden hun eigen balance
        assertEquals(acc2.getBalance(), balance3b);
        assertEquals(acc.getBalance(), balance3c);
        //acc in de database heeft dezelfde balance als acc2 (balance3b),
        //omdat acc2 gekoppeld is in de persistence context
        found2 = accountDAO.findByAccountNr(acc.getAccountNr());
        assertEquals(found2.getBalance(), balance3b);

        // scenario 4
        Account account = new Account(114L);
        account.setBalance(450L);
        em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(account);
        em.getTransaction().commit();

        Account account2 = new Account(114L);
        Account tweedeAccountObject = account2;
        tweedeAccountObject.setBalance(650l);
        //account2 en tweedeAccountObject zijn een referentie naar hetzelfde object,
        //dus als de balance van tweedeAccountObject aangepast wordt, wordt die 
        //van account2 ook aangepast
        assertEquals((Long)650L,account2.getBalance());
        account2.setId(account.getId());
        em.getTransaction().begin();
        account2 = em.merge(account2);
        //Merge zoekt binnen de persistence context naar een object met hetzelfde id
        //account zit al in de persistence context en heeft hetzelfde id,
        //dus account2 krijgt een koppeling naar de persistence context van account
        //account en account2 verwijzen daarna naar hetzelfde object
        assertSame(account,account2);
        //doordat de koppeling naar de persistence context wordt weggeschreven in account2
        //bevat de entitymanager account en account2
        assertTrue(em.contains(account));
        assertTrue(em.contains(account2));
        //account2 en tweedeAccountObject verwijzen niet meer naar hetzelfde object,
        //want account2 verwijst nu naar hetzelfde object als account,
        //dus de entitymanager bevat tweedeAccountObject niet
        assertFalse(em.contains(tweedeAccountObject));
        tweedeAccountObject.setBalance(850l);
        //account2 en tweedeAccountObject verwijzen niet meer naar hetzelfde object,
        //want account2 verwijst nu naar hetzelfde object als account,
        //dus een wijziging in tweedeAccount wordt niet doorgevoerd bij account2
        assertEquals((Long)650L,account.getBalance());
        assertEquals((Long)650L,account2.getBalance());
        em.getTransaction().commit();
        em.close();

        // Find en clear
        Account acc1 = new Account(77L);
        em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(acc1);
        em.getTransaction().commit();
        //Database bevat nu een account.

        // scenario 1        
        Account accF1;
        Account accF2;
        accF1 = em.find(Account.class, acc1.getId());
        accF2 = em.find(Account.class, acc1.getId());
        //Verwijzen naar hetzelfde object in de database
        assertSame(accF1, accF2);

        // scenario 2        
        accF1 = em.find(Account.class, acc1.getId());
        em.clear();
        accF2 = em.find(Account.class, acc1.getId());
        //em.clear() gooit de persistence context leeg
        //bij het zoeken naar een object met het id van acc1 (bij accF2)
        //bestaat dit object niet meer in de persistence context en
        //moet het opnieuw aangemaakt worden
        //hierdoor verwijzen accF1 en accF2 niet naar hetzelfde object
        assertNotSame(accF1, accF2);
    }
    
    /**
     * 1. Asserties: 
     *    Printline:
     * 2. -
     * 3. -
     * 4. -
     */
//    @Test
//    public void vraag7() {
//        Account acc1 = new Account(77L);
//        em.getTransaction().begin();
//        em.persist(acc1);
//        em.getTransaction().commit();
//        //Database bevat nu een account.
//
//        // scenario 1        
//        Account accF1;
//        Account accF2;
//        accF1 = em.find(Account.class, acc1.getId());
//        accF2 = em.find(Account.class, acc1.getId());
//        assertSame(accF1, accF2);
//
//        // scenario 2        
//        accF1 = em.find(Account.class, acc1.getId());
//        em.clear();
//        accF2 = em.find(Account.class, acc1.getId());
//        assertSame(accF1, accF2);
//        //TODO verklaar verschil tussen beide scenario's
//    }
    
    /**
     * 1. Asserties: 
     *    Printline:
     * 2. -
     * 3. -
     * 4. -
     */
//    @Test
//    public void vraag8() {
//        Account acc1 = new Account(88L);
//        em.getTransaction().begin();
//        em.persist(acc1);
//        em.getTransaction().commit();
//        Long id = acc1.getId();
//        //Database bevat nu een account.
//
//        em.remove(acc1);
//        assertEquals(id, acc1.getId());        
//        Account accFound = em.find(Account.class, id);
//        assertNull(accFound);
//        //TODO: verklaar bovenstaande asserts
//    }
    
    /**
     * 1. Asserties: 
     *    Printline:
     * 2. -
     * 3. -
     * 4. -
     */
    @Test
    public void vraag9() {
        //Opgave 1 heb je uitgevoerd met @GeneratedValue(strategy = GenerationType.IDENTITY)
        //Voer dezelfde opdracht nu uit met GenerationType SEQUENCE en TABLE.
        //Verklaar zowel de verschillen in testresultaat als verschillen van de database structuur.
        
    }
}
