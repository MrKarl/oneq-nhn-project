'use strict';
require.config({
    //Initialize the application with the main application file
    deps: ['app/oneView'],

    baseUrl: 'js',

    paths: {
        //Lib
        'jquery': 'lib/jquery-2.2.0.min',
        'underscore': 'lib/underscore-min',
        'backbone': 'lib/backbone-min',
        'semantic': 'lib/semantic.min',
        'loader': 'lib/jquery.loader'
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
        'loader': {
            deps: ['jquery'],
            exports: 'Loader'
        }
    }
});
