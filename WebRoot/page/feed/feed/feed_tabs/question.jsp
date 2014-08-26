<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script type="text/javascript">
function edit_question(id){
	var url = "${ctx}/sys/question/input/"+id;
	var t = new $.dialog({title:'添加互动问答',id:'edit_question', page:url,rang:true,width :600,height:550,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}

function add_question(id){
	var program_id = $("#program_id").val();
	var episode_id = $("#episode_id").val();
	if(!isNotBlank(episode_id)){
		alert("剧集信息为空，请先设置剧集信息");
		return;
	}
	var url = "${ctx}/sys/question/input?program_id="+program_id+"&episode_id="+episode_id;
	var t = new $.dialog({title:'修改互动问答',id:'add_question', page:url,rang:true,width :600,height:600,
		onXclick:function(){
			t.cancel();
		}
	});
	t.ShowDialog();
}

/** 删除 **/
function del_question(id){
	confirmDialog(function(){
		var url = "${ctx}/sys/question/del/"+id+"?t="+new Date().getTime();
		$.ajax({url:url,dataType:"json",error:function(){alert("删除失败，请稍后重试")}, 
			success: function(data){
				if(data.status=="0"){
					load_question();
				}
			}
		});
	});
}

function set_question_start_time(id){
	var schedule_id = $("#schedule_id").val();
	if(!isNotBlank(schedule_id)){
		alert("请先设置epg的信息");
		return;
	}
	
	if(confirm("确认设置时间？")){
		var url = "${ctx}/sys/question/set_start_time?id="+id+"&schedule_id="+schedule_id;
		$.ajax({url:url,dataType:"json",error:function(){alert("设置失败，请稍后重试");}, 
			success: function(data){
				if(data.status=="0"){
					//alert("设置成功！");
					load_question();
				}else{
					alert(data.msg);
				}
			}
		});	
	}
}

function set_question_time(id,field){
	var schedule_id = $("#schedule_id").val();
	if(!isNotBlank(schedule_id)){
		alert("请先设置epg的信息");
		return;
	}
	if(confirm("确认设置时间？")){
		var url = "${ctx}/sys/question/set_time?id="+id+"&schedule_id="+schedule_id+"&field="+field;
		$.ajax({url:url,dataType:"json",error:function(){alert("设置失败，请稍后重试");}, 
			success: function(data){
				if(data.status=="0"){
					load_question();
				}else{
					alert(data.msg);
				}
			}
		});
	}
}

function load_question(){
	var episode_id = $("#episode_id").val();
	if(!isNotBlank(episode_id)){
		alert("剧集信息为空，请先设置剧集信息");
		return;
	}
	var url = "${ctx}/sys/feed/get_feeds?episode_id="+episode_id+"&modules=question&t="+new Date().getTime();
	$.ajax({url:url,dataType:"json",error:function(){alert("加载失败，请稍后重试");}, 
		success: function(data){
			_TimeArray[4] = 0;//时间归零
			draw_question_list(data.question_list);
		}
	});
}

function draw_question_list(list){
	draw_question(list,"#questionCellTemplate","#questionList",function(obj,html){
		var str = obj.start_time;
		if(!isNotBlank(obj.start_time)){
			str = "<a href=\"javascript:set_question_start_time('"+obj.id+"')\">点击设置</a>";
		}
		html = html.replace("#start_time#",str);
		
		str = obj.end_time;
		if(!isNotBlank(obj.end_time)){
			str = "<a href=\"javascript:set_question_time('"+obj.id+"','end_time')\">点击设置</a>";
		}
		html = html.replace("#end_time#",str);
		
		str = obj.public_time;
		if(!isNotBlank(obj.public_time)){
			str = "<a href=\"javascript:set_question_time('"+obj.id+"','public_time')\">点击设置</a>";
		}
		html = html.replace("#public_time#",str);
		
		str = obj.deadline;
		if(!isNotBlank(obj.deadline)){
			str = "<a href=\"javascript:set_question_time('"+obj.id+"','deadline')\">点击设置</a>";
		}
		html = html.replace("#deadline#",str);
		html = draw_html_time_warn(obj,html,4);//时间冲突标记
		html = draw_inner_time_warn(obj,html);//内部时间标记
		return html;
	});
}

//渲染人物
function draw_question(list,templateId,tableId,cellCallback){
	var colName = ["id","title","deadline","public_time","answer","option_0","option_1","option_2","option_3","option_4","updated_user","updated_at","start_time","end_time"];
	if(list && list.length>0){
		var template = $(templateId).html();
		draw_html_by_template(list,template,tableId,colName,cellCallback);
	}
}

function draw_inner_time_warn(obj,html){
	var inner_mark = obj.long_start_time;
	var time = obj.long_deadline;
	var str = "feed-table-td";
	if(time<inner_mark){
		str = "danger";
	}else{
		inner_mark = time;	
	}
	html = html.replace("#deadline_warn#",str);
	
	time = obj.long_public_time;
	str = "feed-table-td";
	if(time<inner_mark){
		str = "danger";
	}else{
		inner_mark = time;
	}
	html = html.replace("#public_warn#",str);
	
	time = obj.long_end_time;
	str = "feed-table-td";
	if(time<inner_mark){
		str = "danger";
	}else{
		inner_mark = time;
	}
	html = html.replace("#end_warn#",str);
	return html;
}


</script>

<table class="table table-striped table-bordered feed-table">
    <thead class="feed-thead-affix">
        <tr>
            <th colspan="15">
                <div class="clearfix feed-thead-affix-wrap">
                    互动问答Feed列表
                    <a class="btn btn-success btn-sm pull-right" href="javascript:add_question()" title="添加互动问答" target="rightFrame">添加互动问答</a>
                </div>
            </th>
        </tr>
        <tr>
            <th class="text-center feed-qw5">序号</th>
            <th class="text-center feed-qw20">问题</th>
            <th class="text-center feed-qw6">答题出现时间</th>
            <th class="text-center feed-qw6">答题结束时间</th>
            <th class="text-center feed-qw6">答案揭晓时间</th>
            <th class="text-center feed-qw6">问答结束时间</th>
            <th class="text-center feed-qw5">答案</th>
            <th class="text-center feed-qw5">选项A</th>
            <th class="text-center feed-qw5">选项B</th>
            <th class="text-center feed-qw5">选项C</th>
            <th class="text-center feed-qw5">选项D</th>
            <th class="text-center feed-qw5">选项E</th>
            <th class="text-center feed-qw5">操作人</th>
            <th class="text-center feed-qw10">最近一次操作</th>
            <th class="text-center feed-qw5">操作</th>
        </tr>
    </thead>
    <tbody id="questionList">
    <%-- <c:forEach var="cell" items="${element.question_list}">
	<tr class="hovcolor">
		<td>${cell.id}</td>
		<td><a href="javascript:edit_question(${cell.id})">${cell.title}</a></td>
		<td>
		${cell.start_time}
		<c:if test="${cell.start_time==null || cell.start_time==''}">
		<a href="javascript:set_question_start_time('${cell.id}')">点击设置</a>
		</c:if>
   		</td>
		<td>${cell.deadline}</td>
		<td>${cell.public_time}</td>
		<td>${cell.end_time}</td>
		<td>${cell.answer}</td>
		<c:forEach var="option" items="${cell.option}">
		  <td>${option.title}</td>
		</c:forEach>
		<td>${cell.updated_user}</td>
		<td><fmt:formatDate value="${cell.updated_at}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		<td><a href="javascript:edit_question(${cell.id})">编辑</a>
		<a href="javascript:del_question(${cell.id})"> 删除</a></td>
	 </tr>
	</c:forEach> --%>
    </tbody>
</table>


<table style="display: none">
<tbody id="questionCellTemplate">
	<tr>
		<td class="feed-table-td feed-qw5">#id#</td>
		<td class="feed-table-td feed-qw20">
			<a href="javascript:edit_question('#id#')">#title#</a>
		</td>
		<td class="#start_warn# feed-qw6">#start_time#</td>
		<td class="#deadline_warn# feed-qw6">#deadline#</td>
		<td class="#public_warn# feed-qw6">#public_time#</td>
		<td class="#end_warn# feed-qw6">#end_time#</td>
		<td class="feed-table-td feed-qw5">#answer#</td>
		<td class="feed-table-td feed-qw5">#option_0#</td>
		<td class="feed-table-td feed-qw5">#option_1#</td>
		<td class="feed-table-td feed-qw5">#option_2#</td>
		<td class="feed-table-td feed-qw5">#option_3#</td>
		<td class="feed-table-td feed-qw5">#option_4#</td>
		<td class="feed-table-td feed-qw5">#updated_user#</td>
		<td class="feed-table-td feed-qw10">#updated_at#</td>
		<td class="feed-table-td feed-qw5">
			<a href="javascript:edit_question('#id#')">编辑</a>
			<a href="javascript:del_question('#id#')"> 删除</a>
		</td>
	 </tr>
</tbody>
</table>