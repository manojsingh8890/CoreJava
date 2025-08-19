package arrays;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SortZeroinOneLoop {

	public static void main(String arg[]) {
		
		int arr[] = { 34, 0, 9, 5, 7, 4, 0, 4, 0, 23 };
		int i = 0;
		int j = arr.length - 1;
		while (i < j) {
			if(arr[j] == 0 && arr[i] != 0) {
				int temp = arr[j];
				arr[j] = arr[i];
				arr[i] = temp;
				i++;
				j--;
			}if(arr[j] == 0 && arr[i] == 0) {
				i++;
				int temp = arr[j];
				arr[j] = arr[i];
				arr[i] = temp;
				i++;
				j--;
			}else {
				j--;
			}
		}
		
		for(i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}
		
		
		List<String> list = Arrays.asList("apple", "bat", "banana", "cat", "dog", "orange");
	    Map<Integer, List<String>> map	= list.stream().collect(Collectors.groupingBy(x -> x.length(), Collectors.mapping(x -> x, Collectors.toList())));
	   map.forEach((x, y) -> System.out.println("value :" + y));
	}

}
