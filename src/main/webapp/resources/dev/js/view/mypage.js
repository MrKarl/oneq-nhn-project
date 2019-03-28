'use strict';
define(
['jquery', 'underscore', 'backbone', 'semantic', 'util/tpl',
 'model/question-collection', 'view/question-list'],

function($, _, Backbone, Semantic, tpl, Questions, QuestionListView) {
    var MypageView = Backbone.View.extend({
        tagName: 'div',
        className: 'mypageContent',

        initialize: function() {
            this.template = _.template(tpl.get('mypage'));
            this.model.bind('change', this.render, this);

            _.bindAll(this, 'detectScroll');
            $(window).scroll(this.detectScroll);
            $('#notFoundContents').hide();
        },

        render: function() {
            this.$el.html(this.template(this.model.toJSON()));
            this.write();
            return this;
        },

        events: {
            'click .writed-question': 'write',
            'click .voted-question': 'vote'
        },

        write: function() {
            this.before(function() {
                this.questions = new Questions([], {'userId': this.getCookieUid('uuid'), 'option': 0});
                this.questionListView = new QuestionListView({
                    model: this.questions
                });
                $('.mypage-content').html(this.questionListView.render().el);
                this.questions.fetch();
                this.$el.find('.writed-question').removeClass('basic');
                this.$el.find('.voted-question').addClass('basic');
            });
        },

        vote: function() {
            this.before(function() {
                this.questions = new Questions([], {'userId': this.getCookieUid('uuid'), 'option': 1});
                this.questionListView = new QuestionListView({
                    model: this.questions
                });
                $('.mypage-content').html(this.questionListView.render().el);
                this.questions.fetch();
                this.$el.find('.voted-question').removeClass('basic');
                this.$el.find('.writed-question').addClass('basic');
            });
        },
        /*
         * Callback function 수행 전 작업을 위한 function
         * @param  {function}   callback function
         * @return {void}
         */
        before: function(callback) {
            if (callback) {
                callback.call(this);
            }
        },

        detectScroll: function() {
            var scrollHeight = $(window).scrollTop() + $(window).height();
            var documentHeight = $(document).height();
            if (scrollHeight === documentHeight) {
                this.model.fetch();
            }
        },

        getCookieUid: function(uuid) {
            var i, x, y, ARRcookies = document.cookie.split(';');
            for (i = 0; i < ARRcookies.length; i = i + 1) {
                x = ARRcookies[i].substr(0, ARRcookies[i].indexOf('='));
                y = ARRcookies[i].substr(ARRcookies[i].indexOf('=') + 1);
                x = x.replace(/^\s+|\s+$/g, '');
                if (x === uuid) {
                    return unescape(y);
                }
            }
        }
    });

    return MypageView;
});
