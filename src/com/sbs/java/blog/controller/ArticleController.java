package com.sbs.java.blog.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sbs.java.blog.dto.Article;
import com.sbs.java.blog.dto.ArticleReply;
import com.sbs.java.blog.dto.CateItem;
import com.sbs.java.blog.util.Util;

public class ArticleController extends Controller {
	
	public ArticleController(Connection dbConn, String actionMethodName, HttpServletRequest req,
			HttpServletResponse resp) {
		super(dbConn, actionMethodName, req, resp);
		System.out.println("ArticleController 생성자");
	}
	
	public void beforeAction() {
		super.beforeAction();
		// 이 메서드는 게시물 컨트롤러의 모든 액션이 실행되기 전에 실행된다.
		// 필요없다면 지워도 된다.
	}

	@Override
	public String doAction() {
		switch (actionMethodName) {
		
		case "write":
			return actionWrite();
		case "modify":
			return actionModify();	
		case "list":
			return actionList();
		case "detail":
			return actionDetail();
		case "modifyReply":
			return actionModifyReply();
		
		case "doWrite":
			return actionDoWrite();
		case "doModify":
			return actionDoModify();
		case "doDelete":
			return actionDoDelete();
		case "doWriteReply":
			return actionDoWriteReply();
		case "doModifyReply":
			return actionDoModifyReply();
		case "doDeleteReply":
			return actionDoDeleteReply();
		}
		return "";
	}

	private String actionWrite() {
		System.out.println("doActionWrite()");
		return "article/write.jsp";
	}

	private String actionDoWrite() {
		System.out.println("doActionWrite()");
		
		String title = req.getParameter("title");
		System.out.println("title : " + title);
		
		String body = req.getParameter("body");
		System.out.println("body : " + body);
		
		int cateItemId = Util.getInt(req, "cateItemId");
		System.out.println("cateItemId : " + cateItemId);
		
		int loginedMemberId = (int) session.getAttribute("loginedMemberId");
		System.out.println("loginedMemberId : " + loginedMemberId);
		
		int id = articleService.write(cateItemId, loginedMemberId, title, body);
		System.out.println("id : " + id);

		return "html:<script> alert('" + id + "번 게시물이 생성되었습니다.'); location.replace('list'); </script>";
	}
	
	private String actionModify() {
		System.out.println("doActionModify()");
		
		if (Util.empty(req, "id")) {
			return "html:id를 입력해주세요.";
		}

		if (Util.isNum(req, "id") == false) {
			return "html:id를 정수로 입력해주세요.";
		}

		int id = Util.getInt(req, "id");
		System.out.println("id : " + id);
		
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");
		System.out.println("loginedMemberId : " + loginedMemberId);
		
		Article article = articleService.getForPrintArticle(id, loginedMemberId);
		System.out.println("article : " + article);
		
		req.setAttribute("article", article);
		return "article/modify.jsp";
	}

	private String actionDoModify() {
		System.out.println("doActionDoModify()");
		
		if (Util.empty(req, "id")) {
			return "html:id를 입력해주세요.";
		}

		if (Util.isNum(req, "id") == false) {
			return "html:id를 정수로 입력해주세요.";
		}
		
		int id = Util.getInt(req, "id");
		System.out.println("id : " + id);
		
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");
		System.out.println("loginedMemberId : " + loginedMemberId);
		
		Map<String, Object> getCheckRsModifyAvailableRs = articleService.getCheckRsModifyAvailable(id, loginedMemberId);
		System.out.println("getCheckRsModifyAvailableRs : " + getCheckRsModifyAvailableRs);
		
		if (Util.isSuccess(getCheckRsModifyAvailableRs) == false) {
			System.out.println("Util.isSuccess(getCheckRsModifyAvailableRs) == false 조건");
			
			return "html:<script> alert('" + getCheckRsModifyAvailableRs.get("msg") + "'); history.back(); </script>";
		}
		
		int cateItemId = Util.getInt(req, "cateItemId");
		System.out.println("cateItemId : " + cateItemId);
		
		String title = req.getParameter("title");
		System.out.println("title : " + title);
		
		String body = req.getParameter("body");
		System.out.println("body : " + body);

		articleService.modifyArticle(id, cateItemId, title, body);
		
		return "html:<script> alert('" + id + "번 게시물이 수정되었습니다.');" + "location.replace('detail?id=" + id + "');</script>";
	}
	
	private String actionDoDelete() {
		System.out.println("doActionDoDelete()");
		
		if (Util.empty(req, "id")) {
			return "html:id를 입력해주세요.";
		}

		if (Util.isNum(req, "id") == false) {
			return "html:id를 정수로 입력해주세요.";
		}
		
		int id = Util.getInt(req, "id");
		System.out.println("id : " + id);

		int loginedMemberId = (int) req.getAttribute("loginedMemberId");
		System.out.println("loginedMemberId : " + loginedMemberId);

		Map<String, Object> getCheckRsDeleteAvailableRs = articleService.getCheckRsDeleteAvailable(id, loginedMemberId);
		System.out.println("getCheckRsDeleteAvailableRs : " + getCheckRsDeleteAvailableRs);

		if (Util.isSuccess(getCheckRsDeleteAvailableRs) == false) {
			System.out.println("Util.isSuccess(getCheckRsDeleteAvailableRs) == false 조건 실행");
			return "html:<script> alert('" + getCheckRsDeleteAvailableRs.get("msg") + "'); history.back(); </script>";
		}
		articleService.deleteArticle(id);
		return "html:<script> alert('"+ id + "번 게시물이 삭제되었습니다.'); location.replace('list'); </script>";
	}

	private String actionList() {
		System.out.println("doActionList()");
		
		int page = 1;
		if (!Util.empty(req, "page") && Util.isNum(req, "page")) {
			System.out.println("!Util.empty(req, \"page\") && Util.isNum(req, \"page\") 조건");
			page = Util.getInt(req, "page");
		}
		System.out.println("page : " + page);

		int cateItemId = 0;
		if (!Util.empty(req, "cateItemId") && Util.isNum(req, "cateItemId")) {
			cateItemId = Util.getInt(req, "cateItemId");
		}
		System.out.println("cateItemId : " + cateItemId);

		String cateItemName = "전체";
		if (cateItemId != 0) {
			System.out.println("cateItemId != 0 조건");
			
			CateItem cateItem = articleService.getCateItem(cateItemId);
			System.out.println("cateItem : " + cateItem);
			
			cateItemName = cateItem.getName();
		}
		System.out.println("cateItemName : " + cateItemName);

		req.setAttribute("cateItemName", cateItemName);

		String searchKeywordType = "";
		if (!Util.empty(req, "searchKeywordType")) {
			System.out.println("!Util.empty(req, \"searchKeywordType\" 조건 실행");
			
			searchKeywordType = Util.getString(req, "searchKeywordType");
		}
		System.out.println("searchKeywordType : " + searchKeywordType);

		String searchKeyword = "";
		if (!Util.empty(req, "searchKeyword")) {
			System.out.println("!Util.empty(req, \"searchKeyword\" 조건");
			
			searchKeyword = Util.getString(req, "searchKeyword");
		}
		System.out.println("searchKeyword : " + searchKeyword);

		int itemsInAPage = 10;
		System.out.println("itemsInAPage : " + itemsInAPage);
		
		int totalCount = articleService.getForPrintListArticlesCount(cateItemId, searchKeywordType, searchKeyword);
		System.out.println("totalCount : " + totalCount);
		
		int totalPage = (int) Math.ceil(totalCount / (double) itemsInAPage);
		System.out.println("totalPage : " + totalPage);

		req.setAttribute("totalCount", totalCount);
		req.setAttribute("totalPage", totalPage);
		req.setAttribute("page", page);

		List<Article> articles = articleService.getForPrintListArticles(page, itemsInAPage, cateItemId,
				searchKeywordType, searchKeyword);
		System.out.println("articles : " + articles);
		
		req.setAttribute("articles", articles);
		return "article/list.jsp";
	}

	private String actionDetail() {
		if (Util.empty(req, "id")) {
			return "html:id를 입력해주세요.";
		}

		if (Util.isNum(req, "id") == false) {
			return "html:id를 정수로 입력해주세요.";
		}

		int id = Util.getInt(req, "id");
		System.out.println("id : " + id);
		
		articleService.increaseHit(id);
		
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");
		System.out.println("loginedMemberId : " + loginedMemberId);
		
		Article article = articleService.getForPrintArticle(id, loginedMemberId);
		System.out.println("article : " + article);
		
		List<ArticleReply> articleReplies = articleService.getForPrintArticleReplies(id, loginedMemberId, article.getId());
		System.out.println("articleReplies : " + articleReplies);
		
		req.setAttribute("article", article);
		req.setAttribute("articleReplies", articleReplies);
		return "article/detail.jsp";
	}
	
	private String actionDoWriteReply() {
		if (Util.empty(req, "articleId")) {
			return "html:articleId를 입력해주세요.";
		}

		if (Util.isNum(req, "articleId") == false) {
			return "html:articleId를 정수로 입력해주세요.";
		}
		
		int	articleId = Util.getInt(req, "articleId");
		System.out.println("articleId : " + articleId);
		
		int loginedMemberId  = (int) req.getAttribute("loginedMemberId");
		System.out.println("loginedMemberId : " + loginedMemberId);
		
		String body = Util.getString(req, "body");
		System.out.println("body : " + body);
		
		String redirectUri  = Util.getString(req, "redirectUri");
		System.out.println("redirectUri : " + redirectUri);
		
		int id = articleService.writeArticleReply(articleId, loginedMemberId, body);
		System.out.println("id : " + id);
		
		redirectUri = Util.getNewUri(redirectUri, "lastWorkArticleReplyId", id + "");
		System.out.println("redirectUri : " + redirectUri);
		
		return "html:<script> alert('" + id + "번 댓글이 작성되었습니다.'); location.replace('" + redirectUri  +"'); </script>";
	}

	private String actionDoDeleteReply() {
		if (Util.empty(req, "id")) {
			return "html:id를 입력해주세요.";
		}

		if (Util.isNum(req, "id") == false) {
			return "html:id를 정수로 입력해주세요.";
		}
		
		int id = Util.getInt(req, "id");
		System.out.println("id : " + id);
		
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");
		System.out.println("loginedMemberId : " + loginedMemberId);
		
		Map<String, Object> getReplyCheckRsDeleteAvailableRs = articleService.getReplyCheckRsDeleteAvailable(id,
				loginedMemberId);
		System.out.println("getReplyCheckRsDeleteAvailableRs : " + getReplyCheckRsDeleteAvailableRs);
		
		if (Util.isSuccess(getReplyCheckRsDeleteAvailableRs) == false) {
			System.out.println("Util.isSuccess(getReplyCheckRsDeleteAvailableRs) == false 조건");
			System.out.println("getReplyCheckRsDeleteAvailableRs.get(\"msg\") : " + getReplyCheckRsDeleteAvailableRs.get("msg"));
			return "html:<script> alert('" + getReplyCheckRsDeleteAvailableRs.get("msg")
					+ "'); history.back(); </script>";
		}
		
		articleService.deleteArticleReply(id);
		
		String redirectUri = Util.getString(req, "redirectUri", "list");
		System.out.println("redirectUri : " + redirectUri);
		
		return "html:<script> alert('" + id + "번 댓글이 삭제되었습니다.'); location.replace('\" + redirectUri + \"'); </script>";
	}
	
	private String actionModifyReply() {
		if (Util.empty(req, "id")) {
			return "html:id를 입력해주세요.";
		}

		if (Util.isNum(req, "id") == false) {
			return "html:id를 정수로 입력해주세요.";
		}

		int id =  Util.getInt(req, "id");
		System.out.println("id : " + id);
		
		int articleId = Integer.parseInt(req.getParameter("articleId"));
		System.out.println("articleId : " + articleId);
		
		ArticleReply articleReply = articleService.getArticleReply(id);
		System.out.println("articleReply : " + articleReply);
		
		req.setAttribute("articleReply", articleReply);
		req.setAttribute("articleId", articleId);
		return "article/modifyReply.jsp";
	}
	
	private String actionDoModifyReply() {
		if (Util.empty(req, "id")) {
			return "html:id를 입력해주세요.";
		}

		if (Util.isNum(req, "id") == false) {
			return "html:id를 정수로 입력해주세요.";
		}
		
		int id = Util.getInt(req, "id");
		System.out.println("id : " + id);
		
		String body = req.getParameter("body");
		System.out.println("body : " + body);
		
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");
		System.out.println("loginedMemberId : " + loginedMemberId);
		
		Map<String, Object> getReplyCheckRsModifyAvailableRs = articleService.getReplyCheckRsModifyAvailable(id,
				loginedMemberId);
		System.out.println("getReplyCheckRsModifyAvailableRs : " + getReplyCheckRsModifyAvailableRs);
		

		if (Util.isSuccess(getReplyCheckRsModifyAvailableRs) == false) {
			System.out.println("Util.isSuccess(getReplyCheckRsModifyAvailableRs) == false 조건 실행");
			System.out.println("getReplyCheckRsModifyAvailableRs.get(\"msg\") : " + getReplyCheckRsModifyAvailableRs.get("msg"));
			return "html:<script> alert('" + getReplyCheckRsModifyAvailableRs.get("msg")
					+ "'); history.back(); </script>";
		}
		
		articleService.modifyArticleReply(id, body);
		
		String redirectUri = Util.getString(req, "redirectUri", "list");
		System.out.println("redirectUri : " + redirectUri);
		
		redirectUri = Util.getNewUri(redirectUri, "lastWorkArticleReplyId", id + "");
		System.out.println("redirectUri : " + redirectUri);
		
		return "html:<script> alert('" + id + "번 댓글이 수정되었습니다.'); location.replace('\" + redirectUri + \"'); </script>";
	}
	
	@Override
	public String getControllerName() {
		return "article";
	}
}