package java8;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WordFrequencyCounter {
    public static Map<String, Long> countWordFrequency(List<String> words) {
        return words.stream()
                    .flatMap(line -> Arrays.stream(line.split("\\s+")))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public static void main(String[] args) {
        List<String> words = List.of("apple banana apple", "banana cherry", "apple banana cherry");
        Map<String, Long> frequencyMap = countWordFrequency(words);
        System.out.println("Word frequency: " + frequencyMap);
        
      
    }
}
