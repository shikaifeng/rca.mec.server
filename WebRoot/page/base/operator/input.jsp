<%@page import="tv.zhiping.common.Cons"%>
<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/page/common/jscommon.jsp"%>
<script src="${stCtx}/js/jquery.validate-1.9.min.js" type="text/javascript" ></script>
<script src="${stCtx}/js/uploadify/jquery.uploadify.v2.1.4.js" type="text/javascript"></script>
<script src="${stCtx}/js/uploadify/swfobject.js" type="text/javascript"></script>
<link href="${stCtx}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${stCtx}/css/edit.css" rel="stylesheet" type="text/css" />
<style type="text/css">
#inputForm label.error {
	height:20px;
	top:5px;
	left:300px;
	background:#FC3D2E;
	z-index:100;
	font-size:12px;
	color:#fff;
	padding:3px 10px 0 5px;
}
</style>

<script type="text/javascript">
$(document).ready(function() {
	$("#inputForm").validate({
		rules:{
			realName:"required",
			name:{
				required:true,
				remote:{
					type:"POST",
					url:"${ctx}/base/operator/nameValidate",
					data:{name:function(){return $("#name").val();},id:function(){return $("#id").val()}}
				}
			},
			email:"email",
			pwd:"required",
			confirmPwd:{required:true,equalTo:"#pwd"},
		},
		messages:{
			realName:"请输入姓名",
			name:{
				required:"请输入用户名",
				remote:"该用户名不可用",
			},
			email:"请输入正确的email地址",
			pwd:"请输入密码",
			confirmPwd:{required:"请输入确认密码",equalTo:"两次输入的密码不一致"}
		},
		submitHandler : function(){
			$.ajax({
				url:'${ctx}/base/operator/save',
				dataType:'text',
				type:'post',
				data:$('#inputForm').serialize(),
				error:function(){
					alert("提交出错，请稍后再试");
				},
				success:function(data){
					if(data == "1"){
						getLhgdialogParent().search();	
						frameElement.lhgDG.cancel();
					}
				}
			})
			return false;
		}
	});
	$("#upload").uploadify({
	    'uploader': '${stCtx}/js/uploadify/uploadify.swf',
	    'script': '${stCtx}/sigleUpload',
	    'buttonImg': '${stCtx}/images/btn_upload.png',
	    'auto': true,
	    'queueID':'showFileId',
	    'sizeLimit':512000,
	    'fileDataName':"upload",
	    'fileDesc':'支持格式:jpg/gif/jpeg/png/bmp.',
	    'fileExt':'*.jpg;*.gif;*.jpeg;*.png;*.bmp',
	    'onSelect':function(event,queueID,fileObj){
		$("#uploadErrorMsg").html("上传中...");
		$("#saveButtonId").attr("disabled","disabled");
		},
	    'onComplete': function(event, ID, fileObj, res, data) {
	    	$("#saveButtonId").removeAttr("disabled");
	    	$("#uploadErrorMsg").html("");
	    	res = $.parseJSON(res);
	    	if(res.msg){
	    		$("#uploadErrorMsg").html(res.msg);
	    	}else{
	        	$("#avatorImgId").attr("src",'${stCtx}'+"/"+uploadfile+"/"+res.path);
	        	$("#avator").val(res.path);
	        	$("#logoSize").val(res.size);
	        	$("#reduceAvatorSize").val(res.reduceSize);
	        	$("#uploadErrorMsg").html("上传成功");
	    	}
	 	},
	 	'onProgress'  : function(event,ID,fileObj,data) {
	 		$("#saveButtonId").attr("disabled","disabled");
	        $("#uploadErrorMsg").html("上传中 上传"+Math.round(data.bytesLoaded / fileObj.size * 100)+"%");
	    },
	 	'onError':function(event,queueId,fileObj,errorObj){
	 		$("#saveButtonId").removeAttr("disabled");
	    	if(errorObj.type=="File Size"){
	 			$("#uploadErrorMsg").html("选择的文件过大");	
	 		}else{
	 			$("#uploadErrorMsg").html("上传失败，请稍后重试");
	 		}
	 	}
	 });
});
</script>
</head>

<body>
<form action="${ctx}/base/operator/save" method="post" id="inputForm">
<input type="hidden" name="id" id="id" value="${object.id}" />
<input type="hidden" name="roleId" value="3" />
<input type="hidden" name="avator" id="avator" value="${object.avator}"/>
<div class="edit_com">
  <ul>
	<li>
	    <div class="r_edit_con"> 姓名：</div>
		<div class="r_edit_inp">
		    <span><input style="width:250px;" type="text" name="realName" id="realName" maxlength="100" value="${object.realName}" /></span>
		</div>
	</li>	
  	<li>
	    <div class="r_edit_con"> 头像：</div>
		<div class="r_edit_inp">
		   <div class="r_edit_img">
				<c:choose>
			    	<c:when test="${object.avator==null || object.avator==''}"> 
			    	 <img id="avatorImgId" width="100" height="100" src="${stCtx}/images/defaultphoto.jpg" />
			   		</c:when>
					<c:otherwise>
						<img id="avatorImgId" width="100" height="100" src="${ctx}/upload/${object.avator}" />
					</c:otherwise>
		   		</c:choose>
			</div>
			<div class="r_img_btn">
			   	<input name="upload" id="upload" type="file" />
				<label id="uploadErrorMsg" style="font-size:12px;color:#0f0f0f;">建议上传大小96*96</label>
			</div>
		</div>
		<div class="r_edit_inp" id="avatorTip" style="width:120px" ></div>
	</li>
	<li>
	    <div class="r_edit_con">用户名：</div>
		<div class="r_edit_inp">
		    <span><input style="width:250px;" type="text" name="name" id="name" maxlength="100" value="${object.name}" /></span>
		</div>
	</li>	
	<li>
	    <div class="r_edit_con"> 邮箱：</div>
		<div class="r_edit_inp">
		    <span><input style="width:250px;" type="text" name="email" id="email" maxlength="100" value="${object.email}" /></span>
		</div>
	</li>	
	<li>
	    <div class="r_edit_con"> 手机号：</div>
		<div class="r_edit_inp">
		    <span><input style="width:250px;" type="text" name="mobile" id="mobile" maxlength="20" value="${object.mobile}" /></span>
		</div>
	</li>
	<li>
	    <div class="r_edit_con">密码：</div>
		<div class="r_edit_inp">
		    <span><input style="width:250px;" type="password" name="pwd" id="pwd" maxlength="100" /></span>
		</div>
	</li>
	<li>
	    <div class="r_edit_con">确认密码：</div>
		<div class="r_edit_inp">
		    <span><input style="width:250px;" type="password" name="confirmPwd" id="confirmPwd" maxlength="100" /></span>
		</div>
	</li>
  </ul>
 <div class="btn_edit_caozuo">
		<input class="btn_cz" type="submit" value="保 存" />
		<input class="btn_cz" type="button" value="关闭" onclick="frameElement.lhgDG.cancel();"/>
   </div><!--btn_edit_caozuo结束-->
</div>
</form>
</body>