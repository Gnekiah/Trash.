<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>main</title>
</head>
<body>
<%
	File dir=new File("D:/shiyan4/");
	if (!dir.exists()) {
	    dir.mkdir();
	}
	for(File file:dir.listFiles())
	{
%>
<pre>
<%
if (file.list().length > 0) {
    File[] fi = file.listFiles();
	out.print("<img src=\"");
	out.print(fi[0].getAbsolutePath());
	out.print("\"  width=\"600\"");
	out.println("\" />");
}
%>
<a href="Viewalbum?album=<%=file.getName()%>"> <%=file.getName() %> </a>
<form action="/shiyan4/Changealbumname?name=<%=file.getName()%>" method="post">
    <input type="text" name="fname" />
	<input type="submit" value="rename" /></form>
<a href="Deletealbum?name=<%=file.getName()%>"> delete </a>
</pre>
<%
	}
%>
<form action="/shiyan4/Addalbum" method="post">
    <input type="text" name="fname" />
	<input type="submit" value="new" /></form>
</body>
</html>