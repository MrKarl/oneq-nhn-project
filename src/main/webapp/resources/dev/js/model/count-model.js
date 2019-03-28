'use strict';
define(
['jquery', 'underscore', 'backbone'],

function($, _, Backbone) {
    var Count = Backbone.Model.extend({
        url: './api/my/question/post-count',
        idAttribute: 'userId',
        defaults: {
            'writeCount': 0,
            'voteCount': 0
        }
    });

    return Count;
});
