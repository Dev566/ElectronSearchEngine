package electron.com;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TermProximitySearch
 */
@WebServlet("/TermProximitySearch")
public class TermProximitySearch extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TermProximitySearch() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		NavigableMap ResultSimilarity = null;
		NavigableMap FinalResult = null;
		TreeMap<String, ArrayList<String>> Similarity = new TreeMap<String, ArrayList<String>>();
		PrintWriter myPrint = response.getWriter();
		myPrint.println("<BR>" + "<H2>Original Query : </H2>");
		ServletContext curServletContext = request.getServletContext();
		TreeMap<String, Integer> hash = (TreeMap<String, Integer>) curServletContext.getAttribute("QueryHash");
		TermProximity myTermProximity = new TermProximity();
		myTermProximity.StartTermProximity(hash);
		TreeMap<Integer, Double> DocSimilarityMap = (TreeMap<Integer, Double>) curServletContext
				.getAttribute("DocSimilarityMap");
		if (myTermProximity.PositionScore != null) {
			for (int i = 0; i < DocSimilarityMap.size(); i++) {
				double newScore = 0;
				if (Double.compare(DocSimilarityMap.get(i), 0.0) > 0
						&& Double.compare(myTermProximity.PositionScore.get(i), 0.0) > 0) {
					newScore = (double) DocSimilarityMap.get(i) / (double) myTermProximity.PositionScore.get(i);
				}
				ArrayList tempArray;
				String sNewScore = String.format("%.8f", newScore);
				tempArray = Similarity.get(sNewScore);
				if (tempArray == null) {
					tempArray = new ArrayList();
					tempArray.add(Integer.toString(i));
					Similarity.put(sNewScore, tempArray);
				} else {
					tempArray.add(Integer.toString(i));
				}
			}

			FinalResult = Similarity.descendingMap();
			curServletContext.setAttribute("FinalResult", FinalResult);
			curServletContext.setAttribute("PositionScore", myTermProximity.PositionScore);
			response.sendRedirect("ResultTermProximity.jsp");
		} else {
			myPrint.println("<BR>" + "<H2>Something went wrong</H2>");
		}
	}

}
