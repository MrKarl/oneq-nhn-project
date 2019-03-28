'use strict';
define(
['jquery', 'underscore', 'backbone'],

function($, _, Backbone) {
    var Question = Backbone.Model.extend({
        urlRoot: './api/question',

        idAttribute: 'questionId',

        defaults: {
            'questionId': null,
            'title': '',
            'content': null,
            'questionTypeCode': 0,
            'hit': 0,
            'mediaTypeCode': 0,
            'mediaPath': null,
            'startAt': null,
            'stopAt': null,
            'voteUserCountMax': 0,
            'voteUserCount': 0,
            'createdAt': null,
            'finishedAt': null,
            'items': null,
            'hashs': null,
            'hitPosted' : false
        },
        
        postHitUrl: function() {
            return this.urlRoot+'/'+this.get("questionId")+'/hit';
        },
        
        isHitPosted: function() {
            return this.get("hitPosted");
        }
    });

    return Question;
});
