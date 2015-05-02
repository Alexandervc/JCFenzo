package util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
     * 1. Asserties: 
     *    Printline:
     * 2. -
     * 3. -
     * 4. -
     */
    @Test
    public void vraag5() {
        //In de vorige opdracht verwijzen de objecten account en found naar dezelfde rij in de database. 
        //Pas een van de objecten aan, persisteer naar de database.
        //Refresh vervolgens het andere object om de veranderde state uit de database te halen.
        //Test met asserties dat dit gelukt is.
        
    }
    
    /**
     * 1. Asserties: 
     *    Printline:
     * 2. -
     * 3. -
     * 4. -
     */
//    @Test
//    public void vraag6() {
//        Account acc = new Account(1L);
//        Account acc2 = new Account(2L);
//        Account acc9 = new Account(9L);
//
//        // scenario 1
//        Long balance1 = 100L;
//        em.getTransaction().begin();
//        em.persist(acc);
//        acc.setBalance(balance1);
//        em.getTransaction().commit();
//        //TODO: voeg asserties toe om je verwachte waarde van de attributen te verifieren.
//        //TODO: doe dit zowel voor de bovenstaande java objecten als voor opnieuw bij de entitymanager opgevraagde objecten met overeenkomstig Id.
//
//
//        // scenario 2
//        Long balance2a = 211L;
//        acc = new Account(2L);
//        em.getTransaction().begin();
//        acc9 = em.merge(acc);
//        acc.setBalance(balance2a);
//        acc9.setBalance(balance2a+balance2a);
//        em.getTransaction().commit();
//        //TODO: voeg asserties toe om je verwachte waarde van de attributen te verifiëren.
//        //TODO: doe dit zowel voor de bovenstaande java objecten als voor opnieuw bij de entitymanager opgevraagde objecten met overeenkomstig Id. 
//        // HINT: gebruik acccountDAO.findByAccountNr
//
//
//        // scenario 3
//        Long balance3b = 322L;
//        Long balance3c = 333L;
//        acc = new Account(3L);
//        em.getTransaction().begin();
//        acc2 = em.merge(acc);
//        assertTrue(em.contains(acc)); // verklaar
//        assertTrue(em.contains(acc2)); // verklaar
//        assertEquals(acc,acc2);  //verklaar
//        acc2.setBalance(balance3b);
//        acc.setBalance(balance3c);
//        em.getTransaction().commit();
//        //TODO: voeg asserties toe om je verwachte waarde van de attributen te verifiëren.
//        //TODO: doe dit zowel voor de bovenstaande java objecten als voor opnieuw bij de entitymanager opgevraagde objecten met overeenkomstig Id.
//
//
//        // scenario 4
//        Account account = new Account(114L);
//        account.setBalance(450L);
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        em.persist(account);
//        em.getTransaction().commit();
//
//        Account account2 = new Account(114L);
//        Account tweedeAccountObject = account2;
//        tweedeAccountObject.setBalance(650l);
//        assertEquals((Long)650L,account2.getBalance());  //verklaar
//        account2.setId(account.getId());
//        em.getTransaction().begin();
//        account2 = em.merge(account2);
//        assertSame(account,account2);  //verklaar
//        assertTrue(em.contains(account2));  //verklaar
//        assertFalse(em.contains(tweedeAccountObject));  //verklaar
//        tweedeAccountObject.setBalance(850l);
//        assertEquals((Long)650L,account.getBalance());  //verklaar
//        assertEquals((Long)650L,account2.getBalance());  //verklaar
//        em.getTransaction().commit();
//        em.close();
//
//        // Find en clear
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
