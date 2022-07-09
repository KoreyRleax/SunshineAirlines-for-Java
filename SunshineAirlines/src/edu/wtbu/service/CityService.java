package edu.wtbu.service;

import java.util.HashMap;
import java.util.List;

import edu.wtbu.dao.CityDao;
import edu.wtbu.pojo.Result;

public class CityService {
	public static Result getCitys() {
		Result rs = new Result("success", null, null);
		List<HashMap<String, Object>> list = CityDao.findAllCity();
		rs.setData(list);
		return rs;
	}
}
