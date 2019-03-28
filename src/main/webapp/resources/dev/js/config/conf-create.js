'use strict';
require.config({
    //Initialize the application with the main application file
    deps: ['app/createApp'],

    baseUrl: '/js',

    paths: {
        //Lib
        'jquery': 'lib/jquery-2.2.0.min',
        'underscore': 'lib/underscore-min',
        'backbone': 'lib/backbone-min',
        'semantic': 'lib/semantic.min',
        'code-snippet': 'lib/code-snippet.min',
        'calendar': 'lib/calendar.min',
        'date-picker': 'lib/date-picker.min',
        'mediator': 'lib/mediator'
    },

    shim: {
        'jquery': {
            exports: '$'
        },
        'underscore': {
            exports: '_'
        },
        'backbone': {
            deps: ['jquery', 'underscore'],
            exports: 'Backbone'
        },
        'semantic': {
            deps: ['jquery'],
            exports: 'Semantic'
        },
        'code-snippet': {
            exports: 'CodeSnippet'
        },
        'calendar': {
            deps: ['code-snippet'],
            exports: 'Calendar'
        },
        'date-picker': {
            deps: ['code-snippet', 'calendar'],
            exports: 'DatePicker'
        },
        'mediator': {
            exports: 'mediator'
        }
    }
});
