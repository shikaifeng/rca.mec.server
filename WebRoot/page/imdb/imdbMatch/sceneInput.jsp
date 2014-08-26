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
	        'script': '${ctx}/sys/feedXls/input/'+$("#imdb_episode_id").val(),
	        'buttonImg': '${stCtx}/images/btn_imageAdd.png',
	        'auto': true,
	        'queueID':'showFileId',
	        'sizeLimit':0,
	        'fileDataName':"upload",
	        'fileDesc':'支持格式:xls.',
	        'fileExt':'*.xls',
	        'method':'GET',
	        //'scriptData':{"program_id":"1"},
//	        'scriptData'  : {'program_id':"1",'imdb_episode_id':"2"},
	        'scriptData': {'program_id':$("#program_id").val(),'imdb_episode_id':$("#imdb_episode_id").val()},
	        'onUploadStart' : function(file) {  
	        	alert("dd");
	        	if($("#program_id").val() == ''){
	        		alert("请输入节目id");
	        		return false;
	        	}
	        	if($("#imdb_episode_id").val() == ''){
	        		alert("请输入节目id");
	        		return false; 
	        	}
	        	return true;
	        },
	        'onSelect':function(event,queueID,fileObj){
	        	alert($("#program_id").val() +" "+$("#imdb_episode_id").val());
	        	
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
				//"imdb_episode_id" : "required"
			},
			messages : {
				//"imdb_episode_id" : "请输入imdb的剧集id"
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
<form action="${ctx}/sys/imdbMatch/matchScene" method="post" id="inputForm"> 
<div class="pop ">
     <div class="popContent">
      	<table width="100%" border="0" cellspacing="0" cellpadding="0">
           </tr>
              <tr>
              <td width="16%" align="right">imdb剧集ID：</td>
              <td  width="84%" height="50"><input class="w220" id="imdb_episode_id" name="imdb_episode_id" type="text" value="${imdb_episode_id}" />
              	<span style="float:none;" class="red">* <label generated="true" for="imdb_episode_id" class="error">imdb剧集id如：/title/tt1408101/ ${_msgFor_imdb_episode_id}</label></span>
              </td>
            </tr>
             <tr>
              <td width="16%" align="right">开始时间：</td>
              <td  width="84%" height="50"><input class="w220" id="created_at" name="created_at" type="text" value="${created_at}" />
              	<span style="float:none;" class="red">* <label generated="true" for="created_at" class="error">开始时间如：2012-03-04 ${_msgFor_created_at}</label></span>
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
              	根据imdb的id，先删除之前的feed信息，再加入imdb的feed信息 <br />
              </td>
            </tr>
          </table>
	<p class="pBtn"><a class="save" href="javascript:doSave();" title="保  存">提交解析</a></p>
      </div>
  </div>
</form>
</body>
</html>
