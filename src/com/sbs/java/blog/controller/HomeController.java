package com.sbs.java.blog.controller;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController extends Controller {

	public HomeController(Connection dbConn, String actionMethodName, HttpServletRequest req,
			HttpServletResponse resp) {
		super(dbConn, actionMethodName, req, resp);
		System.out.println("HomeController 생성자");
	}

	@Override
	public String doAction() {
		switch (actionMethodName) {
		case "main":
			return actionMain(req, resp);
		case "aboutMe":
			return actionAboutMe(req, resp);
		}
		return "";
	}
	
	private String actionMain(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("doActionMain()");
		return "home/main.jsp";
	}

	private String actionAboutMe(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("doActionAboutMe()");
		return "home/aboutMe.jsp";
	}

	@Override
	public String getControllerName() {
		return "home";
	}
}