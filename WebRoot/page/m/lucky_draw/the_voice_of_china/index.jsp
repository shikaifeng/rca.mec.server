<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf8" />
	<meta name="viewport" content="width=device-width,height=device-height,inital-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <title>中国好声音抽奖页面</title>
    <link rel="stylesheet" href="${stCtx}/css/m/luck_draw/the_voice_of_china.min.css?${random}" />
    <script type="text/javascript" src="${stCtx}/js/zepto.min.js"></script>
    <script type="text/javascript" src="${stCtx}/js/m/base.js"></script>
</head>
<body>
	<div class="luck-draw-contain">
        <div class="luck-draw-title">
            <p class="title-content" id="luck_draw_title_content"></p>
            <p class="title-tip" id="luck_draw_title_tip"></p>
        </div>
        <div class="luck-draw-winners">
            <ul>
            	<c:forEach var="cell" items="${historys}">
                <li>ID&nbsp;${cell.udid}&nbsp;获得&nbsp;${cell.opt_title}</li>
                </c:forEach>
            </ul>
        </div>
        <div class="luck-draw-button" id="luck_draw_button">
            <button class="button button-draw" id="luck_draw_button_item_1"><!-- big button --></button>
        </div>
        <div class="luck-draw-wheel">
            <img src="${stCtx}/images/m/luck_draw/the_voice_of_china/luck_draw_wheel.png" alt="抽奖大转盘" id="luck_draw_wheel" class="" />
            <i class="wheel-point"><!-- point --></i>
        </div>
    </div>
    <div class="luck-overlay" id="luck_overlay"><!-- overlay zIndex:800 --></div>
    <div class="luck-dialog" id="luck_dialog"><!-- zIndex:1000 -->
        <h3 class="dialog-title" id="luck_dialog_title"></h3>
        <p class="dialog-text" id="luck_dialog_text"></p>
        <p class="dialog-txt-sub">您还有<em class="mark" id="luck_dialog_count"></em>次抽奖机会</p>
        <div class="dialog-button" id="luck_dialog_button_wrap">
            <button class="button button-draw" id="luck_dialog_button_item_1"><!-- 弹层按钮 --></button>
        </div>
    </div>

    <input type="hidden" id="play_count" value="${obj.lucky_surplus_count}" /><!-- 抽奖剩余次数，需要初始化加载 -->
    <input type="hidden" id="udid" value="${obj.udid}" /><!-- udid，需要初始化载入 -->
    <input type="hidden" id="lucky_url" value="${obj.url}" /><!-- 领奖url，需要初始化加载 -->
    <input type="hidden" id="get_play_result_id" value="${obj.id}" /><!-- 领奖id，需要初始化加载 -->

<script>
    luckList = ${luckList};
</script>
<script type="text/javascript" src="${stCtx}/js/m/the_voice_of_china.js"></script>
</body>
</html>