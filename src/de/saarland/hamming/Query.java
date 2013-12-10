package de.saarland.hamming;

/**
 * @author Almer Bolatov
 *         Date: 11/22/13
 *         Time: 3:40 PM
 */
public class Query {
	private Node node;
	private char[] q;
	private int start;
	private int k;
//	private String id;

	public Query(Node n, char[] q, int start, int k) {
		this.node = n;
		this.q = q;
		this.start = start;
		this.k = k;
	}

//	public Query(Searchable s, char[] q, int start, int k, String id) {
//		this.searchable = s;
//		this.q = q;
//		this.start = start;
//		this.k = k;
//		this.id = id;
//	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node n) {
		this.node = n;
	}

	public char[] getQ() {
		return q;
	}

	public void setQ(char[] q) {
		this.q = q;
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

//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
}
