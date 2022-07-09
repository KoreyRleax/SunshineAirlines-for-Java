package edu.wtbu.dao;

import java.util.HashMap;
import java.util.List;

import edu.wtbu.helper.MysqlHelper;

public class ScheduleDao {

	public static int updateSchedule(int scheduleId, String status) {
		String sql = "update schedule set status = ? where scheduleId = ?";
		return MysqlHelper.Update(sql, new Object[] { status, scheduleId });
	}

	public static List<HashMap<String, Object>> getFlightStatus(String startDate, String endDate, int startPage,
			int pageSize) {
		String sql = "select \r\n" + "`schedule`.ScheduleId,\r\n" + "`schedule`.FlightNumber,\r\n"
				+ "route.ArrivalAirportIATA,\r\n" + "DepartCity.cityName as DepartCityName,\r\n"
				+ "ArrivalCity.cityName as ArrivalCityName,\r\n" + "route.DepartureAirportIATA,\r\n"
				+ "flightstatus.ActualArrivalTime,\r\n" + "`schedule`.Date,\r\n" + "`schedule`.Time,\r\n"
				+ "`schedule`.Gate,\r\n" + "route.FlightTime\r\n" + "from `schedule`\r\n"
				+ "LEFT JOIN flightstatus on flightstatus.ScheduleId = `schedule`.ScheduleId\r\n"
				+ "LEFT JOIN route on route.RouteId = `schedule`.RouteId\r\n"
				+ "LEFT JOIN airport as DepartAirport on DepartAirport.IATACode=route.DepartureAirportIATA\r\n"
				+ "LEFT JOIN airport as ArrivalAirport on ArrivalAirport.IATACode=route.ArrivalAirportIATA\r\n"
				+ "LEFT JOIN city as DepartCity on DepartCity.CityCode = DepartAirport.CityCode\r\n"
				+ "LEFT JOIN city as ArrivalCity on ArrivalCity.CityCode =ArrivalAirport.CityCode\r\n"
				+ "WHERE `schedule`.Date between ? and ? \r\n"
				+ "ORDER BY `schedule`.Date,`schedule`.FlightNumber LIMIT ?,?\r\n" + "";
		return MysqlHelper.Query(sql, new Object[] { startDate, endDate, (startPage - 1) * pageSize, pageSize });
	}

	public static int getFightStatusTotal(String startDate, String endDate) {
		String sql = "select count(1) as total from schedule where schedule.date between ? and ?";
		List<HashMap<String, Object>> list = MysqlHelper.Query(sql, new Object[] { startDate, endDate });
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0).get("total").toString());
		} else {
			return 0;
		}
	}

	public static List<HashMap<String, Object>> getSchedule(String fromCity, String toCity, String startDate,
			String endDate) {
		String sql = "select \r\n" + "`schedule`.ScheduleId,\r\n" + "`schedule`.FlightNumber,\r\n"
				+ "route.DepartureAirportIATA,\r\n" + "route.ArrivalAirportIATA,\r\n"
				+ "DepartCity.cityName as DepartCityName,\r\n" + "ArrivalCity.cityName as ArriveCityName,\r\n"
				+ "aircraft.`Name`,\r\n" + "`schedule`.EconomyPrice,\r\n" + "`schedule`.`Status`,\r\n"
				+ "`schedule`.Date,\r\n" + "`schedule`.Time,\r\n" + "`schedule`.Gate\r\n" + "from `schedule`\r\n"
				+ "LEFT JOIN aircraft on aircraft.AircraftId = `schedule`.AircraftId\r\n"
				+ "LEFT JOIN flightstatus on flightstatus.ScheduleId = `schedule`.ScheduleId\r\n"
				+ "LEFT JOIN route on route.RouteId = `schedule`.RouteId\r\n"
				+ "LEFT JOIN airport as DepartAirport on DepartAirport.IATACode=route.DepartureAirportIATA\r\n"
				+ "LEFT JOIN airport as ArrivalAirport on ArrivalAirport.IATACode=route.ArrivalAirportIATA\r\n"
				+ "LEFT JOIN city as DepartCity on DepartCity.CityCode = DepartAirport.CityCode\r\n"
				+ "LEFT JOIN city as ArrivalCity on ArrivalCity.CityCode =ArrivalAirport.CityCode\r\n"
				+ "WHERE DepartCity.CityName = ? and ArrivalCity.CityName = ? and `schedule`.Date between ? and ? \r\n"
				+ "ORDER BY `schedule`.Date";
		return MysqlHelper.Query(sql, new Object[] { fromCity, toCity, startDate, endDate });AA
	}

	public static HashMap<String, Object> findByScheduleId(int scheduleId) {
		String sql = "select * from `schedule`\r\n" + "LEFT JOIN route on route.RouteId = `schedule`.RouteId \r\n"
				+ "LEFT JOIN aircraft on aircraft.AircraftId = `schedule`.AircraftId\r\n"
				+ "where `schedule`.ScheduleId = ?";
		List<HashMap<String, Object>> list = MysqlHelper.Query(sql, new Object[] { scheduleId });
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public static List<HashMap<String, Object>> findTicketInfoList(int scheduleId) {
		String sql = "select CabinTypeId,count(1) as soldCounts,count(SeatLayoutId) as selectedCounts\r\n"
				+ "from flightreservation\r\n" + "where flightreservation.ScheduleId = ?\r\n" + "GROUP BY CabinTypeId";
		return MysqlHelper.Query(sql, new Object[] { scheduleId });
	}

	public static List<HashMap<String, Object>> findSelectedSeatList(int scheduleId) {
		String sql = "select flightreservation.CabintypeId,seatlayout.ColumnName,seatlayout.RowNumber\r\n"
				+ "from flightreservation\r\n"
				+ "LEFT JOIN seatlayout on  seatlayout.Id = flightreservation.SeatLayoutId \r\n"
				+ "where flightreservation.ScheduleId = ?";
		return MysqlHelper.Query(sql, new Object[] { scheduleId });
	}

	public static List<HashMap<String, Object>> findSeatLayoutList(int aircraftId) {
		String sql = "select * from seatlayout where AircraftId = ?";
		return MysqlHelper.Query(sql, new Object[] { aircraftId });
	}

	public static List<HashMap<String, Object>> getNonStopList(String fromCity, String toCity, String startDate,
			String endDate) {
		String sql = "SELECT \r\n" + "`schedule`.ScheduleId,\r\n" + "`schedule`.EconomyPrice,\r\n"
				+ "`schedule`.FlightNumber,\r\n" + "FlightCount.AllCount,\r\n" + "FlightCount.DelayCount,\r\n"
				+ "FlightCount.NotDelay,\r\n" + "route.DepartureAirportIATA,\r\n"
				+ "DepartCity.CityName as DepartCityName,\r\n" + "`schedule`.Date,\r\n" + "`schedule`.Time,\r\n"
				+ "route.ArrivalAirportIATA,\r\n" + "ArriveCity.CityName as ArriveCityName,\r\n"
				+ "DATE_ADD(`schedule`.Date,INTERVAL FlightTime MINUTE) as PreArrivalTime,\r\n"
				+ "route.FlightTime,\r\n" + "aircraft.FirstSeatsAmount,\r\n" + "aircraft.BusinessSeatsAmount,\r\n"
				+ "aircraft.EconomySeatsAmount\r\n" + "FROM `schedule`\r\n"
				+ "LEFT JOIN Aircraft on Aircraft.AircraftId = `Schedule`.AircraftId \r\n"
				+ "LEFT JOIN Route on Route.RouteId = `Schedule`.RouteId \r\n"
				+ "LEFT JOIN Airport as DepartAirport on Route.DepartureAirportIATA = DepartAirport.IATACode \r\n"
				+ "LEFT JOIN Airport as ArriveAirport on Route.ArrivalAirportIATA = ArriveAirport.IATACode \r\n"
				+ "LEFT JOIN City as DepartCity on DepartAirport.CityCode = DepartCity.CityCode \r\n"
				+ "LEFT JOIN City as ArriveCity on ArriveAirport.CityCode = ArriveCity.CityCode \r\n" + "LEFT JOIN \r\n"
				+ "				(\r\n"
				+ "					SELECT AllFlight.FlightNumber,AllFlight.AllCount,DelayFlight.DelayCount,AllFlight.AllCount-DelayFlight.DelayCount as NotDelay\r\n"
				+ "					FROM\r\n" + "							(\r\n"
				+ "								SELECT `schedule`.FlightNumber,COUNT(1) as AllCount\r\n"
				+ "								FROM `schedule`\r\n"
				+ "								WHERE ? BETWEEN `schedule`.Date AND DATE_ADD(`schedule`.Date,INTERVAL 30 DAY)\r\n"
				+ "								GROUP BY `schedule`.FlightNumber\r\n"
				+ "							)as AllFlight\r\n" + "					LEFT JOIN\r\n"
				+ "										(\r\n"
				+ "											SELECT `schedule`.FlightNumber,COUNT(1) as DelayCount\r\n"
				+ "											FROM `schedule`\r\n"
				+ "											LEFT JOIN route on route.RouteId = `schedule`.RouteId\r\n"
				+ "											LEFT JOIN flightstatus on flightstatus.ScheduleId = `schedule`.ScheduleId\r\n"
				+ "											WHERE ? BETWEEN `schedule`.Date AND DATE_ADD(`schedule`.Date,INTERVAL 30 DAY)\r\n"
				+ "											AND (`schedule`.`Status`=\"Canceled\" or TIMESTAMPDIFF(MINUTE,DATE_ADD(`schedule`.Date,INTERVAL route.FlightTime MINUTE),flightstatus.ActualArrivalTime)>15)\r\n"
				+ "											GROUP BY `schedule`.FlightNumber\r\n"
				+ "										)as DelayFlight\r\n"
				+ "					on AllFlight.FlightNumber = DelayFlight.FlightNumber\r\n"
				+ "				)as FlightCount\r\n" + "on FlightCount.FlightNumber = `schedule`.FlightNumber\r\n"
				+ "WHERE DepartCity.CityName=? and ArriveCity.CityName=? and `schedule`.Date between ? and ? \r\n"
				+ "ORDER BY `schedule`.Date";
		return MysqlHelper.Query(sql, new Object[] { startDate, startDate, fromCity, toCity, startDate, endDate });
	}

	public static int getReservationTickets(int scheduleId, int cabinTypeId) {
		String sql = "SELECT count(1) as total \r\n"
				+ "from flightreservation WHERE ScheduleId = ? and flightreservation.CabinTypeId = ?";
		List<HashMap<String, Object>> list = MysqlHelper.Query(sql, new Object[] { scheduleId, cabinTypeId });
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0).get("total").toString());
		} else {
			return 0;
		}
	}

	public static List<HashMap<String, Object>> getSearchFlightOneStop(String StartDate, String endDate,
			String fromCity, String toCity) {
		String sql = "SELECT \r\n" + 
				"S1.ScheduleId as S1ScheduleId,  \r\n" + 
				"Route.DepartureAirportIATA as S1DepartureAirportIATA, \r\n" + 
				"DepartCity.CityName as S1DepartCityName,  \r\n" + 
				"Route.ArrivalAirportIATA as S1ArrivalAirportIATA, \r\n" + 
				"ArriveCity.CityName as S1ArriveCityName,  \r\n" + 
				"S1.Date as S1Date,  \r\n" + 
				"S1.Time as S1Time,  \r\n" + 
				"DATE_ADD(S1.Date,INTERVAL Route.FlightTime MINUTE) as S1PreArrivalTime,  \r\n" + 
				"Route.FlightTime as S1FlightTime,  \r\n" + 
				"S1.EconomyPrice as S1EconomyPrice,  \r\n" + 
				"S1.FlightNumber as S1FlightNumber,  \r\n" + 
				"Aircraft.FirstSeatsAmount as S1FirstSeatsAmount,  \r\n" + 
				"Aircraft.BusinessSeatsAmount as S1BusinessSeatsAmount,  \r\n" + 
				"Aircraft.EconomySeatsAmount as S1EconomySeatsAmount,  \r\n" + 
				"S2.*\r\n" + 
				"FROM `schedule` as S1\r\n" + 
				"LEFT JOIN Aircraft on Aircraft.AircraftId = S1.AircraftId \r\n" + 
				"LEFT JOIN Route on Route.RouteId = S1.RouteId \r\n" + 
				"LEFT JOIN Airport as DepartAirport on Route.DepartureAirportIATA = DepartAirport.IATACode \r\n" + 
				"LEFT JOIN Airport as ArriveAirport on Route.ArrivalAirportIATA = ArriveAirport.IATACode \r\n" + 
				"LEFT JOIN City as DepartCity on DepartAirport.CityCode = DepartCity.CityCode \r\n" + 
				"LEFT JOIN City as ArriveCity on ArriveAirport.CityCode = ArriveCity.CityCode \r\n" + 
				"LEFT JOIN \r\n" + 
				"				(\r\n" + 
				"					SELECT \r\n" + 
				"					`schedule`.ScheduleId as S2ScheduleId,  \r\n" + 
				"					Route.DepartureAirportIATA as S2DepartureAirportIATA, \r\n" + 
				"					DepartCity.CityName as S2DepartCityName,  \r\n" + 
				"					Route.ArrivalAirportIATA as S2ArrivalAirportIATA, \r\n" + 
				"					ArriveCity.CityName as S2ArriveCityName,  \r\n" + 
				"					`schedule`.Date as S2Date,  \r\n" + 
				"					`schedule`.Time as S2Time,  \r\n" + 
				"					DATE_ADD(`schedule`.Date,INTERVAL Route.FlightTime MINUTE) as S2PreArrivalTime,  \r\n" + 
				"					Route.FlightTime as S2FlightTime,  \r\n" + 
				"					`schedule`.EconomyPrice as S2EconomyPrice,  \r\n" + 
				"					`schedule`.FlightNumber as S2FlightNumber,  \r\n" + 
				"					Aircraft.FirstSeatsAmount as S2FirstSeatsAmount,  \r\n" + 
				"					Aircraft.BusinessSeatsAmount as S2BusinessSeatsAmount,  \r\n" + 
				"					Aircraft.EconomySeatsAmount as S2EconomySeatsAmount\r\n" + 
				"					FROM `schedule`\r\n" + 
				"					LEFT JOIN Aircraft on Aircraft.AircraftId = `Schedule`.AircraftId \r\n" + 
				"					LEFT JOIN Route on Route.RouteId = `Schedule`.RouteId \r\n" + 
				"					LEFT JOIN Airport as DepartAirport on Route.DepartureAirportIATA = DepartAirport.IATACode \r\n" + 
				"					LEFT JOIN Airport as ArriveAirport on Route.ArrivalAirportIATA = ArriveAirport.IATACode \r\n" + 
				"					LEFT JOIN City as DepartCity on DepartAirport.CityCode = DepartCity.CityCode \r\n" + 
				"					LEFT JOIN City as ArriveCity on ArriveAirport.CityCode = ArriveCity.CityCode \r\n" + 
				"				)as S2\r\n" + 
				"on S2.S2DepartureAirportIATA = route.ArrivalAirportIATA\r\n" + 
				"WHERE DepartCity.CityName=? and S2.S2ArriveCityName=? and S1.Date between ? and ?\r\n" + 
				"and TIMESTAMPDIFF(HOUR,DATE_ADD(S1.Date,INTERVAL route.FlightTime MINUTE),S2.S2Date) BETWEEN 2 AND 9\r\n" + 
				"ORDER BY S1.Date";
		return MysqlHelper.Query(sql, new Object[] { fromCity, toCity, StartDate, endDate });
	}

	// 一次中转--延迟航班数量
	public static List<HashMap<String, Object>> getDelayInfoList(String StartDate) {
		String sql = "SELECT AllFlight.FlightNumber,AllFlight.AllCount,DelayFlight.DelayCount,AllFlight.AllCount-DelayFlight.DelayCount as NotDelay\r\n"
				+ "FROM (\r\n" + "			SELECT `schedule`.FlightNumber,COUNT(1) as AllCount\r\n"
				+ "			FROM `schedule`\r\n"
				+ "			WHERE ? BETWEEN `schedule`.Date AND DATE_ADD(`schedule`.Date,INTERVAL 30 DAY)\r\n"
				+ "			GROUP BY `schedule`.FlightNumber\r\n" + "			)as AllFlight\r\n" + "LEFT JOIN \r\n"
				+ "			(\r\n" + "				SELECT `schedule`.FlightNumber,COUNT(1) as DelayCount\r\n"
				+ "				FROM `schedule`\r\n"
				+ "				LEFT JOIN route on route.RouteId = `schedule`.RouteId\r\n"
				+ "				LEFT JOIN flightstatus on flightstatus.ScheduleId = `schedule`.ScheduleId\r\n"
				+ "				WHERE ? BETWEEN `schedule`.Date AND DATE_ADD(`schedule`.Date,INTERVAL 30 DAY)\r\n"
				+ "				AND(`schedule`.`Status`=\"Canceled\" or TIMESTAMPDIFF(MINUTE,DATE_ADD(`schedule`.Date,INTERVAL route.FlightTime MINUTE),flightstatus.ActualArrivalTime)>15)\r\n"
				+ "				GROUP BY `schedule`.FlightNumber\r\n" + "			)as DelayFlight	\r\n"
				+ "on AllFlight.FlightNumber = DelayFlight.FlightNumber\r\n" + "";
		return MysqlHelper.Query(sql, new Object[] { StartDate, StartDate });
	}
}