package de.saarland.hamming;

import java.util.List;
import java.util.Set;

/**
 * @author Almer Bolatov
 *         Date: 10/31/13
 *         Time: 1:15 PM
 */
public class Trie {
	private static final String TAG = Trie.class.getName();
	private static final String DOLLAR = "$";

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
		Logger.log(TAG, String.format("Trie() strgins.size=%d", strings.size()));

		this.strings = strings;
		this.root = new Node(-1, this);

		for (int i = 0; i < strings.size(); i++) {
			addString(i);
		}
	}

	private void addString(int stringIndex) {
		Logger.log(TAG, "addString()");

		// TODO IMPLEMENT
		Node node = root;

		char[] str = getString(stringIndex);
		int charIndex = 0;
		int endIndex = str.length - 1;

		Edge edge = node.findEdge(str[charIndex]);
		if (edge == null) {
			edge = new Edge(stringIndex, charIndex, endIndex, node);
			node.addEdge(stringIndex, charIndex, edge);
			node = edge.getEndNode();
		} else {
			int prevStringIndex = edge.getStringIndex();
			int prevBeginIndex  = edge.getBeginIndex();
			int prevEndIndex    = edge.getEndIndex();
			char[] prevStr = getString(prevStringIndex);
			int minLength = Math.min(prevEndIndex-prevBeginIndex, endIndex-charIndex);
			for (int i = 0; i < minLength; i++) {
				if (str[charIndex] != prevStr[prevStringIndex]) {
					// split edge at position where two strings have different characters
					Node midNode = edge.splitEdge(i);
					Edge newEdge = new Edge(stringIndex, charIndex, endIndex, midNode);
					midNode.addEdge(stringIndex, charIndex, newEdge);
					node = newEdge.getEndNode();
					break;
				}

				prevBeginIndex++;
				charIndex++;
			}
		}

		node.addValue(stringIndex);

		/*
		Set<Integer> values = new HashSet<>();
		values.add(wordId);

		Tree node = this;

		int len = word.length();

		for (int i = 0; i < len; i++) {
			Tree nextNode = node.getChild(word.charAt(i));

			if (nextNode != null) {
				node = nextNode;
			} else {
				for (; i < len; i++) {
					Tree newNode = new Tree();
					char c = word.charAt(i);
//                    node.addChild(Character.toUpperCase(c), newNode);
//                    node.addChild(Character.toLowerCase(c), newNode);
					node.addChild(c, newNode);
					node = newNode;
				}
				break;
			}
		}
		node.getValues().addAll(values);

		 */
	}

	public void buildMismatchesIndex(int k) {
		if (isBuilt) {
			Logger.log(TAG, String.format("Warning. Mismatches index is already built for k=%d", k));
			return;
		}

		Logger.log(TAG, String.format("buildMismatchesIndex() k=%d", k));

		this.k = k;
		// TODO IMPLEMENT


		this.isBuilt = true;
	}

	public Set<Integer> search(String query, int k) {
		Logger.log(TAG, String.format("search() query=%s, k=%d", query, k));

		// TODO IMPLEMENT

		return null;
	}

	private char[] getString(int index) {
		return (this.strings.get(index) + DOLLAR).toCharArray();
	}
}
