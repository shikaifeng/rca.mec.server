<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${sys_name}</title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet" href="${stCtx}/css/bcisc_beta1.0.css" />
<script type="text/javascript" src="${stCtx}/js/frame.js"></script>
<script language="javascript" src="${stCtx}/js/lhgdialog/lhgdialog.js"></script>
<style>
html,body{ overflow-y:hidden; }
/*.header .wel p a { color: #f6a2a2; padding: 0px 4px; }
.header .wel p a:hover { color: #fff; }*/
.header .wel p a { color: #9199a4; padding: 0px 4px; text-decoration:none; }
.header .wel p a:hover { color: #d1d7de; }
</style>
<script type="text/javascript">
function input(){
	var url = "${ctx}/sys/base/operator/inputOfResetPwd";
	var t = new $.dialog({title:'修改',id:'edit', page:url,rang:true,width :550,height:330,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}

function logout(){
	window.location.href = "${ctx}/sys/login/logOut";
}
</script>
</head>
<body class="showmenu">
<div class="header">
	<h1><a href="/sys/index">${sys_name}</a></h1>
    <div class="wel">
    	<p>您好 <span>${username}</span>，欢迎使用${sys_name}！</p>
        <p class="link"><a href="javascript:void(0)" onclick="input()" title="修改密码" target="rightFrame">修改密码</a> | <a href="javascript:logout()" title="退出系统">退出系统</a></p>
    </div>
</div>
<div class="main">
	<div class="left">
         <div class="menu" id="menu">
            <iframe src="${ctx}/sys/base/leftMenu/index"  name="menu" frameborder="0" scrolling="no" noresize="noresize"></iframe>
         </div>
    </div>
	<div class="min">
		<div class="toggle" ></div>
    		<a href="#" id="togglemenu"><img src="${stCtx}/images/list_button.png" /></a>
    </div>
    <div class="right">
    	<iframe src="${ctx}/page/main/right.jsp"  name="rightFrame" id="rightFrame" frameborder="0" scrolling="auto" width="100%" height="100%" noresize="noresize"></iframe>
    </div>
</div>
</body>
</html>