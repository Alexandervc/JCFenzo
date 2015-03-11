/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffmancodering;

import java.util.Comparator;

/**
 *
 * @author Alexander
 */
public class HuffKnoop implements Comparable<HuffKnoop> {
    public Character character;
    public int frequentie;
    public HuffKnoop leftChild, rightChild;
    
    public HuffKnoop(Character character, int frequentie) 
    {
        this.character = character;
        this.frequentie = frequentie;
    }
    
    public HuffKnoop(int frequentie, HuffKnoop leftChild, HuffKnoop rightChild) 
    {
        this.frequentie = frequentie;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    @Override
    public int compareTo(HuffKnoop o) {
        return Integer.compare(this.frequentie, o.frequentie);
    }
    
    @Override
    public String toString() {
        return character + ": " + frequentie;
    }
}
