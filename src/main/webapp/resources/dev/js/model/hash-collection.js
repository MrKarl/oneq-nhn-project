'use strict';
define(
['jquery', 'underscore', 'backbone', 'model/hash-model'],

function($, _, Backbone, Hash) {
    var Hashs = Backbone.Collection.extend({
        model: Hash,
        url: './api/hash'
    });

    return Hashs;
});
