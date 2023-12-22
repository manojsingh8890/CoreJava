package java8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class EmployeeD {
	
	private int id ;
	private String name;
    private String department;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
    
    
}

public class ListOfObjectToMap {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<EmployeeD> list = new ArrayList<>();
		EmployeeD e = new EmployeeD();
		e.setId(1);
		e.setName("manoj");
		e.setDepartment("es");
		list.add(e);
		e = new EmployeeD();
		e.setId(2);
		e.setName("prabhakar");
		e.setDepartment("es");
		list.add(e);
		
		e = new EmployeeD();
		e.setId(3);
		e.setName("prabhakar");
		e.setDepartment("es");
		list.add(e);
		
		e = new EmployeeD();
		e.setId(1);
		e.setName("prabhakar");
		e.setDepartment("hr");
		list.add(e);
		
		
		/*Map<String,List<EmployeeD>> m =list.stream().collect(Collectors.toMap(x -> x.getDepartment(), x -> {
			List<EmployeeD> l = new ArrayList();
			l.add(x);
			return l;
		}, 
	    (x, y) -> {
	    	 x.add(y.get(0));
	    	 y= null;
	    	 return x;
	    }));
		
		
		m.forEach((x, y) -> System.out.println(x +" , "+y.size()));*/
		
		Map<String,List<EmployeeD>> m2 = list.stream().collect(Collectors.groupingBy(x -> x.getDepartment(),Collectors.mapping(x -> x, Collectors.toList())));
		
		m2.forEach((x, y) -> System.out.println(x +" , "+y.size()));
	}

}
