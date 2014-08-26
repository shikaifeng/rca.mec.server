<%@ include file="/page/common/common.jsp"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="org.apache.log4j.Logger"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<style type="text/css">
.center {
    height: 200px;
    margin: 100px auto 0;
    width: 450px;
}
.center img {
    float: left;
    height: 128px;
    width: 128px;
}
.center span {
    color: #FF7000;
    float: left;
    font-size: 22px;
    font-weight: bold;
    margin-left: 5px;
    margin-top: 30px;
}
.center p {
    color: #282828;
    float: left;
    font-size: 12px;
    margin-left: 5px;
    margin-top: 15px;
}
.center p a {
    color: #FF7000;
    font-weight: bold;
}
</style>
</head>

<body>
<div class="center">
<img width="128" height="128" src="${stCtx}/images/cry.png">
<span>系统忙,处理出现异常,请稍后!</span>
<p>点击 <a href="javascript:history.back()">返回</a> 到上一级页面</p>
<%
//这里是系统抛出的异常
Throwable e = (Throwable)request.getAttribute("exception");
if(e!=null){
	Logger log = Logger.getLogger(this.getClass());
	log.error(e.getMessage(),e);
}
%>
</div>
</body>
</html>