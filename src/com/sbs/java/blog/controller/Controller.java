package com.sbs.java.blog.controller;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sbs.java.blog.config.Config;
import com.sbs.java.blog.dto.CateItem;
import com.sbs.java.blog.dto.Member;
import com.sbs.java.blog.service.ArticleService;
import com.sbs.java.blog.service.AttrService;
import com.sbs.java.blog.service.MailService;
import com.sbs.java.blog.service.MemberService;
import com.sbs.java.blog.util.Util;

public abstract class Controller {

	protected Connection dbConn;
	protected String actionMethodName;
	protected HttpServletRequest req;
	protected HttpServletResponse resp;
	protected HttpSession session;
	
	protected ArticleService articleService;
	protected MemberService memberService;
	protected MailService mailService;
	protected AttrService attrService;

	public Controller(Connection dbConn, String actionMethodName, HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("Controller 생성자");
		
		this.dbConn = dbConn;
		this.actionMethodName = actionMethodName;
		this.req = req;
		this.resp = resp;
		this.session = req.getSession();
		
		articleService = new ArticleService(dbConn);
		mailService = new MailService(Config.gmailId, Config.gmailPw, Config.mailFrom, Config.mailFromName);
		attrService = new AttrService(dbConn);
		memberService = new MemberService(dbConn, mailService, attrService);
	}
	
	public abstract String getControllerName();

	public void beforeAction() {
		System.out.println("beforeAction()");
		// 액션 전 실행
		// 이 메서드는 모든 컨트롤러의 모든 액션이 실행되기 전에 실행된다.
		List<CateItem> cateItems = articleService.getForPrintCateItems();
		System.out.println("cateItems : " + cateItems);

		req.setAttribute("cateItems", cateItems);
		
		int loginedMemberId = -1;
		boolean isLogined = false;
		Member loginedMember = null;

		if (session.getAttribute("loginedMemberId") != null) {
			System.out.println("session.getAttribute(\"loginedMemberId\") != null 조건");
			
			loginedMemberId = (int) session.getAttribute("loginedMemberId");
			isLogined = true;
			loginedMember = memberService.getMemberById(loginedMemberId);
		}
		
		System.out.println("loginedMemberId : " + loginedMemberId);
		System.out.println("isLogined : " + isLogined);
		System.out.println("loginedMember : " + loginedMember);
		
		req.setAttribute("loginedMemberId", loginedMemberId);
		req.setAttribute("isLogined", isLogined);
		req.setAttribute("loginedMember", loginedMember);
		
		// 현재 URL
		String currentUri = req.getRequestURI();
		
		if (req.getQueryString() != null) {
			System.out.println("req.getQueryString() != null 조건");
			currentUri += "?" + req.getQueryString();
		}
		System.out.println("currentUri : " + currentUri);
		
		String encodedCurrentUri = Util.getUriEncoded(currentUri);
		
		// 현재 접속된 페이지와 관련된 유용한 정보 담기
		req.setAttribute("currentUri", currentUri);
		req.setAttribute("encodedCurrentUri", encodedCurrentUri);
		req.setAttribute("encodedAfterLoginRedirectUri", encodedCurrentUri);
		req.setAttribute("noBaseCurrentUri", req.getRequestURI().replace(req.getContextPath(), ""));
		
		System.out.println("currentUri : " + currentUri);
		System.out.println("encodedCurrentUri : " + encodedCurrentUri);
		System.out.println("encodedAfterLoginRedirectUri : " + encodedCurrentUri);
		System.out.println("noBaseCurrentUri : " + req.getRequestURI().replace(req.getContextPath(), ""));
		
		// 로그인 페이지에서 로그인 페이지로 이동하는 버튼을 또 누른 경우
		// 기존 afterLoginRedirectUri 정보를 유지시키기 위한 로직
		if (currentUri.contains("/s/member/login")) {
			System.out.println("currentUri.contains(\"/s/member/login\") 조건");
			
			String encodedOldAfterLoginRedirectUri = Util.getString(req, "afterLoginRedirectUrl", "");
			System.out.println("encodedOldAfterLoginRedirectUri : " + encodedOldAfterLoginRedirectUri);
			
			encodedOldAfterLoginRedirectUri = Util.getUriEncoded(encodedOldAfterLoginRedirectUri);
			System.out.println("encodedOldAfterLoginRedirectUri : " + encodedOldAfterLoginRedirectUri);
			
			req.setAttribute("encodedAfterLoginRedirectUri", encodedOldAfterLoginRedirectUri);
		}
		// 로그아웃 후 가야하는 곳, 기본적으로 현재 URL
		req.setAttribute("encodedAfterLogoutRedirectUri", encodedCurrentUri);
		System.out.println("encodedAfterLogoutRedirectUri : " + encodedCurrentUri);
	}
	
	private String doGuard() {
		boolean isLogined = (boolean) req.getAttribute("isLogined");

		// 로그인에 관련된 가드 시작
		boolean needToLogin = false;
		String controllerName = getControllerName();

		switch (controllerName) {
		case "member":
			switch (actionMethodName) {
			case "doLogout":
				needToLogin = true;
				break;
			}
			break;
			
		case "article":
			switch (actionMethodName) {
			case "write":
			case "modify":
			case "modifyReply" :
			
			case "doWrite":
			case "doModify":
			case "doDelete":
			case "doWriteReply":
			case "doModifyReply" :
			case "doDeleteReply" :
				needToLogin = true;
				break;
			}
			break;
		}
		
		String encodedAfterLoginRedirectUri  = (String) req.getAttribute("encodedAfterLoginRedirectUri");

		System.out.println("encodedAfterLoginRedirectUri : " + encodedAfterLoginRedirectUri);
		System.out.println("needToLogin : " + needToLogin);
		System.out.println("isLogined : " + isLogined);
		
		if (needToLogin && isLogined == false) {

			return "html:<script> alert('로그인 후 이용해주세요.'); location.href = '../member/login?afterLoginRedirectUrl=" + encodedAfterLoginRedirectUri  + "'; </script>";
		}
		// 로그인에 관련된 가드 끝

		// 로그아웃에 관련된 가드 시작
		boolean needToLogout = false;
		
		switch (controllerName) {
		case "member":
			switch (actionMethodName) {
			case "login":
			case "join":
				needToLogout = true;
				break;
			}
			break;
		}
		
		System.out.println("needToLogin : " + needToLogin);
		System.out.println("isLogined : " + isLogined);

		if (needToLogout && isLogined) {
			System.out.println("needToLogout && isLogined 조건");
			return "html:<script> alert('로그아웃 후 이용해주세요.'); history.back(); </script>";
		}
		// 로그아웃에 관련된 가드 끝
		System.out.println("null");
		return null;
	}
	
	public abstract String doAction();

	public void afterAction() {
		// 액션 후 실행
	}

	public String executeAction() {
		System.out.println("executeAction()");
		beforeAction();
		
		String doGuardRs = doGuard();

		if (doGuardRs != null) {
			System.out.println("doGuardRs != null 조건");
			return doGuardRs;
		}
		System.out.println("doGuardRs : " + doGuardRs);
		
		String rs = doAction();
		System.out.println("rs : " + rs);
		
		afterAction();
		
		return rs;
	}
}