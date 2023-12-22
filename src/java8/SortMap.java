package java8;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class Employee{
	String name;
	int id;
	Employee(int id, String name){
		this.name = name;
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}

public class SortMap {
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<Employee, String> map = new HashMap<>();
		Employee emp = new Employee(1, "Nivedita");
		Employee emp1 = new Employee(5, "m");
		Employee emp2 = new Employee(9, "p");
		Employee emp3 = new Employee(6, "l");
		map.put(emp3, "8");
		map.put(emp, "8");
		map.put(emp1, "8");
		map.put(emp2, "8");
		Map<Employee, String> map4 = map.entrySet().stream().sorted(Entry.comparingByKey((x, x1) -> x.getId() - x1.getId()))
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue(), (oldValue, newValue) -> oldValue, 
			 LinkedHashMap::new
		));
		
		Map<Employee, String> map5 = map.entrySet().stream().sorted(Entry.comparingByKey((x, x1) -> x.getId() - x1.getId()))
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
		
		
		Map<Employee, String> map7 = map.entrySet().stream().sorted(Entry.comparingByKey((x, x1) -> {
			if(x.getId() == x1.getId()) {
				return 0;
			} else if(x.getId() < x1.getId()) {
				return 1;
			}else {
				return -1;
			}
		})).collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue(), (oldValue, newValue) -> oldValue, () -> {
			return new LinkedHashMap<>();
		}));
		
		

		map4.entrySet().forEach(x -> System.out.print(x.getKey().getId()));
		System.out.println();
		map7.entrySet().forEach(x -> System.out.print(x.getKey().getId()));
	}

}
