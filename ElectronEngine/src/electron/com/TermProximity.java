package electron.com;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

public class TermProximity {
	static int NumberOfDoc;
	static ListManager theListManager;
	static TreeMap<String, String> DocIDPageMap;
	static TreeMap<String, Character> Wordmap = new TreeMap<String, Character>();
	static TreeMap<Integer, Double> PositionScore = new TreeMap<Integer, Double>();
	static TreeMap<Integer, Integer> DocLength;

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

	public void DesearalizeDocLength() {
		File DocLengthFile = new File("F:/IR/dat/DocLength.dat");
		if (DocLengthFile.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(DocLengthFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);
				DocLength = (TreeMap<Integer, Integer>) ois.readObject();
				if (DocLength != null) {
					System.out.println("DocLengthFile loaded successfully");
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

	TermProximity() {
		theListManager = new ListManager();
		theListManager.InitializeFrequencyMatrix();
		theListManager.InitializeKeyList();
		DesearalizeDocIDPageMap();
		NumberOfDoc = DocIDPageMap.size();
	}

	public void StartTermProximity(TreeMap<String, Integer> pQuery) {
		if (pQuery.size() > 1) {
			Set<String> keys = pQuery.keySet();
			int i = 65;
			for (String qkey : keys) {
				Wordmap.put(qkey, (char) i);
				i++;
			}
			DesearalizeDocLength();
			for (int j = 0; j < NumberOfDoc; j++) {
				int count = 0;
				TreeMap<Integer, Character> KeyPosition = new TreeMap<Integer, Character>();
				TreeMap<String, Integer> KeyPresent = new TreeMap<String, Integer>();
				int max = 0;
				for (String key : keys) {

					int keyIndex = theListManager.KeyList.indexOf(key);
					int frequency = (int) ListManager.FrequencyMatric[keyIndex][j];

					if (frequency > 0) {
						count++;
						KeyPresent.put(key, frequency);
						ArrayList<Integer> PositionList = theListManager.GetPositionForKeyInDoc(key, j + 1);
						for (int temp : PositionList) {
							if (max < temp)
								max = temp;
							KeyPosition.put(temp, Wordmap.get(key));
						}

					}
				}
				double score = 0;
				if (count > 1) {
					Set<Integer> positions = KeyPosition.keySet();

					positions.size();
					char[] docPosition = new char[max + 1];
					int k = 0;
					for (int pos : positions) {
						if (k < pos) {
							while (k < pos) {
								docPosition[k] = 'x';
								k++;
							}
						}
						docPosition[pos] = KeyPosition.get(pos);
						k++;
					}

					MinDistance myDistanceHelper = new MinDistance(KeyPresent);
					int MinDIstance = myDistanceHelper.GetMinDistance(docPosition);

					double pageFraction = (double) KeyPresent.size() / (double) pQuery.size();
					score = (double) 1 + (double) MinDIstance * (double) 100 / (double) DocLength.get(j) * pageFraction;

				}
				System.out.println("Doc ID " + j + " Score " + score);
				PositionScore.put(j, score);
			}
		}
		System.out.println("Exiting the Term proximity");
	}
}
