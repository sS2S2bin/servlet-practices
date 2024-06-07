<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>값 출력</h1>
	${ival }
	${bval }
	${sval }
	${lval }
	${fval }
	
	<h4>객체 출력</h4>
	--${obj }--<br>
	${userVo.no }<br>
	${iserVo.name }<br>
	
	<h4>Map 출력</h4>
	${m.ival }<br>
	${m.fval }<br>
	${m.sval }<br>
	
	<h4>산술연산</h4>
	${3*4+6/2 }<br>
	${ival+10 }<br>
	
	<h4>관계연산</h4>
	${ival==10 }<br>
	${obj==null }<br>
	${empty obj }<br>
	
	<if test='${ival==10 }'>
	 	<h4>test가 통과면 이걸 보여줘</h4>
	 </if>
	 
	 <h4>요청 파라미터</h4>
	 --${param.no + 10 }-- <br>
	 --${param.name }-- <br>
	 
	 
	 <h4>Context Page</h4>
	 ${pageContext.request.contextPath }
</body>
</html>