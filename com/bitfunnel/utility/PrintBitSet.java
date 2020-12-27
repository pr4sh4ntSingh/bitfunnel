package com.bitfunnel.utility;

import java.util.BitSet;

public class PrintBitSet {
	  public static String printBits(String prompt, BitSet b) {
	      System.out.print(prompt + " ");
	      StringBuilder sb=new StringBuilder();
	      for (int i = 0; i < b.length(); i++) {
	         String bitValue=b.get(i) ? "1" : "0";
	         sb.append(bitValue);
	      }
	      return sb.toString();
	   }
	  
}
