package java8;

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
		 List<Employee45> empList = null ;
		// TODO Auto-generated method stub
		// Map<Character, List<Employee>> groupByAlphabet = empList.stream().collect(
			//      Collectors.groupingBy(e -> new Character(e.getName().charAt(0))));
		 
		 
		 // Get EmpId list 
		 Map<Character, List<Integer>> idGroupedByAlphabet = empList.stream().collect(
			      Collectors.groupingBy(e -> new Character(e.getEmployeeName().charAt(0)),
			        Collectors.mapping(Employee45::getEmployeeId, Collectors.toList())));
		 
		 // Get salary list 
		 Map<String, List<Double>> mappedValue =  empList.stream().collect(Collectors.groupingBy(x -> x.department, Collectors.mapping(x -> x.salary, Collectors.toList())));
		 mappedValue.forEach((x, y) -> System.out.println (x + "," + y.stream().max((x1,y1) -> x1 > y1 ? 1 : -1).get()));

		 // Get employee list 
		 Map<String, List<Employee45>> mappedValue1 =  empList.stream().collect(Collectors.groupingBy(x -> x.department, Collectors.mapping(x -> x, Collectors.toList())));
			

	}

}
