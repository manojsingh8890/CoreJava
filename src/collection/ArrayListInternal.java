package collection;

import java.util.Arrays;



public class ArrayListInternal<E> {
	Object[] elementData;
	
	Object DEFAULTCAPACITY_EMPTY_ELEMENTDATA = 10;
	int size = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private void add(E e, Object[] elementData, int s) {
        if (s == elementData.length)
            elementData = grow(size);
        elementData[s] = e;
        size = s + 1;
    }
	
	 private Object[] grow(int minCapacity) {
	        int oldCapacity = elementData.length;
	        if (oldCapacity > 0 || elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
	         //  /* int newCapacity = ArraysSupport.newLength(oldCapacity,
	         //           minCapacity - oldCapacity, /* minimum growth */
	        //            oldCapacity >> 1           /* preferred growth ); */
	           // return elementData = Arrays.copyOf(elementData, newCapacity);
	        } else {
	           // return elementData = new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
	        }
	        return null;
	    }
}
