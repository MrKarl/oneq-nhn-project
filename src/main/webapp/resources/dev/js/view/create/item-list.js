'use strict';
define(
['jquery', 'underscore', 'backbone', 'semantic', 'util/tpl', 'mediator'],

function($, _, Backbone, Semantic, tpl, mediator) {
    var MAX_LENGTH_ITEM = 5;

    var ItemView = Backbone.View.extend({
        tagName: 'div',
        className: 'item',

        initialize: function() {
            this.template = _.template(tpl.get('item-create'));
        },

        render: function() {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        },

        events: {
            'click .remove': 'removeItemListener'
        },

        removeItemListener: function(e) {
            $(e.target).parents('.item').remove();
        }
    });

    var ItemListView = Backbone.View.extend({
        tagName: 'div',
        className: 'ui divided items',

        initialize: function() {
            mediator.installTo(this);
            this.subscribe('addItem', this.appendItem);
        },

        render: function() {
            this.appendItem();
            return this;
        },

        appendItem: function() {
            var newItemView;
            if (this.checkItemLength()) {
                newItemView = new ItemView({
                    model: new Backbone.Model({
                        title: 'itemTitleTest'
                    })
                });
                this.$el.append(newItemView.render().el);
            }
        },

        checkItemLength: function() {
            var length = $('#itemList .item').length;

            if (length > MAX_LENGTH_ITEM) {
                mediator.publish('errorModal', '<span style="color:red;font-weight:bold"> * 항목이 너무많아요 :(</span>');
                return false;
            }

            return true;
        },

        showView: function(selector, view) {
            $(selector).html(view.render().el);
            return view;
        }
    });

    return ItemListView;
});
