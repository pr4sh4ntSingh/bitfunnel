package testing;

import java.util.BitSet;
import java.util.Random;

public class testing2 {

	  public static int N_BITS = 16;
	public static void main(String args[]) {
		BitSet b=new BitSet(N_BITS);
		BitSet b2=new BitSet(N_BITS);
		
		setRandomBits(b);
		setRandomBits(b2);
		System.out.println(b);
		System.out.println(b2);
		b.and(b2);
		System.out.print(b);
	
		
		BitSet[] array = new BitSet[1024];
		
		System.out.println(array[1]);
		
		
	}
	
	
	   public static void setRandomBits(BitSet b) {
		      Random r = new Random();
		      for (int i = 0; i < N_BITS / 2; i++)
		      b.set(r.nextInt(N_BITS));

		   }
}
