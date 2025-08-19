package collection;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

class scoreComprator<K, V extends Comparable<V>> implements Comparator<Map.Entry<K, V>>{

	@Override
	public int compare(Entry<K, V> o1, Entry<K, V> o2) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}

public class MapEntryComparator {
	public static void main(String arg[]) {
		HashMap<String, Integer> mapcon = new HashMap<>();   
        mapcon.put("k1", 100);   
        mapcon.put("k2", 200);   
        mapcon.put("k3", 300);   
        mapcon.put("k4", 400);   
    
        System.out.println("HashMap values :\n " + mapcon.toString());   
   
        mapcon.computeIfPresent("k4", (key , val)  -> {
    System.out.println("Key : " + key + " Value: " + val);
        	return   val + 100;
        
	});   
         
        mapcon.computeIfAbsent("k5", (key)  -> {
           System.out.println("Key : " + key );
        	return   100;
        
	}); 
	}
	

}
