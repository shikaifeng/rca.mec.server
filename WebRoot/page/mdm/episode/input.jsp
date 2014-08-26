<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>blog管理</title>
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
	str = "${obj.type}";
	if(isNotBlank(str)){
		$("#type").val(str);
	}
	
	setFormValidate();
});
function doSave(){
	$('#inputForm').submit();
}
function setFormValidate(){
	$("#inputForm").validate({
		rules:{
			"obj.title":"required"
		},
		messages:{
			"obj.title":"请输入标题"
		},
		submitHandler: function(form) {
			if($("#inputForm").valid()){
				$("#inputForm").ajaxSubmit({
					dataType:"json",
			 		success:function(data){
			 			if(data.status == 0){
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
<form action="${ctx}/sys/episode/save" method="post" id="inputForm">&nbsp; 
<input type="hidden" name="obj.id" id="id" value="${obj.id}" />
<input type="hidden" name="obj.pid" id="pid" value="${obj.pid}" />
<div class="pop ">
     <div class="popContent">
      	<table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="16%" align="right">名称：</td>
              <td  width="84%" height="50"><input class="w220" id="title" name="obj.title" type="text" value="${obj.title}" />
              	<span style="float:none;" class="red">* <label generated="true" for="title" class="error">${_msgFor_title}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">类型：</td>
              <td  width="84%" height="50">
              	<select name="obj.type" id="type">
              		<option value="Movie">Movie</option>
              		<option value="TV">TV</option>
              		<option value="Variety">Variety</option>
              	</select>
              </td>
            </tr>
             <tr>
              <td width="16%" align="right">年份：</td>
              <td  width="84%" height="50"><input class="w220" id="year" name="obj.year" type="text" value="${obj.year}" />
              	<span style="float:none;" class="red">* <label generated="true" for="year" class="error">${_msgFor_year}</label></span>
              </td>
            </tr>
             <tr>
              <td width="16%" align="right">集：</td>
              <td  width="84%" height="50"><input class="w220" id="current_episode" name="obj.current_episode" type="text" value="${obj.current_episode}" />
              	<span style="float:none;" class="red">* <label generated="true" for="current_episode" class="error">${_msgFor_current_episode}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">系列：</td>
              <td  width="84%" height="50"><input class="w220" id="series" name="obj.series" type="text" value="${obj.series}" />
              	<span style="float:none;" class="red"> 2014-06-07<label generated="true" for="series" class="error">${_msgFor_series}</label></span>
              </td>
            </tr>
            
            <tr>
              <td width="16%" align="right">简介：</td>
              <td  width="84%" height="50">
              	<textarea name="obj.summary" id="summary" style="width: 350px;height: 80px;">${obj.summary}</textarea>
              </td>
            </tr>
          </table>
	<p class="pBtn"><a class="save" href="javascript:doSave();" title="保  存">保  &nbsp;存</a><a class="back" href="javascript:frameElement.lhgDG.cancel();" title="返  回">返 &nbsp; 回</a></p>
      </div>
  </div>
</form>
</body>
</html>