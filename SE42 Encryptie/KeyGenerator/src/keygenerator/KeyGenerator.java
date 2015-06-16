/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keygenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexander
 */
public class KeyGenerator {
    private static int KEYSIZE = 1024;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // KeyPairGenerator aanmaken
            KeyPairGenerator pairgen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = new SecureRandom();
            pairgen.initialize(KEYSIZE, random);
            
            // Public en private key genereren
            KeyPair keyPair = pairgen.generateKeyPair();
            Key publicKey = keyPair.getPublic();
            Key privateKey = keyPair.getPrivate();
            
            // Public key wegschrijven
            FileOutputStream out = new FileOutputStream("publicKey.txt");
            out.write(publicKey.getEncoded());
            
            // Private key wegschrijven
            FileOutputStream out2 = new FileOutputStream("privateKey.txt");
            out2.write(privateKey.getEncoded());
            
            // Bericht wegschrijven
            FileOutputStream out3 = new FileOutputStream("input.ext");
            String message = "Hierbij verklaar ik dat Melanie en Alexander een 10 krijgen voor hun toets";
            out3.write(message.getBytes());
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
