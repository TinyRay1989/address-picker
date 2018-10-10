;(function(window, document) {
    if (window.NodeList && !NodeList.prototype.forEach) {
        NodeList.prototype.forEach = function (callback, thisArg) {
            thisArg = thisArg || window
            for (var i = 0; i < this.length; i++) {
                callback.call(thisArg, this[i], i, this)
            }
        }
    }
    if (window.Array && !Array.prototype.forEach) {
        Array.prototype.forEach = Array.prototype.forEach || function (callback) {
            var isArray = Object.prototype.toString.call(this) == '[object Array]';
            if(isArray){
                for(var key in this){
                    if(key != 'forEach'){
                        callback.call(this[key],key,this[key],this);
                    }
                }
            }else{
                throw TypeError;
            }
        }
    }
    var defaults = {
        autoSave : true,
        isAjax : false,
        url : 'http://localhost',
        firstLevelPath : '/provinces',
        subPath : '/address/{addressId}/child',
        highlightNameArray : [],
        highlightCodeArray : ['750'],
        separator : '-',
        data : [],
        callback : function(addressInfo) {console.log('addressInfo:'); console.log(addressInfo);}
    }
    var ajax = {
        get: function (url, fn) {
            var xhr = new XMLHttpRequest();
            xhr.open('GET', url, false);
            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4 && xhr.status == 200 || xhr.status == 304) {
                    fn.call(this, xhr.responseText);
                }
            };
            xhr.send();
        }
    }
    var addressHelper = {
        addressId : "",
        addressIndex : 0,
        addressArray : [],
        addressInfo : {},
        increment : function(inc) {
            this.addressIndex += typeof inc === 'number' ? inc : 1;
        },
        getIndex : function(){
            return this.addressIndex;
        },
        updateIndex : function(addressIndex) {
            this.addressIndex = addressIndex;
        },
        init : function (opts){
            var addressInfo = Object.create({
                ids : [],
                values : [],
                separator : defaults.separator,
                getDesc : function() {
                    return this.values.join(this.separator);
                }
            });
            addressInfo.separator = opts.separator;
            addressInfo.ids = [];
            addressInfo.values = [];
            this.addressInfo = addressInfo;
            this.addressArray = opts.data;
            this.isAjax = opts.isAjax;
            this.url = opts.url;
            this.firstLevelPath = opts.firstLevelPath;
            this.subPath = opts.subPath;
        },
        pushInfo : function(id, value){
            var index = this.getIndex();
            this.addressInfo.ids[index] = id;
            this.addressInfo.values[index] = value;
            this.increment();
            this.addressId = id;
        },
        popInfo : function(){
            this.cutInfo(this.getIndex() - 1);
        },
        cutInfo : function(index) {
            var ids = this.addressInfo.ids;
            var values = this.addressInfo.values;
            ids.splice(index);
            values.splice(index);
            this.addressId = ids[ids.length -1];
            this.addressIndex = index;
        },
        findChildAddressArray : function(addressId) {
            var childAddressArray;
            //ajax----------------------------
            if(this.isAjax && addressId){
                var url = (this.url + this.subPath).replace("{addressId}", addressId);
                ajax.get(url, function(result) {
                    childAddressArray = eval(result);
                });
                return childAddressArray;
            }
            //all data------------------------
            var ids = this.addressInfo.ids;
            this.addressArray.forEach(function(address) {
                if(ids[0] == address.id){
                    childAddressArray = address.child;
                }
            });
            for(var i = 1; i < ids.length; i++){
                childAddressArray.forEach(function(childAddress) {
                    if(ids[i] == childAddress.id){
                        childAddressArray = childAddress.child;
                        return childAddressArray;
                    }
                });
            }
            return childAddressArray;
        }
    }
    var AddressPicker = function() {
        this.finished = false;
        this.operatingArea = function(){
            var operatingArea;
            var operatingAreaHTML =
            ( '<div id="address_selector">'
            + '  <div data-widget="tabs" id="address_selector_content">'
            + '    <div class="mt">'
            + '      <ul class="tab">'
            + '      </ul>'
            + '    </div>'
            + '    <div class="mc">'
            + '    </div>'
            + '  </div>'
            + '  <a href="#none"><div id="address_selector_close"></div></a>'
            + '</div>');
            operatingArea = document.createElement("div");
            operatingArea.innerHTML = operatingAreaHTML;
            return operatingArea;
        }();
        this.markPosition = function() {
            if(!this.trigger) return;
            this.operatingArea.style.top = (this.trigger.offsetTop + this.trigger.offsetHeight) + 'px';
            this.operatingArea.style.left = this.trigger.offsetLeft + 'px';
            this.operatingArea.style.position = 'absolute';
            this.operatingArea.style.zIndex = '9999';
        }
        this.bindEvent = function() {
            var _self = this;
            _self.trigger.addEventListener('click', function() {document.body.appendChild(_self.operatingArea);});
        }
        this.initOperatingArea = function() {
            var _self = this, tabItem, tabContent,
                close = _self.operatingArea.querySelector('#address_selector_close'),
                tabItemsContainer =_self.operatingArea.querySelector('#address_selector_content .mt .tab'),
                tabContentsContainer =_self.operatingArea.querySelector('#address_selector_content .mc');
            var createTabItem = function (){
                var currIndex = _self.getIndex();
                var tabItem = document.createElement('li');
                tabItem.classList.add('curr');
                tabItem.dataset.index = currIndex;
                tabItem.dataset.widget = 'tab-item';
                tabItem.innerHTML = '<a href="#none"><em>请选择</em><i></i></a>';
                tabItem.addEventListener('click', function(){
                    _self.finished = false;
                    var tabItems = tabItemsContainer.querySelectorAll('li');
                    var tabContents = tabContentsContainer.querySelectorAll('ul');
                    this.classList.add('curr');
                    tabContents[currIndex].classList.remove('hidden');
                    tabItems.forEach(function(e){
                        if(e.dataset.index > currIndex) e.remove ? e.remove() : e.removeNode(true);
                    });
                    tabContents.forEach(function(e){
                        if(e.dataset.area > currIndex) e.remove ? e.remove() : e.removeNode(true);
                    });
                    _self.addressHelper.cutInfo(currIndex);
                    _self.valueTo.value = _self.addressInfo.getDesc();
                    tabItem.querySelector('em').innerHTML = '请选择';
                });
               return tabItem;
            }
            var createTabContent = function(tabItem, addressArray) {
                var currIndex = _self.getIndex(), tabContent = document.createElement('ul'), options, optionStr = '';
                tabContent.classList.add('area-list');
                tabContent.dataset.area = currIndex;
                tabContent.dataset.widget = 'tab-content';
                addressArray.forEach(function(e){
                    optionStr = optionStr + '<li><a href="#none" data-id=' + e.id + ' data-value=' + e.name+ '>' + e.name + '</a></li>';
                });
                tabContent.innerHTML = optionStr;
                options = tabContent.querySelectorAll('a');
                options.forEach(function(option) {
                    option.addEventListener('click', function() {
                        var id = this.dataset.id;
                        var value = this.dataset.value;
                        if(_self.finished){
                            _self.addressHelper.popInfo();
                        }
                        tabItem.classList.remove('curr')
                        tabContent.classList.add('hidden');
                        tabItem.querySelector('em').innerHTML = value;
                        _self.addressHelper.pushInfo(id, value)
                        _self.valueTo.value = _self.addressInfo.getDesc();
                        var childAddressArray = _self.addressHelper.findChildAddressArray(id)
                        if(!childAddressArray) {
                            _self.finished = true;
                            if(_self.opts.autoSave){
                                _self.save();
                            }else{
                                _self.close();
                            }
                            tabItem.classList.add('curr')
                            tabContent.classList.remove('hidden');
                            return;
                        }

                        var newTabItem = createTabItem();
                        var newTabContent = createTabContent(newTabItem, childAddressArray);
                        tabItemsContainer.appendChild(newTabItem);
                        tabContentsContainer.appendChild(newTabContent);
                    });
                });
                return tabContent;
            }
            tabItem = createTabItem();
            var firstLevelAddressArray;
            if(this.opts.isAjax){
                ajax.get(this.opts.url + this.opts.firstLevelPath, function(result) {
                     firstLevelAddressArray = eval(result);
                });
            }else{
                firstLevelAddressArray = _self.addressHelper.addressArray;
            }
            tabContent = createTabContent(tabItem, firstLevelAddressArray);
            tabItemsContainer.appendChild(tabItem);
            tabContentsContainer.appendChild(tabContent);
            close.addEventListener('click', function(e) { _self.close(e);});

        }
    }
    AddressPicker.prototype = {
        init : function(opts) {
            this.opts = extend(Object.create(defaults), opts);
            this.addressHelper = Object.create(addressHelper);
            this.addressHelper.init(this.opts);
            this.addressInfo = this.addressHelper.addressInfo;
            this.trigger = document.querySelector(opts.trigger);
            this.valueTo = opts.valueTo ? document.querySelector(opts.valueTo) : document.querySelector(opts.trigger);
            this.callback = this.opts.callback;
            this.markPosition();
            this.bindEvent();
            this.initOperatingArea();
            function extend (target, source) {
                for (var p in source) {
                    if (source.hasOwnProperty(p)) {
                        target[p] = source[p];
                    }
                }
                return target;
            }
        },
        getIndex : function(){
            return this.addressHelper.getIndex();
        },
        save : function(e){
            this.callback(this.addressInfo);
            this.close(e);
        },
        close : function(e) {
            if(this.operatingArea.parentNode){
                document.body.removeChild(this.operatingArea);
            }
        }
    }
    window.AddressPicker = AddressPicker;
})(window, document);
