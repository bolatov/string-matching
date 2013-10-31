package de.saarland.hamming;

import java.util.Map;
import java.util.Set;

/**
 * @author Almer Bolatov
 *         Date: 10/31/13
 *         Time: 1:14 PM
 */
public class Node {
	private static final String TAG = Node.class.getName();

	private Trie trie;
	private Map<Character, Edge> edges;
	private int weight;
	private int depth;                          // relative depth in the heavy path
	private Edge heavyEdge;                       // Either Node or Edge
	private Set<Integer> values;
	private GroupNode groupType1;                // head element of type 1 group trees
	private GroupNode groupType2;               // head element of type 2 group trees

	/* Constructor */
	public Node(int stringIndex, Trie trie) {
		Logger.log(TAG, String.format("Node() stringIndex=%d, trie=:)", stringIndex));
	}

	public void addEdge(int stringIndex, int charIndex, Edge edge) {
		Logger.log(TAG, String.format("addEdge() stringIndex=%d, charIndex=%d, edge.beginIndex=%d, edge.endIndex=%d",
				stringIndex, charIndex, edge.getBeginIndex(), edge.getEndIndex()));
	}

	public char charAt(int stringIndex, int charIndex) {
		Logger.log(TAG, String.format("charAt() stringIndex=%d, charIndex=%d", stringIndex, charIndex));
		// TODO IMPLEMENT
		return '?';
	}

	public Edge findEdge(char character) {
		Logger.log(TAG, String.format("findEdge() character=%c", character));

		// TODO IMPLEMENT

		return null;
	}

	public void removeEdge(int stringIndex, int charIndex) {
		Logger.log(TAG, String.format("removeEdge() stringIndex=%d, charIndex=%d", stringIndex, charIndex));
		// TODO IMPLEMENT
	}

	public void buildMismatchesIndex(int k) {
		Logger.log(TAG, String.format("buildMismatchesIndex() k=%d", k));

		// TODO IMPLEMENT
	}

	public void doHeavyPathDecomposition() {
		Logger.log(TAG, String.format("doHeavyPathDecomposition()"));

		// TODO IMPLEMENT
	}

	public void calculateWeights() {
		Logger.log(TAG, String.format("calculateWeights()"));

		// TODO IMPLEMENT
	}

	public static Node mergeNodes(Node m, Node n) {
		Logger.log(TAG, String.format("mergeNodes() m.depth=%d, n.depth=%d", m.getDepth(), n.getDepth()));

		// TODO IMPLEMENT

		return null;
	}

	public Set<Integer> search(String query, int startIndex) {
		Logger.log(TAG, String.format("search() query=%s, startIndex=%d", query, startIndex));

		// TODO IMPLEMENT

		return null;
	}

	public void addValue(int value) {
		this.values.add(value);
	}

	public Trie getTrie() {
		return trie;
	}

	public Map<Character, Edge> getEdges() {
		return edges;
	}

	public int getWeight() {
		return weight;
	}

	public int getDepth() {
		return depth;
	}

	public Edge getHeavyEdge() {
		return heavyEdge;
	}

	public void setValues(Set<Integer> values) {
		this.values = values;
	}
}
