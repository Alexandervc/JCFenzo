package woordenapplicatie.gui;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author frankcoenen
 */
public class WoordenController implements Initializable {
    
   private static final String DEFAULT_TEXT =   "Een, twee, drie, vier\n" +
                                                "Hoedje van, hoedje van\n" +
                                                "Een, twee, drie, vier\n" +
                                                "Hoedje van papier\n" +
                                                "\n" +
                                                "Heb je dan geen hoedje meer\n" +
                                                "Maak er één van bordpapier\n" +
                                                "Eén, twee, drie, vier\n" +
                                                "Hoedje van papier\n" +
                                                "\n" +
                                                "Een, twee, drie, vier\n" +
                                                "Hoedje van, hoedje van\n" +
                                                "Een, twee, drie, vier\n" +
                                                "Hoedje van papier\n" +
                                                "\n" +
                                                "En als het hoedje dan niet past\n" +
                                                "Zetten we 't in de glazenkast\n" +
                                                "Een, twee, drie, vier\n" +
                                                "Hoedje van papier";
    
    @FXML
    private Button btAantal;
    @FXML
    private TextArea taInput;
    @FXML
    private Button btSorteer;
    @FXML
    private Button btFrequentie;
    @FXML
    private Button btConcordantie;
    @FXML
    private TextArea taOutput;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        taInput.setText(DEFAULT_TEXT);
    }
    
    @FXML
    private void aantalAction(ActionEvent event)
    {
        String text = taInput.getText();        
        text = text.replaceAll(",", " ");
        text = text.replaceAll("\\.", " ");
        text = text.replaceAll("\n", " ");
        text = text.replaceAll("\\s+", " ");
        text = text.toLowerCase();        
        String[] words = text.split(" ");
        
        //HashSet add: O(1), size: O(1), geen dubbele waardes
        HashSet<String> hashset = new HashSet<>();
        
        for (String s : words)
        {
            if (!s.isEmpty())
            {
                hashset.add(s);
            }
        }
        
        taOutput.setText("Totaal aantal woorden: " + words.length + "\n" 
                + "Aantal verschillende woorden: " + hashset.size());
    }

    @FXML
    private void sorteerAction(ActionEvent event)
    {
        //SET
        //Unieke woorden omgekeerd alfabetisch ordenen
        //Klasse Collections
        String text = taInput.getText();          
        text = text.replaceAll(",", " ");
        text = text.replaceAll("\\.", " ");
        text = text.replaceAll("\n", " ");
        text = text.replaceAll("\\s+", " ");
        text = text.toLowerCase();        
        String[] words = text.split(" ");
        
        //TreeSet add: geen dubbele waardes, gesorteerd: O(N)
        TreeSet<String> treeset = new TreeSet();        
        
        for (String s : words)
        {
            if (!s.isEmpty())
            {
                treeset.add(s);
            }
        }    
        
        taOutput.setText(treeset.descendingSet().toString());
    }

    @FXML
    private void frequentieAction(ActionEvent event)
    {
        //Aantal keer voorkomen van woord + ordenen op aantal
        
        //unieke waardes
        //aantal ophogen; get(key = woord) + (value = aantal)
        //sorteren value (comparable)
        String text = taInput.getText();
        
        //HashMap get: O(1)
    }

    @FXML
    private void concordantieAction(ActionEvent event)
    {
        //Op welke regel elk woord voorkomt + alfabetisch
        
        //tekst splitten
        //voor elk woord 
        String text = taInput.getText();
        
        //TreeMap ..
    }   
}
