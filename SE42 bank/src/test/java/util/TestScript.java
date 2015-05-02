package util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import bank.domain.Account;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alexander
 */
public class TestScript {
    private EntityManager em;
    private DatabaseCleaner dbCleaner;
    
    public TestScript() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bankPU");
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
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCleaner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void vraag1() {
        Account account = new Account(111L);
        em.getTransaction().begin();
        em.persist(account);
        //TODO: verklaar en pas eventueel aan
        assertNull(account.getId());
        em.getTransaction().commit();
        System.out.println("AccountId: " + account.getId());
        //TODO: verklaar en pas eventueel aan
        assertTrue(account.getId() > 0L);
    }
}
