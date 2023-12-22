package exception;

public class ReturnInTryAndFinally {

	public int getDetails() {
		
       int a = 0;
		
		try {
			return 8;
		}catch(Exception e) {
		   return 0;
		}finally {
			return 3;
		}
	 // return 7;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new ReturnInTryAndFinally().getDetails());

	}

}