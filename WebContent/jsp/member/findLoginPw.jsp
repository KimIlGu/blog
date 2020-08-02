<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="/jsp/part/head.jspf"%>

<div class="findLoginPw-form-box con">
	<form action="doFindLoginPw" method="POST" class="findLoginPw-form form1" onsubmit="FindLoginPwForm__submit(this); return false;">
		<div class="form-row">
			<div class="label">아이디</div>
			<div class="input">
				<input type="text" name="loginId" placeholder="아이디를 입력해주세요." autocomplete="off" autofocus type="text"/>
			</div>
		</div>
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
	var FindLoginPwForm__submitDone = false;
	function FindLoginPwForm__submit(form) {
		if (FindLoginPwForm__submitDone) {
			alert('처리중 입니다.');
			return;
		}
		form.loginId.value = form.loginId.value.trim();
		if (form.loginId.value.length == 0) {
			alert('로그인 아이디를 입력해주세요.');
			form.loginId.focus();
			return;
		}
		form.email.value = form.email.value.trim();
		if (form.email.value.length == 0) {
			alert('이메일을 입력해주세요.');
			form.email.focus();
			return;
		}
		form.submit();
		FindLoginPwForm__submitDone = true;
	}
</script>

<%@ include file="/jsp/part/foot.jspf"%>