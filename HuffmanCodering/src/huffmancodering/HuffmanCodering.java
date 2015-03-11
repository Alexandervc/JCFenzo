/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffmancodering;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
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
        Map<Character, Integer> map = frequentieTellen("bananen");
        PriorityQueue queue = frequentieSorteren(map);
        PriorityQueue boom = huffmanBoomMaken(queue);
        HashMap<Character, String> table = new HashMap<>();
        aflezenCodes((HuffKnoop) boom.poll(), "", table);
        System.out.println(table.toString());
    }
    
    // Stap 1
    public static Map<Character, Integer> frequentieTellen(String input)
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
        
        return hashmap;
    }
    
    // Stap 2
    public static PriorityQueue frequentieSorteren(Map<Character, Integer> map)
    {
        PriorityQueue queue = new PriorityQueue();
        for(Character c : map.keySet()) {
            queue.add(new HuffKnoop(c, map.get(c)));
        }
        
        return queue;
    }
    
    // Stap 3
    public static PriorityQueue huffmanBoomMaken(PriorityQueue queue)
    {
        while(queue.size() >= 2) {
            HuffKnoop left = (HuffKnoop) queue.poll();
            HuffKnoop right = (HuffKnoop) queue.poll();
            int freq = left.frequentie + right.frequentie;
            HuffKnoop knoop = new HuffKnoop(freq, left, right);
            queue.add(knoop);
        }
        
        return queue;
    }
    
    // Stap 4
    public static void aflezenCodes(HuffKnoop knoop, String code, HashMap<Character, String> table)
    {
        if(knoop.leftChild != null) 
        {
            code += "0";
            if(knoop.leftChild.character == null) 
            {
                aflezenCodes(knoop.leftChild, code, table);
            }
            else 
            {
                table.put(knoop.leftChild.character, code);
                code = code.substring(0, code.length() - 1);
            }
        }
        if(knoop.rightChild != null) 
        {
            code += "1";
            if(knoop.rightChild.character == null) 
            {
                aflezenCodes(knoop.rightChild, code, table);
            }
            else 
            {
                table.put(knoop.rightChild.character, code);
                code = code.substring(0, code.length() - 1);
            }
        }
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
