<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Feed管理</title>
<%@ include file="/page/common/jscommon.jsp"%>
<link type="text/css" rel="stylesheet" href="${stCtx}/css/bootstrap.min.css" />
<script src="${stCtx}/js/bootstrap.min.js"></script>
<script language="javascript" src="${stCtx}/js/lhgdialog/lhgdialog.js"></script>
<script>
	$(document).ready(function() {
		//addTabListColourEffect();
		//window.top.scroll(0, 0);
		
		$("#episode_id").val("${episode_id}");
		$("#channel_id").val("${channel_id}");
		
		load_lucky_draw_event();
		
		load_all_feed();

		feedAffix();
		$(window).off('feed.resize').on('feed.resize', function(){
			feedAffixResize();
		})
	});	
	
	function load_lucky_draw_event(){
		var episode_id = $("#episode_id").val();
		var schedule_id = $("#schedule_id").val();
		if(isNotBlank(episode_id)){
			var url = '${ctx}/sys/feed/loadLuckyDrawEvent?episode_id='+episode_id+"&schedule_id="+schedule_id;
			$('#lucky_draw_event_div').load(url);
		}
	}
	
	function feedAffixResize(){
		var width = $('#feed_affix').width();

		$('#feed_affix').css('width', width);
		$('.feed-thead-affix-wrap').css('width', width-18);
	}
	function feedAffix(){
		$('#feed_affix').affix({
		    offset: {
		        top: 224
		    }
		});
		$('.feed-thead-affix').affix({
			offset:{
				top: 224
			}
		});
	}
	
	function load_all_feed(){
		var episode_id = $("#episode_id").val();
		if(!isNotBlank(episode_id)){
			alert("剧集信息为空，请先设置剧集信息");
			return;
		}
		
		loaderDiveShow();
		var url = "${ctx}/sys/feed/get_feeds?episode_id="+episode_id+"&modules=mec_person,baike,music,question,weibo_feed&t="+new Date().getTime();
		$.ajax({url:url,dataType:"json",error:function(){alert("加载失败，请稍后重试")}, 
			success: function(data){
				_TimeArray[0] = 0;
				_TimeArray[1] = 0;
				_TimeArray[2] = 0;
				_TimeArray[3] = 0;
				_TimeArray[4] = 0;
				
				var list = data.mec_person_list;
				draw_mec_person_list(list);
				
				list = data.baike_list;
				draw_baike_list(list);
				
				list = data.music_list;
				draw_music_list(list);
				
				list = data.question_list;
				draw_question_list(list);
				
				list = data.weibo_feed_list;
				draw_weibo_feed_list(list);
				
				loaderDiveHide();

				feedAffixResize();

			}
		});
	}
	
	function search(){
		page_reload();
	}
	
	function change_episode(){
		page_reload();
	}
	
	function change_channel(){
		page_reload();
	}
	
	function page_reload(){
		var program_id = $("#program_id").val();
		var episode_id = $("#episode_id").val();
		var channel_id = $("#channel_id").val();
		var url = "${ctx}/sys/feed/feed_manage?program_id="+program_id+"&episode_id="+episode_id+"&channel_id="+channel_id;
		window.location.href=url;
	}
	
	/** feed预导入 **/
	function tv_tmall_import(){
		var episode_id = $("#episode_id").val();
		if(!isNotBlank(episode_id)){
			alert("剧集信息为空，请先设置剧集信息");
			return;
		}
		var url = "${ctx}/sys/tmall_box_feed/xls_input?episode_id="+episode_id;
		var t = new $.dialog({title:'FEED导入',id:'add', page:url,rang:true,width :600,height:480,
			onXclick:function(){
				t.cancel();
			}
		});
		t.ShowDialog();
	}
	
	//设置开始时间
	function set_epg_start_at(id){
		var channel_id = $("#channel_id").val();
		var episode_id = $("#episode_id").val();
		if(!isNotBlank(episode_id)){
			alert("剧集信息为空，请先设置剧集信息");
			return;
		}
		if(confirm("确认设置时间？")){
			var url = "${ctx}/sys/mec_schedule/sign_set?id="+id+"&episode_id="+episode_id+"&channel_id="+channel_id+"&type=start_at&t="+new Date().getTime();
			$.ajax({url:url,dataType:"json",error:function(){alert("操作失败，请稍后重试")}, 
				success: function(data){
					if(data.status=="0"){
						page_reload();
					}
				}
			});
		}
	}
	
	//设置结束时间
	function set_epg_end_at(id){
		var channel_id = $("#channel_id").val();
		var episode_id = $("#episode_id").val();
		if(!isNotBlank(episode_id)){
			alert("剧集信息为空，请先设置剧集信息");
			return;
		}
		if(confirm("确认设置时间？")){
			var url = "${ctx}/sys/mec_schedule/sign_set?id="+id+"&episode_id="+episode_id+"&channel_id="+channel_id+"&type=end_at&t="+new Date().getTime();
			$.ajax({url:url,dataType:"json",error:function(){alert("操作失败，请稍后重试")}, 
				success: function(data){
					if(data.status=="0"){
						page_reload();
					}
				}
			});
		}
	}
	
	//删除element
	function del_element(id,module) {
		confirmDialog(function(){
			var url = "${ctx}/sys/feed/del_element/"+id;
			$.ajax({url:url,dataType:"json",error:function(){alert("删除失败，请稍后重试");}, 
				success: function(data){
					if(data.status=="0"){
						alert("删除成功！");
						load_feed_route(module);
					}
				}
			});
		});
	}
	
	function set_element_start_time(element_id,module){
		var schedule_id = $("#schedule_id").val();
		if(!isNotBlank(schedule_id)){
			alert("请先设置epg的信息");
			return;
		}
		
		if(confirm("确认设置时间？")){
			var url = "${ctx}/sys/feed/set_element_start_time?element_id="+element_id+"&schedule_id="+schedule_id;
			$.ajax({url:url,dataType:"json",error:function(){alert("设置失败，请稍后重试");}, 
				success: function(data){
					if(data.status=="0"){
						//alert("设置成功！");
						load_feed_route(module);
					}else{
						alert(data.msg);
					}
				}
			});
		}
	}
	
	function set_element_end_time(element_id,module){
		var schedule_id = $("#schedule_id").val();
		if(!isNotBlank(schedule_id)){
			alert("请先设置epg的信息");
			return;
		}
		if(confirm("确认设置时间？")){
			var url = "${ctx}/sys/feed/set_element_end_time?element_id="+element_id+"&schedule_id="+schedule_id;
			$.ajax({url:url,dataType:"json",error:function(){alert("设置失败，请稍后重试");}, 
				success: function(data){
					if(data.status=="0"){
						//alert("设置成功！");
						load_feed_route(module);
					}else{
						alert(data.msg);
					}
				}
			});
		}
	}
	
	function load_feed_route(module){
		if(isNotBlank(module)){
			if("question" == module){
				load_question();
			}else if("mec_person" == module){
				load_mec_person();
			}else if("baike" == module){
				load_baike();
			}else if("music" == module){
				load_music();
			}
		}
	}
	
	//设置epg详细时间
	function set_epg_time(schedule_id){
		var url = "${ctx}/sys/mec_schedule/epg_input?mec_schedule_id="+schedule_id;
		var t = new $.dialog({title:'编辑EPG时间',id:'edit', page:url,rang:true,width :600,height:480,
			onXclick:function(){
				t.cancel();
			}
		});
		t.ShowDialog();
	}
	//时间冲突标记 顺序：人物、百科、微直播、音乐、问答
	var _TimeArray = new Array(0,0,0,0,0);
	function draw_html_time_warn(obj,html,i){
		var time = obj.long_start_time;
		var str = "feed-table-td";
		if(time<_TimeArray[i]){
			str = "danger";
		}else{
			_TimeArray[i] = time;		
		}
		html = html.replace("#start_warn#",str);
		
		time = obj.long_end_time;
		str = "feed-table-td";
		if(time<_TimeArray[i]){
			str = "danger";
		}else{
			_TimeArray[i] = time;
		}
		html = html.replace("#end_warn#",str);
		return html;
	}
	
	
	/**
	*活动时间设置
	*/
	//设置开始时间
	function set_lucky_episode_time(id,type){
		var schedule_id = $("#schedule_id").val();
		if(!isNotBlank(schedule_id)){
			alert("请先设置epg的信息");
			return;
		}
		
		if(confirm("确认设置时间？")){
			var url = "${ctx}/sys/lucky_draw_episode/set_sign_time?id="+id+"&type="+type+"&schedule_id="+schedule_id+"&t="+new Date().getTime();
			$.ajax({url:url,dataType:"json",error:function(){alert("操作失败，请稍后重试")}, 
				success: function(data){
					if(data.status=="0"){
						load_lucky_draw_event();
					}
				}
			});
		}
	}
	
	function set_lucky_episode_time_dialog(id){
		var url = "${ctx}/sys/lucky_draw_episode/input?id="+id;
		var t = new $.dialog({title:'编辑抽奖时间',id:'edit', page:url,rang:true,width :600,height:300,
			onXclick:function(){
				t.cancel();
			}
		});
		t.ShowDialog();
	}
	
	//设置结束时间
	function set_epg_end_at(id){
		var channel_id = $("#channel_id").val();
		var episode_id = $("#episode_id").val();
		if(!isNotBlank(episode_id)){
			alert("剧集信息为空，请先设置剧集信息");
			return;
		}
		if(confirm("确认设置时间？")){
			var url = "${ctx}/sys/mec_schedule/sign_set?id="+id+"&episode_id="+episode_id+"&channel_id="+channel_id+"&type=end_at&t="+new Date().getTime();
			$.ajax({url:url,dataType:"json",error:function(){alert("操作失败，请稍后重试")}, 
				success: function(data){
					if(data.status=="0"){
						page_reload();
					}
				}
			});
		}
	}
</script>
<style>
	.feed-config{
		margin-bottom: 20px;
	}
	.feed-config-row{
		margin-bottom: 10px;
		font-size: 16px;
	}
	
	.feed-affix{
		background-color: #fff;
	}
	.feed-affix .affix-top{
		top: 224px;
	}
	.feed-affix .affix{
		top: 0;
		background-color: #fff;
	}
	.feed-affix .page-header{
		margin-top: 0;
	}

	.feed-table{
		text-align: center;
		border-top: 0;
	}
	.feed-table .feed-table-td{
		text-align: center;
		vertical-align: middle;
		overflow: hidden;
		word-break: break-all;
	}
	.feed-table .affix-top{
		top: 335px;
	}
	.feed-table .affix{
		top: 111px;
	}
	.feed-thead-affix{
		background-color: #fff;
	}
	.feed-qw5{
		width: 5%;
	}
	.feed-qw6{
		width: 6%;
	}
	.feed-qw10{
		width: 10%;
	}
	.feed-qw20{
		width: 20.999%;
	}

</style>
</head>
<body>

<input type="hidden" name="program_id" id="program_id" value="${program.id}"/>
<input type="hidden" name="schedule_id" id="schedule_id" value="${schedule.id}"/>

	<div class="container-fluid">
		<h3 class="page-header">剧集feed管理</h3>
		<div class="feed-config">
			<div class="row feed-config-row">
				<div class="col-md-3 col-lg-3">
					<strong>${program.title}<c:if test="${program.current_season!='' && program.current_season!=0}">第${program.current_season}季</c:if></strong>
				</div>
				<div class="col-md-3 col-lg-3">
					<strong>时间：</strong>${program.year}
				</div>
			</div>
			<div class="row feed-config-row form-inline">
				<div class="col-md-3 col-lg-3 form-group">
					<label class="control-label">频道：</label>
					<select class="form-control" id="channel_id" name="channel_id" onchange="change_channel()">
						<c:forEach var="cell" items="${channel_list}">
						<option value="${cell.id}">${cell.name}</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-md-3 col-lg-3 form-group">
					<label class="control-label">剧集：</label>
					<select class="form-control" id="episode_id" name="episode_id" onchange="change_episode()">
						<c:forEach var="cell" items="${episode_list}">
						<option value="${cell.id}"><c:if test="${cell.current_episode!='' && cell.current_episode!=0}">第${cell.current_episode}集</c:if><c:if test="${cell.series!=''}"> ${cell.series}</c:if></option>
						</c:forEach>
					</select>
				</div>
				<div class="col-md-3 col-lg-3 form-group">
					<label class="control-label">开始时间：</label>
					<c:if test="${schedule.mec_start_at == null}">
		             <a href="javascript:set_epg_start_at('${schedule.id}')">点击快速设置</a>
		             </c:if>
		            <c:if test="${schedule.mec_start_at != null}">
		             <a href="javascript:set_epg_time('${schedule.id}')" title="点击修改">${schedule.mec_start_at}</a>
		             </c:if>
				</div>
				<div class="col-md-3 col-lg-3 form-group">
					<label class="control-label">结束时间：</label>
					<c:if test="${schedule.mec_end_at == null}">
		             <a href="javascript:set_epg_end_at('${schedule.id}')">点击快速设置</a>
		            </c:if>
		            <c:if test="${schedule.mec_end_at != null}">
		             <a href="javascript:set_epg_time('${schedule.id}')" title="点击修改">${schedule.mec_end_at}</a>
		             </c:if>
				</div>
			</div>
			<div class="row feed-config-row" id="lucky_draw_event_div">
			
			</div>
		</div>
	</div>
	<div class="container-fluid">
		<div class="feed-affix">
			<div id="feed_affix">
				<div class="page-header clearfix">
					<h4 class="pull-left">剧集feed列表</h4>
					<a class="btn btn-primary btn-sm pull-right" href="javascript:tv_tmall_import()" title="feed导入" target="rightFrame">feed导入</a>
				</div>
				<ul class="nav nav-tabs" role="tablist">
					<li class="active"><a href="#tab_mec_person" role="tab" data-toggle="tab">人物</a></li>
					<li><a href="#tab_baike" role="tab" data-toggle="tab">百科</a></li>
					<li><a href="#tab_weibo_feed" role="tab" data-toggle="tab">微直播</a></li>
					<li><a href="#tab_music" role="tab" data-toggle="tab">音乐信息</a></li>
					<li><a href="#tab_question" role="tab" data-toggle="tab">互动问答</a></li>
				</ul>
			</div>
		</div>
		<div class="tab-content">
			<div class="tab-pane active" id="tab_mec_person">
				<jsp:include page="/page/feed/feed/feed_tabs/mec_person.jsp" />
			</div>
			<div class="tab-pane" id="tab_baike">
				<jsp:include page="/page/feed/feed/feed_tabs/baike.jsp" />
			</div>
			<div class="tab-pane" id="tab_weibo_feed">
				<jsp:include page="/page/feed/feed/feed_tabs/weibo_feed.jsp" />
			</div>
			<div class="tab-pane" id="tab_music">
				<jsp:include page="/page/feed/feed/feed_tabs/music.jsp" />
			</div>
			<div class="tab-pane" id="tab_question">
				<jsp:include page="/page/feed/feed/feed_tabs/question.jsp" />
			</div>
		</div>
	</div>
</body>
</html>
