package de.saarland;

import de.saarland.trie.Trie;
import de.saarland.util.TestCaseFactory;

import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
//        Map<Integer, String> testData = TestCaseFactory.getTestData(TestCaseFactory.EXACT_MATCHING_TEST_DATA_PATH);
//        Map<Integer, String> queries = TestCaseFactory.getTestQueries(TestCaseFactory.EXACT_MATCHING_TEST_QUERIES_PATH);
//        Map<Integer, Set<Integer>> answers = TestCaseFactory.getTestQueryAnswers(TestCaseFactory.EXACT_MATCHING_TEST_ANSWERS_PATH);
        Map<Integer, String> testData = TestCaseFactory.getTestData(TestCaseFactory.WILDCARDS_IN_THE_PATTERN_TEST_DATA_PATH);
        Map<Integer, String> queries = TestCaseFactory.getTestQueries(TestCaseFactory.WILDCARDS_IN_THE_PATTERN_TEST_QUERIES_PATH);
        Map<Integer, Set<Integer>> answers = TestCaseFactory.getTestQueryAnswers(TestCaseFactory.WILDCARDS_IN_THE_PATTERN_TEST_ANSWERS_PATH);
        int k = 5;

//        Map<Integer, String> testData = TestCaseFactory.getTestData(TestCaseFactory.LIGHT_TEST_DATA_PATH);
//        Map<Integer, String> queries = TestCaseFactory.getTestQueries(TestCaseFactory.LIGHT_TEST_QUERIES_PATH);
//        Map<Integer, Set<Integer>> answers = TestCaseFactory.getTestQueryAnswers(TestCaseFactory.LIGHT_TEST_ANSWERS_PATH);
//        int k = 3;


        Trie trie = new Trie();
        for (Integer testWordId : testData.keySet()) {
            String word = testData.get(testWordId);
            trie.addWord(word, testWordId);
        }

        trie.addWildcardSupport(k);

        for (Integer queryId : queries.keySet()) {
            String query = queries.get(queryId);
            Set<Integer> trieAnswers = trie.lookUp(query);

            Set<Integer> queryAnswers = answers.get(queryId);

            if (!trieAnswers.containsAll(queryAnswers)) {
                System.out.println(String.format("FAIL. Query %d %s.", queryId, query));
                System.out.print("A1: ");
                for (int i : trieAnswers) {
                    System.out.print(i + " ");
                }
                System.out.println();
                System.out.print("A2: ");
                for (int i : queryAnswers) {
                    System.out.print(i + " ");
                }
                System.out.println("\n--------------------------------------------------");
            }
        }
    }
}
