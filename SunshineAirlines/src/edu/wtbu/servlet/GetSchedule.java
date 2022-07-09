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

@WebServlet("/getSchedule")
public class GetSchedule extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		String fromCity =request.getParameter("fromCity");
		String toCity =request.getParameter("toCity");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Result result = ScheduleService.getSchedule(fromCity, toCity, startDate, endDate);
		String json = JSON.toJSON(result).toString();
		response.getWriter().append(json);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
