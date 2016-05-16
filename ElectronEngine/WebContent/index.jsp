<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<form method="post" action="AddDomain" >
Domain Name: <input type="text" name="DomainName" style="width:500px">  Deapth: <input type="text" name="Deapth" style="width:33px" value=2> 
<input type="submit" value="Add Domain"><br> Pls enter the absolute domain link. By Default the Deapth is 2.
</form> 
<br><br>
<center>
<form method="post" action="SearchEngine" autocomplete="on">
  Enter Query: <input type="text" name="Query" style="width:300px" > <input type="submit" value="Submit">
</form> 
</center>

</body>
</html>