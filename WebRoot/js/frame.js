
var $ = jQuery;
var thespeed = 5;
var navIE = document.all && navigator.userAgent.indexOf("Firefox")==-1;
var myspeed=0;
$(function(){
		
		//快捷菜单
		bindQuickMenu();
		
		//左侧菜单开关
		LeftMenuToggle();
		
		//全部功能开关
		//AllMenuToggle();

		//取消菜单链接虚线
		//$(".head").find("a").click(function(){$(this).blur()});
		//$(".menu").find("a").click(function(){$(this).blur()});
		
	
		
	}).keydown(function(event){//快捷键
		if(event.keyCode ==116 ){
			//url = $("#main").attr("src");
			//main.location.href = url;
			//return false;	
		}
		if(event.keyCode ==27 ){
			$("#qucikmenu").slideToggle("fast")
		}
});
	
function bindQuickMenu(){//快捷菜单
		$("#ac_qucikmenu").bind("mouseenter",function(){
			$("#qucikmenu").slideDown("fast");
		}).dblclick(function(){
			$("#qucikmenu").slideToggle("fast");
		}).bind("mouseleave",function(){
			hidequcikmenu=setTimeout('$("#qucikmenu").slideUp("fast");',700);
			$(this).bind("mouseenter",function(){clearTimeout(hidequcikmenu);});
		});
		$("#qucikmenu").bind("mouseleave",function(){
			hidequcikmenu=setTimeout('$("#qucikmenu").slideUp("fast");',700);
			$(this).bind("mouseenter",function(){clearTimeout(hidequcikmenu);});
		}).find("a").click(function(){
			$(this).blur();
			$("#qucikmenu").slideUp("fast");
			//$("#ac_qucikmenu").text($(this).text());
		});
}
	
function LeftMenuToggle(){//左侧菜单开关
		$("#togglemenu").click(function(){
			if($("body").attr("class")=="showmenu"){
				$("body").attr("class","hidemenu");
				$(this).html("<img src='/images/list_button_r.png' />");
			}else{
				$("body").attr("class","showmenu");
				$(this).html("<img src='/images/list_button.png' />");
			}
		});
	}
	
	

	


