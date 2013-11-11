package de.saarland.hamming;

import de.saarland.util.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Almer Bolatov
 *         Date: 10/31/13
 *         Time: 1:15 PM
 */
public class Trie {
	private int nodesCount = 0;

	private static final String TAG = Trie.class.getSimpleName();
	public static final String DOLLAR = "$";

	private List<String> strings;
	private Node root;

	private int k = 0;
	private boolean isBuilt = false;

	/**
	 * Constructor
	 *
	 * @param strings
	 */
	public Trie(List<String> strings) {
		Logger.log(TAG, String.format("Trie() strings.size=%d", strings.size()));

		this.strings = strings;
		this.root = new Node(this);

		for (int i = 0; i < strings.size(); i++) {
			// make all strings prefix free by adding DOLLAR sign
			strings.set(i, strings.get(i) + DOLLAR);

			addString(i);
		}
	}

	private void addString(int stringIndex) {
		Logger.log(TAG, String.format("addString() string=%s", strings.get(stringIndex)));

		Node node = root;

		char[] str      = getString(stringIndex);
		int currBegin   = 0;
		int currEnd     = str.length - 1;

		while (true) {
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
				}
			}
		}
	}

	public void buildMismatchesIndex(int k) {
		if (isBuilt) {
			Logger.log(TAG, String.format("Warning. Mismatches index is already built for k=%d", k));
			return;
		}
		Logger.log(TAG, String.format("buildMismatchesIndex() k=%d", k));

		this.k = k;

		this.root.buildMismatchesIndex(k);

		this.isBuilt = true;
	}

	public Set<Integer> search(String query, int k) {
		Logger.log(TAG, String.format("search() query=%s, k=%d", query, k));

		// TODO IMPLEMENT
		Set<Integer> result = new HashSet<>();

		Node node = root;
		int i = 0;
		char[] q = (query + DOLLAR).toCharArray();
		while (i < q.length) {
			Edge edge = node.findEdge(q[i]);
			if (edge == null) {
				// no need to proceed
				break;
			}

			i++;

			int stringIndex = edge.getStringIndex();
			char[] s = getString(stringIndex);


			for (int j = edge.getBeginIndex()+1; j <= edge.getEndIndex(); j++) {
				if (i == q.length) {
					break;
				}
				if (s[j] != q[i]) {
					return result;
				}
				i++;
			}
			node = edge.getEndNode();
		}

		result.addAll(node.getValues());

		return result;
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
