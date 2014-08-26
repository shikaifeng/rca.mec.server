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
		$("#upload").uploadify({
	        'uploader': '${stCtx}/js/uploadify/uploadify.swf',
	        'script': '${ctx}/sys/feedXls/input/'+$("#episode_id").val(),
	        'buttonImg': '${stCtx}/images/btn_imageAdd.png',
	        'auto': true,
	        'queueID':'showFileId',
	        'sizeLimit':0,
	        'fileDataName':"upload",
	        'fileDesc':'支持格式:xls.',
	        'fileExt':'*.xls',
	        'method':'GET',
	        //'scriptData':{"program_id":"1"},
//	        'scriptData'  : {'program_id':"1",'episode_id':"2"},
	        'scriptData': {'program_id':$("#program_id").val(),'episode_id':$("#episode_id").val()},
	        'onUploadStart' : function(file) {  
	        	alert("dd");
	        	if($("#program_id").val() == ''){
	        		alert("请输入节目id");
	        		return false;
	        	}
	        	if($("#episode_id").val() == ''){
	        		alert("请输入节目id");
	        		return false; 
	        	}
	        	return true;
	        },
	        'onSelect':function(event,queueID,fileObj){
	        	alert($("#program_id").val() +" "+$("#episode_id").val());
	        	
	    		$("#uploadErrorMsg").html("上传中...");
	    	},
	        'onComplete': function(event, ID, fileObj, res, data) {
	        	$("#msg").val(res);
	     	},
	     	'onProgress'  : function(event,ID,fileObj,data) {
	            $("#uploadErrorMsg").html("上传中 上传"+Math.round(data.bytesLoaded / fileObj.size * 100)+"%");
	        },
	     	'onError':function(event,queueId,fileObj,errorObj){
	     		//$("#saveButtonId").removeAttr("disabled");
	        	if(errorObj.type=="File Size"){
	     			$("#uploadErrorMsg").html("选择的过大");	
	     		}else{
	     			$("#uploadErrorMsg").html("上传失败，请稍后重试");
	     		}
	     	}
	     });
	});
	
	function setFormValidate() {
		$("#inputForm").validate({
			rules : {
				"upload" : "required",
				"program_id" : "required",
				"episode_id" : "required"
			},
			messages : {
				"upload" : "请输入文件表格路径",
				"program_id" : "请添加节目ID",
				"episode_id" : "请添加剧集ID"
			},
			submitHandler : function(form) {
				if ($("#inputForm").valid()) {
					$("#inputForm").ajaxSubmit({
						dataType : "text",
						success : function(data) {
							$('#msg').val(data);
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
<form action="${ctx}/sys/feedXls/save" method="post" id="inputForm" > 
<div class="pop ">
     <div class="popContent">
      	<table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="16%" align="right">节目ID：</td>
              <td  width="84%" height="50"><input class="w220" id="program_id" name="program_id" type="text" value="${program_id}" />
              	<span style="float:none;" class="red">* <label generated="true" for="program_id" class="error">${_msgFor_program_id}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">剧集ID：</td>
              <td  width="84%" height="50"><input class="w220" id="episode_id" name="episode_id" type="text" value="${episode_id}" />
              	<span style="float:none;" class="red">* <label generated="true" for="episode_id" class="error">${_msgFor_episode_id}</label></span>
              </td>
            </tr>
           <%--   <tr>
              <td width="16%" align="right">文件路径：</td>
              <td  width="84%" height="50"><input class="w220" id="file_path" name="file_path" type="text" value="${file_path}" />
              	<span style="float:none;" class="red">* <label generated="true" for="file_path" class="error">${_msgFor_file_path}</label></span>
              </td>
            </tr>--%>
           <tr>
              <td width="16%" align="right">feed文件：</td>
              <td  width="84%" height="50"><input name="upload" id="upload" type="file" />
              	<span style="float:none;" class="red">* <label generated="true" for="upload" class="error">${_msgFor_upload}</label></span>
              </td>
            </tr> 
            <tr>
              <td width="16%" align="right">返回内容：</td>
              <td  width="84%" height="50">
              	<textarea name="msg" id="msg" style="width: 550px;height: 150px;">${msg}</textarea>
              	${_msgFor_msg}
              </td>
            </tr>
          </table>
	<p class="pBtn"><a class="save" href="javascript:doSave();" title="保  存">保  &nbsp;存</a></p>
      </div>
  </div>
</form>
</body>
</html>
