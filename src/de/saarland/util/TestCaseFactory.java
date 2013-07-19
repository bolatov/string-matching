package de.saarland.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Almer Bolatov
 *         18.07.13, 10:15
 */
public class TestCaseFactory {
    // exact matching
    public final static String EXACT_MATCHING_TEST_DATA_PATH = "resources/test/testdata_exact_matching.txt";
    public final static String EXACT_MATCHING_TEST_QUERIES_PATH = "resources/test/testqueries_exact_matching.txt";
    public final static String EXACT_MATCHING_TEST_ANSWERS_PATH = "resources/test/testanswers_exact_matching.txt";

    // wildcards in the pattern
    public final static String WILDCARDS_IN_THE_PATTERN_TEST_DATA_PATH = "resources/test/testdata_wildcards_in_the_pattern.txt";
    public final static String WILDCARDS_IN_THE_PATTERN_TEST_QUERIES_PATH = "resources/test/testqueries_wildcards_in_the_pattern.txt";
    public final static String WILDCARDS_IN_THE_PATTERN_TEST_ANSWERS_PATH = "resources/test/testanswers_wildcards_in_the_pattern.txt";

    // light
    public final static String LIGHT_TEST_DATA_PATH = "resources/test/testdata_light.txt";
    public final static String LIGHT_TEST_QUERIES_PATH = "resources/test/testqueries_light.txt";
    public final static String LIGHT_TEST_ANSWERS_PATH = "resources/test/testanswers_light.txt";

    /**
     * Test data for Exact Matching.
     *
     * @return hash map with <WordId, Word> key-value pairs
     */
    public static Map<Integer, String> getTestData(String testDataPath) {
        Map<Integer, String> testData = new HashMap<Integer, String>();

        int idColumn = 0;
        int wordColumn = 1;

        BufferedReader reader = null;
        String line;
        String delimiter = ",";
        try {
            reader = new BufferedReader(new FileReader(testDataPath));
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(delimiter);
                Integer id = Integer.parseInt(columns[idColumn]);
                String word = columns[wordColumn];
                testData.put(id, word);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return testData;
    }

    /**
     * Test queries for Exact Matching.
     *
     * @return hash map with <QueryId, Query> key-value pairs
     */
    public static Map<Integer, String> getTestQueries(String testQueriesPath) {
        Map<Integer, String> queries = new HashMap<Integer, String>();

        int queryIdColumn = 0;
        int queryColumn = 1;

        BufferedReader reader = null;
        String line;
        String delimiter = ":";
        try {
            reader = new BufferedReader(new FileReader(testQueriesPath));
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(delimiter);
                Integer queryId = Integer.parseInt(columns[queryIdColumn]);
                String query = columns[queryColumn];
                queries.put(queryId, query);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return queries;
    }

    /**
     * Test answers for Exact Matching Queries.
     *
     * @return hash map with <QueryId, Set<Answer>>
     */
    public static Map<Integer, Set<Integer>> getTestQueryAnswers(String queryAnswersPath) {
        Map<Integer, Set<Integer>> queryAnswers = new HashMap<Integer, Set<Integer>>();

        int queryIdColumn = 0;
        int queryAnswersColumn = 1;

        BufferedReader reader = null;
        String line;
        String queryAnswersDelimiter = ":";
        String answersDelimiter = ",";
        try {
            reader = new BufferedReader(new FileReader(queryAnswersPath));
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(queryAnswersDelimiter);
                Integer queryId = Integer.parseInt(columns[queryIdColumn]);
                String answersString = columns[queryAnswersColumn];
                Set<Integer> answers = new HashSet<Integer>();
                for (String s : answersString.split(answersDelimiter)) {
                    if (!s.equalsIgnoreCase("NO"))
                        answers.add(Integer.parseInt(s));
                }
                queryAnswers.put(queryId, answers);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return queryAnswers;
    }
}
