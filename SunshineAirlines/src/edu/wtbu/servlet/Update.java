package edu.wtbu.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import edu.wtbu.pojo.Result;
import edu.wtbu.service.UserService;

@WebServlet("/updateUser")
public class Update extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		int userId = 0;
		try {
			userId = Integer.parseInt(request.getParameter("userId").toString());
		} catch (Exception e) {
			userId = 0;
		}
		int roleId = 0;
		try {
			roleId = Integer.parseInt(request.getParameter("roleId").toString());
		} catch (Exception e) {
			roleId = 0;
		}
		String email = request.getParameter("email");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String gender = request.getParameter("gender");
		String dateOfBirth = request.getParameter("dateOfBirth");
		String phone = request.getParameter("phone");
		String photo = request.getParameter("photo");
		String address = request.getParameter("address");
		HashMap<String,Object> map = new HashMap<String,Object>();	
		map.put("Email",email);
		map.put("FirstName",firstName);
		map.put("LastName",lastName);
		map.put("Gender",gender);
		map.put("DateOfBirth",dateOfBirth);
		map.put("Phone",phone);
		map.put("Photo",photo);
		map.put("Address",address);
		map.put("RoleId", roleId);
		map.put("UserId", userId);
		Result result = UserService.updateUser(map);
		String json = JSON.toJSON(result).toString();
		response.getWriter().append(json);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
