package CompletableFuture;

import java.util.concurrent.CompletableFuture;

public class TestCompletableFuture {
	
	public static void main(String arg[]) throws Exception {
	CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
	
	 System.out.println(future1.get());
	 
	// -----------------------------------------
	
	CompletableFuture<String> completableFuture
	  = CompletableFuture.supplyAsync(() -> "Hello");

	CompletableFuture<String> future = completableFuture
	  .thenApply(s -> s + " World");

	   System.out.println(future.get());
	  
	   // ------------------------------------------------------
	   /*
	    * 
	    * If we don't need to return a value down the Future chain, we can use an instance of the Consumer functional interface.
	    *  Its single method takes a parameter and returns void.
          There's a method for this use case in the CompletableFuture. 
          The thenAccept method receives a Consumer and passes it the result of the computation. 
          Then the final future.get() call returns an instance of the Void type:
	    */
	   CompletableFuture<String> completableFuture1
	   = CompletableFuture.supplyAsync(() -> "Hello");

	 CompletableFuture<Void> future2 = completableFuture1
	   .thenAccept(s -> System.out.println("Computation returned: " + s));

	    System.out.println(future2.get());
	    
	    
 // -----------------------------------------------------------------------------------	
	    /*
	     * Finally, if we neither need the value of the computation nor want to return some value at the end of the chain,
	     *  then we can pass a Runnable lambda to the thenRun method. 
	     * In the following example, we simply print a line in the console after calling the future.get():
	     */
	    CompletableFuture<String> completableFuture2
	    = CompletableFuture.supplyAsync(() -> "Hello");

	  CompletableFuture<Void> future3 = completableFuture2
	    .thenRun(() -> System.out.println("Computation finished."));

	  System.out.println(future2.get());
	   // ----------------------------------------------------------------------------------------
	  // thenCompose (flatMap) method receives a function that returns another object of the same type. 
	  CompletableFuture<String> completableFuture3 = CompletableFuture.supplyAsync(() -> "Hello")
		.thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"));

	   System.out.println(completableFuture3.get());
	   
	   
	   // difference 
	   /*
	    * thenApply = We can use this method to work with the result of the previous call.
	    *thenCompose() uses the previous stage as the argument. 
	    *
	    */
	   
	   
	   // ---------------------------------------------
	   
	   CompletableFuture<String> future5  
	   = CompletableFuture.supplyAsync(() -> "Hello");
	 CompletableFuture<String> future6
	   = CompletableFuture.supplyAsync(() -> "Beautiful");
	 CompletableFuture<String> future7  
	   = CompletableFuture.supplyAsync(() -> "World");

	 CompletableFuture<Void> combinedFuture 
	   = CompletableFuture.allOf(future5, future6, future7);

	 // ...

	 System.out.println(combinedFuture.get());

	 System.out.println(future5.isDone());
	 System.out.println(future6.isDone());
	 System.out.println(future7.isDone());
	 
	}

}

