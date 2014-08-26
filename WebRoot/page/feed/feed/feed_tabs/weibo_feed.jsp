<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript">
/*编辑百科*/
function disable_opt(id,opt){
	var msg = "确认要禁用？";
	if(opt == 1){
		msg = "确认要启用？";
	}
	if(confirm(msg)){
		var url = "${ctx}/sys/weibo_feed/disable_opt?id="+id+"&opt="+opt;
		$.ajax({url:url,dataType:"json",error:function(){alert("删除失败，请稍后重试");}, 
			success: function(data){
				if(data.status=="0"){
					if(opt == 1){
						alert("启用成功！");
					}else{
						alert("禁用成功！");
					}
					load_weibo_feed();
				}
			}
		});
	}
}

function load_weibo_feed(){
	var episode_id = $("#episode_id").val();
	var url = "${ctx}/sys/feed/get_feeds?episode_id="+episode_id+"&modules=weibo_feed&t="+new Date().getTime();
	$.ajax({url:url,dataType:"json",error:function(){alert("加载失败，请稍后重试");}, 
		success: function(data){
			draw_weibo_feed_list(data.weibo_feed_list);
		}
	});
}


function draw_weibo_feed_list(list){
	draw_weibo_feed(list,"#weiboFeedCellTemplate","#weiboFeedList",function(obj,html){
		var str = obj.start_time;
		if(!isNotBlank(obj.start_time)){
			str = "<a href=\"javascript:set_question_start_time('"+obj.id+"','question')\">点击设置</a>";
		}
		html = html.replace("#start_time#",str);

		str = "";
		if(obj.status){
			str = "<a href=\"javascript:disable_opt('#id#','0')\">禁用</a>";	
		}else if(!obj.status){
			str = "<a href=\"javascript:disable_opt('#id#','1')\">启用</a>";	
		}
		html = html.replace("#opt#",str);
		return html;
	});
}

//渲染百科
function draw_weibo_feed(list,templateId,tableId,cellCallback){
	var colName = ["id","content","sender_name","sender_avatar","sender_url","updated_user","status","start_time_str","updated_at","start_time"];
	if(list && list.length>0){
		var template = $(templateId).html();
		draw_html_by_template(list,template,tableId,colName,cellCallback);
	}
}

function set_weibo_task(){
	var program_id = $("#program_id").val();
	var episode_id = $("#episode_id").val();
	var url = "${ctx}/sys/weibo_feed/set_weibo_time?program_id="+program_id+"&episode_id="+episode_id;
	var t = new $.dialog({title:'设置微博时间',id:'edit', page:url,rang:true,width :600,height:280,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
	
}
</script>

<input type="hidden" name="episode_id" id="episode_id" value="${episode_id}" />

<table class="table table-striped table-bordered feed-table">
    <thead class="feed-thead-affix">
        <tr>
            <th colspan="7">
                <div class="clearfix feed-thead-affix-wrap">
                    微博Feed列表
                    <a class="btn btn-success btn-sm pull-right" href="javascript:set_weibo_task()" title="微博设置" target="rightFrame">微博设置</a>
                </div>
            </th>
        </tr>
        <tr>
            <th class="text-center col-md-1">序号</th>
            <th class="text-center col-md-1">头像</th>
            <th class="text-center col-md-2">名称</th>
            <th class="text-center col-md-5">内容</th>
            <th class="text-center col-md-1">开始时间</th>
            <th class="text-center col-md-1">发布时间</th>
            <th class="text-center col-md-1">操作</th>
        </tr>
    </thead>
    <tbody id="weiboFeedList">
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
	<tbody id="weiboFeedCellTemplate">
		<tr>
			<td class="feed-table-td col-md-1">#id#</td>
			<td class="feed-table-td col-md-1"><a href="#sender_avatar#" target="_blank"> <img src="#sender_avatar#" alt="" width="50px" height="50px"></a></td>
			<td class="feed-table-td col-md-2"><a href="#sender_url#" target="_blank">#sender_name#</a></td>
			<td class="feed-table-td col-md-5">#content#</td>
			<td class="feed-table-td col-md-1">#start_time#</td>
			<td class="feed-table-td col-md-1">#start_time_str#</td>
			<td class="feed-table-td col-md-1">#opt#</td>
		</tr>
   </tbody>
</table>