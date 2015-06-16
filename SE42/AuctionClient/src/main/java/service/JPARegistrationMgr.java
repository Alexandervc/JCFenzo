package service;

import java.util.*;
import auction.web.*;

public class JPARegistrationMgr {
    private RegistrationService service;
    private Registration port;
    
    public JPARegistrationMgr() {
        service = new RegistrationService();
        port = service.getRegistrationPort();
    }
    
    /**
     * Registreert een gebruiker met het als parameter gegeven e-mailadres, mits
     * zo'n gebruiker nog niet bestaat.
     * @param email
     * @return Een Userobject dat geïdentificeerd wordt door het gegeven
     * e-mailadres (nieuw aangemaakt of reeds bestaand). Als het e-mailadres
     * onjuist is ( het bevat geen '@'-teken) wordt null teruggegeven.
     */
    public User registerUser(String email) {
        try {
            User result = port.registerUser(email);
            System.out.println("Result = "+result);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param email een e-mailadres
     * @return Het Userobject dat geïdentificeerd wordt door het gegeven
     * e-mailadres of null als zo'n User niet bestaat.
     */
    public User getUser(String email) {
        try {
            User result = port.getUser(email);
            System.out.println("Result = "+result);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * @return Een iterator over alle geregistreerde gebruikers
     */
    public List<User> getUsers() {
        try {
            java.util.List<auction.web.User> result = port.getUsers();
            System.out.println("Result = "+result);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public void cleanDB() {
        try {
            port.cleanDB();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
