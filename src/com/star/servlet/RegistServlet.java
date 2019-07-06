package com.star.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.star.domain.User;
import com.star.utils.UploadUtils;

/**
 * 用户注册的Servlet
 */
@WebServlet("/RegistServlet")
public class RegistServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 数据接收
		// 文件上传基本操作：
		
		try {
			//定义Map集合用于保存接收到的数据
			Map<String,String> map = new HashMap<String,String>();
			//1.创建一个磁盘文件项工厂对象
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			//2.创建一个核心解析类
			ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
			//3.解析request请求，返回的是List集合，List集合中存放的是FileItem对象
			List<FileItem> list = servletFileUpload.parseRequest(request);
			//定义一个List集合，用于保存兴趣爱好数据
			List<String> hobbyList = new ArrayList<String>();
			//4.遍历集合，获得每一个FileItem（每一个表单项），判断是表单项还是文件上传项
			String url = null;
			for(FileItem fileItem : list) {
				//判断是表单项还是文件上传项
				if(fileItem.isFormField()) {
					//普通表单项
					//接收表单项参数值：
					String name = fileItem.getFieldName();  //获得表单项的name属性值
					String value = fileItem.getString("UTF-8"); //获得表单项的value值
//					System.out.println(name+"   "+value);
					//接收复选框的数据
					if("hobby".equals(name)) {
						String hobbyValue = fileItem.getString("UTF-8");
						hobbyList.add(hobbyValue);
						hobbyValue = hobbyList.toString().substring(1, hobbyList.toString().length()-1);
//						System.out.println(name+"   "+hobbyValue);
						map.put(name, hobbyValue);
					}else {
						map.put(name, value);
					}
					
				}else {
					//文件上传项
					//获得文件名
//					System.out.println("!!!!!!!!!!!!!!");
					String fileName = fileItem.getName();
					//通过工具类获得唯一文件名
					if(fileName!= null) {
						String uuidFileName = UploadUtils.getUUIDFileName(fileName);
						//获得文件上传数据
						InputStream is = fileItem.getInputStream();
						//获得文件上传路径
						String path = this.getServletContext().getRealPath("/upload");
						//将输入流对接到输出流
						url = path + "\\" + uuidFileName;
						
						OutputStream os = new FileOutputStream(url);
						int len =0;
						byte[] b = new byte[1024];
						while((len = is.read(b))!=-1) {
							os.write(b,0,len);
						}
						is.close();
						os.close();
					}
					
							
				}
			}
			System.out.println(map);
			//获得ServletContext对象
			List<User> userList = (List<User>) this.getServletContext().getAttribute("list");
			//校验用户名：
			for(User u: userList) {
				if(u.getUsername().equals(map.get("username"))) {
					request.setAttribute("msg","用户名已经存在！" );
					request.getRequestDispatcher("/regist.jsp").forward(request,response);
					return;
				}
			}
			User user = new User();
			user.setUsername(map.get("username"));
			user.setPassword(map.get("password"));
			user.setSex(map.get("sex"));
			user.setNickname(map.get("nickname"));
			user.setHobby(map.get("hobby"));
			user.setPath(url);
			
			
			userList.add(user);
			for (User u : userList) {
				System.out.println(u);
			}
			this.getServletContext().setAttribute("list", userList);
			//注册成功，就跳转到登陆页面
			request.getSession().setAttribute("username", user.getUsername());
			response.sendRedirect(request.getContextPath()+"/login.jsp");
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
