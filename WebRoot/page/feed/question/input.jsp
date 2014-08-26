<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>互动问答</title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet" href="${stCtx}/css/bcisc_beta1.0.css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<link href="${stCtx}/css/table_com.css" rel="stylesheet" type="text/css" />

<script src="${stCtx}/js/jqueryformValidator/formValidator.js" type="text/javascript"></script>
<script src="${stCtx}/js/jqueryformValidator/formValidatorRegex.js" type="text/javascript" ></script>

<script src="${stCtx}/js/setTime.js" type="text/javascript"></script>
 
<style type="text/css">
#uploadUploader{
	width:83px;
	height:83px;
}
</style>
<script type="text/javascript">
$(document).ready(function() {
	setEditValidate();
	defaultValue();
	inputQueReady();
});

//默认值的设置 
function defaultValue(){
	$("#answer_index").val("${answer_index}");
	
	var str = secondFormate("${obj.start_time}");
	$("#start_time").val(str);
	
	str = secondFormate("${obj.deadline}");
	$("#deadline").val(str);
	
	str = secondFormate("${obj.public_time}");
	$("#public_time").val(str);
	
	str = secondFormate("${obj.end_time}");
	$("#end_time").val(str);
}

function doSave(){
	$('#inputForm').submit();
}

//设置添加修改时候的校验规则
function setEditValidate(){
	$.formValidator.initConfig({formID:"inputForm",onSuccess:function(){
		var option_len = $("#tableList tr").length;
		if(option_len < 2 || option_len > 5){
			alert("互动问答选项只能在2至5项");
			return;
		}
		var answer_index = $("#answer_index").val();
		if(isNotBlank(answer_index)){
			if(answer_index > (option_len-1)){
				alert("互动问答正确答案超过了问答选项");
				return;		
			}
		}
		$("#timeErrorMsg1").html("");
		$("#timeErrorMsg2").html("");
		$("#timeErrorMsg3").html("");
		var mark = getTime($("#start_time").val());		
		var flag = true;
		var time = getTime($("#deadline").val());		
		if(isNotBlank(time)){
			if(time<mark){
				$("#timeErrorMsg1").html("时间不合格");
				$("#timeErrorMsg1").show();
				flag = false;
			}else{
				mark = time;
			}
		}
		time = getTime($("#public_time").val());
		if(isNotBlank(time)){
			if(time<mark){
				$("#timeErrorMsg2").html("时间不合格");
				$("#timeErrorMsg2").show();
				flag = false;
			}else{
				mark = time;
			}
		}
		time = getTime($("#end_time").val());
		if(isNotBlank(time)){
			if(time<mark){
				$("#timeErrorMsg2").html("时间不合格");
				$("#timeErrorMsg2").show();
				flag = false;
			}
		}
		if(!flag){
			return
		}
		$("#start_time").val(getTime($("#start_time").val()));
		$("#end_time").val(getTime($("#end_time").val()));
		$("#deadline").val(getTime($("#deadline").val()));
		$("#public_time").val(getTime($("#public_time").val()));
		$("#inputForm").ajaxSubmit({
			dataType:"json",
	 		success:function(data){
	 			if(data.status=='0'){
	 				window.parent.load_question();
	 				frameElement.lhgDG.cancel();
				}
		    }
		});
		
		return false;
	}});
	
	$("#title").formValidator({onFocus:"请输入名称"}).inputValidator({min:1,onError:"名称不能为空"});
}

function inputQueReady(){
	$("#addButtonId").click(function(){
		var option_len = $("#tableList tr").length;
		if(option_len > 4){
			alert("互动问答选项只能在2至5项");
			return;
		}
		$("#tableList").append($("#trTemplate").html());
	});
}

function delTr(obj){
	$(obj).parents("tr").remove();
}
</script>
</head>
<body style=" background:#f6f9fb;">
<form action="${ctx}/sys/question/save" method="post" id="inputForm"> 
<input type="hidden" name="obj.id" value="${obj.id}" />
<input type="hidden" name="obj.program_id" value="${obj.program_id}" />
<input type="hidden" name="obj.episode_id" value="${obj.episode_id}" />
<div class="pop ">
     <div class="popContent">
      	<table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="16%" align="right">名称：</td>
              <td  width="84%" height="50"><input class="w220" id="title" name="obj.title" type="text" value="${obj.title}" maxlength="38" />
              	<span style="float:none;" class="red">* <label generated="true" for="title" class="error"></label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">答题出现时间：</td>
              <td  width="84%" height="50"><input class="w220" id="start_time" name="obj.start_time" type="text" value="${obj.start_time}" onclick="_SetTime(this)" />
              	<span style="float:none;" class="red"><label generated="true" for="start_time" class="error"></label></span>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">结束答题时间：</td>
              <td  width="84%" height="50"><input class="w220" id="deadline" name="obj.deadline" type="text" value="${obj.deadline}" onclick="_SetTime(this)"/>
              	<span style="float:none;" class="red"><label generated="true" for="deadline" class="error"></label></span>
              	<label id="timeErrorMsg1" class="red" style="font-size:12px;"></label>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">答案揭晓时间：</td>
              <td  width="84%" height="50"><input class="w220" id="public_time" name="obj.public_time" type="text" value="${obj.public_time}" onclick="_SetTime(this)" />
              	<span style="float:none;" class="red"><label generated="true" for="public_time" class="error"></label></span>
              	<label id="timeErrorMsg2" class="red" style="font-size:12px;"></label>
              </td>
            </tr>
            <tr>
              <td width="16%" align="right">问答结束时间：</td>
              <td  width="84%" height="50"><input class="w220" id="end_time" name="obj.end_time" type="text" value="${obj.end_time}" onclick="_SetTime(this)"/>
              	<span style="float:none;" class="red"><label generated="true" for="end_time" class="error"></label></span>
              	<label id="timeErrorMsg3" class="red" style="font-size:12px;"></label>
              </td>
            </tr>
             <tr>
              <td width="16%" align="right">正确答案：</td>
              <td  width="84%" height="50">
              	<select style="width:225px;" name="answer_index" id="answer_index">
              		<option value="">--请选择--</option>
					<option value="0">A</option>
					<option value="1">B</option>
					<option value="2">C</option>
					<option value="3">D</option>
					<option value="4">E</option>
				</select>
              </td>
            </tr>
          </table>
		  <div id="optDivId">
   <div class="cont_table" style="width: 400px">
   	<div id="tab_cz" style="width:100%;padding:0;margin:0;">
         <div class="tab_but">
          <button type="button" id="addButtonId">增加选项</button>
         </div>
	</div>
     <table summary="" border="1" class="tabcom">
     <tr>
      <th class="th01">名称</th>
      <th class="th02">操作</th>
     </tr>
      <tbody id="tableList">
      <c:forEach var="cell" items="${opt_list}">
		 <tr>
		    <td class="tab_cent">
			    <input type="hidden" name="optId" value="${cell.id}" />
			    <input type="text" name="optTitle" value="${cell.title}"  maxlength="10" size="40" style="width:200px;" />
		    </td>
		    <td class="tab_cent"><a href="#" onclick="delTr(this);return false"><img src="${stCtx}/images/icon_error.png" alt="删除" /></a></td>
		  </tr>
	 	</c:forEach>
      </tbody>
    </table> 
    </div>
   </div>
		<p class="pBtn"><a class="save" href="javascript:doSave();" title="保  存">保  &nbsp;存</a><a class="back" href="javascript:frameElement.lhgDG.cancel();" title="关  闭">关 &nbsp; 闭</a></p>
      </div>
  </div>
</form>
<table style="display: none">
	<tbody id="trTemplate">
	<tr>
	    <td class="tab_cent">
		    <input type="hidden" name="optId" value="" />
		    <input type="text" name="optTitle" value=""  maxlength="10" size="40" style="width:200px;" />
	    </td>
	   	<td class="tab_cent"><a href="#"  onclick="delTr(this);return false"><img src="${stCtx}/images/icon_error.png" alt="删除" /></a></td>
	</tr>
	</tbody>
</table>
</body>
</html>