package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import auction.web.*;
import service.*;

public class JPARegistrationMgrTest {
    
    private JPARegistrationMgr registrationMgr;
    
    public JPARegistrationMgrTest() {  
        registrationMgr = new JPARegistrationMgr();
    }

    @Before
    public void setUp() throws Exception {
        registrationMgr.cleanDB();
    }

    @Test
    public void registerUser() {
        User user1 = registrationMgr.registerUser("xxx1@yyy");
        assertTrue(user1.getEmail().equals("xxx1@yyy"));
        User user2 = registrationMgr.registerUser("xxx2@yyy2");
        assertTrue(user2.getEmail().equals("xxx2@yyy2"));
        User user2bis = registrationMgr.registerUser("xxx2@yyy2");
        //Verwijzing naar object is niet gelijk, maar waardes wel.
        //Id is uniek en wordt in database gegenereerd,
        //dus hetzelfde id betekent hetzelfde object.
        assertEquals(user2bis.getId(), user2.getId());
        //geen @ in het adres
        assertNull(registrationMgr.registerUser("abc"));
    }

    @Test
    public void getUser() {
        User user1 = registrationMgr.registerUser("xxx5@yyy5");
        User userGet = registrationMgr.getUser("xxx5@yyy5");
        //Gegenereerd Id verwijst naar hetzelfde object in database.
        assertEquals(userGet.getId(), user1.getId());
        assertNull(registrationMgr.getUser("aaa4@bb5"));
        registrationMgr.registerUser("abc");
        assertNull(registrationMgr.getUser("abc"));
    }

    @Test
    public void getUsers() {
        List<User> users = registrationMgr.getUsers();
        assertEquals(0, users.size());

        User user1 = registrationMgr.registerUser("xxx8@yyy");
        users = registrationMgr.getUsers();
        assertEquals(1, users.size());
        //Gegenereerd Id verwijst naar hetzelfde object in database.
        assertEquals(users.get(0).getId(), user1.getId());

        User user2 = registrationMgr.registerUser("xxx9@yyy");
        users = registrationMgr.getUsers();
        assertEquals(2, users.size());

        registrationMgr.registerUser("abc");
        //geen nieuwe user toegevoegd, dus gedrag hetzelfde als hiervoor
        users = registrationMgr.getUsers();
        assertEquals(2, users.size());
    }
}
