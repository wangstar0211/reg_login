package com.star.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.star.domain.User;
/**
 * �û�ע��ĳ�ʼ����Servlet
 */
@WebServlet("/InitServlet")
public class InitServlet extends HttpServlet {
	
	@Override
	public void init() throws ServletException {
		// ����һ��List�������ڱ����û�ע�����Ϣ
		List<User> list = new ArrayList<User>();
		//��list������ServletContext�������У�
		this.getServletContext().setAttribute("list", list);
	}

}
