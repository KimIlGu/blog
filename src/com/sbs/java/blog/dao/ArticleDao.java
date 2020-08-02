package com.sbs.java.blog.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sbs.java.blog.dto.Article;
import com.sbs.java.blog.dto.ArticleReply;
import com.sbs.java.blog.dto.CateItem;
import com.sbs.java.blog.util.DBUtil;
import com.sbs.java.blog.util.SecSql;

public class ArticleDao extends Dao {
	private Connection dbConn;

	public ArticleDao(Connection dbConn) {
		System.out.println("ArticleDao 생성자");
		this.dbConn = dbConn;
	}

	public int write(int cateItemId, int memberId, String title, String body) {
		System.out.println("write()");
		SecSql sql = new SecSql();

		sql.append("INSERT INTO article");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", cateItemId = ? ", cateItemId);
		sql.append(", memberId = ? ", memberId);
		sql.append(", displayStatus = '1'");
		sql.append(", title = ? ", title);
		sql.append(", body = ? ", body);
		sql.append(", hit = '0'");
		
		System.out.println("sql : " + sql);

		return DBUtil.insert(dbConn, sql);
	}

	public Article getForPrintArticle(int id) {
		System.out.println("getForPrintArticle()");
		SecSql sql = new SecSql();

		sql.append("SELECT *, '김일구' AS extra__writer");
		sql.append("FROM article");
		sql.append("WHERE 1");
		sql.append("AND id = ?", id);
		sql.append("AND displayStatus = 1");
		
		System.out.println("sql : " + sql);

		Article article = new Article(DBUtil.selectRow(dbConn, sql));
		System.out.println("article : " + article);

		return article;
	}

	public void modifyArticle(int id, int cateItemId, String title, String body) {
		System.out.println("modifyArticle()");

		SecSql sql = new SecSql();

		sql.append("UPDATE article");
		sql.append("SET cateItemId = ?", cateItemId);
		sql.append(", title = ?", title);
		sql.append(", body = ?", body);
		sql.append("WHERE id = ?", id);
		
		System.out.println("sql : " + sql);

		DBUtil.update(dbConn, sql);
	}

	public void deleteArticle(int id) {
		System.out.println("deleteArticle()");
		SecSql sql = new SecSql();

		sql.append("DELETE FROM article");
		sql.append("WHERE id = ?", id);
		
		System.out.println("sql : " + sql);

		DBUtil.delete(dbConn, sql);
	}

	public List<CateItem> getForPrintCateItems() {
		System.out.println("getForPrintCateItems()");
		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM cateItem");
		sql.append("WHERE 1");
		sql.append("ORDER BY id ASC");
		System.out.println("sql : " + sql);

		List<Map<String, Object>> rows = DBUtil.selectRows(dbConn, sql);
		System.out.println("rows : " + rows);
		
		List<CateItem> cateItems = new ArrayList<>();

		for (Map<String, Object> row : rows) {
			cateItems.add(new CateItem(row));
			System.out.println("cateItems : " + cateItems);
		}
		return cateItems;
	}

	public CateItem getCateItem(int cateItemId) {
		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM cateItem");
		sql.append("WHERE 1");
		sql.append("AND id = ?", cateItemId);
		System.out.println("sql : " + sql);

		return new CateItem(DBUtil.selectRow(dbConn, sql));
	}

	public int getForPrintListArticlesCount(int cateItemId, String searchKeywordType, String searchKeyword) {
		System.out.println("getForPrintListArticlesCount()");

		SecSql sql = new SecSql();

		sql.append("SELECT COUNT(*) AS cnt");
		sql.append("FROM article");
		sql.append("WHERE displayStatus = 1");

		if (cateItemId != 0) {
			System.out.println("cateItemId != 0 조건");
			sql.append("AND cateItemId = ?", cateItemId);
		}

		if (searchKeywordType.equals("title") && searchKeyword.length() > 0) {
			System.out.println("searchKeywordType.equals(\"title\") && searchKeyword.length() > 0 조건");
			sql.append("AND title LIKE CONCAT('%', ?, '%')", searchKeyword);
		}
		System.out.println("sql : " + sql);

		int count = DBUtil.selectRowIntValue(dbConn, sql);
		System.out.println("count : " + count);

		return count;
	}

	public List<Article> getForPrintListArticles(int page, int itemsInAPage, int cateItemId, String searchKeywordType,
			String searchKeyword) {
		System.out.println("getForPrintListArticles()");

		SecSql sql = new SecSql();

		int limitFrom = (page - 1) * itemsInAPage;

		sql.append("SELECT *");
		sql.append("FROM article");
		sql.append("WHERE displayStatus = 1");
		if (cateItemId != 0) {
			System.out.println("cateItemId != 0 조건");
			sql.append("AND cateItemId = ?", cateItemId);
		}
		if (searchKeywordType.equals("title") && searchKeyword.length() > 0) {
			System.out.println("searchKeywordType.equals(\"title\") && searchKeyword.length() > 0 조건");
			sql.append("AND title LIKE CONCAT('%', ?, '%')", searchKeyword);
		}
		sql.append("ORDER BY id DESC");
		sql.append("LIMIT ?, ?", limitFrom, itemsInAPage);
		System.out.println("sql : " + sql);

		List<Map<String, Object>> rows = DBUtil.selectRows(dbConn, sql);
		System.out.println("rows : " + rows);

		List<Article> articles = new ArrayList<>();

		for (Map<String, Object> row : rows) {
			articles.add(new Article(row));
		}

		System.out.println("articles : " + articles);
		return articles;
	}

	public int increaseHit(int id) {
		System.out.println("increaseHit()");

		SecSql sql = SecSql.from("UPDATE article");
		sql.append("SET hit = hit + 1");
		sql.append("WHERE id = ?", id);
		
		System.out.println("sql : " + sql);

		return DBUtil.update(dbConn, sql);
	}

	public List<ArticleReply> getForPrintArticleReplies(int id, int articleId) {
		System.out.println("getForPrintArticleReplies()");
		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM articleReply");
		sql.append("WHERE displayStatus = 1");
		sql.append("AND articleId = ?", articleId);
		sql.append("ORDER BY id DESC");
		System.out.println("sql : " + sql);

		List<Map<String, Object>> rows = DBUtil.selectRows(dbConn, sql);
		System.out.println("rows : " + rows);
		
		List<ArticleReply> articleReplies = new ArrayList<>();
		for (Map<String, Object> row : rows) {
			articleReplies.add(new ArticleReply(row));
		}
		System.out.println("articleReplies : " + articleReplies);
		return articleReplies;
	}

	public int writeArticleReply(int articleId, int memberId, String body) {
		System.out.println("writeArticleReply()");

		SecSql sql = new SecSql();

		sql.append("INSERT INTO articleReply");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", articleId = ?", articleId);
		sql.append(", memberId = ?", memberId);
		sql.append(", displayStatus = '1'");
		sql.append(", body = ?", body);
		
		System.out.println("sql : " + sql);

		return DBUtil.insert(dbConn, sql);
	}

	public void replyDelete(int id) {
		System.out.println("replyDelete()");

		SecSql sql = new SecSql();

		sql.append("DELETE FROM articleReply");
		sql.append("WHERE id = ?", id);
		
		System.out.println("sql : " + sql);

		DBUtil.delete(dbConn, sql);
	}

	public ArticleReply getArticleReply(int id) {
		System.out.println("getArticleReply()");

		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM articleReply");
		sql.append("WHERE id = ?", id);
		
		System.out.println("sql : " + sql);

		ArticleReply articleReply = null;

		Map<String, Object> row = DBUtil.selectRow(dbConn, sql);
		System.out.println("row : " + row);

		articleReply = new ArticleReply(row);
		System.out.println("articleReply : " + articleReply);

		return articleReply;
	}

	public void modifyArticleReply(int replyId, String body) {
		System.out.println("modifyArticleReply()");
		SecSql sql = new SecSql();

		sql.append("UPDATE articleReply");
		sql.append("SET body = ? ", body);
		sql.append("WHERE id = ?", replyId);
		
		System.out.println("sql : " + sql);

		DBUtil.update(dbConn, sql);
	}

//	public List<ArticleReply> getArticleRepliesByArticleId(int articleId) {
//		SecSql secSql = new SecSql();
//
//		secSql.append("SELECT A.*, M.nickname AS extra__writer");
//		secSql.append("FROM articleReply AS A");
//		secSql.append("INNER JOIN `member` AS M ");
//		secSql.append("ON A.memberId = M.id ");
//		secSql.append("WHERE articleId = ? ", articleId);
//		secSql.append("ORDER BY id DESC ");
//
//		List<ArticleReply> articleReplies = new ArrayList<>();
//		List<Map<String, Object>> rows = DBUtil.selectRows(dbConn, secSql);
//		for (Map<String, Object> row : rows) {
//			articleReplies.add(new ArticleReply(row));
//		}
//
//		return articleReplies;
//	} ??
}