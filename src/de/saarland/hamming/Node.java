package de.saarland.hamming;

import de.saarland.util.Logger;

import java.util.*;

/**
 * @author Almer Bolatov
 *         Date: 10/31/13
 *         Time: 1:14 PM
 */
public class Node {
	private static final String TAG = Node.class.getName();

	private int name;

	private Trie trie;
	private Map<Character, Edge> edges;
	private int weight;
	private int depth;                          // relative depth in the heavy path
	private Edge heavyEdge;                     // Either Node or Edge
	private Set<Integer> values;
	private GroupNode groupType1;               // head element of type 1 group trees
	private GroupNode groupType2;               // head element of type 2 group trees

	/* Constructor */
	public Node(Trie trie) {
		this.name = trie.getNewNodeNumber();
		Logger.log(TAG, String.format("Node() name=%d", name));

		this.trie = trie;
		this.edges = new HashMap<>();
		this.weight = 1;
		this.depth = 0;
		this.values = new HashSet<>();
	}

	public void addEdge(int stringIndex, int charIndex, Edge edge) {
		Logger.log(TAG, String.format("addEdge() stringIndex=%d, charIndex=%d, edge.beginIndex=%d, edge.endIndex=%d",
				stringIndex, charIndex, edge.getBeginIndex(), edge.getEndIndex()));
		char key = charAt(stringIndex, charIndex);

		if (edges.containsKey(key)) {
			// TODO REMOVE AFTERWARDS
			Logger.err(TAG, String.format("Node name=%d already has edge for character=%c", name, key));
		}

		edges.put(key, edge);
	}

	public char charAt(int stringIndex, int charIndex) {
		Logger.log(TAG, String.format("charAt() string=%s, stringIndex=%d, charIndex=%d",
				new String(trie.getString(stringIndex)),
				stringIndex,
				charIndex));

		if (trie == null) {
			System.err.println("Trie is null here!");
		}

		return trie.getString(stringIndex)[charIndex];
	}

	public Edge findEdge(char ch) {
		Logger.log(TAG, String.format("findEdge() character=%c", ch));

		return edges.get(ch);
	}

	public void removeEdge(int stringIndex, int charIndex) {
		Logger.log(TAG, String.format("removeEdge() stringIndex=%d, charIndex=%d", stringIndex, charIndex));
		edges.remove(charAt(stringIndex, charIndex));
	}

	public void buildMismatchesIndex(int k) {
		Logger.log(TAG, String.format("buildMismatchesIndex() k=%d", k));

		// TODO IMPLEMENT
		doHeavyPathDecomposition();
	}

	public void doHeavyPathDecomposition() {
		Logger.log(TAG, String.format("doHeavyPathDecomposition()"));

		calculateWeights();

		Queue<Node> queue = new LinkedList<>();
		queue.add(this);

		// TODO set depths
		this.depth = 0;

		while (!queue.isEmpty()) {
			Node node = queue.remove();
			if (!node.isLeaf()) {
				Collection<Edge> outEdges = node.getEdges();
				Edge heavy = null;
				int maxWeight = 0;
				for (Edge e : outEdges) {
					Node endNode = e.getEndNode();
					if (endNode.getWeight() > maxWeight) {
						heavy = e;
						maxWeight = endNode.getWeight();
					}

					endNode.depth = 0;
					queue.add(endNode);
				}
				node.setHeavyEdge(heavy);
				heavy.getEndNode().depth = node.depth + 1;
			}
		}
	}

	public void calculateWeights() {
		Logger.log(TAG, String.format("calculateWeights()"));

		this.weight = dfs();
	}

	private int dfs() {
		Collection<Edge> outEdges = getEdges();
		if (outEdges.isEmpty()) {
			return 1;
		} else {
			int leaves = 0;
			for (Edge e : outEdges) {
				Node endNode = e.getEndNode();
				int branchLeaves = endNode.dfs();
				endNode.setWeight(branchLeaves);
				leaves += branchLeaves;
			}

			return leaves;
		}
	}

	public Node deepCopy() {
		Node nodeCopy = new Node(trie);

		for (Edge e : getEdges()) {
			Edge edgeCopy = e.deepCopy(nodeCopy);
			edgeCopy.setStartNode(nodeCopy);
//			edgeCopy.insert();
		}

		nodeCopy.addValues(values);

		return nodeCopy;
	}

	public static Node mergeNodes(Node m, Node n) {
		Logger.log(TAG, String.format("mergeNodes() m.name=%d, n.name=%d", m.name, n.name));

		Set<Character> mNextChars = m.nextChars();
		Set<Character> nNextChars = n.nextChars();

		Set<Character> allNextChars = new HashSet<>();
		allNextChars.addAll(mNextChars);
		allNextChars.addAll(nNextChars);

		Trie t = m.getTrie();
		Node merged = new Node(t);

		// TODO IMPLEMENT
		for (Character ch : allNextChars) {
			if (mNextChars.contains(ch) && nNextChars.contains(ch)) {
				Edge mEdge = m.findEdge(ch).deepCopy(merged);
				Edge nEdge = n.findEdge(ch).deepCopy(merged);

				char[] mString = t.getString(mEdge.getStringIndex());
				char[] nString = t.getString(nEdge.getStringIndex());

				int minLength = Math.min(mEdge.getSpan(), nEdge.getSpan());

				assert minLength >= 0;

				int offset = 1;         // can start from the next char since first ones are same
				int mi = mEdge.getBeginIndex() + offset;
				int ni = nEdge.getBeginIndex() + offset;
				int position = offset;
				for (int i = offset; i <= minLength; i++, mi++, ni++) {
					if (mString[mi] != nString[ni]) {
						break;
					}
					position++;
				}

				// split edge if the place where the characters differ
				// is somewhere on the edge. Otherwise take an end node
				Node mEndNode = (mi <= mEdge.getEndIndex()) ? mEdge.splitEdge(position) : mEdge.getEndNode();
				Node nEndNode = (ni <= nEdge.getEndIndex()) ? nEdge.splitEdge(position) : nEdge.getEndNode();

				Node mergedSubNode = Node.mergeNodes(mEndNode, nEndNode);

				Node mergedEndNode = merged.findEdge(ch).getEndNode();
				for (Edge endEdge : mergedSubNode.getEdges()) {
					mergedEndNode.addEdge(endEdge.getStringIndex(), endEdge.getBeginIndex(), endEdge);
				}
				mergedEndNode.addValues(mergedSubNode.getValues());
			} else if (mNextChars.contains(ch)) {
				Edge edge = m.findEdge(ch).deepCopy(merged);
				edge.insert();
			} else {
				Edge edge = n.findEdge(ch).deepCopy(merged);
				edge.insert();
			}
		}

		// TODO add values
		merged.addValues(m.values);
		merged.addValues(n.values);

		return merged;
	}

	public Set<Integer> search(String query, int startIndex) {
		Logger.log(TAG, String.format("search() query=%s, startIndex=%d", query, startIndex));

		// TODO IMPLEMENT

		return null;
	}

	public Trie getTrie() {
		return trie;
	}

	public Set<Character> nextChars() {
		return edges.keySet();
	}

	public Collection<Edge> getEdges() {
		return edges.values();
	}

	private int getWeight() {
		return weight;
	}

	private void setWeight(int weight) {
		this.weight = weight;
	}

	public int getDepth() {
		return depth;
	}

	private Edge getHeavyEdge() {
		return heavyEdge;
	}

	private void setHeavyEdge(Edge edge) {
		this.heavyEdge = edge;
	}

	public void addValue(int value) {
		values.add(value);
	}

	public void addValues(Set<Integer> values) {
		this.values.addAll(values);
	}

	public Set<Integer> getValues() {
		return values;
	}

	public boolean isLeaf() {
		return edges.isEmpty();
	}
}
