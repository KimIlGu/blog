package com.sbs.java.blog.service;

import java.sql.Connection;
import java.util.UUID;

import com.sbs.java.blog.dao.MemberDao;
import com.sbs.java.blog.dto.Member;

public class MemberService extends Service {
	private MailService mailService;
	private AttrService attrService;
	private MemberDao memberDao;
	
	public MemberService(Connection dbConn, MailService mailService, AttrService attrService) {
		System.out.println("MemberService 생성자");
		this.mailService = mailService;
		this.attrService = attrService;
		memberDao = new MemberDao(dbConn);
	}
	
	public Member getMemberById(int id) {
		return memberDao.getMemberById(id);
	}

	public int join(String loginId, String loginPw, String name, String nickname, String email) {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5); 
		mailService.send(email, "가입을 환영합니다.", "\"https://kig.my.iu.gy/member/doAuthMail?code=" + uuid);
		return memberDao.join(loginId, loginPw, name, nickname, email);
	}
	
	public int getMemberIdByLoginIdAndLoginPw(String loginId, String loginPw) {
		return memberDao.getMemberIdByLoginIdAndLoginPw(loginId, loginPw);
	}
	
	public boolean isJoinableLoginId(String loginId) {
		return memberDao.isJoinableLoginId(loginId);
	}

	public boolean isJoinableNickname(String nickname) {
		return memberDao.isJoinableNickname(nickname);
	}

	public boolean isJoinableEmail(String email) {
		return memberDao.isJoinableEmail(email);
	}

	public Member getMemberByNameAndEmail(String name, String email) {
		return memberDao.getMemberByNameAndEmail(name, email);
	}

	public Member getMemberByNameAndEmailAndLoginId(String loginId, String name, String email) {
		return memberDao.getMemberByNameAndEmailAndLoginId(loginId, name, email);
	}

	public void updatePwByIdAndUuid(int id, String uuid, String email, String temporaryPw) {
		mailService.send(email, "A 블로그 임시 비빌번호 발송 완료", "임시 비밀번호 : " + temporaryPw);
		memberDao.updatePwByIdAndUuid(id, uuid);
	}

	public String genModifyPrivateAuthCode(int actorId) {
		String authCode = UUID.randomUUID().toString();
		attrService.setValue("member__" + actorId + "__extra__modifyPrivateAuthCode", authCode);

		return authCode;
	}

	public boolean isValidModifyPrivateAuthCode(int actorId, String authCode) {
		String authCodeOnDB = attrService.getValue("member__" + actorId + "__extra__modifyPrivateAuthCode");
		return authCodeOnDB.equals(authCode);
	}

	public void modify(int actorId, String loginPw) {
		memberDao.modify(actorId, loginPw);
	}
}