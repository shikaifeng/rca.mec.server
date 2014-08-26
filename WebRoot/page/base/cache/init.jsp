<%@page import="tv.zhiping.common.Cons"%>
<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/page/common/jscommon.jsp"%>
<link href="${stCtx}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${stCtx}/css/table_com.css" rel="stylesheet" type="text/css" />

<script language="javascript" src="${stCtx}/js/lhgdialog/lhgdialog.js"></script>

<title>品牌管理</title>
<script type="text/javascript">
$(document).ready(function() {
	//鼠标滑过变色效果
	$("#tableList tr").hover(
		function () {
			if($(this).attr("class") == "hovcolor"){
				$(this).attr("class","onmuose01");
			}else{
				$(this).attr("class","onmuose02");
			}
		},
		function () {
			if($(this).attr("class") == "onmuose01"){
				$(this).attr("class","hovcolor");
			}else{
				$(this).removeClass();
			}
		}
	);
});

function updateCache(type){
	if(confirm("确认更新？")){
		var url = ctx+"/cache/updateCache/"+type+"?t="+new Date().getTime();
		$.ajax({url:url,dataType:"text",error:function(){alert("更新失败")}, 
			success: function(data){
				if(data=="1"){
					alert("更新成功");
				}
			}
		});
	}
}
</script>
</head>

<body>
   <div id="cont_r">
   		<br/>
      <div class="cont_table">
     <table summary="" class="tabcom">
     <tr>
	  <th style="width:25%;">名称</th>
	 <th style="width:20%;">操作</th>
     </tr>
      <tbody id="tableList">
			<tr class="">
				<td>
					区域代码
				</td>
				<td class="td01">
					<a href="javascript:updateCache('1')">更新</a>
				</td>
			 </tr>
			 
			<tr class="hovcolor">
				<td>
					枚举表
				</td>
				<td class="td01">
					<a href="javascript:updateCache('2')">更新</a>
				</td>
			 </tr>
			<tr class="">
				<td>
					系统配置参数表
				</td>
				<td class="td01">
					<a href="javascript:updateCache('3')">更新</a>
				</td>
			 </tr>
			 <tr class="hovcolor">
				<td>
					管理员表
				</td>
				<td class="td01">
					<a href="javascript:updateCache('4')">更新</a>
				</td>
			 </tr>
		</tbody>
    </table> 
    </div>
   </div>
</body>
</html>