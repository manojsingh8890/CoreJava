package java8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


class Employee45{

String department;
String employeeName;
int employeeId;
Double salary;
public String getDepartment() {
	return department;
}
public void setDepartment(String department) {
	this.department = department;
}
public String getEmployeeName() {
	return employeeName;
}
public void setEmployeeName(String employeeName) {
	this.employeeName = employeeName;
}
public int getEmployeeId() {
	return employeeId;
}
public void setEmployeeId(int employeeId) {
	this.employeeId = employeeId;
}
public Double getSalary() {
	return salary;
}
public void setSalary(Double salary) {
	this.salary = salary;
}

}


public class Grouping {

	public static void main(String[] args) {
		 List<Employee45> empList = new ArrayList<>() ;
		 Employee45 emp = new Employee45();
		 emp.setDepartment("bb");
		 emp.setSalary(20d);
		 empList.add(emp);
		 Employee45 emp1 = new Employee45();
		 emp1.setDepartment("bb");
		 emp1.setSalary(21d);
		 empList.add(emp1);
		 Employee45 emp2 = new Employee45();
		 emp2.setDepartment("cc");
		 emp2.setSalary(30d);
		 empList.add(emp2);
		 Employee45 emp3 = new Employee45();
		 emp3.setDepartment("cc");
		 emp3.setSalary(31d);
		 empList.add(emp3);
		 
		 
		 
		// TODO Auto-generated method stub
		// Map<Character, List<Employee>> groupByAlphabet = empList.stream().collect(
			//      Collectors.groupingBy(e -> new Character(e.getName().charAt(0))));
		 
		 
		 // Get EmpId list 
	/*	 Map<Character, List<Integer>> idGroupedByAlphabet = empList.stream().collect(
			      Collectors.groupingBy(e -> new Character(e.getEmployeeName().charAt(0)),
			        Collectors.mapping(Employee45::getEmployeeId, Collectors.toList())));
		*/ 
		 // Get salary list 
		 Map<String, List<Double>> mappedValue =  empList.stream().collect(Collectors.groupingBy(x -> x.department, Collectors.mapping(x -> x.salary, Collectors.toList())));
		 mappedValue.forEach((x, y) -> System.out.println (x + "," + y.stream().max(Double::compare).get()));
		 
		 Map<String, List<Double>> mappedValue7 =  empList.stream().collect(Collectors.groupingBy(x -> x.department, Collectors.mapping(x -> x.salary, Collectors.toList())));
		 mappedValue.forEach((x, y) -> System.out.println (x + "," + y.stream().mapToDouble( u -> u.doubleValue()).average()));
		 mappedValue.entrySet().stream().collect(Collectors.toMap(x -> x.getKey(), y -> y.getValue().stream().collect(Collectors.averagingDouble(x -> x))));
		 
		 //Map<String, Double> max =
		 mappedValue.entrySet().stream().map(x -> x.getValue().stream().max((x1,y1) -> x1 > y1 ? 1 : -1).get())
				 .forEach(x -> System.out.println(x));
		 
		 Map<String, Double> maxd =  mappedValue.entrySet().stream().collect(Collectors.toMap(x -> x.getKey(), y -> y.getValue().stream().max((x1,y1) -> x1 > y1 ? 1 : -1).get()));
		 maxd.forEach((x, y) -> System.out.println (x + "," + y));
		 //map(x -> x.getValue().stream().max((x1,y1) -> x1 > y1 ? 1 : -1).get())
		 //.forEach(x -> System.out.println(x));

		 // Get employee list 
		 Map<String, List<Employee45>> mappedValue1 =  empList.stream().collect(Collectors.groupingBy(x -> x.department, Collectors.mapping(x -> x, Collectors.toList())));
		
		 
		// Get employee list 
				 Map<String, List<Employee45>> mappedValue2 =  empList.stream().collect(Collectors.groupingBy(x -> x.department));
				

	}

}
