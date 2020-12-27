package com.bitfunnel.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileUtility {
	

	public static ArrayList<String> listFilesForFolder(final File folder) {
		 ArrayList<String> fileNames = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				//System.out.println(fileEntry.getName());
				fileNames.add(fileEntry.getName());
			}
		}
		return fileNames;
	}
	
	public static Object readObjectFromFile(String fileName) throws ClassNotFoundException, IOException {
		FileInputStream fin = new FileInputStream(fileName);
	    ObjectInputStream ois = new ObjectInputStream(fin);
	    
	    Object os=ois.readObject();
	    
	    fin.close();
	    ois.close();
	    return os;
	    
	}
	

	public static void writeObjectToFile(Object obj,String fileName) throws IOException {
		 	FileOutputStream fos = new FileOutputStream(fileName);
	        ObjectOutputStream oos = new ObjectOutputStream(fos);
	        oos.writeObject(obj);
	        fos.flush();
	        oos.flush();
	        fos.close();
	        oos.close();
	}

}
