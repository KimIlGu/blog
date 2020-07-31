package com.sbs.java.blog.dao;

import java.sql.Connection;

import com.sbs.java.blog.dto.Attr;
import com.sbs.java.blog.util.DBUtil;
import com.sbs.java.blog.util.SecSql;

public class AttrDao {
	private Connection dbConn;

	public AttrDao(Connection dbConn) {
		System.out.println("AttrDao 생성자");
		this.dbConn = dbConn;
	}

	public int setValue(String name, String value) {
		System.out.println("setValue()");
		
		SecSql secSql = new SecSql();

		secSql.append("INSERT INTO attr (regDate, updateDate, `name`, `value`)");
		secSql.append("VALUES (NOW(), NOW(), ?, ?)", name, value);
		secSql.append("ON DUPLICATE KEY UPDATE");
		secSql.append("updateDate = NOW()");
		secSql.append(", `value` = ?", value);
		System.out.println("sql : " + secSql);

		return DBUtil.update(dbConn, secSql);
	}
	
	public Attr get(String name) {
		System.out.println("get()");
		
		SecSql secSql = new SecSql();

		secSql.append("SELECT *");
		secSql.append("FROM attr");
		secSql.append("WHERE `name` = ?", name);
		System.out.println("sql : " + secSql);

		return new Attr(DBUtil.selectRow(dbConn, secSql));
	}

	public String getValue(String name) {
		System.out.println("getValue()");
		
		SecSql secSql = new SecSql();

		secSql.append("SELECT `value`");
		secSql.append("FROM attr");
		secSql.append("WHERE `name` = ?", name);
		System.out.println("AttrDao.getValue() sql : " + secSql);

		return DBUtil.selectRowStringValue(dbConn, secSql);
	}
	
	public int remove(String name) {
		System.out.println("remove()");
		
		SecSql secSql = new SecSql();

		secSql.append("DELETE FROM attr");
		secSql.append("WHERE `name` = ?", name);
		System.out.println("sql : " + secSql);

		return DBUtil.delete(dbConn, secSql);
	}
}