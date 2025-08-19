package java8;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StringCharCountc {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str  = "ManojSingh";
		char[] ch =str.toCharArray();
		List<Character> myStreamOfCharacters = IntStream
		          .range(0, ch.length)
		          .mapToObj(i -> ch[i]).collect(Collectors.toList());
		
		List<Character> myStreamOfCharacters1 = str.chars().mapToObj(x -> (char)x).collect(Collectors.toList());
		
		  int a[] = { 3, 6, 32, 1, 8, 5, 31, 22 };
		  int sum = Arrays.stream(a).boxed().reduce(0, (x, y) -> x + y).intValue();
		  System.out.println(sum);
		  
		  int am[] = { 2, 3, 1, 22, 11, 33, 5 };

		// Find the max number
		  int max = Arrays.stream(am).boxed().max((x, y) -> x.compareTo(y)).get();
		// Find the max number
		  int max8 = Arrays.stream(am).boxed().max(Integer::compare).get();
		
		/*Map<Character, Integer> map = myStreamOfCharacters.collect(Collectors.toMap(x -> x, x -> {
										return 1;
									  },
								       (x, y) ->{
											x = x+y;
											return x;
									   }
								      , LinkedHashMap::new));
		
									
		map.forEach((x, y) -> System.out.println(x +","+ y));
		*/
	/*	Map<Character, Long> counts =
				 myStreamOfCharacters.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
		   counts.forEach((x, y) -> System.out.println(x +""+ y));
		   */
		   
		  /* Map<Character, Long> counts33 =
					 myStreamOfCharacters.stream().filter(x -> Collections.frequency(myStreamOfCharacters, x) == 1).collect(Collectors.groupingBy(e -> e, Collectors.counting()));
					 counts33.forEach((x, y) -> System.out.println(x +""+ y));
		   */
		   
		/*   Character ch1  =
					 myStreamOfCharacters.stream().filter(x -> Collections.frequency(myStreamOfCharacters, x) == 1).collect(Collectors.groupingBy(e -> e, Collectors.counting()))
		   .entrySet().stream().map(x -> x.getKey()).findFirst().get();
		   
		  
		System.out.println(ch1);
		*/
		/*   String input = "Java Stream API is very good concept";

		  // char firstNotRepetedChar = input.chars().mapToObj(x -> Character.toUpperCase((char) x))//converting the object format
		   char firstNotRepetedChar =  myStreamOfCharacters.stream()  .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))// find duplicate freq in linkedHashMap
		     .entrySet().stream().filter(x -> x.getValue() == 1L).map(x -> x.getKey()).findFirst().get();//filtering the freq which is not first time

		   System.out.println("First non repeated char  : " + firstNotRepetedChar);
		*/
	}

}
