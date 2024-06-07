package jstlel;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class _02Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* 1. 객체의 Scope (변수 아님!!)
		 * 	  객체의 존재 범위 
		 * 
		 * 2. 객체가 오래 존속하는 순서
		 *     Application(tomcat에선 Context) Scope > Session Scope > Request Scope > Page Scope
		 * 
		 * 3. EL ${vo.name } el이 이름으로 객체를 찾는 순서
		 *    Page Scope > Request Scope > Session Scope > Application(tomcat에선 Context) Scope
		 *    그러므로 [주의] 같은 이름으로 여러 범위 객체를 저장하는 경우 주의가 필요
		 * 
		 * */
		
		
		//application scope
		UserVo vo1 = new UserVo();
		vo1.setNo(1L);
		vo1.setName("둘리1");
		request.getServletContext().setAttribute("vo",vo1);
	
		//session scope
		UserVo vo2 = new UserVo();
		vo2.setNo(2L);
		vo2.setName("둘리2");
		request.getSession(true).setAttribute("vo", vo2);
		
		//request Scope
		UserVo vo3 = new UserVo();
		vo3.setNo(3L);
		vo3.setName("둘리3");
		request.setAttribute("vo", vo3);

		
		
		request.getRequestDispatcher("/WEB-INF/views/02.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
