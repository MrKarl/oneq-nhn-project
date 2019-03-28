'use strict';
define(
['jquery', 'underscore', 'backbone'],

function($, _, Backbone) {
    var Item = Backbone.Model.extend({
        url: './api/item',
        idAttribute: 'itemId',
        defaults: {
            isVoted: 0
        }
    });

    return Item;
});
