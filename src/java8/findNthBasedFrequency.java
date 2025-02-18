package java8;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public class findNthBasedFrequency {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Integer> arr = Arrays.asList(0,0,1,1,2,2,2,3,3,3,4,4,4,4);
		
		long ab = arr.stream().collect(Collectors.groupingBy(Function.identity() , Collectors.counting()))
			      .entrySet().stream()
			      .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).map(x -> x.getValue()).distinct()
			      .skip(2).findFirst().get();
		System.out.println(ab);    
		
		/*
		 4,3,3,2
		 */
		
		
		arr.stream().collect(Collectors.groupingBy(Function.identity() , Collectors.counting()))
		.entrySet().stream().filter(x -> x.getValue() == ab)
		.peek(System.out::println)
		.sorted(Entry.comparingByKey(Comparator.reverseOrder()))
		.peek(s -> System.out.println("s : " + s))
		.count();
		
		
		Map.Entry<Integer,Long> xy = arr.stream().collect(Collectors.groupingBy(Function.identity() , Collectors.counting()))
				.entrySet().stream().filter(x -> x.getValue() == ab)
				.sorted(Entry.comparingByKey(Comparator.reverseOrder()))
				.findFirst().get();
		
		System.out.println(xy.getKey() +" : "+ xy.getValue()); 
		
		Map.Entry<Integer,Long> a = arr.stream().collect(Collectors.groupingBy(Function.identity() , Collectors.counting()))
			      .entrySet().stream()
			      .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
			     // .map(Map.Entry::getKey)
			      .skip(2).findFirst().get();
		System.out.println(a.getKey() +" : "+ a.getValue()); 
		 
		 
		 /* 4 -> 4
		    3 -> 3
		    2 -> 3
		    1 -> 2
		  */
	}
}
