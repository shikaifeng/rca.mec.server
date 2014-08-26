<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet" href="${stCtx}/css/bcisc_beta1.0.css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<script language="javascript" src="${stCtx}/js/lhgdialog/lhgdialog.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	initOpetionsByJssj($("#year"),1888);
	search();
});

function search(){
	var colName = ["id","cover","title","orig_title","type",'year',"country","current_season","episodes_count","person_count","music_count","mtime_url","imdb_url"];
	getQueryForPageData(null,null,colName,null,null,function(obj,html){
		if(!isNotBlank(obj.cover)){
			html = html.replace("#cover#",defaultPhotp);
		}
		return html;
	},addTabListColourEffect);
	window.top.scroll(0,0);
}

/** 新增 **/
function add(){
	return;
	var url = "${ctx}/sys/program/input";
	var t = new $.dialog({title:'添加program',id:'add', page:url,rang:true,width :600,height:480,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}


/** 编辑 **/
function edit(id){
	return;
	var url = "${ctx}/sys/program/input/"+id;
	var t = new $.dialog({title:'修改program',id:'edit', page:url,rang:true,width :600,height:480,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}

/** 删除 **/
function del(id){
	confirmDialog(function(){
		var url = "${ctx}/sys/program/del/"+id+"?t="+new Date().getTime();
		$.ajax({url:url,dataType:"json",error:function(){alert("删除失败，请稍后重试")}, 
			success: function(data){
				if(data.status=="0"){
					search();
				}
			}
		});
	});
}

//剧集查看
function episode_index(pid){
	var url = ctx+"/sys/episode/index?program_id="+pid;
	window.open(url);
}

//明星查看
function person_index(pid){
	var url = ctx+"/sys/person/index_program_episode?program_id="+pid;
	window.open(url);
}

//歌曲查看
function music_index(pid){
	var url = ctx+"/sys/music/index_program_episode?program_id="+pid;
	window.open(url);
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
	    	<div class="location"><span class="current">program管理</span></div>
	        <div class="rightBtn"><!-- <a href="javascript:add()" title="添加program" target="rightFrame">添加program</a> --></div>
	    </div>
		<div class="content">
			<form action="${ctx}/sys/program/list_person" id="queryForm" method="post">
			<input type="hidden" name="person_id" id="person_id" value="${person.id}" />
	    	<table  class="search" width="830" border="0" cellspacing="0" cellpadding="0">
	          <tr>
	            <td width="220"><span>标题：</span><input name="obj.title" id="title" type="text" style="width: 160px;"/></td>
	            <td width="220"><span>类型：</span>
	            <select id="type" name="obj.type" style="width: 160px;">
	            	<option value="">--请选择--</option>
	            	<option value="Movie">电影</option>
	            	<option value="TV">电视剧</option>
	            	<option value="Variety">综艺</option>
	            </select>
	            </td>
	             <td width="220"><span>年份：</span>
	            <select id="year" name="obj.year" style="width: 160px;">
	            </select>
	            </td>
	            <td width="80"><a href="javascript:search()" title="查 询">查 询</a></td>
	            
	            <td width="56">&nbsp;</td>
	          </tr>
	        </table>
	        
	        <table class="borderTable" width="100%" border="0" cellspacing="0" cellpadding="0">
	          <tr>
	            <th style="width: 5%">编号</th>
	            <th style="width: 10%">海报</th>
	            <th style="width: 20%">标题</th>
	            <th style="width: 5%">类型</th>
	            <th style="width: 5%">年份</th>
	            <th style="width: 10%">国籍</th>
	            <th style="width: 10%">地址</th>
	            <th style="width: 15%">操作</th>
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
		   			<td><img src="#cover#" alt="" width="50px" height="50px"/></td>
					<td><a href="javascript:edit('#id#')">#title#  #orig_title#</a></td>
					<td>#type#</td>
					<td>#year# 第#current_season#季</td>
					<td>#country#</td>
					<td>
					<a href="#mtime_url#" target="_blank">时光网地址</a>
					<a href="#imdb_url#" target="_blank">IMDB地址</a>
					</td>
					<td class="code">
					 	<a href="javascript:episode_index('#id#')">剧集(#episodes_count#)</a>
					 	<a href="javascript:person_index('#id#')">明星(#person_count#)</a>
					 	<a href="javascript:music_index('#id#')">歌曲(#music_count#)</a>
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