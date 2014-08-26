<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet"
	href="${stCtx}/css/bcisc_beta1.0.css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<script src="${stCtx}/js/jquery.validate-1.9.min.js"
	type="text/javascript"></script>
<script src="${stCtx}/js/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<style type="text/css">
#uploadUploader {
	width: 83px;
	height: 83px;
}
</style>
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
				"obj.title" : "required",
				"obj.win_day" : "required",
				"obj.win_count" : "required",
				"obj.start_time" : "required",
				"obj.end_time" : "required"
			},
			messages : {
				"obj.title" : "请输入标题",
				"obj.win_day" : "请输入中奖频率",
				"obj.win_count" : "请输入中奖频率",
				"obj.start_time" : "请输入活动开始时间",
				"obj.end_time" : "请输入活动结束时间"
			},
			submitHandler : function(form) {
				if ($("#inputForm").valid()) {
					$("#inputForm").ajaxSubmit({
						dataType : "json",
						success : function(data) {
							if (data.status == 0) {
								getLhgdialogParent().location.reload();
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
<body style="background: #f6f9fb;">
	<form action="${ctx}/sys/luck/save" method="post" id="inputForm">
		&nbsp; <input type="hidden" name="obj.id" id="id" value="${obj.id}" />
		<input type="hidden" name="obj.url" id="url" value="m/lucky_draw/the_voice_of_china" />
		<div class="pop ">
			<div class="popContent">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="20%" align="right">名称：</td>
						<td width="80%" height="50"><input class="w220" id="obj.title"
							name="obj.title" type="text" value="${obj.title}" /> <span
							style="float: none;" class="red">* <label for="title"
								class="error">${_msgFor_title}</label></span></td>
					</tr>
					<tr>
						<td width="20%" align="right">简介：</td>
						<td width="80%" height="50"><textarea name="obj.summary"
								id="obj.summary" style="width: 350px; height: 80px;">${obj.summary}</textarea>
						</td>
					</tr>
					<%-- <tr>
						<td width="20%" align="right">活动链接：</td>
						<td width="80%" height="50"><input class="w220" id="url"
							name="obj.url" type="text" value="${obj.url}" /></td>
					</tr> --%>
					<tr>
						<td width="20%" align="right">单账户最大获奖率：</td>
						<td width="80%" height="50"><select id="obj.win_day"
							name="obj.win_day" style="width: 50px;">
								<c:forEach var='index' begin='1' end='5'>
									<c:if test="${obj.win_day==index}">
										<option value="${index}" selected="selected">${index}</option>
									</c:if>
									<c:if test="${obj.win_day!=index}">
										<option value="${index}">${index}</option>
									</c:if>
								</c:forEach>
						</select> 天 <select id="obj.win_count"
							name="obj.win_count" style="width: 50px;">
								<c:forEach var='index2' begin='1' end='10'>
									<c:if test="${obj.win_count==index2}">
										<option value="${index2}" selected="selected">${index2}</option>
									</c:if>
									<c:if test="${obj.win_count!=index2}">
										<option value="${index2}">${index2}</option>
									</c:if>
								</c:forEach>
						</select> 次<span style="float: none;" class="red">* <label for="win_day"
								class="error">${_msgFor_win_day}</label><label for="win_count"
								class="error">${_msgFor_win_count}</label></span></td>
					</tr>
					<tr>
						<td width="20%" align="right">开始时间：</td>
						<td width="80%" height="50">
						<input id="obj.start_time" name="obj.start_time" value="<fmt:formatDate value="${obj.start_time}" pattern="yyyy-MM-dd HH:mm:ss"/>" type="text" 
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate" style="width:150px"/>  
						<span style="float: none;" class="red">*如2014-7-19 17:13:00 <label
						for="title" class="error">${_msgFor_start_time}</label></span>
						</td>
					</tr>
					<tr>
						<td width="20%" align="right">结束时间：</td>
						<td width="80%" height="50">
						<input id="obj.end_time" name="obj.end_time" value="<fmt:formatDate value="${obj.end_time}" pattern="yyyy-MM-dd HH:mm:ss"/>" type="text" 
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate" style="width:150px"/>  
						<span style="float: none;" class="red">*如2014-7-19 19:30:00 <label
						for="title" class="error">${_msgFor_end_time}</label></span>
						</td>
					</tr>
				</table>
				<p class="pBtn">
					<a class="save" href="javascript:doSave();" title="保  存">保
						&nbsp;存</a><a class="back"
						href="javascript:frameElement.lhgDG.cancel();" title="返  回">关
						&nbsp; 闭</a>
				</p>
			</div>
		</div>
	</form>
</body>
</html>