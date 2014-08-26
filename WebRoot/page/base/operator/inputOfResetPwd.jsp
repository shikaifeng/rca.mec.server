<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${systemName}-添加员工</title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet" href="${stCtx}/css/bcisc_beta1.0.css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<script src="${stCtx}/js/jqueryformValidator/formValidator.js" type="text/javascript"></script>
<script src="${stCtx}/js/jqueryformValidator/formValidatorRegex.js" type="text/javascript" ></script>
<style type="text/css">
#uploadUploader{
	width:83px;
	height:83px;
}
</style>
<script type="text/javascript">
$(document).ready(function() {
	setEditValidate();
	setInitFormValidate();
});
function doSave(){
	if(checkFormValidate()) {
		$('#inputForm').submit();
	}
}
function setEditValidate(){
	$.formValidator.initConfig({formID:"inputForm",onSuccess:function(){
		$("#inputForm").ajaxSubmit({
			type:"post",
			dataType:"text",
	 		success:function(data){
	 			if(data==1) {
	 				alert("密码修改成功！");
	 				frameElement.lhgDG.cancel();
	 			} else {
	 				$("#pwd_tip").html("旧密码不正确");
	 			}
		    }
		});
		return false;
	}});
}
function checkPwd() {
	if(!$("#pwd").val().match(regexEnum.notempty)) {
		$("#pwd_tip").html("请输入旧密码");
		return false;
	} else {
		$("#pwd_tip").html("");
		return true;
	}
}
function checkNewPwd() {
	if(!$("#newPwd").val().match(regexEnum.notempty)) {
		$("#newpwd_tip").html("请输入新密码");
		return false;
	} else {
		$("#newpwd_tip").html("");
		return true;
	}
}
function checkRePwd() {
	if(!$("#rePwd").val().match(regexEnum.notempty)) {
		$("#repwd_tip").html("请输入确认密码");
		return false; 
	} else {
		if($("#rePwd").val()==$("#newPwd").val()) {
			$("#repwd_tip").html("");
			return true;
		} else {
			$("#repwd_tip").html("与新密码不一致");
			return false;
		}
	}
}
function setInitFormValidate() {
	$("#pwd").blur(function() {
		checkPwd();
	});
	
	$("#newPwd").blur(function() {
		checkNewPwd();
	});
	
	$("#rePwd").blur(function() {
		checkRePwd();
	});
}
function checkFormValidate() {
	return checkPwd() && checkNewPwd()&& checkRePwd();
}
</script>
</head>
<body style=" background:#f6f9fb;">
<form action="${ctx}/base/operator/resetPwd" method="post" id="inputForm">
<input type="hidden" name="id" value="${object.id}"/>
<div class="pop ">
     <div class="popContent" >
      	<table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="14%" align="right">旧密码：</td>
              <td  width="86%" height="50">
              	<input id="pwd" name="pwd" type="password" class="w220"/><span style="float: none;" class="red"> *</span>
              	<div id="pwd_tip" class="red" style="float: right; margin-right: 100px; margin-top: 5px;"></div>
              </td>
            </tr>
            <tr>
              <td width="14%" align="right">新密码：</td>
              <td  width="86%" height="50">
              	<input id="newPwd" name="newPwd" type="password" class="w220"/><span style="float: none;" class="red"> *</span>
              	<div id="newpwd_tip" class="red" style="float: right; margin-right: 100px; margin-top: 5px;"></div>
              </td>
            </tr>
            <tr>
              <td width="14%" align="right">确认密码：</td>
              <td  width="86%" height="50">
              	<input id="rePwd" name="rePwd" type="password" class="w220" /><span style="float: none;" class="red"> *</span>
              	<div id="repwd_tip" class="red" style="float: right; margin-right: 87px; margin-top: 5px;"></div>
              </td>
            </tr>
          </table>
		<p class="pBtn"><a class="save" href="javascript:doSave()" title="保  存">保  &nbsp;存</a><a class="back" href="javascript:frameElement.lhgDG.cancel();" title="返  回">返 &nbsp; 回</a></p>
      </div>
</div>
</form>
</body>
</html>