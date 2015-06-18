/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Verifier;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexander
 */
public class Verifier {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // Public key ophalen
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedPublicKey = Files.readAllBytes(new File("publicKey.txt").toPath());
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            
            // Handtekening en bericht ophalen
            FileInputStream in = new FileInputStream("input(signedByMellex).ext");
            DataInputStream in2 = new DataInputStream(in);
            int messageSize = (int) in.getChannel().size();
            
            int length = in2.readInt();
            messageSize -= 4;
            
            byte[] signatureBytes = new byte[length];
            for(int i = 0; i < length; i++) {
                signatureBytes[i] = in2.readByte();
                messageSize -= 1;
            }
            
            byte[] messageBytes = new byte[messageSize];
            for(int i = 0; i < messageSize; i++) {
                messageBytes[i] = in2.readByte();
            }
            
            String message = new String(messageBytes, "UTF-8");
            
            // Signature aanmaken en initialiseren als verifier
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(publicKey);
            
            // Voeg data toe
            signature.update(messageBytes);
            
            // Verifieer handtekening
            boolean valid = signature.verify(signatureBytes);
            
            if(valid) {
                System.out.println("Handtekening goedgekeurd");
                System.out.println(message);
            } else {
                System.out.println("Handtekening niet goedgekeurd");
            }
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException ex) {
            Logger.getLogger(Verifier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
