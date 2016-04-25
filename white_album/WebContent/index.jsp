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
<p>所有相册</p>
<%
	File dir=new File("D:/whitealbum");
	for(File file:dir.listFiles())
	{
%>
<pre>
<%
if (file.list().length > 0) {
    File[] fi = file.listFiles();
	out.print("<img src=\"");
	out.print(fi[0].getAbsolutePath());
	out.print("\"  width=\"350\"");
	out.println("\" />");
}
%>
<a href="ImageServlet?op=viewAlbum&album=<%=file.getName()%>"> <%=file.getName() %> </a>
<form action="/white_album/ImageServlet?op=modifyAlbum&name=<%=file.getName()%>" method="post">
    <input type="text" name="fname" />
	<input type="submit" value="重命名" /></form>
<a href="ImageServlet?op=deleteAlbum&name=<%=file.getName()%>"> 删除 </a>
</pre>
<%
	}
%>
<form action="/white_album/ImageServlet?op=addAlbum" method="post">
    <input type="text" name="fname" />
	<input type="submit" value="新建相册" /></form>
</body>
</html>