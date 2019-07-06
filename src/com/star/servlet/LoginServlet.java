package com.star.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.star.domain.User;



/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//��������
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		//��ServletContext���л�ñ�����û���Ϣ
		List<User> list = (List<User>) this.getServletContext().getAttribute("list");
		for(User user: list) {
			if(username.equals(user.getUsername())) {
				//�û�����ȷ
				if(password.equals(user.getPassword())) {
					//������ȷ
					//��¼�ɹ�
					//�жϸ�ѡ���Ƿ�ѡ
					String remember = request.getParameter("remember");
					if("true".equals(remember)) {
						//��ס�û���
						Cookie cookie = new Cookie("username", user.getUsername());
						//������Ч·��
						cookie.setPath("/reg_login");
						//������Чʱ��
						cookie.setMaxAge(60*60*24); //24Сʱ
						//��cookie��д�������
						response.addCookie(cookie);
					}
					//���û���Ϣ����
					request.getSession().setAttribute("user", user);
					response.sendRedirect(request.getContextPath()+"/success.jsp");
					return;
				}
			}
		}
		//��¼ʧ��
		request.setAttribute("msg", "�û��������������");
		request.getRequestDispatcher("/login.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
