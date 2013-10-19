package de.saarland.hamming;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Almer Bolatov
 *         Date: 10/16/13
 *         Time: 11:22 PM
 */
public class TestHamming {
	public static void main(String[] args) {
//		Tree tree = new Tree();
//		tree.addWord("far", 1);
//		tree.addWord("fat", 2);
//		tree.addWord("fit", 3);
//		tree.addWord("pay", 4);
//		tree.addWord("pin", 5);
//		tree.addWord("sit", 6);
//
//		tree.buildMismatchesIndex(3);
//
//		System.out.println("Test end");

		Tree a0 = new Tree();
		Tree a1 = new Tree();
		Tree a2 = new Tree();

		a0.addChild('a', a1);
		a1.addChild('a', a2);

		Set<Integer> aSet = new HashSet<>();
		aSet.add(1);
		aSet.add(2);
		a2.setValues(aSet);

		Tree b0 = new Tree();
		Tree b1 = new Tree();
		Tree b2 = new Tree();

		b0.addChild('a', b1);
		b1.addChild('a', b2);

		Set<Integer> bSet = new HashSet<>();
		bSet.add(3);
		bSet.add(4);
		b2.setValues(bSet);

		Tree merge = Tree.merge(a0, b0);
//		assertNotNull(merge);

		Tree c1 = merge.getChild('a');
//		assertNotNull(c1);

		Tree c2 = merge.getChild('a');
//		assertNotNull(c2);

		Set<Integer> cSet = c2.getValues();
//		assertEquals(cSet.size(), 4);
	}
}
