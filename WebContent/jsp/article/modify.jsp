<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/jsp/part/head.jspf"%>
<%@ include file="/jsp/part/toastUiEditor.jspf"%>

<style>
/* lib */
.form1 {
    display: block; 
    width: 100%;
}
.form1 .form-row {
    align-items: center; 
    display: flex;
}
.form1 .form-row:not(:first-child) { 
	margin-top : 10px; 
}
.form1 .form-row >.label {
    width: 100px;
}
.form1 .form-row >.input {
    flex-grow: 1;
}
.form1 .form-row >.input > input, .form1 .form-row > .input > textarea {
    display: block; width: 100%; box-sizing: border-box; padding: 10px;
}
.form1 .form-row > .toast-editor1 {
	display: block; width: 100%; box-sizing: border-box;
}
.form1 .form-row > .input > select {
    padding: 10px;
}
.form1 .form-row > .input > textarea {
    height: 500px;
}
@media ( max-width : 700px ) {
    .form1 .form-row {
        display: block;
    }
}
/* cus */
.write-form-box {
    margin-top: 30px;
}

.form-row > .input-box > .input {
	margin-left:67px;
	display:inline-block;
}

.toast-editor1 {
	width:1195px;
}
</style>

<div class="modify-form-box con">
	<div style="padding:0 30px">
		<form action="doModify" method="POST" class="modify-form form1" onsubmit="submitModifyForm(this); return false;">
			<input type="hidden" name="id" value="${article.id}">
			<input type="hidden" name="body">
			
			<div class="form-row">
				<div class="label">카테고리 선택</div>
				<div class="input">
					<select name="cateItemId">
						<c:forEach items="${cateItems}" var="cateItem">
							<option ${article.cateItemId == cateItem.id ? 'selected' : ''} value="${cateItem.id}">${cateItem.name}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="form-row">
				<div class="label">제목</div>
				<div class="input">
					<input name="title" type="text" value="${article.title}" placeholder="제목을 입력해주세요."/>
				</div>
			</div>
			
			<div class="form-row">
				<div class="label">내용</div>
				<div class="input">
					<script type="text/x-template">${article.bodyForXTemplate}</script>
					<div class="toast-editor"></div>
				</div>
				</div>
			</div>
			
			<div class="form-row">
				<div class="label">수정</div>
				<div class="input">
					<input type="submit" value="수정"/>
				</div>
			</div>
		</form>
	</div>
</div>

<script>
	var submitModifyFormDone = false;
	function submitModifyForm(form) {
		if (submitModifyFormDone) {
			alert('처리중입니다.');
			return;
		}
		form.title.value = form.title.value.trim();
		if (form.title.value.length == 0) {
			alert('제목을 입력해주세요.');
			form.title.focus();
			return false;
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
		submitModifyFormDone = true;
}
</script>

<%@ include file="/jsp/part/foot.jspf"%>