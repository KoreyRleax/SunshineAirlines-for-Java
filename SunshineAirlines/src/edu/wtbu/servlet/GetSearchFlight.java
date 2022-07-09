package edu.wtbu.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import edu.wtbu.pojo.Result;
import edu.wtbu.service.ScheduleService;

@WebServlet("/getSearchFlight")
public class GetSearchFlight extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		String fromCity = request.getParameter("fromCity");
		String toCity = request.getParameter("toCity");
		String departureDate = request.getParameter("departureDate");
		String startDate = departureDate + " 00:00:00";
		String endDate = departureDate + " 23:59:59";
		int cabinTypeId = Integer.parseInt(request.getParameter("cabinTypeId").toString());
		String flightType = request.getParameter("flightType");
		Result result = ScheduleService.getSearchFlight(fromCity, toCity, startDate, endDate, cabinTypeId, flightType);
		response.getWriter().append(JSON.toJSONString(result, SerializerFeature.WriteDateUseDateFormat));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
