<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>抽奖时间设置管理</title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet" href="${stCtx}/css/bcisc_beta1.0.css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<script src="${stCtx}/js/jquery.validate-1.9.min.js" type="text/javascript" ></script>
<script src="${stCtx}/js/setTime.js" type="text/javascript"></script>

<script type="text/javascript">
$(document).ready(function() {
	var str = secondFormate("${obj.start_time}");
	$("#start_time").val(str);
	
	str = secondFormate("${obj.end_time}");
	$("#end_time").val(str);
	
	setFormValidate();
	set_upload();
});

function doSave(){
	$('#inputForm').submit();
}

function setFormValidate(){
	$("#inputForm").validate({
		rules:{
		},
		messages:{
		},
		submitHandler: function(form) {
			if($("#inputForm").valid()){
				$("#start_time").val(getTime($("#start_time").val()));
				//$("#end_time").val(getTime($("#end_time").val()));
				$("#inputForm").ajaxSubmit({
					dataType:"json",
			 		success:function(data){
			 			if(data.status == 0){
			 				window.parent.load_lucky_draw_event();
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
<form action="${ctx}/sys/lucky_draw_episode/save" method="post" id="inputForm">&nbsp; 
<input type="hidden" name="obj.id" id="id" value="${obj.id}" />

<div class="pop ">
     <div class="popContent">
      	<table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="16%" align="right">开始时间：</td>
              <td  width="84%" height="50"><input class="w220" id="start_time" name="obj.start_time" type="text" value="${obj.start_time}" onclick="_SetTime(this)" />
              	<span style="float:none;" class="red"><label generated="true" for="start_time" class="error"></label></span>
              </td>
            </tr>
            <%--  <tr>
              <td width="16%" align="right">截止时间：</td>
              <td  width="84%" height="50"><input class="w220" id="end_time" name="obj.end_time" type="text" value="${obj.end_time}" onclick="_SetTime(this)" />
              	<span style="float:none;" class="red"><label generated="true" for="end_time" class="error"></label></span>
              </td>
            </tr> --%>
          </table>
	<p class="pBtn"><a class="save" href="javascript:doSave();" title="保  存">保  &nbsp;存</a><a class="back" href="javascript:frameElement.lhgDG.cancel();" title="返  回">关 &nbsp; 闭</a></p>
      </div>
  </div>
</form>
</body>
</html>
