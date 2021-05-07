package linkedLists;
/**
 * Singly linked list with references to its first and its
 * last node. 
 *
 * @author pirvos
 *
 */
import interfaces.Node;

import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * Implementations of SFLList functions.
 *
 * @author Andre A. Clavell Alvarez
 * CIIC4020-086
 * 843-15-1170
 *
 */

public class SLFLList<E> extends AbstractSLList<E> {
	private SNode<E> first, last;   // reference to the first node and to the last node
	int length;

	public SLFLList() {       // to create an empty list instance
		first = last = null;
		length = 0;
	}

	/**
	 * Adds a node to the beginning of the list
	 * and changes the reference of the first node
	 * to the "nuevo" node.
	 *
	 * @param nuevo
	 */
	public void addFirstNode(Node<E> nuevo) {

		((SNode<E>) nuevo).setNext(first);
		first = (SNode<E>) nuevo;
		if(last==null){
			last = first;
		}
		length++;

	}

	/**
	 * Adds a node after the target node.
	 *
	 * @param target
	 * @param nuevo
	 */
	public void addNodeAfter(Node<E> target, Node<E> nuevo) {
		/*
		 * By using the assumption that the target node already exists in the list
		 * we can avoid using a while loop helping with the time complexity.
		 * */
		if (target == last) {
			addLastNode(nuevo);
		} else {
			((SNode<E>) nuevo).setNext(((SNode<E>) target).getNext());
			((SNode<E>) target).setNext((SNode<E>) nuevo);
			length++;
		}

	}

	/**
	 * Adds node before target node;
	 * @param target
	 * @param nuevo
	 */
	public void addNodeBefore(Node<E> target, Node<E> nuevo) {
		/*
		* if the target node turns out to be the same as the node saved in the "first" reference we
		* re-use the addFirstNode() code,
		* otherwise we utilize the getNodeBefore() then we call the addNodeAfter() function to be resourceful
		* with the code that is available.
		* */
		if (target == first)
			this.addFirstNode(nuevo);
		else {
			Node<E> prevNode = getNodeBefore(target);
			this.addNodeAfter(prevNode, nuevo);
		}
	}

	/**
	 * returns the node that is saved in the "first" node reference.
	 * @return Node<E>
	 */
	public Node<E> getFirstNode() {
		if(first==null){
			throw new NoSuchElementException("getFirstNode() : linked list is empty");
		}
		return first;
	}

	/**
	 * returns the node that is saved in the "last" node reference.
	 * @return Node<E>
	 */
	public Node<E> getLastNode() {
			if(first==null){
				throw new NoSuchElementException("getFirstNode() : linked list is empty");
			}
		return last;
	}

	/**
	 * returns the node after the target node.
	 * @param target
	 * @return Node<E>
	 */
	public Node<E> getNodeAfter(Node<E> target) {
	//We assume the target node already exists in the list to help with time complexity.
		return ((SNode<E>) target).getNext();
	}

	/**
	 * returns the node before the target node.
	 * @param target
	 * @return Node<E>
	 */
	public Node<E> getNodeBefore(Node<E> target) {
		/*
		* If the target node is the same as the first node,
		*  then we return null since this is a singly linked list.
		* Otherwise we must go through the list and find the previous node,
		*  with this type of implementation we notice how doubly linked lists are more efficient.
		* */
		if(target == first){
			return null;
		}
		SNode<E> current = first;
		while(current!=null && current.getNext() != target)
			current = current.getNext();
		return current;
	}

	/**
	 * returns the current list length.
	 * @return length
	 */
	public int length() {
		return length;
	}

	/**
	 * removes the target node
	 * @param target
	 */
	public void removeNode(Node<E> target) {
		/* we use the assumption that the target node exists in the list,
		* to help with simplifying the time complexity.
		* */
		if(target == first){
			first = first.getNext();
		}else{
			SNode<E> prevNode = (SNode<E>) this.getNodeBefore(target);
			prevNode.setNext(((SNode<E>) target).getNext());
		}
		((SNode<E>)target).clean();
		length--;
	}

	public Node<E> createNewNode() {
		return new SNode<E>();
	}

	/**
	 * returns a Element iterator.
	 * @return Iterator<E>
	 */
	@Override
	public Iterator<E> iterator() {
		return new ElementsIterator();

	}

	/**
	 * adds new node to the end of the list.
	 * @param newNode
	 */
	@Override
	public void addLastNode(Node<E> newNode) {
		/*
		* By having a reference to the last node we can make this function more efficient
		* than going into a loop to get to it.
		* */
		SNode<E> ntba = (SNode<E>) newNode;
		last.setNext(ntba);
		last = ntba;
		length++;

	}

	/**
	 * returns a Node iterable
	 * @return Iterable<Node<E>>
	 */
	@Override
	public Iterable<Node<E>> nodes() {
		return new NodesIterable();
	}

	/**
	 * returns a light clone of the target list.
	 * @return SFLList<E>
	 */
	@Override
	public SLFLList<E> clone() {
		SLFLList<E> listClone = new SLFLList<>();
		Iterator<Node<E>> itr = new NodesIterator();
		while(itr.hasNext())
				listClone.addLastNode(new SNode<>(itr.next().getElement()));
		return listClone;
	}

	/**
	 * private class to enable iteration over the list.
	 * @author Andre Clavell
	 */
	private class NodesIterable implements Iterable<Node<E>>{

		@Override
		public Iterator<Node<E>> iterator() {
			return new NodesIterator();
		}
	}
	private class NodesIterator implements Iterator<Node<E>>{

		private SNode<E> curr = first;
		private SNode<E> ptntr = null;
		private boolean canRemove = false;

		/**
		 * returns true if there exists a node after the current node.
		 * @return boolean
		 */
		@Override
		public boolean hasNext() {
			return curr!=null;
		}

		/**
		 * returns the current Node.
		 * @return Node<E>
		 */
		@Override
		public Node<E> next() {
			if(!hasNext())
				throw new NoSuchElementException("Iterator completed.");
			if(canRemove)
				ptntr = (ptntr == null ? first : ptntr.getNext());//saves a node for the remove function
			canRemove = true; //enables the ability to remove a node in the iteration
			SNode<E> ntr = curr;
			curr = curr.getNext();
			return ntr;
		}

		/**
		 * removes the node that is contained in the ptntr Node.
		 */
		public void remove(){
			if(!canRemove)
				throw new IllegalStateException("Not valid to remove.");
			if(ptntr == null)
				first = first.getNext();
			else
				ptntr.setNext(ptntr.getNext().getNext());
			length--;
			canRemove = false;
		}
	}

	private class ElementsIterator implements Iterator<E>{

		NodesIterator nodesIter = new NodesIterator();

		@Override
		public boolean hasNext() {
			return nodesIter.hasNext();
		}

		@Override
		public E next() {
			return nodesIter.next().getElement();
		}

		@Override
		public void remove() {
			nodesIter.remove();
		}
	}

	/**
	 * Class to represent a node of the type used in singly linked lists. 
	 * @author pedroirivera-vega
	 *
	 * @param <T> Data type of element in a node.
	 */
	protected static class SNode<T> implements interfaces.Node<T> {
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

}
