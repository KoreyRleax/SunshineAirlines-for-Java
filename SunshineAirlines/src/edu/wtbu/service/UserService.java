package edu.wtbu.service;

import java.util.HashMap;
import java.util.List;

import edu.wtbu.dao.UserDao;
import edu.wtbu.pojo.Page;
import edu.wtbu.pojo.Result;

public class UserService {
	public static Boolean isEmail(String email) {
		List<HashMap<String, Object>> list = UserDao.QueryEmail(email);
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static Result login(String email, String password) {
		Result rs = new Result("fail", null, null);
		HashMap<String, Object> map = UserDao.login(email, password);
		if (map != null) {
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("Email", map.get("Email"));
			data.put("RoleId", map.get("RoleId"));
			rs.setFlag("success");
			rs.setData(data);
		} else {
			if (isEmail(email)) {
				rs.setData("密码错误");
			} else {
				rs.setData("邮箱不存在");
			}
		}
		return rs;
	}

	public static Result getUserInfo(int userId) {
		Result rs = new Result("fail", null, null);
		HashMap<String, Object> map = UserDao.findUserList(userId);
		if (map != null && map.size() > 0) {
			rs.setFlag("success");
			rs.setData(map);
		} else {
			rs.setData("用户信息不存在");
		}
		return rs;
	}

	public static Result userList(int roleId, String name, int startPage, int pageSize) {
		Result rs = new Result("success", null, null);
		Page page = null;
		List<HashMap<String, Object>> list = null;
		int total = 0;
		if (roleId != 0) {
			list = UserDao.findUserPageByRoleId(roleId, name, startPage, pageSize);
			total = UserDao.getTotalByRoleId(roleId, name);
		} else {
			list = UserDao.findUserPage(name, startPage, pageSize);
			total = UserDao.getTotal(name);
		}
		page = new Page(total, startPage, pageSize);
		rs.setPage(page);
		rs.setData(list);
		return rs;
	}

	public static Result addUser(HashMap<String, Object> map) {
		Result rs = new Result("fail", null, null);
		String email = map.get("Email").toString();
		if (isEmail(email)) {
			rs.setData("邮箱重复");
			return rs;
		}
		UserDao.addUser(map);
		rs.setFlag("success");
		return rs;
	}

	public static Boolean isRepatEmail(String email, int userId) {
		HashMap<String, Object> map = UserDao.IsReaptEmail(email, userId);
		if (map != null && map.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static Result updateUser(HashMap<String, Object> map) {
		Result rs = new Result("fail", null, null);
		String email = map.get("Email").toString();
		int userId = Integer.parseInt(map.get("UserId").toString());
		HashMap<String, Object> isUserId = UserDao.findUserList(userId);
		if(isUserId==null) {
			rs.setData("用户信息不存在");
			return rs;
		}
		if(isRepatEmail(email, userId)) {
			rs.setData("邮箱重复");
			return rs;
		}
		int updateResult = UserDao.updateUser(map);
		if(updateResult>=0) {
			rs.setFlag("success");
		}
		return rs;
	}
}
