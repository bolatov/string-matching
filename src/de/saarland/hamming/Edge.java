package de.saarland.hamming;

/**
 * @author Almer Bolatov
 *         Date: 10/31/13
 *         Time: 1:15 PM
 */
public class Edge {
	private static final String TAG = Edge.class.getName();

	private int beginIndex;                    // can't be changed
	private int endIndex;
	private int stringIndex;
	private Node startNode;
	private Node endNode;                    // can't be changed

	/**
	 * Constructor
	 */
	public Edge(int stringIndex, int beginIndex, int endIndex, Node startNode) {
		Logger.log(TAG, String.format("Edge() stringIndex=%d, beginIndex=%d, endIndex=%d, startNode=?",
				stringIndex, endIndex));

		// TODO IMPLEMENT
	}
	//getSpan()						: int


	public void insert() {
		Logger.log(TAG, String.format("insert()"));

		// TODO IMPLEMENT
	}

	public void remove() {
		Logger.log(TAG, String.format("remove()"));

		// TODO IMPLEMENT
	}


	public Node splitEdge(int position) {
		Logger.log(TAG, String.format("splitEdge() position=%d", position));

		// TODO IMPLEMENT

		return null;
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
