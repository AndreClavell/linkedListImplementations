package linkedLists;

import interfaces.Node;

import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * Implementations of DLDHDTList functions.
 *
 * @author Andre A. Clavell Alvarez
 * CIIC4020-086
 * 843-15-1170
 *
 */

@SuppressWarnings("unchecked cast")
public class DLDHDTList<E> extends AbstractDLList<E> {

	private DNode<E> header, trailer; 
	private int length; 
	
	public DLDHDTList() {
		header = trailer = new DNode<>();
		header.setPrev(trailer);
		trailer.setNext(header);
		length = 0;
	}
	
	public void addFirstNode(Node<E> nuevo) {
		addNodeAfter(header, nuevo); 
	}

	/**
	 * adds node at the end of the list.
	 * @param nuevo
	 */
	public void addLastNode(Node<E> nuevo) { 
		DNode<E> dnuevo = (DNode<E>) nuevo; 
		DNode<E> nBefore = trailer.getPrev();  
		nBefore.setNext(dnuevo); 
		trailer.setPrev(dnuevo); 
		dnuevo.setPrev(nBefore); 
		dnuevo.setNext(trailer); 
		length++; 
	}

	/**
	 * adds the new  node after the target node.
	 * @param target
	 * @param nuevo
	 */
	public void addNodeAfter(Node<E> target, Node<E> nuevo) {
		DNode<E> dnuevo = (DNode<E>) nuevo; 
		DNode<E> nBefore = (DNode<E>) target; 
		DNode<E> nAfter = nBefore.getNext(); 
		nBefore.setNext(dnuevo); 
		nAfter.setPrev(dnuevo); 
		dnuevo.setPrev(nBefore); 
		dnuevo.setNext(nAfter); 
		length++; 
	}

	/**
	 * adds the new node before the target node.
	 * @param target
	 * @param nuevo
	 */
	public void addNodeBefore(Node<E> target, Node<E> nuevo) {
		DNode<E> dnuevo = (DNode<E>) nuevo;
		DNode<E> nAfter = (DNode<E>) target;
		DNode<E> nBefore = nAfter.getPrev();
		nAfter.setPrev(dnuevo);
		nBefore.setNext(dnuevo);
		dnuevo.setNext(nAfter);
		dnuevo.setPrev(nBefore);
		length++;
	}

	public Node<E> createNewNode() {
		return new DNode<>();
	}

	/**
	 * returns the first Node in the list.
	 * @return Node<E>
	 * @throws NoSuchElementException
	 */
	public Node<E> getFirstNode() throws NoSuchElementException {
		if (length == 0) 
			return null; 
		return header.getNext();
	}

	/**
	 * returns last node of the list.
	 * @return Node<E>
	 * @throws NoSuchElementException
	 */
	public Node<E> getLastNode() throws NoSuchElementException {
		if (length == 0) 
			return null; 
		return trailer.getPrev();
	}

	/**
	 * returns node after target.
	 * @param target
	 * @return Node<E>
	 */
	public Node<E> getNodeAfter(Node<E> target) {
		// ADD CODE HERE AND MODIFY RETURN ACCORDINGLY
		if(target == null){
			return header.getNext();
		}
		return ((DNode<E>)target).getNext();
	}

	/**
	 * returns node before target.
	 * @param target
	 * @return Node<E>
	 */
	public Node<E> getNodeBefore(Node<E> target) {
		// ADD CODE HERE AND MODIFY RETURN ACCORDINGLY
		if(target == null){
			return trailer.getPrev();
		}
		return ((DNode<E>)target).getPrev();
	}

	/**
	 * returns length of the list.
	 * @return length
	 */
	public int length() {
		return length;
	}

	/**
	 * removes the target node from the list.
	 * @param target
	 */
	public void removeNode(Node<E> target) {
		DNode<E> ntr = (DNode<E>) target;
		DNode<E> nextN = ntr.getNext();
		DNode<E> prevN = ntr.getPrev();
		nextN.setPrev(prevN);
		prevN.setNext(nextN);
		ntr.clean();//helps with the garbage collector
		length--;
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
		while (header != null) { 
			DNode<E> nnode = header.getNext(); 
			header.clean(); 
			header = nnode; 
		}
	}
	
	/**
	 * The execution of this method removes all the data nodes from
	 * the current instance of the list, leaving it as a valid empty
	 * doubly linked list with dummy header and dummy trailer nodes. 
	 */
	public void makeEmpty() {
		destroy();
		header = trailer = new DNode<>();
		header.setPrev(trailer);
		trailer.setNext(header);
		length=0;

	}


	/**
	 * returns a light clone of the target list upon execution.
	 *
	 * @return DLDHDTList<E>
	 */
	public DLDHDTList<E> clone(){
		DLDHDTList<E> listClone = new DLDHDTList<>();
		NodesIterator itr = new NodesIterator();
		while(itr.hasNext())
					listClone.addLastNode(new DNode<>(itr.next().getElement()));
		return listClone;
	}

	@Override
	public Iterable<Node<E>> nodes() {
		return new NodesIterable();
	}

	@Override
	public Iterator<E> iterator() {
		return new ElementsIterator();
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

		private DNode<E> curr = header.getNext();    // node containing element to return on next next()
		private DNode<E> ptntr = null;   // node preceding node valid to be removed
		private boolean canRemove = false;       // to control when remove() is valid to execute

		public boolean hasNext() {
			return curr != trailer;
		}

		public DNode<E> next() {
			if (!hasNext())
				throw new NoSuchElementException("Iterator is completed.");
			if (canRemove)
				ptntr = (ptntr == null ? header : ptntr.getNext());  // Why this? Think...
			canRemove = true;
			DNode<E> ntr = curr;
			curr = curr.getNext();   // get element and prepare for future
			return ntr;
		}

		public void remove() {
			if (!canRemove)
				throw new IllegalStateException("Not valid to remove.");
			if (ptntr == null)
				header.setNext(header.getNext().getNext());             // removes the first node
			else
				ptntr.setNext(ptntr.getNext().getNext());     // removes node after ptntr
			length--;
			canRemove = false;
		}

	}

	/**
	 * Class to represent a node of the type used in doubly linked lists. 
	 * @author pedroirivera-vega
	 *
	 * @param <T> Data type of element in a node.
	 */
	protected static class DNode<T> implements Node<T> {
		private T element; 
		private DNode<T> prev, next; 

		// Constructors
		public DNode() {
			this(null, null, null); 
		}
		
		public DNode(T e) {
			this(e, null, null); 
		}
		
		public DNode(T e, DNode<T> p, DNode<T> n) { 
			element = e; 
			prev = p; 
			next = n; 
		}
		
		// Methods
		public DNode<T> getPrev() {
			return prev;
		}
		public void setPrev(DNode<T> prev) {
			this.prev = prev;
		}
		public DNode<T> getNext() {
			return next;
		}
		public void setNext(DNode<T> next) {
			this.next = next;
		}
		public T getElement() {
			return element; 
		}

		public void setElement(T data) {
			element = data; 
		} 
		
		/**
		 * Just set all fields to null. 
		 */
		public void clean() { 
			element = null; 
			prev = next = null; 
		}
		
	}


}
