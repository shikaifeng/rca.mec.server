# @Dependencies: base.js.coffee
# 2014.7.22

@currentItem = null

class @Navigator
    disable: ->
        this._moving = true
    enable: ->
        this._moving = false
    up: ->
        if this._currentZone && this._currentZone.up
            this._currentZone.up()
    down: ->
        if this._currentZone && this._currentZone.down
            this._currentZone.down()
    left: ->
        if this._currentZone && this._currentZone.left
            this._currentZone.left()
    right: ->
        if this._currentZone && this._currentZone.right
            this._currentZone.right()
    topZone: ->
        self = this
        curZone = self._currentZone
        if curZone
            if typeof curZone.topZone is 'function'
                KeyController.active curZone.topZone()
            else if self.topZone
                KeyController.active curZone.topZone
        return
    bottomZone: ->
        self = this
        curZone = self._currentZone
        if curZone
            if typeof curZone.bottomZone is 'function'
                KeyController.active curZone.bottomZone()
            else if self.bottomZone
                KeyController.active curZone.bottomZone
        return
    leftZone: ->
        self = this
        curZone = self._currentZone
        if curZone
            if typeof curZone.leftZone is 'function'
                KeyController.active curZone.leftZone()
            else if self.leftZone
                KeyController.active curZone.leftZone
        return
    rightZone: ->
        self = this
        curZone = self._currentZone
        if curZone
            if typeof curZone.rightZone is 'function'
                KeyController.active curZone.rightZone()
            else if self.rightZone
                KeyController.active curZone.rightZone
        return
    constructor: ->
        this._zones = {}
        this._moving = false
        # return 0
    addZone: (zone)->
        if !zone || !zone.id
            throw new Error("invalid zone")
        else if this._zones[zone.id] 
            throw new Error("Zone #{zone.id} already added")
        else
            this._zones[zone.id] = zone
    removeZone: (zone)->
        if !zone || !zone.id
            throw new Error("invalid zone")
        else if this._zones[zone.id] 
            delete this._zones[zone.id]
    active: (zone)->
        self = this
        if typeof zone is 'string' && this._zones[zone]
            zone = this._zones[zone]
        else
            if zone && zone.active
                return zone.active()
        if this._currentZone is zone
            return
        else if zone
            if this._currentZone?
                this._currentZone.leaveZone()
            this._currentZone = zone
            # setTimeout ->
            self._currentZone.enterZone()
            # ,100
    focusAction: (item)->
        if this._currentZone?.focusAction?
            this._currentZone.focusAction(item,KeyController.currentItem)
    selectAction: ()->
        if this._currentZone?.selectAction?
            this._currentZone.selectAction(KeyController.currentItem)

    backAction: ()->
        if this._currentZone?.backAction?
            return this._currentZone.backAction()
        if window._backAction?
            return window._backAction()
        else
            history.go(-1)
            return 1

class @View
    constructor: (options={}) ->
        optionkeys = ["enableFocusClassName","enableZoneFocusClassName", "autoNextRow", "backAction","left", "right", "up", "down",'getCurrentItem', 'onEnterZone', 'onLeaveZone', 'presetPositions', 'onFocus', 'onSelect', 'id', 'el', 'leftZone', 'rightZone', 'topZone', 'bottomZone', 'itemidPrefix', 'idStartFrom', 'visibleRowCount', 'rowItemsCount', 'itemRect', 'focusClassName', 'itemClassName','init','enterZone']
        self = this
        for optionName in optionkeys
            if typeof options[optionName] isnt "undefined"
                self[optionName] = options[optionName]
        self._currentIndex = 0 
        this._node = document.getElementById(self.id)
        # if self.el
        #   this.$el = $(self.el)
        self._cursorPos = [0, 0]
        if !this.presetPositions
            this.presetPositions = []
            # outer width means distance between items' left edge
            itemDistanceX = self.itemRect.distanceX
            itemDistanceY = self.itemRect.distanceY
            # # first item offset
            itemOffset = self.itemRect.initOffset
            for i in [0..self.visibleRowCount - 1]
                # # each row
                thisRowPosArray = new Array()
                for j in [0.. self.rowItemsCount - 1]
                    # # each item in row
                    thisRowPosArray.push
                        x: itemOffset[0] + itemDistanceX*j
                        y: itemOffset[1] + itemDistanceY*i
                        width: self.itemRect.outerWidth
                        height: self.itemRect.outerHeight
                self.presetPositions.push(thisRowPosArray)
        this.init?()
    enableFocusClassName: true
    enableNavigation: ->
        KeyController.addZone this
    active: ->
        KeyController.active this.id
    leftZone: null
    rightZone: null
    topZone: null
    bottomZone: null
    itemidPrefix: 'item-'
    focusClassName: 'item-focus'
    focusZoneClassName: 'zone-focus'
    itemClassName: 'item'
    idStartFrom: 1
    visibleRowCount: 1
    rowItemsCount: 1
    autoNextRow: true
    itemRect: 
        distanceX: 342
        distanceY: 342
        outerWidth: 302
        outerHeight: 302
        initOffset: [110, 0]
    enterZone: ()->
        this.onEnterZone?()
        if this.enableZoneFocusClassName
            $(this._node).addClass(this.focusZoneClassName)
            # addClass this._node, this.focusZoneClassName
        KeyController.currentItem = this.getCurrentItem()
        this.focusAction(KeyController.currentItem)
    leaveZone: ->
        this.onLeaveZone?()
        if this.enableZoneFocusClassName
            $(this._node).removeClass(this.focusZoneClassName)
            # removeClass this._node, this.focusZoneClassName
        if this._focusedItem
            $(this._focusedItem).removeClass(this.focusClassName)
            # removeClass this._focusedItem, this.focusClassName
    getCurrentItem: ()->
        if !this._focusedItem
            this._focusedItem = document.getElementById(this.itemidPrefix + this.idStartFrom + "")
        return this._focusedItem
    onFocus: (item)->
    init: ()->
    onSelect: (item)->
    selectAction: (item, currentItem)->
        self = this
        self.onSelect(item, currentItem)
    focusAction: (item, currentItem)->
        self = this
        # $('#shadow')[0].style.display = 'none'
        if item
            # setTimeout ->
            self._focusedItem = item
            if self.enableFocusClassName
                if currentItem
                    $(currentItem).removeClass(self.focusClassName)
                    # removeClass currentItem, self.focusClassName
                # addClass item, self.focusClassName
                $(item).addClass(self.focusClassName)
            # $(item).addClass(self.focusClassName)
            self.onFocus(item, currentItem)
            # focusStyle = self.presetPositions[self._cursorPos[1]][self._cursorPos[0]]
            # $('#shadow')[0].style.left = focusStyle.x + 'px'
            # $('#shadow')[0].style.top = focusStyle.y + 'px'
            # $('#shadow')[0].style.display = 'block'
            KeyController._moving = false
            # ,10
        
    up : ->
        self = this
        self._shouldScroll = false
        curNum = parseInt(self._focusedItem.id.match(/(\d+)$/)[1])
        nextNum = curNum - this.rowItemsCount
        if nextNum < 1
            return null
        nextEl = $('#' + this.itemidPrefix + nextNum)[0]
        # nextEl = $id('' + this.itemidPrefix + nextNum)
        nextXPos = this._cursorPos[0]
        if nextEl && nextEl isnt self._focusedItem
            self._cursorPos[0] = nextXPos
            if self._cursorPos # && self.presetPositions 
                if self._cursorPos[1] > 0
                    self._cursorPos[1]--
                else
                    self._currentIndex-=2
                    self._cursorPos[1] = self.visibleRowCount - 1
                    self._shouldScroll = true
        nextEl
    down : ->
        self = this
        self._shouldScroll = false
        curNum = parseInt(self._focusedItem.id.match(/(\d+)$/)[1])
        nextNum = curNum + this.rowItemsCount
        nextEl = $('#' + this.itemidPrefix + nextNum)[0]
        # nextEl = $id('' + this.itemidPrefix + nextNum)
        nextXPos = this._cursorPos[0]
        if !nextEl and (this._cursorPos[1] is (this.visibleRowCount - 2))
            for i in [(curNum + this.rowItemsCount)..(this.rowItemsCount * (this._cursorPos[1] + 1) + 1)]
                potentialFocusItemId = self.itemidPrefix + i
                potentialFocusItem = $('#' + potentialFocusItemId)[0]
                # potentialFocusItem = $id potentialFocusItemId
                if potentialFocusItem
                    nextEl = potentialFocusItem
                    nextXPos = i % this.rowItemsCount - 1
                    break
        if !nextEl
            return null
        if nextEl && nextEl isnt self._focusedItem

            if self._cursorPos # && self.presetPositions 
                self._cursorPos[0] = nextXPos
                if self._cursorPos[1] < self.presetPositions.length - 1
                    self._cursorPos[1]++
                else
                    self._currentIndex+=2
                    self._cursorPos[1] = 0
                    self._shouldScroll = true
        nextEl
            # cursorInst.$el.css    self.presetPositions[ self._cursorPos[1]][self._cursorPos[0]]
    left : ->
        self = this
        self._shouldScroll = false
        if self._cursorPos[0] is 0
            if typeof self.leftZone is "function" and self.leftZone() or typeof self.leftZone is "string"
                return null
                        # ...
            else
                if self._cursorPos[1] > 0
                    self._cursorPos[0] = self.rowItemsCount - 1
                    self._cursorPos[1]--
                else
                    if self._currentIndex > 0
                        self._currentIndex--
                        self._shouldScroll = true
        else
            self._cursorPos[0]--
        curNum = parseInt(self._focusedItem.id.match(/(\d+)$/)[1])
        nextNum = curNum - 1
        next = document.getElementById(self.itemidPrefix + nextNum )
            # $id('shadow-' + curNum).style.display = 'none'
            # $id('shadow-' + nextNum).style.display = 'block'
    right : ->
        self = this
        self._shouldScroll = false
        curNum = parseInt(self._focusedItem.id.match(/(\d+)$/)[1])
        nextNum = curNum + 1
        # next = document.getElementById('item-'+ nextNum )
        next = $('#' + self.itemidPrefix + nextNum)[0]
        # next = $id(self.itemidPrefix + nextNum)
        if !next
            return null
        if self._cursorPos[0] is (self.rowItemsCount - 1)
            if typeof self.rightZone is "function" and self.rightZone() or typeof self.rightZone is "string"
                return null
            else
                self._cursorPos[0] = 0
                if self._cursorPos[1] is (self.visibleRowCount - 1)
                    self._currentIndex++
                    self._shouldScroll = true
                else
                    self._cursorPos[1]++
        else
            self._cursorPos[0]++
        next


window.keyDown = (event)->
    # alert event.keyCode
    if KeyController._moving
        return 0
    try
        # ...
        # ...
    
        switch (event.keyCode)
            # when 38,1
            when 38, 19
                # up()
                nextItem = KeyController.up()
            # when 40,2
            when 40, 20
                # down()
                nextItem = KeyController.down()
            # when 37,3
            when 37, 21
                # left()
                nextItem = KeyController.left()
            # when 39,4
            when 39, 22
                # right()
                nextItem = KeyController.right()
            # when 13
            when 13, 23
                # select()
                KeyController.selectAction(KeyController.currentItem)
            # when 8, 4
            #     back()
            # when 8,45,46
            #     preventDefault = KeyController.backAction()
            #     if preventDefault
            #         event.preventDefault()
            #     return preventDefault
            # when 33
            #     todo("pageUp()")
            # when 34
            #     todo("pageDown()")
            # when 45
            #     todo("goBack()")
            else
                # alert(event.keyCode)
        if nextItem
            if (KeyController.currentItem isnt nextItem)
                KeyController._moving = true
                KeyController.focusAction(nextItem) 
                KeyController.currentItem = nextItem
        else
            switch (event.keyCode)
                # when 38,1
                when 38, 19
                    # up()
                    KeyController.topZone()
                # when 40,2
                when 40, 20
                    # down()
                    KeyController.bottomZone()
                # when 37,3
                when 37, 21
                    # left()
                    KeyController.leftZone()
                # when 39,4
                when 39, 22
                    # right()
                    KeyController.rightZone()
                # when 8, 4
                #     back()
    catch e
        throw e
    return 0

@KeyController = new Navigator

document.onkeydown = keyDown
    