package com.sbs.java.blog.service;

import java.sql.Connection;

import com.sbs.java.blog.dao.AttrDao;
import com.sbs.java.blog.dto.Attr;

public class AttrService extends Service {

	private AttrDao attrDao;

	public AttrService(Connection dbConn) {
		System.out.println("AttrService 생성자");
		attrDao = new AttrDao(dbConn);
	}

	public Attr get(String name) {
		System.out.println("get()");
		return attrDao.get(name);
	}

	public int setValue(String name, String value) {
		System.out.println("setValue()");
		return attrDao.setValue(name, value);
	}

	public String getValue(String name) {
		System.out.println("getValue()");
		return attrDao.getValue(name);
	}

	public int remove(String name) {
		System.out.println("remove()");
		return attrDao.remove(name);
	}
}