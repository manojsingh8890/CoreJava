package arrays;

public class CorJAva {
	
  public static void main(String arg[]) {
	  new CorJAva().a(null);
  }
  
  public void a(Object a) {
	  System.out.println("Object");
  }
  
  
  public void a(String b) {
	  System.out.println("String");
  }
  
  public void b(float[] a) {
	  System.out.println("arr in");
  }
  
  
  public void b(int... bn) {
	  
	  System.out.println("String");
  }
  
 /* public void a(StringBuilder b) {
	  System.out.println("String");
  }*/

}
