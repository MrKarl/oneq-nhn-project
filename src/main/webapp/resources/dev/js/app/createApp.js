'use strict';
require(['jquery', 'underscore', 'backbone', 'util/tpl',
         'code-snippet', 'calendar', 'date-picker', 'mediator',
         'view/create/question-create', 'view/create/item-list',
         'view/create/hash-list', 'view/create/header'],

function($, _, Backbone, tpl, CodeSnippet, Calendar, DatePicker,
          mediator, QuestionCreateView, ItemListView, HashListView, HeaderView) {
    var AppRouter = Backbone.Router.extend({
        initialize: function() {
            var popUpArray = [
                {
                    target : '.create-question',
                    content : '투표 등록'
                },
                {
                    target : '.login',
                    content : '로그인'
                },
                {
                    target: '.notServiced',
                    title: '구현 추가 예정',
                    content: '추가 예정 기능입니다.'
                },
                {
                    target: '#item_add_btn',
                    title: '투표 항목수',
                    content: '최대 투표 항목의 개수는 6개입니다.'
                },
                {
                    target: '.media_add',
                    title: '미디어 추가',
                    content: '이미지나 비디오를 추가하실 수 있습니다.'
                },
                {
                    target: '#hashtag_add_text',
                    title: '해시태그 추가',
                    content: '해시태그를 추가하실 수 있습니다.(최대글자수 20자)'
                },
                {
                    target: '#hashtag_add_btn',
                    title: '해시태그 추가',
                    content: '해시태그를 추가하실 수 있습니다.(최대글자수 20자)'
                }
            ];
            this.questionMetaView = new QuestionCreateView();
            this.headerView = new HeaderView();
            this.itemListView = new ItemListView();
            this.hashListView = new HashListView();

            this.showView('#question-create', this.questionMetaView);
            this.showView('#itemList', this.itemListView);
            this.showView('#hashList', this.hashListView);
            this.initializeCalendar();

            this.initializePopUp(popUpArray);
        },

        showView: function(selector, view) {
            $(selector).html(view.render().el);
            return view;
        },

        initializePopUp: function(popUpArray) {
            popUpArray.forEach(function(element) {
                $(element.target).popup({
                    title: element.title,
                    content: element.content
                });
            });
        },

        initializeCalendar: function() {
            var pickerGenerater = function(target, calendar) {
                var picker = new tui.component.DatePicker({
                    element: target,
                    dateForm: 'yyyy-mm-dd',
                    pos: {
                        left: 0,
                        top: -50
                    }
                }, calendar);

                return picker;
            };

            var calendar = new tui.component.Calendar({
                element: '#layer',
                titleFormat: 'yyyy-m',
                todayFormat: 'yyyy-mm-dd (D)'
            });
            var picker1 = pickerGenerater('#startAt', calendar);
            var picker2 = pickerGenerater('#stopAt', calendar);

            var validateStartStopDate = function($me, $other) {
                var picker1Date = picker1.getDateHash();
                var picker2Date = picker2.getDateHash();

                var meVal = $me.val();
                if (meVal !== '') {
                    if (picker1Date.year <= picker2Date.year &&
                      picker1Date.month <= picker2Date.month &&
                      picker1Date.date <= picker2Date.date) {
                        //
                    } else if (picker1Date.year <= picker2Date.year &&
                       picker1Date.month < picker2Date.month) {
                        //
                    } else {
                        $other.val('');
                        alert('투표 시작일보다 투표 마감일이 작을 수 없습니다.');
                    }
                }
            };

            var today = picker1.getDateHash();
            var endRange = {year: 2100, month: 12, date: 31};

            picker1.addRange(today, endRange);
            picker2.addRange(today, endRange);

            $('#startAt').val('');
            $('#stopAt').val('');

            picker1.on('update', function() {
                validateStartStopDate($('#stopAt'), $('#startAt'));
            });

            picker2.on('update', function() {
                validateStartStopDate($('#startAt'), $('#stopAt'));
            });
        }
    });

    tpl.loadTemplates(['question-create', 'item-create', 'hash-create'],
        function() {
            window.app = new AppRouter();
            Backbone.history.start();
        }
    );
});
