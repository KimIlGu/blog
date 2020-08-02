package com.sbs.java.blog.controller;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sbs.java.blog.dto.Member;
import com.sbs.java.blog.util.Util;

public class MemberController extends Controller {

	public MemberController(Connection dbConn, String actionMethodName, HttpServletRequest req,
			HttpServletResponse resp) {
		super(dbConn, actionMethodName, req, resp);
	}

	@Override
	public String doAction() {
		switch (actionMethodName) {
		case "join":
			return actionJoin();
		case "doJoin":
			return actionDoJoin();
			
		case "login":
			return actionLogin();
		case "doLogin":
			return actionDoLogin();
		case "doLogout":
			return actionDoLogout();
			
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
		
		case "findAccount":
			return actionFindAccount();
		
		case "findLoginId":
			return actionFindLoginId();
		case "findLoginPw":
			return actionFindLoginPw();
		case "doFindLoginId":
			return actionDoFindLoginId();
		case "doFindLoginPw":
			return actionDoFindLoginPw();
			
		case "doMailAuth" :
			return actionDoMailAuth();
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
		
		return String.format("html:<script> alert('인증메일이 %s(으)로 전송되었습니다.'); location.replace('login'); </script>", email);
	}
	
	private String actionDoMailAuth() {
		// 코드로 유효성 체크함
		int id = Integer.parseInt(req.getParameter("id"));
		String authCode = req.getParameter("authCode");
		String email = req.getParameter("email");
		
		String authedCode = memberService.mailAuth(id, email);
		
		if (authedCode.equals(authCode)) {
			memberService.mailAuthRemove(id);
			return "html:<script> alert('인증이 완료됐습니다.');location.replace('../member/login'); </script>";
		}
		return "html:<script> alert('인증코드가 일치하지 않아 인증에 실패하였습니다.'); history.back(); </script>";
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
		
		boolean isEmailAuthed = memberService.isEmailAuthed(loginedMemberId);
		
		if (isEmailAuthed == false) {
			return String
					.format("html:<script> alert('로그인은 메일 인증이 필요합니다.'); history.back(); </script>");
		}
		
		session.setAttribute("loginedMemberId", loginedMemberId);
		
		boolean isNeedToChangePasswordForTemp = memberService.isNeedToChangePasswordForTemp(loginedMemberId);
		
		String redirectUri  = Util.getString(req, "redirectUri", "../home/main");
		System.out.println("redirectUri : " + redirectUri);
		
		req.setAttribute("jsAlertMsg", "로그인 되었습니다.");
		
		if ( isNeedToChangePasswordForTemp ) {
			req.setAttribute("jsAlertMsg2", "현재 임시패스워드를 사용중입니다. 비밀번호를 변경해주세요.");	
		}
		
		boolean isValidPasswordChangeDate = memberService.lastPasswordChangeDate(loginedMemberId);
		
		if (isValidPasswordChangeDate) {
			req.setAttribute("jsAlertMsg2", "비밀번호는 6개월 마다 변경하시는 것이 안전합니다.");	
		}
		
		req.setAttribute("redirectUri", redirectUri);
		
		return "common/data.jsp";
	}
	
	private String actionDoLogout() {
		System.out.println("doActionDoLogout()");
		
		session.removeAttribute("loginedMemberId");
		String redirectUri  = Util.getString(req, "redirectUri", "../home/main");
		System.out.println("redirectUri : " + redirectUri);
		
		return String
				.format("html:<script> alert('로그아웃 되었습니다.'); location.replace('" + redirectUri + "'); </script>");
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
	
	// 해커에게 방지하기 위해 비밀번호 입력창에서 코드를 하나 생성해서 저장하고, 가져와서 일치하는지 비교
	private String actionModifyPrivate() {
		System.out.println("actionModifyPrivate()");
		
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
		System.out.println("actionDoModifyPrivate()");
		
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
	
	private String actionFindAccount() {
		return "member/findAccount.jsp";	
	}
	
	private String actionFindLoginId() {
		System.out.println("actionFindLoginId()");
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
			req.setAttribute("jsAlertMsg", "일치하는 회원이 없습니다.");
			req.setAttribute("jsHistoryBack", true);
			return "common/data.jsp";
		}
		
		System.out.println(" member : " + member);
		
		req.setAttribute("jsAlertMsg", "일치하는 회원을 찾았습니다.\\n아이디 : " + member.getLoginId());
		req.setAttribute("jsHistoryBack", true);
		return "common/data.jsp";
	}
	
	private String actionFindLoginPw() {
		System.out.println("doActionFindLoginPw()");
		return "member/findLoginPw.jsp";
	}
	
	private String actionDoFindLoginPw() {
		System.out.println("doActionDoFindLoginPw()");
		
		String loginId = req.getParameter("loginId");
		System.out.println("loginId : " + loginId);
		
		String email = req.getParameter("email");
		System.out.println("email : " + email);
		
		Member member = memberService.getMemberByLoginId(loginId);
		
		if (member == null || member.getEmail().equals(email) == false) {
			req.setAttribute("jsAlertMsg", "일치하는 회원이 없습니다.");
			req.setAttribute("jsHistoryBack", true);
			return "common/data.jsp";
		}
		
		System.out.println("member : " + member);
		
		memberService.notifyTempLoginPw(member);
		req.setAttribute("jsAlertMsg", "메일로 임시패스워드가 발송되었습니다.");
		req.setAttribute("redirectUri", "../member/login");

		return "common/data.jsp";
	}

	@Override
	public String getControllerName() {
		return "member";
	}
}