package java8;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class findNthBasedFrequency {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Integer> arr = Arrays.asList(1,1,2,3,3,3,4,4,4);
		Map.Entry<Integer,Long> a = arr.stream().collect(Collectors.groupingBy(Function.identity() , Collectors.counting()))
			      .entrySet().stream()
			      .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
			     // .map(Map.Entry::getKey)
			      .skip(2).findFirst().get();
		System.out.println(a.getKey() +" : "+ a.getValue()); 
		 System.out.println(a);
	}
}
