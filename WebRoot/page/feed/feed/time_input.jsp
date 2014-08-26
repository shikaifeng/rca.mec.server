<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理</title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet" href="${stCtx}/css/bcisc_beta1.0.css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<script src="${stCtx}/js/jquery.validate-1.9.min.js" type="text/javascript" ></script>
<style type="text/css">
#uploadUploader{
	width:83px;
	height:83px;
}
</style>
<script type="text/javascript">
$(document).ready(function() {
	setFormValidate();
});
function doSave(){
	$('#inputForm').submit();
}
function setFormValidate(){
	$("#inputForm").validate({
		rules:{
			"obj.start_time":"required",
			"obj.end_time":"required"
		},
		messages:{
			"obj.start_time":"请输入开始时间",
			"obj.start_time":"请输入结束时间"
		},
		submitHandler: function(form) {
			if($("#inputForm").valid()){
				$("#inputForm").ajaxSubmit({
					dataType:"json",
			 		success:function(data){
			 			if(data.status == 0){
				 			//getLhgdialogParent().search();
				 			parent.location.reload();
							frameElement.lhgDG.cancel();
			 			}else{
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
<body style=" background:#f6f9fb;">
<form action="${ctx}/sys/feed/time_save" method="post" id="inputForm">&nbsp; 
<input type="hidden" name="obj.id" id="id" value="${obj.id}" />
<div class="pop ">
     <div class="popContent">
      	<table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="16%" align="right">开始时间：</td>
              <td  width="84%" height="50"><input class="w220" id="start" name="obj.start_time" type="text" value="${obj.start_time}" />
              	<span style="float:none;" class="red">* <label generated="true" for="name" class="error">${_msgFor_start_time}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">结束时间：</td>
              <td  width="84%" height="50"><input class="w220" id="end" name="obj.end_time" type="text" value="${obj.end_time}" />
              	<span style="float:none;" class="red">* <label generated="true" for="name" class="error">${_msgFor_end_time}</label></span>
              </td>
            </tr>
          </table>
	<p class="pBtn"><a class="save" href="javascript:doSave();" title="保  存">保  &nbsp;存</a><a class="back" href="javascript:frameElement.lhgDG.cancel();" title="返  回">返 &nbsp; 回</a></p>
      </div>
  </div>
</form>
</body>
</html>