package de.saarland.hamming;

import java.util.*;


/**
 * @author Almer Bolatov
 *         Date: 10/12/13
 *         Time: 12:57 PM
 * TODO
 *
 * HINTS:
 * 1. pass char buffer instead of string
 * 2. add words one by one, instead of reading all and then passing
 *      to the trie.
 */
public class Tree {
	private Map<Character, Tree> children;

	private Tree heavyChild;

	private int heavyPathId;

	private Character heavyChildPointer;

	private Set<Integer> values;

	private int weight = 1;

	/**
	 * Constructor
	 */
	public Tree() {
		this.children = new HashMap<>();
		this.values = new HashSet<>();
	}


	/**
	 * Add word to a subtree.
	 */
	public void addWord(String word, int wordId) {
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
	}

	public void buildMismatchesIndex(int k) {
		if (k <= 0) {
			return;
		}

		// Do heavy path decomposition
		decompose();

		System.out.printf("buildMismatchesIndex(): %d%n", hashCode());

		Queue<Tree> heavyPathHeads = new LinkedList<>();
		heavyPathHeads.add(this);

		int i = 1;
		while (!heavyPathHeads.isEmpty()) {
			Tree t = heavyPathHeads.remove();
			System.out.printf("outerWhile %d %n", i++);// heavyChild = %s%n", t.getHeavyChildPointer().toString());

			List<Tree> errorTrees = new ArrayList<>();

			Tree errorTree = new Tree();

			// Gather Err(T,v_i) along the heavy path
			for (Tree current = t; !current.isLeaf(); current = current.getChild(current.getHeavyChildPointer())) {

				Tree mergedChildren = new Tree();
				for (Character childPointer : current.getChildren().keySet()) {
					if (!childPointer.equals(current.getHeavyChildPointer())) {
						Tree subTree = current.getChild(childPointer);
						mergedChildren = Tree.merge(mergedChildren, subTree);

						// for BFS traversal
						heavyPathHeads.add(subTree);

//						System.out.printf("  ch = %s%n", childPointer.toString());
					}
				}

				Tree leaf = errorTree.getAnyLeaf();

				leaf.addChild(current.getHeavyChildPointer(), mergedChildren);
				errorTrees.add(errorTree);

				// new Tree (with new hashCode id) for the next level
				errorTree = errorTree.deepCopy();
			}

			GroupTree groupTree = GroupTree.buildGroupTrees(errorTrees);
			GroupTreeStorage.getInstance().addGroupTree(t.hashCode(), groupTree);

			// build k-1 mismatches index on built group trees
//			if (k-1 > 0) {
//				Queue<GroupTree> groupTreesQueue = new LinkedList();
//				groupTreesQueue.add(groupTree);
//
//				while (!groupTreesQueue.isEmpty()) {
//					System.out.println(" --innerWhile");
//					GroupTree gTree = groupTreesQueue.remove();
//					gTree.getTree().buildMismatchesIndex(k - 1);
//
//					GroupTree leftChildGroupTree  = gTree.getLeftChild();
//					GroupTree rightChildGroupTree = gTree.getRightChild();
//					groupTreesQueue.add(leftChildGroupTree);
//					groupTreesQueue.add(rightChildGroupTree);
//				}
//			}
		}

	}

	/**
	 * Return bottom most leaf
	 * TODO think of better implementation!
	 * @return
	 */
	private Tree getAnyLeaf() {
		Tree current = this;
		Character ch = null;
		while (!current.isLeaf()) {
			current = current.getChildren().entrySet().iterator().next().getValue();
		}
		return current;
	}

	public Tree getChild(Character character) {
		return children.get(character);
	}

	public void addChild(Character character, Tree tree) {
		if (children.containsKey(character)) {
			// TODO notify!
			System.out.println("Tree already has this Character: '" + character + "'");
		}
		children.put(character, tree);
	}

	/**
	 * Returns children array with,
	 * possibly with null values inside.
	 *
	 * @return children of this node
	 */
	public Map<Character, Tree> getChildren() {
		return children;
	}
	
	public Set<Integer> getValues() {
		return values;
	}

	public void setValues(Set<Integer> values) {
		this.values = values;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public boolean isLeaf() {
		return children.isEmpty();
	}

	public Character getHeavyChildPointer() {
		return heavyChildPointer;
	}

	public void setHeavyChildPointer(Character pointer) {
		this.heavyChildPointer = pointer;
	}

	/**
	 * Classify nodes as heavy or light.
	 * Heavy path decomposition.
	 */
	private void decompose() {
		countWeights();

		Queue<Tree> queue = new LinkedList<Tree>();
		queue.add(this);

		while (!queue.isEmpty()) {
			Tree node = queue.remove();
			if (!node.isLeaf()) {
				Map<Character, Tree> children = node.getChildren();
				Character heavyChildPointer = null;
				int maxWeight = 0;
				for (Character ch : children.keySet()) {
					Tree child = children.get(ch);

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
	 * Set appropriate weights to the nodes
	 * rooted in a subtree.
	 * Weight of a node is a number of nodes
	 * rooted at it plus 1.
	 * Weight of a leaf equals to 1.
	 *
	 */
	private void countWeights() {
		int weight = 1;
		for (Character ch : children.keySet()) {
			Tree subTree = children.get(ch);
			subTree.countWeights();
			weight += subTree.getWeight();
		}
		setWeight(weight);
	}


	/**
	 * Returns merge of two trees
	 *
	 * @param iTree
	 * @param jTree
	 * @return
	 */
	public static Tree merge(Tree iTree, Tree jTree) {
		Tree mergedTree = new Tree();

		// TODO Implement

		return mergedTree;
	}

	/**
	 * Creates a deep copy of itself
	 */
	public Tree deepCopy() {
		Tree result = new Tree();

		for (Character key : children.keySet()) {
			Tree child = children.get(key).deepCopy();
			result.addChild(key, child);

		}
		result.getValues().addAll(values);

		return result;
	}
}
