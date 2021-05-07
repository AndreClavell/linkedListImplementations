package interfaces;

public interface LinkedList<E> extends Iterable<E>, Cloneable {
	
	/**
	 * Determines the number of nodes currently in the linked list. 
	 * @return integer value (>= 0) corresponding to the number of
	 * nodes actually in the linked list.
	 */
	int length(); 

	/**
	 * 
	 * @param target
	 * @return
	 */
	Node<E> getNodeBefore(Node<E> target); 
	
	/**
	 * 
	 * @param target
	 * @return
     */
	Node<E> getNodeAfter(Node<E> target); 
	
	/**
	 *  @return reference to the first node of the linked list
	 */
	Node<E> getFirstNode(); 
	
	/**
	 * 
	 * @return
	 */
	Node<E> getLastNode(); 
	
	/**
	 * 
	 * @param nuevo
	 */
	void addFirstNode(Node<E> nuevo); 
	
	/**
	 * 
	 * @param newNode
	 */
	void addLastNode(Node<E> newNode); 
	
	/**
	 * 
	 * @param target
	 * @param nuevo
	 */
	void addNodeAfter(Node<E> target, Node<E> nuevo); 
	
	/**
	 * 
	 * @param target
	 * @param nuevo
	 */
	void addNodeBefore(Node<E> target, Node<E> nuevo); 
		
	/**
	 * 
	 * @param target
	 */
	void removeNode(Node<E> target); 
	
		
	/**
	 * Creates a new node instance of the type of nodes that the linked list
	 * uses. The new node will have all its instance fields initialized to
	 * null. The new node is not linked to the list in any way.
	 * @return reference to the new node instance. 
	 */
	Node<E> createNewNode(); 
	
	
	/**
	 * Returns an Iterable<Node<E>> object that allows iteration over the 
	 * nodes in the linked list. 
	 */
	Iterable<Node<E>> nodes();

	Object clone() throws CloneNotSupportedException;

}
