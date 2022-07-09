package edu.wtbu.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import edu.wtbu.pojo.Result;
import edu.wtbu.service.ScheduleService;

@WebServlet("/updateSchedule")
public class UpdateSchedule extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		int scheduleId = 0;
		try {
			scheduleId = Integer.parseInt(request.getParameter("scheduleId").toString());
		} catch (Exception e) {
			scheduleId = 0;
		}
		String status = request.getParameter("status");
		Result result = ScheduleService.updateSchdule(scheduleId, status);
		String json = JSON.toJSON(result).toString();
		response.getWriter().append(json);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
