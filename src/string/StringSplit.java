package string;

public class StringSplit {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	// ------------------------------------------------------------------------
		
		var s = "apple-banana-cherry";
		String[] res = s.split("-");
		System.out.println("1. match found." + res.length);
		
   // ------------------------------------------------------------------------
		
		var s1 = "apple, banana; cherry|grape";
		String[] res1 = s1.split("[,;|]");
		System.out.println("2. match found." + res1.length);
		
	// ------------------------------------------------------------------------
		
		var s2 = "apple   banana  cherry";
		String[] res2 = s2.split("\\s+");
		System.out.println("3. match found." + res2.length);
	
	// ------------------------------------------------------------------------
		
		var s3 = "HelloWorld";
		String[] res3 = s3.split("(?=[A-Z])");
		System.out.println("4. match found." + res3.length);
	
	// ------------------------------------------------------------------------
		
		var s4 = "Hello&World&Hello&World";
		String[] res4 = s4.split("([&])");
		System.out.println("5. match found." + res4.length);
		
    // -------------------------------------------------------------------------
		
		var s5 = "one, two; three;two; three";
		String[] res5 = s5.split("([,;])");
		System.out.println("6. match found." + res5.length);
		
	// ------------------------------------------------------------------------
		
		var s6 = "apple-banana-cherry";
		String[] res6 = s6.split("-", 2);
		System.out.println("7. match found." + res6.length);
		
	// ------------------------------------------------------------------------
		
		var s7 = "apple1banana2cherry3grape";
		String[] res7 = s7.split("\\d");
		System.out.println("8. match found." + res7.length);
		
	// ------------------------------------------------------------------------
		
		var s8 = "username@gmail.com";
		String[] res8 = s8.split("[@.]");
		System.out.println("9. match found." + res8.length);
		
	// ------------------------------------------------------------------------
		
		var s9 = "username\n"
				+ "kjhkjh";
		String[] res9 = s9.split("\\n");
		System.out.println("10. match found." + res9.length);
	// ------------------------------------------------------------------------
		
		var s10 = "HeL";
		String[] res10 = s10.split("(?=[a-z])");
		System.out.println("11. match found." + res10.length);
		
	// ------------------------------------------------------------------------
		
		var s11 = "H1L2P";
		String[] res11 = s11.split("(?=[0-9])");
		System.out.println("12. match found." + res11.length);
		
	// ------------------------------------------------------------------------
		
		var s12 = "H1L2P";
		String[] res12 = s12.split("(?=[a-zA-Z0-9])");
		System.out.println("13. match found." + res12.length);
		
	// ------------------------------------------------------------------------
		
		var s13 = "dv$%&ghj&@h";
		String[] res13 = s13.split("(?=[\\W+]+)");
		System.out.println("14. match found." + res13.length);

	
	// ------------------------------------------------------------------------
		
		var s14 = "dv$%&ghj&@h";
		String[] res14 = s14.split("([^a-zA-Z0-9]+)");
		System.out.println("15. match found." + res14.length);
		
	// ------------------------------------------------------------------------
		
		var s15 = "dv$%&ghj&@h";
		String[] res15 = s15.split("([^a-zA-Z0-9]+)");
		System.out.println("16. match found." + res15.length);
	}

}
