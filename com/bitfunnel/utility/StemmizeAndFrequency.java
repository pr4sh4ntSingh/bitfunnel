package com.bitfunnel.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

/**
 * @author singpra2
 *
 */
public class StemmizeAndFrequency {

	private HashMap<String, Integer> frequencyMap = new HashMap<>();
	private HashMap<String, HashFunctionDetails> hashFunctionMap= new HashMap<String, HashFunctionDetails>();
	private int m=-1
			;
	public int getM() {
		if (this.m==-1) {
			new Exception("M is not calculated yet.");
		}
		return this.m;
	}

	public HashMap<String, HashFunctionDetails> getNoOfHashFunctions() {
		return hashFunctionMap;
	}

	public HashMap<String, Integer> getFrequencyMap() {
		return this.frequencyMap;
	}

	
	
	/**
	 * @param folderPath
	 * @param d
	 * @param snr
	 * @throws IOException
	 * this is one time operation which is applied on whole corpus.
	 * This function executes each file one by one and store results at class level variables.
	 */
	public void createStemmizeAndFrequencyForDocumentAndHashFunctions(String folderPath, float d, float snr)
			throws IOException {

		final File folder = new File(folderPath);
		ArrayList<String> fileNames = FileUtility.listFilesForFolder(folder);
		for (String file : fileNames) {
			String fulllFilePath = folder + "//" + file;

			createStemmizeAndFrequency(fulllFilePath);
		}
		//System.out.println(this.frequencyMap);
		calculateNoOfHashFunctions(d, snr,fileNames.size());
		calculateM(d,fileNames.size());

	}

	private void calculateNoOfHashFunctions(float d, float snr,int totalDocuments) {
		for (String word : frequencyMap.keySet()) {
			int k = getNoOfHashNumber(word, d, snr,totalDocuments);
			ArrayList<Integer> random_seeds=randomArray(k);
			HashFunctionDetails hfd=new HashFunctionDetails(k, random_seeds);
			hashFunctionMap.put(word, hfd);
		}

	}

	/**
	 * @param fullFilePath
	 * @return
	 * @return frequency hashmap with stemmize words
	 * @throws IOException
	 */
	private void createStemmizeAndFrequency(String fullFilePath) throws IOException {
		//System.out.println(fullFilePath);
		BufferedReader br = new BufferedReader(new FileReader(fullFilePath));
		String st;
		SnowballStemmer stemmer = new englishStemmer();
		HashSet<String> stemmedWordSet= new HashSet<>();
		while ((st = br.readLine()) != null) {
			// System.out.println( st.split(" ")[0].toString());
			for (String word : st.split(" ")) {
				stemmer.setCurrent(word);
				stemmer.stem();
				String stemmedWord = stemmer.getCurrent();
				stemmedWordSet.add(stemmedWord);				
			}
		}
		br.close();
		for(String word:stemmedWordSet) {
			if (frequencyMap.containsKey(word)){
				int f=frequencyMap.get(word);
				frequencyMap.put(word, f+1);
			}else {
				frequencyMap.put(word, 1);
				
			}
		}

	}

	public int getNoOfHashNumber(String word, float d, float snr,int totalDocuments) {
		int freq = frequencyMap.get(word);
		double ai=(double)freq/totalDocuments;
		double term = ai / ((1 - ai) * snr);
		double val=Math.log(term) / Math.log(d);
		double log_term = Math.ceil(val);
		int k = (int) Math.max(3, log_term);
		k=(int)k;
		return k;
	}
	
	private void calculateM(float d,int totalDocuments) {
		double sum=0;
		for(String word:frequencyMap.keySet()) {
			double ai=(double)frequencyMap.get(word)/totalDocuments;
			double ki=hashFunctionMap.get(word).getNoOfHashFunctions();
			sum=sum+ai*ki;
			
		}
		double term=1/(1-d);
		double log_term=Math.log(term);
		int m_val=(int) ((1/log_term)* sum);
		//System.out.println(m_val+"ddd");
		this.m=m_val;
		
		
		
	}
	
	
	private ArrayList<Integer> randomArray(int k){
		ArrayList<Integer> ar=new ArrayList<Integer>();
		 Random rand = new Random(); 
		 for(int i=0;i<k;i++) {
			 int rand_int = rand.nextInt(1000); 
			 ar.add(rand_int);
		 }
		
		return ar;
		
		
	}
}
