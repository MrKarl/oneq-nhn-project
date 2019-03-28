'use strict';
define(
['jquery', 'underscore', 'backbone', 'semantic', 'util/tpl', 'mediator'],

function($, _, Backbone, Semantic, tpl, mediator) {
    var HashView = Backbone.View.extend({
        tagName: 'span',
        className: 'hash field',

        initialize: function() {
            this.template = _.template(tpl.get('hash-create'));
        },

        render: function() {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        },

        events: {
            'click .hashtagBtn': 'removeHashTagListener'
        },

        removeHashTagListener: function(e) {
            var _this = e.target;
            $(_this).parent('span').remove();
        }
    });

    var HashListView = Backbone.View.extend({
        tagName: 'div',
        className: 'hashes',

        initialize: function() {
            mediator.installTo(this);
            this.subscribe('addHash', this.appendHash);
            this.hashViews = [];
        },

        render: function() {
            return this;
        },

        appendHash: function(hashName) {
            var hashTagText = hashName;
            var hashtagArr = hashTagText.split(/[ ,]+/);
            var hashtagArrAlready = [];
            var $hashTagExist;
            var hashValue = '';
            var hashtag = '';
            var i;
            var hashidx;
            var newHashView;
            hashTagText = hashTagText.replace(/#/g, '').replace(/\</g, '&lt;').replace(/\>/g, '&gt;');
            hashtagArr = hashtagArr.reduce(function(a, b) {
                if (a.indexOf(b) < 0) {
                    a.push(b);
                }
                return a;
            }, []);

            $hashTagExist = $('#hashList input[name="hashName[]"]');
            $hashTagExist.each(function() {
                hashtagArrAlready.push($(this).val());
            });

            var hashLength = hashtagArr.length;
            for (i = 0; i < hashLength; i = i + 1) {
                hashtag = hashtagArr[i].trim();
                hashtag = hashtag.replace(/ /g, '');

                if ($.inArray(hashtag, hashtagArrAlready) < 0 && hashtag !== '') {
                    hashValue = hashtag.replace(/\'/g, '&#39;').replace(/\'/g, '&quot;');
                    newHashView = new HashView({
                        model: new Backbone.Model({
                            hashName: hashtag,
                            hashValue: hashValue
                        })
                    });
                    this.hashViews.push(newHashView);
                    this.$el.append(newHashView.render().el);
                }
            }
        },

        showView: function(selector, view) {
            if (this.currentView) {
                this.currentView.close();
            }
            $(selector).html(view.render().el);
            this.currentView = view;

            return view;
        }
    });
    return HashListView;
});
