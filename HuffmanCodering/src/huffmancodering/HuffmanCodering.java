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
        //String bericht = "bananen";
        String bericht = "laboratoriumonderzoek";
        
        // Stap 1
        Map<Character, Integer> map = frequentieTellen(bericht);
        System.out.println(map.toString());
        
        // Stap 2
        PriorityQueue queue = frequentieSorteren(map);
        
        // Stap 3
        PriorityQueue boom = huffmanBoomMaken(queue);
        
        // Stap 4
        HuffKnoop hoofdKnoop = (HuffKnoop) boom.poll();
        HashMap<Character, String> tabel = new HashMap<>();
        aflezenCodes(hoofdKnoop, "", tabel);
        System.out.println(tabel.toString());
        
        // Stap 5
        String gecodeerdBericht = coderenBericht(tabel, bericht);
        System.out.println(gecodeerdBericht);
        
        // Stap 6
        ArrayList<Character> gecodeerdArray = new ArrayList<>();
        
        for(char c : gecodeerdBericht.toCharArray()) 
        {
            gecodeerdArray.add(c);
        }
        
        StringBuilder ontvangenBericht = new StringBuilder("");
        decoderenBericht(hoofdKnoop, hoofdKnoop, ontvangenBericht, gecodeerdArray, 0);
        System.out.println(ontvangenBericht.toString());
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
        // PriorityQueue add: O(log n)
        // HashMap get: O(1)
        PriorityQueue queue = new PriorityQueue();
        for(Character c : map.keySet()) {
            queue.add(new HuffKnoop(c, map.get(c)));
        }
        
        return queue;
    }
    
    // Stap 3
    public static PriorityQueue huffmanBoomMaken(PriorityQueue queue)
    {
        // PriorityQueue poll: O(log n), add: O(log n), size: O(1)
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
    public static void aflezenCodes(HuffKnoop knoop, String code, HashMap<Character, String> tabel)
    {
        // HashMap put: O(1)
        if(knoop.leftChild != null) 
        {
            code += "0";
            
            if(knoop.leftChild.character == null) 
            {
                aflezenCodes(knoop.leftChild, code, tabel);
            }
            else 
            {
                tabel.put(knoop.leftChild.character, code);
            }
            
            code = code.substring(0, code.length() - 1);
        }
        
        if(knoop.rightChild != null) 
        {
            code += "1";
            
            if(knoop.rightChild.character == null) 
            {
                aflezenCodes(knoop.rightChild, code, tabel);
            }
            else 
            {
                tabel.put(knoop.rightChild.character, code);
            }
            
            code = code.substring(0, code.length() - 1);
        }
    }
    
    // Stap 5
    public static String coderenBericht(HashMap<Character, String> tabel, String bericht)
    {
        // HashMap get: O(1)
        char[] chars = bericht.toCharArray();
        String gecodeerdBericht = "";
        
        for(char c : chars) 
        {
            gecodeerdBericht += tabel.get(c);
        }
        
        return gecodeerdBericht;
    }
    
    // Stap 6
    public static void decoderenBericht(HuffKnoop hoofdKnoop, HuffKnoop knoop, StringBuilder bericht, ArrayList<Character> gecodeerdBericht, int count) 
    {
        // ArrayList get: O(1), size: O(1)
        if(gecodeerdBericht.size() > count) 
        {
            Character nummer = gecodeerdBericht.get(count);
            
            if(nummer == '0') 
            {
                if(knoop.leftChild.character != null)
                {
                    bericht.append(knoop.leftChild.character);
                    count++;
                    decoderenBericht(hoofdKnoop, hoofdKnoop, bericht, gecodeerdBericht, count);
                }
                else 
                {
                    count++;
                    decoderenBericht(hoofdKnoop, knoop.leftChild, bericht, gecodeerdBericht, count);
                }
            }
            else if (nummer == '1') 
            {
                if(knoop.rightChild.character != null) 
                {
                    bericht.append(knoop.rightChild.character);
                    count++;
                    decoderenBericht(hoofdKnoop, hoofdKnoop, bericht, gecodeerdBericht, count);
                }
                else
                {
                    count++;
                    decoderenBericht(hoofdKnoop, knoop.rightChild, bericht, gecodeerdBericht, count);
                }
            }
        }
    }
}
