package de.saarland;

import java.util.*;

public class Trie {
    // Don't care or wildcard character
    public static final Character WILDCARD = '*';

    // The root node of the tree.
    private Node root;

    // helper buffer to convert Strings to char arrays
    private char[] charBuffer = new char[0];

    private boolean supportsWildcards = false;

    public Trie() {
        this.root = new Node();
    }

    public Node getRoot() {
        return root;
    }

    /**
     * Classify nodes as heavy or light.
     * Heavy path decomposition.
     */
    public void decompose(Node startNode) {
        countWeights(startNode);

        Queue<Node> queue = new LinkedList<Node>();
        queue.add(startNode);

        while (!queue.isEmpty()) {
            Node node = queue.remove();
            if (!node.isLeaf()) {
                Map<Character, Node> children = node.getChildren();
                Character heavyChildPointer = null;
                int maxWeight = 0;
                for (Character ch : children.keySet()) {
                    Node child = children.get(ch);

                    if (child.getWeight() > maxWeight) {
                        heavyChildPointer = ch;
                        maxWeight = child.getWeight();
                    }

                    // for BFS
                    queue.add(child);
                }
                node.setHeavyChildPointer(heavyChildPointer);
            }
        }
    }

    /**
     * Add a word to the trie
     *
     * @param key   word to add to the trie
     * @param value to associate with this word
     */
    public void addWord(String key, int value) {
        Set<Integer> values = new HashSet<Integer>();
        values.add(value);
        addWord(key, values, root);
    }

    /**
     * Add word to a subtree.
     *
     * @param key    string itself
     * @param values to associate with this word
     * @param node   root node of a subtree
     */
    private void addWord(String key, Set<Integer> values, Node node) {
        final int len = key.length();
        if (len > charBuffer.length) {
            charBuffer = new char[len];
        }

        for (int i = 0; i < len; i++) {
            Node nextNode = node.getChild(key.charAt(i));

            if (nextNode != null) {
                node = nextNode;
            } else {
                for (; i < len; i++) {
                    Node newNode = new Node();
                    char c = key.charAt(i);
//                    node.addChild(Character.toUpperCase(c), newNode);
                    node.addChild(Character.toLowerCase(c), newNode);
                    node = newNode;
                }
                break;
            }
        }
        node.getValues().addAll(values);
    }

    /**
     * Look up word in the trie.
     *
     * @param key word to search for
     * @return list of values assigned to words
     */
    public Set<Integer> lookUp(final String key) {
        final int len = key.length();
        Set<Integer> result = new HashSet<Integer>();

        // no point in checking if
        // the word is longer than the longest
        // word in the trie or if it is an empty word
        if (charBuffer.length < len || len == 0) {
            return result;
        }

//        Node node = root;
        key.getChars(0, len, charBuffer, 0);

        Queue<Pair<Node, Integer>> queue = new LinkedList<Pair<Node, Integer>>();
        int rootDepth = 0;
        Pair<Node, Integer> rootPair = new Pair<Node, Integer>(root, rootDepth);
        queue.add(rootPair);
        while (!queue.isEmpty()) {
            Pair<Node, Integer> pair = queue.remove();
            Node node = pair.getFirst();
            int depth = pair.getSecond();
            int nextDepth = depth + 1;
            if (depth == charBuffer.length && node.isLeaf()) {
                result.addAll(node.getValues());
            } else {
                Character ch = charBuffer[depth];
                if (ch == WILDCARD && supportsWildcards) {
                    Node heavyChild = node.getHeavyChild();
                    Pair<Node, Integer> heavyPair = new Pair<Node, Integer>(heavyChild, nextDepth);
                    queue.add(heavyPair);
                }
                Node child = node.getChild(ch);
                if (child != null) {
                    Pair<Node, Integer> childPair = new Pair<Node, Integer>(child, nextDepth);
                    queue.add(childPair);
                }
            }
        }

        return result;

    }

    /**
     * Adds wildcard support to the specified node.
     * Merge light children in to a new node that starts
     * with a wildcard character.
     *
     * @param k number of wildcards to support
     */
    public void addWildcardSupport(int k) {
        decompose(root);

        if (k > charBuffer.length) {
            System.err.println(String.format("Error. Can support at most %d wildcards.", charBuffer.length));
            return;
        }

        // traverse the tree in BFS-manner
        Queue queue = new LinkedList();
        Pair rootPair = new Pair(getRoot(), k);
        queue.add(rootPair);

        while (!queue.isEmpty()) {
            Pair<Node, Integer> p = (Pair) queue.remove();
            Node node = p.getFirst();
            int j = p.getSecond();  // j<=k

            if (j > 0 && !node.isLeaf()) {
                // add heavy child for the next traversal level
                Pair heavyChildPair = new Pair(node.getHeavyChild(), j);
                queue.add(heavyChildPair);

                List<Node> lightChildren = node.getLightChildren();
                List<Pair<String, Set<Integer>>> lightWords = new ArrayList<Pair<String, Set<Integer>>>();
                for (Node lightChild : lightChildren) {

                    // extract words and values hanging
                    // from this node
                    extractWords(lightChild, "", lightWords);

                    Pair lightChildPair = new Pair(lightChild, j);
                    queue.add(lightChildPair);
                }

                Node wildcardNode = new Node();
                for (Pair<String, Set<Integer>> pair : lightWords) {
                    String word = pair.getFirst();
                    Set<Integer> values = pair.getSecond();
                    addWord(word, values, wildcardNode);
                }
                decompose(wildcardNode);

                node.addChild(WILDCARD, wildcardNode);

                // for BFS traversal
                Pair wildcardChildPair = new Pair(wildcardNode, new Integer(j - 1));
                queue.add(wildcardChildPair);
            }
        }
        this.supportsWildcards = true;
    }

    /**
     * Extract suffixes hanging from the node
     * and store to the bag.
     *
     * @param node
     * @param prefix
     * @param bag
     */
    private void extractWords(Node node, String prefix, List<Pair<String, Set<Integer>>> bag) {
        // Recursively traverse a subtree hanging from the node
        // in a DFS manner by appending characters to the prefix string.
        // Add the word to the bag if leaf is reached.
        if (node.isLeaf()) { // && !"".equals(prefix)) {
            Set<Integer> values = node.getValues();
            Pair<String, Set<Integer>> pair = new Pair<String, Set<Integer>>();
            pair.setFirst(prefix);
            pair.setSecond(values);
            bag.add(pair);
            return;
        }
        Map<Character, Node> children = node.getChildren();
        for (Character ch : children.keySet()) {
            Node child = children.get(ch);
            extractWords(child, prefix + ch, bag);
        }
    }

    /**
     * Set appropriate weights to the nodes
     * rooted in a subtree.
     * Weight of a node is a number of nodes
     * rooted at it plus 1.
     * Weight of a leaf equals to 1.
     *
     * @param node root of a subtree
     */
    private void countWeights(Node node) {

        int weight = 1;
        Map<Character, Node> children = node.getChildren();
        for (Character ch : children.keySet()) {
            Node childNode = children.get(ch);
            countWeights(childNode);
            weight += childNode.getWeight();
        }
        node.setWeight(weight);
    }

}
