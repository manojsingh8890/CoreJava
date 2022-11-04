package handson;

public class CustomerHashMap<k, v> {
	private Entry<k, v> table[] = null;
	private int size = 4;
	
	static class Entry<k, v> {
		k key;
		v value;
		Entry<k, v> next;
		
		Entry(k key, v value){
			this.key = key;
			this.value = value;
			this.next = null;
		}
	}
	
	@SuppressWarnings("unchecked")
	CustomerHashMap(){
		table = new Entry[size];
	}
	
	public void put(k key, v value) {
		if(key ==null) {
			return;
		}
		Entry<k, v> entry = new Entry<k, v>(key, value);
		int hash = hash(key);
		if(table[hash] == null) {
			table[hash] = entry;
			return;
		}else {
			Entry<k, v> previous = null;
			Entry<k, v> current = table[hash];
			while(current != null) {
				if(current.key.equals(key)) {
					if(previous == null) {
						entry.next = current.next;
						table[hash] = entry;
					}else {
						entry.next = current.next;
						previous.next = entry;
					}
				}
				
				previous = current;
				current = current.next;
			}
			
			previous.next = entry;
		}
	}
	
	public int hash(k key) {
		return Math.abs(key.hashCode())%size;
	}
	
	public void show() {
		for(Entry<k, v> entry: table) {
			while(entry != null) {
				System.out.println(entry.key +"->"+ entry.value);
				entry = entry.next;
			}
		}
	}
	
	public void get(k key) {
		int hash = hash(key);
		Entry<k, v> current = table[hash];
		while(current != null) {
			if(current.key.equals(key)) {
				System.out.println(current.value);
				return;
			}else {
				current = current.next;
			}
			
		}
	}
	
	public void remove(k key) {
		if(key ==null) {
			return;
		}
		int hash = hash(key);
		Entry<k, v> previous = null;
		Entry<k, v> current = table[hash];
		while(current != null) {
			if(current.key.equals(key)) {
				if(previous == null) {
					current = current.next;
					table[hash] = current;
					return;
				}else {
					previous.next = current.next;
					return;
				}
			}
			
			previous = current;
			current = current.next;
		}
	}
	
	public static void main(String arg[]) {
		CustomerHashMap custom = new CustomerHashMap();
		custom.put("Manoj", "1");
		custom.put("Vijay", "2");
		custom.put("Saurabh", "3");
		custom.put("Saurabh", "3");
		custom.put("Manoj", "2");
		//custom.show();
		//custom.get("Manoj");
		custom.remove("Manoj");
		custom.show();
	}

}
