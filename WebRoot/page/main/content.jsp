<%@page import="tv.zhiping.common.Cons"%>
<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登陆页面</title>
<%@ include file="/page/common/jscommon.jsp"%>
<link href="${stCtx}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${stCtx}/css/list.css" rel="stylesheet" type="text/css" />
</head>
<body>
   <div id="cont_r"><!--cont_r-->
        <div id="wh_search">
           <dl>
               <dt>文汇讲堂</dt>
               <dd>
                  <input class="inp_text" onblur="if(this.value==''||this.value=='关键字搜索') this.value='关键字搜索'" onfocus="if(this.value=='关键字搜索') this.value=''" value="关键字搜索" name="" type="text" />
                  <input class="inp_but" name="" type="button" />
                </dd>
           </dl>
        </div><!--wh_search-->
        
        <div id="list_cont">
            <div class="whlist">
              <h3><a href="#">2011上海网货交易会</a><span><img src="${stCtx}/images/icon_status01.gif" width="44" height="22" /></span></h3>
              <div class="wh_timeplace">
                 <p><img src="${stCtx}/images/icon_time.gif" width="20" height="20" /> 2011年12月16日 -2011年12月18日</p>
                 <p><img src="${stCtx}/images/icon_place.gif" width="20" height="20" /> 上海世贸商城延安路2299号上海世贸商城延安路2299号</p>
              </div>
              <ul>
                    <li><a href="#"><img src="${stCtx}/images/icon_set.gif" width="16" height="16" />设置</a></li>
                    <li><a href="#"><img src="${stCtx}/images/icon_user.gif" width="16" height="16" />参加者</a></li>
                    <li><a href="#"><img src="${stCtx}/images/icon_share.gif" width="16" height="16" />分享</a></li> 
              </ul>
            </div><!--meetlis结束-->
            
            <div class="whlist">
              <h3><a href="#">2011上海网货交易会</a><span><img src="${stCtx}/images/icon_status01.gif" width="44" height="22" /></span></h3>
              <div class="wh_timeplace">
                 <p><img src="${stCtx}/images/icon_time.gif" width="20" height="20" /> 2011年12月16日 -2011年12月18日</p>
                 <p><img src="${stCtx}/images/icon_place.gif" width="20" height="20" /> 上海世贸商城延安路2299号上海世贸商城延安路2299号</p>
              </div>
              <ul>
                    <li><a href="#"><img src="${stCtx}/images/icon_set.gif" width="16" height="16" />设置</a></li>
                    <li><a href="#"><img src="${stCtx}/images/icon_user.gif" width="16" height="16" />参加者</a></li>
                    <li><a href="#"><img src="${stCtx}/images/icon_share.gif" width="16" height="16" />分享</a></li> 
              </ul>
            </div><!--meetlis结束-->
            
            <div id="page">
                    <a href="#" class="previous">上一页</a>
					<b>1</b> 
					<a href="#">2</a><a href="#">3</a><a href="#">4</a>
					<a href="#0">5</a><a href="#">6</a><a href="#">7</a><a href="#5">8</a>
					<a href="#">9</a><a href="#">10</a>
					<a href="#" class="next">下一页</a>
					<a href="#" class="last">尾页</a>
		   </div><!--page结束-->
        </div> <!--list_cont结束-->
   </div><!--cont_r结束-->
</body>
</html>