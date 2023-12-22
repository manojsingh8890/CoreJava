package java8;

interface sayable{
}  
@FunctionalInterface  
interface Doable extends sayable{  
    // Invalid '@FunctionalInterface' annotation; Doable is not a functional interface  
    void doIt();  
}  
public class FunctionalInterfaceTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
