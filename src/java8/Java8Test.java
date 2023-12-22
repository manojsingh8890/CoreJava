package java8;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Java8Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
		int arr[] = {100,89,76,200,100,34,56};
		List<Integer> list = IntStream.range(0, arr.length).mapToObj(x -> arr[x]).collect(Collectors.toList());
		/*List<Integer> list = Arrays.stream(arr)        
                .boxed()          // Stream<Integer>
                .collect(Collectors.toList());
                
                
		list = list.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
		
		System.out.println(list.get(2)); */
		
		/*String str= "I    am Manoj";
		String str1 = "";
	    List<String> list = new ArrayList<>();
	    String spclChar="";
	    int j = 0;
	    
 		for(int i= 0 ; i < str.length()-1 ; i ++) {
			if(!Character.isAlphabetic(str.charAt(i))) {
				spclChar += str.charAt(i);
			}else if(!spclChar.equals("")){
				list.add(spclChar);
				spclChar="";
			}
		}
		j = 0;
		
		StringTokenizer token = new StringTokenizer(str, " ");
		while(token.hasMoreElements()) {
			String strWord = token.nextToken();
			str1 += reverse(strWord);
			if(list.size() > j) {
			  str1 += list.get(j);
			  j++;
			}
		}
		
		System.out.println(str1); 

	}
	
	 static String reverse(String str) {
	     String revStr = "";
		for(int i= str.length()-1 ; i >= 0 ; i --) {
			revStr += str.charAt(i); 
		}
		return revStr;
	} */
		System.out.print(2.0-1.1);
		System.out.print(", ");
		System.out.print(2.0f-1.1f);
	}
}
