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

@WebServlet("/getFlightStatus")
public class GetFlightStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		String departureDate =request.getParameter("departureDate");
		String startDate = departureDate+" 00:00:00";
		String endDate = departureDate+" 23:59:00";
		int startPage = 0;
		try {
			startPage = Integer.parseInt(request.getParameter("startPage").toString());
		} catch (Exception e) {
			startPage = 0;
		}
		int pageSize = 0;
		try {
			pageSize = Integer.parseInt(request.getParameter("pageSize").toString());
		} catch (Exception e) {
			pageSize = 0;
		}
		Result result = ScheduleService.getFlightStatus(startDate, endDate, startPage, pageSize);
		String json = JSON.toJSON(result).toString();
		response.getWriter().append(json);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
