package java8;

//Java program to demonstrate the case
//when two interfaces are overridden

//Creating Interface One
interface GfGa{
public default void display()
{
   System.out.println("GEEKSFORGEEKS");
}
}

//Creating Interface Two
interface gfg{

public default void display() 
{
   System.out.println("geeksforgeeks");
}
}

public class InterfaceDuplicateDefaultMethodExample implements GfGa,gfg {

//Interfaces are Overrided
public void display() {

	GfGa.super.display();
   
   gfg.super.display();
}

public static void main(String args[]) {
	InterfaceDuplicateDefaultMethodExample obj = new InterfaceDuplicateDefaultMethodExample();
   obj.display();
}
}