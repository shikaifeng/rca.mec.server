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
		$("#question_status").val("${obj.question_status}");
		setFormValidate();
	});
	function doSave() {
		$('#inputForm').submit();
	}
	function setFormValidate() {
		$("#inputForm").validate({
			rules : {
				"obj.title" : "required",
				"obj.question_status": "required"
			},
			messages : {
				"obj.title" : "请输入标题",
				"obj.question_status" : "请选择类别",
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
	<form action="${ctx}/sys/banner/save" method="post" id="inputForm">
		&nbsp; <input type="hidden" name="obj.id" id="id" value="${obj.id}" />
		<div class="pop ">
			<div class="popContent">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="20%" align="right">名称：</td>
						<td width="80%" height="50"><textarea name="obj.title"
								id="obj.title" style="width: 350px; height: 80px;">${obj.title}</textarea><span
							style="float: none;" class="red">* <label for="title"
								class="error">${_msgFor_title}</label></span></td>
					</tr>
					<tr>
						<td width="20%" align="right">问题类别：</td>
						<td width="80%" height="50"><select id="question_status"
							name="obj.question_status">
							<option value="0">互动问答还没有出现</option>
							<option value="2">答对-公布答案,答题正确</option>
							<option value="3">答错-公布答案,答题错误</option>
							<option value="4">未参与-公布答案，未答题</option>
							<option value="7">等待答案-已答题，等待答案中</option>
						</select><span style="float: none;" class="red">*<label for="question_status"
								class="error">${_msgFor_question_status}</label></span></td>
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