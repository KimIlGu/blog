package com.sbs.java.blog.service;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.sbs.java.blog.config.Config;
import com.sbs.java.blog.dao.MemberDao;
import com.sbs.java.blog.dto.Member;
import com.sbs.java.blog.util.Util;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class MemberService extends Service {
	private MemberDao memberDao;
	private MailService mailService;
	private AttrService attrService;
	
	public MemberService(Connection dbConn, MailService mailService, AttrService attrService) {
		System.out.println("MemberService 생성자");
		memberDao = new MemberDao(dbConn);
		this.mailService = mailService;
		this.attrService = attrService;
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
	
	public int join(String loginId, String loginPw, String name, String nickname, String email) {
		int id = memberDao.join(loginId, loginPw, name, nickname, email);
		
		String authCodeOrigin = Util.getTempPassword(6);
		String authCode = Util.sha256(authCodeOrigin);
		String title = "가입을 환영합니다."; 
		String body = "<a href= \"https://kig.my.iu.gy/blog/s/member/doMailAuth?id=" + id + "&authCode=" + authCode + "&email=" + email + "\">인증하기</a> ";
		mailService.sendAuth(email, title, body, authCode);
		
		attrService.setValue("member__" + id + "__extra__emailAuthCode", authCode);
		return id;
	}
	
	public String mailAuth(int actorId, String email) {
		memberDao.mailAuth(actorId, email);
		attrService.setValue("member__" + actorId + "__extra__emailAuthed", email);
		
		return attrService.getValue("member__" + actorId + "__extra__emailAuthCode");
	}
	
	public void mailAuthRemove(int actorId) {
		attrService.remove("member", actorId, "extra", "emailAuthCode");
	}
	
	public boolean isEmailAuthed(int actorId) {
		Member member = memberDao.getMemberById(actorId);
		String authedCodeOnDB = attrService.getValue("member__" + actorId + "__extra__emailAuthed");
		
		return authedCodeOnDB.equals(member.getEmail());
	}
	
	public boolean lastPasswordChangeDate(int actorId) {
		String regDateStr = Util.getNowDateStr();
		String lastRegDateStr = attrService.getValue("member__" + actorId + "__extra__emailAuthed");
		long days = 0;
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
			Date regDate = format.parse(regDateStr);
			Date lastRegDate = format.parse(lastRegDateStr);

			long compareDate = regDate.getTime() - lastRegDate.getTime();
			days = compareDate / (24 * 60 * 60 * 1000);
			days = Math.abs(days);
		} catch (ParseException e) {
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		
		return days > 180;
	}
	
	public Member getMemberById(int id) {
		return memberDao.getMemberById(id);
	}
	
	public Member getMemberByLoginId(String loginId) {
		return memberDao.getMemberByLoginId(loginId);
	}
	
	public Member getMemberByNameAndEmail(String name, String email) {
		return memberDao.getMemberByNameAndEmail(name, email);
	}
	
	public int getMemberIdByLoginIdAndLoginPw(String loginId, String loginPw) {
		return memberDao.getMemberIdByLoginIdAndLoginPw(loginId, loginPw);
	}
	
	public boolean isNeedToChangePasswordForTemp(int actorId) {
		return attrService.getValue("member", actorId, "extra", "useTempPassword").equals("1");
	}
	
	public String genModifyPrivateAuthCode(int actorId) {
		String authCode = UUID.randomUUID().toString();
		attrService.setValue("member__" + actorId + "__extra__modifyPrivateAuthCode", authCode);

		return authCode;
	}
	
	public boolean isValidModifyPrivateAuthCode(int actorId, String authCode) {
		String authCodeOnDB = attrService.getValue("member__" + actorId + "__extra__modifyPrivateAuthCode");
		System.out.println("authCodeOnDB : " + authCodeOnDB);
		return authCodeOnDB.equals(authCode);
	}

	public void modify(int actorId, String loginPw) {
		memberDao.modify(actorId, loginPw);
		
		Member member = memberDao.getMemberById(actorId);
		attrService.setValue("member", actorId, "extra", "lastPasswordChangeDate", member.getUpdateDate());
		
		attrService.remove("member", actorId, "extra", "useTempPassword");
	}
	
	public void notifyTempLoginPw(Member member) {
		String to = member.getEmail();
		System.out.println("to : " + to);
		
		String tempPasswordOrigin = Util.getTempPassword(6);
		System.out.println("tempPasswordOrigin : " + tempPasswordOrigin);
		
		String tempPassword = Util.sha256(tempPasswordOrigin);
		System.out.println("tempPassword : " + tempPassword);
		
		modify(member.getId(), tempPassword);
		attrService.setValue("member", member.getId(), "extra", "useTempPassword", "1");
		
		String title = String.format("[%s] 임시패스워드 발송", Config.getSiteName());
		String body = String.format("<div>임시 패스워드 : %s</div>\n", tempPasswordOrigin);
		mailService.send(to, title, body);
	}
	
	public Member getMemberByIdForSession(int actorId) {
		Member member = getMemberById(actorId);
		
		boolean isNeedToChangePasswordForTemp = isNeedToChangePasswordForTemp(member.getId());
		member.getExtra().put("isNeedToChangePasswordForTemp", isNeedToChangePasswordForTemp);
		
		return member;
	}
//	public void updatePwByIdAndUuid(int id, String uuid, String email, String temporaryPw) {
//	mailService.send(email, "A 블로그 임시 비빌번호 발송 완료", "임시 비밀번호 : " + temporaryPw);
//	memberDao.updatePwByIdAndUuid(id, uuid);
//}
}