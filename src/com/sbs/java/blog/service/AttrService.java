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
		
		String[] nameBits = name.split("__");
		String relTypeCode = nameBits[0];
		int relId = Integer.parseInt(nameBits[1]);
		String typeCode = nameBits[2];
		String type2Code = nameBits[3];
		
		System.out.println("relTypeCode : " + relTypeCode);
		System.out.println("relId : " + relId);
		System.out.println("typeCode : " + typeCode);
		System.out.println("type2Code : " + type2Code);
		
		return attrDao.get(relTypeCode, relId, typeCode, type2Code);
	}

	public String getValue(String name) {
		System.out.println("getValue()");
		
		String[] nameBits = name.split("__");
		String relTypeCode = nameBits[0];
		int relId = Integer.parseInt(nameBits[1]);
		String typeCode = nameBits[2];
		String type2Code = nameBits[3];
		
		System.out.println("relTypeCode : " + relTypeCode);
		System.out.println("relId : " + relId);
		System.out.println("typeCode : " + typeCode);
		System.out.println("type2Code : " + type2Code);
		
		return getValue(relTypeCode, relId, typeCode, type2Code);
	}
	
	public String getValue(String relTypeCode, int relId, String typeCode, String type2Code) {
		return attrDao.getValue(relTypeCode, relId, typeCode, type2Code);
	}
	
	public int setValue(String name, String value) {
		System.out.println("setValue()");
		
		String[] nameBits = name.split("__");
		String relTypeCode = nameBits[0];
		int relId = Integer.parseInt(nameBits[1]);
		String typeCode = nameBits[2];
		String type2Code = nameBits[3];
		
		System.out.println("relTypeCode : " + relTypeCode);
		System.out.println("relId : " + relId);
		System.out.println("typeCode : " + typeCode);
		System.out.println("type2Code : " + type2Code);

		return setValue(relTypeCode, relId, typeCode, type2Code, value);
	}
	
	public int setValue(String relTypeCode, int relId, String typeCode, String type2Code, String value) {
		return attrDao.setValue(relTypeCode, relId, typeCode, type2Code, value);
	}

	public int remove(String name) {
		System.out.println("remove()");
		
		String[] nameBits = name.split("__");
		String relTypeCode = nameBits[0];
		int relId = Integer.parseInt(nameBits[1]);
		String typeCode = nameBits[2];
		String type2Code = nameBits[3];
		
		System.out.println("relTypeCode : " + relTypeCode);
		System.out.println("relId : " + relId);
		System.out.println("typeCode : " + typeCode);
		System.out.println("type2Code : " + type2Code);

		return remove(relTypeCode, relId, typeCode, type2Code);
	}
	
	public int remove(String relTypeCode, int relId, String typeCode, String type2Code) {
		return attrDao.remove(relTypeCode, relId, typeCode, type2Code);
	}
}