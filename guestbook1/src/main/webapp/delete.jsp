<%@page import="guestbook.dao.GuestbookDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	request.setCharacterEncoding("utf-8");
	String no = request.getParameter("no");
	String password = request.getParameter("password");
	

	new GuestbookDao().deleteByNo(no,password);
	response.sendRedirect("/guestbook1");
%>

<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
</body>
</html>