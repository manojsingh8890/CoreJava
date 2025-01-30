package java8;
import java.util.function.Predicate;
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.BiPredicate;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class Java8FunctionalInterfaces {

	public static void main(String[] args) {
		
		
		
		//Predicate
		// We can use Predicate<T> to implement some conditional checks. However, from it’s method signature : boolean test(T t) 
		//it is clear that it takes an input parameter and returns a Boolean result. When you have this type of requirement to write a method, use it confidently. 
		/* public interface Predicate<T> {
		    boolean test(T t);
		}
		=> T denotes the input parameter type. */
		Predicate<Integer> p = (i) -> (i > -10) && (i < 10);
		System.out.println(p.test(9));
		
		//filter
		
		
		
		// Function
		//Function<T, R> is used to perform some operation & returns some result. Unlike Predicate<T> which returns only boolean, Function<T, R> can return any type of value. 
		//Therefore, we can also say that Predicate is a special type of Function which returns only Boolean values.
		/*interface Function<T,R> { 
		    R apply(T t);
		}

		=> T is method input parameter & R is return type */
		Function<String, Integer> f = s -> s.length(); 
		System.out.println(f.apply("I am happy now")); 
		
		
		
		
		
		// Consumer
		// Consumer<T> is used when we have to provide some input parameter, perform certain operation, but don’t need to return anything. 
		//Moreover, we can use Consumer to consume object and perform certain operation.
		// => T denotes the input parameter type.
		/*interface Consumer<T> {
			   void accept(T t);
			}

			=> T denotes the input parameter type. */
		Consumer<String> c = s -> System.out.println(s); 
		c.accept("I consume data but don't return anything"); 
		
		// foreach
		
		
		
		
		// Supplier
		// Supplier<R> doesn’t take any input and it always returns some object. 
		//However, we use it when we need to get some value based on some operation like supply Random numbers, supply Random OTPs, supply Random Passwords etc.
		/*interface Supplier<R>{
		    R get();
		}

		=> R is a return type */
		Supplier<String> otps = () -> {
		     String otp = "";
		     for (int i = 1; i <= 4; i++) {
		        otp = otp + (int) (Math.random() * 10);
		     }
		   return otp;
		};
		System.out.println(otps.get());
		
		
		
		// BiPredicate
		
		// BiPredicate<T, U> is same as Predicate<T> except that it has two input parameters. For example, below code denotes it:
		/*interface BiPredicate<T, U> {
		    boolean test(T t, U u)
		}

		=> T & U are input parameter types*/

		BiPredicate<Integer,Integer> bp = (i,j)->(i+j) %2==0; 
		System.out.println(bp.test(24,34)); 
		
		
		/*BiFunction<T, U, R> is same as Function<T, R> except that it has two input parameters. For example, below code denotes it:

			interface BiFunction<T, U, R> {
			    R apply(T t, U u);
			}

			=> T & U are method input parameters & R is return type */
		
		BiFunction<Integer,Integer,Integer> bf = (i,j)->i+j; 
		System.out.println(bf.apply(24,4)); 
		
		
		/*BiConsumer<T> is same as Consumer<T> except that it has two input parameters. For example, below code denotes it:

			interface BiConsumer<T, U> {
			   void accept(T t, U u);
			}

			=> T & U are method input parameters */
		BiConsumer<String,String> bc = (s1, s2)->System.out.println(s1+s2); 
		bc.accept("Bi","Consumer"); 
	}

}
