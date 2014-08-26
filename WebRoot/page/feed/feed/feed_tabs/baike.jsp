<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript">
/*添加百科*/
function add_baike(){
	var program_id = $("#program_id").val();
	var episode_id = $("#episode_id").val();
	if(!isNotBlank(episode_id)){
		alert("剧集信息为空，请先设置剧集信息");
		return;
	}
	var url = "${ctx}/sys/baike/input_element?program_id="+program_id+"&episode_id="+episode_id;
	var t = new $.dialog({title:'添加百科',id:'edit', page:url,rang:true,width :600,height:600,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}
/*编辑百科*/
function edit_baike(element_id){
	var program_id = $("#program_id").val();
	var episode_id = $("#episode_id").val();
	if(!isNotBlank(episode_id)){
		alert("剧集信息为空，请先设置剧集信息");
		return;
	}
	var url = "${ctx}/sys/baike/input_element?program_id="+program_id+"&episode_id="+episode_id+"&element_id="+element_id;
	var t = new $.dialog({title:'编辑百科',id:'edit', page:url,rang:true,width :600,height:600,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}


function load_baike(){
	var episode_id = $("#episode_id").val();
	if(!isNotBlank(episode_id)){
		alert("剧集信息为空，请先设置剧集信息");
		return;
	}
	var url = "${ctx}/sys/feed/get_feeds?episode_id="+episode_id+"&modules=baike&t="+new Date().getTime();
	$.ajax({url:url,dataType:"json",error:function(){alert("加载失败，请稍后重试");}, 
		success: function(data){
			_TimeArray[1] = 0;//时间归零
			draw_baike_list(data.baike_list);
		}
	});
}
function draw_baike_list(list){
	draw_baike(list,"#baikeCellTemplate","#baikeList",function(obj,html){
		var str = obj.start_time;
		if(!isNotBlank(obj.start_time)){
			str = "<a href=\"javascript:set_element_start_time('"+obj.id+"','baike')\">点击设置</a>";
		}
		html = html.replace("#start_time#",str);
		
		str = obj.end_time;
		if(!isNotBlank(obj.end_time)){
			str = "<a href=\"javascript:set_element_end_time('"+obj.id+"','baike')\">点击设置</a>";
		}
		html = html.replace("#end_time#",str);
		
		str = obj.cover;
		if(isNotBlank(obj.cover)){
			str = "<img alt=\"\" src=\""+obj.cover+"\" width=\"50px\" height=\"50px\">";
		}else{
			str="";
		}
		html = html.replace("#show_cover#",str);
		html = draw_html_time_warn(obj,html,1);//时间冲突标记
		return html;
	});
}


//渲染百科
function draw_baike(list,templateId,tableId,cellCallback){
	var colName = ["id","baike_title","desc","cover","baike_url","baike_url","updated_user","updated_at","start_time","end_time"];
	if(list && list.length>0){
		var template = $(templateId).html();
		draw_html_by_template(list,template,tableId,colName,cellCallback);
	}
}
</script>

<input type="hidden" name="episode_id" id="episode_id" value="${episode_id}" />

<table class="table table-striped table-bordered feed-table">
	<thead class="feed-thead-affix">
        <tr>
            <th colspan="10">
                <div class="clearfix feed-thead-affix-wrap">
                    百科Feed列表
                    <a class="btn btn-success btn-sm pull-right" href="javascript:add_baike()" title="添加百科" target="rightFrame">添加百科</a>
                </div>
            </th>
        </tr>
        <tr>
            <th class="text-center col-md-1">序号</th>
            <th class="text-center col-md-1">词条</th>
            <th class="text-center col-md-2">详细介绍</th>
            <th class="text-center col-md-1">图片URL</th>
            <th class="text-center col-md-1">百科URL</th>
            <th class="text-center col-md-1">开始时间</th>
            <th class="text-center col-md-1">结束时间</th>
            <th class="text-center col-md-1">操作人</th>
            <th class="text-center col-md-2">最近一次操作</th>
            <th class="text-center col-md-1">操作</th>
        </tr>
    </thead>
    <tbody id="baikeList">
    <%-- <c:forEach var="cell" items="${element.baike_list}">
			<tr class="hovcolor">
				<td>${cell.id}</td>
				<td>${cell.baike_title}</td>
				<td>${cell.desc}</td>
				<td><a href="${cell.cover}" target="_blank"> <img src="${cell.cover}" alt="" width="50px" height="50px"></a></td>
				<td><a href="${cell.baike_url}" target="_blank">${cell.baike_url}</a></td>
				<td>${cell.start_time}</td>
				<td>${cell.end_time}</td>
				<td><a href="javascript:edit_baike(${cell.id})">编辑</a>
					<a href="javascript:del_element(${cell.id})"> 删除</a>
				</td>
			</tr>
		</c:forEach> --%>
    </tbody>
</table>


<table style="display: none">
	<tbody id="baikeCellTemplate">
		<tr>
			<td class="feed-table-td col-md-1">#id#</td>
			<td class="feed-table-td col-md-1"><a href="javascript:edit_baike('#id#')">#baike_title#</a></td>
			<td class="feed-table-td col-md-2">#desc#</td>
			<td class="feed-table-td col-md-1"><a href="#cover#" target="_blank">#show_cover#</a></td>
			<td class="feed-table-td col-md-1"><a href="#baike_url#" target="_blank">#baike_url#</a></td>
			<td class="#start_warn# col-md-1">#start_time#</td>
			<td class="#end_warn# col-md-1">#end_time#</td>
			<td class="feed-table-td col-md-1">#updated_user#</td>
			<td class="feed-table-td col-md-2">#updated_at#</td>
			<td class="feed-table-td col-md-1">
				<a href="javascript:edit_baike('#id#')">编辑</a>
				<a href="javascript:del_element('#id#','baike')">删除</a>
			</td>
		</tr>
   </tbody>
</table>