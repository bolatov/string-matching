package de.saarland.hamming;

import de.saarland.util.DataReader;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.TIntSet;
import junit.framework.TestCase;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * @author Almer Bolatov
 *         Date: 11/26/13
 *         Time: 9:20 PM
 */
public class TestHammingAdvanced extends TestCase {
	public void testSmall() {
		System.out.println("START");

		int k = 4;
		File dataFile = new File("res/hamming/simple_testdata.txt");
		List<String> strings = DataReader.readDataAsStrings(dataFile);

		Trie t = new Trie(strings);
		t.buildMismatchesIndex(k);

		File queriesFile = new File("res/hamming/simple_testqueries.txt");
		List<DataReader.Query> queries = DataReader.readQueries(queriesFile);

		File answersFile = new File("res/hamming/simple_testanswers.txt");
		List<DataReader.Answer> answers = DataReader.readAnswers(answersFile);

		for (DataReader.Query q : queries) {
			int qid = q.id;


			String pattern = q.pattern;
			int localK = q.k;

			Set<Integer> myAnswers = t.search(pattern, localK);
			DataReader.Answer answer = answers.get(qid-1);
			if (answer.queryId != qid) {
				System.err.println("Mismatch!!!");
			}
			Set<Integer> correctAnswers = answer.positions;

			assertEquals(correctAnswers.size(), myAnswers.size());
			for (int myAnswer : myAnswers) {
				assertTrue(correctAnswers.contains(myAnswer+1));
			}
			System.out.printf("Query %d PASSED\n", qid);
		}

		System.out.println("FINISH");
	}

	public void testGeoNamesK0() {
        System.out.println("START");

        int k = 0;
        File dataFile = new File(String.format("res/hamming/geonames_k%d_testdata.csv", k));
        List<String> strings = DataReader.readDataAsStrings(dataFile);

        Trie t = new Trie(strings);
        t.buildMismatchesIndex(k);

        File queriesFile = new File(String.format("res/hamming/geonames_k%d_testqueries.csv", k));
        List<DataReader.Query> queries = DataReader.readQueries(queriesFile);

        File answersFile = new File(String.format("res/hamming/geonames_k%d_testanswers.csv", k));
        List<DataReader.Answer> answers = DataReader.readAnswers(answersFile);

		int i = 1;
        for (DataReader.Query q : queries) {
            int qid = q.id;

            System.out.printf("%d. Query %d STARTED...\n", i++, qid);

            String pattern = q.pattern;
            int localK = q.k;

            Set<Integer> myAnswers = t.search(pattern, localK);
            DataReader.Answer answer = answers.get(qid-1);
            if (answer.queryId != qid) {
                System.err.println("Mismatch!!!");
            }
            Set<Integer> correctAnswers = answer.positions;

            if (myAnswers.size() != correctAnswers.size()) {
//			test
                System.out.println();
                System.out.printf("\t%s    Query\n", pattern);
//				System.out.printf("\tMyAnswers: ");
                for (int myanswer : myAnswers) {
//					System.out.printf("%d, %s", myanswer+1, strings.get(myanswer));
                    System.out.printf("\t%s     MyAnswer\n", strings.get(myanswer));
                }
                System.out.println();
//
//				System.out.printf("\tCorrectAnswers: ");
                for (int correctAnswer : correctAnswers) {
//					System.out.printf("%d, ", correctAnswer);
                    System.out.printf("\t%s     CorrectAnswer\n", strings.get(correctAnswer-1));
                }
                System.out.println();
            }
//			end test

            assertEquals(myAnswers.size(), correctAnswers.size());
            for (int myAnswer : myAnswers) {
                assertTrue(correctAnswers.contains(myAnswer+1));
            }
            System.out.printf("Query %d PASSED\n", qid);
        }

        System.out.println("FINISH");

        assertTrue(true);

    }

	public void testGeoNamesK1() {
//        System.out.println("START");

        int k = 1;
        File dataFile = new File(String.format("res/hamming/geonames_k%d_testdata.csv", k));
        List<String> strings = DataReader.readDataAsStrings(dataFile);

        Trie t = new Trie(strings);
//        de.saarland.hamming_trove.Trie t = new de.saarland.hamming_trove.Trie(strings);
        t.buildMismatchesIndex(k);

        File queriesFile = new File(String.format("res/hamming/geonames_k%d_testqueries.csv", k));
        List<DataReader.Query> queries = DataReader.readQueries(queriesFile);

        File answersFile = new File(String.format("res/hamming/geonames_k%d_testanswers.csv", k));
        List<DataReader.Answer> answers = DataReader.readAnswers(answersFile);

		int i = 1;
        for (DataReader.Query q : queries) {
            int qid = q.id;

            System.out.printf("%d. Query %d STARTED...\n", i++, qid);

            String pattern = q.pattern;
            int localK = q.k;

            Set<Integer> myAnswers = t.search(pattern, localK);
//            TIntSet myAnswers = t.search(pattern, localK);
            DataReader.Answer answer = answers.get(qid-1);
            if (answer.queryId != qid) {
                System.err.println("Mismatch!!!");
            }
            Set<Integer> correctAnswers = answer.positions;

            if (myAnswers.size() != correctAnswers.size()) {
//			test
                System.out.println();
                System.out.printf("\t%s    Query\n", pattern);
//				System.out.printf("\tMyAnswers: ");

//                TIntIterator iterator = myAnswers.iterator();
//                while (iterator.hasNext()) {
//                    int myanswer = iterator.next();

                for (int myanswer : myAnswers) {
//					System.out.printf("%d, %s", myanswer+1, strings.get(myanswer));
                    System.out.printf("\t%s     MyAnswer\n", strings.get(myanswer));
                }
                System.out.println();
//
//				System.out.printf("\tCorrectAnswers: ");
                for (int correctAnswer : correctAnswers) {
//					System.out.printf("%d, ", correctAnswer);
                    System.out.printf("\t%s     CorrectAnswer\n", strings.get(correctAnswer-1));
                }
                System.out.println();
            }
//			end test

            assertEquals(myAnswers.size(), correctAnswers.size());
//            TIntIterator iterator = myAnswers.iterator();
//            while (iterator.hasNext()) {
//                int myAnswer = iterator.next();
            for (int myAnswer : myAnswers) {
                assertTrue(correctAnswers.contains(myAnswer+1));
            }
//            System.out.printf("Query %d PASSED\n", qid);
        }

//        System.out.println("FINISH");

        assertTrue(true);
	}

	public void testGeoNamesK2() {
        System.out.println("START");

        int k = 2;
        File dataFile = new File(String.format("res/hamming/geonames_k%d_testdata.csv", k));
        List<String> strings = DataReader.readDataAsStrings(dataFile);

        Trie t = new Trie(strings);
        t.buildMismatchesIndex(k);

        File queriesFile = new File(String.format("res/hamming/geonames_k%d_testqueries.csv", k));
        List<DataReader.Query> queries = DataReader.readQueries(queriesFile);

        File answersFile = new File(String.format("res/hamming/geonames_k%d_testanswers.csv", k));
        List<DataReader.Answer> answers = DataReader.readAnswers(answersFile);

		int i = 1;
        for (DataReader.Query q : queries) {
            int qid = q.id;

            System.out.printf("%d. Query %d STARTED...\n", i++, qid);

            String pattern = q.pattern;
            int localK = q.k;

            Set<Integer> myAnswers = t.search(pattern, localK);
            DataReader.Answer answer = answers.get(qid-1);
            if (answer.queryId != qid) {
                System.err.println("Mismatch!!!");
            }
            Set<Integer> correctAnswers = answer.positions;

            if (myAnswers.size() != correctAnswers.size()) {
//			test
                System.out.println();
                System.out.printf("\t%s    Query\n", pattern);
//				System.out.printf("\tMyAnswers: ");
                for (int myanswer : myAnswers) {
//					System.out.printf("%d, %s", myanswer+1, strings.get(myanswer));
                    System.out.printf("\t%s     MyAnswer\n", strings.get(myanswer));
                }
                System.out.println();
//
//				System.out.printf("\tCorrectAnswers: ");
                for (int correctAnswer : correctAnswers) {
//					System.out.printf("%d, ", correctAnswer);
                    System.out.printf("\t%s     CorrectAnswer\n", strings.get(correctAnswer-1));
                }
                System.out.println();
            }
//			end test

            assertEquals(myAnswers.size(), correctAnswers.size());
            for (int myAnswer : myAnswers) {
                assertTrue(correctAnswers.contains(myAnswer+1));
            }
            System.out.printf("Query %d PASSED\n", qid);
        }

        System.out.println("FINISH");

        assertTrue(true);

    }

    public void testGeoNamesK3() {
        System.out.println("START");

        int k = 3;
        File dataFile = new File(String.format("res/hamming/geonames_k%d_testdata.csv", k));
        List<String> strings = DataReader.readDataAsStrings(dataFile);

        Trie t = new Trie(strings);
//        de.saarland.hamming_trove.Trie t = new de.saarland.hamming_trove.Trie(strings);
        t.buildMismatchesIndex(k);

        File queriesFile = new File(String.format("res/hamming/geonames_k%d_testqueries.csv", k));
        List<DataReader.Query> queries = DataReader.readQueries(queriesFile);

        File answersFile = new File(String.format("res/hamming/geonames_k%d_testanswers.csv", k));
        List<DataReader.Answer> answers = DataReader.readAnswers(answersFile);

	    int i = 1;
        for (DataReader.Query q : queries) {
            int qid = q.id;

            System.out.printf("%d. Query %d STARTED...\n", i++, qid);

            String pattern = q.pattern;
            int localK = q.k;

            Set<Integer> myAnswers = t.search(pattern, localK);
//            TIntSet myAnswers = t.search(pattern, localK);
            DataReader.Answer answer = answers.get(qid-1);
            if (answer.queryId != qid) {
                System.err.println("Mismatch!!!");
            }
            Set<Integer> correctAnswers = answer.positions;

            if (myAnswers.size() != correctAnswers.size()) {
//			test
                System.out.println();
                System.out.printf("\t%s    Query\n", pattern);
//				System.out.printf("\tMyAnswers: ");
                for (int myanswer : myAnswers) {
//                TIntIterator iterator = myAnswers.iterator();
//                while (iterator.hasNext()) {
//                    int myanswer = iterator.next();
//					System.out.printf("%d, %s", myanswer+1, strings.get(myanswer));
                    System.out.printf("\t%s     MyAnswer\n", strings.get(myanswer));
                }
                System.out.println();
//
//				System.out.printf("\tCorrectAnswers: ");
                for (int correctAnswer : correctAnswers) {
//					System.out.printf("%d, ", correctAnswer);
                    System.out.printf("\t%s     CorrectAnswer\n", strings.get(correctAnswer-1));
                }
                System.out.println();
            }
//			end test

            assertEquals(myAnswers.size(), correctAnswers.size());
            for (int myAnswer : myAnswers) {
//            TIntIterator iterator = myAnswers.iterator();
//            while (iterator.hasNext()) {
//                int myAnswer = iterator.next();
                assertTrue(correctAnswers.contains(myAnswer+1));
            }
            System.out.printf("Query %d PASSED\n", qid);
        }

        System.out.println("FINISH");

        assertTrue(true);

    }

    public void testGeoNamesK4() {
//		System.out.println("START");

		int k = 4;
		File dataFile = new File(String.format("res/hamming/geonames_k%d_testdata.csv", k));
		List<String> strings = DataReader.readDataAsStrings(dataFile);

		Trie t = new Trie(strings);
		t.buildMismatchesIndex(k);

		File queriesFile = new File(String.format("res/hamming/geonames_k%d_testqueries.csv", k));
		List<DataReader.Query> queries = DataReader.readQueries(queriesFile);

		File answersFile = new File(String.format("res/hamming/geonames_k%d_testanswers.csv", k));
		List<DataReader.Answer> answers = DataReader.readAnswers(answersFile);

	    int i = 1;
	    int iterations = 40;
	    while (iterations-- > 0) {
		for (DataReader.Query q : queries) {
			int qid = q.id;

			System.out.printf("%d. Query %d STARTED...\n", i++, qid);

			String pattern = q.pattern;
			int localK = q.k;

			Set<Integer> myAnswers = t.search(pattern, localK);
			DataReader.Answer answer = answers.get(qid-1);
			if (answer.queryId != qid) {
				System.err.println("Mismatch!!!");
			}
			Set<Integer> correctAnswers = answer.positions;

			if (myAnswers.size() != correctAnswers.size()) {
//			test
				System.out.println();
				System.out.printf("\t%s    Query\n", pattern);
//				System.out.printf("\tMyAnswers: ");
				for (int myanswer : myAnswers) {
//					System.out.printf("%d, %s", myanswer+1, strings.get(myanswer));
					System.out.printf("\t%s     MyAnswer\n", strings.get(myanswer));
				}
				System.out.println();
//
//				System.out.printf("\tCorrectAnswers: ");
				for (int correctAnswer : correctAnswers) {
//					System.out.printf("%d, ", correctAnswer);
					System.out.printf("\t%s     CorrectAnswer\n", strings.get(correctAnswer-1));
				}
				System.out.println();
			}
//			end test

			assertEquals(myAnswers.size(), correctAnswers.size());
			for (int myAnswer : myAnswers) {
				assertTrue(correctAnswers.contains(myAnswer+1));
			}
//			System.out.printf("Query %d PASSED\n\n", qid);
		}
	    }
//	    ObjectPool pool = ObjectPool.getInstance();
//	    System.out.println("max created pairs: " + pool.getMaxPairs());
//	    System.out.println("max created queries: " + pool.getMaxQueries());

//		System.out.println("FINISH");

		assertTrue(true);
	}
}
