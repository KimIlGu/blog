package com.sbs.java.blog.dto;

import java.util.Map;

public class CateItem extends Dto {
	private String name;

	public CateItem(Map<String, Object> row) {
		super(row);
		System.out.println("CateItem 생성자");
		this.name = (String) row.get("name");
		System.out.println("name : " + name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "CateItem [name=" + name + ", getId()=" + getId() + ", getRegDate()=" + getRegDate()
				+ ", getUpdateDate()=" + getUpdateDate() + ", getExtra()=" + getExtra() + "]";
	}

}