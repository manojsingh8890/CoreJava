package java8;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlatMap {

	public static void main(String[] args) {
		    List<List<String>> namesNested = Arrays.asList( 
		      Arrays.asList("Jeff", "Bezos"), 
		      Arrays.asList("Bill", "Gates"), 
		      Arrays.asList("Mark", "Zuckerberg"));

		    String namesFlatStream = namesNested.stream()
		      .flatMap(x -> x.stream()).map(x -> "["+new StringBuilder(x).reverse()+"]")
		      .collect(Collectors.joining());
		    
		    System.out.println(namesFlatStream);
		}

}
