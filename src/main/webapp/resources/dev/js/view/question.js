'use strict';
define(
['jquery', 'underscore', 'backbone', 'semantic', 'util/tpl',
 'model/item-collection', 'view/item-list', 'model/hash-collection', 'view/hash-list'],

function($, _, Backbone, Semantic, tpl, Items, ItemListView, Hashs, HashListView) {
    var QuestionView = Backbone.View.extend({
        tagName: 'div',
        className: 'ui fluid card',

        /*
         * @ parameter viewMode :   0 : defaut(First seen Vote)
         *                          1 : Result Mode(First seen Result)
         *                          2 : Result Only(Result View Only)
         */
        initialize: function(options) {
            this.template = _.template(tpl.get('question'));

            this.viewMode = Number(options.viewMode);
            this.initItemList();
            this.initHashList();
            
            this.model.bind('change:hit', this.reRenderHit, this);
            this.model.bind('change', this.reRender, this);
            this.model.bind('destroy', this.close, this);
        },

        render: function() {
            this.$el.html(this.template(this.model.toJSON()));
            this.renderItemList();
            this.renderHashList();

            this.$dimmerImage = this.$el.find('.dimmer .dimmerImage');
            this.$dimmerYoutube = this.$el.find('.dimmer .dimmerYoutube');
            this.$dimmerShare = this.$el.find('.dimmer .dimmerShare');

            if (this.isOutOfDate() || this.isOverVoteNum()) {
                this.viewResultOnly();
                return this;
            }

            if (this.viewMode === 1) {
                this.viewResult();
            } else {
                this.viewVote();
            }
            return this;
        },

        reRender: function() {
            var voteUserCount = this.model.get('voteUserCount');
            this.$el.find('.vote-num').text(voteUserCount);
            this.itemList.set(this.model.get('items'));

            if (this.isOutOfDate() || this.isOverVoteNum()) {
                this.viewResultOnly();
            }
            return this;
        },
        
        reRenderHit: function() {
            var hitNumEl = this.$el.find('.hitNum');
            hitNumEl.find('.value').text(this.model.get('hit'));
            hitNumEl.transition('flash');
            return this;
        },

        events: {
            'click .result-button': 'viewResult',
            'click .vote-button': 'viewVote',
            'click .iframe-mode': 'setIframeSource',
            'click .itemImage': 'showDimmerImage',
            'click .itemYoutube': 'showDimmerYoutube',
            'click .dimmer-show': 'showDimmerShare',
            'click .dimmer-hide': 'dimmerHide'
        },

        setLinkSource: function() {
            var url = 'http://' + location.host + '/#v/',
                questionId = this.model.get('questionId'),
                src = url + questionId;
            this.$el.find('.link-source').val(src);
        },

        setIframeSource: function() {
            var width = 600,
                height = this.$el.height(),
                url = 'http://' + location.host + '/i#',
                questionId = this.model.get('questionId'),
                mode = this.$el.find('input[name=iframe-mode]:checked').val(),
                src = '<iframe width="' + width + '"height="' + height + '"src="' + url + questionId + '/' + mode + '"frameborder="0"></iframe>';
            this.$el.find('.dimmer .dimmerShare').removeClass('shareDiv');
            this.$el.find('.iframe-source').val(src);
        },

        initItemList: function() {
            this.itemList = new Items(this.model.get('items'),
                    {'questionId': this.model.get('questionId'),
                    'questionTypeCode': this.model.get('questionTypeCode'),
                    'startAt': this.model.get('startAt'),
                    'stopAt': this.model.get('stopAt'),
                    'question': this.model});
            this.itemListView = new ItemListView({
                model: this.itemList
            });
        },

        initHashList: function() {
            this.hashList = new Hashs(this.model.get('hashs'));
            this.hashListView = new HashListView({
                model: this.hashList
            });
        },

        renderItemList: function() {
            this.$el.find('.itemlist').html(this.itemListView.el);
        },

        renderHashList: function() {
            this.$el.find('.hashlist').html(this.hashListView.el);
        },

        viewResult: function() {
            this.$el.find('.result-button').hide();
            this.$el.find('.vote-button').show();
            this.itemListView.viewResult();
            this.postHit();
        },

        viewResultOnly: function() {
            this.$el.find('.extra .button').remove();
            this.itemListView.viewResult();
            this.$el.addClass('finished');
            this.postHit();
        },

        viewVote: function() {
            this.$el.find('.vote-button').hide();
            this.$el.find('.result-button').show();
            this.itemListView.viewVote();
        },
        
        getCurrentDate: function() {
            var now = new Date();
            var mm = (now.getMonth()+1).toString();
            var dd = now.getDate().toString();
            mm = mm[1] ? mm : '0' + mm[0];
            dd = dd[1] ? dd : '0' + dd[0];
            return now.getFullYear()+"-"+mm+"-"+dd;
        },

        isOutOfDate: function() {
            var now = this.getCurrentDate(),
                startAt = this.model.get('startAt'),
                stopAt = this.model.get('stopAt');

            if (now < startAt || now > stopAt) {
                return true;
            }
            return false;
        },

        isOverVoteNum: function() {
            var voteUserCount = this.model.get('voteUserCount');
            var voteUserCountMax = this.model.get('voteUserCountMax');

            if (voteUserCount >= voteUserCountMax && voteUserCountMax !== 0) {
                return true;
            }
            return false;
        },

        hideDimmerContents: function() {
            this.$dimmerImage.hide();
            this.$dimmerYoutube.hide();
            this.$dimmerShare.hide();
        },

        showDimmerImage: function(item) {
            var maxHeight = this.$el.height();
            var maxWidth = this.$el.width();
            var imgHeight, imgWidth;
            var img = new Image();
            this.hideDimmerContents();

            img.src = item.target.currentSrc;

            imgHeight = img.height;
            imgWidth = img.width;

            this.$dimmerImage.attr('src', item.target.src).show();

            this.$el.dimmer('show');

            if (imgHeight > imgWidth) {
                this.resizeByHeight(this, maxHeight);
                if (this.$dimmerImage.width() > maxWidth) {
                    this.resizeByWidth(this, maxWidth, maxHeight);
                }
            } else {
                this.resizeByWidth(this, maxWidth, maxHeight);
                if (this.$dimmerImage.height() > maxHeight) {
                    this.resizeByHeight(this, maxHeight);
                }
            }
        },


        resizeByHeight: function(target, maxHeight) {
            var height = maxHeight - 20;
            target.$dimmerImage.css('margin-top', 10);
            target.$dimmerImage.css('height', height);
            target.$dimmerImage.css('width', 'auto');
        },

        resizeByWidth: function(target, maxWidth, maxHeight) {
            var width = maxWidth - 20;
            target.$dimmerImage.css('width', width);
            target.$dimmerImage.css('height', 'auto');
            target.$dimmerImage.css('margin-top', (maxHeight - target.$dimmerImage.height()) / 2);
        },

        showDimmerYoutube: function(item) {
            var maxHeight = this.$el.height();
            this.hideDimmerContents();
            this.$dimmerYoutube.attr('src', $(item.target).data('youtube')).show();
            this.$dimmerYoutube.css('margin-top', (maxHeight - 350) / 2);
            this.$el.dimmer('show');
        },

        showDimmerShare: function() {
            this.hideDimmerContents();
            this.$dimmerShare.show();
            this.$dimmerShare.find('.menu .item').tab();
            this.$dimmerShare.find('.inline.fields .checkbox').checkbox('check');
            this.setLinkSource();
            this.setIframeSource();
            this.$el.dimmer('show');
        },

        dimmerHide: function() {
            this.$el.dimmer('hide');
        },
        
        postHit: function() {
            var self = this;
            if(!this.model.isHitPosted()){
                $.ajax({
                    url: this.model.postHitUrl(),
                    method: "POST",
                    success: function(response) {
                        var header = response.header;
                        if (header.isSuccessful) {
                            self.model.set("hit",response.body.hit) 
                        }
                    }
                });
                this.model.set("hitPosted",true);
            }
        }
    });

    return QuestionView;
});
