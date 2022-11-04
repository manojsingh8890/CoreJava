package datastructure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
  
  
  
/** Copyright (c), AnkitMittal www.JavaMadeSoEasy.com */
public class LinkedListIntersectionPointExample {
 
 public static void main(String[] args) {
     List<Integer> l1=new LinkedList<Integer>();
     List<Integer> l2=new LinkedList<Integer>();
     
     l1.add(11); l1.add(13); l1.add(16); l1.add(19); l1.add(22); l1.add(23);
                         l2.add(12); l2.add(14); l2.add(19); l2.add(22); l2.add(23);
     
     twoListsAreMergingOrNot(l1,l2);
 
 }
 
 public static void twoListsAreMergingOrNot(List<Integer> l1, List<Integer> l2){
 
     List<Integer> smallLinkedList=l1;
     List<Integer> largeLinkedList=l2;
     Integer diffInSize= l2.size()-l1.size();
     int ctr=0;
     
     if(diffInSize<0){   //if difference is negative, swap the references of lists.
            smallLinkedList=l2;
            largeLinkedList=l1;
            diffInSize=Math.abs(diffInSize);
     }
     
     Iterator<Integer> smallListIterator=smallLinkedList.iterator();
     Iterator<Integer> largeListIterator=largeLinkedList.iterator();
     
     while(largeListIterator.hasNext()){
            int listValue=largeListIterator.next();
            if(ctr<diffInSize){
                   ctr++;
                   continue;
            }
            
            if(listValue==smallListIterator.next()){
                   System.out.println("Lists are merging at :"+listValue);
                   return;
            }
     }
     
 }
}
