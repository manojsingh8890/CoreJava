package java8;

//Java Program to illustrate Collecting a Stream to
//an Immutable Collection
//Post java 10
//using toUnmodifiableList() method 

//Importing classes from java.util package
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//Main Class
//ImmutableCollectionList
public class ToUnmodifiableJava10 {

 // Main driver method
 public static void main(String[] args)
 {
     // Creating Stream class object of integer type
     Stream<Integer> evenNumberStream
         = Stream.iterate(0, i -> i + 2).limit(50);

     // Creating List class object of integer type
 //1 List
     List<Integer> evenNumbers
         = (List<Integer>)evenNumberStream.collect(
             Collectors.toUnmodifiableList());
 //2 Map  
     
  /*   // Declaring object of integer and string type
     Map<Integer, String> unmutableInventory
         = libInventory.stream().collect(
             Collectors.toUnmodifiableMap(
                 Books::getBookNumber,
                 Books::getBookName)); */
 //3 Set    
    /* // Now creating Set class object of type Double
     Set<Double> randomSet = randomDecimals.collect(
         Collectors.toUnmodifiableSet());*/

     // Print all elements in the List object
     System.out.println(evenNumbers);

     // These will result in
     // java.lang.UnsupportedOperationException

     evenNumbers.add(90);
     // evenNumbers.remove(1);
 }
}