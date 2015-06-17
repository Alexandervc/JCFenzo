/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Signer;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexander
 */
public class Signer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FileOutputStream out = null;
        try {
            String name = "Mellex";
            
            // Ophalen private key
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedPrivateKey = Files.readAllBytes(new File("privateKey.txt").toPath());
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            
            // Nieuwe Signature aanmaken en initialiseren voor ondertekenen
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(privateKey);
            
            // Bericht ophalen
            byte[] message = Files.readAllBytes(new File("input.ext").toPath());
            signature.update(message);
            
            // Bericht ondertekenen
            byte[] signed = signature.sign();
            
            // Ondertekend bericht wegschrijven
            out = new FileOutputStream("input(signedBy" + name + ").ext");
            DataOutputStream out2 = new DataOutputStream(out);
            out2.writeInt(signed.length);
            out2.write(signed);
            out2.write(message);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException | SignatureException | IOException | InvalidKeySpecException | InvalidKeyException ex) {
            Logger.getLogger(Signer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
