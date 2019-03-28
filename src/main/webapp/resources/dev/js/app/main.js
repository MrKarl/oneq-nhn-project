'use strict';
require(['jquery', 'underscore', 'backbone', 'util/tpl',
        'model/question-model', 'model/question-collection',
        'view/question', 'view/question-list', 'view/header', 'view/mypage', 'model/count-model'],

function($, _, Backbone, tpl, Question, Questions, QuestionView, QuestionListView, HeaderView, MypageView, Count) {
    var responseParse = function(resp) {
        var header = resp.header;
        if (!header) {
            return resp;
        }
        if (header.isSuccessful) {
            return resp.body;
        }
        alert(header.resultMessage);
        location.href = '/';
        //            location.href ="/error";
        return null;
    };

    var AppRouter = Backbone.Router.extend({
        initialize: function() {
            var popUpArray = [
                {
                    target: '.create-question',
                    content: '투표 등록'
                 },
                 {
                     target: '.login',
                     content: '로그인'
                 }
            ];
            this.headerView = new HeaderView();
            /* for IE, cache disabled */
            $.ajaxSetup({cache: false});
            this.initializePopUp(popUpArray);
        },
        routes: {
            '': 'home',
            'tag/:hashName': 'tag',
            'my/questions': 'mypage',
            'v/:questionId': 'questionPage'
        },

        home: function() {
            this.before(function() {
                this.questions = new Questions();
                this.questions.fetch();
                this.questionListView = new QuestionListView({
                    model: this.questions
                });
                this.showView('#content', this.questionListView);
            });
        },
        
        initializePopUp: function(popUpArray) {
            popUpArray.forEach(function(element) {
                $(element.target).popup({
                    content: element.content
                });
            });
        },
        
        /*
         * @param  {String} Hashname for search
         * @return {void}   Rendering View
         */
        tag: function(hashName) {
            this.before(function() {
                $.loader({
                    className: 'blue-with-image',
                    content: ''
                });
                this.questions = new Questions([], {'hashName': hashName});
                this.questionListView = new QuestionListView({
                    model: this.questions
                });
                this.headerView.changeSearchForm(hashName);
                this.showView('#content', this.questionListView);
                this.questions.fetch();
            });

            if ($('#content > div').children().length !== 0) {
                $('#notFoundContents').css('display', 'none');
            } else {
                $('#notFoundContents').css('display', 'block');
            }
            $.loader('close');
        },

        mypage: function() {
            this.before(function() {
                this.count = new Count();
                this.count.fetch();
                this.mypage = new MypageView({
                    model: this.count
                });
                $('#content').html(this.mypage.render().el);
            });
        },
        
        questionPage: function(questionId) {
            var self = this;
            
            this.headerView.changeSearchForm(":"+questionId);
            
            this.question = new Question({'questionId': questionId});
            $.loader({
                className: 'blue-with-image',
                content: ''
            });
            this.question.fetch({
                success: function() {
                    self.questionView = new QuestionView({
                        model: self.question,
                        viewMode: 0
                    });
                    self.showView('#content', self.questionView);
                    $.loader('close');
                },
                error: function() {
                    $.loader('close');
                }
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
    tpl.loadTemplates(['question', 'item', 'hash', 'mypage'],
        function() {
            window.app = new AppRouter();
            Backbone.history.start();
        }
    );
}); // End require
