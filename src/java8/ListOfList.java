package java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class EmployeeNew{
	String name;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	List<Address> zyz = new ArrayList<>();

	public List<Address> getZyz() {
		return zyz;
	}

	public void setZyz(List<Address> zyz) {
		this.zyz = zyz;
	}
	
	
	
}

class Address{
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
public class ListOfList {
	
	public static void main(String args[]) {
		Address address = new Address();
		address.setName("ab");
		
		Address address2 = new Address();
		address2.setName("xx");
		
		List<Address> list1 = new ArrayList<>();
		list1.add(address2);
		list1.add(address);
		
		List<EmployeeNew> list = new ArrayList<>();
		EmployeeNew emp = new EmployeeNew();
		emp.setName("ppp");
		emp.setZyz(list1);
		list.add(emp);
		EmployeeNew emp1 = new EmployeeNew();
		emp1.setName("ttt");
		emp1.setZyz(list1);
		list.add(emp1);
		
		
	    List<String> bb =list.stream().flatMap(x -> x.getZyz().stream().map(y -> y.getName())).collect(Collectors.toList());
	    bb.forEach(System.out::println);
	    
	    /*
	    		
	    Map<String, List<String>> bbb =	list.stream().collect(Collectors.groupingBy(x -> x.getName(), Collectors.mapping(y -> y.getZyz().stream().map(z -> z.getName()).collect(Collectors.joining(":")), Collectors.toList())));
	    
	    bbb.forEach((x,y) -> 
	    {
	    System.out.println("Employee :" + x);
	    
	    y.stream().forEach(System.out::println);
	    });
	    */
	    List<Integer> integerList = Arrays.asList(1,2,3,8,10);
	    System.out.println(integerList.stream().sorted(Comparator.reverseOrder()).skip(1).findFirst().get());
	 
		
	}

}
