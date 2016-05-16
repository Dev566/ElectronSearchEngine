package electron.com;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class MinDistance {
	TreeMap<Character, String> Wordmap = new TreeMap<Character, String>();

	MinDistance(TreeMap<String, Integer> pQuery) {
		Set<String> keys = pQuery.keySet();
		int i = 65;
		for (String key : keys) {
			Wordmap.put((char) i, key);
			i++;
		}
	}

	public int GetMinDistance(char[] Document) {
		TreeMap<Character, Integer> WordIndex = new TreeMap<Character, Integer>();
		TreeSet<Integer> IndexLocation = new TreeSet<Integer>();
		int FirstIndex = -1;
		int LastIndex = -1;
		int minDistance = -1;
		for (int i = 0; i < Document.length; i++) {
			if (Wordmap.containsKey(Document[i])) {
				if (WordIndex.containsKey(Document[i])) {
					IndexLocation.remove(WordIndex.get(Document[i]));
				}
				WordIndex.put(Document[i], i);
				IndexLocation.add(i);
			}
			if (IndexLocation.size() == Wordmap.size()) {
				int firstIndex = IndexLocation.first();
				int lastIndex = IndexLocation.last();
				int distance = lastIndex - firstIndex + 1;
				if (minDistance == -1) {
					minDistance = distance;
					FirstIndex = firstIndex;
					LastIndex = lastIndex;
				} else if (minDistance > distance) {
					minDistance = distance;
					FirstIndex = firstIndex;
					LastIndex = lastIndex;
				}
			}
		}
		return minDistance;
	}

}
