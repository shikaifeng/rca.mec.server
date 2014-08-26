<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>明星管理</title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet" href="${stCtx}/css/bcisc_beta1.0.css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<script src="${stCtx}/js/jquery.validate-1.9.min.js" type="text/javascript" ></script>
<script src="${stCtx}/js/setTime.js" type="text/javascript"></script>

<script src="${stCtx}/js/uploadify/jquery.uploadify.v2.1.4.js" type="text/javascript"></script>
<script src="${stCtx}/js/uploadify/swfobject.js" type="text/javascript"></script>

<script type="text/javascript">
$(document).ready(function() {
	var str = secondFormate("${element.start_time}");
	$("#start_time").val(str);
	
	str = secondFormate("${element.end_time}");
	$("#end_time").val(str);
	
	setFormValidate();
//	set_upload();
});

function set_upload(){
	$("#upload").uploadify({
        'uploader': '${stCtx}/js/uploadify/uploadify.swf',
        'script': '${ctx}/sys/upload/sign_save',
        'buttonImg': '${stCtx}/images/btn_upload.png',
        'width': 116,
        'height': 28,
        'auto': true,
        'queueID':'showFileId',
        'sizeLimit':0,
        'fileDataName':"upload",
        'fileDesc':'支持格式:jpg/gif/jpeg/png/bmp.',
        'fileExt':'*.jpg;*.gif;*.jpeg;*.png;*.bmp',
        'method':'GET',
        'onSelect':function(event,queueID,fileObj){
        	$("#uploadErrorMsg").html("上传中...");
    	},
        'onComplete': function(event, ID, fileObj, res, data) {
        	res = JSON.parse(res);
        	$("#avatarImgId").attr("src",res.path);
        	$("#avatar").val(res.path);
        	
        	$("#uploadErrorMsg").html("");
     	},
     	'onProgress'  : function(event,ID,fileObj,data) {
            $("#uploadErrorMsg").html("上传进度"+Math.round(data.bytesLoaded / fileObj.size * 100)+"%");
        },
     	'onError':function(event,queueId,fileObj,errorObj){
     		if(errorObj.type=="File Size"){
     			$("#uploadErrorMsg").html("选择文件过大");	
     		}else{
     			$("#uploadErrorMsg").html("上传失败，请稍后重试");
     		}
     	}
     });
}

function doSave(){
	$('#inputForm').submit();
}

function setFormValidate(){
	$("#inputForm").validate({
		rules:{
			"obj.name":"required",
			"obj.description":{required:true,maxlength:30},
			"upload":{accept:'.jpg'}
		},
		messages:{
			"obj.name":"请输入名称",
			"obj.description":"请输入简介",
			"upload":"上传的文件格式不正确"
		},
		submitHandler: function(form) {
			if($("#inputForm").valid()){
				var flag = true;
				var str = $("#avatar").val();
				if(!isNotBlank(str) && !isNotBlank($("#upload").val())){
					$("#uploadErrorMsg").html("请上传头像");
					$("#uploadErrorMsg").show();
					flag = false;
				}
				
				if(!flag){
					return;
				}
				
				var start = getTime($("#start_time").val());
				var end = getTime($("#end_time").val());				
				if(isNotBlank(start) && isNotBlank(end) && end<start){
					$("#timeErrorMsg").html("时间不合格");
					$("#timeErrorMsg").show();
					return;
				}
				$("#start_time").val(start);
				$("#end_time").val(end);
				
				$("#inputForm").ajaxSubmit({
					dataType:"json",
			 		success:function(data){
			 			if(data.status == 0){
			 				window.parent.load_mec_person();
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
<form action="${ctx}/sys/mec_person/save_element" method="post" id="inputForm"  enctype="multipart/form-data">&nbsp; 
<input type="hidden" name="element.id" id="element_id" value="${element.id}" />
<input type="hidden" name="element.program_id" id="program_id" value="${element.program_id}" />
<input type="hidden" name="element.episode_id" id="episode_id" value="${element.episode_id}" />
<input type="hidden" name="element.scene_id" id="scene_id" value="${element.scene_id}" />

<input type="hidden" name="obj.id" id="id" value="${obj.id}" />
<input type="hidden" name="obj.avatar" id="avatar" value="${obj.avatar}" />

<div class="pop ">
     <div class="popContent">
      	<table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="16%" align="right">名称：</td>
              <td  width="84%" height="50"><input class="w220" id="name" name="obj.name" type="text" value="${obj.name}" maxlength="10"/>
              	<span style="float:none;" class="red">*10字符内 <label generated="true" for="name" class="error">${_msgFor_name}</label></span>
              </td>
            </tr>
       		<tr>
              <td width="12%" align="right">头像：</td>
              <td  width="88%" height="50">
              	<c:choose>
			    	<c:when test="${obj.show_avatar==null || obj.show_avatar==''}"> 
			    	 <img id="avatarImgId" width="50" height="50" src="${stCtx}/images/defaultphoto.jpg" />
			   		</c:when>
					<c:otherwise>
						<img id="avatarImgId" width="50" height="50" src="${obj.show_avatar}" />
					</c:otherwise>
		   		</c:choose><br />
		   		<input name="upload" id="upload" type="file" />
			   	<label id="uploadErrorMsg" class="error" style="font-size:12px;color:#0f0f0f;"><!-- 建议上传大小160*90 --></label>
			   	<span style="float:none;" class="red">* <label generated="true" for="avatar" class="error">${_msgFor_avatar}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">百科URL：</td>
              <td  width="84%" height="50"><input class="w220" id="baike_url" name="obj.baike_url" type="text" value="${obj.baike_url}" maxlength="122"/>
              	<span style="float:none;" class="red"> <label generated="true" for="baike_url" class="error">${_msgFor_baike_url}</label></span>
              </td>
            </tr>
             <tr>
              <td width="16%" align="right">开始时间：</td>
              <td  width="84%" height="50"><input class="w220" id="start_time" name="element.start_time" type="text" value="${element.start_time}" onclick="_SetTime(this)" />
              	<span style="float:none;" class="red"><label generated="true" for="start_time" class="error"></label></span>
              </td>
            </tr>
             <tr>
              <td width="16%" align="right">截止时间：</td>
              <td  width="84%" height="50"><input class="w220" id="end_time" name="element.end_time" type="text" value="${element.end_time}" onclick="_SetTime(this)" />
              	<span style="float:none;" class="red"><label generated="true" for="end_time" class="error"></label></span>
              	<label id="timeErrorMsg" class="red" style="font-size:12px;"></label>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">简介：</td>
              <td  width="84%" height="50">
              	<textarea name="obj.description" id="description" style="width: 350px;height: 80px;">${obj.description}</textarea>
              	<span style="float:none;" class="red">*30字符内 <label generated="true" for="name" class="error">${_msgFor_description}</label></span>
              </td>
            </tr>
          </table>
	<p class="pBtn"><a class="save" href="javascript:doSave();" title="保  存">保  &nbsp;存</a><a class="back" href="javascript:frameElement.lhgDG.cancel();" title="返  回">返 &nbsp; 回</a></p>
      </div>
  </div>
</form>
</body>
</html>
