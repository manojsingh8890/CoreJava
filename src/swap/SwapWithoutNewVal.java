package swap;

public class SwapWithoutNewVal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int x= 5;
		int y= 7;
		System.out.println(x +" - "+y);
		x = x+y;
		y = x-y;
		x= x-y;
		System.out.println(x +" - "+y);

	}

}
