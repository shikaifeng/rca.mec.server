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
		//setFormValidate();
		return ;
	});
	
	function setFormValidate() {
		$("#inputForm").validate({
			rules : {
				//"name_en" : "required"
			},
			messages : {
				//"name_en" : "请输入英文名称"
			},
			submitHandler : function(form) {
				if ($("#inputForm").valid()) {
					$('#msg').val("正在导出中");
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
<form action="${ctx}/sys/xlsExport/save" method="post" id="inputForm" target="_blank"> 
<div class="pop ">
     <div class="popContent">
      	<table width="100%" border="0" cellspacing="0" cellpadding="0">
      	 	<tr>
              <td width="16%" align="right">命令：</td>
              <td  width="84%" height="50">
              	<select name="type" id="type" style="width: 200px">
              		<option value="imdb_fact">imdb百科新</option>
              		<option value="imdb_person_match_error">明星匹配失败信息</option>
              		<option value="imdb_sound_match_error">歌曲匹配失败信息</option>
              	</select>
              	${_msgFor_msg}
              </td>
            </tr>
             <tr>
              <td width="16%" align="right">说明：</td>
              <td  width="84%" height="50" style="color: red">
              	
              </td>
            </tr>
          </table>
	<p class="pBtn"><a class="save" href="javascript:doSave();" title="保  存">导出</a></p>
      </div>
  </div>
</form>
</body>
</html>
