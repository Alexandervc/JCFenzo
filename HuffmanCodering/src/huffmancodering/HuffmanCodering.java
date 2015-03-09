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
    
    // Stap 1
    public static void frequentieTellen(String input)
    {
        char[] chars = input.toCharArray();
        
        //HashMap get: O(1), put: O(1)
        HashMap<Character, Integer> hashmap = new HashMap<>();
        
        for (char c : chars)
        {
            Integer i = hashmap.get(c);
            
            if (i != null)
            {
                i++;
                hashmap.put(c, i);
            }
            else
            {
                hashmap.put(c, 1);
            }
        }
        
        // blabla
    }
    
    // Stap 2
    public static void frequentieSorteren(Map<Character, Integer> map)
    {
        //Treeset addAll: O(n log n)
        TreeSet<Map.Entry<Character, Integer>> sortedmap = new TreeSet<>(
            new Comparator<Map.Entry<Character, Integer>>()
            {
                @Override
                public int compare(Map.Entry<Character, Integer> t, Map.Entry<Character, Integer> t1) {
                    if (t.getValue().equals(t1.getValue()))
                    {
                        return t.getKey().compareTo(t1.getKey());
                    }

                    return t.getValue().compareTo(t1.getValue());
                }
            }
        );
        
        sortedmap.addAll(map.entrySet());
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
