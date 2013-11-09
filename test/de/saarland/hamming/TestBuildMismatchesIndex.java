package de.saarland.hamming;

import de.saarland.util.Logger;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Almer Bolatov
 *         Date: 11/9/13
 *         Time: 10:38 PM
 */
public class TestBuildMismatchesIndex extends TestCase{
	private static final String TAG = TestBuildMismatchesIndex.class.getSimpleName();

	public void test1() {
		Logger.log(TAG, "test1(): START");
		List<String> s = new ArrayList<>();
		s.add("far");
		s.add("fat");
		s.add("fix");
		s.add("pay");
		s.add("pin");
		s.add("sit");

		Trie t = new Trie(s);
		int k = 1;
		t.buildMismatchesIndex(k);

		Logger.log(TAG, "test1(): END");
	}
}
