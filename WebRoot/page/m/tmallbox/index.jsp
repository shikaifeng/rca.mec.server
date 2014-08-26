<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf8" />
	<meta name="viewport" content="width=device-width,height=device-height,inital-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <title>微电视</title>
    <link rel="stylesheet" href="${stCtx}/css/m/tmallbox.css?${random}" />
    <script type="text/javascript" src="${stCtx}/js/zepto.min.js"></script>
</head>
<body>
	<div id="loki" class="loki">
        <div class="loki-feed" id="loki_feed">
            <div class="feed-weibo" id="feed_weibo"></div>
            <!--//.feed-weibo -->


            <div class="feed-wiki">
                <h2 class="wiki-head"><!-- wiki --></h2>
                <div class="wiki-wrap" id="feed_wiki"></div>
            </div>
            <!--//.feed-wiki -->

            <div class="feed-interaction">
                <h2 class="ian-head"><!-- 互动问答 <--></h2>
                <div class="ian-wrap" id="feed_ian">
                    <article class="ian-item ian-none" id="ian_init">
                        <p class="none-title">参与互动获神秘礼物</p>
                        <div class="none-banner"><img src="${stCtx}/images/m/interaction-banner.png" alt="" /></div>
                    </article>
                </div>
            </div>
            <!--//.feed-interaction -->
             <div class="feed-close"><!-- close --></div>
            <!--//.feed-close -->
        </div>
        <!--//.loki-feed -->

        <div class="loki-lucky" id="loki_lucky">
            <div class="lucky-banner">
                <div class="lucky-banner-mask-top"><!-- lucky-banner-mask-top --></div>
                <div class="lucky-banner-mask-bottom"><!-- lucky-banner-mask-bottom --></div>
                <img src="${stCtx}/images/m/lucky-banner-pic.png" alt="中国好声音 第三季" class="lucky-banner-pic">
            </div>
            <p class="lucky-tip">感谢您参与本节目互动</p>
            <div id="loki_lucky_extra">
                <p class="lucky-title">您有<em class="mark"><span id="lucky_count"></span>次</em>抽奖机会</p>
                <a href="javascript:void(0);" class="lucky-button"><!-- button --></a>
            </div>
        </div>
    </div>
    <!--//#loki -->
    
    <script type="text/javascript" src="${stCtx}/js/m/animate.js?${random}"></script>
    <script type="text/javascript" src="${stCtx}/js/m/tmallbox.js?${random}"></script>
</body>
</html>