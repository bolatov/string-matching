package de.saarland.hamming;

/**
 * @author Almer Bolatov
 *         Date: 10/31/13
 *         Time: 1:15 PM
 */
public class Edge {
	private static final String TAG = "Ed";//Edge.class.getSimpleName();

	// string index in the strings dictionary
	private int stringIndex;

	// begin index of the character in the string
	private int beginIndex;                    // can't be changed

	// end index of the character in the string
	private int endIndex;

	// start node of this edge
	private Node startNode;

	// end node of this edge
	private Node endNode;                    // can't be changed

	/**
	 * Constructor
	 */
	public Edge(int stringIndex, int beginIndex, int endIndex, Node startNode) {
		assert stringIndex >= 0;
		assert beginIndex >= 0;
		assert beginIndex <= endIndex;

		this.stringIndex = stringIndex;
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
		this.startNode = startNode;
		this.endNode = new Node(getStartNode().getTrie());
	}

	public int getSpan() {
		return this.endIndex - this.beginIndex;
	}

	public void insert() {
		startNode.addEdge(stringIndex, beginIndex, this);
	}

	public void remove() {
		startNode.removeEdge(stringIndex, beginIndex);
	}

	/**
	 * Let w be the child of v such that the first character of the label
	 * of the edge (v,w) is a. Define SUB(T,v,a) to be the tree obtained by
	 * first taking the subtree of T induced by v,w, and all the descendants of w.
	 * Furthermore, if the label of (v,w) contains only one character then the
	 * vertex v and the edge (v,w) are removed from SUB(T,v,a). Otherwise, the
	 * first character of the label of (v,w) is erased.
	 *
	 * @return subtree of start node
	 */
	public Node sub() {
		// if the edge contains only one character
		if (beginIndex == endIndex) {
			return endNode.deepCopy();
		} else {
			Node node = new Node(startNode.getTrie());
			Edge edge = new Edge(stringIndex, beginIndex+1, endIndex, node);
			edge.insert();
			edge.endNode = endNode.deepCopy();
			return node;
		}
	}

	/**
	 * Splits the edge.
	 *
	 * @param position - at which position to split
	 * @return - new node where edge was splitted
	 */
	public Node splitEdge(int position) {
//		Logger.log(TAG, String.format("splitEdge() position=%d", position));

		assert position > 0;
		assert beginIndex + position <= endIndex;

		// remove current edge from the starting node
		this.remove();

		// create two edges splitting the old edge at 'position'
		Edge upperEdge = new Edge(stringIndex, beginIndex, beginIndex + position - 1, startNode);
		upperEdge.insert();

		Edge lowerEdge = this;
		lowerEdge.startNode = upperEdge.getEndNode();
		lowerEdge.beginIndex += position;
		lowerEdge.insert();

		// the node connecting upper and lower edges
		return upperEdge.getEndNode();
	}


	/**
	 * @param node start node to append copied edge to
	 * @return Deep copy of itself
	 */
	public Edge deepCopy(Node node) {
		Edge edgeCopy = new Edge(stringIndex, beginIndex, endIndex, node);
		edgeCopy.endNode = endNode.deepCopy();
		// test
		edgeCopy.insert();
		//end test

		return edgeCopy;
	}

	public int getBeginIndex() {
		return beginIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public int getStringIndex() {
		return stringIndex;
	}

	public Node getStartNode() {
		return startNode;
	}

	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}

	public Node getEndNode() {
		return endNode;
	}
}
