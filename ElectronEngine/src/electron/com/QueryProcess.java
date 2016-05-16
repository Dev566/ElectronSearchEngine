package electron.com;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import org.tartarus.snowball.ext.EnglishStemmer;

public class QueryProcess implements Serializable {
	static int NumberOfDoc;
	static ListManager theListManager;
	static TreeMap<String, String> DocIDPageMap;
	static NavigableMap ResultSimilarity = null;
	static NavigableMap FinalResult = null;
	Spider curSpider;
	static double[] QueryVector;
	static TreeMap<String, Integer> hash;
	static TreeMap<Integer, Double> DocSimilarityMap = new TreeMap<Integer, Double>();

	public void DesearalizeDocIDPageMap() {
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
	}

	public TreeMap<String, String> DesearalizeTitleMap() {
		TreeMap<String, String> TitleMap = null;
		File DocIDIndexFile = new File("F:/IR/dat/TitleMap.dat");
		if (DocIDIndexFile.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(DocIDIndexFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);
				TitleMap = (TreeMap<String, String>) ois.readObject();
				if (TitleMap != null) {
					System.out.println("TitleMaploaded successfully");
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
			TitleMap = new TreeMap<String, String>();
		}
		return TitleMap;
	}

	QueryProcess() {
		theListManager = new ListManager();
		DesearalizeDocIDPageMap();
		theListManager.InitializeFrequencyMatrix();
		theListManager.InitializeKeyList();
		NumberOfDoc = DocIDPageMap.size();
		theListManager.InitializeLDocMagnitude(NumberOfDoc);
		theListManager.InitializeLogatithmicDocF();
		theListManager.InitializeLogatithmicDocF();
		curSpider = new Spider();
	}

	public static void ProcessQuery(String pMessage) {
		hash = new TreeMap<String, Integer>();
		;
		EnglishStemmer english = new EnglishStemmer();
		for (String str : pMessage.split(" ")) {
			str = str.toLowerCase();
			english.setCurrent(str);
			english.stem();
			String StemmedWord = english.getCurrent();

			if (hash.containsKey(StemmedWord))
				hash.put(StemmedWord, hash.get(StemmedWord) + 1);
			else
				hash.put(StemmedWord, 1);
		}
		int i = 0;
		if (hash != null) {
			double[][] QueryMatric = new double[NumberOfDoc][hash.size()];
			Set<String> keys = hash.keySet();
			QueryVector = new double[hash.size()];
			for (String key : keys) {
				int df = 0;
				int keyIndex = theListManager.KeyList.indexOf(key);
				if (keyIndex != -1) {
					double LogIDF = theListManager.LogatithmicDocF.get(key);
					for (int j = 0; j < NumberOfDoc; j++) {
						QueryMatric[j][i] = (double) ListManager.FrequencyMatric[keyIndex][j] * LogIDF;
						// if(ListManager.DocMagnitude[j]>0)
						// QueryMatric[j][i]
						// =QueryMatric[j][i]/ListManager.DocMagnitude[j];
						String strDouble = String.format("%.4f", QueryMatric[j][i]);
						System.out.print(strDouble + "  ");
						if (QueryMatric[j][i] > 0)
							df += 1;

					}
					double divide = Math.log10(NumberOfDoc / (double) df);
					String strDouble = String.format("%.4f", divide);
					System.out.print(key + "  " + strDouble + "\n");
					QueryVector[i] = LogIDF * (double) hash.get(key);

				}
				i++;
			}
			i = 0;
			System.out.println("Query Vector : ");
			for (String key : keys) {
				String strDouble = String.format("%.4f", QueryVector[i]);
				System.out.print(key + "  " + strDouble + "\n");
				i++;
			}
			TreeMap<String, ArrayList<String>> Similarity = new TreeMap<String, ArrayList<String>>();
			TreeMap<String, String> SimilarityMap = new TreeMap<String, String>();
			for (int j = 0; j < NumberOfDoc; j++) {
				theListManager.Document = j;
				double similarity = theListManager.GetSimilarity(QueryMatric[j], QueryVector);
				DocSimilarityMap.put(j, similarity);
				String strDouble = String.format("%.7f", similarity);
				SimilarityMap.put(strDouble, Integer.toString(j));
				ArrayList tempArray;
				tempArray = Similarity.get(strDouble);
				if (tempArray == null) {
					tempArray = new ArrayList();
					tempArray.add(Integer.toString(j));
					Similarity.put(strDouble, tempArray);
				} else {
					tempArray.add(Integer.toString(j));
				}
				System.out.println("Similarity " + j + " " + similarity);
			}
			theListManager.Document = -1;
			ResultSimilarity = SimilarityMap.descendingMap();
			// Set<String> ResultKey = ResultSimilarity.keySet();
			// for (String tempKey : ResultKey) {
			// System.out.println("Doc ID : " + ResultSimilarity.get(tempKey) +
			// " : " + tempKey);
			// }

			FinalResult = Similarity.descendingMap();

			// Set<String> ResultKey1 = FinalResult.keySet();
			// ArrayList<String> tempArray1;
			// for (String tempKey : ResultKey1) {
			// System.out.println("Similar : " + tempKey);
			// tempArray1 = (ArrayList<String>) FinalResult.get(tempKey);
			// for (String str : tempArray1) {
			// System.out.print(" " + str);
			// }
			// System.out.println();
			// }
		}
	}

	public static String GetLinkMaping(String pStr) {
		if (DocIDPageMap != null)
			return DocIDPageMap.get(pStr);
		return "";

	}

	public double[] GetRelevantVector(ArrayList<Integer> RelevantDocument) {
		double[] RelevantVector = new double[theListManager.theTreeMapList.theTreeMap.size()];
		for (int i = 0; i < RelevantDocument.size(); i++) {
			for (int j = 0; j < TreeMapInvertedList.theTreeMap.size(); j++) {
				RelevantVector[j] += theListManager.FrequencyMatric[j][RelevantDocument.get(i)];
			}
		}
		return RelevantVector;
	}

	public double[] GetNonRelevantVector(ArrayList<Integer> NonRelevantDocument) {
		double[] NonRelevantVector = new double[theListManager.theTreeMapList.theTreeMap.size()];
		for (int i = 0; i < NonRelevantDocument.size(); i++) {
			for (int j = 0; j < TreeMapInvertedList.theTreeMap.size(); j++) {
				NonRelevantVector[j] += theListManager.FrequencyMatric[j][NonRelevantDocument.get(i)];
			}
		}
		return NonRelevantVector;
	}

	public double[] GetQueryVector() {
		double[] FullQueryVector = new double[theListManager.theTreeMapList.theTreeMap.size()];
		if (hash != null) {

			Set<String> keys = hash.keySet();
			for (String key : keys) {
				int keyIndex = theListManager.KeyList.indexOf(key);
				if (keyIndex != -1) {
					double LogIDF = theListManager.LogatithmicDocF.get(key);
					FullQueryVector[keyIndex] = (double) LogIDF * (double) hash.get(key);
				}
			}
		}
		return FullQueryVector;
	}

	public double[] GetDocVecterID(int DocID) {
		double[] DocVector = new double[theListManager.theTreeMapList.theTreeMap.size()];
		for (int j = 0; j < TreeMapInvertedList.theTreeMap.size(); j++) {
			DocVector[j] += theListManager.FrequencyMatric[j][DocID];
		}
		return DocVector;
	}

	public void RelevanceFeedBack(ArrayList<Integer> RelevantDocument, ArrayList<Integer> NonRelevantDocument) {
		double NewMagnitude = 0;
		double[] RelevantVector = GetRelevantVector(RelevantDocument);
		double[] NonRelevantVector = GetNonRelevantVector(NonRelevantDocument);
		double[] NewQueryVector = GetQueryVector();
		double sum = 0;
		for (int j = 0; j < TreeMapInvertedList.theTreeMap.size(); j++) {
			NewQueryVector[j] = (double) NewQueryVector[j] + (double) 0.75 * (double) RelevantVector[j]
					- (double) 0.15 * (double) NonRelevantVector[j];
			sum += (double) NewQueryVector[j] + (double) RelevantVector[j] + (double) NonRelevantVector[j];
			NewMagnitude += (double) NewQueryVector[j] * (double) NewQueryVector[j];
		}
		NewMagnitude = Math.sqrt(NewMagnitude);
		for (int j = 0; j < TreeMapInvertedList.theTreeMap.size(); j++) {
			NewQueryVector[j] = (double) NewQueryVector[j] / (double) NewMagnitude;
		}
		// Calculating similarity with the new query
		TreeMap<String, ArrayList<String>> Similarity = new TreeMap<String, ArrayList<String>>();
		TreeMap<String, String> SimilarityMap = new TreeMap<String, String>();
		for (int j = 0; j < NumberOfDoc; j++) {

			double similarity = theListManager.GetSimilarity(GetDocVecterID(j), NewQueryVector);
			String strDouble = String.format("%.7f", similarity);
			SimilarityMap.put(strDouble, Integer.toString(j));
			ArrayList tempArray;
			tempArray = Similarity.get(strDouble);
			if (tempArray == null) {
				tempArray = new ArrayList();
				tempArray.add(Integer.toString(j));
				Similarity.put(strDouble, tempArray);
			} else {
				tempArray.add(Integer.toString(j));
			}
			System.out.println("Similarity " + j + " " + similarity);
		}
		theListManager.Document = -1;
		if (ResultSimilarity != null)
			ResultSimilarity.clear();
		if (FinalResult != null)
			FinalResult.clear();
		ResultSimilarity = SimilarityMap.descendingMap();
		FinalResult = Similarity.descendingMap();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		QueryProcess NewQuery = new QueryProcess();
		NewQuery.ProcessQuery("Chadwick");
	}

}
