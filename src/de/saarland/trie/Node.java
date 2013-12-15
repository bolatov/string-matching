package de.saarland.trie;

import java.util.*;

/**
 * @author almer
 *         7/6/13, 11:36 AM
 */
public class Node {

    // Children of this node.
//    private final Node children[];
    private final Map<Character, Node> children;

    // Values to store on the leaf nodes
    private final Set<Integer> values;

    // Pointer to a heavy child
    private Character heavyChildPointer;

    // Number of children in the subtree
    // including itself.
    private int weight;

    /**
     * Constructor
     */
    public Node() {
        this.children = new HashMap<>();
        this.values = new HashSet<>();
        this.weight = 1;
    }

    public void addChild(Character c, Node node) {
        if (children.containsKey(c))
            System.out.println(String.format("Node already contains character %c", c));
        children.put(c, node);
    }

    public Node getChild(Character c) {
        if (children.containsKey(c))
            return children.get(c);
        return null;
    }

    public Set<Integer> getValues() {
        return values;
    }

    /**
     * Returns children array with,
     * possibly with null values inside.
     *
     * @return children of this node
     */
    public Map<Character, Node> getChildren() {
        return children;
    }

    public Node getHeavyChild() {
        return children.get(heavyChildPointer);
    }

//    public Character getHeavyChildPointer() {
//        return heavyChildPointer;
//    }

    public void setHeavyChildPointer(Character pointer) {
        this.heavyChildPointer = pointer;
    }

    public List<Node> getLightChildren() {
        List<Node> lightChildren = new ArrayList<>();
        for (Character ch : children.keySet()) {
            if (!ch.equals(heavyChildPointer))
                lightChildren.add(children.get(ch));
        }

        return lightChildren;
    }

    public boolean isLeaf() {
        return weight == 1;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {

        this.weight = weight;
    }

    /**
     * Creates a deep copy of itself
     *
     * @return a deep copy of itself
     */
    public Node deepCopy() {
        Node result = new Node();

        for (Character key : children.keySet()) {
            Node child = children.get(key).deepCopy();
            result.addChild(key, child);

        }
        result.getValues().addAll(values);

        return result;
    }
}
