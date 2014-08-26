<%@ include file="/page/common/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script type="text/javascript">
	/** 编辑 **/
	function edit_mec_person(element_id){
		var url = "${ctx}/sys/mec_person/input_element/"+element_id;
		var t = new $.dialog({title:'编辑明星',id:'edit', page:url,rang:true,width :600,height:600,
			onXclick:function(){
				t.cancel();
			}
		});
		t.ShowDialog();
	}
	
	function add_mec_person(element_id){
		var program_id = $("#program_id").val();
		var episode_id = $("#episode_id").val();
		if(!isNotBlank(episode_id)){
			alert("剧集信息为空，请先设置剧集信息");
			return;
		}
		var url = "${ctx}/sys/mec_person/input_element?program_id="+program_id+"&episode_id="+episode_id;
		var t = new $.dialog({title:'添加明星',id:'edit', page:url,rang:true,width :600,height:600,
			onXclick:function(){
				t.cancel();
			}
		});
		t.ShowDialog();
	}

	function load_mec_person(){
		var episode_id = $("#episode_id").val();
		if(!isNotBlank(episode_id)){
			alert("剧集信息为空，请先设置剧集信息");
			return;
		}
		var url = "${ctx}/sys/feed/get_feeds?episode_id="+episode_id+"&modules=mec_person&t="+new Date().getTime();
		$.ajax({url:url,dataType:"json",error:function(){alert("加载失败，请稍后重试");}, 
			success: function(data){
				_TimeArray[0] = 0;//时间归零
				draw_mec_person_list(data.mec_person_list);
			}
		});
	}
	function draw_mec_person_list(list){

		draw_mec_person(list,"#mecPersonCellTemplate","#mecPersonList",function(obj,html){
			var str = obj.start_time;
			if(!isNotBlank(obj.start_time)){
				str = "<a href=\"javascript:set_element_start_time('"+obj.id+"','mec_person')\">点击设置</a>";
			}
			html = html.replace("#start_time#",str);
			
			str = obj.end_time;
			if(!isNotBlank(obj.end_time)){
				str = "<a href=\"javascript:set_element_end_time('"+obj.id+"','mec_person')\">点击设置</a>";
			}
			html = html.replace("#end_time#",str);
			
			str = obj.person_avatar;
			if(isNotBlank(obj.person_avatar)){
				str = "<img alt=\"\" src=\""+obj.person_avatar+"\" width=\"50px\" height=\"50px\">";
			}else{
				str="";
			}
			html = html.replace("#show_avatar#",str);
			html = draw_html_time_warn(obj,html,0);//时间冲突标记
			return html;
		});
	}
	
	//渲染人物
	function draw_mec_person(list,templateId,tableId,cellCallback){
		var colName = ["id","person_name","person_desc","person_avatar","baike_url","updated_user","updated_at","start_time","end_time"];
		if(list && list.length>0){
			var template = $(templateId).html();
			draw_html_by_template(list,template,tableId,colName,cellCallback);
		}
	}
</script>


<table class="table table-striped table-bordered feed-table">
    <thead class="feed-thead-affix">
        <tr>
            <th colspan="10">
                <div class="clearfix feed-thead-affix-wrap">
                    人物Feed列表
                    <a class="btn btn-success btn-sm pull-right" href="javascript:add_mec_person()" title="添加人物" target="rightFrame">添加人物</a>
                </div>
            </th>
        </tr>
        <tr>
            <th class="text-center col-md-1">序号</th>
            <th class="text-center col-md-1">人名</th>
            <th class="text-center col-md-2">简介</th>
            <th class="text-center col-md-1">图片</th>
            <th class="text-center col-md-2">百科URL</th>
            <th class="text-center col-md-1">开始时间</th>
            <th class="text-center col-md-1">结束时间</th>
            <th class="text-center col-md-1">操作人</th>
            <th class="text-center col-md-1">最近一次操作</th>
            <th class="text-center col-md-1">操作</th>
        </tr>
    </thead>
    <tbody id="mecPersonList">
    <%--<c:forEach var="cell" items="${element.mec_person_list}">
        <tr>
            <td>${cell.id}</td>
            <td>
                <a href="javascript:edit_mec_person(${cell.id})">${cell.person_name}</a>
            </td>
            <td>${cell.person_desc}</td>
            <td>
                <a href="${cell.person_avatar}"><img alt="" src="${cell.person_avatar}" width="50px" height="50px"></a>
            </td>
            <td>
                ${cell.start_time}
                <c:if test="${cell.start_time==null || cell.start_time==''}">
                <a href="javascript:set_element_start_time('${cell.id}')">点击设置</a>
                </c:if>
            </td>
            <td>${cell.end_time}</td>
            <td>${cell.updated_user}</td>
            <td>${cell.updated_at}</td>
            <td>
                <a href="javascript:edit_mec_person(${cell.id})">编辑</a>
                <a href="javascript:del_element(${cell.id})">删除</a>
            </td>
        </tr>
    </c:forEach>--%>
    </tbody>
</table>

<table style="display: none">
    <tbody id="mecPersonCellTemplate">
    	<tr>
    		<td class="feed-table-td col-md-1">#id#</td>
    		<td class="feed-table-td col-md-1">
                <a href="javascript:edit_mec_person('#id#')">#person_name#</a>
            </td>
    		<td class="feed-table-td col-md-2">#person_desc#</td>
    		<td class="feed-table-td col-md-1">
                <a href="#person_avatar#">#show_avatar#</a>
            </td>
    		<td class="feed-table-td col-md-2">#baike_url#</td>
    		<td class="#start_warn# col-md-1">#start_time#</td>
    		<td class="#end_warn# col-md-1">#end_time#</td>
    		<td class="feed-table-td col-md-1">#updated_user#</td>
    		<td class="feed-table-td col-md-1">#updated_at#</td>
    		<td class="feed-table-td col-md-1">
                <a href="javascript:edit_mec_person('#id#')">编辑</a>
                <a href="javascript:del_element('#id#','mec_person')"> 删除</a>
            </td>
    	</tr>
  </tbody>
</table>