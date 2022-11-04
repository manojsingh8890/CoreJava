package memory;

import java.util.ArrayList;
import java.util.List;

public class MemoryLeaksDemo    
{   
public static void main(String[] args)    
{   
    int[] arr = new int[999999999];     //allocating memory to array  
    //System.out.println("OutOfMemoryError"); 
    new MemoryLeaksDemo().populateList();
}   

public static List<Double> list = new ArrayList<>();

public void populateList() {
    for (int i = 0; i < 10000000; i++) {
        list.add(Math.random());
    }
}

}