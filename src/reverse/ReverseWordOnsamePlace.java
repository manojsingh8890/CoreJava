package reverse;

public class ReverseWordOnsamePlace {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
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
	
	public static char[] reverse(char[] raw, int first, int i) {
		
		for (int l = first, h = i-1; l < h; l++, h--)
        {
            // swap values at `l` and `h`
            char temp = raw[l];
            raw[l] = raw[h];
            raw[h] = temp;
        }
		
		return raw;
	}

}
