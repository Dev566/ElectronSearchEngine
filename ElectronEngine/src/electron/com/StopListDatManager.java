package electron.com;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeMap;

public class StopListDatManager {
	static BufferedReader SWStopWord;
	static Set<String> StopWords = new LinkedHashSet<String>();

	public static void SearilizeVisitedMap() {
		try {
			FileOutputStream fileOut = new FileOutputStream("F:/IR/dat/StopWorList.dat");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			if (StopWords != null) {
				out.writeObject(StopWords);
				out.close();
				fileOut.close();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Set<String> DeseralizeStopList() {
		File StopListFIle = new File("F:/IR/dat/StopWorList.dat");
		if (StopListFIle.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(StopListFIle);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);
				StopWords = (Set<String>) ois.readObject();
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

		}
		if (StopWords != null)
			System.out.println(StopWords);
		return StopWords;
	}

	public void UpdateStopList() {
		try {
			SWStopWord = new BufferedReader(new FileReader("F:/IR/StopWordList.txt"));
			for (String line; (line = SWStopWord.readLine()) != null;)
				StopWords.add(line.trim());

			SWStopWord.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		SearilizeVisitedMap();

	}

	public static void main(String[] args) {

		// DeseralizeStopList();

	}

}
