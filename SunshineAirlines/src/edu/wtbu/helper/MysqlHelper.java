package edu.wtbu.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MysqlHelper {
	public static String driver = "com.mysql.cj.jdbc.Driver";
	public static String url = "jdbc:mysql://localhost:3306/Session1?serverTimezone=GMT%2B8&useOldAliasMetadataBehavior=true";
	public static Connection con = null;
	public static PreparedStatement pstm = null;
	public static ResultSet rs = null;
	static {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		try {
			con = DriverManager.getConnection(url, "root", "123456");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

	public static List<HashMap<String, Object>> Query(String sql, Object para[]) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		try {
			pstm = getConnection().prepareStatement(sql);
			if (para != null) {
				for (int i = 0; i < para.length; i++) {
					String className = para[i].getClass().toString();
					if (className.contains("Integer")) {
						pstm.setInt(i + 1, Integer.parseInt(para[i].toString()));
					} else if (className.contains("String")) {
						pstm.setString(i + 1, para[i].toString());
					}
				}
			}
			rs = pstm.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					map.put(rsmd.getColumnName(i + 1), rs.getObject(i + 1));
				}
				list.add(map);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, pstm, con);
		}
		return list;
	}

	public static int Update(String sql, Object para[]) {
		int result = 0;
		try {
			pstm = getConnection().prepareStatement(sql);
			if (para != null) {
				for (int i = 0; i < para.length; i++) {
					String className = para[i].getClass().toString();
					if (className.contains("Integer")) {
						pstm.setInt(i + 1, Integer.parseInt(para[i].toString()));
					} else if (className.contains("String")) {
						pstm.setString(i + 1, para[i].toString());
					}
				}
			}
			result = pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, pstm, con);
		}
		return result;
	}

	public static void close(ResultSet rs, PreparedStatement pstm, Connection con) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pstm != null) {
			try {
				pstm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
