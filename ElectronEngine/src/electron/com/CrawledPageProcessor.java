package electron.com;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import org.tartarus.snowball.ext.EnglishStemmer;

public class CrawledPageProcessor {
	String DirLoc = "F:/IR/CrawledPages/";
	String ProcessedFileLoc = "F:/IR/Processed/";
	static int NumberOfDoc;
	static ListManager theListManager;
	static TreeMap<String, String> DocIDPageMap;
	Set<String> StopWordSet;
	static TreeMap<Integer, Integer> DocLength;
	int docID=-1;

	CrawledPageProcessor() {
		StopWordSet = StopListDatManager.DeseralizeStopList();
		theListManager = new ListManager();
		File DocIDIndexFile = new File("F:/IR/dat/DocIDPageMap.dat");
		if (DocIDIndexFile.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(DocIDIndexFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);
				DocIDPageMap = (TreeMap<String, String>) ois.readObject();
				if (DocIDPageMap != null) {
					System.out.println("DocIDPageMaploaded successfully");
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
		} else {
			DocIDPageMap = new TreeMap<String, String>();
		}
		GetDocLengthMap();
	}

	public void GetDocLengthMap() {
		File DocLengthFile = new File("F:/IR/dat/DocLength.dat");
		if (DocLengthFile.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(DocLengthFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);
				DocLength = (TreeMap<Integer, Integer>) ois.readObject();
				if (DocLength != null) {
					System.out.println("DocLength loaded successfully");
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
		} else {
			DocLength = new TreeMap<Integer, Integer>();
		}
		
	}

	public void StartProcessing() {
		File file = new File(DirLoc);
		File[] AllFiles = file.listFiles();
		int i, j;
		j = DocIDPageMap.size();
		NumberOfDoc = AllFiles.length;
		for (i = 0; i < NumberOfDoc; i++, j++) {
			String FileName = AllFiles[i].getName();
			
			ArrayList<String> ArrayListForFile;
			DocIDPageMap.put(Integer.toString(j), FileName);
			try {
				docID=j;
				ProcessFileToGeneratedInvertedList(AllFiles[i], j + 1);
				docID=-1;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			if (theListManager.GetInvertedList().SerTreeMap()) {
				System.out.println("Successfully Searalized");
				ProcessAfterSerInvertedList();
			} else
				System.out.println("Problem in searalizing");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void ProcessAfterSerInvertedList() throws IOException {
		SerDocIDPageMap();
		SerDocLengthMap();
		FileAction MyFileAction = new FileAction(DirLoc, ProcessedFileLoc);
		MyFileAction.MoveDirectoryContent();
		theListManager.CreateMatric(DocIDPageMap.size());
	}

	public void SerDocIDPageMap() throws IOException {
		FileOutputStream fileOut = new FileOutputStream("F:/IR/dat/DocIDPageMap.dat");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		if (DocIDPageMap != null) {
			out.writeObject(DocIDPageMap);
			out.close();
			fileOut.close();
			System.out.println("DocIDPageMap Searalized");
		}

	}

	public void SerDocLengthMap() throws IOException {
		FileOutputStream fileOut = new FileOutputStream("F:/IR/dat/DocLength.dat");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		if (DocLength != null) {
			out.writeObject(DocLength);
			out.close();
			fileOut.close();
			System.out.println("DocLength Searalized");
		}

	}

	public void ProcessFileToGeneratedInvertedList(File pFile, int pDocID) throws FileNotFoundException {
		Scanner s = new Scanner(pFile);

		int i = 0;
		int count=0;
		while (s.hasNext()) {
			String str = s.next();
			// trimming the string
			str = str.trim();
			if (!str.isEmpty()) {
				// Converting to lower case
				str = str.toLowerCase();
				// checking for stopWords
				i++;
				if (!StopWordSet.contains(str)) {
					// Using stemmer to stem the word
					EnglishStemmer Stemmer = new EnglishStemmer();
					Stemmer.setCurrent(str);
					Stemmer.stem();
					String StemmedWord = Stemmer.getCurrent();

					// Adding the word to the inverted index
					// System.out.println(StemmedWord + " " + i);
					theListManager.AddInList(StemmedWord, pDocID, i);
					count++;
				}
				// ArrayListForFile.add();
			}
		}
		s.close();
		DocLength.put(docID, count);
	}

	public ArrayList<String> ProcessArrayToLowerCase(ArrayList<String> pArraylist) {
		ArrayList<String> LowerCase = new ArrayList<String>();
		return null;

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CrawledPageProcessor PageProcessor = new CrawledPageProcessor();
		PageProcessor.StartProcessing();

	}

}
