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
				//"upload" : "required"
			},
			messages : {
				//"upload" : "请选择上传的文件"
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
<form action="${ctx}/sys/imdbMatch/save" method="post" id="inputForm"> 
<div class="pop ">
     <div class="popContent">
      	<table width="100%" border="0" cellspacing="0" cellpadding="0">
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
	<p class="pBtn"><a class="save" href="javascript:doSave();" title="保  存">提交匹配</a></p>
      </div>
  </div>
</form>
</body>
</html>
