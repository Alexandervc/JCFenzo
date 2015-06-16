/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package secretmessage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;

/**
 *
 * @author Alexander
 */
public class SecretMessageFXMLController implements Initializable {
    @FXML PasswordField pfPassword;
    @FXML TextArea taMessage;    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void encrypt() {
        
    }
    
    public void decrypt() {
        
    }
    
}
