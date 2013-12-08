package de.saarland.hamming;

import de.saarland.util.DataReader;
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
		File dataFile = new File("res/hamming/geonames_k0_testdata.csv");
		List<String> strings = DataReader.readDataAsStrings(dataFile);

		Trie t = new Trie(strings);
		t.buildMismatchesIndex(k);

		File queriesFile = new File("res/hamming/geonames_k0_testqueries.csv");
		List<DataReader.Query> queries = DataReader.readQueries(queriesFile);

		File answersFile = new File("res/hamming/geonames_k0_testanswers.csv");
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
		System.out.println("START");

		int k = 1;
		File dataFile = new File("res/hamming/geonames_k1_testdata.csv");
		List<String> strings = DataReader.readDataAsStrings(dataFile);

		Trie t = new Trie(strings);
		t.buildMismatchesIndex(k);

		File queriesFile = new File("res/hamming/geonames_k1_testqueries.csv");
		List<DataReader.Query> queries = DataReader.readQueries(queriesFile);

		File answersFile = new File("res/hamming/geonames_k1_testanswers.csv");
		List<DataReader.Answer> answers = DataReader.readAnswers(answersFile);

		for (DataReader.Query q : queries) {
			int qid = q.id;

			System.out.printf("Query %d STARTED...\n", qid);

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

	public void testGeoNamesK2() {
//		System.out.println("START");
//
//		int k = 2;
//		File dataFile = new File("res/hamming/geonames_k2_testdata.csv");
//		List<String> strings = DataReader.readDataAsStrings(dataFile);
//
//		Trie t = new Trie(strings);
//		t.buildMismatchesIndex(k);
//
//		File queriesFile = new File("res/hamming/geonames_k2_testqueries.csv");
//		List<DataReader.Query> queries = DataReader.readQueries(queriesFile);
//
//		File answersFile = new File("res/hamming/geonames_k2_testanswers.csv");
//		List<DataReader.Answer> answers = DataReader.readAnswers(answersFile);
//
//		for (DataReader.Query q : queries) {
//			int qid = q.id;
//
//			System.out.printf("Query %d STARTED...\n", qid);
//
//			String pattern = q.pattern;
//			int localK = q.k;
//
//			Set<Integer> myAnswers = t.search(pattern, localK);
//			DataReader.Answer answer = answers.get(qid-1);
//			if (answer.queryId != qid) {
//				System.err.println("Mismatch!!!");
//			}
//			Set<Integer> correctAnswers = answer.positions;
//
//			if (myAnswers.size() != correctAnswers.size()) {
////			test
//				System.out.println();
//				System.out.printf("\t%s    Query\n", pattern);
////				System.out.printf("\tMyAnswers: ");
//				for (int myanswer : myAnswers) {
////					System.out.printf("%d, %s", myanswer+1, strings.get(myanswer));
//					System.out.printf("\t%s     MyAnswer\n", strings.get(myanswer));
//				}
//				System.out.println();
////
////				System.out.printf("\tCorrectAnswers: ");
//				for (int correctAnswer : correctAnswers) {
////					System.out.printf("%d, ", correctAnswer);
//					System.out.printf("\t%s     CorrectAnswer\n", strings.get(correctAnswer-1));
//				}
//				System.out.println();
//			}
////			end test
//
//			assertEquals(myAnswers.size(), correctAnswers.size());
//			for (int myAnswer : myAnswers) {
//				assertTrue(correctAnswers.contains(myAnswer+1));
//			}
//			System.out.printf("Query %d PASSED\n", qid);
//		}
//
//		System.out.println("FINISH");
//
		assertTrue(true);
	}
}
