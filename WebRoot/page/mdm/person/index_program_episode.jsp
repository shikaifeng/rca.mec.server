<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>明星：${program.title}<c:if test="${program.current_season!='' && program.current_season!=0}">第${program.current_season}季</c:if> 
	            <c:if test="${episode!=null && episode.current_episode!='' && episode.current_episode!=0}">第${episode.current_episode}集</c:if>
	            <c:if test="${episode!=null && episode.series!='' && episode.series!=0}">${episode.series}</c:if></title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet" href="${stCtx}/css/bcisc_beta1.0.css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<script language="javascript" src="${stCtx}/js/lhgdialog/lhgdialog.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	search();
});


function search(){
	var colName = ["id","avatar","name","name_en","gender",'birthday',"born_place","constellation","height","weight","country","profession","character_name","mtime_url","imdb_url"];
	getQueryForPageData(null,null,colName,null,null,function(obj,html){
		if(!isNotBlank(obj.avatar)){
			html = html.replace("#avatar#",defaultPhotp);
		}

		var str = "";
		if(isNotBlank(obj.mtime_url)){
			str = str + '<a href="'+obj.mtime_url+'" target="_blank">时光网地址</a>';
		}
		if(isNotBlank(obj.imdb_url)){
			str = str + ' <a href="'+obj.imdb_url+'" target="_blank">IMDB地址</a>';
		}
		html = html.replace("#link_url#",str);		
		return html;
	},addTabListColourEffect);
	window.top.scroll(0,0);
}

/** 新增 **/
function add(){
	return;
	var url = "${ctx}/sys/person/input";
	var t = new $.dialog({title:'添加person',id:'add', page:url,rang:true,width :600,height:480,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}


/** 编辑 **/
function edit(id){
	return;
	var url = "${ctx}/sys/person/input/"+id;
	var t = new $.dialog({title:'修改person',id:'edit', page:url,rang:true,width :600,height:480,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}

/** 删除 **/
function del(id){
	confirmDialog(function(){
		var url = "${ctx}/sys/person/del/"+id+"?t="+new Date().getTime();
		$.ajax({url:url,dataType:"json",error:function(){alert("删除失败，请稍后重试")}, 
			success: function(data){
				if(data.status=="0"){
					search();
				}
			}
		});
	});
}

//feed查看
function feed_index(pid){
	var url = ctx+"/sys/feed/index?episode_id="+pid;
	window.location.href=url;
}
</script>
<style>
a{ text-align:center;}
</style>
</head>
<body>

<div class="container">
	<div class="radius">
		<div class="title">
	    	<div class="location"><span class="current">person查看</span></div>
	        <div class="rightBtn"><!-- <a href="javascript:add()" title="添加person" target="rightFrame">添加person</a> --></div>
	    </div>
		<div class="content">
			<form action="${ctx}/sys/person/list_program_episode" id="queryForm" method="post">
			<input type="hidden" name="program_id" id="program_id" value="${program_id}"/>
			<input type="hidden" name="episode_id" id="episode_id" value="${episode_id}"/>
	    	<table  class="search" width="830" border="0" cellspacing="0" cellpadding="0">
	          <tr>
	            <td width="220"><span>${program.title}<c:if test="${program.current_season!='' && program.current_season!=0}">第${program.current_season}季</c:if> 
	            <c:if test="${episode!=null && episode.current_episode!='' && episode.current_episode!=0}">第${episode.current_episode}集</c:if>
	            <c:if test="${episode!=null && episode.series!='' && episode.series!=0}">${episode.series}</c:if></span></td>
	            <td width="80"><!-- <a href="javascript:search()" title="查 询">查 询</a> --></td>
	            <td width="56">&nbsp;</td>
	          </tr>
	        </table>
	        <table class="borderTable" width="100%" border="0" cellspacing="0" cellpadding="0">
	          <tr>
	            <th style="width: 5%">编号</th>
	            <th style="width: 5%">头像</th>
	            <th style="width: 10%">名称</th>
	            <th style="width: 7%">职位</th>
	            <th style="width: 8%">角色</th>
	            <th style="width: 25%">人员信息</th>
	            <th style="width: 25%">地理信息</th>
	            <th style="width: 13%">外部地址</th>
	            <th style="width: 2%">操作</th>
	          </tr>
	          <tbody id="tableList">
	      		</tbody>
	        </table>
	        <div class="pageCode" id="tabpage">
	         	<%@ include file="/page/common/paginate.jsp"%>
			 </div>
			 <table style="display: none">
		   		<tbody id="listCellTemplate">
		   		<tr class="hovcolor">
		   			<td>#id#</td>
		   			<td><img src="#avatar#" alt="" width="50px" height="50px"/></td>
					<td><a href="javascript:edit('#id#')">中文名：#name#  英文名:#name_en#</a></td>
					<td>#profession#</td>
					<td>#character_name#</td>
					<td>性别：#gender# 出生日期：#birthday# 身高：#height# 体重：#height#</td>
					<td>国籍：#country# 家庭地址：#born_place# 星座：#constellation#</td>
					<td>
					#link_url#
					</td>
					<td class="code">
						<!-- <a href="javascript:feed_index('#id#')">Feed</a> -->
					</td>
			     </tr>
			     </tbody>
		    </table>
		    </form>
	    </div>
	</div>   
</div>
</body>
</html>