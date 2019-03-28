'use strict';
define(
['jquery', 'underscore', 'backbone', 'semantic', 'util/tpl'],

function($, _, Backbone) {
    var HeaderView = Backbone.View.extend({
        el: '#header',

        initialize: function() {
            this.questionIdSearchPattern = /^\:\d+$/;    // :questionId
            this.searchForm = this.$el.find('.searchForm');
            this.render();
        },

        events: {
            'keypress .searchForm': 'search',
            'click .login': 'login',
            'click .create-question': 'createQuestion'
        },

        render: function() {
            if (this.isLoginUser()) {
                this.$el.addClass('user');
            } else {
                this.$el.removeClass('user');
            }
        },

        search: function(e) {
            var searchValue;
            if (e.keyCode === 13) {
                searchValue = this.searchForm.val(); 
                if(this.questionIdSearchPattern.test(searchValue)) {
                    this.questionIdSearch(searchValue.replace(/:/,''));
                } else {
                    this.hashSearch(searchValue)
                }
            }
        },
        questionIdSearch: function(questionId) {
            app.navigate('#v/'+questionId, {trigger:true});
        },
        
        hashSearch: function(hashName) {
            var hashName = this.searchForm.val().replace(/#/g,"");
            if (hashName === '') {
                app.navigate('', {trigger:true});
            } else {
                app.navigate('#tag/' + hashName, {trigger:true});
            }
        },

        changeSearchForm: function(inputValue) {
            this.searchForm.val(inputValue);
        },

        login: function() {
            if (!this.isLoginUser()) {
                window.open('./oauth', '_blank',
                            'top=0,left=0,width=700,height=750,resizable=no,scrollbars=no,titlebar=no');
                return false;
            }

            location.reload();
            return true;
        },
        
        createQuestion: function() {
            if (this.isLoginUser()) {
                location.href = '/create';
            } else {
                this.login();
            }
        },

        isLoginUser: function() {
            var status = document.cookie.indexOf('uuid');
            return status !== -1;
        }
    });

    return HeaderView;
});
