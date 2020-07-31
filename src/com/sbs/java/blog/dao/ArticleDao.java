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

	public int write(int cateItemId, int loginedMemberId, String title, String body) {
		System.out.println("write()");
		SecSql secSql = new SecSql();

		secSql.append("INSERT INTO article");
		secSql.append("SET regDate = NOW()");
		secSql.append(", updateDate = NOW()");
		secSql.append(", cateItemId = ? ", cateItemId);
		secSql.append(", memberId = ? ", loginedMemberId);
		secSql.append(", displayStatus = '1'");
		secSql.append(", title = ? ", title);
		secSql.append(", body = ? ", body);
		secSql.append(", hit = '0'");
		System.out.println("sql : " + secSql);

		return DBUtil.insert(dbConn, secSql);
	}

	public Article getForPrintArticle(int id) {
		System.out.println("getForPrintArticle()");
		SecSql secSql = new SecSql();

		secSql.append("SELECT *, '김일구' AS extra__writer");
		secSql.append("FROM article");
		secSql.append("WHERE 1");
		secSql.append("AND id = ?", id);
		secSql.append("AND displayStatus = 1");
		System.out.println("sql : " + secSql);

		Article article = new Article(DBUtil.selectRow(dbConn, secSql));
		System.out.println("article : " + article);

		return article;
	}

	public void modifyArticle(int id, int cateItemId, String title, String body) {
		System.out.println("modifyArticle()");

		SecSql secSql = new SecSql();

		secSql.append("UPDATE article");
		secSql.append("SET cateItemId = ?", cateItemId);
		secSql.append(", title = ?", title);
		secSql.append(", body = ?", body);
		secSql.append("WHERE id = ?", id);
		System.out.println("sql : " + secSql);

		DBUtil.update(dbConn, secSql);
	}

	public void deleteArticle(int id) {
		System.out.println("deleteArticle()");
		SecSql secSql = new SecSql();

		secSql.append("DELETE FROM article");
		secSql.append("WHERE id = ?", id);
		System.out.println("sql : " + secSql);

		DBUtil.delete(dbConn, secSql);
	}

	public List<CateItem> getForPrintCateItems() {
		System.out.println("getForPrintCateItems()");
		SecSql secSql = new SecSql();

		secSql.append("SELECT *");
		secSql.append("FROM cateItem");
		secSql.append("WHERE 1");
		secSql.append("ORDER BY id ASC");
		System.out.println("sql : " + secSql);

		List<Map<String, Object>> rows = DBUtil.selectRows(dbConn, secSql);
		System.out.println("rows : " + rows);
		
		List<CateItem> cateItems = new ArrayList<>();

		for (Map<String, Object> row : rows) {
			cateItems.add(new CateItem(row));
			System.out.println("cateItems : " + cateItems);
		}
		return cateItems;
	}

	public CateItem getCateItem(int cateItemId) {
		SecSql secSql = new SecSql();

		secSql.append("SELECT *");
		secSql.append("FROM cateItem");
		secSql.append("WHERE 1");
		secSql.append("AND id = ?", cateItemId);
		System.out.println("sql : " + secSql);

		return new CateItem(DBUtil.selectRow(dbConn, secSql));
	}

	public int getForPrintListArticlesCount(int cateItemId, String searchKeywordType, String searchKeyword) {
		System.out.println("getForPrintListArticlesCount()");

		SecSql secSql = new SecSql();

		secSql.append("SELECT COUNT(*) AS cnt");
		secSql.append("FROM article");
		secSql.append("WHERE displayStatus = 1");

		if (cateItemId != 0) {
			System.out.println("cateItemId != 0 조건");
			secSql.append("AND cateItemId = ?", cateItemId);
		}

		if (searchKeywordType.equals("title") && searchKeyword.length() > 0) {
			System.out.println("searchKeywordType.equals(\"title\") && searchKeyword.length() > 0 조건");
			secSql.append("AND title LIKE CONCAT('%', ?, '%')", searchKeyword);
		}
		System.out.println("sql : " + secSql);

		int count = DBUtil.selectRowIntValue(dbConn, secSql);
		System.out.println("count : " + count);

		return count;
	}

	public List<Article> getForPrintListArticles(int page, int itemsInAPage, int cateItemId, String searchKeywordType,
			String searchKeyword) {
		System.out.println("getForPrintListArticles()");

		SecSql secSql = new SecSql();

		int limitFrom = (page - 1) * itemsInAPage;

		secSql.append("SELECT *");
		secSql.append("FROM article");
		secSql.append("WHERE displayStatus = 1");
		if (cateItemId != 0) {
			System.out.println("cateItemId != 0 조건");
			secSql.append("AND cateItemId = ?", cateItemId);
		}
		if (searchKeywordType.equals("title") && searchKeyword.length() > 0) {
			System.out.println("searchKeywordType.equals(\"title\") && searchKeyword.length() > 0 조건");
			secSql.append("AND title LIKE CONCAT('%', ?, '%')", searchKeyword);
		}
		secSql.append("ORDER BY id DESC");
		secSql.append("LIMIT ?, ?", limitFrom, itemsInAPage);
		System.out.println("sql : " + secSql);

		List<Map<String, Object>> rows = DBUtil.selectRows(dbConn, secSql);
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

		SecSql secSql = SecSql.from("UPDATE article");
		secSql.append("SET hit = hit + 1");
		secSql.append("WHERE id = ?", id);
		System.out.println("sql : " + secSql);

		return DBUtil.update(dbConn, secSql);
	}

	public List<ArticleReply> getForPrintArticleReplies(int id, int articleId) {
		System.out.println("getForPrintArticleReplies()");
		SecSql secSql = new SecSql();

		secSql.append("SELECT *");
		secSql.append("FROM articleReply");
		secSql.append("WHERE displayStatus = 1");
		secSql.append("AND articleId = ?", articleId);
		secSql.append("ORDER BY id DESC");
		System.out.println("sql : " + secSql);

		List<Map<String, Object>> rows = DBUtil.selectRows(dbConn, secSql);
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

		SecSql secSql = new SecSql();

		secSql.append("INSERT INTO articleReply");
		secSql.append("SET regDate = NOW()");
		secSql.append(", updateDate = NOW()");
		secSql.append(", articleId = ?", articleId);
		secSql.append(", memberId = ?", memberId);
		secSql.append(", displayStatus = '1'");
		secSql.append(", body = ?", body);
		System.out.println("sql : " + secSql);

		return DBUtil.insert(dbConn, secSql);
	}

	public void replyDelete(int id) {
		System.out.println("replyDelete()");

		SecSql secSql = new SecSql();

		secSql.append("DELETE FROM articleReply");
		secSql.append("WHERE id = ?", id);
		System.out.println("sql : " + secSql);

		DBUtil.delete(dbConn, secSql);
	}

	public ArticleReply getArticleReply(int id) {
		System.out.println("getArticleReply()");

		SecSql secSql = new SecSql();

		secSql.append("SELECT *");
		secSql.append("FROM articleReply");
		secSql.append("WHERE id = ?", id);
		System.out.println("sql : " + secSql);

		ArticleReply articleReply = null;

		Map<String, Object> row = DBUtil.selectRow(dbConn, secSql);
		System.out.println("row : " + row);

		articleReply = new ArticleReply(row);
		System.out.println("articleReply : " + articleReply);

		return articleReply;
	}

	public void modifyArticleReply(int replyId, String body) {
		System.out.println("modifyArticleReply()");
		SecSql secSql = new SecSql();

		secSql.append("UPDATE articleReply");
		secSql.append("SET body = ? ", body);
		secSql.append("WHERE id = ?", replyId);
		System.out.println("sql : " + secSql);

		DBUtil.update(dbConn, secSql);
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