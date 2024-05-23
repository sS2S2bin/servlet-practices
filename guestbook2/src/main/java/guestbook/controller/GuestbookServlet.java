package guestbook.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import guestbook.dao.GuestbookDao;
import guestbook.vo.GuestbookVo;


public class GuestbookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("a");
		if("add".equals(action)) {
			request.setCharacterEncoding("utf-8");
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String contents = request.getParameter("message");
			
			GuestbookVo vo = new GuestbookVo();
			vo.setName(name);
			vo.setPassword(password);
			vo.setContents(contents);
			
			new GuestbookDao().insert(vo);
			
			response.sendRedirect(request.getContextPath()+"/gb");
			
		}else if("delete".equals(action)) {
			String no = request.getParameter("no");
			String password = request.getParameter("password");
			

			new GuestbookDao().deleteByNo(Long.parseLong(no),password);
			response.sendRedirect(request.getContextPath()+"/gb");
			
		}else if ("deleteform".equals(action)){
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/deleteform.jsp");
			rd.forward(request, response);
		} else {
			/*default action (list)*/
			List<GuestbookVo> list = new GuestbookDao().findAll();
			request.setAttribute("list", list);
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/index.jsp");
			rd.forward(request, response);
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
