package arrays;

import java.util.PriorityQueue;

public class LargestElement {
  
    // Function to find the K'th largest element
    public static int kthLargest(int[] arr, int K) {
      
        // Min heap to store K largest elements
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        
        // Iterate through the array elements
        for (int val : arr) {
          
            // Add current element to the min heap
            pq.add(val);
            // If heap exceeds size K, remove smallest element
            if (pq.size() > K)
                pq.poll();
        }

        // Top of the heap is the K'th largest element
        return pq.peek();
    }

    public static void main(String[] args) {
        int[] arr = {12, 3, 5, 7, 19};
        int K = 1;
        System.out.println(kthLargest(arr, K));
    }
}