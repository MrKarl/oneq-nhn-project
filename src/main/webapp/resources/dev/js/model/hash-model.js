'use strict';
define(
['jquery', 'underscore', 'backbone'],

function($, _, Backbone) {
    var Hash = Backbone.Model.extend({
        urlRoot: './api/hash',
        idAttribute: 'hashId',
        defaults: {
            'color': ''
        }
    });

    return Hash;
});
