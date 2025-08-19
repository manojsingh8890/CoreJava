package collection;

//important import statement  
import java.util.*;

class Employee implements Comparable<Employee> {

	int empId;
	String name;

//getting the name of the employee  
	String getName() {
		return this.name;
	}

//setting the name of the employee  
	void setName(String name) {
		this.name = name;
	}

//setting the employee id   
//of the employee  
	void setId(int a) {
		this.empId = a;
	}

//retrieving the employee id of  
//the employee  
	int getId() {
		return this.empId;
	}

	@Override
	public int compareTo(Employee o) {
		// TODO Auto-generated method stub
		return 0;
		// return o.hashCode();
	}

}

public class TestTreeSet {

//main method  
	public static void main(String[] argvs) {
//creating objects of the class Employee  
		Employee obj1 = new Employee();

		Employee obj2 = new Employee();

		TreeSet<Employee> ts = new TreeSet<Employee>();

//adding the employee objects to   
//the TreeSet class  
		ts.add(obj1);
		ts.add(obj2);

		System.out.println("The program has been executed successfully." + ts.size());

	}
}