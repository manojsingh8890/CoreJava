package java8;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RevertStrOnUppercaseLetter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String input = "JavaInterviewDemo";
		String[] str = input.split("(?=[A-Z])");
		
		 Arrays.stream(str).map(x -> "["+new StringBuilder(x).reverse()+"]")
		        			      .collect(Collectors.joining());
		
		 String namesFlatStream = Stream.of(str).map(x -> "["+new StringBuilder(x).reverse()+"]")
			      .collect(Collectors.joining());
		 
		 
		 
		/*for(int i = 0; i < str.length; i++) {
			s += "["+new StringBuilder().append(str[i]).reverse().toString()+"]";
		}*/
		System.out.println(namesFlatStream);
		
		
		char[] arr = input.toLowerCase().toCharArray();
		
		Stream<Character> stream = IntStream.range(0, arr.length-1).mapToObj(x -> (char)arr[x]);
		List<Character> wovel = Arrays.asList('a','e','i','o','u');
		stream.filter(x -> wovel.contains(x)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
		.forEach((x,y) -> System.out.println(x +" : " +y));
		
		
		int l = 2;
		Stream<Integer> evenNumStream = Stream.iterate(l,  t -> {
			System.out.println(t);
			return t <= 5;
			}, i -> i * 2);

	    List<Integer> collect = evenNumStream
	      //.limit(5)
	      .collect(Collectors.toList());
	    
	    System.out.println(collect);
	    
	    
		// Java 8: Creating an infinite Stream, limit is used to limit the size of the stream
        Stream<Integer> infiniteStream = Stream.iterate(1, n -> n + 1).limit(5);
        System.out.println("Infinite Stream with limit:");
        infiniteStream.forEach(System.out::println);

        // Java 9: Creating a finite Stream using a hasNext predicate
        int o = 0;
        Stream<Integer> finiteStream = Stream.iterate(o, n -> n <= 5, n -> n + 1);
        System.out.println("Finite Stream:");
        finiteStream.forEach(System.out::println);
	}

}
