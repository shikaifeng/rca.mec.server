// Generated by CoffeeScript 1.7.1
this.currentItem = null;

this.Navigator = (function() {
  Navigator.prototype.disable = function() {
    return this._moving = true;
  };

  Navigator.prototype.enable = function() {
    return this._moving = false;
  };

  Navigator.prototype.up = function() {
    if (this._currentZone && this._currentZone.up) {
      return this._currentZone.up();
    }
  };

  Navigator.prototype.down = function() {
    if (this._currentZone && this._currentZone.down) {
      return this._currentZone.down();
    }
  };

  Navigator.prototype.left = function() {
    if (this._currentZone && this._currentZone.left) {
      return this._currentZone.left();
    }
  };

  Navigator.prototype.right = function() {
    if (this._currentZone && this._currentZone.right) {
      return this._currentZone.right();
    }
  };

  Navigator.prototype.topZone = function() {
    var curZone, self;
    self = this;
    curZone = self._currentZone;
    if (curZone) {
      if (typeof curZone.topZone === 'function') {
        KeyController.active(curZone.topZone());
      } else if (self.topZone) {
        KeyController.active(curZone.topZone);
      }
    }
  };

  Navigator.prototype.bottomZone = function() {
    var curZone, self;
    self = this;
    curZone = self._currentZone;
    if (curZone) {
      if (typeof curZone.bottomZone === 'function') {
        KeyController.active(curZone.bottomZone());
      } else if (self.bottomZone) {
        KeyController.active(curZone.bottomZone);
      }
    }
  };

  Navigator.prototype.leftZone = function() {
    var curZone, self;
    self = this;
    curZone = self._currentZone;
    if (curZone) {
      if (typeof curZone.leftZone === 'function') {
        KeyController.active(curZone.leftZone());
      } else if (self.leftZone) {
        KeyController.active(curZone.leftZone);
      }
    }
  };

  Navigator.prototype.rightZone = function() {
    var curZone, self;
    self = this;
    curZone = self._currentZone;
    if (curZone) {
      if (typeof curZone.rightZone === 'function') {
        KeyController.active(curZone.rightZone());
      } else if (self.rightZone) {
        KeyController.active(curZone.rightZone);
      }
    }
  };

  function Navigator() {
    this._zones = {};
    this._moving = false;
  }

  Navigator.prototype.addZone = function(zone) {
    if (!zone || !zone.id) {
      throw new Error("invalid zone");
    } else if (this._zones[zone.id]) {
      throw new Error("Zone " + zone.id + " already added");
    } else {
      return this._zones[zone.id] = zone;
    }
  };

  Navigator.prototype.removeZone = function(zone) {
    if (!zone || !zone.id) {
      throw new Error("invalid zone");
    } else if (this._zones[zone.id]) {
      return delete this._zones[zone.id];
    }
  };

  Navigator.prototype.active = function(zone) {
    var self;
    self = this;
    if (typeof zone === 'string' && this._zones[zone]) {
      zone = this._zones[zone];
    } else {
      if (zone && zone.active) {
        return zone.active();
      }
    }
    if (this._currentZone === zone) {

    } else if (zone) {
      if (this._currentZone != null) {
        this._currentZone.leaveZone();
      }
      this._currentZone = zone;
      return self._currentZone.enterZone();
    }
  };

  Navigator.prototype.focusAction = function(item) {
    var _ref;
    if (((_ref = this._currentZone) != null ? _ref.focusAction : void 0) != null) {
      return this._currentZone.focusAction(item, KeyController.currentItem);
    }
  };

  Navigator.prototype.selectAction = function() {
    var _ref;
    if (((_ref = this._currentZone) != null ? _ref.selectAction : void 0) != null) {
      return this._currentZone.selectAction(KeyController.currentItem);
    }
  };

  Navigator.prototype.backAction = function() {
    var _ref;
    if (((_ref = this._currentZone) != null ? _ref.backAction : void 0) != null) {
      return this._currentZone.backAction();
    }
    if (window._backAction != null) {
      return window._backAction();
    } else {
      history.go(-1);
      return 1;
    }
  };

  return Navigator;

})();

this.View = (function() {
  function View(options) {
    var i, itemDistanceX, itemDistanceY, itemOffset, j, optionName, optionkeys, self, thisRowPosArray, _i, _j, _k, _len, _ref, _ref1;
    if (options == null) {
      options = {};
    }
    optionkeys = ["enableFocusClassName", "enableZoneFocusClassName", "autoNextRow", "backAction", "left", "right", "up", "down", 'getCurrentItem', 'onEnterZone', 'onLeaveZone', 'presetPositions', 'onFocus', 'onSelect', 'id', 'el', 'leftZone', 'rightZone', 'topZone', 'bottomZone', 'itemidPrefix', 'idStartFrom', 'visibleRowCount', 'rowItemsCount', 'itemRect', 'focusClassName', 'itemClassName', 'init', 'enterZone'];
    self = this;
    for (_i = 0, _len = optionkeys.length; _i < _len; _i++) {
      optionName = optionkeys[_i];
      if (typeof options[optionName] !== "undefined") {
        self[optionName] = options[optionName];
      }
    }
    self._currentIndex = 0;
    this._node = document.getElementById(self.id);
    self._cursorPos = [0, 0];
    if (!this.presetPositions) {
      this.presetPositions = [];
      itemDistanceX = self.itemRect.distanceX;
      itemDistanceY = self.itemRect.distanceY;
      itemOffset = self.itemRect.initOffset;
      for (i = _j = 0, _ref = self.visibleRowCount - 1; 0 <= _ref ? _j <= _ref : _j >= _ref; i = 0 <= _ref ? ++_j : --_j) {
        thisRowPosArray = new Array();
        for (j = _k = 0, _ref1 = self.rowItemsCount - 1; 0 <= _ref1 ? _k <= _ref1 : _k >= _ref1; j = 0 <= _ref1 ? ++_k : --_k) {
          thisRowPosArray.push({
            x: itemOffset[0] + itemDistanceX * j,
            y: itemOffset[1] + itemDistanceY * i,
            width: self.itemRect.outerWidth,
            height: self.itemRect.outerHeight
          });
        }
        self.presetPositions.push(thisRowPosArray);
      }
    }
    if (typeof this.init === "function") {
      this.init();
    }
  }

  View.prototype.enableFocusClassName = true;

  View.prototype.enableNavigation = function() {
    return KeyController.addZone(this);
  };

  View.prototype.active = function() {
    return KeyController.active(this.id);
  };

  View.prototype.leftZone = null;

  View.prototype.rightZone = null;

  View.prototype.topZone = null;

  View.prototype.bottomZone = null;

  View.prototype.itemidPrefix = 'item-';

  View.prototype.focusClassName = 'item-focus';

  View.prototype.focusZoneClassName = 'zone-focus';

  View.prototype.itemClassName = 'item';

  View.prototype.idStartFrom = 1;

  View.prototype.visibleRowCount = 1;

  View.prototype.rowItemsCount = 1;

  View.prototype.autoNextRow = true;

  View.prototype.itemRect = {
    distanceX: 342,
    distanceY: 342,
    outerWidth: 302,
    outerHeight: 302,
    initOffset: [110, 0]
  };

  View.prototype.enterZone = function() {
    if (typeof this.onEnterZone === "function") {
      this.onEnterZone();
    }
    if (this.enableZoneFocusClassName) {
      $(this._node).addClass(this.focusZoneClassName);
    }
    KeyController.currentItem = this.getCurrentItem();
    return this.focusAction(KeyController.currentItem);
  };

  View.prototype.leaveZone = function() {
    if (typeof this.onLeaveZone === "function") {
      this.onLeaveZone();
    }
    if (this.enableZoneFocusClassName) {
      $(this._node).removeClass(this.focusZoneClassName);
    }
    if (this._focusedItem) {
      return $(this._focusedItem).removeClass(this.focusClassName);
    }
  };

  View.prototype.getCurrentItem = function() {
    if (!this._focusedItem) {
      this._focusedItem = document.getElementById(this.itemidPrefix + this.idStartFrom + "");
    }
    return this._focusedItem;
  };

  View.prototype.onFocus = function(item) {};

  View.prototype.init = function() {};

  View.prototype.onSelect = function(item) {};

  View.prototype.selectAction = function(item, currentItem) {
    var self;
    self = this;
    return self.onSelect(item, currentItem);
  };

  View.prototype.focusAction = function(item, currentItem) {
    var self;
    self = this;
    if (item) {
      self._focusedItem = item;
      if (self.enableFocusClassName) {
        if (currentItem) {
          $(currentItem).removeClass(self.focusClassName);
        }
        $(item).addClass(self.focusClassName);
      }
      self.onFocus(item, currentItem);
      return KeyController._moving = false;
    }
  };

  View.prototype.up = function() {
    var curNum, nextEl, nextNum, nextXPos, self;
    self = this;
    self._shouldScroll = false;
    curNum = parseInt(self._focusedItem.id.match(/(\d+)$/)[1]);
    nextNum = curNum - this.rowItemsCount;
    if (nextNum < 1) {
      return null;
    }
    nextEl = $('#' + this.itemidPrefix + nextNum)[0];
    nextXPos = this._cursorPos[0];
    if (nextEl && nextEl !== self._focusedItem) {
      self._cursorPos[0] = nextXPos;
      if (self._cursorPos) {
        if (self._cursorPos[1] > 0) {
          self._cursorPos[1]--;
        } else {
          self._currentIndex -= 2;
          self._cursorPos[1] = self.visibleRowCount - 1;
          self._shouldScroll = true;
        }
      }
    }
    return nextEl;
  };

  View.prototype.down = function() {
    var curNum, i, nextEl, nextNum, nextXPos, potentialFocusItem, potentialFocusItemId, self, _i, _ref, _ref1;
    self = this;
    self._shouldScroll = false;
    curNum = parseInt(self._focusedItem.id.match(/(\d+)$/)[1]);
    nextNum = curNum + this.rowItemsCount;
    nextEl = $('#' + this.itemidPrefix + nextNum)[0];
    nextXPos = this._cursorPos[0];
    if (!nextEl && (this._cursorPos[1] === (this.visibleRowCount - 2))) {
      for (i = _i = _ref = curNum + this.rowItemsCount, _ref1 = this.rowItemsCount * (this._cursorPos[1] + 1) + 1; _ref <= _ref1 ? _i <= _ref1 : _i >= _ref1; i = _ref <= _ref1 ? ++_i : --_i) {
        potentialFocusItemId = self.itemidPrefix + i;
        potentialFocusItem = $('#' + potentialFocusItemId)[0];
        if (potentialFocusItem) {
          nextEl = potentialFocusItem;
          nextXPos = i % this.rowItemsCount - 1;
          break;
        }
      }
    }
    if (!nextEl) {
      return null;
    }
    if (nextEl && nextEl !== self._focusedItem) {
      if (self._cursorPos) {
        self._cursorPos[0] = nextXPos;
        if (self._cursorPos[1] < self.presetPositions.length - 1) {
          self._cursorPos[1]++;
        } else {
          self._currentIndex += 2;
          self._cursorPos[1] = 0;
          self._shouldScroll = true;
        }
      }
    }
    return nextEl;
  };

  View.prototype.left = function() {
    var curNum, next, nextNum, self;
    self = this;
    self._shouldScroll = false;
    if (self._cursorPos[0] === 0) {
      if (typeof self.leftZone === "function" && self.leftZone() || typeof self.leftZone === "string") {
        return null;
      } else {
        if (self._cursorPos[1] > 0) {
          self._cursorPos[0] = self.rowItemsCount - 1;
          self._cursorPos[1]--;
        } else {
          if (self._currentIndex > 0) {
            self._currentIndex--;
            self._shouldScroll = true;
          }
        }
      }
    } else {
      self._cursorPos[0]--;
    }
    curNum = parseInt(self._focusedItem.id.match(/(\d+)$/)[1]);
    nextNum = curNum - 1;
    return next = document.getElementById(self.itemidPrefix + nextNum);
  };

  View.prototype.right = function() {
    var curNum, next, nextNum, self;
    self = this;
    self._shouldScroll = false;
    curNum = parseInt(self._focusedItem.id.match(/(\d+)$/)[1]);
    nextNum = curNum + 1;
    next = $('#' + self.itemidPrefix + nextNum)[0];
    if (!next) {
      return null;
    }
    if (self._cursorPos[0] === (self.rowItemsCount - 1)) {
      if (typeof self.rightZone === "function" && self.rightZone() || typeof self.rightZone === "string") {
        return null;
      } else {
        self._cursorPos[0] = 0;
        if (self._cursorPos[1] === (self.visibleRowCount - 1)) {
          self._currentIndex++;
          self._shouldScroll = true;
        } else {
          self._cursorPos[1]++;
        }
      }
    } else {
      self._cursorPos[0]++;
    }
    return next;
  };

  return View;

})();

window.keyDown = function(event) {
  var e, nextItem;
  if (KeyController._moving) {
    return 0;
  }
  try {
    switch (event.keyCode) {
      case 38:
      case 19:
        nextItem = KeyController.up();
        break;
      case 40:
      case 20:
        nextItem = KeyController.down();
        break;
      case 37:
      case 21:
        nextItem = KeyController.left();
        break;
      case 39:
      case 22:
        nextItem = KeyController.right();
        break;
      case 13:
      case 23:
        KeyController.selectAction(KeyController.currentItem);
        break;
    }
    if (nextItem) {
      if (KeyController.currentItem !== nextItem) {
        KeyController._moving = true;
        KeyController.focusAction(nextItem);
        KeyController.currentItem = nextItem;
      }
    } else {
      switch (event.keyCode) {
        case 38:
        case 19:
          KeyController.topZone();
          break;
        case 40:
        case 20:
          KeyController.bottomZone();
          break;
        case 37:
        case 21:
          KeyController.leftZone();
          break;
        case 39:
        case 22:
          KeyController.rightZone();
      }
    }
  } catch (_error) {
    e = _error;
    throw e;
  }
  return 0;
};

this.KeyController = new Navigator;

document.onkeydown = keyDown;
