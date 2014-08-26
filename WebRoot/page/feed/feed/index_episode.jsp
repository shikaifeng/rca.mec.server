<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="tv.zhiping.common.util.ComUtil"%>
<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>feed:${program.title}<c:if test="${program.current_season!='' && program.current_season!=0}">第${program.current_season}季</c:if> 
	            <c:if test="${episode.current_episode!='' && episode.current_episode!=0}">第${episode.current_episode}集</c:if>
	             <c:if test="${episode.series!=''}">${episode.series}</c:if></title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet" href="${stCtx}/css/bcisc_beta1.0.css" />
<link type="text/css" rel="stylesheet" href="${stCtx}/css/table.css" />
<script language="javascript" src="${stCtx}/js/lhgdialog/lhgdialog.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	addTabListColourEffect();
	window.top.scroll(0,0);
});
/** 新增时间 **/
function time_add(id){
	var url = "${ctx}/sys/feed/time_input?element_id="+id;
	var t = new $.dialog({title:'添加时间',id:'add', page:url,rang:true,width :600,height:480,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
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
	    	<div class="location"><span class="current">剧集feed查看</span></div>
	        <div class="rightBtn"><!-- <a href="javascript:add()" title="添加feed" target="rightFrame">添加feed</a> --></div>
	    </div>
		<div class="content">
			<form action="${ctx}/sys/feed/list" id="queryForm" method="post">
			<input type="hidden" name="program_id" id="program_id" value="${program_id}"/>
			<input type="hidden" name="episode_id" id="episode_id" value="${episode_id}"/>
	    	<table  class="search" width="830" border="0" cellspacing="0" cellpadding="0">
	          <tr>
	            <td width="220"><span>${program.title}<c:if test="${program.current_season!='' && program.current_season!=0}">第${program.current_season}季</c:if> 
	            <c:if test="${episode.current_episode!='' && episode.current_episode!=0}">第${episode.current_episode}集</c:if>
	             <c:if test="${episode.series!=''}">${episode.series}</c:if>
	            </span></td>
	            <td width="80"><!-- <a href="javascript:search()" title="查 询">查 询</a> --></td>
	            <td width="56">&nbsp;</td>
	          </tr>
	        </table>
	        
	        <table class="borderTable" width="100%" border="0" cellspacing="0" cellpadding="0">
	          <tr>
	            <th style="width: 5%">场景编号</th>
	            <th style="width: 5%">元素编号</th>
	            <th style="width: 10%">时间</th>
	            <th style="width: 10%">类型</th>
	            <th style="width: 40%">标题</th>
	            <th style="width: 40%">操作</th>
	            
	          </tr>
	          <tbody id="tableList">
	          <c:forEach var="cell" items="${table_list}">
		   		<tr class="hovcolor">
		   			<td>${cell.id}</td>
		   			<td></td>
		   			<td>${cell.start_time} 到 ${cell.end_time}</td>
		   			<td>场景</td>
					<td>${cell.summary} 数量:${cell.element_count}</td>
					<td></td>
			     </tr>
			  <c:forEach var="child_cell" items="${cell.element_list}">
			     <tr class="hovcolor">
		   			<td>&nbsp;</td>
		   			<td>${child_cell.id}</td>
					<td>
					<%JSONObject json = (JSONObject)pageContext.getAttribute("child_cell");%>
					<%=ComUtil.secondFormate(json.getLong("start_time"))+" 到 "+ComUtil.secondFormate(json.getLong("end_time"))%>
					<a href="javascript:time_add(${child_cell.id})">点击添加</a>
					</td>
					<td>
						<c:if test="${child_cell.type=='person' || child_cell.type=='mec_person'}">
							明星
						</c:if>
						<c:if test="${child_cell.type=='music'}">
							音乐
						</c:if>
						<c:if test="${child_cell.type=='baike'}">
							百度百科
						</c:if>
						<c:if test="${child_cell.type=='video'}">
							视频
						</c:if>
						<c:if test="${child_cell.type=='fact'}">
							imdb百科
						</c:if>
						<c:if test="${child_cell.type=='weibo'}">
							微直播
						</c:if>
					</td>
					<td>
						<c:if test="${child_cell.type=='person' || child_cell.type=='mec_person'}">
							 <a href="${child_cell.url}" target="_blank"><img src="${child_cell.avatar}" alt="" width="50px" height="50px"/></a> ${child_cell.name} 饰演： ${child_cell.character} 
						</c:if>
						<c:if test="${child_cell.type=='music'}">
							<a href="${child_cell.url}" target="_blank"><img src="${child_cell.cover}" alt="" width="50px" height="50px"/></a> ${child_cell.title} 歌唱者：${child_cell.singer} 类型：${child_cell.type_title}
						</c:if>
						<c:if test="${child_cell.type=='baike'}">
							<a href="${child_cell.url}" target="_blank"><img src="${child_cell.cover}" alt="" width="50px" height="50px"/></a> ${child_cell.title}
						</c:if>
						<c:if test="${child_cell.type=='video'}">
							<a href="${child_cell.url}" target="_blank"><img src="${child_cell.cover}" alt="" width="50px" height="50px"/></a> ${child_cell.title}
						</c:if>
						<c:if test="${child_cell.type=='fact'}">
							${child_cell.title}
						</c:if>
						<c:if test="${child_cell.type=='weibo'}">
							<a href="${child_cell.sender_url}" target="_blank"><img src="${child_cell.sender_avatar}" alt="" width="50px" height="50px"/></a> ${child_cell.sender_name}
						</c:if>
					</td>
					<td>
						<c:if test="${child_cell.type=='person' || child_cell.type=='mec_person'}">
							<img src="${child_cell.cover}" alt="" width="50px" height="50px"/>  <a href="${child_cell.url}" target="_blank">点击打开 </a>
						</c:if>
						<c:if test="${child_cell.type=='music'}">
							<a href="${child_cell.url}" target="_blank">点击播放</a>
						</c:if>
						<c:if test="${child_cell.type=='baike'}">
							${child_cell.summary}  <a href="${child_cell.url}" target="_blank">点击打开</a>
						</c:if> 
						<c:if test="${child_cell.type=='video'}">
							${child_cell.content}  <a href="${child_cell.url}" target="_blank">点击播放</a>
						</c:if>
						<c:if test="${child_cell.type=='fact'}">
							${child_cell.summary}
						</c:if>
						<c:if test="${child_cell.type=='weibo'}">
							${child_cell.content}<br />
							<c:forEach var="link" items="${child_cell.links}">
								<a href="${link.url}" target="_blank"> <img src="${link.thumbnail_pic}" alt="" width="50px" height="50px"/></a>
							</c:forEach>
						</c:if>
					</td>
			     </tr>
			     </c:forEach>
	   		</c:forEach>
	      	</tbody>
	        </table>
	        <div class="pageCode" id="tabpage">
	         	<%@ include file="/page/common/paginate.jsp"%>
			 </div>
		    </form>
	    </div>
	</div>   
</div>
</body>
</html>