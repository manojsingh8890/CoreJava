package arrays;

import java.util.HashSet;
import java.util.Set;

public class PairsOfElementsInArray
{
    static void findThePairs(int inputArray[], int inputNumber)
    {
        System.out.println("Pairs of elements whose sum is "+inputNumber+" are : ");
        
        // o(n2)
 
        for (int i = 0; i < inputArray.length; i++)
        {
            for (int j = i+1; j < inputArray.length; j++)
            {
                if(inputArray[i]+inputArray[j] == inputNumber)
                {
                    System.out.println(inputArray[i]+" + "+inputArray[j]+" = "+inputNumber);
                }
            }
        }
    }
    
    public static void findUniquePairsWithSum(int[] arr, int targetSum) {
        Set<Integer> seenNumbers = new HashSet<>();
        Set<String> uniquePairs = new HashSet<>(); // To store unique pairs as strings to avoid duplicates like (2,3) and (3,2)

        for (int num : arr) {
            int complement = targetSum - num;
            if (seenNumbers.contains(complement)) {
                // Found a pair
                // Ensure unique pairs are stored, regardless of order
                String pair1 = num + "," + complement;
                String pair2 = complement + "," + num;
                if (!uniquePairs.contains(pair1) && !uniquePairs.contains(pair2)) {
                    System.out.println("Pair found: (" + num + ", " + complement + ")");
                    uniquePairs.add(pair1);
                }
            }
            seenNumbers.add(num);
        }
    }
 
    public static void main(String[] args)
    {
        findThePairs(new int[] {4, 6, 5, -10, 8, 5, 20}, 10);
 
        findThePairs(new int[] {4, -5, 9, 11, 25, 13, 12, 8}, 20);
 
        findThePairs(new int[] {12, 13, 40, 15, 8, 10, -15}, 25);
 
        findThePairs(new int[] {12, 23, 125, 41, -75, 38, 27, 11}, 50);
        
        findUniquePairsWithSum(new int[] {12, 23, 125, 41, -75, 38, 27, 11}, 50);
    }
}