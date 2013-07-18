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
    private Set<Integer> values;

    // Pointer to a heavy child
    private Character heavyChildPointer;

    // Number of children in the subtree
    // including itself.
    private int weight;

    /**
     * Constructor
     */
    public Node() {
        this.children = new HashMap<Character, Node>();
        this.values = new HashSet<Integer>();
        this.weight = 1;
    }

    public void addChild(Character c, Node node) {
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
     * @return
     */
    public Map<Character, Node> getChildren() {
        return children;
    }

    public Node getHeavyChild() {
        return children.get(heavyChildPointer);
    }

    public Character getHeavyChildPointer() {
        return heavyChildPointer;
    }

    public void setHeavyChildPointer(Character pointer) {
        this.heavyChildPointer = pointer;
    }

    public List<Node> getLightChildren() {
        List<Node> lightChildren = new ArrayList<Node>();
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

}
