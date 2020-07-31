<%@ page import="com.sbs.java.blog.util.Util"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="/jsp/part/head.jspf"%>
<%@ include file="/jsp/part/toastUiEditor.jspf"%>

<style>
	.box-body {
		padding:0 30px;
	}
	
	.box-body > .detail-group > input {
		margin-left: 30px;
	}
	
	.reply-box {
		padding: 0 30px;
	}	
	
	.reply-box-inner {
		padding: 0 15px;
	}	
	
	.con {
		max-width:1100px;
	}
</style>

<section class="detail con">
	<div class="box">
		
		<div class="box-body" style="margin-top:50px;">
		
			<div class="detail-title" style="margin-top:30px;">
				<h1 style="color:#434a53; font-size:30px; font-weight: normal; font-family: Avenir,'Lato','나눔바른고딕','NanumBarunGothic','애플 SD 산돌고딕 Neo','Apple SD Gothic Neo','나눔고딕',NanumGothic,'맑은 고딕','Malgun Gothic','돋움',dotum,AppleGothic,sans-serif;">${article.title}</h1>
			</div>
			<div style="margin-top:30px; color:#b5b5b5; font-size:15px; font-family: Avenir,'Lato','애플 SD 산돌고딕 Neo','Apple SD Gothic Neo','나눔바른고딕',NanumBarunGothic,'나눔고딕',NanumGothic,'맑은 고딕','Malgun Gothic','돋움',dotum,AppleGothic,sans-serif;">
				<span><i class="far fa-clock"></i></span>
				<span>${article.regDate}</span>
				<span style="margin-left:15px;">|</span>
				<span style="margin-left:15px;"><i class="fas fa-eye"></i></span>
				<span>${article.hit}</span>
			</div>
			<div class="detail-body">
				<div class="detail-group">
					<script type="text/x-template">${article.bodyForXTemplate}</script>
					<div class="toast-editor toast-editor-viewer"></div>
				</div>
			</div>
			<hr />
			<div class="detail-items con" style="margin-top:20px; margin-bottom:20px;">
				<span style="margin-left:0;">작성자 : <span>${article.getExtra().get("writer")}</span></span>
			</div>
			<hr />
			<div style="margin-top: 50px;">
			<c:if test="${article.extra.deleteAvailable}">
				<a onclick="if ( confirm('삭제하시겠습니까?') == false ) return false;" href="./doDelete?id=${article.id}">삭제</a>
			</c:if>	
			<c:if test="${article.extra.modifyAvailable}">
				<a href="./modify?id=${article.id}">수정</a>
			</c:if>
			</div>
		</div>
	</div>
	
	<div class="reply-box">
		<div class="reply-header"><h2>댓글</h2></div>
		
		<c:if test="${isLogined == false}">
			<div class="con">
				<c:set var="loginUri" value="../member/login?afterLoginRedirectUri=${Util.getNewUriAndEncoded(currentUri, 'jsAction', 'WriteReplyForm__focus')}" />
				<a href="${loginUri}">로그인</a> 후 이용해주세요.
			</div>
		</c:if>
		
		<c:if test="${isLogined}">
	
			<script>
				var WriteReplyForm__submitDone = false;
				function WriteReplyForm__focus() {
					var editor = $('.write-reply-form .toast-editor').data(
							'data-toast-editor');
					editor.focus();
				}
				function WriteReplyForm__submit(form) {
					if (WriteReplyForm__submitDone) {
						alert('처리중입니다.');
						return;
					}
					var editor = $(form).find('.toast-editor')
							.data('data-toast-editor');
					var body = editor.getMarkdown();
					body = body.trim();
					if (body.length == 0) {
						alert('내용을 입력해주세요.');
						editor.focus();
						return false;
					}
					form.body.value = body;
					form.submit();
					WriteReplyForm__submitDone = true;
				}
				function WriteReplyForm__init() {
					$('.write-reply-form .cancel').click(
							function() {
								var editor = $('.write-reply-form .toast-editor').data(
										'data-toast-editor');
								editor.setMarkdown('');
							});
				}
				$(function() {
					WriteReplyForm__init();
				});
			</script>
		
			<div class="write-reply-form-box con">
				<form action="doWriteReply" method="POST" class="write-reply-form form1" onsubmit="WriteReplyForm__submit(this); return false;">
					<input type="hidden" name="articleId" value="${article.id}"> 
					
					<c:set var="redirectUri"
						value="${Util.getNewUriRemoved(currentUri, 'lastWorkArticleReplyId')}" />
					<c:set var="redirectUri"
						value="${Util.getNewUri(redirectUri, 'jsAction', 'WriteReplyList__showDetail')}" />
					
					<input type="hidden" name="redirectUri" value="${redirectUri}">
					<input type="hidden" name="body">
						
					<div class="form-row">
						<div class="input">
							<script type="text/x-template"></script>
							<div data-toast-editor-height="300" class="toast-editor"></div>
						</div>
					</div>
					
					<div class="form-row">
						<div class="input">
							<input type="submit" value="작성" />
							<input class="cancel" type="button" value="취소" />
						</div>
					</div>
				</form>
			</div>
		</c:if>
	
		<script>
			function WriteReplyList__showTop() {
				var top = $('.article-replies-list-box').offset().top;
				$(window).scrollTop(top);
			}
			function WriteReplyList__showDetail() {
				WriteReplyList__showTop();
				var $tr = $('.article-replies-list-box > [data-id="'
						+ param.lastWorkArticleReplyId + '"]');
				$tr.addClass('high');
				setTimeout(function() {
					$tr.removeClass('high');
				}, 1000);
			}
		</script>
		
<style>
.article-replies-list-box>.high {
	background-color: #dfdfdf;
}
.article-replies-list-box>.body-box>div {
	transition: background-color 1s;
}
</style>
		<h2 class="con">댓글 리스트</h2>
		<div class="con article-replies-list-box" style="padding:0 30px">
			<c:forEach items="${articleReplies}" var="articleReply">
				<div data-id="${articleReply.id}" style="margin-top:30px;" class="body-box">
					
					<div><b>${articleReply.getExtra().get("writer")}</b></div>
					<script type="text/x-template">${articleReply.bodyForXTemplate}</script>
					<div class="toast-editor toast-editor-viewer"></div>
					<div>
						<span style="opacity:0.7;">${articleReply.regDate}</span>
					</div>
					<div style="margin-top: 20px;">
						<c:if test="${articleReply.extra.deleteAvailable}">
							<c:set var="afterDeleteReplyRedirectUri"
								value="${Util.getNewUriRemoved(currentUri, 'lastWorkArticleReplyId')}" />
							<c:set var="afterDeleteReplyRedirectUri"
								value="${Util.getNewUriAndEncoded(afterDeleteReplyRedirectUri, 'jsAction', 'WriteReplyList__showTop')}" />
	
							<c:set var="afterModifyReplyRedirectUri"
								value="${Util.getNewUriRemoved(currentUri, 'lastWorkArticleReplyId')}" />
							<c:set var="afterModifyReplyRedirectUri"
								value="${Util.getNewUriAndEncoded(afterModifyReplyRedirectUri, 'jsAction', 'WriteReplyList__showDetail')}" />
	
							<a onclick="if ( confirm('삭제하시겠습니까?') == false ) return false;"
								href="./doDeleteReply?id=${articleReply.id}&redirectUri=${afterDeleteReplyRedirectUri}">삭제</a>
						</c:if>
						
						<c:if test="${articleReply.extra.modifyAvailable}">
							<a
								href="./modifyReply?id=${articleReply.id}&redirectUri=${afterModifyReplyRedirectUri}">수정</a>
						</c:if>
					</div>					
				</div>
			</c:forEach>
		</div>
	</div>
	<div class="detail-footer con">
		<button onclick="location.href='list'">이전</button>
	</div>
	
</section>


<%@ include file="/jsp/part/foot.jspf"%>