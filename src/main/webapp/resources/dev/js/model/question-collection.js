'use strict';
define(
['jquery', 'underscore', 'backbone', 'model/question-model'],

function($, _, Backbone, Question) {
    var Questions = Backbone.Collection.extend({
        initialize: function(models, options) {
            if (options && options.hashName) {
                this.hashName = options.hashName;
            } else if (options && options.userId) {
                this.userId = options.userId;
                this.option = options.option;
            }
        },
        model: Question,
        url: function() {
            if (this.hashName) {
                return './api/tag/' + this.hashName + '?startIndex=' + this.length;
            } else if (this.userId && this.option === 0) {
                return './api/my/question/write-question?startIndex=' + this.length;
            } else if (this.userId && this.option === 1) {
                return './api/my/question/vote-question?startIndex=' + this.length;
            }

            return './api/question?startIndex=' + this.length;
        }
    });

    return Questions;
});
