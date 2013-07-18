package de.saarland.trie;

import de.saarland.util.TestCaseFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 *
 *
 * @author Almer Bolatov
 * 18.07.13, 09:35
 */
public class TestExactMatching {
    private Trie trie;
    private Map<Integer, String> testData;
    private Map<Integer, String> queries;
    private Map<Integer, Set<Integer>> answers;

    @Before
    public void setUp() throws Exception {
        trie = new Trie();
        testData = TestCaseFactory.getTestData(TestCaseFactory.EXACT_MATCHING_TEST_DATA_PATH);
        queries = TestCaseFactory.getTestQueries(TestCaseFactory.EXACT_MATCHING_TEST_QUERIES_PATH);
        answers = TestCaseFactory.getTestQueryAnswers(TestCaseFactory.EXACT_MATCHING_TEST_ANSWERS_PATH);

        for (Integer testWordId : testData.keySet()) {
            String word = testData.get(testWordId);
            trie.addWord(word, testWordId);
        }
    }

    @Test
    public void testQueries() {
        for (Integer queryId : queries.keySet()) {
            String query = queries.get(queryId);
            Set<Integer> trieAnswers = trie.lookUp(query);

            Set<Integer> queryAnswers = answers.get(queryId);
            boolean isSuccess = trieAnswers.containsAll(queryAnswers);
            if (!isSuccess) {
                System.err.println(String.format("Query %d FAILED.", queryId));
            }
            assertTrue(isSuccess);
        }
    }
}
