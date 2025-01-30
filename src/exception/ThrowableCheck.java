package exception;

import java.io.IOException;

public class ThrowableCheck{  
	  void m() throws Throwable{  
	    throw new Throwable("device error");//checked exception  
	  }  
	  void n() throws Throwable{  
	    m();  
	  }  
	  void p(){  
	   try{  
	    n();  
	   }catch(Throwable e){System.out.println("exception handeled");}  
	  }  
	  public static void main(String args[]){  
	   TestExceptionPropagation2 obj=new TestExceptionPropagation2();  
	   obj.p();  
	   System.out.println("normal flow");  
	  }  
	}  