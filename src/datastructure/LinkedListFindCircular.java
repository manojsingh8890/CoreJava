package datastructure;



class CircularNode <d> {
	
	d data;
	CircularNode next;
	
	CircularNode(d data){
		this.data  = data;
		this.next = null ;
	}
	
	/**
    * Display Node's data
    */
   public void displayNode() {
          System.out.print( data + " ");
   }
}

public class LinkedListFindCircular<d> {
	CircularNode first = null;
	LinkedListFindCircular(){
		
	}
	
	public void add(d data) {
		if(data == null) {
			return;
		}else {
			CircularNode newNode = new CircularNode(data);
			newNode.next = first;
			first = newNode;
		}
	}
	
	public void createCircular() {
		if(first != null) {
			CircularNode temp = first;
			while(temp.next != null) {
				temp = temp.next;
			}
			temp.next = first;
		}
	}
	
	public boolean findCircular() {
		CircularNode slow = first;
		CircularNode fast = first;
		while(fast!= null && fast.next != null) {
			slow =  slow.next;
			fast = fast.next.next;
			
			if(slow == fast) {
				System.out.println("Circular");
				return true;
			}
		}
		 return false;
	}
	
	public static void main(String arg[]) {
		LinkedListFindCircular  circular= new LinkedListFindCircular();
		circular.add(1);
		circular.add(2);
		circular.add(3);
		circular.add(4);
		circular.add(5);
		circular.add(6);
		circular.add(7);
		circular.add(8);
		circular.add(9);
		circular.add(10);
	    circular.createCircular();
		System.out.println(circular.findCircular());
	}
}
