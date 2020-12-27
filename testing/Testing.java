package testing;

import java.io.File;
import java.util.BitSet;

import com.bitfunnel.utility.MurmurHash3;
import com.bitfunnel.utility.FileUtility;
import com.bitfunnel.bloomfilter.BloomFilter;;;

public class Testing {
	MurmurHash3 m = new MurmurHash3();

	public static void main(String[] args) {
		System.out.println("hello Java. Again32.");
		String data=new String("What the fuck brod");
		String data1=new String("What the fuck bro22d");
		String data2=new String("What the fuck bro11d");
		String data3=new String("Prashant");

		int offset=3;
		int len=4;
		int seed=42;
		int x;
		x=MurmurHash3.murmurhash3_x86_32(data, offset, len, seed);
		System.out.println(x);
		x=MurmurHash3.murmurhash3_x86_32(data1, offset, len, seed);
		System.out.println(x);
		x=MurmurHash3.murmurhash3_x86_32(data2, offset, len, seed);
		System.out.println(x);
		x=MurmurHash3.murmurhash3_x86_32(data3, offset, len, seed);
		System.out.println(x);

		final File folder = new File("D:\\!! Prashant Credential info !!\\IIT Hyderabad\\Second Semester\\Scalable Algorithm\\Assignment 1\\BitFunnel\\data");
		System.out.println(FileUtility.listFilesForFolder(folder));
		
		
		BitSet d=new BitSet();
		d.set(3);
		System.out.println(d);
		
		
	}
}
