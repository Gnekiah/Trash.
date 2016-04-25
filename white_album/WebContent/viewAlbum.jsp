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
<a href="index.jsp"> 主页 </a>
<p>所有相册</p>
<%
	File file = (File) request.getAttribute("viewAlbum");
	for(File f : file.listFiles())
	{
%>
<pre>
<%
out.print("<img src=\"");
out.print(f.getAbsolutePath());
out.print("\"  width=\"350\" alt=\"");
out.print(f.getName());
out.println("\" />");
%>
<a href="ImageServlet?op=viewImage&album=<%=request.getParameter("album") %>&name=<%=f.getName()%>" target="_blank"> <%=f.getName() %> </a>
<form action="/white_album/ImageServlet?op=modifyImage&album=<%=request.getParameter("album") %>&name=<%=f.getName()%>" method="post">
    <input type="text" name="fname" />
	<input type="submit" value="重命名" /></form>
<a href="ImageServlet?op=deleteImage&album=<%=request.getParameter("album") %>&name=<%=f.getName()%>"> 删除 </a>
</pre>
<%
	}
%>
<form action="/white_album/ImageServlet?op=upload&album=<%=request.getParameter("album") %>" method="post" encType="multipart/form-data">
<p>选择图片：<input type="file" name="fname"></p>
<p><input type="submit" value="上传"></p>
</form>	
</body>
</html>