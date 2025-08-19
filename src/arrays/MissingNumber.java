package arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MissingNumber {
	
	 static List<String> al = new ArrayList<>();
	public static void main(String[] args) {
		int[] nums = { 3, 0, 1, 4, 6, 2 };
		int missingNumber = findMissingNumber(nums);
		System.out.println("Missing Number: " + missingNumber);

		// ------------------------------------------------------------------------------------

		int i = 20+ +9- -12+ +4- -13+ +19;
		System.out.println(i);

		// ---------------------------------------------------------------------------------------

		String search = "bca";
		String x = "abbcabcnamncbcaklk";
		x.contains(search);
		Matcher m = Pattern.compile(search).matcher(x);
		//System.out.println("m.results().count() " + m.results().count());
		while (m.find()) {
			System.out.println("Start : " + m.start() + " End : " + m.end());
		}

		// ---------------------------------------------------------------------------------------

		String strForsubsequence = "abcd";
		findsubsequences(strForsubsequence, ""); // Calling a function
		System.out.println(al);
		
		// ---------------------------------------------------------------------------------------
		
		String strSequence = "PrajPrajPrajPraj";
		strSequence.split("(?=[P])");
		
		new TestJ().Test(null);

	}

	private static void findsubsequences(String s, String ans) {
		System.out.println("S : "+ s + " ans : " + ans);
		if (s.length() == 0) {
			al.add(ans);
			return;
		}

		// We add adding 1st character in string
		findsubsequences(s.substring(1), ans + s.charAt(0));

		// Not adding first character of the string
		// because the concept of subsequence either
		// character will present or not
		findsubsequences(s.substring(1), ans);
		
		
	}

	public static int findMissingNumber(int[] nums) {
		int n = nums.length;
		int sum = (n * (n + 1)) / 2;
		for (int num : nums) {
			sum -= num;
		}
		return sum;
	}
	
	
}


 class TestJ {

	public void Test(String[] args) {
		// TODO Auto-generated method stub
		int [] A = {1,2,3};
		//int [] A = {-1, -3};
		List<Integer> bb = Arrays.stream(A).filter(x -> x > 0).boxed().sorted().collect(Collectors.toList());
		int small = 0;
		for(int x = 1 ; x < bb.size(); x++) {
			if(! bb.contains(x)) {
				small = x;
				break;
			}
		}
		if(small == 0) {
			small = !bb.isEmpty() ? bb.get(bb.size()-1)+1: 1;
			System.out.println("Small : " + small);
		}else {
			System.out.println("Small : " + small);
		}
	}

}
