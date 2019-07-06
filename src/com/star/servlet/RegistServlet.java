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
 * �û�ע���Servlet
 */
@WebServlet("/RegistServlet")
public class RegistServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ���ݽ���
		// �ļ��ϴ�����������
		
		try {
			//����Map�������ڱ�����յ�������
			Map<String,String> map = new HashMap<String,String>();
			//1.����һ�������ļ��������
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			//2.����һ�����Ľ�����
			ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
			//3.����request���󣬷��ص���List���ϣ�List�����д�ŵ���FileItem����
			List<FileItem> list = servletFileUpload.parseRequest(request);
			//����һ��List���ϣ����ڱ�����Ȥ��������
			List<String> hobbyList = new ArrayList<String>();
			//4.�������ϣ����ÿһ��FileItem��ÿһ��������ж��Ǳ�����ļ��ϴ���
			String url = null;
			for(FileItem fileItem : list) {
				//�ж��Ǳ�����ļ��ϴ���
				if(fileItem.isFormField()) {
					//��ͨ����
					//���ձ������ֵ��
					String name = fileItem.getFieldName();  //��ñ����name����ֵ
					String value = fileItem.getString("UTF-8"); //��ñ����valueֵ
//					System.out.println(name+"   "+value);
					//���ո�ѡ�������
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
					//�ļ��ϴ���
					//����ļ���
//					System.out.println("!!!!!!!!!!!!!!");
					String fileName = fileItem.getName();
					//ͨ����������Ψһ�ļ���
					if(fileName!= null) {
						String uuidFileName = UploadUtils.getUUIDFileName(fileName);
						//����ļ��ϴ�����
						InputStream is = fileItem.getInputStream();
						//����ļ��ϴ�·��
						String path = this.getServletContext().getRealPath("/upload");
						//���������Խӵ������
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
			//���ServletContext����
			List<User> userList = (List<User>) this.getServletContext().getAttribute("list");
			//У���û�����
			for(User u: userList) {
				if(u.getUsername().equals(map.get("username"))) {
					request.setAttribute("msg","�û����Ѿ����ڣ�" );
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
			//ע��ɹ�������ת����½ҳ��
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
