package com.sbs.java.blog.dto;

import java.util.Map;

public class Article extends Dto {
	private int cateItemId;
	private int memberId;
	private int displayStatus;
	private String title;
	private String body;
	private int hit;

	public Article(Map<String, Object> row) {
		super(row);
		System.out.println("Article 생성자");

		this.cateItemId = (int) row.get("cateItemId");
		this.memberId = (int) row.get("memberId");
		this.displayStatus = (int)row.get("displayStatus");
		this.title = (String) row.get("title");
		this.body = (String) row.get("body");
		this.hit = (int) row.get("hit");
	}

	public int getCateItemId() {
		return cateItemId;
	}

	public void setCateItemId(int cateItemId) {
		this.cateItemId = cateItemId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getDisplayStatus() {
		return displayStatus;
	}

	public void setDisplayStatus(int displayStatus) {
		this.displayStatus = displayStatus;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}
	
	public String getBodyForXTemplate() {
		return body.replaceAll("(?i)script", "<!--REPLACE:script-->");
	}
	
	@Override
	public String toString() {
		return "Article [cateItemId=" + cateItemId + ", memberId=" + memberId + ", hit=" + hit + ", title=" + title
				+ ", body=" + body + ", getId()=" + getId() + ", getRegDate()=" + getRegDate() + ", getUpdateDate()="
				+ getUpdateDate() + ", getExtra()=" + getExtra() + "]";
	}
}