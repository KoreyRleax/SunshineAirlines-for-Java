package edu.wtbu.dao;

import java.util.HashMap;
import java.util.List;

import edu.wtbu.helper.MysqlHelper;

public class CityDao {
	public static List<HashMap<String,Object>> findAllCity(){
		String sql = "select * from city";
		return MysqlHelper.Query(sql, null);
	}
}
