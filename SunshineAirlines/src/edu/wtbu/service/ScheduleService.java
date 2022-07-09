package edu.wtbu.service;

import java.util.HashMap;
import java.util.List;

import edu.wtbu.dao.ScheduleDao;
import edu.wtbu.pojo.Page;
import edu.wtbu.pojo.Result;

public class ScheduleService {
	public static Result updateSchdule(int scheduleId, String status) {
		Result rs = new Result("fail", null, null);
		HashMap<String, Object> map = ScheduleDao.findByScheduleId(scheduleId);
		if (map == null) {
			rs.setData("航班计划不存在");
			return rs;
		}
		int updateResult = ScheduleDao.updateSchedule(scheduleId, status);
		if (updateResult >= 0) {
			rs.setFlag("success");
		}
		return rs;
	}

	public static Result getFlightStatus(String startDate, String endDate, int startPage, int pageSize) {
		Result rs = new Result("success", null, null);
		int total = ScheduleDao.getFightStatusTotal(startDate, endDate);
		Page page = new Page(total, startPage, pageSize);
		List<HashMap<String, Object>> list = ScheduleDao.getFlightStatus(startDate, endDate, startPage, pageSize);
		rs.setData(list);
		rs.setPage(page);
		return rs;
	}

	public static Result getSchedule(String fromCity, String toCity, String startDate, String endDate) {
		Result rs = new Result("success", null, null);
		List<HashMap<String, Object>> list = ScheduleDao.getSchedule(fromCity, toCity, startDate, endDate);
		rs.setData(list);
		return rs;
	}

	public static Result getScheduleDetail(int scheduleId) {
		Result rs = new Result("fail", null, null);
		HashMap<String, Object> scheduleInfo = ScheduleDao.findByScheduleId(scheduleId);
		if (scheduleInfo == null) {
			rs.setData("航班计划不存在");
		}
		List<HashMap<String, Object>> ticketInfoList = ScheduleDao.findTicketInfoList(scheduleId);
		List<HashMap<String, Object>> selectedSeatList = ScheduleDao.findSelectedSeatList(scheduleId);
		int aircraftId = Integer.parseInt(scheduleInfo.get("AircraftId").toString());
		List<HashMap<String, Object>> seatLayoutList = ScheduleDao.findSeatLayoutList(aircraftId);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ScheduleInfo", scheduleInfo);
		map.put("TicketInfoList", ticketInfoList);
		map.put("SelectedSeatList", selectedSeatList);
		map.put("SeatLayoutList", seatLayoutList);
		rs.setFlag("success");
		rs.setData(map);
		return rs;
	}

	public static Result getSearchFlight(String fromCity, String toCity, String startDate, String endDate,
			int cabinTypeId, String flightType) {
		Result rs = new Result("success", null, null);
		if ("Non-stop".equals(flightType)) {
			List<HashMap<String, Object>> list = getNonStop(fromCity, toCity, startDate, endDate, cabinTypeId);
			rs.setData(list);
		} else if ("1-stop".equals(flightType)) { // 一次中转
			List<HashMap<String, Object>> list = getOneStop(fromCity, toCity, startDate, endDate, cabinTypeId);
			rs.setData(list);
		} else {
			HashMap<String, Object> map = new HashMap<String, Object>();
			List<HashMap<String, Object>> list1 = getNonStop(fromCity, toCity, startDate, endDate, cabinTypeId);
			List<HashMap<String, Object>> list2 = getOneStop(fromCity, toCity, startDate, endDate, cabinTypeId);
			map.put("NonStopList", list1);
			map.put("OneStopList", list2);
			map.put("FlightType", "All");
			rs.setData(map);
		}
		return rs;
	}

	public static List<HashMap<String, Object>> getNonStop(String fromCity, String toCity, String startDate,
			String endDate, int cabinTypeId) {
		List<HashMap<String, Object>> list = ScheduleDao.getNonStopList(fromCity, toCity, startDate, endDate);
		for (int i = 0; i < list.size(); i++) {
			HashMap<String, Object> map = list.get(i);
			int scheduleId = Integer.parseInt(map.get("ScheduleId").toString());
			int residueTickets = 0;
			int reServationTickets = ScheduleDao.getReservationTickets(scheduleId, cabinTypeId);
			if (cabinTypeId == 1) {
				residueTickets = Integer.parseInt(map.get("EconomySeatsAmount").toString()) - reServationTickets;
			} else if (cabinTypeId == 2) {
				residueTickets = Integer.parseInt(map.get("BusinessSeatsAmount").toString()) - reServationTickets;
			} else if (cabinTypeId == 3) {
				residueTickets = Integer.parseInt(map.get("FirstSeatsAmount").toString()) - reServationTickets;
			}
			map.put("ResidueTickets", residueTickets);
			map.put("FlightType", "Non-stop");
		}
		return list;
	}

	public static List<HashMap<String, Object>> getOneStop(String fromCity, String toCity, String startDate,
			String endDate, int cabinTypeId) {
		List<HashMap<String, Object>> list = ScheduleDao.getSearchFlightOneStop(startDate, endDate, fromCity, toCity);
		List<HashMap<String, Object>> delayList = ScheduleDao.getDelayInfoList(startDate);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				HashMap<String, Object> map = list.get(i);
				int S1ScheduleId = Integer.parseInt(map.get("S1ScheduleId").toString());
				int S2ScheduleId = Integer.parseInt(map.get("S2ScheduleId").toString());
				int S1FlightNumber = Integer.parseInt(map.get("S1FlightNumber").toString());
				int S2FlightNumber = Integer.parseInt(map.get("S2FlightNumber").toString());
				for (int j = 0; j < delayList.size(); j++) { 
					HashMap<String,Object> map2 = delayList.get(j);
					if (S1FlightNumber == Integer.parseInt(map2.get("FlightNumber").toString())) {
						map.put("S1AllCount", map2.get("AllCount"));
						map.put("S1DelayCount", map2.get("DelayCount"));
						map.put("S1NotDelay", map2.get("NotDelay"));
					} else if (S2FlightNumber == Integer.parseInt(map2.get("FlightNumber").toString())) {
						map.put("S2AllCount", map2.get("AllCount"));
						map.put("S2DelayCount", map2.get("DelayCount"));
						map.put("S2NotDelay", map2.get("NotDelay"));
					}
				}
				int S1ResidueTickets = 0;
				int S2ResidueTickets = 0;
				int S1soldTickets = ScheduleDao.getReservationTickets(S1ScheduleId, cabinTypeId);
				int S2soldTickets = ScheduleDao.getReservationTickets(S2ScheduleId, cabinTypeId);
				if (1 == cabinTypeId) {
					S1ResidueTickets = Integer.parseInt(map.get("S1EconomySeatsAmount").toString()) - S1soldTickets;
					S2ResidueTickets = Integer.parseInt(map.get("S2EconomySeatsAmount").toString()) - S2soldTickets;
				} else if (2 == cabinTypeId) {
					S1ResidueTickets = Integer.parseInt(map.get("S1BusinessSeatsAmount").toString()) - S1soldTickets;
					S2ResidueTickets = Integer.parseInt(map.get("S2BusinessSeatsAmount").toString()) - S2soldTickets;
				} else if (3 == cabinTypeId) {
					S1ResidueTickets = Integer.parseInt(map.get("S1FirstSeatsAmount").toString()) - S1soldTickets;
					S2ResidueTickets = Integer.parseInt(map.get("S2FirstSeatsAmount").toString()) - S2soldTickets;
				}
				map.put("S1ResidueTickets", S1ResidueTickets);
				map.put("S2ResidueTickets", S2ResidueTickets);
				map.put("FlightType", "1-stop");
			}
		}
		return list;
	}
}
