package java8;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StringCharCountc {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str  = "ManojSingh";
		char[] ch =str.toCharArray();
		Stream<Character> myStreamOfCharacters = IntStream
		          .range(0, ch.length)
		          .mapToObj(i -> ch[i]);
		
		Map<Character, Integer> map = myStreamOfCharacters.collect(Collectors.toMap(x -> x, x -> {
										return 1;
									  },
								       (x, y) ->{
											x = x+y;
											return x;
									   }
								      , LinkedHashMap::new));
		
									
		map.forEach((x, y) -> System.out.println(x +","+ y));
		
		
		
		
	}

}
