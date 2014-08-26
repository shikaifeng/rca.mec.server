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
				"upload" : "required"
			},
			messages : {
				"upload" : "请选择上传的文件"
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
<form action="${ctx}/sys/imdbFeed/save" method="post" id="inputForm" enctype="multipart/form-data"> 
<div class="pop ">
     <div class="popContent">
     	<p class="pBtn"><a class="save" href="javascript:doSave();" title="保  存">提交解析</a></p>
      	<table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="16%" align="right">feed文件：</td>
              <td  width="84%" height="50"><input name="upload" id="upload" type="file" />
              	<span style="float:none;" class="red">* <label generated="true" for="upload" class="error">上传文件类型只能上传zip压缩包或txt文件${_msgFor_upload}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">返回内容：</td>
              <td  width="84%" height="50">
              	<textarea name="msg" id="msg" style="width: 550px;height: 550px;">${msg}</textarea>
              	${_msgFor_msg}
              </td>
            </tr>
             <tr>
              <td width="16%" align="right">说明：</td>
              <td  width="84%" height="50" style="color: red">
              	上传文件类型只能上传zip压缩包，每部压缩成一个压缩包上传<br />
              	内容如果是电影：请在文件夹下分别保存：program.txt和event.txt <br />
              	如果是电视剧：请在文件夹下分别保存：program.txt，episode_季剧集.txt，event_季剧集.txt<br />
              	出错分类：<br />
              	1：event_0113.txt 场景文件，对应的季集信息部存在，请检查 。 表示：在imdb中没有该剧集，去imdb网站核对，如果没有，需要删除这个文件<br />
              	2：episode_0315.txt 应该是剧集信息文件，文件命名错误，请检查。表示：工具抓取完毕，保存到文件中时候命名错误，可以打开文件："@type": "imdb.api.title.fulldetails":表示剧集文件。  "@type": "imdb.api.video.events"：表示场景文件<br />
              	3: event_0315.txt 应该是剧集信息文件，文件命名错误，请检查。表示：工具抓取完毕，保存到文件中时候命名错误，可参考上条<br />
              	4：episode_0116.txt 所属的 parentTitleId不一致，请检查  http://www.imdb.com/title/tt0200276/ 是The West Wing 信息 : 表示不同的剧集文件，放到了同个文件夹下需要检查这个文件： 可以打开episode的文件查看：parentTitle 是否一致<br />
              	5：文件内容空,请检查：表示文件内无内容。<br />
              	6：其他问题联系开发<br />
              	<!-- 流程：1：先导入 2：下载图片 3：执行匹配 -->
              </td>
            </tr>
          </table>
      </div>
  </div>
</form>
</body>
</html>
