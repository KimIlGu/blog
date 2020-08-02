<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
.write-form-box {
    margin-top: 30px;
}
</style>

<div class="join-form-box con">
	<div style="padding: 0 30px; margin-top:50px">
		<form action="doJoin" method="POST" class="join-form form1"
		onsubmit="JoinForm__submit(this); return false;">
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
				<input name="loginPw" type="password" placeholder="비밀번호를 입력해주세요." autocomplete="off"></input>
			</div>
		</div>
		<div class="form-row">
			<div class="label">비밀번호확인</div>
			<div class="input">
				<input name="loginPwConfirm" type="password"
					placeholder="로그인 비번확인을 입력해주세요." autocomplete="off" />
			</div>
		</div>
		<div class="form-row">
			<div class="label">이름</div>
			<div class="input">
				<input name="name" placeholder="이름을 입력해주세요." autocomplete="off"/>
			</div>
		</div>
		<div class="form-row">
			<div class="label">닉네임</div>
			<div class="input">
				<input name="nickname" placeholder="닉네임을 입력해주세요." autocomplete="off"/>
			</div>
		</div>
		<div class="form-row">
			<div class="label">이메일</div>
			<div class="input">
				<input name="email" placeholder="이메일을 입력해주세요." autocomplete="off"/>
			</div>
		</div>
		<div class="form-row">
			<div class="label">전송</div>
			<div class="input">
				<input type="submit" value="확인" /> 
			</div>
		</div>
	</form>
	<div style="margin-top:50px;">
		<button onclick="location.href='${pageContext.request.contextPath}/s/home/main'">이전</button>
	</div>
	</div>
</div>

<script>
var JoinForm__validLoginId = '';
function JoinForm__submit(form) {
	form.loginId.value = form.loginId.value.trim();
	
	if (form.loginId.value.length == 0) {
		alert('로그인 아이디를 입력해주세요.');
		form.loginId.focus();
		return;
	}
	if (form.loginId.value == JoinForm__validLoginId) {
		alert('다른 아이디를 입력해주세요.');
		form.loginId.focus();
		return;
	}
	form.loginPw.value = form.loginPw.value.trim();
	if (form.loginPw.value.length == 0) {
		alert('로그인 비번을 입력해주세요.');
		form.loginPw.focus();
		return;
	}
	form.loginPwConfirm.value = form.loginPwConfirm.value.trim();
	if (form.loginPwConfirm.value.length == 0) {
		alert('로그인 비번확인을 입력해주세요.');
		form.loginPwConfirm.focus();
		return;
	}
	if (form.loginPw.value != form.loginPwConfirm.value) {
		alert('로그인 비번확인이 일치하지 않습니다.');
		form.loginPwConfirm.focus();
		return;
	}
	form.name.value = form.name.value.trim();
	if (form.name.value.length == 0) {
		alert('이름을 입력해주세요.');
		form.name.focus();
		return;
	}
	form.nickname.value = form.nickname.value.trim();
	if (form.nickname.value.length == 0) {
		alert('별명을 입력해주세요.');
		form.nickname.focus();
		return;
	}
	form.email.value = form.email.value.trim();
	if (form.email.value.length == 0) {
		alert('이메일을 입력해주세요.');
		form.email.focus();
		return;
	}
	form.loginPwReal.value = sha256(form.loginPw.value);
	form.loginPw.value = '';
	form.loginPwConfirm.value = '';
	form.submit();
}

function JoinForm__checkLoginIdDup(input) {
	var form = input.form;
	form.loginId.value = form.loginId.value.trim();
	if (form.loginId.value.length == 0) {
		return;
	}
	$.get('getLoginIdDup', {
		loginId : form.loginId.value
	}, function(data) {
		var $message = $(form.loginId).next();
		if (data.resultCode.substr(0, 2) == 'S-') {
			$message.empty().append(
					'<div style="color:green;">' + data.msg + '</div>');
			JoinForm__validLoginId = data.loginId;
		} else {
			$message.empty().append(
					'<div style="color:red;">' + data.msg + '</div>');
			JoinForm__validLoginId = '';
		}
	}, 'json');
}
var JoinForm__checkLoginIdValid__debounce = _.debounce(JoinForm__checkLoginIdDup, 1000);
</script>

<%@ include file="/jsp/part/foot.jspf"%>