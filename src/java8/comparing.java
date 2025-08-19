package java8;

import java.util.*;
import java.util.stream.Collectors;

class Employee4 {
    String name;
    String department;
    double salary;

    public Employee4(String name, String department, double salary) {
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "Employee{name='" + name + "', department='" + department + "', salary=" + salary + "}";
    }
}

public class comparing {
    public static void main(String[] args) {
        List<Employee4> employees = Arrays.asList(
            new Employee4("Liam", "Finance", 75000),
            new Employee4("Olivia", "Engineering", 80000),
            new Employee4("Ethan", "Engineering", 75000),
            new Employee4("Sophia", "Marketing", 75000),
            new Employee4("Mason", "Finance", 85000)
        );
        
       System.out.println( "MAx : " + employees.stream().max((x,y) -> Double.compare(x.getSalary(), y.getSalary())).get());
        
        employees.stream().collect(Collectors.groupingBy(x -> x.getDepartment(), Collectors.mapping(x -> x.getSalary(), Collectors.toList())))
        .entrySet().stream().collect(Collectors.toMap(x -> x.getKey(), y -> y.getValue().stream().collect(Collectors.averagingDouble(x -> x))));
        
        employees.stream().collect(Collectors.groupingBy(x -> x.getDepartment(), Collectors.mapping(x -> x.getSalary(), Collectors.toList())))
        .entrySet().stream()
        .forEach(x -> System.out.println("dep : " + x.getKey() + " Avarage : " + x.getValue().stream().mapToDouble(y -> y).average().getAsDouble()));
        
        
        employees.sort(Comparator.comparing(Employee4::getDepartment).thenComparing(Employee4::getSalary));
        
        
        employees.stream()
        .sorted(Comparator.comparing(Employee4::getDepartment)
                .thenComparing(Employee4::getSalary))
        .map(Employee4::getSalary)
        .forEach(System.out::println);
        
      double bbb=  employees.stream()
        .sorted(Comparator.comparing(Employee4::getDepartment)
                .thenComparing(Employee4::getSalary))
        .map(Employee4::getSalary).collect(Collectors.averagingDouble(x-> x));

      System.out.println("bbb : "+ bbb);
      
        System.out.println(employees);
        
        
        /////Max Int
        List<Integer> myList = Arrays.asList(10,15,8,49,25,98,98,32,15);
        int max =  myList.stream()
                         .max((x,y) -> x.compareTo(y))
                         .get();
        System.out.println(max);  
        
    /////Max float
        List<Float> myList1 = Arrays.asList(10f,15f);
        float max1 =  myList1.stream()
                         .max((x,y) -> x.compareTo(y))
                         .get();
        System.out.println(max1);  
        
    /////Max float
        List<Double> myList2 = Arrays.asList(10d,15d);
        Double max2 =  myList2.stream()
                         .max((x,y) -> x.compareTo(y))
                         .get();
        System.out.println(max2);  
    }
}