<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/jsp/part/head.jspf"%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/js-sha256/0.9.0/sha256.min.js"></script>

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
.form1 .form-row > #editor1 {
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
.login-form-box {
    margin-top: 30px;
}
</style>

<div class="login-form-box con">
	<div style="padding:0 30px">
		<form action="doLogin" method="POST" class="login-form form1" onsubmit="submitLoginForm(this); return false;">
		<input type="hidden" name="redirectUrl" value="${param.afterLoginRedirectUrl}"/>
		<input type="hidden" name="loginPwReal" />
			<div class="form-row">
				<div class="label">아이디</div>
				<div class="input">
					<input name="loginId" type="text" placeholder="아이디를 입력해주세요." autocomplete="off"/>
				</div>
			</div>
			<div class="form-row">
				<div class="label">비밀번호</div>
				<div class="input">
					<input name="loginPw" type="password" placeholder="비밀번호를 입력해주세요." autocomplete="off"/>
				</div>
			</div>
			<div class="form-row">
				<div class="label">전송</div>
				<div class="input">
					<input type="submit" value="확인"/> 
				</div>
			</div>
		</form>
		<div style="margin-top:50px;">
			<button onclick="location.href='${pageContext.request.contextPath}/s/home/main'">이전</button>
		</div>
		
		<div>
			<span>
				<a href="${pageContext.request.contextPath}/s/member/findLoginId" >아이디 찾기</a>
			</span>
			<span>/</span>
			<span>
				<a href="${pageContext.request.contextPath}/s/member/findLoginPw" >비밀번호 찾기</a>
			</span>
		</div>
	</div>
</div>

<script>
	function submitLoginForm(form) {
		form.loginId.value = form.loginId.value.trim();
		if (form.loginId.value.length == 0) {
			alert('로그인 아이디를 입력해주세요.');
			form.loginId.focus();
			return;
		}
		form.loginPw.value = form.loginPw.value.trim();
		if (form.loginPw.value.length == 0) {
			alert('로그인 비번을 입력해주세요.');
			form.loginPw.focus();
			return;
		}

		form.loginPwReal.value = sha256(form.loginPw.value);
		form.loginPw.value = '';
		
		form.submit();
	}
</script>

<%@ include file="/jsp/part/foot.jspf"%>