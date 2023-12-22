package java8;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NumberOfOccureance {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		 List<String> names = Arrays.asList("AA", "BB", "AA", "CC", "AB", "BB");
			/*   Map<String, Integer> mapValue = names.stream().collect
			    (Collectors.toMap(x -> x, x -> {return 1;},
			    (x, y) ->{
			        return x+y;
			    },
			    LinkedHashMap::new
			    )); */
			   
			   
			   Map<String, Long> counts =
					   names.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
			   counts.forEach((x, y) -> System.out.println(x +""+ y));
			   
		/*	// TODO Auto-generated method stub
				 Map<Character, List<Employee>> groupByAlphabet = empList.stream().collect(
					      Collectors.groupingBy(e -> new Character(e.getName().charAt(0))));
				 
				 
				 Map<Character, List<Integer>> idGroupedByAlphabet = empList.stream().collect(
					      Collectors.groupingBy(e -> new Character(e.getName().charAt(0)),
					        Collectors.mapping(Employee::getId, Collectors.toList()))); */

	}

}
