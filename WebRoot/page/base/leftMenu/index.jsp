<%@page import="tv.zhiping.common.Cons"%>
<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>${sys_name}</title>
	<link type="text/css" rel="stylesheet" href="${stCtx}/css/bcisc_beta1.0.css" />
	<%@ include file="/page/common/jscommon.jsp"%>
	<script type="text/javascript" src="${stCtx}/js/menu.js"></script>
	<style>
	body { background: #ecf1f7; }
	</style>
	<script type="text/javascript">
	$(document).ready(function() {
		$("li").each(function(){
			$(this).bind("click",function(event){
				$(".select").removeClass("select");
				$(this).addClass("select");
				event.stopPropagation();
			});
		});
	});
</script>
</head>

<body class="sidebar">
<ul id="menu">
  <%-- <li class="star"><span>明星库管理</span>
    <ul>
      <li><a href="${ctx}/sys/blog/index" title="明星库" target="rightFrame">明星库</a></li>
    	<li><a href="${ctx}/sys/blog/index" title="blog管理" target="rightFrame">blog管理</a></li>  	
    </ul>
  </li>
  <li class="house"> <span>工具管理</span>
    <ul>
    	<li><a href="${ctx}/sys/episodeFilePathMatch/index" title="" target="rightFrame">磁盘匹配提供</a></li>
    	<li><a href="${ctx}/sys/supplyEpisode/index" title="" target="rightFrame">acr匹配提供</a></li>
    	<li><a href="${ctx}/sys/ffmpegExport/index" title="" target="rightFrame">ffmpeg导出</a></li>
    	<li><a href="${ctx}/sys/weiboFeedMatch/index" title="" target="rightFrame">微直播匹配</a></li> 暂时用不到
    </ul>
  </li>
  <li class="house"> <span>IMDB管理</span>
    <ul>
    	<li><a href="${ctx}/sys/imdbFeed/index" title="" target="rightFrame">IMDB导入</a></li>
		<li><a href="${ctx}/sys/imdbMatch/index" title="" target="rightFrame">IMDB匹配</a></li>
		<li><a href="${ctx}/sys/xlsExport/index" title="" target="rightFrame">IMDB匹配结果导出</a></li>
		<li><a href="${ctx}/sys/imdbFactMatch/index" title="" target="rightFrame">IMDB百科翻译导入</a></li>
		<li><a href="${ctx}/sys/imdbPersonMatch/index" title="" target="rightFrame">IMDB人工匹配</a></li>
    </ul>
  </li>
  
   <li class="house"> <span>节目管理</span>
    <ul>
    	<li><a href="${ctx}/sys/program/index" title="" target="rightFrame">节目管理</a></li>
    	<li><a href="${ctx}/sys/person/index" title="" target="rightFrame">明星管理</a></li>
    	<li><a href="${ctx}/sys/mec_schedule/epg_index" title="" target="view_window">各台EPG</a></li>
    </ul>
  </li> --%>
  <li class="house"> <span>FEED管理</span>
    <ul>
    	<li><a href="${ctx}/sys/feedXls/index" title="excel管理" target="rightFrame">Feed_EXCEL导入</a></li>
    	<li><a href="${ctx}/sys/feed/feed_manage?program_id=147401" title="excel管理" target="view_window">Feed管理</a></li>
    	<li><a href="${ctx}/sys/feed/feed_manage?program_id=140751" title="excel管理" target="view_window">天天向上Feed管理</a></li>
    	<li><a href="${ctx}/sys/luck/index" title="抽奖管理" target="rightFrame">抽奖管理</a></li>
    	<li><a href="${ctx}/sys/banner/index" title="banner文案" target="rightFrame">banner文案</a></li>
    	<li><a href="${ctx}/sys/banner/img_index" title="banner文案" target="rightFrame">banner图片</a></li>    	
    </ul>
  </li>
  <li class="set"> <span>系统设置</span>
    <ul>
    	<li><a href="${ctx}/sys/appconfig/index" title="app系统配置" target="rightFrame">app系统配置</a></li>
    	<li><a href="${ctx}/sys/sysconfig/index" title="web系统配置" target="rightFrame">web系统配置</a></li>
    	<li><a href="${ctx}/sys/enumValue/index" title="字典配置" target="rightFrame">字典配置</a></li>
    	<li><a href="${ctx}/sys/user/reset" title="修改密码" target="rightFrame">修改密码</a></li>
    </ul>
  </li>
  
</ul>
</body>
</html>