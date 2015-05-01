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
    
    public String[] getWords(String text)
    {       
        text = text.replaceAll(",", " ");
        text = text.replaceAll("\\.", " ");
        text = text.replaceAll("\n", " ");
        
        //extra filter
        text = text.replaceAll("\\W+", " ");
        
        text = text.replaceAll("\\s+", " ");
        text = text.toLowerCase();        
        String[] words = text.split(" ");
        return words;
    }
    
    @FXML
    private void aantalAction(ActionEvent event)
    {   
        String[] words = getWords(taInput.getText());
        
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
        String[] words = getWords(taInput.getText());
        
        //TreeSet add: geen dubbele waardes, gesorteerd: O(log n)
        TreeSet<String> treeset = new TreeSet();        
        
        for (String s : words)
        {
            if (!s.isEmpty())
            {
                treeset.add(s);
            }
        }    
        
        String output = "";
        
        for (String s : treeset.descendingSet())
        {
            output += s + "\n";
        }
        
        taOutput.setText(output);
    }

    @FXML
    private void frequentieAction(ActionEvent event)
    {        
        String[] words = getWords(taInput.getText());
        
        //HashMap get: O(1), put: O(1)
        HashMap<String, Integer> hashmap = new HashMap<>();
        
        for (String s : words)
        {
            Integer i = hashmap.get(s);
            
            if (i != null)
            {
                i += 1;
                hashmap.put(s, i);
            }
            else
            {
                hashmap.put(s, 1);
            }
        }
        
        //Treeset addAll: O(n log n)
        TreeSet<Map.Entry<String, Integer>> sortedmap = new TreeSet<>(
            new Comparator<Map.Entry<String, Integer>>()
            {
                @Override
                public int compare(Map.Entry<String, Integer> t, Map.Entry<String, Integer> t1) {
                    if (t.getValue().equals(t1.getValue()))
                    {
                        return t.getKey().compareTo(t1.getKey());
                    }

                    return t.getValue().compareTo(t1.getValue());
                }
            }
        );
        
        sortedmap.addAll(hashmap.entrySet());
        
        String output = "";
        
        for (Map.Entry<String, Integer> m : sortedmap)
        {
            output += m.getKey() + ": " + m.getValue() + "\n";
        }
        
        taOutput.setText(output);
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
        
        // HashSet[] add: O(1), contains: O(1)
        HashSet[] lines = new HashSet[lineStrings.length];
        
        // HashSet geen dubbele waardes, add: O(1)
        HashSet<String> uniqueWords = new HashSet<>();        
        
        for(int i = 0; i < lineStrings.length; i++) 
        {
            //extra filter
            lineStrings[i] = lineStrings[i].replaceAll("\\W+", " ");
            
            lineStrings[i] = lineStrings[i].replaceAll("\\s+", " ");
            String[] wordStrings = lineStrings[i].split(" ");
            
            // HashSet add: O(1)
            HashSet<String> words = new HashSet<>();
            for(String w : wordStrings) 
            {
                if (!w.isEmpty())
                {
                    uniqueWords.add(w);
                    words.add(w);
                }
            }
            lines[i] = words;
        }
        
        Iterator<String> itUniqueWords = uniqueWords.iterator();
        
        // TreeMap key-value paren, gesorteerd, get: O(log n), put: O(log n)
        // TreeSet gesorteerd, add: O(log n)
        TreeMap<String, TreeSet<Integer>> treemap = new TreeMap<>();
        
        // per uniqueWord kijken in welke regels die zit
        while(itUniqueWords.hasNext()) 
        {
            String word = itUniqueWords.next();
            for(int i = 0; i < lines.length; i++) 
            {
                if(lines[i].contains(word)) 
                {
                    // toevoegen aan TreeMap
                    if(treemap.containsKey(word)) 
                    {
                        treemap.get(word).add(i + 1);
                    }
                    else
                    {
                        TreeSet<Integer> references = new TreeSet<> ();
                        references.add(i + 1);
                        treemap.put(word, references);
                    }
                }
            }
        }
        
        String output = "";
        
        for (Map.Entry<String, TreeSet<Integer>> me : treemap.entrySet())
        {
            output += me.getKey() + ": [";
            for(Integer i : me.getValue()) 
            {
                output += i + ", ";
            }
            output = output.substring(0, output.length() - 2);
            output += "] \n";
        }
        
        taOutput.setText(output);
    }   
}
