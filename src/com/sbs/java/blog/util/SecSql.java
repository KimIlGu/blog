package com.sbs.java.blog.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SecSql {
	private StringBuilder sqlBuilder;
	private List<Object> datas;

	public SecSql() {
		System.out.println("SecSql 생성자");
		sqlBuilder = new StringBuilder();
		datas = new ArrayList<>();
	}

	public SecSql append(Object... args) {
		System.out.println("append()");
		System.out.println("args : " + args);
		
		if (args.length > 0) {
			System.out.println("args.length > 0 조건");
			System.out.println("args.length : " + args.length);
			
			String sqlBit = (String) args[0];
			System.out.println("args[0] : " + args[0]);
			System.out.println("sqlBit : " + (String) args[0]);
			
			sqlBuilder.append(sqlBit + " ");
			System.out.println("sqlBuilder : " + sqlBit + " ");
		}

		for (int i = 1; i < args.length; i++) {
			System.out.println("args[1] : " + args[1]);
			System.out.println("args.length : " + args.length);
			
			datas.add(args[i]);
			System.out.println("datas : " + datas);
		}
		System.out.println("this : " + this);
		return this; // this = toString()
	}
	
	@Override
	public String toString() {
		System.out.println("toString()");
		return "sql=" + getFormat() + ", datas=" + datas;
	}

	public String getFormat() {
		System.out.println("getFormat()");
		return sqlBuilder.toString();
	}
	
	public boolean isInsert() {
		System.out.println("isInsert()");
		return getFormat().startsWith("INSERT");
	}

	public PreparedStatement getPreparedStatement(Connection dbConn) throws SQLException {
		System.out.println("getPreparedStatement()");
		
		PreparedStatement stmt = null;
		System.out.println("stmt : " + stmt);

		if (isInsert()) {
			System.out.println("isInsert() 조건");
			stmt = dbConn.prepareStatement(getFormat(), Statement.RETURN_GENERATED_KEYS);
		} else {
			System.out.println("!isInsert() 조건");
			stmt = dbConn.prepareStatement(getFormat());
		}

		for (int i = 0; i < datas.size(); i++) {
			System.out.println("datas : " + datas);
			System.out.println("datas.size() : " + datas.size());
			
			Object data = datas.get(i);
			System.out.println("datas.get(i) : " + datas.get(i));
			System.out.println("data : " + data);
			
			int parameterIndex = i + 1;
			System.out.println("parameterIndex : " + parameterIndex);

			if (data instanceof Integer) {
				System.out.println("data instanceof Integer 조건");
				stmt.setInt(parameterIndex, (int) data);
			} else if (data instanceof String) {
				System.out.println("data instanceof String 조건");
				stmt.setString(parameterIndex, (String) data);
			}
			System.out.println("stmt : " + stmt);
		}
		return stmt;
	}
	
	public static SecSql from(String sql) {
		System.out.println("from()");
		System.out.println("sql : " + sql);
		
		SecSql secSql = new SecSql().append(sql);
		System.out.println("secSql : " + secSql);
		
		return secSql;
	}
}