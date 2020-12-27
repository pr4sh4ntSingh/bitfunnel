package com.bitfunnel.bloomfilter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

import com.bitfunnel.utility.HashFunctionDetails;
import com.bitfunnel.utility.MurmurHash3;

public class BloomFilter {

	private BitSet bloomFilter = new BitSet();
	private HashMap<String, Integer> noOfHashFunctions = new HashMap<>();

	public HashMap<String, Integer> getNoOfHashFunctions() {
		return noOfHashFunctions;
	}

	public BitSet getBloomFilter() {
		return bloomFilter;
	}

	public BitSet createBloomFilterForDocument(String fullFilePath, float d, float snr,
			HashMap<String, Integer> frequencyMap, HashMap<String, HashFunctionDetails> noOfHashFunctions,int i,int m) throws IOException {
		
		//System.out.println(noOfHashFunctions);
		System.out.println(i+"th File ");
		BufferedReader br = new BufferedReader(new FileReader(fullFilePath));
		String st;
		SnowballStemmer stemmer = new englishStemmer();
		while ((st = br.readLine()) != null) {
			for (String word : st.split(" ")) {
				word=word.toLowerCase();
				stemmer.setCurrent(word);
				stemmer.stem();
				String stemmedWord = stemmer.getCurrent();
				
				int num_of_hash_functions = noOfHashFunctions.get(stemmedWord).getNoOfHashFunctions();
				ArrayList<Integer> randomSeeds=noOfHashFunctions.get(stemmedWord).getRandom_seeds();
				setBloomFilterForWord(stemmedWord, num_of_hash_functions,m,randomSeeds);
				
				
			}	
		}
		br.close();
		return this.bloomFilter;

	}

	/**
	 * @param word
	 * @param num_of_hash_functions creates hashvalue of word and sets BloomFilters
	 *                              bit to 1 for given word
	 */
	private void setBloomFilterForWord(String word, int num_of_hash_functions,int m, ArrayList<Integer> randomSeeds) {
		for (int i = 0; i < num_of_hash_functions; i++) {
			int randomSeed=randomSeeds.get(i);
			int hashValue = getHashValue(word, randomSeed,m);
			bloomFilter.set(hashValue);
		}

	}

	public int getHashValue(String word, int seed,int m) {
		int hashvalue = Math.abs((MurmurHash3.murmurhash3_x86_32(word, 0, word.length(), seed))) % m;
		return hashvalue;
	}

}
