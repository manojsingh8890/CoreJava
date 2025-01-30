package corejavaPro;

import java.util.BitSet;  

public class BitSetFlipExample1 {  
    public static void main(String[] args) {  
        BitSet bitset = new BitSet();  
        bitset.set(0);  
        bitset.set(1);  
        bitset.set(2);  
        bitset.set(3);  
        System.out.println("bitset: "+bitset);  
        System.out.println("bitset value: "+bitset.get(0)+" "+bitset.get(1)+" "+bitset.get(2)+" "+bitset.get(3));  
        bitset.flip(1);  
        //printing bitset after flip index 1  
        System.out.println("bitset after flip index 1: "+bitset);  
        System.out.println("bitset value after flip index 1: "+bitset.get(0)+" "+bitset.get(1)+" "+bitset.get(2)+"  "+bitset.get(3));  
    }  
}  