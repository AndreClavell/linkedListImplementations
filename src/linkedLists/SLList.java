/**
 * 
 */
package linkedLists;

import interfaces.Node;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author pirvos
 *
 */

/**
 * clone method
 *
 * @author Andre A. Clavell Alvarez
 * CIIC4020-086
 * 843-15-1170
 *
 */
public class SLList<E> extends AbstractSLList<E> {
	private SNode<E> first; 
	private int length; 

	public SLList() {   // to create an empty list instance
		first = null; 
		length = 0; 
	}
	
	public void addFirstNode(Node<E> nuevo) {
		// Pre: nuevo is not a node in the list
		((SNode<E>) nuevo).setNext(first); 
		first = (SNode<E>) nuevo; 
		length++;
	}

	public void addLastNode(Node<E> nuevo) { 
		SNode<E> sNuevo = (SNode<E>) nuevo; 
		sNuevo.setNext(null);
		if (length == 0)
			first = sNuevo; 
		else {   // find current last node and add the new one after that last node
			SNode<E> p = first; 
			while (p.getNext() != null) 
				p = p.getNext(); 
			p.setNext(sNuevo);
		}
		length++; 
	}
	
	public void addNodeAfter(Node<E> target, Node<E> nuevo) {
		// Pre: target is a node in the list
		// Pre: nuevo is not a node in the list
		((SNode<E>) nuevo).setNext(((SNode<E>) target).getNext()); 
		((SNode<E>) target).setNext((SNode<E>) nuevo); 
		length++; 
	}

	public void addNodeBefore(Node<E> target, Node<E> nuevo) {
		// Pre: target is a node in the list
		// Pre: nuevo is not a node in the list

		if (target == first)
			this.addFirstNode(nuevo); 
		else { 
			Node<E> prevNode = findNodePrevTo(target);  
			this.addNodeAfter(prevNode, nuevo); 
		}
	}

	private Node<E> findNodePrevTo(Node<E> target) {
		// Pre: target is a node in the list
		if (target == first)  // the list is empty
			return null; 
		else { 
			SNode<E> prev = first; 
			while (prev != null && prev.getNext() != target) 
				prev = prev.getNext();  
			return prev; 
		}
	}

	public Node<E> getLastNode() 
	{
		if (first == null)   // the list is empty
			return null;  
		else { 
			SNode<E> curr = first; 
			while (((SNode<E>) curr).getNext() != null)
				curr = curr.getNext(); 
			return curr; 
		}
	}

	public Node<E> getNodeAfter(Node<E> target) 
	{
		// Pre: target is a node in the list
		SNode<E> aNode = ((SNode<E>) target).getNext(); 
		if (aNode == null)  
			return null;  
		else 
			return aNode;
	}


	public Node<E> getNodeBefore(Node<E> target) 
	{
		// Pre: target is a node in the list
		if (target == first)  
			return null;  
		else 
			return findNodePrevTo(target);
	}

	public int length() {
		return this.length;
	}

	public void removeNode(Node<E> target) {
		// Pre: target is a node in the list; hence, the list is not empty
		
		if (target == first) 
			first = first.getNext(); 
		else { 
			SNode<E> prevNode = (SNode<E>) this.getNodeBefore(target); 
			prevNode.setNext(((SNode<E>) target).getNext()); 
		}
		((SNode<E>) target).clean();   // clear all references from target
		length--; 
	}

	
	public Node<E> getFirstNode() 
	throws NoSuchElementException 
	{
		if (first == null)
			throw new NoSuchElementException("getFirstNode() : linked list is empty..."); 
		
		// the linked list is not empty....
		return first;
	}
	
	/**
	 * Prepares every node so that the garbage collector can free 
	 * its memory space, at least from the point of view of the
	 * list. This method is supposed to be used whenever the 
	 * list object is not going to be used anymore. Removes all
	 * physical nodes (data nodes and control nodes, if any)
	 * from the linked list
	 */
	private void destroy() {
		while (first != null) { 
			SNode<E> nnode = first.getNext(); 
			first.setElement(null); 
			first.setNext(null); 
			first = nnode; 
		}
	}
	
	/**
	 * The execution of this method removes all the data nodes
	 * from the current instance of the list. The list becomes
	 * an empty list. Notice that in general this is not the
	 * same as destroy(). However, in this type of list it
	 * can be done by first invoking the destroy method and
	 * then make length = 0. 
	 * 
	 * NOTE: For other types of list, this strategy may not
	 * be correct as it is here.
	 */
	public void makeEmpty() { 
		destroy(); 
		length = 0; 
	}
	
	public Node<E> createNewNode() {
		return new SNode<E>();
	}

	@Override
	public Iterable<Node<E>> nodes() {
		return new NodesIterable();
	}


	@Override
	public Iterator<E> iterator() {
		return new ElementsIterator();
	}

	@Override
	public SLList<E> clone(){
		SLList<E> listClone = new SLList<>();
			Iterator<Node<E>> itr = new NodesIterator();
			while(itr.hasNext())
					listClone.addLastNode(new SNode<>(itr.next().getElement()));
			return listClone;
	}


	/**
	 * Class to represent a node of the type used in singly linked lists. 
	 * @author pedroirivera-vega
	 *
	 * @param <T> Data type of element in a node.
	 */
	protected static class SNode<T> implements Node<T> {
		private T element; 
		private SNode<T> next; 
		public SNode() { 
			element = null; 
			next = null; 
		}
		public SNode(T data, SNode<T> next) { 
			this.element = data; 
			this.next = next; 
		}
		public SNode(T data)  { 
			this.element = data; 
			next = null; 
		}
		public T getElement() {
			return element;
		}
		public void setElement(T data) {
			this.element = data;
		}
		public SNode<T> getNext() {
			return next;
		}
		public void setNext(SNode<T> next) {
			this.next = next;
		}
		public void clean() { 
			element = null; 
			next = null; 
		}
	}

	private class NodesIterable implements Iterable<Node<E>> {

		@Override
		public Iterator<Node<E>> iterator() {
			return new NodesIterator();
		} 
		
	}
	
	private class ElementsIterator implements Iterator<E> {

		NodesIterator nodesIter = new NodesIterator(); 
		
		@Override
		public boolean hasNext() {
			return nodesIter.hasNext();
		}

		@Override
		public E next() {
			return nodesIter.next().getElement();
		} 
		
		public void remove() { 
			nodesIter.remove(); 
		}
	}
	
	private class NodesIterator implements Iterator<Node<E>> {

		private SNode<E> curr = first;    // node containing element to return on next next()
		private SNode<E> ptntr = null;   // node preceding node valid to be removed
		private boolean canRemove = false;       // to control when remove() is valid to execute

		public boolean hasNext() { 
			return curr != null;
		}    

		public SNode<E> next() { 
			if (!hasNext()) 
				throw new NoSuchElementException("Iterator is completed.");
			if (canRemove) 
				ptntr = (ptntr == null ? first : ptntr.getNext());  // Why this? Think...
			canRemove = true;    
			SNode<E> ntr = curr;   
			curr = curr.getNext();   // get element and prepare for future
			return ntr; 
		} 

		public void remove() { 
			if (!canRemove) 
				throw new IllegalStateException("Not valid to remove."); 
			if (ptntr == null) 
				first = first.getNext();             // removes the first node
			else 
				ptntr.setNext(ptntr.getNext().getNext());     // removes node after ptntr     
			length--;    
			canRemove = false; 
		}   

	}


}
