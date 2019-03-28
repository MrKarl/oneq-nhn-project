'use strict';
define(
['jquery', 'underscore', 'backbone', 'model/item-model'],

function($, _, Backbone, Item) {
    var Items = Backbone.Collection.extend({
        initialize: function(models, options) {
            this.questionId = options.questionId;
            this.userId = options.userId;
            this.questionTypeCode = options.questionTypeCode;
            this.question = options.question;
        },
        model: Item,
        url: './api/item',

        getResultTotal: function() {
            return this.reduce(function(memo, value) {
                return memo + value.get('resultCount');
            }, 0);
        }
    });

    return Items;
});
