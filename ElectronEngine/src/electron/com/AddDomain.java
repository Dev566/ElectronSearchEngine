package electron.com;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddDomain
 */
@WebServlet("/AddDomain")
public class AddDomain extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddDomain() {
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
		String message = request.getParameter("DomainName");
		String Deapth = request.getParameter("Deapth");
		Spider spider = new Spider();

		spider.CrawlURL(message, Integer.parseInt(Deapth));
		CrawledPageProcessor PageProcessor = new CrawledPageProcessor();
		PageProcessor.StartProcessing();
		response.sendRedirect("index.jsp");
	}

}
