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

	public Attr get(String relTypeCode, int relId, String typeCode, String type2Code) {
		System.out.println("get()");
		
		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM attr");
		sql.append("WHERE 1");
		sql.append("AND `relTypeCode` = ?", relTypeCode);
		sql.append("AND `relId` = ?", relId);
		sql.append("AND `typeCode` = ?", typeCode);
		sql.append("AND `type2Code` = ?", type2Code);
		
		System.out.println("sql : " + sql);

		return new Attr(DBUtil.selectRow(dbConn, sql));
	}

	public String getValue(String relTypeCode, int relId, String typeCode, String type2Code) {
		System.out.println("getValue()");
		
		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM attr");
		sql.append("WHERE 1");
		sql.append("AND `relTypeCode` = ?", relTypeCode);
		sql.append("AND `relId` = ?", relId);
		sql.append("AND `typeCode` = ?", typeCode);
		sql.append("AND `type2Code` = ?", type2Code);

		System.out.println("sql : " + sql);

		return DBUtil.selectRowStringValue(dbConn, sql);
	}
	
	public int setValue(String relTypeCode, int relId, String typeCode, String type2Code, String value) {
		System.out.println("setValue()");
		
		SecSql sql = new SecSql();

		sql.append("INSERT INTO attr (regDate, updateDate, `relTypeCode`, `relId`, `typeCode`, `type2Code`, `value`)");
		sql.append("VALUES (NOW(), NOW(), ?, ?, ?, ?, ?)", relTypeCode, relId, typeCode, type2Code, value);
		sql.append("ON DUPLICATE KEY UPDATE");
		sql.append("updateDate = NOW()");
		sql.append(", `value` = ?", value);
		
		System.out.println("sql : " + sql);

		return DBUtil.update(dbConn, sql);
	}
	
	public int remove(String relTypeCode, int relId, String typeCode, String type2Code) {
		System.out.println("remove()");
		
		SecSql sql = new SecSql();

		sql.append("DELETE FROM attr");
		sql.append("WHERE 1");
		sql.append("AND `relTypeCode` = ?", relTypeCode);
		sql.append("AND `relId` = ?", relId);
		sql.append("AND `typeCode` = ?", typeCode);
		sql.append("AND `type2Code` = ?", type2Code);
		
		System.out.println("sql : " + sql);

		return DBUtil.delete(dbConn, sql);
	}
}