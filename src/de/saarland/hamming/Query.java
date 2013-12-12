package de.saarland.hamming;

/**
 * @author Almer Bolatov
 *         Date: 11/22/13
 *         Time: 3:40 PM
 */
public class Query {
	private Node node;
	private int start;
	private int k;

	public Query() {}

	public Query(Node n, int start, int k) {
		this.node = n;
		this.start = start;
		this.k = k;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node n) {
		this.node = n;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}
}
