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

	private final int name;

	private final Trie trie;
	private final Map<Character, Edge> edges;
	private int weight;                         // number of leaves hanging from this node
	private int depth;                          // relative depth in the heavy path
	private Edge heavyEdge;                     // Either Node or Edge
	private Set<Integer> values;
	private GroupNode groupType1;               // head element of type 1 group trees
	private Node groupType2;               // head element of type 2 group trees

	private final ObjectPool pool;

	/**
	 *  Constructor
	 */
	public Node(Trie trie) {
		this.name = trie.getNewNodeNumber();
//		Logger.log(TAG, String.format("Node() name=%d", name));

		this.trie = trie;
		this.edges = new HashMap<>();

		this.pool = ObjectPool.getInstance();
	}

	/**
	 * Add edge to the map
	 * @param stringIndex
	 * @param charIndex
	 * @param edge
	 */
	public void addEdge(int stringIndex, int charIndex, Edge edge) {
		edges.put(charAt(stringIndex, charIndex), edge);
	}

	/**
	 * Character at a position
	 * @param stringIndex - string index of the word in the dictionary
	 * @param charIndex - character index in the string
	 * @return character
	 */
	private char charAt(int stringIndex, int charIndex) {
		return trie.getString(stringIndex).charAt(charIndex);
	}

	/**
	 * Find edge that is mapped with character ch
	 * @param ch - character that points to an edge
	 * @return edge if edges map contains character ch, null otherwise
	 */
	public Edge findEdge(char ch) {
		return edges.get(ch);
	}

	/**
	 * Removes edge from the edges map
	 * @param stringIndex - string index of the word in the dictionary
	 * @param charIndex - character index in the string
	 */
	public void removeEdge(int stringIndex, int charIndex) {
		edges.remove(charAt(stringIndex, charIndex));
	}

	/**
	 * Builds rooted mismatch index.
	 * @param distance - maximum allowed number of mismatches to support
	 */
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
//		heavyQueue.add(new Pair<>(this, distance));
		heavyQueue.add(pool.acquirePair(this, distance));

		List<GroupNode> onPathVertices = new ArrayList<>();

		while (!heavyQueue.isEmpty()) {
			Pair<Node, Integer> pair = heavyQueue.remove();

			Node head = pair.getFirst();
			int k = pair.getSecond();

			// return to pool
			pool.releasePair(pair);

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

				Node mergedChildren = null;

				// heavy path heads
				for (Edge edge : current.getEdges()) {
					if (!edge.equals(current.heavyEdge)) {
						Node headNode = edge.getEndNode();
						if (!headNode.isLeaf()) {
//							heavyQueue.add(new Pair<>(headNode, k));
							heavyQueue.add(pool.acquirePair(headNode, k));
						}

						// Merge off-path children
						Node subNode = edge.sub();
						mergedChildren = (mergedChildren == null) ? subNode : Node.mergeNodes(mergedChildren, subNode);
					}
				}

				if (mergedChildren != null) {
					// type 2 group trees
					current.groupType2 = mergedChildren.deepCopy();

					Node errTV = current.prepareErrTree(head, mergedChildren);

					// Create a single group node from an error tree
					// and it to the list
					onPathVertices.add(new GroupNode(current.depth, errTV));

					// Recursively build mismatches index on group trees
					if (k - 1 > 0) {
//					    heavyQueue.add(new Pair<>(current.groupType2, k - 1));
						heavyQueue.add(pool.acquirePair(current.groupType2, k - 1));
					}
				}

				// next vertex on the heavy path
				current = current.heavyEdge.getEndNode();
			}


			// Set type 1 group tree to vertices along the heavy path
			if (!onPathVertices.isEmpty()) {
				GroupNode type1GroupNode = GroupNode.buildGroup(onPathVertices);
				Node iteratorNode = head;
				while (true) {
					iteratorNode.groupType1 = type1GroupNode;

					if (iteratorNode.isLeaf()) {
						break;
					}

					// next vertex on the heavy path
					iteratorNode = iteratorNode.heavyEdge.getEndNode();
				}

				if (k - 1 > 0) {
					for (Node n : type1GroupNode.getNodes()) {
						if (!n.isLeaf()) {
//							heavyQueue.add(new Pair<>(n, k - 1));
							heavyQueue.add(pool.acquirePair(n, k - 1));
						}
					}
				}
			}
		}

		pool.destroyPairs();

		Logger.decrement();
	}

	/**
	 * The ERR(T,v_i) is the concatenation of the following:
	 *  1. The nodes along the path from v_1 till this v_i
	 *  2. The character that points to the heavy edge of this node
	 *  3. The merge of light edges without first characters
	 *
	 * @param head - top most node of the heavy path to which this node v_i belongs to
	 * @param mergedChildren - merged light edges without first characters
	 * @return errTV
	 */
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
					Edge newLastEdge = new Edge(tStr, tBegin, tEnd, lastEdge.getEndNode());
					newLastEdge.insert();
					lastEdge = newLastEdge;
				}
			}

			prevStr = tStr;
			temp = tempHeavy.getEndNode();
		}
		// end str(l)

		final int nextStringIndex = heavyEdge.getStringIndex();
		final int nextBeginIndex = heavyEdge.getBeginIndex();

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
				Edge newLastEdge = new Edge(nextStringIndex, nextBeginIndex, nextBeginIndex, lastEdge.getEndNode());
				newLastEdge.insert();
				lastEdge = newLastEdge;
			}
		}
		// end nextChar

		// append edges from merged off-path children
		Node endNode = lastEdge.getEndNode();
		for (Edge e : mergedChildren.getEdges()) {
			endNode.addEdge(e.getStringIndex(), e.getBeginIndex(), e);
		}

		Logger.decrement();
		return errTV;
	}

	/**
	 * A node with the highest weight is set as heavy and the rest
	 * are considered light.
	 * Since edges are used as outgoing objects, edges are set as heavy and light.
	 */
	public void doHeavyPathDecomposition() {
		Logger.increment();
		Logger.log(TAG, String.format("doHeavyPathDecomposition()"));

		calculateWeights();

//		Queue<Node> queue = new LinkedList<>();
		Queue<Node> queue = pool.acquireNodeList();
		queue.add(this);

		this.depth = 0;

		while (!queue.isEmpty()) {
			Node node = queue.remove();
			if (!node.isLeaf()) {
				Edge heavy = null;
				int maxWeight = 0;
				for (Edge e : node.getEdges()) {
					Node endNode = e.getEndNode();
					if (endNode.weight > maxWeight) {
						heavy = e;
						maxWeight = endNode.weight;
					}

					endNode.depth = 0;
					queue.add(endNode);
				}

				assert heavy != null;

				node.heavyEdge = heavy;
				heavy.getEndNode().depth = node.depth + 1;
			}
		}
		pool.releaseNodeList(queue);
		Logger.decrement();
	}

	/**
	 * Sets weights to this and child nodes
	 * weight - number of leaves hanging out of some node n.
	 */
	public void calculateWeights() {
//		Logger.log(TAG, String.format("calculateWeights()"));

		this.weight = dfs();
	}

	/**
	 * Depth-first search traversal.
	 * @return number of leaves hanging out of this node
	 */
	private int dfs() {
		if (isLeaf()) {
			return 1;
		}

		int leaves = 0;
		for (Edge e : getEdges()) {
			Node endNode = e.getEndNode();
			int branchLeaves = endNode.dfs();
			endNode.weight = branchLeaves;
			leaves += branchLeaves;
		}

		return leaves;
	}

	/**
	 * @return copy of the current node
	 */
	public Node deepCopy() {
		Node nodeCopy = new Node(trie);

		for (Edge e : getEdges()) {
			Edge edgeCopy = e.deepCopy(nodeCopy);
			edgeCopy.setStartNode(nodeCopy);
		}

		nodeCopy.addValues(values);

		return nodeCopy;
	}

	/**
	 * Merge two nodes into one
	 * @param m - node to merge
	 * @param n - node to merge
	 * @return - new node that contains both nodes m and n
	 */
	public static Node mergeNodes(Node m, Node n) {
		Set<Character> mNextChars = m.nextChars();
		Set<Character> nNextChars = n.nextChars();

		Set<Character> allNextChars = ObjectPool.getInstance().acquireCharacterSet(); //new HashSet<>();
		allNextChars.addAll(mNextChars);
		allNextChars.addAll(nNextChars);

		Trie t = m.getTrie();
		Node merged = new Node(t);

		for (Character ch : allNextChars) {
			if (mNextChars.contains(ch) && nNextChars.contains(ch)) {
				Edge mEdge = m.findEdge(ch).deepCopy(merged);
				Edge nEdge = n.findEdge(ch).deepCopy(merged);

				String mString = t.getString(mEdge.getStringIndex());
				String nString = t.getString(nEdge.getStringIndex());

				int minLength = Math.min(mEdge.getSpan(), nEdge.getSpan());

				assert minLength >= 0;

				int offset = 1;         // can start from the next char since first ones are same
				int mi = mEdge.getBeginIndex() + offset;
				int ni = nEdge.getBeginIndex() + offset;
				int position = offset;
				for (int i = offset; i <= minLength; i++, mi++, ni++) {
					if (mString.charAt(mi) != nString.charAt(ni)) {
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

		ObjectPool.getInstance().releaseCharacterSet(allNextChars);

		return merged;
	}

	/**
	 * Search the trie for query matches with allowed hamming
	 * distance between the query and the match
	 * @param q - query to search for
	 * @param distance - allowed hamming distance
	 * @return - the set of position indices in the dictionary
	 */
	public Set<Integer> search(final String q, int distance) {
		Logger.increment();
		Logger.log(TAG, String.format("search()"));
		Set<Integer> results = new HashSet<>();

		Queue<Query> queue = new LinkedList<>();
		queue.add(pool.acquireQuery(this, 0, distance));

		int queryLength = q.length();

		while (!queue.isEmpty()) {
			// traverse along the heavy path
			Query query = queue.remove();

			Node node = query.getNode();
			int iStart = query.getStart();
			int kStart = query.getK();
			int k = query.getK();

			// return to the pool
			pool.releaseQuery(query);

			assert node != null;
			assert iStart >= 0;
			assert k >= 0;

			if (iStart == queryLength) {
				Set<Integer> nodeValues = node.values;
				if (nodeValues != null && !nodeValues.isEmpty()) {
					results.addAll(nodeValues);
				}
				continue;
			}

			int i = iStart;
			boolean toContinue = true;
			boolean isLengthExceeded = false;
//			boolean areGroupsQueried = false;
			while (i < queryLength && toContinue && !node.isLeaf()) {
				assert k >= 0;

				int offset = 0;

				int iForType2Search = i;

				Edge edge = node.findEdge(q.charAt(i));

				if (edge == null) {
					if (k <= 0 || node.heavyEdge == null) {
						break;
					}

					if (node.groupType1 != null) {
						for (Node n : node.groupType1.getSearchableNodes(node.depth)) {
							queue.add(pool.acquireQuery(n, iStart, kStart - 1));
						}
					}

					if (node.groupType2 != null && i + 1 < queryLength) {
						queue.add(pool.acquireQuery(node.groupType2, i + 1, k - 1));
					}

					edge = node.heavyEdge;
					i++;
					k--;
					offset = 1;
				} else if (node.heavyEdge != null && !edge.equals(node.heavyEdge) && k > 0) {

					if (node.groupType1 != null) {
						for (Node n : node.groupType1.getSearchableNodes(node.depth)) {
							queue.add(pool.acquireQuery(n, iStart, kStart - 1));
						}
					}

					if (node.groupType2 != null && i + 1 < queryLength) {// && k > 0) {
						queue.add(pool.acquireQuery(node.groupType2, i + 1, k - 1));
					}

					int iEdge = i;
					int kEdge = k;
					String s = trie.getString(edge.getStringIndex());

					for (int j = edge.getBeginIndex(); j <= edge.getEndIndex(); j++) {
						if (iEdge >= queryLength) break;

						if (iEdge != j)
							Logger.err(TAG, String.format("Assertion error \tiEdge=%d, j=%d", iEdge, j));
						assert iEdge == j;

						if (s.charAt(j) != q.charAt(iEdge)) {
							if (kEdge > 0) kEdge--;
							else break;
						}
						iEdge++;
					}

					if (iEdge > edge.getEndIndex()) {
						queue.add(pool.acquireQuery(edge.getEndNode(), iEdge, kEdge));
					}

					edge = node.heavyEdge;

//					areGroupsQueried = false;
					i++;
					k--;
					offset = 1;
				}

				assert k >= 0;

				int stringIndex = edge.getStringIndex();
				String s = trie.getString(stringIndex);
				for (int j = edge.getBeginIndex() + offset; j <= edge.getEndIndex(); j++) {

					if (i != j)
						Logger.err(TAG, String.format("Assertion error\ti=%d, j=%d", i, j));
					assert i == j;

					if (i >= queryLength) {
						toContinue = false;
						isLengthExceeded = true;
						break;
					}

					if (s.charAt(j) != q.charAt(i)) {
						if (k > 0) {
//							if (!areGroupsQueried) {
//								areGroupsQueried = true;
								if (node.depth > 0 && node.groupType1 != null) {
									for (Node n : node.groupType1.getSearchableNodes(node.depth)) {
										queue.add(pool.acquireQuery(n, iStart, kStart - 1));
									}
								}

								if (node.groupType2 != null) {
									queue.add(pool.acquireQuery(node.groupType2, iForType2Search + 1, k - 1));
								}
//							}
							k--;
						} else {
							toContinue = false;
							break;
						}
					}
					i++;
				}

				if (i == queryLength && !isLengthExceeded) {
					Set<Integer> endNodeValues = edge.getEndNode().values;
					if (endNodeValues != null && !endNodeValues.isEmpty()) {
						results.addAll(endNodeValues);
					}

//					if (!areGroupsQueried) {
						if (node.groupType1 != null && node.depth > 0 && k > 0) {
							for (Node n : node.groupType1.getSearchableNodes(node.depth)) {
								queue.add(pool.acquireQuery(n, iStart, kStart - 1));
							}

						}
						if (node.groupType2 != null && k > 0 && iForType2Search + 1 < queryLength) {
							queue.add(pool.acquireQuery(node.groupType2, iForType2Search + 1, k - 1));
						}
//					}
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

	public Node getGroupType2() {
		return groupType2;
	}

	public void setGroupType2(Node groupType2) {
		this.groupType2 = groupType2;
	}

	public int getName() {
		return name;
	}
}
