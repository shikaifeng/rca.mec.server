//js 的全局变量
var defaultPhotp = stCtx+"/images/defaultphoto.jpg";
var uploadfile = "upload";
var maxPerPage = 20;

String.prototype.trim=function() {
    return this.replace(/(^\s*)|(\s*$)/g,'');
};

//一些公共方法
var seleOrginId;
var topNull;
//这个一般是ajax的回调函数
function createOpts(data){
	$(seleOrginId).empty();
	if(topNull){
		$(seleOrginId).append($("<option/>").attr("value","").text("")); 
	}
	if(data!=null){
		$.each(data,function(key,value){
			$(seleOrginId).append($("<option/>").attr("value",key).text(value)); 
		});
	}
}

//通过省得到市
function getSelAreaMap(province,sel,top){
	seleOrginId=sel;
	topNull=top;
	if(province!=""){
		$.ajax({
			type:"post",
			dataType:"json",
			url:ctx+"/ajax/getSelAreaMap.do",
			data:{parentName:province,q:"a"},
			success:function(data){
				createOpts(data);
			}
		});
	}else{
		$(seleOrginId).empty();
		if(topNull){
			$(seleOrginId).append($("<option/>").attr("value","").text("")); 
		}
	}
}


//加载框显示
function loaderDiveShow(){
	if($("#loaderDiv").length==0){
		$("body").prepend('<div id="loaderDiv" style="position: absolute;display: none;z-index:999;"><img src="'+stCtx+'/images/wait.gif" alt="loading..." /></div>');
		$("#loaderDiv").css("top","50%").css("left","50%");
	}
	$("#loaderDiv").show();	
}


//字符串时间转换： 01:01:01 -> 3723
function getTime(str){
	var time = str;
	if(isNotBlank(str)){
		str = str.replace("：",":");
		var index = str.indexOf(":");
		if(index > -1){
			var hour = parseInt(str.substring(0,index));			
			str = str.substring(index+1);
			index = str.indexOf(":");
			if(index > -1){
				var minture = parseInt(str.substring(0,index));
				var secon = parseInt(str.substring(index+1));
				time = secon + minture*60 + hour*60*60;
			}		
		}
	}
	return time;
}

//时间格式化: 3723 转换成1:02:03
function secondFormate(secon){
	var t = "";
	if(isNotBlank(secon)){
		if(secon > -1){
			var hour = parseInt(secon/3600);
			var min = parseInt(secon/60 % 60);
			var sec = parseInt(secon % 60);
			var day = parseInt(hour/24);
			if (day > 0) {
				hour = hour - 24 * day;
				t = day + "day " + hour + ":";
			}else{
				t = hour + ":";   
			}
			if(min < 10){
				t += "0";
			}
			t += min + ":";
			if(sec < 10){
				t += "0";
			}
			t += sec;
		}
	}
	return t;
}

//加载框不显示
function loaderDiveHide(){
	$("#loaderDiv").hide();
}

//选择所有
function checksAll(){
	var flag = $("#checksAll").attr("checked");
	if(flag){
		flag = true;
	}else{
		flag = false;	
	}
	$("[name=checks]").attr("checked",flag);
}

function getLhgdialogParent(){
	var p = window.top.rightFrame;
	if(!isNotBlank(p)){
		p = window.parent;
	}
	return p;
}

//删除确认
function confirmDialog(delCallBack){
	/*if($("#confirmDialogDiv").length==0){
		$("body").prepend('<div id="confirmDialogDiv" style="display: none"><p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>是否确认删除?</p></div>');
	}
	$( "#confirmDialogDiv" ).dialog({
		resizable: false,
		title:"删除确认",
		height:150,
		modal: true,
		buttons: {
			"确认": function() {
				$(this).dialog("close");
				delCallBack();				
			},
			"关闭": function() {
				$( this ).dialog( "close" );
			}
		}
	});*/
	
	if(confirm("确认删除？")){
		delCallBack();
	}
}

//得到下拉列表中value对应的text
function getSelText(sel,value){
	var v;
	sel.each(function(){
		if($(this).val() == value){
			v = $(this).text();
		}
	});
	return v;
}

//创建一个空的下拉列表
function createEmptyOpts(){
	$(seleOrginId).empty();
	$(seleOrginId).append($("<option/>").attr("value","").text(""));
}

function noteCharLen(src,showTargetTip) {
	if(showTargetTip==null){
		showTargetTip="noteCharLenId";
	}
	$("#"+showTargetTip).html($(src).val().length);
}


//判断是否选择了
function checkIsSel(){
	return ($("[name=checks]:checked").length != 0);
}

function checkIsSel(){
	return ($("[name=checks]:checked").length != 0);
}

//选择所有
function getChecksId(){
	var srr = $("[name=checks]:checked");
	var ids = "";
	for(var i=0;i<srr.length;i++){
		ids = ids + ","+ $(srr[i]).val();
	}
	ids = ids.substr(1,ids.length);
	return ids;
}


function selectAllMultipleOpts(src){
	src.find("option").each(function() {
	       $(this).attr("selected",true);;
	});
}

function moveMultipleOpts(src,target){
	src.find("option:selected").each(function() {
       target.append($("<option/>").attr("value",this.value).text(this.text)); 
       $(this).remove();
    });	
}

function moveAllMultipleOpts(src,target){
	src.find("option").each(function() {
       target.append($("<option/>").attr("value",this.value).text(this.text)); 
       $(this).remove();
    });	
}

function getCookieValue(name){
	var cookieStr = new String(document.cookie);
	var cookieHeader = name+"=";
	var beginPosition = cookieStr.indexOf(cookieHeader);
	
	var endPosition = cookieStr.indexOf (";", beginPosition);
	if (endPosition == -1){
		endPosition = document.cookie.length;
	}
	
	if (beginPosition != -1){//cookie已经设置值，应该 不显示提示框　　　　
		return cookieStr.substring(beginPosition + cookieHeader.length,endPosition);
	}else{//cookie没有设置值应该显示提示框
		return null;
	}
}

function isInt(value){	
    var strCheck=/^[1-9]\d*$/;
    if (!strCheck.exec(value)){
       return false;
    }
    return true;
}

//判断url
function isUrl(value){
	var strCheck=/http:\/\/.+/;
    if (!strCheck.exec(value)){
       return false;
    }
	return true;
}

function isShowPageInt(text){
	if(text.value!=''){
		if(!isInt(text.value)){
			text.value="";
			text.focus();
		}
	}
}

//默认自动选择第一个树
function selectTreeFirstPoint(){
	if(tree!=null&&tree!='undefined'){
		var first = tree.getFirstChild();
		if(first!=null&&first!='undefined'){
			selectTree(first.id);
		}
	}
}

function turn(pageNumber){ 
	$("[name=pageNumber]").val(pageNumber);
	search();
}

//增加隔行显示的效果
function addTabListColourEffect(){
	$('#tableList tr:even').each(function(){
		$(this).removeClass();
		//$(this).addClass("onmuose01");
	});
	//鼠标滑过变色效果
	$("#tableList tr").hover(
		function () {
			if($(this).attr("class") == "hovcolor"){
				$(this).attr("class","onmuose01");
			}else{
				$(this).attr("class","onmuose02");
			}
		},
		function () {
			if($(this).attr("class") == "onmuose01"){
				$(this).attr("class","hovcolor");
			}else{
				$(this).removeClass();
			}
		}
	);
	
	$("#queryForm").keypress(function(e){
		if (e.keyCode == 13) {
			return false;
		}
	});
}

//先删除css样式，再增加隔行显示
function removeDataTablesAddTabListColourEffect(){
	$(".sorting_2").removeClass("sorting_2");
	addTabListColourEffect();
}

function addTabListNoTitleColourEffect(){
	$('#tabList').children().filter(':odd').each(function(){
		$(this).attr("id","hovcolor");
	});
}

function initOpetionsByJssj(sel,kssj){
	var currentTime = new Date();
	var currentYear = currentTime.getFullYear();
	sel.append($("<option/>").attr("value","").text("--请选择--"));
	for(var i=currentYear;i>=kssj;i--){
		sel.append(new Option(i,i));
	}
	sel.value = currentYear;
}

//初始化年
function initYear(birthYear,keyValue){
	var currentTime = new Date();
	var currentYear = currentTime.getFullYear();
	if(keyValue){
		birthYear.options.add(new Option("--请选择--",""));
	}
	for(var i=currentYear;i>=1930;i--){
		birthYear.options.add(new Option(i,i));
	}
	birthYear.value = 1980;
}

//初始化月
function initMonth(orgId,keyValue){
	if(keyValue){
		orgId.options.add(new Option("--请选择--",""));
	}
	for(var i=1;i<10;i++){
		orgId.options.add(new Option("0"+i,"0"+i));
	}
	for(var i=10;i<13;i++){
		orgId.options.add(new Option(i,i));
	}
}


//初始化所有小时
function initAllHour(orgId){
	var sel = $(orgId);
	for(var i=0;i<10;i++){
		sel.append($("<option/>").attr("value","0"+i).text("0"+i));
	}
	for(var i=10;i<24;i++){
		sel.append($("<option/>").attr("value",i).text(i));
	}
}

//初始化有效工作小时
function initHour(orgId){
	var sel = $(orgId);
	for(var i=0;i<10;i++){
		sel.append($("<option/>").attr("value","0"+i).text("0"+i));
	}
	for(var i=10;i<24;i++){
		sel.append($("<option/>").attr("value",i).text(i));
	}
}

//初始化分
function initMinute(orgId){
	var sel = $(orgId);
	sel.append($("<option/>").attr("value","00").text("00"));
	sel.append($("<option/>").attr("value","05").text("05"));
	sel.append($("<option/>").attr("value","10").text("10"));
	sel.append($("<option/>").attr("value","15").text("15"));
	sel.append($("<option/>").attr("value","20").text("20"));
	sel.append($("<option/>").attr("value","25").text("25"));
	sel.append($("<option/>").attr("value","30").text("30"));
	sel.append($("<option/>").attr("value","35").text("35"));
	sel.append($("<option/>").attr("value","40").text("40"));
	sel.append($("<option/>").attr("value","45").text("45"));
	sel.append($("<option/>").attr("value","50").text("50"));
	sel.append($("<option/>").attr("value","55").text("55"));
	/*for(var i=0;i<10;i++){
		sel.append($("<option/>").attr("value","0"+i).text("0"+i));
	}
	for(var i=10;i<60;i++){
		sel.append($("<option/>").attr("value",i).text(i));
	}*/
}

function changeTopMenu(topMenuTitle){
	$('#nav2 a').each(function(){
		if($(this).html()==topMenuTitle){
			$(this).attr('id','nav_hover');
		}
	});
}

function changeLiftMenu(ctx){
	var url = location.href;
	if(ctx.length>0){
		url = url.substring(url.lastIndexOf(ctx)+ctx.length+1);
	}
	url = url.substring(0,url.lastIndexOf("."));
	$('#leftMenuId a').each(function(){
		if(this.href.indexOf(url)>-1){
			$(this).parent().attr("id","me_n_select");
		}else{
			$(this).parent().removeAttr("id");
		}
	});
}

//加载框显示
function showDevelopIng(){
	if($("#developDiv").length==0){
		$("body").prepend('<div id="developDiv" style="position: absolute;display: none;z-index:999" onclick="$(\'#developDiv\').hide()"><img src="'+stCtx+'/images/develop.jpg" alt="loading..." /></div>');
		$("#developDiv").css("top","50%").css("left","50%");
	}
	$("#developDiv").hide();
	$("#developDiv").show(500);	
}

//判断不等于空
function isNotBlank(str){
	return str!=null && str!="" && str!="null";
}


//12px -> 12
function getPxSize(str){
	return parseInt(str.substr(0,str.indexOf("px")));
}


//选择的选项，点击后都到
//比如：moveMultipleOpts($('#src'),$('#target'));
function moveMultipleOpts(src,target){
	src.find("option:selected").each(function() {
     target.append($("<option/>").attr("value",this.value).text(this.text)); 
     $(this).remove();
  });	
}

//所有的目标选项都到目标
//moveAllMultipleOpts($('#src'),$('#target'));
function moveAllMultipleOpts(src,target){
	src.find("option").each(function() {
     target.append($("<option/>").attr("value",this.value).text(this.text)); 
     $(this).remove();
  });	
}


function getNotNull(str){
	return (str == null || str == "null" || (str == "" && typeof(str) == 'string') ||typeof(str) == "undefined") ? "&nbsp;" : str;
}


function noDataDivShow(){
	if($("#noDataDiv").length==0){
		$("body").prepend('<div id="noDataDiv" style="position: absolute;display: none;z-index:999;">暂无数据</div>');
		$("#noDataDiv").css("top","50%").css("left","50%");
	}
	$("#noDataDiv").show();	
}

function noDataDivHide(){
	$("#noDataDiv").hide();
}


/////////////////////////分页有关的

//获取分页数据
//formId 查询条件的form id
//templateId 采用的模板信息
//colName 需要用到什么字段
//tableId 最终要放的到得地址id
//endCallback 截至完的回调函数
//pageId  没用

function getQueryForPageData(formId,templateId,colName,tableId,pageId,cellCallback,endCallback){
	if(!isNotBlank(formId)){
		formId = "#queryForm";
	}
	if(!isNotBlank(templateId)){
		templateId = "#listCellTemplate";
	}
	if(!isNotBlank(tableId)){
		tableId = "#tableList";
	}
	loaderDiveShow();
	noDataDivHide();
	$(formId).ajaxSubmit({
		dataType:"json",
		success:function(data){
			loaderDiveHide();
			var list = data.page.list;
			if(list && list.length>0){
				var template = $(templateId).html();
				var cell = null;
				var result = "";
				var pageNumber = data.page.pageNumber;
				if(!isNotBlank(pageNumber)){
					pageNumber = 1;
				}
				if(pageNumber < 1){
					pageNumber = 1;
				}
				pageNumber--;
				var pageSize = data.page.pageSize;
				var seq = pageNumber*pageSize+1;
				
				for(var i=0;i<list.length;i++){
					cell = template;
					if(jQuery.isFunction(cellCallback)){
						cell = cellCallback(list[i],cell);
					}
					for(var j=0;j<colName.length;j++){
						//var p in colName
						cell = cell.replace(new RegExp("#"+colName[j]+"#", 'g'),getNotNull(list[i][colName[j]]));
						//cell = cell.split("#"+colName[p]+"#").join(getNotNull(list[i][colName[p]]));
					}
					cell = cell.replace(new RegExp("#_seq#", 'g'),seq+i);
					result += cell;
				}
				$(tableId).html(result);
			}else{
				$(tableId).html("");
				noDataDivShow();
			}
			setQueryForPageHtml(data.page);
			if(jQuery.isFunction(endCallback)){
				endCallback();
			}
	    }
	});	
}

//
function draw_html_by_template(list,template,tableId,colName,cellCallback){
	var result = "";
	for(var i=0;i<list.length;i++){
		cell = template;
		if(jQuery.isFunction(cellCallback)){
			cell = cellCallback(list[i],cell);
		}
		for(var p in colName){
			cell = cell.replace(new RegExp("#"+colName[p]+"#", 'g'),getNotNull(list[i][colName[p]]));
		}
		result += cell;
	}
	$(tableId).html(result);
}

//设置分页的内容
function setQueryForPageHtml(data){
	$("#pageNumber").val(data.pageNumber);
	$("#totalPage").val(data.totalPage);
	$("#totalRow").val(data.totalRow);
	$("#pageShowId").html(data.pageNumber+"/"+data.totalPage+"页");
}

function setPageNumber(t){
	var p = 1;
	var currPage = parseInt($("#pageNumber").val());//当前页
	var totalPage = $("#totalPage").val();
	
	if("first" == t){//首页
		p = 1;
	}else if("previou" == t){//上一页
		p = currPage-1;
	}else if("next" == t){//下一页
		p = currPage+1;
	}else if("last" == t){//最后一页
		p = $("#totalPage").val();
	}else{
		p = parseInt(t)-1;
	}
	if(p<1){
		p = 1;
	}else if(p>totalPage){
		p = totalPage;
	}
	$("#pageNumber").val(p);
	search();
}



//用于判断单选或多选
function nameChecked(name){
	var list = document.getElementsByName(name);
	if(list!=null && list.length>0){
		var size = list.length;
		for(var i=0;i<size;i++){
			if(list[i].checked){
				return true;
			}
		}
	}
	return false;
}


/**
 * 增加数字的选项
 * @param orgId
 * @param len
 */
function addOptByInt(orgId,len){
	var sel = $(orgId);
	sel.empty();
	for(var i=1;i<len;i++){
		sel.append($("<option/>").attr("value",""+i).text(""+i));		
	}
}