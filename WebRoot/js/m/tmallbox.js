/**
 * Tmallbox js functions
 * @Author    Robin Huang
 * @Version   1.0
 */

var udid = null;
var timer_id = null;
var millisec = 6000;
var mark = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'];
var runtime = null;
var total = 5345000;
var pos = 534000;
var debug = true;
var luckyURL = "";

// Go to URL of LuckyDraw
function gotoLuckydraw(){
  if(luckyURL!==""){
    if(debug && !window.Zhiping){
      window.location.href = luckyURL;
      return;
    }
    window.Zhiping.setRewardUrl(luckyURL);
  }else{
    if(debug && console.log){
      console.log("<gotoLuckydraw>: luckyURL is "+luckyURL);
    }
  }
}

/**
 * Query feeds by backend interface
 */
function query_feeds(params) {
  var url = '/api/v1/get_feeds';
  $.getJSON(url, params, function(obj) {
    var status = obj["status"],
        msg = obj["msg"],
        data = obj["data"],
        lucky = data["lucky"] || null;
    
    if (status == 0) {
      if (lucky == null){
        refresh_weibo_box(data["weibo"]);
        refresh_feed_box(data["feeds"]);
        refresh_question_box(data["question"]);
      }else{
        showLucky(lucky);
      }
    }
  });
}

/**
 * Switch the lucky, hide feed
 */
function showLucky(lucky){
  // debug
  if(debug && console.log){
    console.log("<showLucky> lucky is :"+lucky);
  }

  if(wikiFeed){
    wikiFeed.destroy();
    wikiFeed = null;
  }
  if(weiboFeed){
    weiboFeed.destroy();
    weiboFeed = null;
  }
  if(timer_id!==null){
    clearInterval(timer_id);
    timer_id = null;
  }

  luckyURL = lucky["url"];
  if(lucky["lucky_surplus_count"]==0){
    $("#loki_lucky_extra").css("display","none");
  }else{
    $("#lucky_count").html(lucky["lucky_surplus_count"]);
  }

  // 延时1s执行切换，确保feed的动画效果已经执行完成;
  setTimeout(function(){
    $("#loki_feed").addClass("feed-left-out");
    $("#loki_lucky").addClass("feed-right-in");
  },1000)
}

/**
 * Reload query action
 */
 function reload() {
 	// Real environment
 	if (debug) {
 		if (pos > total) {
 			pos = 0;
 		}
 		else {
 			pos += 10000;
		}
 		var params = {
      type: 'nolive',
      reference_id: 113391,
      start_time: pos
    };
 	}
 	else {
	 	try {
	 		var acr_result = get_acr_result();
	 		var params = eval('(' + acr_result + ')');
	 	}
	 	catch (e) {
	 		// do nothing
	 	}
	 }
 	params.udid = udid;
 	query_feeds(params);
 }


/**
 * Get udid by app interface
 */
function get_udid() {
  udid = window.Zhiping.get_udid();
  return udid;
}

/**
 * Get acr result by app interface
 */
function get_acr_result() {
  var obj = window.Zhiping.get_acr_result();
  return obj;
}

/**
 * Truncate the text
 */
 function truncate(cls) {
 	var len = 40;
 	var obj = $('.'+cls);
 	if (obj) {
 		var text = obj.text();
 		if (text.length > len) {
 			text = text.substring(0, len);
 			text = text.replace(/\w+$/, '');
 			text += '...';
 			obj.text(text);
 		}
 	}
 }


/**
 * Refresh feed box
 */
// NEW FeedAnimate
var wikiFeed = new FeedAnimate('feed_wiki');
function refresh_feed_box(obj) {
  if (obj) {
    // clean trashQueue when trashQueue.length > 10
    wikiFeed.cleanTrash();
    if (obj.person) {
      if($('#wiki_person_' + obj['person']['id'] + '_'  + obj['person']['updated_at']).length === 0){
        wikiFeed.append([
          '<article class="wiki-person" id="wiki_person_' + obj['person']['id'] + '_'  + obj['person']['updated_at'] + '">',
            '<div class="person-item-wrap">',
              '<div class="person-item-avatar">',
                '<div class="person-item-avatar-left" style="background-image:url(\'' + obj['person']['avatar'] + '\');">',
                  '<span class="person-item-avatar-left-mask"><!-- left-mask --></span>',
                '</div>',
                '<div class="person-item-avatar-right" style="background-image:url(\'' + obj['person']['avatar'] + '\');"><!-- right --></div>',
              '</div>',
              '<div class="person-item-avatar-mask"><!-- mask --></div>',
              '<div class="person-item-cnt">',
                '<h2 class="person-name">' + obj['person']['name'] + '</h2>',
                '<p class="person-tip">代表作</p>',
                '<p class="person-description">' + obj['person']['description'] + '</p>',
              '</div>',
            '</div>',
          '</article>'
        ].join(''));
      }
      wikiFeed.pushAnimateQueue('wiki_person_' + obj['person']['id'] + '_'  + obj['person']['updated_at']);
    }
    if (obj.baike) {
      if($('#wiki_baike_' + obj['baike']['id'] + '_'  + obj['baike']['updated_at']).length === 0){
          if(!obj['baike']['cover']){
            wikiFeed.append([
              '<article class="wiki-baike" id="wiki_baike_' + obj['baike']['id'] + '_'  + obj['baike']['updated_at'] + '">',
                '<div class="baike-item-wrap">',
                  '<div class="baike-item-cnt">',
                    '<h2 class="baike-title-nocover">' + obj['baike']['title'] + '</h2>',
                    '<p class="baike-summary-nocover">' + obj['baike']['summary'] + '</p>',
                  '</div>',
                '</div>',
              '</article>'
            ].join(''));
          }else{
            // person & sight
            wikiFeed.append([
              '<article class="wiki-baike" id="wiki_baike_' + obj['baike']['id'] + '_'  + obj['baike']['updated_at'] + '">',
                '<div class="baike-item-wrap">',
                  '<div class="baike-item-cover">',
                    '<div class="baike-item-cover-left" style="background-image:url(\'' + obj['baike']['cover'] + '\');">',
                      '<span class="baike-item-cover-left-mask"><!-- left-mask --></span>',
                    '</div>',
                    '<div class="baike-item-cover-right" style="background-image:url(\'' + obj['baike']['cover'] + '\');"><!-- right --></div>',
                  '</div>',
                  '<div class="baike-item-cover-mask"><!-- mask --></div>',
                  '<div class="baike-item-cnt">',
                    '<h2 class="baike-title">' + obj['baike']['title'] + '</h2>',
                    '<p class="baike-summary">' + obj['baike']['summary'] + '</p>',
                  '</div>',
                '</div>',
              '</article>'
            ].join(''));
          }
      }
      wikiFeed.pushAnimateQueue('wiki_baike_' + obj['baike']['id'] + '_'  + obj['baike']['updated_at']);
    }
    if (obj.music) {
      if($('#wiki_music_' + obj['music']['id'] + '_'  + obj['music']['updated_at']).length === 0){
        wikiFeed.append([
          '<article class="wiki-music" id="wiki_music_' + obj['music']['id'] + '_'  + obj['music']['updated_at'] + '">',
            '<div class="music-item-wrap">',
              '<div class="music-item-cnt">',
                '<h3 class="music-title">' + obj['music']['title'] + '</h3>',
                '<p class="music-tag">' + obj['music']['tag'] + '</p>',
                '<p class="music-singer">' + obj['music']['singer'] + '</p>',
              '</div>',
              '<div class="music-item-cover">',
                '<img class="music-item-cover-img" src="' + obj['music']['cover'] + '" alt="'+ obj['music']['title'] +'" />',
              '</div>',
            '</div>',
          '</article>'
        ].join(''));
      }
      wikiFeed.pushAnimateQueue('wiki_music_' + obj['music']['id'] + '_'  + obj['music']['updated_at']);
    }
    wikiFeed.play();
  }
}

/**
 * Refresh music box
 */
// NEW FeedAnimate
var weiboFeed = new FeedAnimate('feed_weibo', {
  'classhide': 'weibo-hide'
});

function refresh_weibo_box(obj) {
  if (obj) {
    if($('#weibo_' + obj['id'] + '_'  + obj['updated_at']).length === 0){
      weiboFeed.append([
        '<article class="weibo-item" id="weibo_' + obj['id'] + '_'  + obj['updated_at'] + '">',
          '<aside class="weibo-head">',
            '<span class="weibo-avatar">',
              '<img class="weibo-avatar-img" src="' + obj['sender_avatar'] + '" alt="' + obj['sender_name'] + '" />',
            '</span>',
            '<span class="weibo-name">' + obj['sender_name'] + '</span>',
          '</aside>',
          '<p class="weibo-cnt">' + obj['content'] + '</p>',
        '</article>'
      ].join(''));
    }
    weiboFeed.pushAnimateQueue('weibo_' + obj['id'] + '_'  + obj['updated_at']);
  }
  weiboFeed.play();
}

/**
 * Refresh question box
 */
var ian_id_current = 'ian_init',
    ian_id_prev = '',
    ian_answer_prefix = '',
    ian_answer_queue = [],
    ian_answer_index = 0,
    ian_lock = 'ian-lock';
function refresh_question_box(obj) {
  if (obj) {
    if(($('#ian_' + obj['id']).length !== 0) && ($('#ian_' + obj['id'] + '_forbidden').length === 0) && ((obj['status'] === 6) || (obj['status'] === 4))){
      // reset ian
      ian_answer_queue = [];
      ian_answer_index = 0;
      ian_id_prev = ian_id_current;
      ian_id_current = 'ian_' + obj['id'] + '_forbidden';
      ian_answer_prefix = ian_id_current + '_';

      $('#feed_ian').append([
          '<article class="ian-item ian-none ian-hide" id="' + ian_id_current + '">',
            // 很遗憾，您未参加本次竞猜<br />下一题即将开始
            '<p class="none-title">'+ obj['banner_title'] +'</p>',
            '<div class="none-banner"><img src="'+ obj['banner_img_path'] +'" alt="banner" /></div>',
          '</article>'
        ].join(''));

      // animate
      if(ian_id_prev!==''){
        $('#'+ian_id_prev).addClass('feed-left-out');
        setTimeout(function(){
          $('#'+ian_id_prev).remove();
        }, 2000);
      }
      $('#'+ian_id_current).removeClass('ian-hide').addClass('feed-right-in');
      return;

    }else if (($('#ian_' + obj['id']).length === 0) && ($('#ian_' + obj['id'] + '_forbidden').length === 0)) {
      // reset ian
      ian_answer_queue = [];
      ian_answer_index = 0;
      ian_id_prev = ian_id_current;
      ian_id_current = 'ian_' + obj['id'];
      ian_answer_prefix = ian_id_current + '_';

      $('#feed_ian').append(creat_question_item(obj));      
      // animate
      if(ian_id_prev!==''){
        $('#'+ian_id_prev).addClass('feed-left-out');
        setTimeout(function(){
          $('#'+ian_id_prev).remove();
        }, 2000);
      }
      $('#'+ian_id_current).removeClass('ian-hide').addClass('feed-right-in');

      // active option
      $('#'+ian_answer_queue[ian_answer_index]).addClass('active');

    }
    // switch status
    var $answer_wrap = $('#ian_answer_'+obj['id']);

    switch(obj['status']){
      // 公布答案（答题正确）
      case 2:
      // 3：公布答案（答题错误）
      case 3:
        if($('.ian-answer', '#'+ian_id_current).eq(0).hasClass('ian-hide') || !$('.ian-options', '#'+ian_id_current).eq(0).hasClass('ian-hide')){
          $('.answer-title', '#'+ian_id_current).eq(0).html($('#'+ian_answer_queue[ian_answer_index]).html())
          $('.ian-options', '#'+ian_id_current).eq(0).addClass('ian-hide');
          $('.ian-answer', '#'+ian_id_current).eq(0).removeClass('ian-hide');
        }
        $answer_wrap.append(creat_answer_status(obj['status'], obj['user_right_count']));
        $answer_wrap.children('article.status-waiting').eq(0).addClass('feed-left-out');
        $answer_wrap.children('article.status-publish').eq(0).removeClass('ian-hide').addClass('feed-right-in');
        break;
      // // 4：公布答案（未答题) 
      // case 4:
      //   // dosomthing
      //   $('#'+ian_id_current).addClass(ian_lock);
      //   $('#'+ian_answer_queue[ian_answer_index]).removeClass('active');
      //   break;
      // // 5：未答题（可以答题状态）
      // case 5:
      //   break;
      // // 6：未答题（不可以答题）
      // case 6:
      //   $('#'+ian_id_current).addClass(ian_lock);
      //   // $('#'+ian_answer_queue[ian_answer_index]).removeClass('active');
      //   break;
      // 7：已答题（等待答案中）
      case 7:
        $('#'+ian_id_current).addClass(ian_lock);
        $('#'+ian_answer_queue[ian_answer_index]).removeClass('active');
        // record title
        for(var i=0; i<ian_answer_queue.length; i++){
          if(ian_answer_queue[i].indexOf(obj['user_option_id'])!==-1){
            ian_answer_index = i;
            break;
          }
        }
        $('.answer-title', '#'+ian_id_current).eq(0).html($('#'+ian_answer_queue[ian_answer_index]).html())
        // animate
        $('.ian-options', '#'+ian_id_current).eq(0).addClass('ian-hide');
        $('.ian-answer', '#'+ian_id_current).eq(0).removeClass('ian-hide');
        break;
    }
  }
}

function creat_question_item(obj) {
  var options = [];
  for (var i = 0; i < obj['options'].length; i++) {
    options.push([
      '<li id="ian_' + obj['id'] + '_' + obj['options'][i]['id'] + '">',
        '<em>',
          mark[i],
        '</em>',
        obj['options'][i]['title'],
      '</li>'
    ].join(''));
    // change ian_answer_queue
    ian_answer_queue.push('ian_' + obj['id'] + '_' + obj['options'][i]['id']);
  }
  return [
    '<article class="ian-item ian-hide" id="ian_' + obj['id'] + '">',
      '<h3 class="ian-title">' + obj['title'] + '</h3>',
      '<div class="ian-main-wrap">',
        '<ul class="ian-options">',
          options.join(''),
        '</ul>',
        '<div class="ian-answer ian-hide">',
          '<h4 class="answer-label">您选择的答案是：</h4>',
          '<p class="answer-title"></p>',
          '<div class="answer-wrap" id="ian_answer_'+ obj['id'] +'">',
            creat_answer_status(obj['status'], obj['user_right_count']),
          '</div>',
        '</div>',
      '</div>',
    '</article>'
  ].join('');
}


function creat_answer_status(status, user_right_count) {
  var tpl = [];
  switch (status) {
    // undo || init
    case 0:
    case 5:
    case 7:
      tpl.push([
        '<article class="answer-status status-waiting">',
          '<p class="answer-tip">结果马上揭晓</p>',
          '<p>请耐心等待哦~</p>',
        '</article>'
      ].join(''));
      break;
    // success
    case 2:
      tpl.push([
        '<article class="answer-status status-publish ian-hide">',
          '<p class="answer-tip">您真有眼光，答对咯~</p>',
          '<p class="answer-result">您已经答对<i>'+ user_right_count +'</i>道题目</p>',
        '</article>'
      ].join(''));
      break;
    // error
    case 3:
      tpl.push([
        '<article class="answer-status status-publish ian-hide">',
          '<p class="answer-tip">很遗憾，再接再厉哦~</p>',
        '</article>'
      ].join(''));
      break;
    default:
      // alert('Error: answer sratus is unknown!');
      break;
  }
  return tpl.join('');
}

function switch_answer_option(orient, index){
  if(ian_answer_queue.length===0 || $('#'+ian_id_current).hasClass(ian_lock)){
    return false;
  }
  var orient = (orient==='up') ? -1 : (orient==='down') ? 1 : 1,
      prev = ian_answer_index,
      current = ian_answer_index + orient,
      len = ian_answer_queue.length;
  if(!!index || (index === 0)){
    index = index|0;
    prev = index - orient;
    current = index;
  }
  if(orient===1){
    prev = (prev < 0) ? (len -1) : prev;
    current = ((current - len)===0) ? 0 : current;
  }else{
    prev = ((prev - len)===0) ? 0 : prev;
    current = (current < 0) ? (len -1) : current;
  }

  $('#' + ian_answer_queue[prev]).removeClass('active');
  $('#' + ian_answer_queue[current]).addClass('active');

  ian_answer_index = current;
  return current;
}

/**
 * Answer the question
 */
function answer_question(node) {
  if($('#'+ian_id_current).hasClass(ian_lock)){
    alert('答案不能重复选择！');
    return false;
  }

  var url = '/api/v1/question_and_answer';
  var question_id = ian_id_current.replace('ian_','');
  var option_id = ian_answer_queue[ian_answer_index].replace(ian_answer_prefix,'');
  var params = {
    question_id: question_id,
    answer: option_id,
    time: runtime,
    udid: udid
  };
  // lock
  $('#'+ian_id_current).addClass(ian_lock);
  
  // post data
  $.getJSON(url, params, function(obj) {
    var status = obj.status;
    var msg = obj.msg;
    var data = obj.data;
    if (status == 0) {
      // record title
      $('.answer-title', '#'+ian_id_current).html($('#'+ian_answer_queue[ian_answer_index]).html())
      // animate
      $('.ian-options', '#'+ian_id_current).addClass('feed-left-out');
      $('.ian-answer', '#'+ian_id_current).removeClass('ian-hide').addClass('feed-right-in');
    }
  });
}


/**
 * Init while loading
 */
function init() {
  if(debug){
    udid = '3307';
    var params = {
      type: 'nolive',
      reference_id: 112804,
      start_time: pos
    };
  }else{
    try {
      udid = get_udid();
      $('#udid').html(udid);

      var acr_result = get_acr_result();
      var params = eval('(' + acr_result + ')');
    } catch (err) {
      // 
    } 
  }

  params.udid = udid;
  query_feeds(params);

  // add event
  $(document).on('keydown.loki', function(evt){
    evt = (evt) ? evt : ((window.event) ? window.event : ""); //兼容IE和Firefox获得keyBoardEvent对象
    var key = evt.keyCode ? evt.keyCode : evt.which; //兼容IE和Firefox获得keyBoardEvent对象的键值
    switch(key){
      case 38:
      case 19:
        // up
        if(luckyURL==""){
          switch_answer_option('up');
        }
        break;
      case 40:
      case 20:
        // down
        if(luckyURL==""){
          switch_answer_option('down');
        }
        break;
      case 37:
      case 21:
        // left
        break;
      case 39:
      case 22:
        // right
        break;
      case 13:
      case 23:
        // entry
        // 切换遥控器焦点
        if(luckyURL==""){
          answer_question();
        }else{
          gotoLuckydraw();
        }
        break;
    }
  })

}

function start_timer() {
  if(timer_id !== null){
    clearInterval(timer_id);
    timer_id = null
  }

  timer_id = setInterval(function(){
    reload();
  }, millisec);
}

/**
 * Start
 */
$(function() {
  init();
  start_timer();
});
