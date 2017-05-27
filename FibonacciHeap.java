//import FibonacciHeap.HeapNode;

/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over non-negative integers.
 *
 */
public class FibonacciHeap
{
	public HeapNode heapMin;
	public int size;
	public int markedNum;
	public static int cuts=0;
	public static int links=0;
	
	public FibonacciHeap(){//create a new Fibonacci Heap
		this.heapMin = null;
		this.size = 0;
	}

	
   /**
    * public boolean empty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    *   
    */
    public boolean empty()
    {
    	if (this.heapMin == null)// if the heapMin doesn't point at any node, the heap is empty
    		return true;
    	return false; 
    }
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap. 
    */
    public HeapNode insert(int key)
    {   
    	HeapNode newNode = new HeapNode(key);
		rootListConcat(newNode); // we add the new node to the list of roots of the heap
		if (key<heapMin.key) // update the heapMin
			heapMin = newNode;
		this.size++; // update size of heap (+1)
    	return newNode; 
    }

    
    /**
     * public void linkedListConcat
     * 
     * receives a node and concatenates the root-list he is in, with the root list of this.
     * 
     * @param node
     */
    public void rootListConcat(HeapNode node){//node is a part of linkedlist2
    	if (this.heapMin == null){//the tree is empty
    		this.heapMin = node;
    		return;
    	}
    	HeapNode temp1 = this.heapMin.left; // this.heapMin is a part of linklist1
    	HeapNode temp2 = node.right;//this will be the last node in linkedlist2
    	
    	//concatenating two linked-lists:
    	this.heapMin.left = node;
    	node.right = heapMin;
    	temp2.left = temp1;
    	temp1.right = temp2;
    }
    
    /**
     * public void SingleNodeConcat
     * 
     * receives a node that was a part of a chuldren's list of another node and concatenates it to the root list
     *  
     * @param node
     */
    public void SingleNodeConcat(HeapNode node){//adding node to root list
    	   
    	if (this.heapMin == null)//the tree is empty
        	{
        		this.heapMin = node;
        		return;
        	}
        	HeapNode temp1 = this.heapMin.left;//this node will be at the node's left at the end
        	this.heapMin.left = node;//node <-- heapMin
        	node.right = heapMin;//node --> heapMin
        	node.left = temp1;//heapMin.left <-- node
        	temp1.right = node;//heapMin --> node 
        }


    /*
 	* public void rootListRemove(HeapNode node)
 	* 
 	* recieve a node and removes it from the list of roots
 	* 
 	* @param node
 	*/
    public void rootListRemove(HeapNode node){
    	if (size == 1)//this is the only node in the linked list
    		this.heapMin = null;
    	
    	else // this is not the last node in the list
    	{
    		node.right.left = node.left;//update siblings
    		node.left.right = node.right;//update siblings
    	}
    }
    

   /**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key.
    * Successively link the roots of the heap after deleting the heapMin.
    *
    */
    public void deleteMin()
    {
    	if (this.heapMin!=null)
    	{
    		
    		HeapNode child = this.heapMin.child;
    		if (child != null){ // we add all of the heapMin's children to the root-list
    			HeapNode start = child;
        		child.parent = null;
    			while(child.left != start)//iterating over all of the node's children
    			{
    				child = child.left;
    				child.parent = null;
    			}
    			rootListConcat(child);// the children of heapMin are connected to one another by a doubly linked-list. 
    			//now the children of the heapMin are roots in the heap root-list
    		}
    		if (this.heapMin.left == this.heapMin) // there is only one node in the tree
    		{
    			this.heapMin = null;
    			this.size --;
    			return;
    		}
    		rootListRemove(this.heapMin);// the heapMin is now deleted from the root-list, hence from the heap
    		this.heapMin = this.heapMin.right; // we appoint a temporary heapMin
    		SuccessiveLinking(); 
    		this.size --;// update the size after deleting a node (-1)
    	}
     	return; 	
    }

    
    /**
     * public HeapNode SuccessiveLinking()
     * 
     *Successive links the roots of the fibonacci heap, so that there are no two trees with the same rank
     *  
     */    
    public void SuccessiveLinking(){
    	int k = 42;
    	HeapNode[] arrayOfRoots = new HeapNode[k];
    	HeapNode x = heapMin;
    	HeapNode next = x;
    	HeapNode a = x;
     	do //Iterating over the root list
    	{
     		a=x;
    		int d = x.rank;
    		x.parent = null;
    		next = x.left;
    		while (arrayOfRoots[d]!=null)// we check if we found a tree with the same rank by now. if so - we link them
    		{
    			HeapNode y = arrayOfRoots[d];	// y<- the root of the tree with rank d 
    			a=fibHeapLink(y,a); // a <- linked tree where the rank of the root is d+1
    			arrayOfRoots[d] = null; // we currently have no trees with rank d;
    			d ++;
    		}// iterating over array[]
    		arrayOfRoots[d] = a; // we put a in the correct cell
    		x=next;
    	}while(x != heapMin);// iterating over rootList
    	this.heapMin = null;
    	rebuildHeap(arrayOfRoots);
    	return;
    }//SuccessiveLinking

    /*
     * public void rebuildHeap(HeapNode[] arrayOfRoots) 
     * 
     * receives an array of HeapNodes, and adds all of the HeapNodes to this's root-list
     * updates HeapMin to be the node with the minimal key 
     * 
     */
	public void rebuildHeap(HeapNode[] arrayOfRoots) {
		for (int i = 0; i< arrayOfRoots.length ; i ++){//rebuilding the rootList and finding new heapMin
    		if (arrayOfRoots[i]!=null){
    			arrayOfRoots[i].left = arrayOfRoots[i];//updating the siblings for rootListConcat
    			arrayOfRoots[i].right = arrayOfRoots[i];//updating the siblings for rootListConcat
    			rootListConcat(arrayOfRoots[i]); // ads the HeapNode in cell number i to the root-list
    			if (this.heapMin == null || arrayOfRoots[i].key < this.heapMin.key) // updating HeapMin
    				this.heapMin = arrayOfRoots[i];
    		}// if
    	}//for
	}
    
/**
 * public void fibHeapLink(HeapNode y, HeapNode x)//FIX THIS
 * 
 * recieves to HeapNode's, y.key > x.key, and makes y a child of x
 * 
 * @param y
 * @param x
 */
    public HeapNode fibHeapLink(HeapNode y, HeapNode x){
    if (y.key <= x.key){ //we want x to be the node with the minimal key
    	HeapNode temp = x;
		x = y;
		y = temp;
    }
    HeapNode temp = y;
    HeapNode t1 = x;    
    x.rank +=1; // we are going to add a child to x, so the rank (which is the number of children) will be +1
    x.childListConcat(temp); // we add y to be one of x's children
    t1.child = temp;
    temp.parent = x;
    links++;
    return x;
    }
    
   /**
    * public HeapNode findMin()
    *
    * Return the node of the heap whose key is minimal. 
    *
    */
    public HeapNode findMin()
    {
    	return this.heapMin;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	if (heap2.empty())// if heap2 is empty, we don't make any changes
    		return;
    	if (this.heapMin == null || this.heapMin.key > heap2.heapMin.key)//updating the root with the minimal key in the new heap
    		this.heapMin =heap2.heapMin;
    	rootListConcat(heap2.heapMin); // we add the the root of heap2 to this's root-list
    	this.size = this.size + heap2.size; // update size accordingly
    	return;    		
    }

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *   
    */
    public int size()
    {
    	return this.size; 
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * 
    */
    public int[] countersRep()
    {
	int[] arr = new int[42];
	HeapNode x = heapMin;
	if (x != null){
		int rank = x.rank;
		arr[rank]++;
		while (x.right != heapMin){//iterating over rootList  
			x=x.right;
			rank = x.rank;
			arr[rank]++;
			
		}
	}
        return arr; 
    }

   /**
    * public void arrayToHeap()
    *
    * Insert the array to the heap. Delete previous elements in the heap.
    * 
    */
    public void arrayToHeap(int[] array)
    {
    	this.heapMin = null; // deletes all elements in the heap
    	this.size = 0; // the size of the heap is now 0
    	for (int i = 0; i <array.length; i++)
    		this.insert(array[i]);   	
        return; //	 to be replaced by student code
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap. 
    *
    */
    public void delete(HeapNode x) 
    {   
    	double inf = Double.NEGATIVE_INFINITY; 
    	decreaseKey(x, (int) inf);
    	this.deleteMin();
    	
    	return; 
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * The function decreases the key of the node x by delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta) 
    {    
    	x.key -= delta;
    	HeapNode parent = x.parent;
    	if ((parent != null) &&(x.key < parent.key)){//if x is not a root and his new key is smaller than his parent's
    		cut(x, parent);
    		cascadingCut(parent);
    	}
    	if (x.key < heapMin.key)//if x's key is smaller than heapMin's key, replace them
    		this.heapMin = x;
    	return;
    }
    /**
     * public void cut(HeapNode x, HeapNode y)
     *
     * The function deletes the node x from the children list of his parent y and add it to the roots list
     * then updating the number of marked nodes and x's bit to false
     * 
     */
    public void cut(HeapNode x, HeapNode y){
    	x.parent = null;
    	y.rank--;
    	if (x.bit)
    		markedNum--;
    	x.bit = false;
    	
    	//deleting x from the child list of y
    	if (x.right == x)//if x is an only child
    		y.child = null;
    	else{
    		y.child = x.right;
    		x.right.left = x.left;
    		x.left.right = x.right;
    	}
    	SingleNodeConcat(x);//add x to root list

    	cuts++;
    	
    }
    /**
     * public void cascadingCut(HeapNode y)
     *
     * The function checks if other cuts should be made along the tree 
     * 
     */
    public void cascadingCut(HeapNode y){
    	HeapNode z = y.parent;
    	if (z !=null){//if y isn't a root
    		if (y.bit == false){ //if x was the first child of y that was cut
            	y.bit=true;
            	markedNum++;
    		}
    		else{	//if x was the second child of y that was cut
    			cut(y,z);
    			cascadingCut(z);
    	}
    }
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {   
    	return treesNum()+2*markedNum; 
    }
    
    
    
    /**
     * public int treesNum() 
     *
     * This function returns the number of trees in the heap 
     */
    public int treesNum(){
    	int counter=0;
    	HeapNode x = heapMin;
    	if (heapMin != null){// if list isn't empty
    		counter++;
    		while (x.right != heapMin){//iterating over the roots list and incrementing counter
    		x = x.right;
    		counter++;
    		}
    	}
		return counter;
    	
    }
   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the run-time of the program.
    * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
    * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
    * in its root.
    */
    public static int totalLinks()
    {    
    	return links; 
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the run-time of the program.
    * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return cuts; 
    }

   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in 
    * another file 
    *  
    */
    public class HeapNode{
    	public int key;
    	public HeapNode child;
    	public HeapNode parent;
    	public HeapNode right;
    	public HeapNode left;
    	boolean bit = false;
    	public int rank;
    	
    	
    	public HeapNode(int key){
    	this.key = key;
    	this.left = this;// the siblings are the node itself
    	this.right =this;// the siblings are the node itself
    	}
    	
    	/**
    	 * public int getKey()
    	 * 
    	 * returns this's key
    	 *  
    	 */
    	public int getKey()
    	{
    		return this.key;
    	}
    	
    /*
     * childListConcat(HeapNode node)
     * 
     * receives a node which is a part of a linked-list
     * adds node to the children's list of this
     * 
     */
    public void childListConcat(HeapNode node){//node -> a part of linkedlist2
    	if (node.parent == null){//node is a root
    		node.left = node;
    		node.right = node;
    	}
    	if (this.child == null){ // this does not have any children
    		return;
    	}
        HeapNode temp = this.child.right;
        this.child.right = node.right;
        this.child.right.left = this.child;
        node.right = temp;
        node.right.left = node;
    	node.parent = this;  	
    	}
    }
}
    
    
	

