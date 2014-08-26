<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>魔盒导入管理</title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet" href="${stCtx}/css/bcisc_beta1.0.css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<%-- <script src="${stCtx}/js/jquery.validate-1.9.min.js" type="text/javascript" ></script> --%>

<script src="${stCtx}/js/uploadify/jquery.uploadify.v2.1.4.js" type="text/javascript"></script>
<script src="${stCtx}/js/uploadify/swfobject.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function() {
	//setFormValidate();
	
	$("#upload").uploadify({
        'uploader': '${stCtx}/js/uploadify/uploadify.swf',
        'script': '${ctx}/sys/tmall_box_feed/xls_save',
        'buttonImg': '${stCtx}/images/btn_upload.png',
        'width': 116,
        'height': 28,
        'auto': true,
        'queueID':'showFileId',
        'sizeLimit':0,
        'fileDataName':"upload",
        'fileDesc':'支持格式:xls.',
        'fileExt':'*.xls',
        'method':'GET',
        'scriptData': {'episode_id':$("#episode_id").val()},
        'onUploadStart' : function(file) {
        	if($("#episode_id").val() == ''){
        		alert("请输入剧集id");
        		return false; 
        	}
        	$('#msg').val("");
        	return true;
        },
        'onSelect':function(event,queueID,fileObj){
        	$("#uploadErrorMsg").html("上传中...");
    	},
        'onComplete': function(event, ID, fileObj, res, data) {
        	res = JSON.parse(res);
 			$('#msg').val(res.msg);
        	if(res.status == '0'){
        		window.parent.load_all_feed();
 				frameElement.lhgDG.cancel();
 			}
        	$("#uploadErrorMsg").html("");
     	},
     	'onProgress'  : function(event,ID,fileObj,data) {
            $("#uploadErrorMsg").html("上传进度"+Math.round(data.bytesLoaded / fileObj.size * 100)+"%");
        },
     	'onError':function(event,queueId,fileObj,errorObj){
     		//$("#saveButtonId").removeAttr("disabled");
        	if(errorObj.type=="File Size"){
     			$("#uploadErrorMsg").html("选择文件过大");	
     		}else{
     			$("#uploadErrorMsg").html("上传失败，请稍后重试");
     		}
     	}
     });
});
function doSave(){
	$('#inputForm').submit();
}
function setFormValidate(){
	$("#inputForm").validate({
		rules:{
			"upload":"required"
		},
		messages:{
			"upload":"请选择导入文件"
		},
		submitHandler: function(form) {
			if($("#inputForm").valid()){
				$('#msg').val("努力加载中");
				$("#inputForm").ajaxSubmit({
					dataType:"json",
			 		success:function(data){
			 			if(data.status == '0'){
			 				window.parent.load_all_feed();
			 				frameElement.lhgDG.cancel();
			 			}
			 			$('#msg').val(data.msg);
				    },
					error : function(data) {
						$('#msg').val("异常，请稍后重试");
					}
				}); 
            }
        }
	});
}
</script>
</head>
<body style=" background:#f6f9fb;">
<form action="${ctx}/sys/tmall_box_feed/xls_save" method="post" id="inputForm" enctype="multipart/form-data">
<input type="hidden" name="episode_id" id="episode_id" value="${episode.id}" />
<div class="pop ">
     <div class="popContent">
      	<table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="16%" align="right">节目信息：</td>
              <td  width="84%" height="50"> ${program.title}<c:if test="${program.current_season!='' && program.current_season!=0}">第${program.current_season}季</c:if> 
	            <c:if test="${episode.current_episode!='' && episode.current_episode!=0}">第${episode.current_episode}集</c:if>
	             <c:if test="${episode.series!=''}">${episode.series}</c:if>
              	<span style="float:none;" class="red"><label generated="true" for="title" class="error">${_msgFor_title}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">文件：</td>
              <td  width="84%" height="50"><input class="w220" id="upload" name="upload" type="file" />
              	<span style="float:none;" class="red">* 导入会先删除原来的feed信息<label generated="true" for="upload" class="error" id="uploadErrorMsg">${_msgFor_upload}</label></span>
              </td>
            </tr>
            <%--  <tr>
              <td width="16%" align="right">模板：</td>
              <td  width="84%" height="50"><a href="${ctx}/page/feed/tmallbox/tmallbox_feed.xls">点击下载</a></td>
            </tr> --%>
            <tr>
              <td width="16%" align="right">导入信息：</td>
              <td  width="84%" height="50">
              	<textarea name="msg" id="msg" style="width: 400px;height: 220px;"></textarea>
              </td>
            </tr>
          </table>
	<p class="pBtn"><!-- <a class="save" href="javascript:doSave();" title="保  存">保  &nbsp;存</a> --><a class="back" href="javascript:frameElement.lhgDG.cancel();" title="返  回">关 &nbsp; 闭</a></p>
      </div>
  </div>
</form>
</body>
</html>