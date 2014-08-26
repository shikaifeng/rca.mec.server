<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="col-md-3 col-lg-3">
	<strong>领奖活动名称：${lucky_draw_event.title}</strong>
</div>
<div class="col-md-3 col-lg-3">
	<strong>抽奖出现时间：
	<c:if test="${lucky_draw_episode.id != null}">
		<c:if test="${lucky_draw_episode.start_time != null && lucky_draw_episode.start_time != ''}">
			<a href="javascript:set_lucky_episode_time_dialog('${lucky_draw_episode.id}')">${lucky_draw_episode.start_time}</a>
		</c:if>
		<c:if test="${lucky_draw_episode.start_time == null || lucky_draw_episode.start_time == ''}">
			<a href="javascript:set_lucky_episode_time('${lucky_draw_episode.id}','start_time')" title="点击修改">点击快速设置</a>
		</c:if>
	</c:if>
	</strong>
</div>
<%-- <div class="col-md-3 col-lg-3">
	<strong>抽奖结束时间：
	<c:if test="${lucky_draw_episode.id != null}">
		<c:if test="${lucky_draw_episode.end_time != null && lucky_draw_episode.end_time != ''}">
			<a href="javascript:set_lucky_episode_time_dialog('${lucky_draw_episode.id}')">${lucky_draw_episode.end_time}</a>
		</c:if>
		<c:if test="${lucky_draw_episode.end_time == null || lucky_draw_episode.end_time == ''}">
			<a href="javascript:set_lucky_episode_time('${lucky_draw_episode.id}','end_time')" title="点击修改">点击快速设置</a>
		</c:if>
	</c:if>
	</strong>
</div> --%>