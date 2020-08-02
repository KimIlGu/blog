package com.sbs.java.blog.service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sbs.java.blog.dao.ArticleDao;
import com.sbs.java.blog.dto.Article;
import com.sbs.java.blog.dto.ArticleReply;
import com.sbs.java.blog.dto.CateItem;
import com.sbs.java.blog.util.Util;

public class ArticleService extends Service {
	private ArticleDao articleDao;	
	
	public ArticleService(Connection dbConn) {
		System.out.println("ArticleService 생성자");
		articleDao = new ArticleDao(dbConn);
	}
	
	public int write(int cateItemId, int memberId, String title, String body) {
		return articleDao.write(cateItemId, memberId, title, body);
	}

	public Article getForPrintArticle(int id, int actorId) {
		Article article = articleDao.getForPrintArticle(id);
		updateArticleExtraDataForPrint(article, actorId);
		return article;
	}
	
	private void updateArticleExtraDataForPrint(Article article, int actorId) {
		System.out.println("updateArticleExtraDataForPrint()");
		
		boolean modifyAvailable = Util.isSuccess(getCheckRsModifyAvailable(article, actorId));
		System.out.println("modifyAvailable : " + modifyAvailable);
		
		article.getExtra().put("modifyAvailable", modifyAvailable);
		System.out.println("article.getExtra() : " + article.getExtra());
		
		boolean deleteAvailable = Util.isSuccess(getCheckRsDeleteAvailable(article, actorId));
		System.out.println("deleteAvailable : " + deleteAvailable);
		
		article.getExtra().put("deleteAvailable", deleteAvailable);
		System.out.println("article.getExtra() : " + article.getExtra());
	}
	
	public Map<String, Object> getCheckRsModifyAvailable(Article article, int actorId) {
		System.out.println("getCheckRsModifyAvailable()");
		return getCheckRsDeleteAvailable(article, actorId);
	}
	
	public Map<String, Object> getCheckRsModifyAvailable(int id, int actorId) {
		System.out.println("getCheckRsModifyAvailable()");
		return getCheckRsDeleteAvailable(id, actorId);
	}
	
	public Map<String, Object> getCheckRsDeleteAvailable(int id, int actorId) {
		System.out.println("getCheckRsDeleteAvailable()");
		
		Article article = articleDao.getForPrintArticle(id);
		System.out.println("article : " + article);

		return getCheckRsDeleteAvailable(article, actorId);
	}
	private Map<String, Object> getCheckRsDeleteAvailable(Article article, int actorId) {
		System.out.println("getCheckRsDeleteAvailable()");
		
		Map<String, Object> rs = new HashMap<>();

		if (article == null) {
			System.out.println("article == null 조건");
			rs.put("resultCode", "F-1");
			rs.put("msg", "존재하지 않는 게시물 입니다.");
			System.out.println("rs : " + rs);
			return rs;
		}

		if (article.getMemberId() != actorId) {
			System.out.println("article.getMemberId() != actorId 조건");
			rs.put("resultCode", "F-2");
			rs.put("msg", "권한이 없습니다.");
			System.out.println("rs : " + rs);
			return rs;
		}
		rs.put("resultCode", "S-1");
		rs.put("msg", "작업이 가능합니다.");
		
		System.out.println("rs : " + rs);
		return rs;
	}
	
	public void modifyArticle(int id, int cateItemId, String title, String body) {
		System.out.println("ArticleService.modifyArticle()");
		articleDao.modifyArticle(id, cateItemId, title, body);
	}
	
	public void deleteArticle(int id) {
		articleDao.deleteArticle(id);
	}

	public List<CateItem> getForPrintCateItems() {
		return articleDao.getForPrintCateItems();
	} 

	public CateItem getCateItem(int cateItemId) {
		return articleDao.getCateItem(cateItemId);
	}
	
	public int getForPrintListArticlesCount(int cateItemId, String searchKeywordType, String searchKeyword) {
		return articleDao.getForPrintListArticlesCount(cateItemId, searchKeywordType, searchKeyword);
	}
	
	public List<Article> getForPrintListArticles(int page, int itemsInAPage, int cateItemId, String searchKeywordType, String searchKeyword) {
		return articleDao.getForPrintListArticles(page, itemsInAPage, cateItemId, searchKeywordType, searchKeyword);
	}
	
	public void increaseHit(int id) {
		articleDao.increaseHit(id);
	}
	
	public List<ArticleReply> getForPrintArticleReplies(int id, int actorId, int articleId) {
		System.out.println("getForPrintArticleReplies()");
		List<ArticleReply> articleReplies = articleDao.getForPrintArticleReplies(id, articleId);

		for (ArticleReply articleReply : articleReplies) {
			updateArticleReplyExtraDataForPrint(articleReply, actorId);
		}
		return articleReplies;
	}

	private void updateArticleReplyExtraDataForPrint(ArticleReply articleReply, int actorId) {
		boolean modifyAvailable = Util.isSuccess(getReplyCheckRsModifyAvailable(articleReply, actorId));
		articleReply.getExtra().put("modifyAvailable", modifyAvailable);
		System.out.println("articleReply.getExtra() : " + articleReply.getExtra());
		
		boolean deleteAvailable = Util.isSuccess(getReplyCheckRsDeleteAvailable(articleReply, actorId));
		articleReply.getExtra().put("deleteAvailable", deleteAvailable);
		System.out.println("articleReply.getExtra() : " + articleReply.getExtra());
	}

	private Map<String, Object> getReplyCheckRsModifyAvailable(ArticleReply articleReply, int actorId) {
		return getReplyCheckRsDeleteAvailable(articleReply, actorId);
	}
	
	private Map<String, Object> getReplyCheckRsDeleteAvailable(ArticleReply articleReply, int actorId) {
		Map<String, Object> rs = new HashMap<>();

		if (articleReply == null) {
			rs.put("resultCode", "F-1");
			rs.put("msg", "존재하지 않는 댓글 입니다.");

			return rs;
		}

		if (articleReply.getMemberId() != actorId) {
			rs.put("resultCode", "F-2");
			rs.put("msg", "권한이 없습니다.");

			return rs;
		}

		rs.put("resultCode", "S-1");
		rs.put("msg", "작업이 가능합니다.");

		return rs;
	}
	
	public int writeArticleReply(int articleId, int memberId, String body) {
		return articleDao.writeArticleReply(articleId, memberId, body);
	}
	
	public Map<String, Object> getReplyCheckRsDeleteAvailable(int id, int actorId) {
		System.out.println("getReplyCheckRsDeleteAvailable()");
		
		ArticleReply articleReply = this.getArticleReply(id);
		System.out.println("articleReply : " + articleReply);
		
		return getReplyCheckRsDeleteAvailable(articleReply, actorId);
	}
	
	public ArticleReply getArticleReply(int id) {
		return articleDao.getArticleReply(id);
	}
	
	public void deleteArticleReply(int id) {
		articleDao.replyDelete(id);
	}
	
	public Map<String, Object> getReplyCheckRsModifyAvailable(int id, int actorId) {
		System.out.println("getReplyCheckRsModifyAvailable()");
		ArticleReply articleReply = getArticleReply(id);
		System.out.println("articleReply : " + articleReply);
		return getReplyCheckRsModifyAvailable(articleReply, actorId);
	}
	
	public void modifyArticleReply(int replyId, String body) {
		articleDao.modifyArticleReply(replyId, body);
	}

//	public List<ArticleReply> getArticleRepliesByArticleId(int articleId) {
//		return articleDao.getArticleRepliesByArticleId(articleId);
//	} ?

}