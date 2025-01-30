package corejavaPro;


import java.sql.Timestamp;
import java.time.Instant;  
public class JavaTimestampFromExample1 {  
    public static void main(String[] args) {  
       Timestamp instant= Timestamp.from(Instant.now());  
        System.out.println("from() method will return "+instant);  
        Timestamp ts1 = Timestamp.valueOf("2018-09-01 09:01:16");  
        Timestamp ts2 = Timestamp.valueOf("2018-09-01 09:01:16");  
        if(ts1.equals(ts2)){  
            System.out.println("Both values are equal");  
        }  
        else{  
            System.out.println("Both values are unequal.");  
        }  
    }  
}  