<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="tv.zhiping.common.util.ComUtil"%>
<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script type="text/javascript">

/*添加music*/
function add_music(){
	var program_id = $("#program_id").val();
	var episode_id = $("#episode_id").val();
	if(!isNotBlank(episode_id)){
		alert("剧集信息为空，请先设置剧集信息");
		return;
	}
	var url = "${ctx}/sys/music/input_element?program_id="+program_id+"&episode_id="+episode_id;
	var t = new $.dialog({title:'添加音乐',id:'edit', page:url,rang:true,width :600,height:600,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}
/*编辑music*/
function edit_music(element_id){
	var program_id = $("#program_id").val();
	var episode_id = $("#episode_id").val();
	if(!isNotBlank(episode_id)){
		alert("剧集信息为空，请先设置剧集信息");
		return;
	}
	var url = "${ctx}/sys/music/input_element?program_id="+program_id+"&episode_id="+episode_id+"&element_id="+element_id;
	var t = new $.dialog({title:'编辑音乐',id:'edit', page:url,rang:true,width :600,height:600,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}

function load_music(){
	var episode_id = $("#episode_id").val();
	if(!isNotBlank(episode_id)){
		alert("剧集信息为空，请先设置剧集信息");
		return;
	}
	var url = "${ctx}/sys/feed/get_feeds?episode_id="+episode_id+"&modules=music&t="+new Date().getTime();
	$.ajax({url:url,dataType:"json",error:function(){alert("加载失败，请稍后重试");}, 
		success: function(data){
			_TimeArray[3] = 0;//时间归零
			draw_music_list(data.music_list);
		}
	});
}


function draw_music_list(list){
	draw_music(list,"#musicCellTemplate","#musicList",function(obj,html){
		var str = obj.start_time;
		if(!isNotBlank(obj.start_time)){
			str = "<a href=\"javascript:set_element_start_time('"+obj.id+"','music')\">点击设置</a>";
		}
		html = html.replace("#start_time#",str);
		
		str = obj.end_time;
		if(!isNotBlank(obj.end_time)){
			str = "<a href=\"javascript:set_element_end_time('"+obj.id+"','music')\">点击设置</a>";
		}
		html = html.replace("#end_time#",str);
		
		str = obj.cover;
		if(isNotBlank(obj.cover)){
			str = "<img alt=\"\" src=\""+obj.cover+"\" width=\"50px\" height=\"50px\">";
		}else{
			str="";
		}
		html = html.replace("#cover#",str);
		html = draw_html_time_warn(obj,html,3);//时间冲突标记
		return html;
	});
}

//渲染百科
function draw_music(list,templateId,tableId,cellCallback){
	var colName = ["id","music_tag","music_title","singer","cover","source_url","updated_user","updated_at","start_time","end_time"];
	if(list && list.length>0){
		var template = $(templateId).html();
		draw_html_by_template(list,template,tableId,colName,cellCallback);
	}
}
</script>

<table class="table table-striped table-bordered feed-table">
    <thead class="feed-thead-affix">
        <tr>
            <th colspan="11">
                <div class="clearfix feed-thead-affix-wrap">
                    音乐Feed列表
                    <a class="btn btn-success btn-sm pull-right" href="javascript:add_music()" title="微博设置" target="rightFrame">添加音乐</a>
                </div>
            </th>
        </tr>
        <tr>
            <th class="text-center col-md-1">序号</th>
            <th class="text-center col-md-1">标签</th>
            <th class="text-center col-md-1">歌名</th>
            <th class="text-center col-md-1">歌手</th>
            <th class="text-center col-md-1">封面</th>
            <th class="text-center col-md-1">播放连接</th>
            <th class="text-center col-md-1">开始时间</th>
            <th class="text-center col-md-1">结束时间</th>
            <th class="text-center col-md-1">操作人</th>
            <th class="text-center col-md-2">最近一次操作</th>
            <th class="text-center col-md-1">操作</th>
        </tr>
    </thead>
    <tbody id="musicList">
    <%-- <c:forEach var="cell" items="${element.music_list}">
    <tr class="hovcolor">
        <td>${cell.id}</td>
        <td>${cell.music_tag}</td>
        <td>${cell.music_title}</td>
        <td>${cell.singer}</td>
        <td><img src="${cell.cover}" alt="" width="50px" height="50px"></td>
        <td><a href="${cell.source_url}" target="_blank">${cell.source_url}</a></td>
        <td>${cell.start_time}</td>
        <td>${cell.end_time}</td>
        <td><a href="javascript:edit_music(${cell.id})">编辑</a>
        <a href="javascript:del_element(${cell.id})"> 删除</a></td>
  </tr>
</c:forEach> --%>
    </tbody>
</table>


<table style="display: none">
	<tbody id="musicCellTemplate">
		<tr>
  			<td class="feed-table-td col-md-1">#id#</td>
  			<td class="feed-table-td col-md-1">#music_tag#</td>
  			<td class="feed-table-td col-md-1"><a href="javascript:edit_music(#id#)">#music_title#</a></td>
  			<td class="feed-table-td col-md-1">#singer#</td>
  			<td class="feed-table-td col-md-1">#cover#</td>
  			<td class="feed-table-td col-md-1"><a href="#source_url#" target="_blank">#source_url#</a></td>
  			<td class="#start_warn# col-md-1">#start_time#</td>
			<td class="#end_warn# col-md-1">#end_time#</td>
			<td class="feed-table-td col-md-1">#updated_user#</td>
			<td class="feed-table-td col-md-2">#updated_at#</td>
  			<td class="feed-table-td col-md-1">
                <a href="javascript:edit_music(#id#)">编辑</a>
                <a href="javascript:del_element(#id#,'music')"> 删除</a>
            </td>
   </tbody>
</table>
