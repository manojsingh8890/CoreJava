package java8;

//Java code to demonstrate 
//ParallelStreams 

import java.io.*; 
import java.util.*; 
import java.util.stream.*; 

public class ParallelStreamExample { 
 public static void main(String[] args) 
 { 
     // create a list 
     List<String> list = Arrays.asList("Hello ",  
                      "G", "E", "E", "K", "S!"); 

     // using parallelStream()  
     // method for parallel stream 
     list.parallelStream().forEach(System.out::print); 
     System.out.println();
     
     list.parallelStream().forEachOrdered(System.out::print); 
 } 
}