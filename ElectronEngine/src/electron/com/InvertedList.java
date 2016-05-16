package electron.com;

import java.util.ArrayList;

public class InvertedList implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9001206771412940954L;
	private int DocumentID;
	private int Frequency;
	private ArrayList<Integer> Position = new ArrayList<Integer>();
	private int PositionCount = 0;
	private InvertedList NxtList;

	public void AddPosition(int pPosition) {
		//System.out.println(PositionCount + " "+ Frequency);
		//Position[PositionCount++] = pPosition;
		Position.add(pPosition);
		
	}

	public InvertedList(int pDocID, int pFrequency, int pPosition) {
		DocumentID = pDocID;
		Frequency = pFrequency;
		AddPosition(pPosition);
		NxtList = null;
	}

	public int GetDocID() {

		return DocumentID;
	}
	public ArrayList<Integer> GetPositionList() {

		return Position;
	}
	public InvertedList GetNextNode() {

		return this.NxtList;

	}

	public int GetFrequency() {
		return this.Frequency;
	}

	public void AddFrequency(int pPosition) {

		Frequency++;
		AddPosition(pPosition);
	}

	public void AddNxtNode(InvertedList pNxtNode) {
		this.NxtList = pNxtNode;
	}
}
