<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>feed管理</title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet"
	href="${stCtx}/css/bcisc_beta1.0.css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<script src="${stCtx}/js/jquery.validate-1.9.min.js"
	type="text/javascript"></script>
<script src="${stCtx}/js/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script language="javascript" src="${stCtx}/js/lhgdialog/lhgdialog.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	setFormValidate();
});
function doSave() {
	$('#inputForm').submit();
}
function setFormValidate() {
	$("#inputForm").validate({
		rules : {
			"obj.min_time" : "required",
			"obj.max_time" : "required"
		},
		messages : {
			"obj.min_time" : "请添加开始时间",
			"obj.max_time" : "请添加结束时间"				
		},
		submitHandler : function(form) {
			if ($("#inputForm").valid()) {
				$("#inputForm").ajaxSubmit({
					dataType : "json",
					success : function(data) {
						if (data.status == 0) {
							frameElement.lhgDG.cancel();
						} else {
							alert(data.msg);
						}
					}
				});
			}
		}
	});
}


</script>

</head>
<body>
	<form action="${ctx}/sys/weibo_feed/save_weibo_time" method="post"
		id="inputForm">
		<input type="hidden" name="program_id" id="program_id" value="${program_id}" />
		<input type="hidden" name="episode_id" id="episode_id" value="${episode_id}" />
		<input type="hidden" name="obj.id" id="obj.id" value="${obj.id}" />
		<table>
			<tr>
				<td width="16%" align="right">开始时间：</td>
				<td width="84%" height="50"><input id="obj.min_time"name="obj.min_time" value="<fmt:formatDate value="${obj.min_time}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
					class="Wdate" style="width: 150px" /> <span style="float: none;"
					class="red">*如2014-7-19 17:13 <label generated="true"
						for="title" class="error">${_msgFor_min_time}</label></span></td>
			</tr>
			<tr>
				<td width="16%" align="right">结束时间：</td>
				<td width="84%" height="50"><input id="obj.max_time" name="obj.max_time" value="<fmt:formatDate value="${obj.max_time}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
					class="Wdate" style="width: 150px" /> <span style="float: none;"
					class="red">*如2014-7-19 19:30 <label generated="true"
						for="title" class="error">${_msgFor_max_time}</label></span></td>
			</tr>
		</table>
		<p class="pBtn">
			<a class="save" href="javascript:doSave();" title="保  存">保
				&nbsp;存</a><a class="back"
				href="javascript:frameElement.lhgDG.cancel();" title="返  回">返
				&nbsp; 回</a>
		</p>
	</form>
</body>
</html>