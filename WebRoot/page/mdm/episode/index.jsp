<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>剧集：${program.title}<c:if test="${program.current_season!='' && program.current_season!=0}">第${program.current_season}季</c:if>时间：${program.year}</title>
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
	var colName = ["id","pid","cover","title","orig_title","type",'year',"current_episode","series","person_count","music_count","scene_count","weibo_count","mtime_url","imdb_url"];
	getQueryForPageData(null,null,colName,null,null,function(obj,html){
		if(!isNotBlank(obj.cover)){
			html = html.replace("#cover#",defaultPhotp);
		}
		
		var str = "";
		if(isNotBlank(obj.mtime_url)){
			str = str + '<a href="'+obj.mtime_url+'" target="_blank">时光网地址</a>';
		}
		if(isNotBlank(obj.imdb_url)){
			str = str + '<a href="'+obj.imdb_url+'" target="_blank">IMDB地址</a>';
		}
		
		html = html.replace("#link_url#",str);
		return html;
	},addTabListColourEffect);
	window.top.scroll(0,0);
}

/** 新增 **/
function add(program_id){
	var url = "${ctx}/sys/episode/input?program_id="+program_id;
	var t = new $.dialog({title:'添加episode',id:'add', page:url,rang:true,width :600,height:550,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}


/** 编辑 **/
function edit(id){
	var url = "${ctx}/sys/episode/input/"+id;
	var t = new $.dialog({title:'修改episode',id:'edit', page:url,rang:true,width :600,height:550,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}

/** 删除 **/
function del(id){
	confirmDialog(function(){
		var url = "${ctx}/sys/episode/del/"+id+"?t="+new Date().getTime();
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
	var url = ctx+"/sys/feed/index_episode?episode_id="+pid;
	window.open(url);
}

//明星查看
function person_index(pid,eid){
	var url = ctx+"/sys/person/index_program_episode?program_id="+pid+"&episode_id="+eid;
	window.open(url);
}

//歌曲查看
function music_index(pid,eid){
	var url = ctx+"/sys/music/index_program_episode?program_id="+pid+"&episode_id="+eid;
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
	    	<div class="location"><span class="current">节目下的剧集episode查看</span></div>
	        <div class="rightBtn">
	        <a href="javascript:add('${program_id}')" title="添加episode" target="rightFrame">添加episode</a>
	        </div>
	    </div>
		<div class="content">
			<form action="${ctx}/sys/episode/list" id="queryForm" method="post">
			<input type="hidden" name="program_id" id="program_id" value="${program_id}"/>
	    	<table  class="search" width="830" border="0" cellspacing="0" cellpadding="0">
	          <tr>
	           <td width="220"><span>
	           ${program.title}  
	           <c:if test="${program.current_season!='' && program.current_season!=0}">
	           第${program.current_season}季
	           </c:if>
	           时间：${program.year}
	           </span></td>
	            <td width="80"><!-- <a href="javascript:search()" title="查 询">查 询</a> --></td>
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
	            <th style="width: 10%">集</th>
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
					<td>#year#</td>
					<td>#current_episode# #series#</td>
					<td>
					#link_url#
					</td>
					<td class="code">
						<a href="javascript:person_index('#pid#','#id#')">明星(#person_count#)</a>
						<a href="javascript:music_index('#pid#','#id#')">歌曲(#music_count#)</a>
						<a href="javascript:feed_index('#id#')">Feed(场景：#scene_count#,微博：#weibo_count#)</a>
						<!-- <a href="javascript:tv_tmall_export('#id#')">魔盒导入</a> -->
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