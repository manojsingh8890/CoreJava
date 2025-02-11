package java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

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
		//Employee emp4 = new Employee(6, "a");
		Employee emp3 = new Employee(6, "x");
		
		map.put(emp3, "8");
		map.put(emp, "8");
		map.put(emp1, "8");
		map.put(emp2, "8");
		//map.put(emp4, "8");
		Map<Employee, String> map4 = map.entrySet().stream().sorted(Entry.comparingByKey((x, x1) -> ((Integer)x.getId()).compareTo((Integer)x1.getId())))
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue(), (oldValue, newValue) -> oldValue, 
			 LinkedHashMap::new
		));
		
		Map<Employee, String> map90 = map.entrySet().stream().sorted(Entry.comparingByKey((x, x1) -> ((Integer)x.getId()).compareTo((Integer)x1.getId())))
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue(), (oldValue, newValue) -> oldValue, 
			 LinkedHashMap::new
		));
	
		
		Map<Employee, String> map8 = map.entrySet().stream().sorted(Entry.comparingByKey((x, x1) -> x.getId()- x1.getId()))
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue(), (oldValue, newValue) -> oldValue, 
			 LinkedHashMap::new
		));
		
		
		
		Map<Employee, String> map5 = map.entrySet().stream().sorted(Entry.comparingByKey((x, x1) -> 
		{
			if(x.getId() == x1.getId()) {
				return x.name.compareTo(x1.getName());
				//return 0;
			} else if(x.getId() > x1.getId()) {
				return 1;
			}else {
				return -1;
			}
		}))
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue(), (oldValue, newValue) -> oldValue, 
			 LinkedHashMap::new
		));
	
		
		
		
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
		map5.entrySet().forEach(x -> System.out.println(x.getKey().getId() + " : "+ x.getKey().getName()));
		System.out.println();
		map8.entrySet().forEach(x -> System.out.print(x.getKey().getId()));
		//map5.entrySet().forEach(x -> System.out.print(x.getKey().getId()));
		//System.out.println();
		//map7.entrySet().forEach(x -> System.out.print(x.getKey().getId()));
		
		
		
		// ---------------------------------------
		// stream.iterate
	//	Stream.iterate(1,  i -> i+1).filter(i -> i%2 == 0).limit(5).forEach(System.out::println);
		
		List<Product> productsList = new ArrayList<Product>();  
        //Adding Products  
        productsList.add(new Product(1,"HP Laptop",25000f));  
        productsList.add(new Product(2,"Dell Laptop",30000f));  
        productsList.add(new Product(3,"Lenevo Laptop",28000f));  
        productsList.add(new Product(4,"Sony Laptop",28000f));  
        productsList.add(new Product(5,"Apple Laptop",90000f));  
        // This is more compact approach for filtering data  
        Float totalPrice = productsList.stream()  
                    .map(product->product.price)  
                    .reduce(0.0f,(sum, price)->sum+price);   // accumulating price 
        
        productsList.stream().map(x -> x.price).reduce(0.0F, (sum, price) -> sum+price);
        //OR
        double totalPrice3 = productsList.stream()  
                .collect(Collectors.summingDouble(product->product.price));  
      //  System.out.println(totalPrice);  
        
        // Stream.toArray
      //  Employee[] employees = empList.stream().toArray(Employee[]::new);
        
        List<String> numbersAsString = Arrays.asList("1000", "2000");
        IntStream intStream = numbersAsString.stream()
                                               .mapToInt(x -> Integer.parseInt(x));
        
        

        
        
       
	}
	
	 public void whenFlatMapEmployeeNames_thenGetNameStream() {
         List<List<String>> namesNested = Arrays.asList( 
           Arrays.asList("Jeff", "Bezos"), 
           Arrays.asList("Bill", "Gates"), 
           Arrays.asList("Mark", "Zuckerberg"));

         List<String> namesFlatStream = namesNested.stream()
           .flatMap(Collection::stream)
           .collect(Collectors.toList());

        // assertEquals(namesFlatStream.size(), namesNested.size() * 2);
         
         //  return s.chars() .filter(c -> c == ch).count(); 
         
	 }
	 
	 public static boolean isStringOnlyAlphabet(String str)
	    {
	        return (
	            (str != null) && (!str.equals(""))
	            && (str.chars().allMatch(Character::isLetter)));
	    }

}

class Product{  
    int id;  
    String name;  
    float price;  
    public Product(int id, String name, float price) {  
        this.id = id;  
        this.name = name;  
        this.price = price;  
    }  
}  
