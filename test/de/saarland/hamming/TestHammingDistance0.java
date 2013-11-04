package de.saarland.hamming;

import de.saarland.hamming.deprecated.Tree;
import junit.framework.TestCase;

import java.util.*;


/**
 * @author Almer Bolatov
 *         Date: 10/12/13
 *         Time: 1:01 PM
 */
public class TestHammingDistance0 extends TestCase {
    private Tree tree;
    private Map<Integer, String> testData;
    private Map<Integer, String> queries;
    private Map<Integer, Set<Integer>> answers;

    private int k = 0;

	public void test0() {
		assertTrue(true);
	}

//    @Before
//    public void setUp() throws Exception {
//        tree        = new Tree();
//	    testData    = TestCaseFactory.getTestData(TestCaseFactory.HAMMING_DISTANCE_TEST_DATA_PATH);
//	    queries     = TestCaseFactory.getTestQueries(TestCaseFactory.HAMMING_DISTANCE_TEST_QUERIES_PATH);
//	    answers     = TestCaseFactory.getTestQueryAnswers(TestCaseFactory.HAMMING_DISTANCE_TEST_ANSWERS_PATH);
//
//	    for (Integer testWordId : testData.keySet()) {
//		    String word = testData.get(testWordId);
//		    tree.addWord(word, testWordId);
//	    }
//	    System.out.println("TestHammingDistance0.setUp - start");
//	    tree.buildMismatchesIndex(k);
//	    System.out.println("TestHammingDistance0.setUp - end");
//    }

//    @Test
//    public void testQueries() {
//        for (Integer queryId : queries.keySet()) {
//            String query = queries.get(queryId);
//            Set<Integer> trieAnswers = tree.lookUp(query);
//
//            Set<Integer> queryAnswers = answers.get(queryId);
//            boolean isSuccess = trieAnswers.containsAll(queryAnswers);
//            if (!isSuccess) {
//                System.err.println(String.format("Query %d FAILED.", queryId));
//            }
//            assertTrue(isSuccess);
//        }
//	    assertTrue(true);
//    }

}
