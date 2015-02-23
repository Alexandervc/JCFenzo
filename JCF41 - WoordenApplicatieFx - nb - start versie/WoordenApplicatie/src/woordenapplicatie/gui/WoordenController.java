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
        String text = taInput.getText();        
        text = text.replaceAll(",", " ");
        text = text.replaceAll("\\.", " ");
        text = text.replaceAll("\n+", "\n");
        text = text.toLowerCase();        
        
        String[] lineStrings = text.split("\n");
        // Array lines van HashSet words, want daarop vooral Add en Contains aanroepen en die hebben O(1)
        HashSet[] lines = new HashSet[lineStrings.length];
        // HashSet van alle unieke woorden, want geen dubbelen en vooral Add aanroepen en die heeft O(1)
        HashSet<String> uniqueWords = new HashSet<>();
        
        
        for(int i = 0; i < lineStrings.length; i++) 
        {
            lineStrings[i] = lineStrings[i].replaceAll("\\s+", " ");
            String[] wordStrings = lineStrings[i].split(" ");
            HashSet<String> words = new HashSet<>();
            for(String w : wordStrings) 
            {
                uniqueWords.add(w);
                words.add(w);
            }
            lines[i] = words;
        }
        
        Iterator<String> itUniqueWords = uniqueWords.iterator();
        
        // TreeMap, want je hebt Key-Value paren nodig en dan is het meteen op alfabetische volgorde, maar containsKey duurt wss wel langer?
        // en get en put zijn O(log n) en die worden wel vaak aangeroepen, dus miss toch HashMap, maar ivm met alfabet??
        // met als value HashSet omdat de Add O(1) heeft
        TreeMap<String, HashSet<Integer>> treemap = new TreeMap<>();
        
        // door alle items in uniqueWords loopen en dan per line kijken of die hem bevat,
        while(itUniqueWords.hasNext()) 
        {
            String word = itUniqueWords.next();
            for(int i = 0; i < lines.length; i++) 
            {
                if(lines[i].contains(word)) 
                {
                    // zo ja voeg toe aan TreeMap
                    if(treemap.containsKey(word)) 
                    {
                        treemap.get(word).add(i + 1);
                    }
                    else
                    {
                        HashSet<Integer> references = new HashSet<> ();
                        references.add(i + 1);
                        treemap.put(word, references);
                    }
                }
            }
        }
        
        taOutput.setText(treemap.toString());
    }   
}
