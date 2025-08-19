package reverse;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ReverseWordOnsamePlace {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String[] x4 = "India is my %^& Country".split("(?=[\\s+])");
		
		for(int i = 0; i < x4.length; i++) {
			x4[i] = new StringBuilder(x4[i]).reverse().toString();
		}
		String nn= Arrays.stream(x4).collect(Collectors.joining());
		System.out.println("India is my %^& Country");
		System.out.println(nn);
		
		char[] str = "  India is my %^& Country ".toCharArray();
		int i = 0;
		int j = 0;
		while(j != str.length) {
			if(!Character.isAlphabetic(str[j])) {
				if(i == j) {
				  i++;
				  j++;
				}else {
				  reverse(str, i, j);
				  i=j;
				}
			}else{
				j++;
			}
			
		}
		reverse(str, i, j);
       System.out.println(new String(str));
		
	}
	
	public static char[] reverse(char[] raw, int first, int last) {
		last--;
		while(first < last )
        {
            // swap values at `l` and `h`
            char temp = raw[first];
            raw[first] = raw[last];
            raw[last] = temp;
            first ++;
            last--;
        }
		
		return raw;
	}

}
