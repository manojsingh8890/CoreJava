package java8;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DistinctCharacterFinder {
    public static Set<Character> findDistinctCharacters(List<String> strings) {
       /* return strings.stream()
                      .flatMapToInt(p -> p.chars())
                      .mapToObj(ch -> (char) ch)
                      .collect(Collectors.toSet());*/
        
       // OR
    	/* strings.stream().flatMap(x -> Arrays.stream(x.split(""))).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
         .entrySet().stream().filter(x -> x.getValue() == 1).collect(Collectors.toMap(x -> x.getKey(), y -> y.getValue()))
         .forEach((x, y) -> System.out.print(x +"::::::"+ y));*/
    	 
    	// OR
       
       return  strings.stream().flatMap(x -> x.chars().mapToObj(z -> (char)z)).collect(Collectors.toSet());
       
      // return  strings.stream().flatMap(x -> Arrays.stream(x.split(""))).collect(Collectors.toSet());
    }

    public static void main(String[] args) {
        List<String> strings = List.of("apple", "banana", "cherry");
        Set<Character> distinctChars = findDistinctCharacters(strings);
        System.out.println("Distinct characters: " + distinctChars);
    }
}
