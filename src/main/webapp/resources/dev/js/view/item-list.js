'use strict';
define(
['jquery', 'underscore', 'backbone', 'semantic', 'loader', 'util/tpl'],

function($, _, Backbone, Semantic, Loader, tpl) {
    var ItemView = Backbone.View.extend({
        tagName: 'div',
        className: 'item',

        initialize: function() {
            this.template = _.template(tpl.get('item'));
            this.model.bind('change', this.reRender, this);
            this.model.bind('destroy', this.close, this);
        },

        events: {
            'click .content>.vote': 'clickVote'
        },

        render: function() {
            this.$el.html(this.template(this.model.toJSON()));
            this.voteRender();
            return this;
        },

        reRender: function() {
            var resultCount = this.model.get('resultCount');
            this.$el.find('.count').text(resultCount);
            this.voteRender();
            return this;
        },

        voteRender: function() {
            if (this.model.get('isVoted') === 1) {
                this.$el.addClass('isVoted');
            } else {
                this.$el.removeClass('isVoted');
            }
        },

        clickVote: function() {
            var status = document.cookie.indexOf('uuid');
            var method, self;

            if (status === -1) {
                window.open('./oauth', '_blank',
                            'top=0,left=0,width=700,height=750,resizable=yes,scrollbars=no,titlebar=no');
                return false;
            }

            $.loader({
                className: 'blue-with-image',
                content: ''
            });

            self = this;
            if (this.model.get('isVoted')) {
                method = 'PUT';
            } else {
                method = 'POST';
            }

            this.model.save(null, {
                type: method,
                success: function() {
                    self.model.collection.question.fetch();
                    $.loader('close');
                },
                error: function() {
                    /*location.reload();*/
                    alert('유효하지 않은 투표입니다.');
                }
            });
        },

        viewVote: function() {
            this.$el.find('.result').hide();
            this.$el.find('.vote').show();
        },

        viewResult: function(resultTotal) {
            var percent,
                result = this.$el.find('.result'),
                bar = result.find('.bar');
            result.show();

            bar.css('width', 0);
            this.$el.find('.vote').hide();
            if (resultTotal === 0) {
                percent = 0;
            } else {
                percent = this.model.get('resultCount') / resultTotal * 100;
            }
            bar.css('width', percent + '%');
        }
    });

    var ItemListView = Backbone.View.extend({
        tagName: 'div',
        className: 'ui items',

        initialize: function() {
            this.render();
        },

        render: function() {
            this.itemViews = [];
            _.each(this.model.models, function(item) {
                item.set('questionId', this.model.questionId);
                item.set('userId', this.model.userId);
                item.set('questionTypeCode', this.model.questionTypeCode);
                this.appendList(item);
            }, this);

            return this;
        },

        appendList: function(item) {
            var newItemView = new ItemView({
                model: item
            });
            this.itemViews.push(newItemView);
            this.$el.append(newItemView.render().el);
        },

        viewResult: function() {
            var resultTotal = this.model.getResultTotal();
            _.each(this.itemViews, function(itemView) {
                itemView.viewResult(resultTotal);
            }, this);
        },
        viewVote: function() {
            _.each(this.itemViews, function(itemView) {
                itemView.viewVote();
            }, this);
        }
    });

    return ItemListView;
});
