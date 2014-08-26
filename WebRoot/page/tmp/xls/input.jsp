<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>剧集导入</title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet"
	href="${stCtx}/css/bcisc_beta1.0.css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<script src="${stCtx}/js/jquery.validate-1.9.min.js"
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
				"filename" : "required",
				"programid" : "required",
				"episodeid" : "required",
				"name" : "required"
			},
			messages : {
				"filename" : "请输入文件表格路径",
				"programid" : "请添加节目ID",
				"episodeid" : "请添加剧集ID",
				"name" : "请输入第几集"
			},
			submitHandler : function(form) {
				if ($("#inputForm").valid()) {
					$("#inputForm").ajaxSubmit({
						dataType : "json",
						success : function(data) {							
							if (data.status == 0) {
								getLhgdialogParent().search();
								frameElement.lhgDG.cancel();
								alert("保存成功！");
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
	<form action="${ctx}/sys/tmpUtilXls/save" method="post" id="inputForm">
		&nbsp; <input type="hidden" name="id" id="id" value="${id}" />
		<div class="pop ">
			<div class="popContent">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="16%" align="right">文件路径：</td>
						<td width="84%" height="50"><input class="w220" id="filename"
							name="filename" type="text" value="${filename}" /> <span
							style="float: none;" class="red">* <label generated="true"
								for="title" class="error">${_msgFor_filename}</label></span></td>
					</tr>
					<tr>
						<td width="16%" align="right">节目ID：</td>
						<td width="84%" height="50"><input class="w220" id="programid"
							name="programid" type="text" value="${programid}" /> <span
							style="float: none;" class="red">* <label generated="true"
								for="title" class="error">${_msgFor_programid}</label></span></td>
					</tr>
					<tr>
						<td width="16%" align="right">剧集ID：</td>
						<td width="84%" height="50"><input class="w220" id="episodeid"
							name="episodeid" type="text" value="${episodeid}" /> <span
							style="float: none;" class="red">* <label generated="true"
								for="title" class="error">${_msgFor_episodeid}</label></span></td>
					</tr>
					<tr>
						<td width="16%" align="right">第几集：</td>
						<td width="84%" height="50"><textarea name="name"
								id="name" style="width: 350px; height: 80px;">${name}</textarea>
							${_msgFor_name}</td>
					</tr>
				</table>
				<p class="pBtn">
					<a class="save" href="javascript:doSave();" title="保  存">确
						&nbsp;定</a><a class="back"
						href="javascript:frameElement.lhgDG.cancel();" title="返  回">返
						&nbsp; 回</a>
				</p>
			</div>
		</div>
	</form>
</body>
</html>