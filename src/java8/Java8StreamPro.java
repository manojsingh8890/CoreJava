package java8;

//Java Program to Compare Streams to Loops 

//Importing required libraries
import java.io.IOException;
import java.lang.String;
import java.nio.file.*;
import java.util.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.*;

//Main class
//JavaStreams
public class Java8StreamPro {

 // Main driver method
 public static void main(String[] args)
     throws IOException
 {

     // 1. Integer Stream
     System.out.println("Integer Stream : ");
     IntStream.range(1, 10).forEach(System.out::print);

     // New line
     System.out.println();

     // 2. Integer Stream with skip
     System.out.println("Integer Stream with skip : ");
     IntStream.range(1, 10).skip(5).forEach(
         x -> System.out.println(x));

     // New line
     System.out.println();

     // 3. Integer Stream with sum
     System.out.println("Integer Stream with sum : ");
     System.out.println(IntStream.range(1, 5).sum());

     // New line
     System.out.println();

     // 4. Stream.of, sorted and findFirst
     System.out.println(
         "Stream.of, sorted and findFirst : ");
     Stream.of("Java ", "Scala ", "Ruby ")
         .sorted()
         .findFirst()
         .ifPresent(System.out::println);

     // New line
     System.out.println();

     // 5. Stream from Array, sort, filter and print
     String[] names = { "AI",        "Matlab",
                        "Scikit",    "TensorFlow",
                        "OpenCV",    "DeepLearning",
                        "NLP",       "NeuralNetworks",
                        "Regression" };
     System.out.println(
         "Stream from Array, sort, filter and print : ");
     Arrays
         .stream(names) // same as Stream.of(names)
         .filter(x -> x.startsWith("S"))
         .sorted()
         .forEach(System.out::println);

     // New line
     System.out.println();

     // 6. average of squares of an int array
     System.out.println(
         "Average of squares of an int array : ");
     Arrays.stream(new int[] { 2, 4, 6, 8, 10 })
         .map(x -> x * x)
         .average()
         .ifPresent(System.out::println);

     // New line
     System.out.println();

     // 7. Stream from List, filter and print

     // Display message only
     System.out.println(
         "Stream from List, filter and print : ");

     List<String> people = Arrays.asList(
         "AI", "Matlab", "Scikit", "TensorFlow",
         "OpenCV", "DeepLearning", "NLP",
         "NeuralNetworks");
     people.stream()
         .map(String::toLowerCase)
         .filter(x -> x.startsWith("a"))
         .forEach(System.out::println);

     // New line
     System.out.println();

     // 8. Reduction - sum

     // Display message only
     System.out.println("Reduction - sum : ");

     double total
         = Stream.of(7.3, 1.5, 4.8)
               .reduce(0.0, (a,  b) -> a + b);

     // Print and display
     System.out.println("Total = " + total);

     System.out.println();

     // 9. Reduction - summary statistics
     System.out.println(
         "Reduction - summary statistics : ");
     IntSummaryStatistics summary
         = IntStream.of(7, 2, 19, 88, 73, 4, 10)
               .summaryStatistics();

     // Print and display
     System.out.println(summary);

     System.out.println();
 }
}