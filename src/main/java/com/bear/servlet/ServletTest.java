package com.bear.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletTest extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=utf-8");  
		PrintWriter writer = resp.getWriter();
		
		writer.print("<!DOCTYPE HTML>");
		writer.print("<html>");
		writer.print("<head>");
		writer.print("<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">");
		writer.print("</head>");
		writer.print("<body>");
		writer.print("<h1>Hello World!</h1>");
		writer.print("</body>");
		writer.print("</html>");
		
		writer.flush();
		writer.close();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
