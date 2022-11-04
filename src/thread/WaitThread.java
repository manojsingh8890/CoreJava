package thread;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class WaitThread {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//LinkedList l = new LinkedList();
	//	l.getFirst();
		Map<String, String>  set = new HashMap();
		
		set.put(new String("a"), "mmm");
		set.put(new String("a"), "mmmm");
		System.out.println(set);
		
		System.out.println(new String("a").hashCode() == new String("a").hashCode() );
		

	}

}
