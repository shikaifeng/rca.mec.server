// Generated by CoffeeScript 1.7.1
var FeedAnimate,
  __indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; };

FeedAnimate = (function() {
  function FeedAnimate(id, option) {
    var _option;
    if (id == null) {
      alert('FeedAnimate ERROR: constructor\'s argument "id" is NONE!');
      return false;
    }
    _option = {
      classin: 'feed-right-in',
      classout: 'feed-left-out',
      classhide: 'wiki-hide',
      totalTime: 6000
    };
    this.option = $.extend(_option, option);
    this.elid = id;
    this.$el = $("#" + id);
    this._init();
  }

  FeedAnimate.prototype._init = function() {
    this.appendTpls = [];
    this.animateQueue = [];
    this.animateLoop = null;
    this.animateSpeed = this.option['totalTime'];
    this.trashQueue = [];
    return this;
  };

  FeedAnimate.prototype.append = function(tpl) {
    if (tpl != null) {
      this.appendTpls.push(tpl);
    }
    return this;
  };

  FeedAnimate.prototype._isTrash = function(id) {
    if ((id != null) && this.trashQueue.length !== 0) {
      return __indexOf.call(this.trashQueue, id) >= 0;
    }
    return false;
  };

  FeedAnimate.prototype.pushAnimateQueue = function(id) {
    if (id != null) {
      this.animateQueue.push(id);
    }
    return this;
  };

  FeedAnimate.prototype._filterTrashQueue = function(id) {
    var filterQueue, item, _i, _len, _ref;
    if ((id != null) && this.trashQueue.length !== 0) {
      filterQueue = [];
      _ref = this.trashQueue;
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        item = _ref[_i];
        if (item !== id) {
          filterQueue.push(item);
        }
      }
      this.trashQueue = filterQueue;
    }
    return this.trashQueue;
  };

  FeedAnimate.prototype._pushTrashQueue = function(id) {
    this._filterTrashQueue(id);
    this.trashQueue.push(id);
    return this;
  };

  FeedAnimate.prototype._resetAnimate = function(id) {
    var _ref;
    if (id != null) {
      if ((_ref = $("#" + id)) != null) {
        _ref.removeClass(this.option['classin']).removeClass(this.option['classout']);
      }
    }
    return this;
  };

  FeedAnimate.prototype._render = function() {
    if (this.appendTpls.length !== 0) {
      this.$el.append(this.appendTpls.join(''));
    }
    this.appendTpls = [];
    return this;
  };

  FeedAnimate.prototype._refixSpeed = function() {
    var rate;
    rate = this.animateQueue.length === 0 ? 1 : this.animateQueue.length > 3 ? 3 : this.animateQueue.length;
    this.animateSpeed = Math.floor(this.option['totalTime'] / rate);
    return rate;
  };

  FeedAnimate.prototype._unique = function(array) {
    var key, output, value, _i, _ref, _results;
    output = {};
    for (key = _i = 0, _ref = array.length; 0 <= _ref ? _i < _ref : _i > _ref; key = 0 <= _ref ? ++_i : --_i) {
      output[array[key]] = array[key];
    }
    _results = [];
    for (key in output) {
      value = output[key];
      _results.push(value);
    }
    return _results;
  };

  FeedAnimate.prototype._animate = function() {
    var needLoop, self, _ref, _ref1;
    if (this.animateQueue.length === 0) {
      return this;
    }
    if (this.animateQueue.length > 1) {
      if ((this.trashQueue.length !== 0) && (("" + this.animateQueue[0]) !== ("" + this.animateQueue[1]))) {
        this._resetAnimate("" + this.animateQueue[1]);
      }
      this._pushTrashQueue(this.animateQueue.shift());
    }
    if (("" + this.trashQueue.slice(-1)) !== ("" + this.animateQueue[0])) {
      if ((_ref = $("#" + this.trashQueue.slice(-1))) != null) {
        _ref.addClass(this.option['classout']);
      }
      if ((_ref1 = $("#" + this.animateQueue[0])) != null) {
        _ref1.addClass(this.option['classin']);
      }
    }
    if (this.animateLoop != null) {
      this._pause();
    }
    needLoop = this._refixSpeed() === 1 ? false : true;
    if (needLoop != null) {
      self = this;
      this.animateLoop = setTimeout(function() {
        self._animate();
      }, this.animateSpeed);
    }
    return this;
  };

  FeedAnimate.prototype.play = function() {
    this._render();
    if (this.animateLoop == null) {
      return this._animate();
    }
  };

  FeedAnimate.prototype._pause = function() {
    if (this.animateLoop != null) {
      clearTimeout(this.animateLoop);
      return this.animateLoop = null;
    }
  };

  FeedAnimate.prototype.cleanTrash = function() {
    var trashID, trashQueue, _i, _len, _ref, _ref1;
    if (this.trashQueue.length > 10) {
      trashQueue = [];
      _ref = this.trashQueue;
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        trashID = _ref[_i];
        if (__indexOf.call(this.animateQueue, trashID) >= 0) {
          trashQueue.push(trashID);
        } else {
          if ((_ref1 = $("#" + trashID)) != null) {
            _ref1.remove();
          }
        }
      }
      return this.trashQueue = trashQueue;
    }
  };

  FeedAnimate.prototype.destroy = function() {
    return this._pause();
  };

  return FeedAnimate;

})();
