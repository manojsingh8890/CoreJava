package collection;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckNumb {
	
	
	public void getNumbers(int arr[]) {
		Map<Integer, List<Integer>> countMap = new HashMap<Integer, List<Integer>>();
		
		for(int i =0; i < arr.length ; i++) {
			if(countMap.containsKey(arr[i])) {
				countMap.get(arr[i]).add(i);
			}else {
				List<Integer> initList=  new ArrayList<>();
				initList.add(i);
				countMap.put(arr[i], initList);
			}
		}
		
		for(Map.Entry<Integer, List<Integer>>  entry : countMap.entrySet()) {
			System.out.println(entry.getKey()+ "->" + entry.getValue() + "->" +  entry.getValue().size());
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int [] arr = {9,0,1,9,8,0,11,7,8,7,0,9,4,5,6,4,5,6};
		new CheckNumb().getNumbers(arr);

	}

}
