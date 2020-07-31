package com.sbs.java.blog.dto;

import java.util.HashMap;
import java.util.Map;

public class Dto {
	private int id;
	private String regDate;
	private String updateDate;
	private Map<String, Object> extra;

	public Dto(Map<String, Object> row) {
		System.out.println("Dto 생성자");
		
		this.id = (int)row.get("id");
		this.regDate = (String)row.get("regDate");
		this.updateDate = (String) row.get("updateDate");
		this.extra = new HashMap<>();

		for (String key : row.keySet()) {
			System.out.println("row.keySet() : " + row.keySet());
			System.out.println("key : " + key);

			if (key.startsWith("extra__")) {
				System.out.println("key.startsWith(\"extra__\" 조건");
				Object value = row.get(key);
				System.out.println("value : " + value);
				
				String extraKey = key.substring(7);
				System.out.println("extraKey : " + extraKey);
				
				this.extra.put(extraKey, value);
				System.out.println("extra : " + extra);
			}
		}
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getRegDate() {
		return regDate;
	}
	
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	
	public Map<String, Object> getExtra() {
		return extra;
	}
	
	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}
}