package com.sbs.java.blog.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Util {
	public static String getUriEncoded(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}
	
	public static String getString(HttpServletRequest req, String paramName, String elseValue) {
		if (req.getParameter(paramName) == null) {
			System.out.println("req.getParameter(paramName) == null 조건");
			return elseValue;
		}

		if (req.getParameter(paramName).trim().length() == 0) {
			System.out.println("req.getParameter(paramName).trim().length() == 0 조건");
			return elseValue;
		}
		return getString(req, paramName);
	}

	public static String getString(HttpServletRequest req, String paramName) {
		System.out.println("req.getParameter(paramName) : " + req.getParameter(paramName));
		return req.getParameter(paramName);
	}
	
	public static int sendMail(String smtpServerId, String smtpServerPw, String from, String fromName, String to,
			String title, String body) {
		System.out.println("sendMail()");

		Properties prop = System.getProperties();
		System.out.println("prop : " + prop);

		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.port", "587");

		System.out.println("prop : " + prop);

		Authenticator auth = new MailAuth(smtpServerId, smtpServerPw);
		System.out.println("auth : " + auth);

		Session session = Session.getDefaultInstance(prop, auth);
		System.out.println("session : " + session);

		MimeMessage msg = new MimeMessage(session);
		System.out.println("msg : " + msg);

		try {
			msg.setSentDate(new Date());
			System.out.println("msg.setSentDate : " + msg);

			msg.setFrom(new InternetAddress(from, fromName));
			System.out.println("msg.setFrom : " + msg);

			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			System.out.println("msg.setRecipient : " + msg);

			msg.setSubject(title, "UTF-8");
			System.out.println("msg.setSubject : " + msg);

			msg.setText(body, "UTF-8");
			System.out.println("msg.setText : " + msg);

			Transport.send(msg);

		} catch (AddressException ae) {
			System.out.println("AddressException : " + ae.getMessage());
			return -1;
		} catch (MessagingException me) {
			System.out.println("MessagingException : " + me.getMessage());
			return -2;
		} catch (UnsupportedEncodingException e) {
			System.out.println("UnsupportedEncodingException : " + e.getMessage());
			return -3;
		}
		return 1;
	}

	public static boolean empty(HttpServletRequest req, String paramName) {
		System.out.println("Util.empty(req, \"id\") 실행");

		System.out.println("Util.empty(req, \"id\") paramName : " + paramName);
		String paramValue = req.getParameter(paramName);
		System.out.println("Util.empty(req, \"id\") paramValue : " + paramValue);

		return empty(paramValue);
	}

	public static boolean empty(Object obj) {
		System.out.println("Util.empty(obj) 실행");

		if (obj == null) {
			System.out.println("Util.empty(obj) obj == null 조건 실행");
			return true;
		}

		if (obj instanceof String) {
			System.out.println("Util.empty(obj) obj instanceof String 조건 실행");
			return ((String) obj).trim().length() == 0;
		}

		System.out.println("Util.empty(obj) 그 외의 조건 실행");
		return true;
	}

	public static boolean isNum(HttpServletRequest req, String paramName) {
		System.out.println("Util.isNum(req, \"id\") 실행");

		System.out.println("Util.isNum(req, \"id\") paramName : " + paramName);
		String paramValue = req.getParameter(paramName);
		System.out.println("Util.isNum(req, \"id\") paramValue : " + paramValue);

		return isNum(paramValue);
	}

	public static boolean isNum(Object obj) {
		System.out.println("Util.isNum(obj) 실행");

		if (obj == null) {
			System.out.println("Util.isNum(obj) obj == null 조건 실행");
			return false;
		}

		if (obj instanceof Long) {
			System.out.println("Util.isNum(obj) obj instanceof Long 조건 실행");
			return true;
		} else if (obj instanceof Integer) {
			System.out.println("Util.isNum(obj) obj instanceof Integer 조건 실행");
			return true;
		} else if (obj instanceof String) {
			System.out.println("Util.isNum(obj) obj instanceof String 조건 실행");
			try {
				Integer.parseInt((String) obj);
				System.out.println("Util.isNum(obj) true");
				return true;
			} catch (NumberFormatException e) {
				System.out.println("Util.isNum(obj) false");
				return false;
			}
		}

		System.out.println("Util.isNum(obj) false");
		return false;
	}

	public static int getInt(HttpServletRequest req, String paramName) {
		System.out.println("getInt()");
		return Integer.parseInt(req.getParameter(paramName));
	}

	public static void printEx(String errName, HttpServletResponse resp, Exception e) {
		try {
			resp.getWriter()
					.append("<h1 style='color:red; font-weight:bold; text-align:left;'>[에러 : " + errName + "]</h1>");

			resp.getWriter().append("<pre style='text-align:left; font-weight:bold; font-size:1.3rem;'>");
			e.printStackTrace(resp.getWriter());
			resp.getWriter().append("</pre>");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static boolean isSuccess(Map<String, Object> rs) {
		System.out.println("isSuccess()");
		System.out.println("rs.get(\"resultCode\") : " + ((String) rs.get("resultCode")));
		return ((String) rs.get("resultCode")).startsWith("S-");
	}

	public static String sha256(String msg) throws NoSuchAlgorithmException {
		System.out.println("Util.sha256() 실행");

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		System.out.println("Util.sha256() md : " + md);

		md.update(msg.getBytes());
		System.out.println("Util.sha256() msg.getBytes() : " + msg.getBytes());
		System.out.println("Util.sha256() md.update(msg.getBytes()) : " + md);

		System.out.println("Util.sha256() md.digest : " + md.digest());
		System.out.println("Util.sha256() bytesToHex(md.digest) : " + bytesToHex(md.digest()));
		return bytesToHex(md.digest());
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (byte b : bytes) {
			builder.append(String.format("%02x", b));
			System.out.println("Util.bytesToHex() builder.append(String.format(\"%02x\", b)) : "
					+ builder.append(String.format("%02x", b)));
		}
		System.out.println("Util.bytesToHex() builder.toString() : " + builder.toString());
		return builder.toString();
	}

	public static String getNewUriAndEncoded(String uri, String paramName, String pramValue) {
		System.out.println("Util.getNewUriAndEncoded() uri : " + uri);
		System.out.println("Util.getNewUriAndEncoded() paramName : " + paramName);
		System.out.println("Util.getNewUriAndEncoded() pramValue : " + pramValue);
		System.out.println("Util.getNewUriAndEncoded() getUriEncoded(getNewUri(uri, paramName, paramValue)) : "
				+ getUriEncoded(getNewUri(uri, paramName, pramValue)));
		return getUriEncoded(getNewUri(uri, paramName, pramValue));
	}

	// ArticleController 댓글 작성, 댓글 수정
	public static String getNewUri(String uri, String paramName, String paramValue) {
		System.out.println("getNewUri()");
		System.out.println("uri : " + uri);
		System.out.println("paramName : " + paramName);
		System.out.println("paramValue : " + paramValue);

		uri = getNewUriRemoved(uri, paramName);

		if (uri.contains("?")) {
			uri += "&" + paramName + "=" + paramValue;
			System.out.println("Util.getNewUri() ? uri : " + uri);
		} else {
			uri += "?" + paramName + "=" + paramValue;
			System.out.println("Util.getNewUri() not ? uri : " + uri);
		}

		uri = uri.replace("?&", "?");
		System.out.println("Util.getNewUri() uri : " + uri);
		return uri;
	}

	public static String getNewUriRemoved(String uri, String paramName) {
		System.out.println("getNewUriRemoved()");
		System.out.println("uri : " + uri);
		System.out.println("paramName : " + paramName);

		String deleteStrStarts = paramName + "=";
		System.out.println("deleteStrStarts : " + deleteStrStarts);

		int delStartPos = uri.indexOf(deleteStrStarts);

		if (delStartPos != -1) {
			System.out.println("delStartPos != -1 조건");
			int delEndPos = uri.indexOf("&", delStartPos);

			if (delEndPos != -1) {
				System.out.println("delEndPos != -1 조건");
				delEndPos++;
				uri = uri.substring(0, delStartPos) + uri.substring(delEndPos, uri.length());
			} else {
				uri = uri.substring(0, delStartPos);
			}
			System.out.println("delStartPos : " + delStartPos);
			System.out.println("delEndPos : " + delEndPos);
			System.out.println("uri : " + uri);
		}

		if (uri.charAt(uri.length() - 1) == '?') {
			System.out.println("uri.charAt(uri.length() - 1) == '?' 조건");
			uri = uri.substring(0, uri.length() - 1);
		}

		if (uri.charAt(uri.length() - 1) == '&') {
			System.out.println("uri.charAt(uri.length() - 1) == '&' 조건");
			uri = uri.substring(0, uri.length() - 1);
		}
		System.out.println("uri : " + uri);
		return uri;
	}
}