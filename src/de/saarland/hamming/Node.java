package de.saarland.hamming;

import de.saarland.util.Logger;
import de.saarland.util.Pair;

import java.util.*;

/**
 * @author Almer Bolatov
 *         Date: 10/31/13
 *         Time: 1:14 PM
 */
public class Node {
	private static final String TAG = "Nd";//Node.class.getSimpleName();

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
//		Logger.log(TAG, String.format("Node() name=%d", name));

		this.trie = trie;
		this.edges = new HashMap<>();
	}

	public void addEdge(int stringIndex, int charIndex, Edge edge) {
		char key = charAt(stringIndex, charIndex);
		edges.put(key, edge);
	}

	public char charAt(int stringIndex, int charIndex) {
		return trie.getString(stringIndex)[charIndex];
	}

	public Edge findEdge(char ch) {
		return edges.get(ch);
	}

	public void removeEdge(int stringIndex, int charIndex) {
		edges.remove(charAt(stringIndex, charIndex));
	}

	public void buildMismatchesIndex(int distance) {
		Logger.increment();
		Logger.log(TAG, String.format("buildMismatchesIndex() k=%d", distance));

		if (distance <= 0) {
			Logger.log(TAG, String.format(" k<=0 Nothing to do here, node=%d", this.name));
			Logger.decrement();
			return;
		}

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

		Queue<Pair<Node, Integer>> heavyQueue = new LinkedList<>();
		heavyQueue.add(new Pair<>(this, distance));

		List<GroupNode> onPathVertices = new ArrayList<>();

		while (!heavyQueue.isEmpty()) {
			Pair<Node, Integer> pair = heavyQueue.remove();

			Node head = pair.getFirst();
			int k = pair.getSecond();

			Logger.log(TAG, String.format("  heavyQueue.size()=%d", heavyQueue.size()));

			assert k >= 0;

			// check if node has heavy path decomposition
			if (!head.isLeaf() && head.heavyEdge == null) {
				head.doHeavyPathDecomposition();
			}

			onPathVertices.clear();
			Node current = head;

			// Traverse vertices along the heavy path
			while (!current.isLeaf()) {

				// heavy path heads
				for (Edge edge : current.getEdges()) {
					Node headNode = edge.getEndNode();
					if (!edge.equals(current.heavyEdge) && !headNode.isLeaf()) {
						heavyQueue.add(new Pair<>(headNode, k));
					}
				}

				// Set type 2 group tree that are built from off-path vertices
				current.groupType2 = current.prepareType2GroupNode();

				Node mergedChildren = current.prepareMergedOffPathChildren();
				if (mergedChildren != null) {
					Node errTV = current.prepareErrTree(head, mergedChildren);

					// Create a single group node from an error tree
					GroupNode.GroupType type = GroupNode.GroupType.ONE;
					GroupNode onPathVertex = new GroupNode(type);
					onPathVertex.setId(current.getDepth() + "");
					onPathVertex.setNode(errTV);

					onPathVertices.add(onPathVertex);
				}
				// clean resources
//				mergedChildren = null;

				/**
				 * Recursively build mismatches index on a group trees
				 */
				if (k - 1 > 0 && current.groupType2 != null) {
					for (Node n : current.groupType2.getNodes()) {
						if (!n.isLeaf()) {
							heavyQueue.add(new Pair<>(n, k - 1));
						}
					}
				}

				// next vertex on the heavy path
				current = current.getHeavyEdge().getEndNode();
			}


			// Set type 1 group tree to vertices along the heavy path
			if (!onPathVertices.isEmpty()) {
				GroupNode type1GroupNode = GroupNode.buildGroup(onPathVertices);
				Node iteratorNode = head;
				while (!iteratorNode.isLeaf()) {
					iteratorNode.groupType1 = type1GroupNode;

					// next vertex on the heavy path
					iteratorNode = iteratorNode.heavyEdge.getEndNode();
				}

				if (k - 1 > 0) {
					for (Node n : type1GroupNode.getNodes()) {
						if (!n.isLeaf()) {
							heavyQueue.add(new Pair<>(n, k - 1));
						}
					}
				}
			}
		}
		Logger.decrement();
	}

	private Node prepareMergedOffPathChildren() {
		Node mergedChildren = null;

		for (char nextChar : nextChars()) {
			Edge edge = findEdge(nextChar);

			if (!edge.equals(heavyEdge)) {
				Node subNode = edge.sub();
				mergedChildren = (mergedChildren == null) ? subNode : Node.mergeNodes(mergedChildren, subNode);
			}
		}

		return mergedChildren;
	}

	private GroupNode prepareType2GroupNode() {
		Logger.increment();
//		Logger.log(TAG, "prepareType2GroupNode()");

		List<GroupNode> offPathChildren = new ArrayList<>();

		for (char nextChar : nextChars()) {
			Edge edge = findEdge(nextChar);

			if (!edge.equals(heavyEdge)) {
				Node subNode = edge.sub();

				// type 2
				GroupNode offPathGroupNode = new GroupNode(GroupNode.GroupType.TWO);
				offPathGroupNode.setId(nextChar + "");
				offPathGroupNode.setNode(subNode);

				offPathChildren.add(offPathGroupNode);
			}
		}

		Logger.decrement();
		return offPathChildren.isEmpty() ? null : GroupNode.buildGroup(offPathChildren);
	}

	private Node prepareErrTree(final Node head, final Node mergedChildren) {
		Logger.increment();
//		Logger.log(TAG, "prepareErrTree()");

		Edge lastEdge = null;

		int prevStr = -1;

		Node errTV = new Node(head.trie);

		// str(l)
		Node temp = head;
		for (int i = 0; i < depth; i++) {
			Edge tempHeavy = temp.getHeavyEdge();
			int tStr = tempHeavy.getStringIndex();
			int tBegin = tempHeavy.getBeginIndex();
			int tEnd = tempHeavy.getEndIndex();
			if (lastEdge == null) {
				lastEdge = new Edge(tStr, tBegin, tEnd, errTV);
				lastEdge.insert();
			} else {
				if (prevStr == tStr) {
					lastEdge.setEndIndex(tEnd);
				} else {
					Node lastNode = lastEdge.getEndNode();
					Edge newLastEdge = new Edge(tStr, tBegin, tEnd, lastNode);
					newLastEdge.insert();
					lastEdge = newLastEdge;
				}
			}

			prevStr = tStr;
			temp = tempHeavy.getEndNode();
		}
		// end str(l)

		final Edge nextHeavyEdge = heavyEdge;
		final int nextStringIndex = nextHeavyEdge.getStringIndex();
		final int nextBeginIndex = nextHeavyEdge.getBeginIndex();

		// nextChar
		if (lastEdge == null) {
			lastEdge = new Edge(nextStringIndex, nextBeginIndex, nextBeginIndex, errTV);
			lastEdge.insert();
		} else {
			int lastStringIndex = lastEdge.getStringIndex();
			if (lastStringIndex == nextStringIndex) {
				int newEndIndex = lastEdge.getEndIndex() + 1;
				lastEdge.setEndIndex(newEndIndex);
			} else {
				Node lastNode = lastEdge.getEndNode();
				Edge newLastEdge = new Edge(nextStringIndex, nextBeginIndex, nextBeginIndex, lastNode);
				newLastEdge.insert();
				lastEdge = newLastEdge;
			}
		}
		// end nextChar

		Node endNode = lastEdge.getEndNode();
		for (char ch : mergedChildren.nextChars()) {
			final Edge e = mergedChildren.findEdge(ch);
			endNode.addEdge(e.getStringIndex(), e.getBeginIndex(), e);
		}

		Logger.decrement();
		return errTV;
	}

	public void doHeavyPathDecomposition() {
		Logger.increment();
		Logger.log(TAG, String.format("doHeavyPathDecomposition()"));

		calculateWeights();

		Queue<Node> queue = new LinkedList<>();
		queue.add(this);

		this.depth = 0;

		Collection<Edge> outEdges;
		while (!queue.isEmpty()) {
			Node node = queue.remove();
			if (!node.isLeaf()) {
				outEdges = node.getEdges();
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

				assert heavy != null;

				node.setHeavyEdge(heavy);
				heavy.getEndNode().depth = node.depth + 1;
			}
		}
		Logger.decrement();
	}

	public void calculateWeights() {
//		Logger.log(TAG, String.format("calculateWeights()"));

		this.weight = dfs();
	}

	private int dfs() {
		Collection<Edge> outEdges = getEdges();
		if (isLeaf()) {
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
//		Logger.log(TAG, String.format("mergeNodes() m.name=%d, n.name=%d", m.name, n.name));

		Set<Character> mNextChars = m.nextChars();
		Set<Character> nNextChars = n.nextChars();

		Set<Character> allNextChars = new HashSet<>();
		allNextChars.addAll(mNextChars);
		allNextChars.addAll(nNextChars);

		Trie t = m.getTrie();
		Node merged = new Node(t);

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
				if (mergedSubNode.values != null && !mergedSubNode.values.isEmpty()) {
					mergedEndNode.addValues(mergedSubNode.values);
				}
			} else if (mNextChars.contains(ch)) {
				Edge edge = m.findEdge(ch).deepCopy(merged);
				edge.insert();
			} else {
				Edge edge = n.findEdge(ch).deepCopy(merged);
				edge.insert();
			}
		}

		if (m.values != null && !m.values.isEmpty()) {
			merged.addValues(m.values);
		}
		if (n.values != null && !n.values.isEmpty()) {
			merged.addValues(n.values);
		}

		return merged;
	}

	public Set<Integer> search(char[] q, int start, int distance) {
		Logger.increment();
		Logger.log(TAG, String.format("search() query=%s, k=%d", String.valueOf(q).substring(start), distance));
		Set<Integer> results = new HashSet<>();

		Queue<Query> queue = new LinkedList<>();
		queue.add(new Query(this, q, start, distance));

		while (!queue.isEmpty()) {
			// traverse along the heavy path
			Query query = queue.remove();
			Node node = query.getNode();
			int iStart = query.getStart();
			int k = query.getK();

			assert k >= 0;

			if (iStart == q.length) {// && searchable instanceof Node) {
				Logger.log(TAG, String.format("WRITING VALUES: iStart==q.length==%d", iStart));
				Set<Integer> nodeValues = node.values;
				if (nodeValues != null && !nodeValues.isEmpty()) {
					results.addAll(nodeValues);
					nodeValues = null;
				}
				continue;
			}

			int i = iStart;
			boolean toContinue = true;
			boolean isLengthExceeded = false;
			boolean areGroupsQueried = false;
			while (i < q.length && toContinue && !node.isLeaf()) {
				assert k >= 0;

				int offset = 0;

				int iForType2Search = i;

				char ch = q[i];
				Edge edge = node.findEdge(ch);

				if (edge == null) {
					if (k <= 0) break;

					if (node.heavyEdge == null) break;

					edge = node.heavyEdge;

					if (node.groupType1 != null) {
						// Query.getK()
						for (Node n : node.groupType1.getSearchableNodes(String.valueOf(node.depth))) {
							queue.add(new Query(n, q, iStart, query.getK() - 1));
						}
					}

					if (node.groupType2 != null && i + 1 < q.length) {
						for (Node n : node.groupType2.getSearchableNodes(null)) {
							queue.add(new Query(n, q, i + 1, k - 1));
						}
					}
					i++;
					k--;
					offset = 1;
				} else if (node.heavyEdge != null && !edge.equals(node.heavyEdge) && k > 0) {

					if (node.groupType1 != null) {
						// Query.getK()
						for (Node n : node.groupType1.getSearchableNodes(String.valueOf(node.depth))) {
							queue.add(new Query(n, q, iStart, query.getK() - 1));
						}
					}

					if (node.groupType2 != null && i + 1 < q.length) {// && k > 0) {
						for (Node n : node.groupType2.getSearchableNodes(null)) {
							queue.add(new Query(n, q, i + 1, k - 1));
						}

					}

					int iEdge = i;
					int kEdge = k;
					char[] s = trie.getString(edge.getStringIndex());

					for (int j = edge.getBeginIndex(); j <= edge.getEndIndex(); j++) {
						if (iEdge >= q.length) break;

						if (iEdge != j)
							Logger.err(TAG, String.format("Assertion error \tiEdge=%d, j=%d", iEdge, j));
						assert iEdge == j;

						if (s[j] != q[iEdge]) {
							if (kEdge > 0) kEdge--;
							else break;
						}
						iEdge++;
					}

					if (iEdge > edge.getEndIndex()) {
						queue.add(new Query(edge.getEndNode(), q, iEdge, kEdge));
					}

					edge = node.heavyEdge;

					areGroupsQueried = false;
					i++;
					k--;
					offset = 1;
				}

				assert k >= 0;

				int stringIndex = edge.getStringIndex();
				char[] s = trie.getString(stringIndex);
				for (int j = edge.getBeginIndex() + offset; j <= edge.getEndIndex(); j++) {

					if (i != j)
						Logger.err(TAG, String.format("Assertion error\ti=%d, j=%d", i, j));
					assert i == j;

					if (i >= q.length) {
						toContinue = false;
						isLengthExceeded = true;
						break;
					}

					if (s[j] != q[i]) {
						if (k > 0) {
							if (!areGroupsQueried) {
								areGroupsQueried = true;
								if (node.depth > 0 && node.groupType1 != null) {
									Logger.log(TAG, String.format("Type 1 group tree from node=%d, depth=%d", node.name, node.depth));
									// Query.getK()
									for (Node n : node.groupType1.getSearchableNodes(String.valueOf(node.depth))) {
										queue.add(new Query(n, q, iStart, query.getK() - 1));
									}
								}

								if (node.groupType2 != null) {
									Logger.log(TAG, String.format("Type 2 group tree from node=%d, depth=%d", node.name, node.depth));
									for (Node n : node.groupType2.getSearchableNodes(null)) {
										queue.add(new Query(n, q, iForType2Search + 1, k - 1));
									}

								}
							}
							k--;
						} else {
							toContinue = false;
							break;
						}
					}
					i++;
				}

				if (i == q.length && !isLengthExceeded) {
					Logger.log(TAG, String.format("WRITING VALUES: i==q.length==%d", i));
					Set<Integer> endNodeValues = edge.getEndNode().values;
					if (endNodeValues != null && !endNodeValues.isEmpty()) {
						results.addAll(endNodeValues);
						endNodeValues = null;
					}

					if (!areGroupsQueried) {
						if (node.groupType1 != null && node.depth > 0 && k > 0) {
							// query.getK()
							for (Node n : node.groupType1.getSearchableNodes(String.valueOf(node.depth))) {
								queue.add(new Query(n, q, iStart, query.getK() - 1));
							}

						}
						if (node.groupType2 != null && k > 0 && iForType2Search + 1 < q.length) {
							for (Node n : node.groupType2.getSearchableNodes(null)) {
								queue.add(new Query(n, q, iForType2Search + 1, k - 1));
							}

						}
					}
				}

				node = edge.getEndNode();
			}

		}

		Logger.decrement();
		return results;
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

	public void setHeavyEdge(Edge edge) {
		this.heavyEdge = edge;
	}

	public void addValue(int value) {
		if (this.values == null) {
			this.values = new HashSet<>();
		}
		values.add(value);
	}

	public void addValues(Set<Integer> vs) {
		if (vs == null || vs.isEmpty()) {
			return;
		}

		if (this.values == null) {
			this.values = new HashSet<>();
		}
		this.values.addAll(vs);
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
