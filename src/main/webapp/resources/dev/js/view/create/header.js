'use strict';
define(
['jquery', 'underscore', 'backbone', 'semantic', 'util/tpl'],

function($, _, Backbone) {
    var HeaderView = Backbone.View.extend({
        el: '#header',

        initialize: function() {
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
            var hashName;
            if (e.keyCode === 13) {
                hashName = this.searchForm.val();
                if (hashName === '') {
                    // do nothing
                } else if (hashName === ':mypage') {
                    if (!this.isLoginUser()) {
                        window.open('./oauth', '_blank',
                        'top=0,left=0,width=700,height=300,resizable=yes,scrollbars=no,titlebar=no');
                        // return false;
                    }
                    document.location = '/#my/questions';
                } else {
                    document.location = '/#tag/' + hashName;
                }
            }
        },

        createQuestion: function() {
            if (this.isLoginUser()) {
                location.href = '/create';
            } else {
                this.login();
            }
        },

        login: function() {
            if (!this.isLoginUser()) {
                window.open('./oauth', '_blank',
                'top=0,left=0,width=700,height=300,resizable=yes,scrollbars=no,titlebar=no');
                // return false;
            } else {
                location.reload();
            }
        },

        isLoginUser: function() {
            var status = document.cookie.indexOf('uuid');
            return status !== -1;
        }

    });

    return HeaderView;
});
