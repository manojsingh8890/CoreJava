package java8;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NumberOfOccureance {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		 List<String> names = Arrays.asList("AA", "BB", "AA", "CC", "AB", "BB");
			
			   
			//   Map<String, Long> counts =
			//		   names.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
			 //  counts.forEach((x, y) -> System.out.println(x +""+ y));
			   
			//   Map<String, Long> counts =
					   names.stream().filter(x -> Collections.frequency(names, x) > 1).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
					   .forEach((x, y) -> System.out.println(x + "+" +y));
					   
					   // find last
					   System.out.println(names.stream().reduce((x, y) -> y).get());;
					  // or
					   System.out.println(names.stream().skip(names.size()-1).findFirst().get());;
					   
					  
		/*	// TODO Auto-generated method stub
				 Map<Character, List<Employee>> groupByAlphabet = empList.stream().collect(
					      Collectors.groupingBy(e -> new Character(e.getName().charAt(0))));
				 
				 
				 Map<Character, List<Integer>> idGroupedByAlphabet = empList.stream().collect(
					      Collectors.groupingBy(e -> new Character(e.getName().charAt(0)),
					        Collectors.mapping(Employee::getId, Collectors.toList()))); */

	}

}
