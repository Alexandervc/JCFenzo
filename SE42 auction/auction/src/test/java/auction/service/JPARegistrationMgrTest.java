package auction.service;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import auction.domain.User;
import java.util.List;

public class JPARegistrationMgrTest {
    
    private JPARegistrationMgr registrationMgr;

    @Before
    public void setUp() throws Exception {
        registrationMgr = new JPARegistrationMgr();
    }

    @Test
    public void registerUser() {
        registrationMgr.cleanDatabase();
        
        User user1 = registrationMgr.registerUser("xxx1@yyy");
        assertTrue(user1.getEmail().equals("xxx1@yyy"));
        User user2 = registrationMgr.registerUser("xxx2@yyy2");
        assertTrue(user2.getEmail().equals("xxx2@yyy2"));
        User user2bis = registrationMgr.registerUser("xxx2@yyy2");
        //Verwijzing naar object is niet gelijk, maar waardes wel.
        //Id is uniek en wordt in database gegenereerd,
        //dus hetzelfde id betekent hetzelfde object.
        assertSame(user2bis.getId(), user2.getId());
        //geen @ in het adres
        assertNull(registrationMgr.registerUser("abc"));
    }

    @Test
    public void getUser() {
        registrationMgr.cleanDatabase();
        
        User user1 = registrationMgr.registerUser("xxx5@yyy5");
        User userGet = registrationMgr.getUser("xxx5@yyy5");
        //Gegenereerd Id verwijst naar hetzelfde object in database.
        assertSame(userGet.getId(), user1.getId());
        assertNull(registrationMgr.getUser("aaa4@bb5"));
        registrationMgr.registerUser("abc");
        assertNull(registrationMgr.getUser("abc"));
    }

    @Test
    public void getUsers() {
        registrationMgr.cleanDatabase();
        
        List<User> users = registrationMgr.getUsers();
        assertEquals(0, users.size());

        User user1 = registrationMgr.registerUser("xxx8@yyy");
        users = registrationMgr.getUsers();
        assertEquals(1, users.size());
        //Gegenereerd Id verwijst naar hetzelfde object in database.
        assertSame(users.get(0).getId(), user1.getId());

        User user2 = registrationMgr.registerUser("xxx9@yyy");
        users = registrationMgr.getUsers();
        assertEquals(2, users.size());

        registrationMgr.registerUser("abc");
        //geen nieuwe user toegevoegd, dus gedrag hetzelfde als hiervoor
        users = registrationMgr.getUsers();
        assertEquals(2, users.size());
    }
}
