package com.sbs.java.blog.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sbs.java.blog.exception.SQLErrorException;

public class DBUtil {
	
	public static int selectRowIntValue(Connection dbConn, SecSql secSql) {
		System.out.println("selectRowIntValue()");
		Map<String, Object> row = selectRow(dbConn, secSql);
		System.out.println("row : " + row);

		for (String key : row.keySet()) {
			System.out.println("row.keySet() : " + row.keySet());
			System.out.println("key : " + key);
			return (int) row.get(key);
		}
		System.out.println("-1");
		return -1;
	}
	
	public static String selectRowStringValue(Connection dbConn, SecSql secSql) {
		Map<String, Object> row = selectRow(dbConn, secSql);
			
		for (String key : row.keySet()) {
			System.out.println("key : " + key);
			System.out.println("row : " + row);
			return String.valueOf(row.get(key));
		}
		return "";
	}

	public static boolean selectRowBooleanValue(Connection dbConn, SecSql secSql) {
		Map<String, Object> row = selectRow(dbConn, secSql);

		for (String key : row.keySet()) {
			return ((int) row.get(key)) == 1;
		}
		return false;
	}

	public static Map<String, Object> selectRow(Connection dbConn, SecSql secSql) {
		List<Map<String, Object>> rows = selectRows(dbConn, secSql);
		
		if (rows.size() == 0) {
			System.out.println("rows.size() == 0");
			return new HashMap<>();
		}
		System.out.println("rows.get(0) : " + rows.get(0));
		return rows.get(0);
	}

	public static List<Map<String, Object>> selectRows(Connection dbConn, SecSql secSql) throws SQLErrorException {
		
		List<Map<String, Object>> rows = new ArrayList<>();
		System.out.println("rows : " + rows);
		
		PreparedStatement stmt = null;
		System.out.println("stmt : " + stmt);
		
		ResultSet rs = null;
		System.out.println("rs : " + rs);

		try {
			stmt = secSql.getPreparedStatement(dbConn); // 쿼리문 획득
			System.out.println("stmt : " + stmt); 
			
			rs = stmt.executeQuery(); // 종이
			System.out.println("rs : " + rs); 
			
			ResultSetMetaData metaData = rs.getMetaData(); // 쿼리문 수행
			System.out.println("metaData : " + metaData);
			
			int columnSize = metaData.getColumnCount(); // 쿼리문이 가져온 개수
			System.out.println("columnSize : " + columnSize);

			while (rs.next()) {
				Map<String, Object> row = new HashMap<>();
				System.out.println("row : " + row);

				for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
					
					String columnName = metaData.getColumnName(columnIndex + 1); // 컬럼명
					System.out.println("columnName : " + columnName);
					
					Object value = rs.getObject(columnName); // 컬럼값
					System.out.println("value : " + value);

					if (value instanceof Long) {
						System.out.println("value instanceof Long 조건");
						int numValue = (int) (long) value;
						row.put(columnName, numValue);
					} else if (value instanceof Timestamp) {
						System.out.println("value instanceof Timestamp 조건");
						String dateValue = value.toString();
						System.out.println("dataValue.length() : " + dateValue.length());
						dateValue = dateValue.substring(0, dateValue.length() - 2);
						row.put(columnName, dateValue);
					} else {
						System.out.println("그 외");
						row.put(columnName, value);
					}
					System.out.println("row : " + row);
				}
				rows.add(row);
			}
		} catch (SQLException e) {
			throw new SQLErrorException("SQL 예외, SQL : " + secSql, e);
		} finally {
			if (rs != null) {
				System.out.println("rs != null 조건");
				try {
					rs.close();
					System.out.println("rs.close()");
				} catch (SQLException e) {
					throw new SQLErrorException("SQL 예외, rs 닫기" + secSql, e);
				}
			}

			if (stmt != null) {
				System.out.println("stmt != null 조건");
				try {
					stmt.close();
					System.out.println("stmt.close()");
				} catch (SQLException e) {
					throw new SQLErrorException("SQL 예외, stmt 닫기" + secSql, e);
				}
			}
		}
		return rows;
	}
	
	public static int insert(Connection dbConn, SecSql secSql) {
		System.out.println("insert()");
		
		int id = -1;
		System.out.println("id : " + id);

		PreparedStatement stmt = null;
		System.out.println("stmt : " + stmt);
		
		ResultSet rs = null;
		System.out.println("rs : " + rs);

		try {
			stmt = secSql.getPreparedStatement(dbConn);
			System.out.println("stmt : " + stmt);
			
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			System.out.println("rs : " + rs);
			
			if (rs.next()) {
				id = rs.getInt(1);
			}

		} catch (SQLException e) {
			throw new SQLErrorException("SQL 예외, SQL : " + secSql, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
					System.out.println("rs.close()");
				} catch (SQLException e) {
					throw new SQLErrorException("SQL 예외, rs 닫기" + secSql, e);
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
					System.out.println("stmt.close()");
				} catch (SQLException e) {
					throw new SQLErrorException("SQL 예외, stmt 닫기" + secSql, e);
				}
			}

		}
		System.out.println("id : " + id);
		return id;
	}

	public static int update(Connection dbConn, SecSql secSql) {
		System.out.println("update()");
		
		int affectedRows = 0;

		PreparedStatement stmt = null;

		try {
			stmt = secSql.getPreparedStatement(dbConn);
			System.out.println("stmt : " + stmt);
			
			affectedRows = stmt.executeUpdate();
		} catch (SQLException e) {
			throw new SQLErrorException("SQL 예외, SQL : " + secSql, e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					throw new SQLErrorException("SQL 예외, stmt 닫기, SQL : " + secSql, e);
				}
			}
		}
		System.out.println("affectedRows : " + affectedRows);
		return affectedRows;
	}
	
	public static int delete(Connection dbConn, SecSql secSql) {
		System.out.println("delete()");
		return update(dbConn, secSql);
	}
}