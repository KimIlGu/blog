package com.sbs.java.blog.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sbs.java.blog.config.Config;
import com.sbs.java.blog.controller.ArticleController;
import com.sbs.java.blog.controller.Controller;
import com.sbs.java.blog.controller.HomeController;
import com.sbs.java.blog.controller.MemberController;
import com.sbs.java.blog.controller.TestController;
import com.sbs.java.blog.exception.SQLErrorException;
import com.sbs.java.blog.util.Util;

public class App {
	private HttpServletRequest req;
	private HttpServletResponse resp;

	public App(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("App 생성자");
		this.req = req;
		this.resp = resp;
	}

	public void start() throws ServletException, IOException {
		System.out.println("start()");
		
		// Config 구성
		if ( req.getServletContext().getInitParameter("gmailId") != null ) {
			System.out.println("req.getServletContext().getInitParameter(\"gmailId\") != null 조건 실행");
			Config.gmailId = (String) req.getServletContext().getInitParameter("gmailId");	
		}
		
		if ( req.getServletContext().getInitParameter("gmailPw") != null ) {
			System.out.println("req.getServletContext().getInitParameter(\"gmailPw\") != null 조건 실행");
			Config.gmailPw = (String) req.getServletContext().getInitParameter("gmailPw");
		}
		
		// DB 드라이버 로딩
		loadDbDriver();
		System.out.println("loadDbDriver()");

		// DB 접속정보 세팅
		String url = getDbUrl();
		System.out.println("url : " + url);
		
		String user = getDbId();
		System.out.println("user : " + user);
		
		String password = getDbPassword();
		System.out.println("password : " + password);

		Connection dbConn = null;

		try {
			// DB 접속 성공
			dbConn = DriverManager.getConnection(url, user, password);
			System.out.println("DriverManager.getConnection(url, user, password)");
			
			// 올바른 컨트롤러로 라우팅
			route(dbConn, req, resp);
			
		} catch (SQLException e) {
			Util.printEx("SQL 예외(커넥션 열기)", resp, e);
		} catch (SQLErrorException e) {
			Util.printEx(e.getMessage(), resp, e.getOrigin());
		} catch (Exception e) {
			Util.printEx("기타 예외", resp, e);
		} finally {
			if (dbConn != null) {
				try {
					dbConn.close();
					System.out.println("dbConn.close()");
				} catch (SQLException e) {
					Util.printEx("SQL 예외(커넥션 닫기)", resp, e);
				}
			}
		}
	}
	
	private void loadDbDriver() throws IOException {
		// DB 커넥터 로딩 시작
		String driverName = "com.mysql.cj.jdbc.Driver";

		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			System.err.printf("[ClassNotFoundException 예외, %s]\n", e.getMessage());
			resp.getWriter().append("DB 드라이버 클래스 로딩 실패");
			return;
		}
		// DB 커넥터 로딩 성공
	}

	private String getDbUrl() {
		return "jdbc:mysql://site22.iu.gy:3306/site22?serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeBehavior=convertToNull";
	}
	
	private String getDbId() {
		return "site22";
	}

	private String getDbPassword() {
		return "sbs123414";
	}

	private void route(Connection dbConn, HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		System.out.println("route()");
		
		resp.setContentType("text/html; charset=UTF-8");
		System.out.println("req.setContentType(\"text/html; UTF-8\") : 응답 브라우저마다 html 번역기가 다르기 때문");
		
		String contextPath = req.getContextPath();
		System.out.println("contextPath : " + contextPath);
		
		String requestURI = req.getRequestURI();
		System.out.println("requestURI : " + requestURI);
		
		String actionStr = requestURI.replace(contextPath + "/s/", "");
		System.out.println("actionStr : " + actionStr);
		
		String[] actionStrBits = actionStr.split("/");
		System.out.println("actionStrBits.length : " + actionStrBits.length);
		
		String controllerName = actionStrBits[0];
		System.out.println("controllerName : " + controllerName);
		
		String actionMethodName = actionStrBits[1];
		System.out.println("actionMethodName : " + actionMethodName);
		
		Controller controller = null;

		switch (controllerName) {
		case "home":
			controller = new HomeController(dbConn, actionMethodName, req, resp);
			break;
		case "member":
			controller = new MemberController(dbConn, actionMethodName, req, resp);		
			break;
		case "article":
			controller = new ArticleController(dbConn, actionMethodName, req, resp);
			break;
		case "test":
			controller = new TestController(dbConn, actionMethodName, req, resp);
			break;
		}

		if (controller != null) {
			System.out.println("controller != null 조건");
			
			String actionResult = controller.executeAction();
			System.out.println("actionResult : " + actionResult);
			if (actionResult.equals("")) {
				resp.getWriter().append("액션의 결과가 없습니다.");
			} else if (actionResult.endsWith(".jsp")) {
				String viewPath = "/jsp/" + actionResult;
				req.getRequestDispatcher(viewPath).forward(req, resp);
			} else if (actionResult.startsWith("html:")) {
				resp.getWriter().append(actionResult.substring(5));
			} else if (actionResult.startsWith("json:")) {
				resp.setContentType("application/json");
				resp.getWriter().append(actionResult.substring(5));
			} else {
				resp.getWriter().append("처리할 수 없는 액션결과입니다.");
			}
		} else {
			resp.getWriter().append("존재하지 않는 페이지 입니다.");
		}
	}
}