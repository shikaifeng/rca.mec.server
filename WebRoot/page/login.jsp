<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>星库运营系统-用户登录</title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet" href="${stCtx}/css/login_beta1.0.css" />
<script type="text/javascript">
function login(){
	var username = $("#username").val();
	var password = $("#password").val();
	$.ajax({
		type:"POST",
		url:"${ctx}/sys/login/logIn",
		dataType:"text",
		data:{'name':username,'pwd':password},
		success:function(returnedData){
			if(returnedData == 1){
				window.location.href = "${ctx}/sys/index";
			}else{
				$("#error_p").html("您输入的用户名或密码有误，请重新输入！");
				$("#error_p").show();
			}
		}
	});
}
</script>
</head>
<body>
<div class="main">
    <div class="login">
    	<h3>${systemName}</h3>
        <div class="edit">
            <p class="bg_txt"><span>用户名：</span><input id="username" name="username" type="text" /></p>
            <p class="bg_txt"><span>密&nbsp;&nbsp;码：</span><input id="password" name="password" type="password" /></p>
            <p><a class="btnLogin" title="登 录" href="javascript:login();" >登 录</a></p>
        </div>
        <p id="error_p" class="error" style="display: none;">您输入的密码有误请重新输入！</p>
    </div>
    <div class="footer">
        <p><span class="copy">Copyright &copy 2014～2016&nbsp; &nbsp;华数智屏&nbsp;&nbsp; 版权所有 </span></p>
 	</div>
</div>
</body>
</html>