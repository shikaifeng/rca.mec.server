# 
class FeedAnimate
    constructor: (id, option) ->
        if not id?
            alert('FeedAnimate ERROR: constructor\'s argument "id" is NONE!')
            return false
        _option =
            classin: 'feed-right-in'
            classout: 'feed-left-out'
            classhide: 'wiki-hide'
            totalTime: 6000
        @option = $.extend(_option, option)
        @elid = id
        @$el = $("##{id}")

        @_init()
    _init: ()->
        @appendTpls = []
        @animateQueue = []
        @animateLoop = null
        @animateSpeed = @option['totalTime']
        @trashQueue = []
        ## debug
        # @i = 0
        return this
    append: (tpl) ->
        @appendTpls.push(tpl) if tpl?
        return this
    _isTrash: (id) ->
        if id? and @trashQueue.length isnt 0
            return id in @trashQueue
        return false
    pushAnimateQueue: (id) ->
        @animateQueue.push id if id?
        return this
    _filterTrashQueue: (id)->
        if id? and @trashQueue.length isnt 0
            filterQueue = []
            for item in @trashQueue
                if item isnt id
                    filterQueue.push item
            @trashQueue = filterQueue
        return @trashQueue
    _pushTrashQueue: (id)->
        # delete 'id' from @trashQueue
        @_filterTrashQueue(id)
        # push 'id' to @trashQueue
        @trashQueue.push(id)
        return this
    _resetAnimate: (id)->
        # if id? and (@animateQueue[-1..][0] isnt id)
        $("##{id}")?.removeClass(@option['classin']).removeClass(@option['classout']) if id?
        return this
    _render: ()->
        if @appendTpls.length isnt 0
            @$el.append(@appendTpls.join(''))
        @appendTpls = []
        return this
    _refixSpeed: ()->
        # max loop queue is 3, CSS3 need 1s
        rate = if @animateQueue.length is 0 then 1 else if @animateQueue.length > 3 then 3 else @animateQueue.length
        @animateSpeed = Math.floor(@option['totalTime']/rate)
        ## debug
        # console.log "new rate: #{rate}"
        return rate
    _unique: (array)->
        # Removing Duplicate Elements from Arrays
        # http://island205.github.io/coffeescript-cookbook.github.com/chapters/arrays/removing-duplicate-elements-from-arrays.html
        output = {}
        output[array[key]] = array[key] for key in [0...array.length]
        value for key, value of output

    _animate: () ->
        if @animateQueue.length is 0
            # alert("Error: @animateQueue.length is 0 !!!")
            return this

        if @animateQueue.length > 1
            if (@trashQueue.length isnt 0) and ("#{@animateQueue[0]}" isnt "#{@animateQueue[1]}")
                @_resetAnimate("#{@animateQueue[1]}")
            @_pushTrashQueue(@animateQueue.shift())

        ## debug
        # console.log @trashQueue
        # console.log "toHide: #{@trashQueue[-1..]}"
        # console.log @animateQueue
        # console.log "toShow: #{@animateQueue[0]}"
        # console.log "@animateSpeed: #{@animateSpeed}"
        # @i++
        # console.log "------------- #{@i} -----------------"
        # 
        # if @trashQueue.length isnt 0
        #     if "#{@trashQueue[-1..]}" isnt "#{@animateQueue[0]}"
        #         $("##{@trashQueue[-1..]}")?.addClass(@option['classout'])
        #     $("##{@animateQueue[0]}")?.addClass(@option['classin'])
        # else
        #     $("##{@animateQueue[0]}")?.addClass(@option['classin'])
        #     
        if "#{@trashQueue[-1..]}" isnt "#{@animateQueue[0]}"
            $("##{@trashQueue[-1..]}")?.addClass(@option['classout'])
            $("##{@animateQueue[0]}")?.addClass(@option['classin'])
        
        @_pause() if @animateLoop?
        
        needLoop = if @_refixSpeed() is 1 then false else true

        if needLoop?
            self = this
            @animateLoop = setTimeout ->
                self._animate()
                return
            , @animateSpeed

        return this
    play: ()->
        @_render()

        if !@animateLoop?
            @_animate()

    _pause: ()->
        if @animateLoop?
            clearTimeout @animateLoop
            @animateLoop = null
    cleanTrash: ()->
        if @trashQueue.length > 10
            trashQueue = []
            for trashID in @trashQueue
                if trashID in @animateQueue
                    trashQueue.push trashID
                else
                    $("##{trashID}")?.remove()
            @trashQueue = trashQueue
            # if "#{@trashQueue[-1..]}" is "#{@animateQueue[0]}"
            #     for trashID in @trashQueue[-1..]
            #         $("##{trashID}")?.remove()
            #     @trashQueue = [].push("#{@trashQueue[-1..]}")
            # else
            #     for trashID in @trashQueue
            #         $("##{trashID}")?.remove()
            #     @trashQueue = []
    destroy: ()->
        @_pause()

