package edu.wtbu.dao;

import java.util.HashMap;
import java.util.List;

import edu.wtbu.helper.MysqlHelper;

public class UserDao {
	public static List<HashMap<String, Object>> QueryEmail(String email) {
		String sql = "select * from users where email=?";
		return MysqlHelper.Query(sql, new Object[] { email });

	}

	public static HashMap<String, Object> login(String email, String password) {
		String sql = "select * from users where email = ? and password = ?";
		List<HashMap<String, Object>> list = MysqlHelper.Query(sql, new Object[] { email, password });
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public static HashMap<String, Object> findUserList(int userId) {
		String sql = "select * from users where userId = ?";
		List<HashMap<String, Object>> list = MysqlHelper.Query(sql, new Object[] { userId });
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	// ≤È—Ø
	public static List<HashMap<String, Object>> findUserPageByRoleId(int roleId, String name, int startPage,
			int pageSize) {
		String sql = "select * from users where roleId = ? and (firstname like ? or lastname like ?) order by lastname limit ?,?";
		return MysqlHelper.Query(sql,
				new Object[] { roleId, "%" + name + "%", "%" + name + "%", (startPage - 1) * pageSize, pageSize });
	}

	public static int getTotalByRoleId(int roleId, String name) {
		String sql = "select count(1) as total from users where  roleId = ? and (firstname like ? or lastname like ?) ";
		List<HashMap<String, Object>> list = MysqlHelper.Query(sql,
				new Object[] { roleId, "%" + name + "%", "%" + name + "%" });
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0).get("total").toString());
		} else {
			return 0;
		}
	}

	public static List<HashMap<String, Object>> findUserPage(String name, int startPage, int pageSize) {
		String sql = "select * from users where firstname like ? or lastname like ? order by lastname limit ?,?";
		return MysqlHelper.Query(sql,
				new Object[] { "%" + name + "%", "%" + name + "%", (startPage - 1) * pageSize, pageSize });
	}

	public static int getTotal(String name) {
		String sql = "select count(1) as total from users where firstname like ? or lastname like ? ";
		List<HashMap<String, Object>> list = MysqlHelper.Query(sql,
				new Object[] { "%" + name + "%", "%" + name + "%" });
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0).get("total").toString());
		} else {
			return 0;
		}
	}

	public static int addUser(HashMap<String, Object> map) {
		String sql = "insert users (email,firstName,lastName,password,gender,dateOfBirth,phone,photo,address,roleId)values(?,?,?,?,?,?,?,?,?,?)";
		return MysqlHelper.Update(sql,
				new Object[] { map.get("Email"), map.get("FirstName"), map.get("LastName"), map.get("Password"),
						map.get("Gender"), map.get("DateOfBirth"), map.get("Phone"), map.get("Photo"),
						map.get("Address"), map.get("RoleId") });
	}

	public static int updateUser(HashMap<String, Object> map) {
		String sql = "update users set email= ? ,firstName= ?,lastName = ?,gender = ?,dateOfBirth = ?,phone = ?,photo = ?,address = ?,roleId = ? where userId = ? ";
		return MysqlHelper.Update(sql,
				new Object[] { map.get("Email"), map.get("FirstName"), map.get("LastName"), map.get("Gender"),
						map.get("DateOfBirth"), map.get("Phone"), map.get("Photo"), map.get("Address"),
						map.get("RoleId"), map.get("UserId") });
	}

	public static HashMap<String, Object> IsReaptEmail(String email, int userId) {
		String sql = "select * from users where email = ? and userId != ?";
		List<HashMap<String, Object>> list = MysqlHelper.Query(sql, new Object[] { email, userId });
		if (list != null && list.size() > 0) {
			return list.get(0);
		}else {
			return null;
		}
	}
}
