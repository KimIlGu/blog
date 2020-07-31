<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ include file="/jsp/part/head.jspf"%>
<%@ include file="/jsp/part/toastUiEditor.jspf"%>

<style>
/* cus */
.modify-reply-form-box {
	margin-top: 30px;
}
</style>


<div class="modify-reply-form-box con" >
	<div style="padding:0 30px">
		<form action="doModifyReply" method="POST" class="modify-reply-form form1" onsubmit="submitModifyReplyForm(this); return false;" style="height:100px;">
			<input type="hidden" name="redirectUri" value="${param.redirectUri}"> 
			<input type="hidden" name="id" value="${articleReply.id}"> <input type="hidden" name="body"> 
			
			<div class="form-row">
				<div class="input">
					<script type="text/x-template">${articleReply.bodyForXTemplate}</script>
					<div class="toast-editor"></div>
				</div>
			</div>
			
			<div class="form-row">
				<div class="input">
					<input type="submit" value="전송" />
					<a href="list">취소</a>
				</div>
			</div>
		</form>
	</div>
</div>

<script>
	var submitModifyReplyFormDone = false;
	function submitModifyReplyForm(form) {
		if (submitModifyReplyFormDone) {
			alert('처리중입니다.');
			return;
		}
		var editor = $(form).find('.toast-editor').data('data-toast-editor');
		var body = editor.getMarkdown();
		body = body.trim();
		if (body.length == 0) {
			alert('내용을 입력해주세요.');
			editor.focus();
			return false;
		}
		form.body.value = body;
		form.submit();
		submitModifyReplyFormDone = true;
	}
</script>

<%@ include file="/jsp/part/foot.jspf"%>