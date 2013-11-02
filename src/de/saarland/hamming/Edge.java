package de.saarland.hamming;

import de.saarland.util.Logger;

/**
 * @author Almer Bolatov
 *         Date: 10/31/13
 *         Time: 1:15 PM
 */
public class Edge {
	private static final String TAG = Edge.class.getName();

	private int stringIndex;
	private int beginIndex;                    // can't be changed
	private int endIndex;
	private Node startNode;
	private Node endNode;                    // can't be changed

	/**
	 * Constructor
	 */
	public Edge(int stringIndex, int beginIndex, int endIndex, Node startNode) {
		Logger.log(TAG, String.format("Edge() stringIndex=%d, beginIndex=%d, endIndex=%d, startNode=?",
				stringIndex, beginIndex, endIndex));

		// TODO IMPLEMENT
		this.stringIndex = stringIndex;
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
		this.startNode = startNode;
		this.endNode = new Node(getStartNode().getTrie());
	}
	//getSpan()						: int

	public void insert() {
		Logger.log(TAG, String.format("insert()"));

		startNode.addEdge(stringIndex, beginIndex, this);
	}

	public void remove() {
		Logger.log(TAG, String.format("remove()"));

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
		Logger.log(TAG, String.format("splitEdge() position=%d", position));

		// remove current edge from the starting node
		this.remove();

		// create two edges splitting the old edge at 'position'
		Edge upperEdge = new Edge(stringIndex, beginIndex, beginIndex + position - 1, startNode);
		upperEdge.insert();

		Edge lowerEdge = this; //new Edge(stringIndex, beginIndex + position, endIndex, upperEdge.getEndNode());
		lowerEdge.startNode = upperEdge.getEndNode();
		lowerEdge.beginIndex += position;
		lowerEdge.insert();

		// the node connecting upper and lower edges
		return upperEdge.getEndNode();
	}


	public Edge deepCopy(Node node) {
		Edge edgeCopy = new Edge(stringIndex, beginIndex, endIndex, node);
		edgeCopy.endNode = endNode.deepCopy();

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
