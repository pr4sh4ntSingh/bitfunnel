package com.bitfunnel.utility;

import java.io.Serializable;
import java.util.ArrayList;

public class HashFunctionDetails implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int noOfHashFunctions;
	private ArrayList<Integer> random_seeds=new ArrayList<Integer>();
	


	public int getNoOfHashFunctions() {
		return noOfHashFunctions;
	}

	public ArrayList<Integer> getRandom_seeds() {
		return random_seeds;
	}

	public HashFunctionDetails(int noOfHashFunctions, ArrayList<Integer> random_seeds) {
		super();
		this.noOfHashFunctions = noOfHashFunctions;
		this.random_seeds = random_seeds;
	}
	
	@Override
	public String toString() {
        return " : "+noOfHashFunctions+","+ random_seeds.toString() +"\n";
	}
}
