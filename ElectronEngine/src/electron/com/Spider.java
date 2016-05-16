package electron.com;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Spider {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	private static final int MAX_PAGES_TO_SEARCH = 100;
	private static int MAX_Deapth = 2;

	private Set<String> pagesVisited = new HashSet<String>();

	static int filecount = 1;
	static TreeMap<String, String> DocumentLinkMap;
	static TreeMap<String, String> VisitedBySpider;
	static TreeMap<String, String> TitleMap;
	String DirLoc = "F:/IR/CrawledPages/";

	Spider() {
		VisitedBySpiderMap();
		InitializeDocMap();
		InitializeTitleMap();
	}

	void setDeapth(int pDeapth) {
		MAX_Deapth = pDeapth;
	}

	private void VisitedBySpiderMap() {
		File SpiderDatFile = new File("F:/IR/dat/SpiderPageVisited.dat");
		if (SpiderDatFile.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(SpiderDatFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);
				VisitedBySpider = (TreeMap) ois.readObject();
				filecount = VisitedBySpider.size();
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
			VisitedBySpider = new TreeMap<String, String>();
		}
	}

	private void InitializeDocMap() {
		File DocLinkDatFile = new File("F:/IR/dat/DocumentLinkMap.dat");
		if (DocLinkDatFile.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(DocLinkDatFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);
				DocumentLinkMap = (TreeMap) ois.readObject();
				ois.close();
				filecount = DocumentLinkMap.size();
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
			DocumentLinkMap = new TreeMap<String, String>();
		}

	}

	private void InitializeTitleMap() {
		File TitleMapFile = new File("F:/IR/dat/TitleMap.dat");
		if (TitleMapFile.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(TitleMapFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ObjectInputStream ois = new ObjectInputStream(bis);
				TitleMap = (TreeMap) ois.readObject();
				ois.close();
				filecount = DocumentLinkMap.size();
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

	}

	private void IncrementFilecount() {
		filecount++;
	}

	public String GetOriginalLink(String pFileLocal) {
		return DocumentLinkMap.get(pFileLocal);
	}

	public Elements getLinks(String url) throws IOException {
		Document URLDocument = null;
		Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
		URLDocument = connection.timeout(60 * 1000).get();
		String URLtitle = URLDocument.title();
		// System.out.println(filecount+URLtitle);
		String URLbody = URLDocument.body().text();
		VisitedBySpider.put(url.toString(), "Cached" + filecount + ".txt");
		DocumentLinkMap.put("Cached" + filecount + ".txt", url.toString());
		String file = DirLoc.concat("Cached" + filecount).concat(".txt");
		IncrementFilecount();
		PrintWriter write = new PrintWriter(file);
		write.println(URLtitle + " \n" + URLbody);
		write.close();
		TitleMap.put("Cached" + filecount + ".txt", URLtitle);
		Elements URLlinks = URLDocument.select("a[href]");
		System.out.println((filecount - 1) + " " + URLtitle + "  " + URLlinks.size() + "  " + url.toString());
		return URLlinks;

	}

	public Boolean visitPage(String url, int pDeapth) {
		boolean result = true;
		if (pDeapth < MAX_Deapth && !VisitedBySpider.containsKey(url)) {
			Document URLDocument = null;

			try {
				if (filecount % 2000 == 0) {
					SearilizeVisitedMap();
					SearilizeTitleMap();
					SearilizeDocLinkMap();
					FileAction FileDirectory = new FileAction("F:/IR1/CrawledPages/", "F:/IR/CrawledPages/");
					FileDirectory.MoveDirectoryContent();
					System.gc();

				}
				Elements URLlinks = getLinks(url);
				if (URLlinks.size() > 0 && (pDeapth + 1 < MAX_Deapth)) {
					for (Element link : URLlinks) {
						String subLink = link.absUrl("href");
						if (VisitedBySpider.containsKey(subLink)) {

							//
						} else {
							if (isURL(subLink)) {
								System.out.println(subLink);
								// Connection connection2 =
								// Jsoup.connect(subLink).userAgent(USER_AGENT);
								// Document URLDocument2 =
								// connection2.timeout(60*1000).get();
								// Elements URLlinks2 =
								// URLDocument2.select("a[href]");
								// if (URLlinks2.size() > 0)
								result = visitPage(subLink, pDeapth + 1);

							}
						}
					}
				}
				return result;
			} catch (UnsupportedMimeTypeException e) {
				return false;
			} catch (HttpStatusException e) {
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

		}
		return result;
	}

	public static boolean isURL(String url) {
		if (url == null) {
			return false;
		}
		// Assigning the url format regular expression
		String urlPattern = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
		return url.matches(urlPattern);
	}

	public static void SearilizeVisitedMap() {
		try {
			FileOutputStream fileOut = new FileOutputStream("F:/IR/dat/SpiderPageVisited.dat");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			if (VisitedBySpider != null) {
				out.writeObject(VisitedBySpider);
				out.close();
				fileOut.close();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void SearilizeDocLinkMap() {
		try {
			FileOutputStream fileOut = new FileOutputStream("F:/IR/dat/DocumentLinkMap.dat");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			if (DocumentLinkMap != null) {
				out.writeObject(DocumentLinkMap);
				out.close();
				fileOut.close();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void SearilizeTitleMap() {
		try {
			FileOutputStream fileOut = new FileOutputStream("F:/IR/dat/TitleMap.dat");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			if (TitleMap != null) {
				out.writeObject(TitleMap);
				out.close();
				fileOut.close();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void CrawlURL(String pURL, int pDeapth) {
		if (pDeapth > 0) {
			MAX_Deapth = pDeapth;
		}
		if (visitPage(pURL, 0)) {
			SearilizeVisitedMap();
			SearilizeDocLinkMap();
			SearilizeTitleMap();
		}
	}

	public static void main(String[] args) throws MalformedURLException {
		Spider spider = new Spider();
		if (spider.visitPage("https://www.ku.edu/", 0)) {
			SearilizeVisitedMap();
			SearilizeDocLinkMap();
			SearilizeTitleMap();
		}
	}
}