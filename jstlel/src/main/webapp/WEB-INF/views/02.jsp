<%@page import="jstlel.UserVo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	// page scope 하려면 자바코드를 써야해
	UserVo vo4 = new UserVo();
	vo4.setNo(4L);
	vo4.setName("둘리4");
	pageContext.setAttribute("vo", vo4);

%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h4>scope(객체의 존속 범위)</h4>
	${vo.no }<br>
	${vo.name }<br>
	
	
	<h4>====request scope=======</h4>
	${requestScope.vo.no }<br>
	${requestScope.vo.name }<br>
	
	<h4>====session scope=======</h4>
	${sessionScope.vo.no }<br>
	${sessionScope.vo.name }<br>
	
	<h4>====application scope=======</h4>
	${applicationScope.vo.no }<br>
	${applicationScope.vo.name }<br>
	
</body>
</html>