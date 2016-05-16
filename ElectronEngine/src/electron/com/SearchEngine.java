package electron.com;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SearchEngine
 */
@WebServlet("/SearchEngine")
public class SearchEngine extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchEngine() {
		super();
		// TODO Auto-generated constructor stub
	}

	private void ProcessQuery(String pmessage, HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		QueryProcess NewQuery = new QueryProcess();
		NewQuery.ProcessQuery(pmessage);

		ServletContext curServletContext = request.getServletContext();
		curServletContext.setAttribute("Query", pmessage);
		curServletContext.setAttribute("QueryHash", NewQuery.hash);
		curServletContext.setAttribute("ResultObject", NewQuery);
		curServletContext.setAttribute("FinalResult", NewQuery.FinalResult);
		curServletContext.setAttribute("ResultSimilarity", NewQuery.ResultSimilarity);
		curServletContext.setAttribute("DocIDPageMap", NewQuery.DocIDPageMap);
		curServletContext.setAttribute("DocumentLinkMap", NewQuery.curSpider.DocumentLinkMap);
		curServletContext.setAttribute("QueryVector", NewQuery.QueryVector);
		curServletContext.setAttribute("DocSimilarityMap", NewQuery.DocSimilarityMap);
		curServletContext.setAttribute("TitleMap", NewQuery.DesearalizeTitleMap());
		response.sendRedirect("Result.jsp");
		// if (NewQuery.ResultSimilarity != null) {
		// Set<String> ResultKey = NewQuery.FinalResult.keySet();
		// ArrayList<String> tempArray1;
		// int i = 0;
		// for (String tempKey : ResultKey) {
		// i += 1;
		// System.out.println("Similar : " + tempKey);
		// tempArray1 = (ArrayList<String>) NewQuery.FinalResult.get(tempKey);
		// String SiteLink;
		// for (String str : tempArray1) {
		// SiteLink = NewQuery.GetLinkMaping(str);
		// response.getWriter().append(
		// " <BR><h4>Rank : " + i + ". Doc Name : " + SiteLink + ". Similarity :
		// " + tempKey + " </h4>");
		//
		// System.out.println("Link Map : " + SiteLink);
		// if (NewQuery.curSpider != null) {
		// SiteLink = NewQuery.curSpider.GetOriginalLink(SiteLink);
		// response.getWriter().append("Link : <a href=" + SiteLink + "> " +
		// SiteLink + " </a> ");
		// }
		//
		// }
		// //System.out.println("Spider : " + NewQuery.spider.DocumentLinkMap);
		// }
		// }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter myPrint = response.getWriter();
		response.getWriter().append("<H1><center>Welcome </H1></marquee>");
		// myPrint.println("\n" + "Response from Server....");
		String message = request.getParameter("Query");
		String Domain = request.getParameter("DomainName");
		myPrint.println("<BR>" + "<H2>Original Query : " + message + "</H2>");
		ProcessQuery(message, response, request);
		// try {
		// //ProcessQuery(request, response);
		// } catch (ClassNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

}
