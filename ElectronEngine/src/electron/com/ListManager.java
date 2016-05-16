package electron.com;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

public class ListManager {
	static double[][] FrequencyMatric;
	static double[] DocMagnitude;
	TreeMapInvertedList theTreeMapList;
	ArrayList<String> KeyList = new ArrayList<String>();
	TreeMap<String, Double> LogatithmicDocF = new TreeMap<String, Double>();
	static int Document = -1;

	ListManager() {
		theTreeMapList = new TreeMapInvertedList();
	}

	public void AddInList(String pWord, int pDocID, int pPosition) {
		InvertedList LastNode;
		InvertedList theTempList = theTreeMapList.GetInvertedList(pWord);
		if (theTempList == null) {
			theTreeMapList.AddKeyValuePair(pWord, new InvertedList(pDocID, 1, pPosition));
			return;
		}

		LastNode = GetLastNode(theTempList);
		if (LastNode.GetDocID() == pDocID) {
			System.out.println(pWord + " " + pDocID);
			LastNode.AddFrequency(pPosition);
		} else {
			LastNode.AddNxtNode(new InvertedList(pDocID, 1, pPosition));
		}

	}

	public void InitializeFrequencyMatrix() {
		File FrequencyMatrixFile = new File("F:/IR/dat/FrequencyMatrix.dat");
		if (FrequencyMatrixFile.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(FrequencyMatrixFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);
				FrequencyMatric = (double[][]) ois.readObject();
				if (FrequencyMatric != null) {
					System.out.println("Frequency Matric successfully");
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
		}
	}

	private void SearlizeFrequencyMatrix() throws IOException {
		FileOutputStream fileOut = new FileOutputStream("F:/IR/dat/FrequencyMatrix.dat");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		if (FrequencyMatric != null) {
			out.writeObject(FrequencyMatric);
			out.close();
			fileOut.close();
			System.out.println("FrequencyMatric Searalized");
		}
	}

	private void SearlizeKeyList() throws IOException {
		FileOutputStream fileOut = new FileOutputStream("F:/IR/dat/KeyList.dat");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		if (KeyList != null) {
			out.writeObject(KeyList);
			out.close();
			fileOut.close();
			System.out.println("KeyList Searalized");
		}
	}

	public void InitializeKeyList() {
		File KeyListFile = new File("F:/IR/dat/KeyList.dat");
		if (KeyListFile.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(KeyListFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);
				KeyList = (ArrayList<String>) ois.readObject();
				if (KeyList != null) {
					System.out.println("KeyList File loaded successfully");
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
			KeyList = new ArrayList<String>();
		}
	}

	public void InitializeLogatithmicDocF() {
		File KeyListFile = new File("F:/IR/dat/LogatithmicDocF.dat");
		if (KeyListFile.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(KeyListFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);
				LogatithmicDocF = (TreeMap<String, Double>) ois.readObject();
				if (LogatithmicDocF != null) {
					System.out.println("LogatithmicDocF successfully");
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
			LogatithmicDocF = new TreeMap<String, Double>();
		}
	}

	public void InitializeLDocMagnitude(int pDocNumber) {
		File DocMagnitudeFile = new File("F:/IR/dat/DocMagnitude.dat");
		if (DocMagnitudeFile.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(DocMagnitudeFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);
				DocMagnitude = (double[]) ois.readObject();
				if (DocMagnitude != null) {
					System.out.println("DocMagnitude successfully");
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
			DocMagnitude = new double[pDocNumber];
		}
	}

	public void CreateMatric(int pDocNumber) {

		InvertedList LastNode;
		InvertedList theTempList;
		if (theTreeMapList != null) {
			// InitializeFrequencyMatrix();
			if (FrequencyMatric == null)
				FrequencyMatric = new double[theTreeMapList.theTreeMap.size()][pDocNumber];
			// InitializeKeyList();
			Set<String> keys = theTreeMapList.theTreeMap.keySet();
			int i = 0;
			for (String key : keys) {

				KeyList.add(key);
				theTempList = theTreeMapList.GetInvertedList(key);
				if (theTempList == null) {
					System.out.println("Word Not found");
				} else {

					while (theTempList != null) {
						int frequencyNode = theTempList.GetFrequency();
						FrequencyMatric[i][theTempList.GetDocID() - 1] = frequencyNode;
						// System.out.print( " "+ frequencyNode);
						if (theTempList.GetNextNode() != null) {
							theTempList = theTempList.GetNextNode();
						} else
							break;
					}
				}
				i++;
				System.out.println("");
			}
			i = 0;
			DecimalFormat Dft = new DecimalFormat("0.000");

			for (String key : keys) {
				int df = 0;
				for (int j = 0; j < pDocNumber; j++) {
					System.out.print(FrequencyMatric[i][j] + "  ");
					if (FrequencyMatric[i][j] > 0)
						df += 1;
				}
				double divide = Math.log10(pDocNumber / (double) df);
				String strDouble = String.format("%.4f", divide);
				LogatithmicDocF.put(key, divide);

				System.out.print(key + "  " + strDouble + "\n");

				i++;
			}
			i = 0;
			DocMagnitude = new double[pDocNumber];
			for (String key : keys) {
				for (int k = 0; k < pDocNumber; k++) {
					if (FrequencyMatric[i][k] > 0)
						DocMagnitude[k] += Math.pow(FrequencyMatric[i][k] * LogatithmicDocF.get(key), 2);
				}
				i++;
			}
			for (int k = 0; k < pDocNumber; k++) {
				DocMagnitude[k] = Math.sqrt(DocMagnitude[k]);
				System.out.println(DocMagnitude[k]);
			}
			// System.out.println(KeyList.get(index));
			try {
				SearlizeKeyList();
				SearlizeFrequencyMatrix();
				SearlizeDocMagnitude();
				SearlizeLogatithmicDocF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void SearlizeDocMagnitude() throws IOException {
		FileOutputStream fileOut = new FileOutputStream("F:/IR/dat/DocMagnitude.dat");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		if (DocMagnitude != null) {
			out.writeObject(DocMagnitude);
			out.close();
			fileOut.close();
			System.out.println("DocMagnitude Searalized");
		}
	}

	private void SearlizeLogatithmicDocF() throws IOException {
		FileOutputStream fileOut = new FileOutputStream("F:/IR/dat/LogatithmicDocF.dat");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		if (LogatithmicDocF != null) {
			out.writeObject(LogatithmicDocF);
			out.close();
			fileOut.close();
			System.out.println("DocMagnitude Searalized");
		}
	}

	public void DisplayKey() {
		theTreeMapList.DisplayAllKeySet();
	}

	public void DisplayRowForWord(String pWord) {
		InvertedList LastNode;
		InvertedList theTempList = theTreeMapList.GetInvertedList(pWord);
		if (theTempList == null) {
			System.out.println("Word Not found");
		} else {

			while (theTempList.GetNextNode() != null) {
				System.out.println("Doc ID " + theTempList.GetDocID());
				theTempList = theTempList.GetNextNode();
			}
			System.out.println("Doc ID" + theTempList.GetDocID());
		}
	}

	public TreeMapInvertedList GetInvertedList() {
		return theTreeMapList;
	}

	public InvertedList GetLastNode(InvertedList pNode) {
		if (pNode.GetNextNode() == null)
			return pNode;
		else
			return GetLastNode(pNode.GetNextNode());
	}

	public int GetDocumentFrequency(String pWord) {

		InvertedList LastNode;
		InvertedList theTempList = theTreeMapList.GetInvertedList(pWord);
		int frequency = 0;
		if (theTempList == null) {
			System.out.println("Word Not found");
		} else {

			while (theTempList.GetNextNode() != null) {
				System.out.println("Doc ID " + theTempList.GetDocID());
				frequency = frequency + theTempList.GetFrequency();
				theTempList = theTempList.GetNextNode();
			}
			System.out.println("Doc ID " + theTempList.GetDocID());
			frequency = frequency + theTempList.GetFrequency();
		}
		return frequency;

	}

	public ArrayList<Integer> GetPositionForKeyInDoc(String pWord, int pDocNumber) {
		ArrayList<Integer> PositionList = null;
		InvertedList theTempList = theTreeMapList.GetInvertedList(pWord);
		if (theTempList == null) {
			System.out.println("Word Not found");
		} else {
			while (theTempList.GetNextNode() != null) {
				if (theTempList.GetDocID() == pDocNumber) {
					PositionList = theTempList.GetPositionList();

					break;
				}
				theTempList = theTempList.GetNextNode();
			}
			if (theTempList.GetDocID() == pDocNumber) {
				PositionList = theTempList.GetPositionList();

			}
		}
		return PositionList;

	}

	public double GetSimilarity(double[] pFirtstVector, double[] pSecondVector) {
		double theMultiplication = 0.0;
		double mag1 = 0.0;
		double mag2 = 0.0;
		double theSimilarity = 0.0;

		for (int i = 0; i < pFirtstVector.length; i++) {
			theMultiplication += pFirtstVector[i] * pSecondVector[i];
			mag1 += Math.pow(pFirtstVector[i], 2);
			mag2 += Math.pow(pSecondVector[i], 2);
		}
		mag1 = Math.sqrt(mag1);
		if (Document > -1)
			mag1 = DocMagnitude[Document];
		mag2 = Math.sqrt(mag2);
		if (Double.compare(mag1, 0) > 0 && Double.compare(mag2, 0) > 0) {
			theSimilarity = theMultiplication / (mag1 * mag2);
		} else {
			return 0.0;
		}
		return theSimilarity;
	}
}
