package java8;

@FunctionalInterface
interface Printable {
	default void show() {
		System.out.println("mm3");
	}
	
   void show1() ;
}

@FunctionalInterface
interface Showable {
	default void show() {
		System.out.println("mm1");
	} 
	 void show1() ;
}

public class InterfacePoly implements Printable, Showable {
	

	public static void main(String args[]) {
		Showable obj = new InterfacePoly();
		//obj.print();
		obj.show();
	}


	@Override
	public void show() {
		// TODO Auto-generated method stub
		Printable.super.show();
	}


	@Override
	public void show1() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}