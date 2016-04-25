<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Index</title>
</head>
<body>
<a href="index.jsp"> 返回上一页 </a>
<%
	File file = (File) request.getAttribute("viewAlbum");
	for(File f : file.listFiles())
	{
%>
<pre>
<%
out.print("<img src=\"");
out.print(f.getAbsolutePath());
out.print("\"  width=\"600\" alt=\"");
out.print(f.getName());
out.println("\" />");
%>
<a href="Viewimage?album=<%=request.getParameter("album") %>&name=<%=f.getName()%>" target="_blank"> <%=f.getName() %> </a>
<form action="/shiyan4/Changeimagename?album=<%=request.getParameter("album") %>&name=<%=f.getName()%>" method="post">
    <input type="text" name="fname" />
	<input type="submit" value="rename" /></form>
<a href="Deleteimage?album=<%=request.getParameter("album") %>&name=<%=f.getName()%>"> delete </a>
</pre>
<%
	}
%>
<form action="/shiyan4/Uploadimage?album=<%=request.getParameter("album") %>" method="post" encType="multipart/form-data">
<p>choose<input type="file" name="fname"></p>
<p><input type="submit" value="upload"></p>
</form>	
</body>
</html>