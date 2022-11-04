package singleton;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Random;

import javax.management.InstanceAlreadyExistsException;

public class MySingleton implements Serializable  {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static MySingleton obj = null;
    private int val;
    
    private MySingleton() throws InstanceAlreadyExistsException{
     if(obj != null){
      System.out.println("\nObject already created....");
      throw new InstanceAlreadyExistsException();
     }
 
     System.out.println("Inside Constructor..");
       val = new Random().nextInt();
       System.setSecurityManager(new SecurityManager());
    }

    public static MySingleton getInstance() throws InstanceAlreadyExistsException{
        if(obj == null){
            synchronized (MySingleton.class) {
                if(obj == null){
                    obj = new MySingleton();
                }
            }
        }
        return obj;
    }
    public int getVal(){
        return val;
    }
    public void setVal(int val){
     this.val = val;
    }
    
    @Override
    protected MySingleton clone() throws CloneNotSupportedException {
     // If user tries to clone the object then we are sending same class object
        try {
           return MySingleton.getInstance();
     } catch (InstanceAlreadyExistsException e) {
      // TODO Auto-generated catch block
       e.printStackTrace();
     }
     return null; 
    }
    
    private Object readResolve() throws ObjectStreamException, InstanceAlreadyExistsException {
     // We are blocking deserilizing object and sending same class object
      return MySingleton.getInstance();
  }
    
    public static void main(String[] args)  {
        try{
            MySingleton obj1 = MySingleton.getInstance();
            MySingleton obj2 = obj1.clone();
           
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("C:\\singleton.ser")); 
            oos.writeObject(obj1); 
            oos.close(); 
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("C:\\singleton.ser")); 
            MySingleton obj3 = (MySingleton) ois.readObject(); 
            ois.close(); 
           
            System.out.println("First Object      :: "+obj1.getVal());
            System.out.println("Clone Object      :: "+obj2.getVal());
            System.out.println("Serialized Object :: "+obj3.getVal()+"\n\n");
            
            // Changing value in Object 3
            obj3.setVal(100);
            
            System.out.println("First Object      :: "+obj1.getVal());
            System.out.println("Clone Object      :: "+obj2.getVal());
            System.out.println("Serialized Object :: "+obj3.getVal()+"\n\n");
            
            // Changing value in Object 3
            obj2.setVal(1234);
            
            System.out.println("First Object      :: "+obj1.getVal());
            System.out.println("Clone Object      :: "+obj2.getVal());
            System.out.println("Serialized Object :: "+obj3.getVal());
            
            Constructor constructor = MySingleton.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            MySingleton newInstance = (MySingleton) constructor.newInstance();
            
            System.out.println("NEW INSTANCE : "+newInstance.getVal());

        }catch (Exception e) {
           System.out.println(e.getCause());
        }
    }
}