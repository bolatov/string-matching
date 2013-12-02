package de.saarland.hamming;

import de.saarland.util.Logger;

import java.util.List;
import java.util.Set;

/**
 * @author Almer Bolatov
 *         Date: 10/31/13
 *         Time: 1:15 PM
 */
public class Trie {
	private int nodesCount = 0;

	private static final String TAG = "Tr";//Trie.class.getSimpleName();
	public static final String DOLLAR = "$";

	private List<String> strings;
	private Node root;

	private int maxK = 0;
	private boolean isBuilt = false;
	private int maxStringLength = 0;

	/**
	 * Constructor
	 *
	 * @param strings
	 */
	public Trie(List<String> strings) {
//		Logger.log(TAG, String.format("Trie() strings.size=%d", strings.size()));

		this.strings = strings;
		this.root = new Node(this);

		for (int i = 0; i < strings.size(); i++) {
			// make all strings prefix free by adding DOLLAR sign
			strings.set(i, strings.get(i) + DOLLAR);

//			Logger.log(TAG, String.format("Add string %d %s", i+1, strings.get(i)));

			addString(i);
		}
	}

	private void addString(int stringIndex) {
//		Logger.log(TAG, String.format("addString() string=%s", strings.get(stringIndex)));

		Node node = root;

		char[] str      = getString(stringIndex);
		int currBegin   = 0;
		int currEnd     = str.length - 1;

		maxStringLength = Math.max(maxStringLength, str.length);

		boolean toStop = false;
		while (!toStop) {
			Edge edge = node.findEdge(str[currBegin]);
			if (edge == null) {
				edge = new Edge(stringIndex, currBegin, currEnd, node);
				node.addEdge(stringIndex, currBegin, edge);
				Node endNode = edge.getEndNode();
				endNode.addValue(stringIndex);
				break;  // break while-loop
			} else {
				node = edge.getEndNode();

				int prevBegin   = edge.getBeginIndex();

				char[] prevStr  = getString(edge.getStringIndex());
				int minLength = Math.min(edge.getSpan(), currEnd - currBegin);
				for (int i = 0; i <= minLength; i++) {
					if (str[currBegin] != prevStr[prevBegin]) {
						// split edge at position where two strings have different characters
						node = edge.splitEdge(i);
						break;  // break for-loop
					}

					prevBegin++;
					currBegin++;

					/**
					 * There are some duplicates in the test data
					 */
					if (prevBegin == currBegin && currBegin == str.length) {
						node.addValue(stringIndex);
						toStop = true;
					}
				}
			}
		}
	}

	public void buildMismatchesIndex(int k) {
		if (isBuilt) {
			Logger.log(TAG, String.format("Warning. Mismatches index is already built for maxK=%d", maxK));
			return;
		}

		if (k > maxStringLength) {
			Logger.err(TAG, String.format("The longest string in the trie has length=%d", maxStringLength));
			return;
		}

		Logger.log(TAG, String.format("buildMismatchesIndex() maxK=%d", k));

		this.maxK = k;

		this.root.buildMismatchesIndex(k);

		this.isBuilt = true;
	}

	public Set<Integer> search(String query, int k) {
//		Logger.log(TAG, String.format("search() query=%s, maxK=%d", query, k));
		if (k > maxK) {
			Logger.err(TAG, String.format("search(): Queries with distance %d are NOT supported.", k));
			return null;
		}

		char[] q = (query + DOLLAR).toCharArray();

		if (q.length > maxStringLength) {
			Logger.err(TAG, String.format("search(): Length of query %s is longer than any in the trie", String.valueOf(query)));
			return null;
		}

		Set<Integer> results = root.search(q, 0, k);

		return results;
	}

	public char[] getString(int index) {
		return (this.strings.get(index)).toCharArray();
	}

	/**
	 * TEST METHODS, REMOVE AFTERWARDS
	 * TODO REMOVE
	 *
	 */

	public int getNewNodeNumber() {
		return nodesCount++;
	}

	public Node getRoot() {
		return root;
	}
}
