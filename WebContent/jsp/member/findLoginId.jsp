<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="/jsp/part/head.jspf"%>

<div class="findLoginId-form-box con">
	<form action="doFindLoginId" method="POST" class="findLoginId-form form1" onsubmit="FindLoginIdForm__submit(this); return false;">
		<div class="form-row">
			<div class="label">이름</div>
			<div class="input">
				<input type="text" name="name" placeholder="이름을 입력해주세요." autocomplete="off" />
			</div>
		</div>
		<div class="form-row">
			<div class="label">이메일</div>
			<div class="input">
				<input type="email" name="email" placeholder="이메일을 입력해주세요." autocomplete="off" />
			</div>
		</div>
		<div class="form-row">
			<div class="label">전송</div>
			<div class="input">
				<input type="submit" value="전송" />
			</div>
		</div>
	</form>
</div>

<script>
	var FindLoginIdForm__submitDone = false;
	function FindLoginIdForm__submit(form) {
		if (FindLoginIdForm__submitDone) {
			alert('처리중 입니다.');
			return;
		}
		form.name.value = form.name.value.trim();
		if (form.name.value.length == 0) {
			alert('이름을 입력해주세요.');
			form.name.focus();
			return;
		}
		form.email.value = form.email.value.trim();
		if (form.email.value.length == 0) {
			alert('이메일을 입력해주세요.');
			form.email.focus();
			return;
		}
		form.submit();
		FindLoginIdForm__submitDone = true;
	}
</script>

<%@ include file="/jsp/part/foot.jspf"%>