<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Result Term Proximity</title>
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
		TreeMap<String, String> TitleMap = (TreeMap<String, String>) application.getAttribute("TitleMap");
		TreeMap<Integer, Double> DocSimilarityMap = (TreeMap<Integer, Double>) application
				.getAttribute("DocSimilarityMap");
		TreeMap<Integer, Double> PositionScore = (TreeMap<Integer, Double>) application
				.getAttribute("PositionScore");
		int i = 0;
		int j = 0;
	%><br><hr><hr><h3>
	Previous Query : <%=Query%>
	</h3>
	<center><h3>Proximity Search Result<br>New Scoring =(Cosine similarity)/(Proximity Score)</h3></center>
		Results:
		<table style="width: 100%">
			<tr>
				<th>Rank</th>
				<th>New Score</th>
				<th>Similarity</th>
				<th>ProximityScore</th>
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
							String SiteTitle = TitleMap.get(DocIDPageMap.get(str));
							String OldScore = String.format("%.8f", DocSimilarityMap.get(Integer.parseInt(str)));
							String PosScore = String.format("%.8f", PositionScore.get(Integer.parseInt(str)));
							
			%>
			<tr>
				<td><%=i%></td>
				<td><%=tempKey%></td>
				<td><%=OldScore%></td>
				<td><%=PosScore%></td>
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