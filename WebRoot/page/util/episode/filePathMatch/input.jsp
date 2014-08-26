<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>剧集导入</title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet" href="${stCtx}/css/bcisc_beta1.0.css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<script src="${stCtx}/js/jquery.validate-1.9.min.js" type="text/javascript"></script>
<script src="${stCtx}/js/uploadify/jquery.uploadify.v2.1.4.js" type="text/javascript"></script>
<script src="${stCtx}/js/uploadify/swfobject.js" type="text/javascript"></script>

<style type="text/css">
#uploadUploader {
	width: 83px;
	height: 83px;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		setFormValidate();
		return ;
	});
	
	function setFormValidate() {
		$("#inputForm").validate({
			rules : {
				"name_en" : "required"
			},
			messages : {
				"name_en" : "请输入英文名称"
			},
			submitHandler : function(form) {
				if ($("#inputForm").valid()) {
					$('#msg').val("努力解析中");
					$("#inputForm").ajaxSubmit({
						dataType : "text",
						success : function(data) {
							$('#msg').val(data);
						},
						error : function(data) {
							$('#msg').val("异常，请稍后重试")
						}
					});
				}
			}
		});
	}
	
	function doSave() {
		$('#inputForm').submit();
	}
</script>
</head>

<body style=" background:#f6f9fb;">
<form action="${ctx}/sys/episodeFilePathMatch/save" method="post" id="inputForm"> 
<div class="pop ">
     <div class="popContent">
      	<table width="100%" border="0" cellspacing="0" cellpadding="0">
      	 	<tr>
              <td width="16%" align="right">英文名称：</td>
              <td  width="84%" height="50"><input class="w220" id="name_en" name="name_en" type="text" value="${name_en}" />
              	<span style="float:none;" class="red">* <label generated="true" for="name_en" class="error">${_msgFor_name_en}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">返回内容：</td>
              <td  width="84%" height="50">
              	<textarea name="msg" id="msg" style="width: 550px;height: 150px;">${msg}</textarea>
              	${_msgFor_msg}
              </td>
            </tr>
             <tr>
              <td width="16%" align="right">说明：</td>
              <td  width="84%" height="50" style="color: red">
              	
              </td>
            </tr>
          </table>
	<p class="pBtn"><a class="save" href="javascript:doSave();" title="保  存">提交查询</a></p>
      </div>
  </div>
</form>
</body>
</html>
