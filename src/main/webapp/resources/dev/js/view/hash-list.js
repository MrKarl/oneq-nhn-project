'use strict';
define(
['jquery', 'underscore', 'backbone', 'semantic', 'util/tpl'],

function($, _, Backbone, Semantic, tpl) {
    var HashView = Backbone.View.extend({

        initialize: function() {
            this.template = _.template(tpl.get('hash'));
            this.model.bind('change', this.render, this);
            this.model.bind('destroy', this.close, this);
        },

        events: {
            'click .content': 'vote'
        },

        render: function() {
            this.setElement(this.template(this.model.toJSON()));
            return this;
        },

        vote: function() {
            this.model.save();
        }
    });

    var HashListView = Backbone.View.extend({
        initialize: function() {
            this.render();

            this.model.bind('reset', this.render, this);
            this.model.bind('add', this.appendList, this);
        },

        render: function() {
            _.each(this.model.models, function(hash) {
                this.appendList(hash);
            }, this);

            return this;
        },

        appendList: function(hash) {
            this.$el.append(new HashView({
                model: hash
            }).render().el);
        }
    });

    return HashListView;
});
