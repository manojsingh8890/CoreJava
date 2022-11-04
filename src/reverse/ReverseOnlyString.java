package reverse;

//Java code to illustrate how to reverse an array without
//affecting special characters.
import java.util.*;

public class ReverseOnlyString {
	
	
	public static void reverse(char[] raw) {
		char[] temp = new char[raw.length];
		int x = 0;
		for(int i = 0 ; i < raw.length; i++) {
			if(Character.isAlphabetic(raw[i])) {
				temp[x] = raw[i];
				x++;
			}
		}
		
		x = x-1;
		for(int i = 0 ; i < raw.length; i++) {
			if(Character.isAlphabetic(raw[i])) {
				raw[i] = temp[x];
				x--;
			}
		} 
		
		String str = new String(raw);
		System.out.println(str);
	}

	// Driver Code
	public static void main(String[] args)
	{
		String str = "Manoj Prabh";
		char[] charArray = str.toCharArray();
		reverse(charArray);
	}
}

//This code is contributed by Aarti_Rathi
