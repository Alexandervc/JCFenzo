/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffmancodering;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 *
 * @author Alexander
 */
public class HuffmanCodering {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
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
    
    // Stap 1
    public static void frequentieTellen(String input)
    {
        String[] words = getWords(input.getText());
        
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
    
    // Stap 2
    public static void frequentieSorteren()
    {
        
    }
    
    // Stap 3
    public static void huffmanBoomMaken()
    {
        
    }
    
    // Stap 4
    public static void aflezenCodes()
    {
        
    }
    
    // Stap 5
    public static void coderenBericht()
    {
        
    }
    
    // Stap 6
    public static void decoderenBericht() 
    {
        
    }
}
