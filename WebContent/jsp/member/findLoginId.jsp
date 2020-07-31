<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="/jsp/part/head.jspf"%>

<div class="findLoginId-form-box con">
	<form action="doFindLoginId" method="POST" class="findLoginId-form form1" onsubmit="submitFindLoginIdForm(this); return false;">
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

<%@ include file="/jsp/part/foot.jspf"%>