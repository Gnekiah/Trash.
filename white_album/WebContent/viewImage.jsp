<%@ page import="java.io.File" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>查看照片</title>
</head>
<body>
<%File result = (File) request.getAttribute("imgFile");
out.print("<img src=\"");
out.print(result.getAbsolutePath());
out.print("\" alt=\"");
out.print(result.getName());
out.println("\" />");
%>
</body>
</html>