package com.sbs.java.blog.dao;

import java.sql.Connection;

import com.sbs.java.blog.dto.Member;
import com.sbs.java.blog.util.DBUtil;
import com.sbs.java.blog.util.SecSql;

public class MemberDao extends Dao {
	private Connection dbConn;

	public MemberDao(Connection dbConn) {
		System.out.println("MemberDao 생성자");
		this.dbConn = dbConn;
	}
	
	public Member getMemberById(int id) {
		System.out.println("getMemberById()");
		SecSql secSql = SecSql.from("SELECT *");
		secSql.append("FROM `member`");
		secSql.append("WHERE id = ?", id);
		System.out.println("sql : " + secSql);
		
		Member member = new Member(DBUtil.selectRow(dbConn, secSql));
		System.out.println("member : " + member);
		
		return member; 
	}
	
	public boolean isJoinableLoginId(String loginId) {
		System.out.println("isJoinableLoginId()");
		SecSql secSql = SecSql.from("SELECT COUNT(*) AS cnt");
		secSql.append("FROM `member`");
		secSql.append("WHERE loginId = ?", loginId);
		System.out.println("sql : " + secSql);

		return DBUtil.selectRowIntValue(dbConn, secSql) == 0;
	}

	public boolean isJoinableNickname(String nickname) {
		System.out.println("isJoinableNickname()");
		SecSql secSql = SecSql.from("SELECT COUNT(*) AS cnt");
		secSql.append("FROM `member`");
		secSql.append("WHERE nickname = ?", nickname);
		System.out.println("sql : " + secSql);

		return DBUtil.selectRowIntValue(dbConn, secSql) == 0;
	}
	
	public boolean isJoinableEmail(String email) {
		System.out.println("isJoinableEmail()");
		SecSql secSql = SecSql.from("SELECT COUNT(*) AS cnt");
		secSql.append("FROM `member`");
		secSql.append("WHERE email = ?", email);
		System.out.println("sql : " + secSql);

		return DBUtil.selectRowIntValue(dbConn, secSql) == 0;
	}
	
	public int join(String loginId, String loginPw, String name, String nickname, String email) {
		System.out.println("join()");
		SecSql secSql = new SecSql();

		secSql.append("INSERT INTO member ");
		secSql.append("SET regDate = NOW() ");
		secSql.append(", updateDate = NOW()");
		secSql.append(", loginId = ? ", loginId);
		secSql.append(", loginPw = ? ", loginPw);
		secSql.append(", name = ? ", name);
		secSql.append(", nickname = ? ", nickname);
		secSql.append(", email = ?", email);
		System.out.println("sql : " + secSql);
		
		return DBUtil.insert(dbConn, secSql);
	}

	public int getMemberIdByLoginIdAndLoginPw(String loginId, String loginPw) {
		System.out.println("getMemberIdByLoginIdAndLoginPw()");
		
		SecSql secSql = SecSql.from("SELECT id");
		secSql.append("FROM `member`");
		secSql.append("WHERE loginId = ?", loginId);
		secSql.append("AND loginPw = ?", loginPw);
		System.out.println("sql : " + secSql);

		return DBUtil.selectRowIntValue(dbConn, secSql);
	}
	
	public Member getMemberByNameAndEmail(String name, String email) {
		System.out.println("getMemberByNameAndEmail()");
		
		SecSql secSql = SecSql.from("SELECT * ");
		secSql.append("FROM `member` ");
		secSql.append("WHERE name = ? ", name);
		secSql.append("AND email = ? ", email);
		System.out.println("sql : " + secSql);

		Member member = new Member(DBUtil.selectRow(dbConn, secSql));
		System.out.println("member : " + member);
		
		return member;
	}

	public Member getMemberByNameAndEmailAndLoginId(String loginId, String name, String email) {
		System.out.println("getMemberByNameAndEmailAndLoginId()");
		
		SecSql secSql = SecSql.from("SELECT *");
		secSql.append("FROM `member`");
		secSql.append("WHERE loginId = ?", loginId);
		secSql.append("AND name = ?", name);
		secSql.append("AND email = ? ", email);
		System.out.println("sql : " + secSql);
		
		Member member = new Member(DBUtil.selectRow(dbConn, secSql));
		System.out.println("member : " + member);
		
		return member;
	}
	
	public void updatePwByIdAndUuid(int id, String uuid) {
		System.out.println("updatePwByIdAndUuid()");
		
		SecSql secSql = new SecSql();
		secSql.append("UPDATE `member` ");
		secSql.append("SET loginPw = ? ", uuid);
		secSql.append("WHERE id = ?", id);
		System.out.println("sql : " + secSql);

		DBUtil.update(dbConn, secSql);
	}

	public void modify(int actorId, String loginPw) {
		System.out.println("modify()");
		SecSql secSql = SecSql.from("UPDATE member");
		secSql.append("SET updateDate = NOW()");
		secSql.append(", loginPw = ?", loginPw);
		secSql.append("WHERE id = ?", actorId);
		System.out.println("sql : " + secSql);
		
		DBUtil.update(dbConn, secSql);
	}
}