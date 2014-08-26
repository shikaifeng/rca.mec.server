# luck-draw
# the voice of china


# 设置CSS3 transform-rotate
setDegree = ($obj, deg)->
    if deg?
        $obj?.css({
            # /* Safari and Chrome */
            "-webkit-transform": "rotate(#{deg}deg)",
            # /* Firefox */
            "-moz-transform": "rotate(#{deg}deg)",
            # /* IE 9 */
            "-ms-transform": "rotate(#{deg}deg)",
            # /* Opera */
            "-o-transform": "rotate(#{deg}deg)",
            "transform": "rotate(#{deg}deg)"
        })

# 检查抽奖机会次数，仅获取PlayCount时，count参数不用设置
checkPlayCount = (count) ->
    $("#play_count").val(count) if count?
    return $("#play_count").val()|0

# 检查抽奖机会次数，仅获取PlayCount时，count参数不用设置
checkLuckUrl = (url) ->
    $("#lucky_url").val(url) if url? and _isURL?(url)
    return $("#lucky_url").val()

# 调用Android接口
goToTVMall = (url)->
    _url = if url then url else checkLuckUrl()
    window.Zhiping?.goToTaoBao?(_url) if _url isnt ""

# 获取udid，默认由页面加载时初始化载入，如果不存在，则通过Android接口调取
getUDID = () ->
    return if $("#udid").val()? then $("#udid").val() else window.Zhiping?.get_udid?()

# 获取udid，默认由页面加载时初始化载入，如果不存在，则通过Android接口调取
getPlayResultID = () ->
    return if $("#get_play_result_id").val()? then $("#get_play_result_id").val() else null

# 过滤抽奖结果
filterPlayResult = (position, isluck) ->
    # 测试数据，上线后由后台获取
    # test
    # testdata = [
    #     {
    #         "description": "iPhone 6",
    #         "position": 0
    #     },
    #     {
    #         # 单反相机
    #         "description": "\u5355\u53CD\u76F8\u673A",
    #         "position": 1
    #     },
    #     {
    #         # 进口清洁用品
    #         "description": "\u8FDB\u53E3\u6E05\u6D01\u7528\u54C1",
    #         "position": 2
    #     },
    #     {
    #         # 谢谢参与
    #         "description": "\u8C22\u8C22\u53C2\u4E0E",
    #         "position": 3
    #     },
    #     {
    #         # 吸尘器
    #         "description": "\u5438\u5C18\u5668",
    #         "position": 4
    #     },
    #     {
    #         "description": "iPad 4",
    #         "position": 5
    #     }
    # ]
    retult = item for item in luckList when item["position"] is position
    $.extend retult, {
        "isluck": isluck
    }
    return retult

# DOM Zepto
$luckDrawTitleCnt = $('#luck_draw_title_content')
$luckDrawTitleTip = $('#luck_draw_title_tip')
$luckDrawButtonItem = $('#luck_draw_button_item_1')

$luckDrawWheel = $('#luck_draw_wheel')

$luckOverlay = $('#luck_overlay')

$luckDialog = $('#luck_dialog')
$luckDialogTitle = $('#luck_dialog_title')
$luckDialogText = $('#luck_dialog_text')
$luckDialogCount = $('#luck_dialog_count')
$luckDialogButtonItem = $('#luck_dialog_button_item_1')

# 初始化状态
# 记录是否在运行抽奖转盘，防止同一时间段多次操作
isPlay = false
# 记录剩余抽奖次数
playCount = checkPlayCount()
# 当前Rotate转动的角度
totalDeg = 0
# 是否接收到服务器返回的数据，以 null 为默认值 
receiveResult = null

# 设置 receiveResult
resetReceiveResult = ()->
    receiveResult = null

# 设置 receiveResult
setReceiveResult = (position, isluck)->
    resetReceiveResult()
    receiveResult = filterPlayResult(position, isluck)

# 重置dialog的button
resetDialogButton = (addClassName, callback)->
    $luckDialogButtonItem.removeClass("#{rvClass}") for rvClass in ["button-award","button-ok","button-draw"]
    $luckDialogButtonItem.addClass("#{addClassName}")
    LuckDialogView.onSelect = callback

# 展示弹层
showDialog = (result)->
    $luckOverlay.addClass('fn-show')
    if result["isluck"] is true
        # 中奖了
        $luckDialogTitle.html("\u4E2D\u5956\u4E86")
        # 恭喜您获得xxxx
        $luckDialogText.html("\u606D\u559C\u60A8\u83B7\u5F97<span class=\"mark\">#{result["description"]}</span>")

        # 设置按钮 立即领奖
        resetDialogButton "button-award", ->
            # 领奖
            # debug
            console.log "<showDialog>:\nLUCK, reward To TVMall !!!" if debug?
            goToTVMall()

    else
        # 可惜了 
        $luckDialogTitle.html("\u53EF\u60DC\u4E86")
        # 真遗憾大奖擦肩而过
        $luckDialogText.html("\u771F\u9057\u61BE\u5927\u5956\u64E6\u80A9\u800C\u8FC7")

        if playCount is 0
            # 设置按钮 知道了
            resetDialogButton "button-ok", ->
                # debug
                console.log "<showDialog>:\nGame over !!!" if debug?
                hideDialog()
                LuckDrawButtonView.active()
        else
            # 设置按钮 继续抽奖
            resetDialogButton "button-draw", ->
                # debug
                console.log "<showDialog>:\nPlay again !!!" if debug?
                hideDialog()
                LuckDrawButtonView.active()


    # 您还有N次抽奖机会
    $luckDialogCount.html("#{playCount}")
    # 显示弹层
    $luckDialog.addClass('fn-show')


# 隐藏弹层
hideDialog = ()->
    $luckOverlay.removeClass('fn-show')
    $luckDialog.removeClass('fn-show')

# 规则
# 1、 每10ms执行一次setDegree()；
# 2、 起始做 缓动加速，加速step(步长)最终定位在15°/次；
# 3、 当step(步长)最终定位在15°/次 以后，开始匀速旋转；
# 4、 匀速运动时，判断 receiveResult 是否存在，否,则继续匀速旋转，每次 currentDeg = currentDeg%360
# 5、 当 receiveResult 存在，取消 currentDeg = currentDeg%360 执行 currentDeg%720==0 获取执行结果；
# 6、 结束时做 缓动减速，旋转至 endDeg；
# 
playLuckDraw = ()->
    if isPlay is true
        # debug
        console.log "<playLuckDraw>:\nplayLuckDraw is doing!!!" if debug?
        return false
    if playCount is 0
        # debug
        console.log "<playLuckDraw>:\nplayCount is 0!\nGAME OVER!!!" if debug?
        return false

    # 隐藏弹层及遮罩
    hideDialog()
    # 重置抽奖结果
    resetReceiveResult()
    # 设置isPlay为true，防止重复触发抽奖
    isPlay = true

    currentDeg = totalDeg%360
    currentSpeed = 0

    # 计算得出 (0+1+2+3...+n)%(360/6)==0
    # 最小n为15，(0+1+2+3...+15)=120
    pendingSpeed = 15
    
    endDeg = null
    endingCriticalDeg = null

    infiniteRotating = null

    infiniteRotate = (_isEnding=false)->
        # begining
        if currentSpeed < pendingSpeed and _isEnding is false
            currentSpeed++
            currentDeg = currentDeg%360 + currentSpeed
        # pending
        else if currentSpeed is pendingSpeed and _isEnding is false
            if receiveResult is null
                currentDeg = currentDeg%360 + currentSpeed
            else
                if endingCriticalDeg is null # and endDeg is null
                    endDeg = 1440 + (6-receiveResult["position"])*60
                    endingCriticalDeg = endDeg - 120

                # 进入ending临界点
                if currentDeg%endingCriticalDeg is 0
                    _isEnding = true
                else
                    currentDeg += currentSpeed
        # ending
        else if _isEnding is true
            currentDeg += currentSpeed
            currentSpeed--

        setDegree($luckDrawWheel, currentDeg)
        
        # 转动结束
        if _isEnding is true and currentSpeed is 0
            clearTimeout infiniteRotating
            infiniteRotating = null
            # end callback
            # 重置全局变量 totalDeg
            totalDeg = currentDeg%360
            # 展示弹层
            showDialog(receiveResult)
            # 激活弹层控制器
            LuckDialogView.active()
            # 重置isPlay
            isPlay = false

        else
            infiniteRotating = setTimeout ->
                # arguments.callee()
                infiniteRotate(_isEnding)
            , 10
        infiniteRotating
    # <= infiniteRotate
    infiniteRotate()

    # ajax get luck draw result
    ajaxLimit = 3
    getPlayResult = () ->
        $.ajax {
            type: 'GET',
            url: '/api/v1/draw_result_register',
            data:
                id: getPlayResultID() #1
                udid:  getUDID() #3307
            dataType: 'json'
            success: (data, status, xhr)->
                # data:{
                #     data:{
                #         lucky_surplus_count: 0,
                #         position: 1,
                #         isluck: false,
                #         url: ""
                #     },
                #     msg: null,
                #     page: null,
                #     status: -1 || 0(true)
                # }

                # test
                # data = {
                #     data:{
                #         lucky_surplus_count: checkPlayCount()-1,
                #         position: 3,
                #         isluck: false,
                #         url: ""
                #     },
                #     msg: null,
                #     page: null,
                #     status: 0
                # }

                if data["status"] is 0
                    # 设置receiveResult 全局变量
                    setReceiveResult data["data"]["position"], data["data"]["isluck"]
                    # 重置抽奖次数
                    playCount = checkPlayCount(data["data"]["lucky_surplus_count"])
                    # 重置抽奖地址
                    checkLuckUrl data["data"]["url"]
                    # 更新页面信息
                    resetLuckDrawInfo()

            error: (xhr, errorType, error)->
                if ajaxLimit > 0
                    getPlayResult()
                # else
                #     # 默认没中奖
                #     setReceiveResult -1
            complete: (xhr, status)->
                ajaxLimit--
        }
    # <- getPlayResult
    getPlayResult()
    
# <<-- playLuckDraw END 

# NEW View
# Dialog View
LuckDialogView = new View
    id: 'luckDialogButton'
    el: "#luck_dialog"
    itemidPrefix: 'luck_dialog_button_item_'
    focusClassName: 'button-active'
    up: ->
        null
    down:  ->
        null
    left: ->
        null
    right:  ->
        null
    onSelect: (item)->
        null

# 重置页面显示信息
resetLuckDrawInfo = ()->
    if checkLuckUrl()
        # 您有奖品尚未领取
        $luckDrawTitleCnt.html("\u60A8\u6709\u5956\u54C1\u5C1A\u672A\u9886\u53D6")
        # 领完再回来抽奖吧
        $luckDrawTitleTip.html("\u9886\u5B8C\u518D\u56DE\u6765\u62BD\u5956\u5427")
        # 重置大按钮为"立即领奖"
        resetLuckDrawButton "button-award", ->
            # debug
            console.log "<resetLuckDrawButton>:\nLUCK, reward To TVMall !!!" if debug?
            goToTVMall()

        return
    else if playCount > 0
        # 您还有<em class="mark">5</em>次抽奖机会
        $luckDrawTitleCnt.html("\u60A8\u8FD8\u6709<em class=\"mark\">#{playCount}</em>\u6B21\u62BD\u5956\u673A\u4F1A")
        # 按OK键立即抽奖
        $luckDrawTitleTip.html("\u6309OK\u952E\u7ACB\u5373\u62BD\u5956")
        # 重置大按钮为"立即抽奖"
        resetLuckDrawButton "button-draw", ->
            # debug
            console.log "<resetLuckDrawButton>:\nPlay again !!!" if debug?
            playLuckDraw()

        return
    else if playCount is 0
        # 参与<span class="mark">《中国好声音》</span>互动
        $luckDrawTitleCnt.html("\u53C2\u4E0E<span class=\"mark\">\u300A\u4E2D\u56FD\u597D\u58F0\u97F3\u300B</span>\u4E92\u52A8")
        # 答题获抽奖资格
        $luckDrawTitleTip.html("\u7B54\u9898\u83B7\u62BD\u5956\u8D44\u683C")
        # 重置大按钮为"参与互动"
        resetLuckDrawButton "button-over", ->
            # debug
            console.log "<resetLuckDrawButton>:\nGame over !!!" if debug?
            null

# 重置页面大按钮
resetLuckDrawButton = (addClassName, callback)->
    $luckDrawButtonItem.removeClass("#{rvClass}") for rvClass in ["button-draw","button-award","button-over"]
    $luckDrawButtonItem.addClass("#{addClassName}")

    LuckDrawButtonView.onSelect = callback

# Dialog View
LuckDrawButtonView = new View
    id: 'LuckDrawButton'
    el: "#luck_draw_button"
    itemidPrefix: 'luck_draw_button_item_'
    focusClassName: 'button-active'
    up: ->
        null
    down:  ->
        null
    left: ->
        null
    right:  ->
        null
    onSelect: (item)->
        null

LuckDialogView.enableNavigation()
LuckDrawButtonView.enableNavigation()

# DOM Ready
$ ->
    # 初始化页面展示信息以及大按钮active
    resetLuckDrawInfo()
    LuckDrawButtonView.active()


