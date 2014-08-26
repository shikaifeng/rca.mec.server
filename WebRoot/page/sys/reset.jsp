<%@page import="tv.zhiping.common.Cons"%>
<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>修改密码</title>
	<%@ include file="/page/common/jscommon.jsp"%>
	<link href="${stCtx}/css/common.css" rel="stylesheet" type="text/css" />
	<link href="${stCtx}/css/list.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript">
		var code = "${code}";
		if (code) {
			if (code == '200') {
				alert("密码修改成功！");
			}
			else if (code == '201') {
				alert("验证码不匹配！");
			}
			else {
				alert("账号或原密码有误！");
			}
		}
	</script>
</head>
<body>
	<div class="content">
		<div class="header">
			<div class="title">
				<h3>修改密码</h3>
			</div>
		</div>
		<div class="body">
			<form id="reset_pwd_form" method="post" action="" name="reset_pwd_form">
				<table class="form_table" width="100%" cellspacing="0" cellpadding="0" border="0">
					<tr>
						<td class="label">用户名</td>
						<td>
							${username}
						</td>
					</tr>
					<tr>
						<td class="label">原密码</td>
						<td>
							<input type="password" class="text_input" name="old_pwd" value="" />
						</td>
					</tr>
					<tr>
						<td class="label">新密码</td>
						<td>
							<input type="password" class="text_input" name="new_pwd" value="" />
						</td>
					</tr>
					<tr>
						<td class="label">确认密码</td>
						<td>
							<input type="password" class="text_input" name="cfm_pwd" value="" />
						</td>
					</tr>
					<tr>
						<td class="label"><input type="hidden" name="user_id" value="${user_id}" /></td>
						<td>
							<input type="submit" class="btn" value="保存" />
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>