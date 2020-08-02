package com.sbs.java.blog.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

	public static boolean empty(HttpServletRequest req, String paramName) {
		System.out.println("empty()");

		String paramValue = req.getParameter(paramName);
		System.out.println("paramValue : " + paramValue);

		return empty(paramValue);
	}

	public static boolean empty(Object obj) {
		System.out.println("empty()");

		if (obj == null) {
			System.out.println("obj == null 조건");
			return true;
		}

		if (obj instanceof String) {
			System.out.println("obj instanceof String 조건");
			return ((String) obj).trim().length() == 0;
		}
		System.out.println("그 외");
		return true;
	}

	public static boolean isNum(HttpServletRequest req, String paramName) {
		System.out.println("isNum()");

		String paramValue = req.getParameter(paramName);
		System.out.println("paramValue : " + paramValue);

		return isNum(paramValue);
	}

	public static boolean isNum(Object obj) {
		System.out.println("isNum()");

		if (obj == null) {
			System.out.println("obj == null 조건");
			return false;
		}

		if (obj instanceof Long) {
			System.out.println("obj instanceof Long 조건");
			return true;
		} else if (obj instanceof Integer) {
			System.out.println("obj instanceof Integer 조건");
			return true;
		} else if (obj instanceof String) {
			System.out.println("obj instanceof String 조건");
			try {
				Integer.parseInt((String) obj);
				System.out.println("true");
				return true;
			} catch (NumberFormatException e) {
				System.out.println("false");
				return false;
			}
		}
		System.out.println("false");
		return false;
	}

	public static int getInt(HttpServletRequest req, String paramName) {
		System.out.println("getInt()");
		return Integer.parseInt(req.getParameter(paramName));
	}

	public static void printEx(String errName, HttpServletResponse resp, Exception e) {
		System.out.println("printEx()");
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

	public static String sha256(String base) {
		System.out.println("sha256()");

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			System.out.println("digest : " + digest);
			
			byte[] hash = digest.digest(base.getBytes("UTF-8"));
			System.out.println("hash.length : " + hash.length);
			
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				System.out.println("hex : " + hex);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			System.out.println("hexString : " + hexString);
			return hexString.toString();

		} catch (Exception ex) {
			System.out.println("Exception 예외");
			return "";
		}
		
	}
	
	public static String getNewUri(String uri, String paramName, String paramValue) {
		System.out.println("getNewUri()");
		System.out.println("uri : " + uri);
		System.out.println("paramName : " + paramName);
		System.out.println("paramValue : " + paramValue);

		uri = getNewUriRemoved(uri, paramName);

		if (uri.contains("?")) {
			System.out.println("uri.contains(\"?\") 조건");
			uri += "&" + paramName + "=" + paramValue;
		} else {
			System.out.println("!uri.contains(\"?\") 조건");
			uri += "?" + paramName + "=" + paramValue;
		}
		System.out.println("uri : " + uri);
		
		uri = uri.replace("?&", "?");
		System.out.println("uri : " + uri);
		return uri;
	}

	public static String getNewUriRemoved(String uri, String paramName) {
		System.out.println("getNewUriRemoved()");
		System.out.println("uri : " + uri);
		System.out.println("paramName : " + paramName);

		String deleteStrStarts = paramName + "=";
		System.out.println("deleteStrStarts : " + deleteStrStarts);

		int delStartPos = uri.indexOf(deleteStrStarts);
		System.out.println("delStartPos : " + delStartPos);

		if (delStartPos != -1) {
			System.out.println("delStartPos != -1 조건");
			int delEndPos = uri.indexOf("&", delStartPos);
			System.out.println("delEndPos : " + delEndPos);

			if (delEndPos != -1) {
				System.out.println("delEndPos != -1 조건");
				delEndPos++;
				uri = uri.substring(0, delStartPos) + uri.substring(delEndPos, uri.length());
			} else {
				System.out.println("delEndPos == -1 조건");
				uri = uri.substring(0, delStartPos);
			}
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
	
	public static int sendMail(String smtpServerId, String smtpServerPw, String from, String fromName, String to,
			String title, String body) {
		System.out.println("sendMail()");
		
		System.out.println("smtpServerId : " + smtpServerId);
		System.out.println("smtpServerPw : " + smtpServerPw);
		System.out.println("from : " + from);
		System.out.println("fromName : " + fromName);
		System.out.println("to : " + to);
		System.out.println("title : " + title);
		System.out.println("body : " + body);

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
			
			msg.setContent(body, "text/html; charset=UTF-8");
			System.out.println("msg.setContent : " + msg);
			
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
	
	public static int sendMailAuth(String smtpServerId, String smtpServerPw, String from, String fromName, String to, String title,
			String body, String authCode) {
		System.out.println("sendMailAuth()");
		
		System.out.println("smtpServerId : " + smtpServerId);
		System.out.println("smtpServerPw : " + smtpServerPw);
		System.out.println("from : " + from);
		System.out.println("fromName : " + fromName);
		System.out.println("to : " + to);
		System.out.println("title : " + title);
		System.out.println("body : " + body);
		System.out.println("authCode : " + authCode);

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
			
			msg.setContent(body, "text/html; charset=UTF-8");
			System.out.println("msg.setContent : " + msg);
			
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
	
	public static String getTempPassword(int length) {
		System.out.println("getTempPassword()");
		
		int index = 0;
		char[] charArr = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
				'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
				'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
				'w', 'x', 'y', 'z' };

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < length; i++) {
			index = (int) (charArr.length * Math.random());
			sb.append(charArr[index]);
		}
		System.out.println("sb : " + sb);
		return sb.toString();
	}
	
	public static String getNowDateStr() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = date.format(cal.getTime());
		return dateStr;
	}
	
//	public static String bytesToHex(byte[] bytes) {
//		System.out.println("bytesToHex()");
//		StringBuilder builder = new StringBuilder();
//		for (byte b : bytes) {
//			System.out.println("b : " + b);
//			builder.append(String.format("%02x", b));
//		}
//		System.out.println("builder.toString() : " + builder.toString());
//		return builder.toString();
//	}
//
//	public static String getNewUriAndEncoded(String uri, String paramName, String pramValue) {
//		System.out.println("getNewUriAndEncoded()");
//		System.out.println("uri : " + uri);
//		System.out.println("paramName : " + paramName);
//		System.out.println("pramValue : " + pramValue);
//		System.out.println("getUriEncoded(getNewUri(uri, paramName, paramValue)) : "
//				+ getUriEncoded(getNewUri(uri, paramName, pramValue)));
//		return getUriEncoded(getNewUri(uri, paramName, pramValue));
//	}
}