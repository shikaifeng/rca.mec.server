<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/page/common/jscommon.jsp"%>
<script src="${stCtx}/js/jqueryformValidator/formValidator.js" type="text/javascript"></script>
<script src="${stCtx}/js/jqueryformValidator/formValidatorRegex.js" type="text/javascript" ></script>
<link href="${stCtx}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${stCtx}/css/edit.css" rel="stylesheet" type="text/css" />


<script type="text/javascript">
$(document).ready(function() {
	setEditValidate();
});

//保存
function doSave(){
	$('#inputForm').submit();
}

$("#inputForm").ajaxSubmit({
	success:function(data){
		if(data=='1'){
			getLhgdialogParent().search();	
			frameElement.lhgDG.cancel();
		}
    }
});
		

//设置添加修改时候的校验规则
function setEditValidate(){
	$.formValidator.initConfig({formID:"inputForm",onSuccess:function(){
		$("#inputForm").ajaxSubmit({
			dataType:"text",
	 		success:function(data){
				if(data=='1'){
					getLhgdialogParent().search();
					frameElement.lhgDG.cancel();
				}
		    }
		});
		return false;
	}});
	$("#name").formValidator({onFocus:"请输入名称"}).inputValidator({min:1,onError:"名称不能为空"});
}
</script>
</head>

<body>
<form action="${ctx}/sys/enumValue/save" method="post" id="inputForm">
<input type="hidden" name="id" id="id" value="${object.id}" />
<input type="hidden" name="type" id="type" value="${type}" />
<div class="edit_com edit_com_box" style="min-height: 30px;">
  <ul>
  	<li>
	    <div class="r_edit_con" style="width: 100px;"> 名称：</div>
		<div class="r_edit_inp">
		    <span><input style="width:220px;" type="text" name="name" id="name" maxlength="100" value="${object.name}" /></span>
		</div>
		<div class="r_edit_inp" id="nameTip" style="width:180px" ></div>
	</li>
  </ul>
 <div class="btn_edit_caozuo">
		<input class="btn_cz" type="button" value="保 存" onclick="doSave()"/>
		<input class="btn_cz" type="button" value="关闭" onclick="frameElement.lhgDG.cancel();"/>
   </div><!--btn_edit_caozuo结束-->
</div>
</form>
</body>