<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>blog管理</title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet" href="${stCtx}/css/bcisc_beta1.0.css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<script src="${stCtx}/js/jquery.validate-1.9.min.js" type="text/javascript" ></script>
<style type="text/css">
#uploadUploader{
	width:83px;
	height:83px;
}
</style>
<script type="text/javascript">
$(document).ready(function() {
	var str = "${obj.gender}";
	if(isNotBlank(str)){
		$("#gender").val(str);
	}
	str = "${obj.blood_type}";
	if(isNotBlank(str)){
		$("#blood_type").val(str);
	}
	setFormValidate();
});
function doSave(){
	$('#inputForm').submit();
}
function setFormValidate(){
	$("#inputForm").validate({
		rules:{
			"obj.name":"required"
		},
		messages:{
			"obj.name":"请输入名称"
		},
		submitHandler: function(form) {
			if($("#inputForm").valid()){
				$("#inputForm").ajaxSubmit({
					dataType:"json",
			 		success:function(data){
			 			if(data.status == 0){
				 			getLhgdialogParent().search();
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
<form action="${ctx}/sys/person/save" method="post" id="inputForm">&nbsp; 
<input type="hidden" name="obj.id" id="id" value="${obj.id}" />
<div class="pop ">
     <div class="popContent">
      	<table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="16%" align="right">名称：</td>
              <td  width="84%" height="50"><input class="w220" id="title" name="obj.name" type="text" value="${obj.name}" />
              	<span style="float:none;" class="red">* <label generated="true" for="name" class="error">${_msgFor_name}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">英文名称：</td>
              <td  width="84%" height="50"><input class="w220" id="title" name="obj.name_en" type="text" value="${obj.name_en}" />
              	<span style="float:none;" class="red"> <label generated="true" for="name_en" class="error">${_msgFor_name_en}</label></span>
              </td>
            </tr>
       		<tr>
              <td width="12%" align="right">头像：</td>
              <td  width="88%" height="50">
              	<c:choose>
			    	<c:when test="${obj.avatar==null || obj.avatar==''}"> 
			    	 <img id="avatorImgId" width="50" height="50" src="${stCtx}/images/defaultphoto.jpg" />
			   		</c:when>
					<c:otherwise>
						<img id="avatorImgId" width="50" height="50" src="${obj.avatar}" />
					</c:otherwise>
		   		</c:choose><br />
              </td>
            </tr>
             <tr>
              <td width="16%" align="right">头像下载URL：</td>
              <td  width="84%" height="50"><input class="w220" id="avatar_mtime" name="obj.avatar_mtime" type="text" value="${obj.avatar_mtime}" />
              	<span style="float:none;" class="red"> <label generated="true" for="avatar_mtime" class="error">${_msgFor_avatar_mtime}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">aka：</td>
              <td  width="84%" height="50"><input class="w220" id="aka" name="obj.aka" type="text" value="${obj.aka}" />
              	<span style="float:none;" class="red"> <label generated="true" for="aka" class="error">${_msgFor_aka}</label></span>
              </td>
            </tr>
             <tr>
              <td width="16%" align="right">aka_en：</td>
              <td  width="84%" height="50"><input class="w220" id="aka_en" name="obj.aka_en" type="text" value="${obj.aka_en}" />
              	<span style="float:none;" class="red"> <label generated="true" for="aka_en" class="error">${_msgFor_aka_en}</label></span>
              </td>
            </tr>
             <tr>
              <td width="16%" align="right">性别：</td>
              <td  width="84%" height="50">
              	<select name="obj.gender" id="gender">
              		<option value="privacy">保密</option>
              		<option value="male">男性</option>
              		<option value="female">女性</option>
              	</select>
              </td>
            </tr>
             <tr>
              <td width="16%" align="right">血型：</td>
              <td  width="84%" height="50">
              	<select name="obj.blood_type" id="blood_type">
              		<option value="">--请选择--</option>
              		<option value="AB型">AB型</option>
              		<option value="A型">A型</option>
              		<option value="B型">B型</option>
              		<option value="O型">O型</option>
              	</select>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">出生日期：</td>
              <td  width="84%" height="50"><input class="w220" id="birthday" name="obj.birthday" type="text" value="${obj.birthday}" />
              <span style="float:none;" class="red"> <label generated="true" for="birthday" class="error">${_msgFor_birthday}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">身高：</td>
              <td  width="84%" height="50"><input class="w220" id="height" name="obj.height" type="text" value="${obj.height}" />
              <span style="float:none;" class="red"> <label generated="true" for="height" class="error">${_msgFor_height}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">体重：</td>
              <td  width="84%" height="50"><input class="w220" id="weight" name="obj.weight" type="text" value="${obj.weight}" />
              <span style="float:none;" class="red"> <label generated="true" for="weight" class="error">${_msgFor_weight}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">毕业院校：</td>
              <td  width="84%" height="50"><input class="w220" id="education" name="obj.education" type="text" value="${obj.education}" />
              <span style="float:none;" class="red"> <label generated="true" for="education" class="error">${_msgFor_education}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">国籍：</td>
              <td  width="84%" height="50"><input class="w220" id="country" name="obj.country" type="text" value="${obj.country}" />
              <span style="float:none;" class="red"> <label generated="true" for="country" class="error">${_msgFor_country}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">出生地址：</td>
              <td  width="84%" height="50"><input class="w220" id="born_place" name="obj.born_place" type="text" value="${obj.born_place}" />
              <span style="float:none;" class="red"> <label generated="true" for="born_place" class="error">${_msgFor_born_place}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">星座：</td>
              <td  width="84%" height="50"><input class="w220" id="constellation" name="obj.constellation" type="text" value="${obj.constellation}" />
              <span style="float:none;" class="red"> <label generated="true" for="constellation" class="error">${_msgFor_constellation}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">简介：</td>
              <td  width="84%" height="50">
              	<textarea name="obj.description" id="description" style="width: 350px;height: 80px;">${obj.description}</textarea>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">时光网URL：</td>
              <td  width="84%" height="50"><input class="w220" id="mtime_url" name="obj.mtime_url" type="text" value="${obj.mtime_url}" />
              <span style="float:none;" class="red"> <label generated="true" for="mtime_url" class="error">${_msgFor_mtime_url}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">IMDB_URL：</td>
              <td  width="84%" height="50"><input class="w220" id="imdb_url" name="obj.imdb_url" type="text" value="${obj.imdb_url}" />
              <span style="float:none;" class="red"> <label generated="true" for="imdb_url" class="error">${_msgFor_imdb_url}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">豆瓣URL：</td>
              <td  width="84%" height="50"><input class="w220" id="douban_url" name="obj.douban_url" type="text" value="${obj.douban_url}" />
              <span style="float:none;" class="red"> <label generated="true" for="douban_url" class="error">${_msgFor_douban_url}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">SOKU_URL：</td>
              <td  width="84%" height="50"><input class="w220" id="soku_url" name="obj.soku_url" type="text" value="${obj.soku_url}" />
              <span style="float:none;" class="red"> <label generated="true" for="soku_url" class="error">${_msgFor_soku_url}</label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">土豆URL：</td>
              <td  width="84%" height="50"><input class="w220" id="tudou_url" name="obj.tudou_url" type="text" value="${obj.tudou_url}" />
              <span style="float:none;" class="red"> <label generated="true" for="tudou_url" class="error">${_msgFor_tudou_url}</label></span>
              </td>
            </tr>
          </table>
	<p class="pBtn"><a class="save" href="javascript:doSave();" title="保  存">保  &nbsp;存</a><a class="back" href="javascript:frameElement.lhgDG.cancel();" title="返  回">返 &nbsp; 回</a></p>
      </div>
  </div>
</form>
</body>
</html>