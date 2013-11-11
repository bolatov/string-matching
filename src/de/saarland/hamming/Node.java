package de.saarland.hamming;

import de.saarland.util.Logger;

import java.util.*;

/**
 * @author Almer Bolatov
 *         Date: 10/31/13
 *         Time: 1:14 PM
 */
public class Node {
	private static final String TAG = Node.class.getSimpleName();

	private int name;

	private Trie trie;
	private Map<Character, Edge> edges;
	private int weight;                         // number of leaves hanging from this node
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
		this.values = new HashSet<>();
	}

	public void addEdge(int stringIndex, int charIndex, Edge edge) {
		Logger.log(TAG, String.format("addEdge() stringIndex=%d, charIndex=%d, edge.beginIndex=%d, edge.endIndex=%d",
				stringIndex, charIndex, edge.getBeginIndex(), edge.getEndIndex()));
		char key = charAt(stringIndex, charIndex);

		if (edges.containsKey(key)) {
			// TODO REMOVE AFTERWARDS
			Logger.err(TAG, String.format("addEdge(): Node name=%d already has edge for character=%c", name, key));
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

		if (k <= 0) {
			Logger.log(TAG, String.format(" k<=0 Nothing to do here"));
			return;
		}

		// TODO
		// possibly no need to do this step for
		// next iteration of k-1 mismatches building
		doHeavyPathDecomposition();

		/**
		 * Traverse each heavy path in a BFS manner each time
		 * adding a head of a heavy path to the queue.
		 *
		 * TYPE 1
		 * For each heavy path build a type 1 group trees.
		 *
		 * TYPE 2
		 * Build type 2 group trees for the off-path children
		 * of a heavy node v_i.
		 */

		Queue<Node> heavyQueue = new LinkedList<>();
		heavyQueue.add(this);

		while (!heavyQueue.isEmpty()) {

			final Node head = heavyQueue.remove();

			int headStringIndex = head.heavyEdge.getStringIndex();
			int headBeginIndex = head.heavyEdge.getBeginIndex();

			Node current = head;

			List<GroupNode> onPathVertices = new ArrayList<>();

			// Traverse vertices along the heavy path
			while (!current.isLeaf()) {

				Node mergedChildren = null;

				List<GroupNode> offPathChildren = new ArrayList<>();

				// only off-path children
				for (char nextChar : current.nextChars()) {
					Edge edge = current.findEdge(nextChar);

					if (!edge.equals(current.getHeavyEdge())) {

						Node subNode = edge.sub();
						mergedChildren = (mergedChildren == null) ? subNode : Node.mergeNodes(mergedChildren, subNode);

						// type 2
						GroupNode offPathGroupNode = new GroupNode(GroupNode.GroupType.TWO);
						offPathGroupNode.setId(nextChar + "");
						offPathGroupNode.setNode(subNode);
						offPathChildren.add(offPathGroupNode);

						// BFS
						Node offPathChild = edge.getEndNode();
						if (!offPathChild.isLeaf()) {
							heavyQueue.add(offPathChild);
						}
					}
				}

				// Set type 2 group tree that are built from off-path vertices
				Logger.log(TAG, String.format("buildMismatchesIndex(): build type 2 group trees"));
				GroupNode type2GroupNode = GroupNode.buildGroup(offPathChildren);
				current.groupType2 = type2GroupNode;

				if (k-1 > 0) {
					type2GroupNode.buildMismatchesIndex(k-1);
				}

				assert mergedChildren != null;

				Node errTV = new Node(current.trie);
				// begin
				Node endNode = null;
				// TODO if stringIndex != stringIndex, create new edge
				int endIndex = headBeginIndex - 1;
				Node tempNode = head;
				for (int i = 0; i < current.depth; i++) {
					endIndex = tempNode.heavyEdge.getEndIndex();
					tempNode = tempNode.heavyEdge.getEndNode();
				}
				// take nextChar that points to the heavy edge
				endIndex++;

				Edge errTVEdge = new Edge(headStringIndex, headBeginIndex, endIndex, errTV);
				errTVEdge.insert();

//				assert endNode != null;

				for (char ch : mergedChildren.nextChars()) {
					Edge edge = mergedChildren.findEdge(ch);
					endNode.edges.put(ch, edge);
				}
				// end
				GroupNode.GroupType type = GroupNode.GroupType.ONE;
				GroupNode onPathVertex = new GroupNode(type);
				onPathVertex.setId(current.getDepth() + "");
				onPathVertex.setNode(errTV);

				onPathVertices.add(onPathVertex);

				// next vertex on the heavy path
				current = current.getHeavyEdge().getEndNode();
			}


			// Set type 1 group tree to vertices along the heavy path
			Logger.log(TAG, String.format("buildMismatchesIndex(): build type 1 group trees"));
			GroupNode type1GroupNode = GroupNode.buildGroup(onPathVertices);
			current = head;
			while (!current.isLeaf()) {
				current.groupType1 = type1GroupNode;

				// next vertex on the heavy path
				current = current.getHeavyEdge().getEndNode();
			}

			if (k-1 > 0) {
				type1GroupNode.buildMismatchesIndex(k-1);
			}
		}
	}

	public void doHeavyPathDecomposition() {
		Logger.log(TAG, String.format("doHeavyPathDecomposition()"));

		calculateWeights();

		Queue<Node> queue = new LinkedList<>();
		queue.add(this);

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

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Edge getHeavyEdge() {
		return heavyEdge;
	}

	private Collection<Edge> getLightEdges() {
		Collection<Edge> lightEdges = new LinkedList<>();
		for (Edge edge : getEdges()) {
			if (!edge.equals(heavyEdge))
				lightEdges.add(edge);
		}
		return lightEdges;
	}

	public void setHeavyEdge(Edge edge) {
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

	public GroupNode getGroupType1() {
		return groupType1;
	}

	public void setGroupType1(GroupNode groupType1) {
		this.groupType1 = groupType1;
	}

	public GroupNode getGroupType2() {
		return groupType2;
	}

	public void setGroupType2(GroupNode groupType2) {
		this.groupType2 = groupType2;
	}

}
