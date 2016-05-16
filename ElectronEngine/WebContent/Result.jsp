<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search Result</title>
<style>
table, th, td {
	border: 1px solid black;
	border-collapse: collapse;
}

th, td {
	padding: 5px;
	text-align: left;
}
</style>
</head>
<body>
	<%@ include file="/index.jsp"%>
	<%
		String Query = (String) application.getAttribute("Query");
		NavigableMap FinalResult = (NavigableMap) application.getAttribute("FinalResult");
		NavigableMap ResultSimilarity = (NavigableMap) application.getAttribute("ResultSimilarity");
		TreeMap<String, String> DocIDPageMap = (TreeMap<String, String>) application.getAttribute("DocIDPageMap");
		TreeMap<String, String> DocumentLinkMap = (TreeMap<String, String>) application
				.getAttribute("DocumentLinkMap");
		TreeMap<String, String> TitleMap = (TreeMap<String, String>) application
				.getAttribute("TitleMap");
		int i = 0;
		int j = 0;
	%>
	<center><h3>Original Query :
	<%=Query%></h3></center><form method="post" action="TermProximitySearch" id="TermProximitySearch">
		<input type="submit" value="Proximity Search" style="float: right">
		</form>
	<form method="post" action="RelevanceFeedback" id="Relevance">
		<input type="submit" value="Relevance FeedBack" style="float: right"><br>
		Results:
		<table style="width: 100%">
			<tr>
				<th>Rank</th>
				<th>Feedback</th>
				<th>Similarity</th>
				<th>Title</th>
				<th style="width: 622px">Link</th>
			</tr>
			<%
				if (ResultSimilarity != null) {
					Set<String> ResultKey = FinalResult.keySet();
					ArrayList<String> tempArray1;
					for (String tempKey : ResultKey) {
						i += 1;
						tempArray1 = (ArrayList<String>) FinalResult.get(tempKey);
						String SiteLink;
						for (String str : tempArray1) {
							SiteLink = DocumentLinkMap.get(DocIDPageMap.get(str));
							String SiteTitle=TitleMap.get(DocIDPageMap.get(str));
			%>
			<tr>
				<td><%=i%></td>
				<td><select name="Relevance<%=j++%>" form="Relevance">
						<option value="None">Select</option>
						<option value="Relevant">Relevant</option>
						<option value="NonRelevant">Non Relevant</option>

				</select></td>
				<td><%=tempKey%></td>
				<td><%=SiteTitle%></td>
				<td><a href=<%=SiteLink%>> <%=SiteLink%>
				</a></td>
			</tr>
			<%
				}
					}
				}
			%>
		</table>
	</form>
</body>
</html>