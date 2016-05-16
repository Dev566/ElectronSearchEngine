package electron.com;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;
import java.util.TreeMap;


public class TreeMapInvertedList implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8710590358522061863L;
	static TreeMap<String, InvertedList> theTreeMap;

	TreeMapInvertedList() {
		File InvertedIndexFile = new File("F:/IR/dat/InvertedIndexFile.dat");
		if (InvertedIndexFile.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(InvertedIndexFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);
				theTreeMap = (TreeMap<String, InvertedList>) ois.readObject();
				if(theTreeMap!=null){
					System.out.println("Tree map loaded successfully");
				}
				ois.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			theTreeMap = new TreeMap<String, InvertedList>();
	}

	public boolean SerTreeMap() throws IOException {
		FileOutputStream fileOut = new FileOutputStream("F:/IR/dat/InvertedIndexFile.dat");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		if (theTreeMap != null) {
			out.writeObject(theTreeMap);
			out.close();
			fileOut.close();
			return true;
		}
		out.close();
		return false;

	}

	public InvertedList GetInvertedList(String pKey) {

		return theTreeMap.get(pKey);
	}

	public void AddKeyValuePair(String pKey, InvertedList pInvertedList) {
		theTreeMap.put(pKey, pInvertedList);
	}

	public static void DisplayAllKeySet() {
		Set<String> keys = theTreeMap.keySet();

		for (String key : keys) {
			System.out.println(key);
		}
	}


}
