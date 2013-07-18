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

            if (trieAnswers.containsAll(queryAnswers)) {
                System.out.println(String.format("Query %d is SUCCESSFULL.", queryId));
            } else {
                System.err.println(String.format("Query %d FAILED", queryId));
            }
        }
    }

    public static void main1(String[] args) {
        // number of wildcards to support
        int k = 3;

        // write your code here
        System.out.println("Hello world");

        Trie trie = new Trie();
        trie.addWord("far", 1);
        trie.addWord("fat", 2);
        trie.addWord("fit", 3);
        trie.addWord("pay", 4);
        trie.addWord("pin", 5);
        trie.addWord("sit", 6);

        trie.addWildcardSupport(k);

        query("pin", trie);
        query("p*n", trie);
        query("*it", trie);
        query("fa*", trie);
        query("f*r", trie);
        query("f*t", trie);
        query("f**", trie);
        query("**t", trie);
        query("***", trie);

        System.out.println("Finished");

    }

    public static void query(String pattern, Trie trie) {
        Set<Integer> results = trie.lookUp(pattern);
        if (results.isEmpty()) {
            System.out.println(String.format("FAIL. Pattern '%s' NOT found", pattern));
        } else {
            String locations = "(";
            for (Integer i : results) {
                locations += i + " ";
            }
            locations += ")";
            System.out.println(String.format("SUCCESS. Pattern '%s' found at %s", pattern, locations));
        }
    }
}
