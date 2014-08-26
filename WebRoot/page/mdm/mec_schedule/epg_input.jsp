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
	$("#channel_id").val("${obj.mec_channel_id}");
	
	if(isNotBlank($("#id").val())){
		document.getElementById('mdm_program_id').disabled=true;
		document.getElementById('mdm_episode_id').disabled=true;
	}
});
	$(document).ready(function() {
		setFormValidate();
	});
	function doSave() {
		$('#inputForm').submit();
	}
	function setFormValidate() {
		$("#inputForm").validate({
			rules : {
				"obj.mec_channel_id" : "required"
			},
			messages : {
				"obj.mec_channel_id" : "请选择频道"
			},
			submitHandler : function(form) {
				var flag = true;
				if(!isNotBlank($("#start_at").val())){
					$("#start_at_tip").html("请输入开始时间");
					$("#start_at_tip").show();
					flag = false;
				}
				if(!flag){
					return;
				}
				if ($("#inputForm").valid()) {
					$("#inputForm").ajaxSubmit({
						dataType : "json",
						success : function(data) {
							if (data.status == 0) {
								getLhgdialogParent().search();
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
	<form action="${ctx}/sys/mec_schedule/epg_save" method="post"
		id="inputForm">
		&nbsp; <input type="hidden" name="obj.id" id="id" value="${obj.id}" />
		<div class="pop ">
			<div class="popContent">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="16%" align="right">频道选择：</td>
						<td width="84%" height="50"><select id="channel_id"
							name="obj.mec_channel_id" style="width: 160px;">
								<c:forEach var="obj" items="${channels}">
									<option value="${obj.id}">${obj.name}</option>
								</c:forEach>
						</select> <span style="float: none;" class="red">* <label
								generated="true" for="channel_id" class="error">${_msgFor_channel_id}</label></span>
						</td>
					</tr>
					<tr>
						<td width="16%" align="right">节目id：</td>
						<td width="84%" height="50">
						<input id = "mdm_program_id" name = "obj.mdm_program_id" type="text" style="width: 160px;" value="${obj.mdm_program_id}"/>
						<span style="float: none;" class="red">* <label
								generated="true" for="program_id" class="error">${_msgFor_program_id}</label></span>
						</td>
					</tr>
					<tr>
						<td width="16%" align="right">剧集id：</td>
						<td width="84%" height="50">
						<input id = "mdm_episode_id" name = "obj.mdm_episode_id" type="text" style="width: 160px;" value="${obj.mdm_episode_id}"/>
						<span style="float: none;" class="red">* <label
								generated="true" for="episode_id" class="error">${_msgFor_episode_id}</label></span>
						</td>
					</tr>
					<tr>
						<td width="16%" align="right">开始时间：</td>
						<td width="84%" height="50">
						<input id="start_at" name="start_at" value="${obj.mec_start_at}" type="text" 
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate" style="width:150px" readonly="readonly"/>  
						<span style="float: none;" class="red">*如2014-7-19 17:13:01 <label
								generated="true" for="start_at" class="error" id="start_at_tip">${_msgFor_start_at}</label></span>
						</td>
					</tr>
					<tr>
						<td width="16%" align="right">结束时间：</td>
						<td width="84%" height="50">
						<input id="end_at" name="end_at" value="${obj.mec_end_at}" type="text" 
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate" style="width:150px" readonly="readonly"/>  
						<span style="float: none;" class="red">如2014-7-19 19:30:01 <label
								generated="true" for="end_at" class="error">${_msgFor_end_at}</label></span>
						</td>
					</tr>
				</table>
				<p class="pBtn">
					<a class="save" href="javascript:doSave();" title="保  存">保
						&nbsp;存</a><a class="back"
						href="javascript:frameElement.lhgDG.cancel();" title="关 闭">关&nbsp; 闭</a>
				</p>
			</div>
		</div>
	</form>
</body>
</html>