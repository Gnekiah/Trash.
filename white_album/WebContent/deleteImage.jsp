<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%String album = (String) request.getAttribute("album");
out.print("<meta http-equiv=refresh content=\"3; url=/white_album/ImageServlet?op=viewAlbum&album=");
out.print(album);
out.println("\">");
%>
<title>照片删除</title>
</head>
<body>
<%boolean result = (boolean) request.getAttribute("deleteImage");
if (result) {
   	out.println("照片删除成功！");
}
else {
    out.println("照片删除失败！");
}
out.println("即将跳转！");
%>
</body>
</html>