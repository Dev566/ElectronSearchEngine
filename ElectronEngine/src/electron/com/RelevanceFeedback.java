package electron.com;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RelevanceFeedback
 */
@WebServlet("/RelevanceFeedback")
public class RelevanceFeedback extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ArrayList<Integer> RelevantDocument = new ArrayList<Integer>();
	ArrayList<Integer> NonRelevantDocument = new ArrayList<Integer>();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RelevanceFeedback() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public void GetRelevantVector(){
		for(int i=0;i<RelevantDocument.size();i++){
			
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		ServletContext context = request.getServletContext();
		String Query = (String) context.getAttribute("Query");
		NavigableMap FinalResult = (NavigableMap) context.getAttribute("FinalResult");
		NavigableMap ResultSimilarity = (NavigableMap) context.getAttribute("ResultSimilarity");
		TreeMap<String, String> DocIDPageMap = (TreeMap<String, String>) context.getAttribute("DocIDPageMap");
		TreeMap<String, String> DocumentLinkMap = (TreeMap<String, String>) context.getAttribute("DocumentLinkMap");

		int i = 0;
		if (ResultSimilarity != null) {
			Set<String> ResultKey = FinalResult.keySet();
			ArrayList<String> tempArray1;
			for (String tempKey : ResultKey) {

				tempArray1 = (ArrayList<String>) FinalResult.get(tempKey);
				String SiteLink;
				for (String str : tempArray1) {
					String message = request.getParameter("Relevance" + i++);
					if (message != null) {
						System.out.println(message + "  Relevance " + i + "  " + str);
						if (message.equals("Relevant")) {
							RelevantDocument.add(Integer.parseInt(str));
						} else if (message.equals("NonRelevant")) {
							NonRelevantDocument.add(Integer.parseInt(str));
						}
					}
				}
			}
			PrintWriter myPrint = response.getWriter();
			myPrint.println(RelevantDocument.size());
			myPrint.println(NonRelevantDocument.size());
			QueryProcess NewQuery = new QueryProcess();
			NewQuery.hash=(TreeMap<String, Integer>) context.getAttribute("QueryHash");
			NewQuery.QueryVector =(double[]) context.getAttribute("QueryVector");
			NewQuery.RelevanceFeedBack(RelevantDocument,NonRelevantDocument);
			ServletContext curServletContext = request.getServletContext();
			curServletContext.setAttribute("FinalResult", NewQuery.FinalResult);
			curServletContext.setAttribute("ResultSimilarity", NewQuery.ResultSimilarity);
			response.sendRedirect("RelevanceResult.jsp");
		}
	}

}
