package com.sbs.java.blog.controller;

import java.sql.Connection;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sbs.java.blog.dto.Member;
import com.sbs.java.blog.util.Util;

public class MemberController extends Controller {

	public MemberController(Connection dbConn, String actionMethodName, HttpServletRequest req,
			HttpServletResponse resp) {
		super(dbConn, actionMethodName, req, resp);
		System.out.println("MemberController 생성자");
	}

	@Override
	public String doAction() {
		switch (actionMethodName) {
		case "join":
			return actionJoin();
		case "login":
			return actionLogin();
		case "findLoginId":
			return actionFindLoginId();
		case "findLoginPw":
			return actionFindLoginPw();
			
		case "doJoin":
			return actionDoJoin();
		case "doLogin":
			return actionDoLogin();
		case "doLogout":
			return actionDoLogout();
			
		case "doFindLoginId":
			return actionDoFindLoginId();
		case "doFindLoginPw":
			return actionDoFindLoginPw();
			
		case "getLoginIdup":
			return actionGetLoginIdup();
		case "passwordForPrivate":
			return actionPasswordForPrivate();
		case "doPasswordForPrivate":
			return actionDoPasswordForPrivate();
		case "modifyPrivate":
			return actionModifyPrivate();
		case "doModifyPrivate":
			return actionDoModifyPrivate();
		}
		return "";
	}
	
	private String actionJoin() {
		System.out.println("doActionJoin()");
		return "member/join.jsp";
	}
	
	private String actionDoJoin() {
		System.out.println("doActionDoJoin()");
		
		String loginId = req.getParameter("loginId");
		System.out.println("loginId : " + loginId);
		
		String loginPw = req.getParameter("loginPwReal");
		System.out.println("loginPw : " + loginPw);
		
		String name = req.getParameter("name");
		System.out.println("name : " + name);
		
		String nickname = req.getParameter("nickname");
		System.out.println("nickname : " + nickname);
		
		String email = req.getParameter("email");
		System.out.println("email : " + email);
		
		boolean isJoinableLoginId = memberService.isJoinableLoginId(loginId);

		if (isJoinableLoginId == false) {
			return String
					.format("html:<script> alert('%s(은)는 이미 사용중인 아이디 입니다.'); history.back(); </script>", loginId);
		}

		boolean isJoinableNickname = memberService.isJoinableNickname(nickname);

		if (isJoinableNickname == false) {
			return String
					.format("html:<script> alert('%s(은)는 이미 사용중인 닉네임 입니다.'); history.back(); </script>", nickname);
		}

		boolean isJoinableEmail = memberService.isJoinableEmail(email);

		if (isJoinableEmail == false) {
			return String
					.format("html:<script> alert('%s(은)는 이미 사용중인 이메일 입니다.'); history.back(); </script>", email);
		}

		memberService.join(loginId, loginPw, name, nickname, email);

		return String
				.format("html:<script> alert('%s님 환영합니다.'); location.replace('../home/main'); </script>", name);
	}

	private String actionLogin() {
		System.out.println("doActionLogin()");
		return "member/login.jsp";
	}
	
	private String actionDoLogin() {
		System.out.println("doActionDoLogin()");
		
		String loginId = req.getParameter("loginId");
		System.out.println("loginId : " + loginId);
		
		String loginPw = req.getParameter("loginPwReal");
		System.out.println("loginPw : " + loginPw);
		
		int loginedMemberId = memberService.getMemberIdByLoginIdAndLoginPw(loginId, loginPw);
		System.out.println("loginedMemberId : " + loginedMemberId);
		
		if (loginedMemberId == -1) {
			return String
					.format("html:<script> alert('일치하는 정보가 없습니다.'); history.back(); </script>");
		}

		session.setAttribute("loginedMemberId", loginedMemberId);
		
		String redirectUri  = Util.getString(req, "redirectUri", "../home/main");
		System.out.println("redirectUri : " + redirectUri);
		
		return String
				.format("html:<script> alert('로그인 되었습니다.'); location.replace('" + redirectUri + "'); </script>");
	}
	
	private String actionDoLogout() {
		System.out.println("doActionDoLogout()");
		
		session.removeAttribute("loginedMemberId");
		String redirectUri  = Util.getString(req, "redirectUri", "../home/main");
		System.out.println("redirectUri : " + redirectUri);
		
		return String
				.format("html:<script> alert('로그아웃 되었습니다.'); location.replace('" + redirectUri + "'); </script>");
	}
	
	private String actionFindLoginId() {
		System.out.println("doActionFindLoginId()");
		return "member/findLoginId.jsp";
	}
	
	private String actionDoFindLoginId() {
		System.out.println("doActionDoFindLoginId()");
		
		String name = req.getParameter("name");
		System.out.println(" name : " + name);
		
		String email = req.getParameter("email");
		System.out.println(" email : " + email);

		Member member = memberService.getMemberByNameAndEmail(name, email);
		
		if (member == null) {
			return "html:<script> alert('이 이메일을 사용하는 계정이 없습니다.'); history.back(); </script>";
		}
		
		System.out.println(" member : " + member);
		
		String redirectUri = Util.getString(req, "redirectUri", "../home/main");

		return String
				.format("html:<script> alert('로그인 되었습니다.'); location.replace('" + redirectUri + "'); </script>");
	}
	
	private String actionFindLoginPw() {
		System.out.println("doActionFindLoginPw()");
		return "member/findLoginPw.jsp";
	}

	private String actionDoFindLoginPw() {
		System.out.println("doActionDoFindLoginPw()");
		
		String loginId = req.getParameter("loginId");
		System.out.println("loginId : " + loginId);
		
		String name = req.getParameter("name");
		System.out.println("name : " + name);
		
		String email = req.getParameter("email");
		System.out.println("email : " + email);
		
		Member member = memberService.getMemberByNameAndEmailAndLoginId(loginId, name, email);
		
		if (member == null) {
			return "html:<script> alert('일치하는 아이디가 없습니다.'); history.back();</script>";
		}
		
		System.out.println("member : " + member);
		
		int id = member.getId();
		System.out.println("id : " + id);
		
		String uuid = "";
		
		uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5); 
		System.out.println("uuid : " + uuid);
		
		String temporaryPw = uuid;
		System.out.println("temporaryPw : " + temporaryPw);

		memberService.updatePwByIdAndUuid(id, uuid, email, temporaryPw);

		return "html:<script> alert('임시 비밀번호를 발송하였습니다.'); location.replace('../home/main'); </script>";
	}

	private String actionGetLoginIdup() {
		System.out.println("actionGetLoginIdup()");
		
		String loginId = req.getParameter("loginId");
		System.out.println("loginId : " + loginId);

		boolean isJoinableLoginId = memberService.isJoinableLoginId(loginId);
		System.out.println("isJoinableLoginId : " + isJoinableLoginId);

		if (isJoinableLoginId) {
			System.out.println("isJoinableLoginId 조건");
			return "json:{\"msg\":\"사용할 수 있는 아이디 입니다.\", \"resultCode\": \"S-1\", \"loginId\":\"" + loginId + "\"}";
		} else {
			System.out.println("!isJoinableLoginId 조건");
			return "json:{\"msg\":\"사용할 수 없는 아이디 입니다.\", \"resultCode\": \"F-1\", \"loginId\":\"" + loginId + "\"}";
		}
	}

	private String actionPasswordForPrivate() {
		System.out.println("actionPasswordForPrivate()");
		return "member/passwordForPrivate.jsp";
	}

	private String actionDoPasswordForPrivate() {
		System.out.println("actionDoPasswordForPrivate()");
		
		String loginPw = req.getParameter("loginPwReal");
		System.out.println("loginPw : " + loginPw);

		Member loginedMember = (Member) req.getAttribute("loginedMember");
		System.out.println("loginedMember : " + loginedMember);
		
		int loginedMemberId = loginedMember.getId();
		System.out.println("loginedMemberId : " + loginedMemberId);

		if (loginedMember.getLoginPw().equals(loginPw)) {
			System.out.println("loginedMember.getLoginPw().equals(loginPw) 조건");
			String authCode = memberService.genModifyPrivateAuthCode(loginedMemberId);
			System.out.println("authCode : " + authCode);

			return String
					.format("html:<script> location.replace('modifyPrivate?authCode=" + authCode + "'); </script>");
		}
		return String
				.format("html:<script> alert('비밀번호를 다시 입력해주세요.'); history.back(); </script>");
	}

	private String actionModifyPrivate() {
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");
		System.out.println("loginedMemberId : " + loginedMemberId);
		
		String authCode = req.getParameter("authCode");
		System.out.println("authCode : " + authCode);
		
		if (memberService.isValidModifyPrivateAuthCode(loginedMemberId, authCode) == false) {
			System.out.println("memberService.isValidModifyPrivateAuthCode(loginedMemberId, authCode) == false 조건");
			return String
					.format("html:<script> alert('비밀번호를 다시 체크해주세요.'); location.replace('../member/passwordForPrivate'); </script>");
		}

		return "member/modifyPrivate.jsp";
	}

	private String actionDoModifyPrivate() {
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");
		System.out.println("loginedMemberId : " + loginedMemberId);
		
		String authCode = req.getParameter("authCode");
		System.out.println("authCode : " + authCode);

		if (memberService.isValidModifyPrivateAuthCode(loginedMemberId, authCode) == false) {
			System.out.println("memberService.isValidModifyPrivateAuthCode(loginedMemberId, authCode) == false 조건");
			return String
					.format("html:<script> alert('비밀번호를 다시 체크해주세요.'); location.replace('../member/passwordForPrivate'); </script>");
		}

		String loginPw = req.getParameter("loginPwReal");
		System.out.println("loginPw : " + loginPw);

		memberService.modify(loginedMemberId, loginPw);
		Member loginedMember = (Member) req.getAttribute("loginedMember");
		System.out.println("loginedMember : " + loginedMember);
		
		loginedMember.setLoginPw(loginPw);

		return String
				.format("html:<script> alert('개인정보가 수정되었습니다.'); location.replace('../home/main'); </script>");
	}

	@Override
	public String getControllerName() {
		return "member";
	}
}