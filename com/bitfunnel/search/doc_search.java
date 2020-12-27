package com.bitfunnel.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

import com.bitfunnel.utility.FileUtility;
import com.bitfunnel.utility.HashFunctionDetails;
import com.bitfunnel.utility.MurmurHash3;
import com.bitfunnel.utility.PrintBitSet;

public class doc_search {

	public static void main(String args[]) throws IOException, ClassNotFoundException {

		String queryFilePath = args[0];

		Object obj1 = FileUtility.readObjectFromFile("frequencyMap");
		Object obj2 = FileUtility.readObjectFromFile("noOfHashFunctions");

		Object obj3 = FileUtility.readObjectFromFile("index");

		Object obj4 = FileUtility.readObjectFromFile("m");

		Object obj5 = FileUtility.readObjectFromFile("fileNames");

		Object obj6 = FileUtility.readObjectFromFile("invertedIndex");

		@SuppressWarnings("unchecked")
		ArrayList<BitSet> invertedIndex = (ArrayList<BitSet>) obj6;

		int m = (int) obj4;
		@SuppressWarnings("unchecked")
		HashMap<String, Integer> frequencyMap = (HashMap<String, Integer>) obj1;
		@SuppressWarnings("unchecked")
		HashMap<String, HashFunctionDetails> noOfHashFunctions = (HashMap<String, HashFunctionDetails>) obj2;

		BitSet index[] = (BitSet[]) obj3;

		ArrayList<String> fileNames = (ArrayList<String>) obj5;

		// System.out.println(frequencyMap);
		// System.out.println(index[0]);

		System.out.println("Loading Complete");
		System.out.println("rows =" + index.length + " columns=" + index[0].length());

		System.out.println("rows =" + invertedIndex.size() + " columns=" + invertedIndex.get(0).length());

		/*
		 * for (int i = 0; i < invertedIndex.size(); i++) {
		 * System.out.println(invertedIndex.get(i));
		 * 
		 * }
		 * 
		 * for (int i = 0; i < invertedIndex.size(); i++) {
		 * System.out.println(PrintBitSet.printBits("", invertedIndex.get(i))); }
		 */

		/**  **/
		long startTime = System.nanoTime();
		
		
		BufferedReader br = new BufferedReader(new FileReader(queryFilePath));
		String st;
		SnowballStemmer stemmer = new englishStemmer();

		// HashSet<String> stemmedWordSet = new HashSet<>();
		ArrayList<String> queryList = new ArrayList<String>();
		while ((st = br.readLine()) != null) {
			// System.out.println( st.split(" ")[0].toString());
			StringBuilder sb = new StringBuilder();
			for (String word : st.split(" ")) {
				word = word.toLowerCase();
				stemmer.setCurrent(word);
				stemmer.stem();
				String stemmedWord = stemmer.getCurrent();
				// stemmedWordSet.add(stemmedWord);
				//System.out.println(stemmedWord);
				sb.append(stemmedWord + " ");
			}
			queryList.add(sb.toString());
		}
		br.close();

		FileWriter outputWriter = new FileWriter("Result.txt");
		boolean notInUniverse = false;
		for (String queryLine : queryList) {
			System.out.println("Searching for queryLine " + queryLine);

			BitSet queryBloomFilter = new BitSet();
			String[] stemmedWordSet = queryLine.split(" ");
			for (String stemmedWord : stemmedWordSet) {
				try {
					int hashFunctions = noOfHashFunctions.get(stemmedWord).getNoOfHashFunctions();
					ArrayList<Integer> randomSeeds = noOfHashFunctions.get(stemmedWord).getRandom_seeds();

					queryBloomFilter = setBloomFilterForWord(queryBloomFilter, stemmedWord, hashFunctions, m,
							randomSeeds);

					//System.out.println(stemmedWord + "is stemmedWord with" + randomSeeds + " k=" + hashFunctions);
				} catch (Exception e) {
					outputWriter.write("--" + queryLine + "--\n");
					outputWriter.write("\tNo matches.\n");
					notInUniverse = true;
					break;
				}
			}
			//if (notInUniverse) {
			///	break;
			//}

			//System.out.println(queryBloomFilter + " is bloomfilter of word ");

			ArrayList<Integer> positions = getSetBits(queryBloomFilter);

			//System.out.println(positions + " are the one that we need");

			//System.out.println("8888");

			/***   ***/

			BitSet query = queryBloomFilter; // new BitSet();
//
//		for (BitSet bs : invertedIndex) {
//			bs.and(query);
//			System.out.println(bs.equals(query));
//
//			System.out.print(bs);
//
//		}
			try {
				BitSet hash1 = invertedIndex.get(positions.get(0));
				for (int position : positions) {

					BitSet hash2 = invertedIndex.get(position);
					hash1.and(hash2);
					// PrintBitSet.printBits(" hash is",(hashes));

				}

				//System.out.println(hash1);

				ArrayList<Integer> matchedDoc = getSetBits(hash1);

				outputWriter.write(" --- " + queryLine + "----\n");
				for (int n : matchedDoc) {
					outputWriter.write("\t Match found at " + fileNames.get(n) + "\n");
				}
			} catch (Exception e) {
				System.out.println( " ");
			}

		}
		
		long endTime = System.nanoTime();

		long duration = (endTime - startTime)/1000000 ;
		
		System.out.println("Took "+duration+" millisecond to query ");
		outputWriter.close();

	}

	/**
	 * 
	 * @param bArray returns array whose each row represents each position like
	 *               row_0 shows first bit of all documents
	 */

	/**
	 * @param word
	 * @param num_of_hash_functions creates hashvalue of word and sets BloomFilters
	 *                              bit to 1 for given word
	 */
	private static BitSet setBloomFilterForWord(BitSet querybloomFilter, String word, int num_of_hash_functions, int m,
			ArrayList<Integer> randomSeeds) {
		for (int i = 0; i < num_of_hash_functions; i++) {
			int randomSeed = randomSeeds.get(i);
			int hashValue = getHashValue(word, randomSeed, m);
			querybloomFilter.set(hashValue);
		}
		return querybloomFilter;

	}

	private static int getHashValue(String word, int seed, int m) {
		int hashvalue = Math.abs((MurmurHash3.murmurhash3_x86_32(word, 0, word.length(), seed))) % m;
		return hashvalue;
	}

	private static ArrayList<Integer> getSetBits(BitSet queryBloomFilter) {

		ArrayList<Integer> positions = new ArrayList<Integer>();
		{
			int i = queryBloomFilter.nextSetBit(0);

			if (i != -1) {
				positions.add(i);
				for (i = queryBloomFilter.nextSetBit(i + 1); i >= 0; i = queryBloomFilter.nextSetBit(i + 1)) {
					int endOfRun = queryBloomFilter.nextClearBit(i);
					do {
						positions.add(i);
					} while (++i < endOfRun);
				}

			}

		}
		return positions;
	}
}
