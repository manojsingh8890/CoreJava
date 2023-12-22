package java8;



interface a1 {
	default void foo(){
		System.out.println("a");
	}
}

interface b1 extends a1{
	default void foo(){
		System.out.println("b");
	}
}

public class CallInterfaceDefaultMethod implements b1{
	
	public static void main(String arg[]) {
		
		a1 a = new a1() {};
		a.foo();
		
		new CallInterfaceDefaultMethod().foo();
		
	}
	
	
}
