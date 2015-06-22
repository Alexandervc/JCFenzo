/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package secretmessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javax.crypto.Cipher;
import javax.crypto.*;
import javax.crypto.spec.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Alexander
 */
public class SecretMessageFXMLController implements Initializable {
    @FXML PasswordField pfPassword;
    @FXML TextArea taMessage; 
    Stage stage;
    FileChooser fileChooser;
    
    private int saltLength = 8;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileChooser = new FileChooser();
        ExtensionFilter filter = new ExtensionFilter("*.ext", "*.ext");
        fileChooser.getExtensionFilters().add(filter);
    }    
    
    public void setValues(Stage stage) {
        this.stage = stage;
    }
    
    private byte[] crypt(int mode, byte[] message, byte[] salt) {
        byte[] ciphertext = null;
        
        try {            
            // Iteration count
            int count = 1000;
            
            // Create PBE parameter set
            PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
            
            // Collect user password as char array, and convert
            // it into a SecretKey object, using a PBE key
            // factory.
            // Haal wachtwoord op
            CharSequence passwordSequence = pfPassword.getCharacters();
            pfPassword.clear();
            char[] password = new char[passwordSequence.length()];
            for(int i = 0; i < passwordSequence.length(); i++) {
                password[i] = passwordSequence.charAt(i);
            }
            passwordSequence = null;
            
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
            
            // Vervang wachtwoord door nullen
            for(int i = 0; i < password.length; i++) {
                password[i] = '0';
            }
            
            SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
            
            // Create PBE Cipher
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            
            // Initialize PBE Cipher with key and parameters
            pbeCipher.init(mode, pbeKey, pbeParamSpec);
            
            // Crypt the message
            ciphertext = pbeCipher.doFinal(message);
            
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException ex) {
            ex.printStackTrace();
        } catch (BadPaddingException ex) {
            throw new IllegalArgumentException("U heeft het verkeerde wachtwoord ingevuld");
        }
        
        return ciphertext;
    }
    
    public void encrypt() {
        try {
            // Controleer input
            if(pfPassword.getCharacters().length() <= 0) {
                throw new IllegalArgumentException("Voer een wachtwoord in");
            }
            if(taMessage.getText() == null || taMessage.getText().isEmpty()) {
                throw new IllegalArgumentException("Voer een bericht in");
            }
            File file = fileChooser.showSaveDialog(stage);
            if(file == null) {
                throw new IllegalArgumentException("Kies een file");
            }
            
            // Genereer salt
            byte[] salt = new byte[saltLength];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            byte[] encryptedMessage = crypt(Cipher.ENCRYPT_MODE, taMessage.getText().getBytes(), salt);

            // Wegschrijven naar file
            FileOutputStream out = new FileOutputStream(file);
            DataOutputStream out2 = new DataOutputStream(out);
            for(byte b : salt) {
                out2.writeByte(b);
            }
            for(byte b : encryptedMessage) {
                out2.writeByte(b);
            }
            
            taMessage.clear();
            JOptionPane.showMessageDialog(null, "Bericht is succesvol weggeschreven");
        } catch (IllegalArgumentException iaEx) {
            JOptionPane.showMessageDialog(null, iaEx.getMessage());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public void decrypt() {
        try {
            // Controleer input
            if(pfPassword.getCharacters().length() <= 0) {
                throw new IllegalArgumentException("Voer een wachtwoord in");
            }
            File file = fileChooser.showOpenDialog(stage);
            if(file == null) {
                throw new IllegalArgumentException("Kies een file");
            }
            
            // File uitlezen
            FileInputStream in = new FileInputStream(file);
            DataInputStream in2 = new DataInputStream(in);
            
            byte[] salt = new byte[saltLength];
            byte[] message = new byte[(int) file.length() - saltLength];
            for(int i = 0; i < file.length(); i++) {
                if(i < saltLength) {
                    salt[i] = in2.readByte();
                } else {
                    message[i - saltLength] = in2.readByte();
                }
            }

            byte[] decryptedMessage = crypt(Cipher.DECRYPT_MODE, message, salt);

            // Omzetten naar tekst
            String decrypted = new String(decryptedMessage, "UTF-8");
            taMessage.setText(decrypted);
        } catch (IllegalArgumentException iaEx) {
            JOptionPane.showMessageDialog(null, iaEx.getMessage());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
