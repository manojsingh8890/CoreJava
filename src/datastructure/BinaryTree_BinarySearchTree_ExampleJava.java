package datastructure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

class Node { 
    public int value;

    public Node leftChild;

    public Node rightChild;

   /* public String toString() {
          return "id=" + value;

    } */

    @Override
    public String toString() {
          //return "[value=" + value + ", left=" + leftChild + ", right=" + rightChild + "]";
          return "[value=" + value +"]";
    }
}

class Tree {
    public Node root; // first node of tree
    // -------------------------------------------------------------

    public Tree() // constructor
    {
          root = null;
    } // no nodes in tree yet
          // -------------------------------------------------------------

    public Node find(int key) // find node with given key
    { // (assumes non-empty tree)
          Node current = root; // start at root
          while (current.value != key) // while no match,
          {
                 if (key < current.value) // go left?
                       current = current.leftChild;
                 else
                       // or go right?
                       current = current.rightChild;
                 if (current == null) // if no child,
                       return null; // didn't find it
          }
          return current; // found it
    } // end find()
          // -------------------------------------------------------------

    public void insert(int id) {
          Node newNode = new Node(); // make new node
          newNode.value = id; // insert data
          if (root == null) // no node in root
                 root = newNode;
          else // root occupied
          {
                 Node current = root; // start at root
                 Node parent;
                 while (true) // (exits internally)
                 {
                       parent = current;
                       if (id < current.value) // go left?
                       {
                              current = current.leftChild;
                              if (current == null) // if end of the line,
                              { // insert on left
                                    parent.leftChild = newNode;
                                    return;
                              }
                       } // end if go left
                       else // or go right?
                       {
                              current = current.rightChild;
                              if (current == null) // if end of the line
                              { // insert on right
                                    parent.rightChild = newNode;
                                    return;
                              }
                       } // end else go right
                 } // end while
          } // end else not root
    } // end insert()
          // -------------------------------------------------------------

    public boolean delete(int key) // delete node with given key
    { // (assumes non-empty list)
          Node current = root;
          Node parent = root;
          boolean isLeftChild = true;
          while (current.value != key) // search for node
          {
                 parent = current;
                 if (key < current.value) // go left?
                 {
                       isLeftChild = true;
                       current = current.leftChild;
                 } else // or go right?
                 {
                       isLeftChild = false;
                       current = current.rightChild;
                 }
                 if (current == null) // end of the line,
                       return false; // didn't find it
          } // end while
                 // found node to delete
                 // 1) if no children, simply delete it
          if (current.leftChild == null && current.rightChild == null) {
                 if (current == root) // if root,
                       root = null; // tree is empty

                 else if (isLeftChild)
                       parent.leftChild = null; // disconnect
                 else
                       // from parent
                       parent.rightChild = null;
          }
          // 2.1) if no right child, replace with left subtree
          else if (current.rightChild == null)
                 if (current == root)
                       root = current.leftChild;
                 else if (isLeftChild)
                       parent.leftChild = current.leftChild;
                 else
                       parent.rightChild = current.leftChild;
          // 2.2) if no left child, replace with right subtree
          else if (current.leftChild == null)
                 if (current == root)
                       root = current.rightChild;
                 else if (isLeftChild)
                       parent.leftChild = current.rightChild;
                 else
                       parent.rightChild = current.rightChild;
          else // 3) two children, so replace with inorder successor
          {
                 // get successor of node to delete (current)
                 Node successor = getSuccessor(current);
                 // connect parent of current to successor instead
                 if (current == root)
                       root = successor;
                 else if (isLeftChild)
                       parent.leftChild = successor;
                 else
                       parent.rightChild = successor;
                 // connect successor to current's left child
                 successor.leftChild = current.leftChild;
          } // end else two children
                 // (successor cannot have a left child)

          return true; // success
    } // end delete()
          // -------------------------------------------------------------
          // returns node with next-highest value after delNode
          // goes to right child, then right child's left descendents

    private Node getSuccessor(Node delNode) {
          Node successorParent = delNode;
          Node successor = delNode;
          Node current = delNode.rightChild; // go to right child
          while (current != null) // until no more
          { // left children,
                 successorParent = successor;
                 successor = current;
                 current = current.leftChild; // go to left child
          }
          // if successor not
          if (successor != delNode.rightChild) // right child,
          { // make connections
                 successorParent.leftChild = successor.rightChild;
                 successor.rightChild = delNode.rightChild;
          }
          return successor;
    }

    public void preOrder(Node localRoot) {
          if (localRoot != null) {
                 System.out.print(localRoot.value + " ");
                 preOrder(localRoot.leftChild);
                 preOrder(localRoot.rightChild);
          }
    }

    public void inOrder(Node localRoot) {
          if (localRoot != null) {
                 inOrder(localRoot.leftChild);
                 System.out.print(localRoot.value + " ");
                 inOrder(localRoot.rightChild);
          }
    }

    public void postOrder(Node localRoot) {
          if (localRoot != null) {
                 postOrder(localRoot.leftChild);
                 postOrder(localRoot.rightChild);
                 System.out.print(localRoot.value + " ");
          }
    }
    
    public void printBSTLevelOrder() {
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
        	Node tempNode = queue.poll();
            System.out.print(tempNode.value + " ");

            /*add left child to the queue */
            if (tempNode.leftChild != null) {
                queue.add(tempNode.leftChild);
            }

            /*add right right child to the queue */
            if (tempNode.rightChild != null) {
                queue.add(tempNode.rightChild);
            }
        }
    }

  /*  // Breadth-First Search //Display output in tree form
    public void displayTree() {
          Stack parentStack = new Stack();
          parentStack.push(root);
          int nBlanks = 32;
          boolean isRowEmpty = false;
          while (isRowEmpty == false) {

                 Stack childStack = new Stack();
                 isRowEmpty = true;
                 for (int j = 0; j < nBlanks; j++)
                       System.out.print(' ');
                 while (parentStack.isEmpty() == false) {
                       Node currentNode = (Node) parentStack.pop();
                       if (currentNode != null) {
                           System.out.print(currentNode.value);
                           childStack.push(currentNode.leftChild);
                           childStack.push(currentNode.rightChild);
                              if (currentNode.leftChild != null || currentNode.rightChild != null)
                                    isRowEmpty = false;
                       } else {
                              System.out.print("--");
                              childStack.push(null);
                              childStack.push(null);
                       }
                       for (int j = 0; j < nBlanks * 2 - 2; j++)
                              System.out.print(' ');
                 } // end while parentStack not empty
                 System.out.println();
                 nBlanks /= 2;
                 while (childStack.isEmpty() == false)
                       parentStack.push(childStack.pop());
          } // end while isRowEmpty is false
    } */

    // Breadth-First Search // Simply- without formatting
    public void displayTree2() {
          Stack parentStack = new Stack();
          parentStack.push(root);
          boolean noChild = false;
          while (noChild == false) {

                 Stack childStack = new Stack();
                 noChild = true; // Initially true, we will iterate till its false

                 while (parentStack.isEmpty() == false) {
                       Node currentNode = (Node) parentStack.pop();
                       if (currentNode != null) {
                           System.out.print(currentNode.value + " ");
                           childStack.push(currentNode.leftChild);
                           childStack.push(currentNode.rightChild);
                              
                              // Find any any child exists or not OR
                              //If any of the left or right child is not null
                              if (currentNode.leftChild != null || currentNode.rightChild != null)
                                    noChild = false; // It was set true at start of while loop and will
                                                                  // remain true if no child exists
                       }
                 }
              
                 while (childStack.isEmpty() == false)
                       parentStack.push(childStack.pop());
          } // end while isRowEmpty is false

          System.out.println();
    }

}

public class BinaryTree_BinarySearchTree_ExampleJava {

    public static void main(String[] args) throws IOException {

          // Insert
          Tree theTree = new Tree();
          theTree.insert(50);
          theTree.insert(17);
          theTree.insert(72);
          theTree.insert(12);
          theTree.insert(23);
          theTree.insert(54);
          theTree.insert(76);
          theTree.insert(9);
          theTree.insert(14);
          theTree.insert(19);
          theTree.insert(67);

          // Breadth-First Search //Display output in tree form
          System.out.println("Breadth-First Search //Display output in tree form");
          theTree.printBSTLevelOrder();
          
          
          // Breadth-First Search // Simply- without formatting
          System.out.println("\nBreadth-First Search");
          theTree.displayTree2();

          /*   // Depth first search
          System.out.println("preOrder");
          theTree.preOrder(theTree.root);
          System.out.println("\ninOrder");
          theTree.inOrder(theTree.root);
          System.out.println("\npostOrder");
          theTree.postOrder(theTree.root);

          // Find
          Node found = theTree.find(19);
          if (found != null) {
                 System.out.print("\nFound");

          } else
                 System.out.print("\nNot found ");

          System.out.print("\n");

          // Delete
          boolean didDelete = theTree.delete(19);
          if (didDelete)
                 System.out.println("Deleted ");
          else
                 System.out.println("Could not delete ");  */

    }
}
