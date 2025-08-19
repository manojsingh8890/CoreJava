package JavaProgram;

public class MaxProfitStock {

// A method that returns the maximized profit  
// which can be made after buying and selling  
// the stocks given   
	public int maximumProfit(int p[], int st, int ed) {

		if (ed <= st) {
			return 0;
		}

// Initializing the profit  
		int prof = 0;

// Looking for the day at which the stock must  
// be purchased  
		for (int i = st; i < ed; i++) {
			for (int j = i + 1; j <= ed; j++) {
				if (p[j] > p[i]) {
					int currProfit = p[j] - p[i] + maximumProfit(p, st, i - 1) + maximumProfit(p, j + 1, ed);
					prof = Math.max(prof, currProfit);
				}
			}
		}
		return prof;
	}

// main method  
	public static void main(String argvs[]) {
// price of the stock  
		int price[] = { 50, 90, 130, 155, 20, 267, 347 };
		int size = price.length; // computing the size

		System.out.println("The price of the stock on different days is: ");

		for (int i = 0; i < size; i++) {
// displaying the stock price  
			System.out.print(price[i] + " ");
		}

		System.out.println();
// creating an object of the class MaxProfitStock  
		MaxProfitStock obj = new MaxProfitStock();

		int ans = obj.maximumProfit(price, 0, size - 1);

		System.out.print("The maximum profit earned is: ");

		System.out.print(ans);
	}
}