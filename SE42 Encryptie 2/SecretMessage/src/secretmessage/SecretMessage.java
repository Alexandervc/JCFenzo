/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package secretmessage;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Alexander
 */
public class SecretMessage extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        URL location = getClass().getResource("SecretMessageFXML.fxml");  
        FXMLLoader fxmlLoader = new FXMLLoader();  
        fxmlLoader.setLocation(location);  
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());  
        Parent root = (Parent) fxmlLoader.load(location.openStream());
        
        ((SecretMessageFXMLController)fxmlLoader.getController()).setValues(stage);
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Secret Message");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
