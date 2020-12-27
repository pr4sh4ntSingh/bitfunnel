package testing;



import java.util.BitSet;
import java.util.Random;

public class testingBitSet {

   public static int N_BITS = 16;

   public static void main(String[] args) {

      BitSet b1 = new BitSet(N_BITS);
      BitSet b2 = new BitSet(N_BITS);
      printBits("inital bit pattern of b1: ", b1);

      printBits("inital bit pattern of b2: ", b2);

      setRandomBits(b1);
      setRandomBits(b2);

      printBits("After random bit set of b1: ", b1);

      printBits("After random bit set of b2: ", b2);

      b2.and(b1);
      printBits("b2 AND b1, b2 = ", b2);

      System.out.println("No. of set values in b1=" +
         b1.cardinality());
      System.out.println("No. of set values in b2=" +
         b2.cardinality());

      b1.or(b2);
      printBits("b1 OR b2, b1 = ", b1);

      b2.xor(b1);
      printBits("b2 XOR b1, b2 = ", b2);

      printBits("b1 = ", b1);
      System.out.println("indexes where bit is set in b1 " +
         b1.toString());
      printBits("b2 = ", b2);
      System.out.println("indexes where bit is set in b2 " +
         b2.toString());

      
      System.out.println(b2.nextSetBit(14));
   }

   public static void setRandomBits(BitSet b) {
      Random r = new Random();
      for (int i = 0; i < N_BITS / 2; i++)
      b.set(r.nextInt(N_BITS));

   }

   public static void printBits(String prompt, BitSet b) {
      System.out.print(prompt + " ");
      for (int i = 0; i < N_BITS; i++) {
         System.out.print(b.get(i) ? "1" : "0");
      }
      System.out.println();
   }
}


