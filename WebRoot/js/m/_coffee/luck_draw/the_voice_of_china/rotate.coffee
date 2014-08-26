# luck-draw
# the voice of china


# ����CSS3 transform-rotate
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

# ���齱�������������ȡPlayCountʱ��count������������
checkPlayCount = (count) ->
    $("#play_count").val(count) if count?
    return $("#play_count").val()|0

# ���齱�������������ȡPlayCountʱ��count������������
checkLuckUrl = (url) ->
    $("#lucky_url").val(url) if url? and _isURL?(url)
    return $("#lucky_url").val()

# ����Android�ӿ�
goToTVMall = (url)->
    _url = if url then url else checkLuckUrl()
    window.Zhiping?.goToTaoBao?(_url) if _url isnt ""

# ��ȡudid��Ĭ����ҳ�����ʱ��ʼ�����룬��������ڣ���ͨ��Android�ӿڵ�ȡ
getUDID = () ->
    return if $("#udid").val()? then $("#udid").val() else window.Zhiping?.get_udid?()

# ��ȡudid��Ĭ����ҳ�����ʱ��ʼ�����룬��������ڣ���ͨ��Android�ӿڵ�ȡ
getPlayResultID = () ->
    return if $("#get_play_result_id").val()? then $("#get_play_result_id").val() else null

# ���˳齱���
filterPlayResult = (position, isluck) ->
    # �������ݣ����ߺ��ɺ�̨��ȡ
    # test
    # testdata = [
    #     {
    #         "description": "iPhone 6",
    #         "position": 0
    #     },
    #     {
    #         # �������
    #         "description": "\u5355\u53CD\u76F8\u673A",
    #         "position": 1
    #     },
    #     {
    #         # ���������Ʒ
    #         "description": "\u8FDB\u53E3\u6E05\u6D01\u7528\u54C1",
    #         "position": 2
    #     },
    #     {
    #         # лл����
    #         "description": "\u8C22\u8C22\u53C2\u4E0E",
    #         "position": 3
    #     },
    #     {
    #         # ������
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

# ��ʼ��״̬
# ��¼�Ƿ������г齱ת�̣���ֹͬһʱ��ζ�β���
isPlay = false
# ��¼ʣ��齱����
playCount = checkPlayCount()
# ��ǰRotateת���ĽǶ�
totalDeg = 0
# �Ƿ���յ����������ص����ݣ��� null ΪĬ��ֵ 
receiveResult = null

# ���� receiveResult
resetReceiveResult = ()->
    receiveResult = null

# ���� receiveResult
setReceiveResult = (position, isluck)->
    resetReceiveResult()
    receiveResult = filterPlayResult(position, isluck)

# ����dialog��button
resetDialogButton = (addClassName, callback)->
    $luckDialogButtonItem.removeClass("#{rvClass}") for rvClass in ["button-award","button-ok","button-draw"]
    $luckDialogButtonItem.addClass("#{addClassName}")
    LuckDialogView.onSelect = callback

# չʾ����
showDialog = (result)->
    $luckOverlay.addClass('fn-show')
    if result["isluck"] is true
        # �н���
        $luckDialogTitle.html("\u4E2D\u5956\u4E86")
        # ��ϲ�����xxxx
        $luckDialogText.html("\u606D\u559C\u60A8\u83B7\u5F97<span class=\"mark\">#{result["description"]}</span>")

        # ���ð�ť �����콱
        resetDialogButton "button-award", ->
            # �콱
            # debug
            console.log "<showDialog>:\nLUCK, reward To TVMall !!!" if debug?
            goToTVMall()

    else
        # ��ϧ�� 
        $luckDialogTitle.html("\u53EF\u60DC\u4E86")
        # ���ź��󽱲������
        $luckDialogText.html("\u771F\u9057\u61BE\u5927\u5956\u64E6\u80A9\u800C\u8FC7")

        if playCount is 0
            # ���ð�ť ֪����
            resetDialogButton "button-ok", ->
                # debug
                console.log "<showDialog>:\nGame over !!!" if debug?
                hideDialog()
                LuckDrawButtonView.active()
        else
            # ���ð�ť �����齱
            resetDialogButton "button-draw", ->
                # debug
                console.log "<showDialog>:\nPlay again !!!" if debug?
                hideDialog()
                LuckDrawButtonView.active()


    # ������N�γ齱����
    $luckDialogCount.html("#{playCount}")
    # ��ʾ����
    $luckDialog.addClass('fn-show')


# ���ص���
hideDialog = ()->
    $luckOverlay.removeClass('fn-show')
    $luckDialog.removeClass('fn-show')

# ����
# 1�� ÿ10msִ��һ��setDegree()��
# 2�� ��ʼ�� �������٣�����step(����)���ն�λ��15��/�Σ�
# 3�� ��step(����)���ն�λ��15��/�� �Ժ󣬿�ʼ������ת��
# 4�� �����˶�ʱ���ж� receiveResult �Ƿ���ڣ���,�����������ת��ÿ�� currentDeg = currentDeg%360
# 5�� �� receiveResult ���ڣ�ȡ�� currentDeg = currentDeg%360 ִ�� currentDeg%720==0 ��ȡִ�н����
# 6�� ����ʱ�� �������٣���ת�� endDeg��
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

    # ���ص��㼰����
    hideDialog()
    # ���ó齱���
    resetReceiveResult()
    # ����isPlayΪtrue����ֹ�ظ������齱
    isPlay = true

    currentDeg = totalDeg%360
    currentSpeed = 0

    # ����ó� (0+1+2+3...+n)%(360/6)==0
    # ��СnΪ15��(0+1+2+3...+15)=120
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

                # ����ending�ٽ��
                if currentDeg%endingCriticalDeg is 0
                    _isEnding = true
                else
                    currentDeg += currentSpeed
        # ending
        else if _isEnding is true
            currentDeg += currentSpeed
            currentSpeed--

        setDegree($luckDrawWheel, currentDeg)
        
        # ת������
        if _isEnding is true and currentSpeed is 0
            clearTimeout infiniteRotating
            infiniteRotating = null
            # end callback
            # ����ȫ�ֱ��� totalDeg
            totalDeg = currentDeg%360
            # չʾ����
            showDialog(receiveResult)
            # ����������
            LuckDialogView.active()
            # ����isPlay
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
                    # ����receiveResult ȫ�ֱ���
                    setReceiveResult data["data"]["position"], data["data"]["isluck"]
                    # ���ó齱����
                    playCount = checkPlayCount(data["data"]["lucky_surplus_count"])
                    # ���ó齱��ַ
                    checkLuckUrl data["data"]["url"]
                    # ����ҳ����Ϣ
                    resetLuckDrawInfo()

            error: (xhr, errorType, error)->
                if ajaxLimit > 0
                    getPlayResult()
                # else
                #     # Ĭ��û�н�
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

# ����ҳ����ʾ��Ϣ
resetLuckDrawInfo = ()->
    if checkLuckUrl()
        # ���н�Ʒ��δ��ȡ
        $luckDrawTitleCnt.html("\u60A8\u6709\u5956\u54C1\u5C1A\u672A\u9886\u53D6")
        # �����ٻ����齱��
        $luckDrawTitleTip.html("\u9886\u5B8C\u518D\u56DE\u6765\u62BD\u5956\u5427")
        # ���ô�ťΪ"�����콱"
        resetLuckDrawButton "button-award", ->
            # debug
            console.log "<resetLuckDrawButton>:\nLUCK, reward To TVMall !!!" if debug?
            goToTVMall()

        return
    else if playCount > 0
        # ������<em class="mark">5</em>�γ齱����
        $luckDrawTitleCnt.html("\u60A8\u8FD8\u6709<em class=\"mark\">#{playCount}</em>\u6B21\u62BD\u5956\u673A\u4F1A")
        # ��OK�������齱
        $luckDrawTitleTip.html("\u6309OK\u952E\u7ACB\u5373\u62BD\u5956")
        # ���ô�ťΪ"�����齱"
        resetLuckDrawButton "button-draw", ->
            # debug
            console.log "<resetLuckDrawButton>:\nPlay again !!!" if debug?
            playLuckDraw()

        return
    else if playCount is 0
        # ����<span class="mark">���й���������</span>����
        $luckDrawTitleCnt.html("\u53C2\u4E0E<span class=\"mark\">\u300A\u4E2D\u56FD\u597D\u58F0\u97F3\u300B</span>\u4E92\u52A8")
        # �����齱�ʸ�
        $luckDrawTitleTip.html("\u7B54\u9898\u83B7\u62BD\u5956\u8D44\u683C")
        # ���ô�ťΪ"���뻥��"
        resetLuckDrawButton "button-over", ->
            # debug
            console.log "<resetLuckDrawButton>:\nGame over !!!" if debug?
            null

# ����ҳ���ť
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
    # ��ʼ��ҳ��չʾ��Ϣ�Լ���ťactive
    resetLuckDrawInfo()
    LuckDrawButtonView.active()


