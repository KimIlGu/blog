package com.sbs.java.blog.service;

import com.sbs.java.blog.util.Util;

public class MailService {
	private String mailId;
	private String mailPw;
	private String from;
	private String fromName;

	public MailService(String mailId, String mailPw, String from, String fromName) {
		System.out.println("MailService 생성자");
		this.mailId = mailId;
		this.mailPw = mailPw;
		this.from = from;
		this.fromName = fromName;
	}
	
	public void send(String to, String title, String body) {
		System.out.println("send()");
		int rs = Util.sendMail(mailId, mailPw, from, fromName, to, title, body);
		System.out.println("rs : " + rs);
	}
}