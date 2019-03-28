'use strict';
define(
['jquery', 'underscore', 'backbone', 'semantic', 'util/tpl', 'view/question'],

function($, _, Backbone, Semantic, tpl, QuestionView) {
    var QuestionListView = Backbone.View.extend({
        initialize: function() {
            this.model.bind('reset', this.render, this);
            this.model.bind('add', this.appendList, this);

            _.bindAll(this, 'detectScroll');
            $(window).scroll(this.detectScroll);
        },

        render: function() {
            _.each(this.model.models, function(question) {
                this.appendList(question);
            }, this);

            return this;
        },

        appendList: function(question) {
            this.$el.append(new QuestionView({
                model: question,
                viewMode: 0
            }).render().el);
        },

        detectScroll: function() {
            var scrollHeight = $(window).scrollTop() + $(window).height();
            var documentHeight = $(document).height();
            if (scrollHeight === documentHeight) {
                this.model.fetch({remove: false});
            }
        }


    });

    return QuestionListView;
});
