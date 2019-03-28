'use strict';
require(['jquery', 'underscore', 'backbone', 'util/tpl',
        'model/question-model', 'view/question'],

function($, _, Backbone, tpl, Question, QuestionView) {
    var responseParse = function(resp) {
        var header = resp.header;
        if (!header) {
            return resp;
        }
        if (header.isSuccessful) {
            return resp.body;
        }
        alert(header.resultCode + ' : ' + header.resultMessage);
        location.href = '/error';
        return null;
    };

    var AppRouter = Backbone.Router.extend({
        initialize: function() {
        },

        routes: {
            ':questionId/:mode': 'home'
        },

        home: function(questionId, mode) {
            this.before(function() {
                var self = this;
                this.question = new Question({'questionId': questionId});
                this.question.fetch({
                    success: function() {
                        self.questionView = new QuestionView({
                            model: self.question,
                            viewMode: mode
                        });
                        self.showView('#content', self.questionView);
                    },
                    error: function() {
                        alert('투표값을 가져오는데 실패하였습니다.');
                    }
                });
            });
        },
        /*
         * @param  {DOM ID} View를 삽입하고 싶은 Dom ID(ex: '#content')
         * @param  {Backbone.View}  Backbone view 모듈
         * @return {Backbone.View}  삽입된 Backbone View
         */
        showView: function(selector, view) {
            if (this.currentView) {
                this.currentView.close();
            }
            $(selector).html(view.render().el);
            this.currentView = view;

            return view;
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
        }
    });

    Backbone.View.prototype.close = function() {
        if (this.beforeClose) {
            this.beforeClose();
        }
        this.remove();
        this.unbind();
    };

    Backbone.Model.prototype.parse = function(resp, options) {
        return responseParse(resp, options);
    };

    Backbone.Collection.prototype.parse = function(resp, options) {
        return responseParse(resp, options);
    };

    /**
     * Templates 를 로드 하고 난 뒤,
     * AppRouter 생성, history 기록
     */
    tpl.loadTemplates(['question', 'item', 'hash'],
        function() {
            window.app = new AppRouter();
            Backbone.history.start();
        }
    );
}); // End require
