package com.sbs.java.blog.dao;

import java.sql.Connection;
import java.util.Map;

import com.sbs.java.blog.dto.Member;
import com.sbs.java.blog.util.DBUtil;
import com.sbs.java.blog.util.SecSql;

public class MemberDao extends Dao {
	private Connection dbConn;

	public MemberDao(Connection dbConn) {
		System.out.println("MemberDao 생성자");
		this.dbConn = dbConn;
	}
	
	public boolean isJoinableLoginId(String loginId) {
		System.out.println("isJoinableLoginId()");
		SecSql sql = SecSql.from("SELECT COUNT(*) AS cnt");
		sql.append("FROM `member`");
		sql.append("WHERE loginId = ?", loginId);
		
		System.out.println("sql : " + sql);

		return DBUtil.selectRowIntValue(dbConn, sql) == 0;
	}

	public boolean isJoinableNickname(String nickname) {
		System.out.println("isJoinableNickname()");
		SecSql sql = SecSql.from("SELECT COUNT(*) AS cnt");
		sql.append("FROM `member`");
		sql.append("WHERE nickname = ?", nickname);

		System.out.println("sql : " + sql);

		return DBUtil.selectRowIntValue(dbConn, sql) == 0;
	}
	
	public boolean isJoinableEmail(String email) {
		System.out.println("isJoinableEmail()");
		SecSql sql = SecSql.from("SELECT COUNT(*) AS cnt");
		sql.append("FROM `member`");
		sql.append("WHERE email = ?", email);
		
		System.out.println("sql : " + sql);

		return DBUtil.selectRowIntValue(dbConn, sql) == 0;
	}
	
	public int join(String loginId, String loginPw, String name, String nickname, String email) {
		System.out.println("join()");
		SecSql sql = SecSql.from("INSERT INTO member");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", loginId = ?", loginId);
		sql.append(", loginPw = ?", loginPw);
		sql.append(", name = ?", name);
		sql.append(", nickname = ?", nickname);
		sql.append(", email = ?", email);
		sql.append(", mailAuthStatus = 0");
		
		System.out.println("sql : " + sql);
		
		return DBUtil.insert(dbConn, sql);
	}
	
	public void mailAuth(int actorId, String email) {
		SecSql sql = SecSql.from("UPDATE member");
		sql.append("SET updateDate = NOW()");
		sql.append(", mailAuthStatus = ?", 1);
		sql.append("WHERE id = ?", actorId);
		sql.append("AND email = ?", email);
		
		DBUtil.update(dbConn, sql);
	}
	
	public Member getMemberById(int id) {
		System.out.println("getMemberById()");
		SecSql sql = SecSql.from("SELECT *");
		sql.append("FROM `member`");
		sql.append("WHERE id = ?", id);
		
		System.out.println("sql : " + sql);
		
		Map<String, Object> row = DBUtil.selectRow(dbConn, sql);
		System.out.println("row : " + row);
		
		if (row.isEmpty()) {
			System.out.println("row.isEmpty() 조건");
			return null;
		}

		return new Member(row);
	}
	
	public Member getMemberByLoginId(String loginId) {
		System.out.println("getMemberByLoginId()");
		
		SecSql sql = SecSql.from("SELECT *");
		sql.append("FROM `member`");
		sql.append("WHERE loginId = ?", loginId);
		
		Map<String, Object> row = DBUtil.selectRow(dbConn, sql);
		System.out.println("row : " + row);
		
		if (row.isEmpty()) {
			System.out.println("row.isEmpty() 조건");
			return null;
		}

		return new Member(row);
	}
	
	public Member getMemberByNameAndEmail(String name, String email) {
		System.out.println("getMemberByNameAndEmail()");
		
		SecSql sql = SecSql.from("SELECT * ");
		sql.append("FROM `member` ");
		sql.append("WHERE name = ? ", name);
		sql.append("AND email = ? ", email);
		System.out.println("sql : " + sql);

		Map<String, Object> row = DBUtil.selectRow(dbConn, sql);
		System.out.println("row : " + row);
		
		if (row.isEmpty()) {
			System.out.println("row.isEmpty() 조건");
			return null;
		}

		return new Member(row);
	}
	
	public int getMemberIdByLoginIdAndLoginPw(String loginId, String loginPw) {
		System.out.println("getMemberIdByLoginIdAndLoginPw()");
		
		SecSql sql = SecSql.from("SELECT id");
		sql.append("FROM `member`");
		sql.append("WHERE loginId = ?", loginId);
		sql.append("AND loginPw = ?", loginPw);
		System.out.println("sql : " + sql);

		return DBUtil.selectRowIntValue(dbConn, sql);
	}
	
	public void modify(int actorId, String loginPw) {
		System.out.println("modify()");
		SecSql sql = SecSql.from("UPDATE member");
		sql.append("SET updateDate = NOW()");
		sql.append(", loginPw = ?", loginPw);
		sql.append("WHERE id = ?", actorId);
		System.out.println("sql : " + sql);
		
		DBUtil.update(dbConn, sql);
	}
	
//	public void updatePwByIdAndUuid(int id, String uuid) {
//		System.out.println("updatePwByIdAndUuid()");
//		
//		SecSql sql = new SecSql();
//		sql.append("UPDATE `member` ");
//		sql.append("SET loginPw = ? ", uuid);
//		sql.append("WHERE id = ?", id);
//		System.out.println("sql : " + sql);
//
//		DBUtil.update(dbConn, sql);
//	}
}