package com.bitfunnel.preprocessing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

import com.bitfunnel.bloomfilter.BloomFilter;
import com.bitfunnel.utility.FileUtility;
import com.bitfunnel.utility.HashFunctionDetails;
import com.bitfunnel.utility.PrintBitSet;
import com.bitfunnel.utility.StemmizeAndFrequency;;

public class doc_index {

	public static void main(String[] args)
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		String folderPath = args[0];
		Float d = Float.parseFloat(args[1]);
		Float snr = Float.parseFloat(args[2]);
		int m=0;

		final File folder = new File(folderPath);
		ArrayList<String> fileNames = FileUtility.listFilesForFolder(folder);
		// System.out.println(fileNames);
		// System.out.println("113");
		StemmizeAndFrequency sf = new StemmizeAndFrequency();
		sf.createStemmizeAndFrequencyForDocumentAndHashFunctions(folderPath, d, snr);
		HashMap<String, Integer> frequencyMap = sf.getFrequencyMap();
		HashMap<String, HashFunctionDetails> noOfHashFunctions = sf.getNoOfHashFunctions();

		try {
			 m = Integer.parseInt(args[3]);
		} catch (Exception e) {
			 m = sf.getM();
		}
		System.out.println(m);
		// System.out.println("freqeuncyMap");
		FileWriter myWriter = new FileWriter("frequency.txt");
		myWriter.write(frequencyMap.toString());
		myWriter.close();

		myWriter = new FileWriter("hashFunctions.txt");
		myWriter.write(noOfHashFunctions.toString());
		myWriter.close();

		// int ll=sf.getNoOfHashNumber("Prashant", (float)0.2, (float)0.25, 5);
		// System.out.println(ll+" __dfd");

		myWriter = new FileWriter("index.txt");
		BitSet index[] = new BitSet[fileNames.size()];
		int doc_no = 0;
		for (String file : fileNames) {
			String fulllFilePath = folder + "//" + file;
			BloomFilter bFilter = new BloomFilter();
			// sayad frequency pure corpus ki ek sath calculate karni hai == na ki seperate
			// document ki

			index[doc_no] = bFilter.createBloomFilterForDocument(fulllFilePath, d, snr, frequencyMap, noOfHashFunctions,
					doc_no, m);

			myWriter.write(PrintBitSet.printBits("indexing file :" + doc_no, index[doc_no]) + "\n");
			doc_no++;

		}
		
		myWriter.write(index.toString() + "\n");
		myWriter.close();

		FileUtility.writeObjectToFile(frequencyMap, "frequencyMap");
		FileUtility.writeObjectToFile(noOfHashFunctions, "noOfHashFunctions");
		FileUtility.writeObjectToFile(index, "index");
		FileUtility.writeObjectToFile(m, "m");
		FileUtility.writeObjectToFile(fileNames, "fileNames");
		// FileUtility.writeObjectToFile(index, fileName);
		System.out.println("dfd");

		
		ArrayList<BitSet> invertedIndex = wtf(index,m);
		FileUtility.writeObjectToFile(invertedIndex, "invertedIndex");
	}

	
	public static ArrayList<BitSet> wtf(BitSet[] bArray,int m) {
		int rows = bArray.length;
		int columns = m;//bArray[0].length();
		System.out.println(rows + " * " + columns);
		ArrayList<BitSet> answer = new ArrayList<BitSet>(); // new BitSet[columns+1];

		for (int i = 0; i < columns; i++) {
			BitSet bi = new BitSet(rows);
			bi.clear();
			answer.add(bi);
		}

//		for (int i = 0; i < answer.size(); i++) {
//			System.out.println(answer.get(i) + " p");
//		}

		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				// System.out.println(r + " " + c);
				if (bArray[r].get(c)) {
					answer.get(c).set(r);
				}
			}
		}

		return answer;
	}

}
