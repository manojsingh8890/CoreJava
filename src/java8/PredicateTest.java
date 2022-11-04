package java8;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PredicateTest {
	
	public static void main(String arg[]) {
		{
	        // Creating predicate
	        Predicate<Integer> lesserthan = i -> (i < 18); 
	  
	        // Calling Predicate method
	        System.out.println(lesserthan.test(10)); 
	    }
	}

}
