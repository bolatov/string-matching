package de.saarland.hamming;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Almer Bolatov
 *         Date: 10/19/13
 *         Time: 4:31 PM
 */
public class TestTreeMerge {

	@Test
	public void testEqual() {
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
		assertNotNull(merge);

		Tree c1 = merge.getChild('a');
		assertNotNull(c1);

		Tree c2 = c1.getChild('a');
		assertNotNull(c2);

		Set<Integer> cSet = c2.getValues();
		System.out.printf("CSet.size = %d%n", cSet.size());
		for (Integer i : cSet) {
			System.out.println("i = " + i);
		}
		assertEquals(cSet.size(), 4);
	}

	@Test
	public void testDiff() {
		Tree a0 = new Tree();
		Tree a1 = new Tree();
		Tree a2 = new Tree();

		a0.addChild('a', a1);
		a1.addChild('b', a2);

		Set<Integer> aSet = new HashSet<>();
		aSet.add(1);
		aSet.add(2);
		a2.setValues(aSet);

		Tree b0 = new Tree();
		Tree b1 = new Tree();
		Tree b2 = new Tree();

		b0.addChild('b', b1);
		b1.addChild('a', b2);

		Set<Integer> bSet = new HashSet<>();
		bSet.add(5);
		b2.setValues(bSet);

		Tree merge = Tree.merge(a0, b0);
		assertNotNull(merge);

		assertEquals(merge.getChildren().size(), 2);

		Tree aChild = merge.getChild('a');
		assertNotNull(aChild);

		Tree bChild = merge.getChild('b');
		assertNotNull(bChild);

		// a
		Tree a_bChild = aChild.getChild('b');
		assertEquals(a_bChild.getValues().size(), 2);

		// b
		Tree b_aChild = bChild.getChild('a');
		assertEquals(b_aChild.getValues().size(), 1);
	}

	@Test
	public void testDiffHeight() {
		// Tree t1
		Tree t1_0 = new Tree();
		Tree t1_a = new Tree();
		Tree t1_ab = new Tree();
		Tree t1_aa = new Tree();
		Tree t1_aba = new Tree();

		t1_0.addChild('a', t1_a);
		t1_a.addChild('b', t1_ab);
		t1_a.addChild('a', t1_aa);
		t1_ab.addChild('a', t1_aba);

		Set<Integer> t1_aSet = new HashSet<>();
		t1_aSet.add(19);
		t1_a.setValues(t1_aSet);

		Set<Integer> t1_abaSet = new HashSet<>();
		t1_abaSet.add(1);
		t1_abaSet.add(2);
		t1_aba.setValues(t1_abaSet);

		Set<Integer> t1_aaSet = new HashSet<>();
		t1_aaSet.add(3);
		t1_aaSet.add(4);
		t1_aa.setValues(t1_aaSet);

		// Tree t2
		Tree t2_0 = new Tree();
		Tree t2_a = new Tree();
		Tree t2_ab = new Tree();
		Tree t2_ac = new Tree();
		Tree t2_aa = new Tree();
		Tree t2_abd = new Tree();
		Tree t2_aba = new Tree();
		Tree t2_acd = new Tree();

		t2_0.addChild('a', t2_a);
		t2_a.addChild('b', t2_ab);
		t2_a.addChild('c', t2_ac);
		t2_a.addChild('a', t2_aa);
		t2_ab.addChild('d', t2_abd);
		t2_ab.addChild('a', t2_aba);
		t2_ac.addChild('d', t2_acd);

		Set<Integer> t2_aSet = new HashSet<>();
		t2_aSet.add(20);
		t2_a.setValues(t2_aSet);

		Set<Integer> t2_abdSet = new HashSet<>();
		t2_abdSet.add(11);
		t2_abd.setValues(t2_abdSet);

		Set<Integer> t2_abaSet = new HashSet<>();
		t2_abaSet.add(9);
		t2_aba.setValues(t2_abaSet);

		Set<Integer> t2_acdSet = new HashSet<>();
		t2_acdSet.add(5);
		t2_acdSet.add(6);
		t2_acd.setValues(t2_acdSet);

		Set<Integer> t2_aaSet = new HashSet<>();
		t2_aaSet.add(7);
		t2_aaSet.add(8);
		t2_aa.setValues(t2_aaSet);

		// Merge
		Tree merge = Tree.merge(t1_0, t2_0);

		// Check merged trees

		Set<Integer> aSet = merge.getChild('a').getValues();
		assertEquals(aSet.size(), 2);
		assertTrue(aSet.contains(19));
		assertTrue(aSet.contains(20));

		int abd = merge.getChild('a').getChild('b').getChild('d').getValues().size();
		assertEquals(abd, 1);

		int aa = merge.getChild('a').getChild('a').getValues().size();
		assertEquals(aa, 4);

		int aba = merge.getChild('a').getChild('b').getChild('a').getValues().size();
		assertEquals(aba, 3);

		int acd = merge.getChild('a').getChild('c').getChild('d').getValues().size();
		assertEquals(acd, 2);
	}
}
