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
		//接收数据
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		//从ServletContext域中获得保存的用户信息
		List<User> list = (List<User>) this.getServletContext().getAttribute("list");
		for(User user: list) {
			if(username.equals(user.getUsername())) {
				//用户名正确
				if(password.equals(user.getPassword())) {
					//密码正确
					//登录成功
					//判断复选框是否勾选
					String remember = request.getParameter("remember");
					if("true".equals(remember)) {
						//记住用户名
						Cookie cookie = new Cookie("username", user.getUsername());
						//设置有效路径
						cookie.setPath("/reg_login");
						//设置有效时间
						cookie.setMaxAge(60*60*24); //24小时
						//将cookie回写到浏览器
						response.addCookie(cookie);
					}
					//将用户信息保存
					request.getSession().setAttribute("user", user);
					response.sendRedirect(request.getContextPath()+"/success.jsp");
					return;
				}
			}
		}
		//登录失败
		request.setAttribute("msg", "用户名或者密码错误");
		request.getRequestDispatcher("/login.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
