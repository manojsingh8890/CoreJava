package java8;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DiffBtwenArraysstreamStreamOf {

	public static void main(String args[]) {
		List<Integer> myList = Arrays.asList(10, 15, 8, 49, 25, 98, 98, 32, 15);
		long count = myList.stream().count();
		System.out.println(count);

		/* or can also try below line code */
		// When numbers are given as Array 
		 int[] arr = {10,15,8,49,25,98,98,32,15}; 
		//Stream<Integer>
		IntStream.range(0, arr.length).mapToObj(x -> arr[x]);
		// IntStrem
		Arrays.stream(arr);
		//Stream<Integer>
		Arrays.stream(arr).boxed();
		//Stream<int[]>
		Stream.of(arr);
		
		String nameStr = "Manoj";
		char[] name =nameStr.toCharArray();
		//Stream<Integer>
		IntStream.range(0, name.length).mapToObj(x -> (char)name[x]);
		//Stream<Character>
		nameStr.chars().mapToObj(x -> (char)x);
		
		//Stream<char[]>
		Stream.of(name);
		
		
		String input = "Java articles are Awesome";
	    
	    Character result = input.chars() // Stream of String       
	            .mapToObj(s -> Character.toLowerCase((char) s)) // First convert to Character object and then to lowercase         
	            .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting())) //Store the chars in map with count 
	            .entrySet()
	            .stream()
	            .filter(entry -> entry.getValue() == 1L)
	            .map(entry -> entry.getKey())
	            .findFirst()
	            .get();
	    System.out.println(result); 
	    
	    Set<Integer> s = new HashSet<>();
	    int[] arr1 = {10,15, 15};
	    int sb =  Arrays.stream(arr1).boxed().filter(x -> ! s.add(x)).collect(Collectors.toList())
	    .size();
	   
	   if(sb > 0) {
		   System.out.println("true");
	   }else {
		   System.out.println("false");
	   }
	   
	   
	   int num = 8;
	  
	   IntStream.range(2, 5).forEach(System.out::println);
	 
	   
	   boolean isPrime = IntStream.range(2, num)
               .noneMatch(x ->{
            	   System.out.println("x : " + x);
            	   return num % x == 0;
            	 }
               );
	   
	   System.out.println(isPrime);
	   
	   //Armstrong:
	   
	   int num3 = 153;
	   
	   int sum = String.valueOf(num3).chars().map(x -> Character.getNumericValue(x)).map(digit -> (digit*digit*digit)).sum();
	   
	   System.out.println("sum : "+ sum);
	   
	  int b =  16461;
	   
	String bn =   new StringBuilder(String.valueOf(b)).reverse().chars().map(Character::getNumericValue).boxed().map(x -> ""+x).collect(Collectors.joining());
	
	System.out.println("bn : " + bn);
	
	String namex = "Manoj";
	Arrays.stream(String.valueOf(namex).split("")).forEach(System.out::println);
	  
	 Stream.iterate(new int[] {0, 1}, f -> new int[] {f[1], f[0]+f[1]})
     .limit(10)
     .map(f -> f[0])
     .forEach(i -> System.out.print(i+" "));
	   
	}

}
