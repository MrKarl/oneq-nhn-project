'use strict';
define(
['jquery', 'underscore', 'backbone', 'semantic', 'calendar', 'util/tpl', 'mediator'],

function($, _, Backbone, Semantic, Calendar, tpl, mediator) {
    var MEDIA_TYPE_IMAGE = 1;
    var MEDIA_TYPE_VIDEO = 2;
    var MEDIA_SRC_URL = 'url';
    var MEDIA_SRC_FILE = 'file';
    var ALLOWED_MEDIA_TYPES = ['.jpg', '.png', '.gif', '.jpeg'];

    var QuestionCreateView = Backbone.View.extend({
        tagName: 'div',
        className: 'question ui fluid card',

        initialize: function() {
            mediator.installTo(this);
            this.subscribe('errorModal', this.errorModalSetAndShow);
            
            this.template = _.template(tpl.get('question-create'));
        },

        render: function() {
            this.$el.html(this.template());
            return this;
        },

        events: {
            'click #item_add_btn': 'addItemListener',
            'click #hashtag_add_btn': 'addHashListener',
            'click #layer': 'layerListener',
            'click #question_create_btn': 'uploadQuestionListener',
            'click #question_create_back_btn': 'backToPage',
            'click .reset_dt': 'resetDateTimeListener',
            'click .media_add': 'addMediaListener',
            'change #voteUserCountMax': 'voteUserCountMaxListener',
            'click .mediaTypeCheck': 'checkMediaTypeListener',
            'click .imgSrcCheck': 'checkImgSrcListener',
            'click #media_upload': 'uploadMediaBtnListener',
            'keydown .itemTitle': 'limitItemInput'
        },
        
        initialsizeItemPopUp : function() {
            $('.media_add').popup({
                title: '미디어 추가',
                content: '이미지나 비디오를 추가하실 수 있습니다.'
            });
        },
        
        backToPage: function() {
            document.location = '/';
        },

        resetDateField: function(e) {
            var _this = e.target;
            $(_this).parent().siblings('.dateO').val('');
        },

        voteUserCountMaxListener: function(e) {
            var $target = $(e.target);
            var voteUserCount = $target.val();

            if (Number(voteUserCount) <= 0) {
                $target.val('');
                this.errorModalSetAndShow('1명 이상의 값만 설정하실 수 있습니다.');
            }
        },

        uploadMediaBtnListener: function(target) {
            var _target = $(target);
            var $mediaContent = _target.children('.mediaContent');
            var mediaTypeCheck = $('.mediaTypeCheck:checked').val();
            var imgSrcCheck = $('.imgSrcCheck:checked').val();
            var mediaPathCheck = $('.mediaPathCheck').val();
            var $mediaPath = _target.siblings('.media_info').children('.mediaPath');
            var $mediaTypeCode = _target.siblings('.media_info').children('.mediaTypeCode');
            var $mediaFile = _target.siblings('.media_info').children('.mediaFile');
            var $imagePreviewMain = $('<img class="previewImg" alt="preview section" src="#"/>');
            var $videoPreviewMain = $('<iframe class="previewVideo" frameborder="0" allowfullscreen></iframe>');
            var mediaFilePath = $mediaFile.val();
            this.initializeModal();
            $mediaTypeCode.val(0);
            $mediaPath.val('');
            if (Number(mediaTypeCheck) === MEDIA_TYPE_IMAGE &&
                this.imageTypeValidation(mediaTypeCheck, imgSrcCheck, mediaPathCheck, mediaFilePath)) {
                if (imgSrcCheck === 'url') {
                    $imagePreviewMain.attr('src', mediaPathCheck);
                    $mediaContent.html($imagePreviewMain);
                    $mediaPath.val(mediaPathCheck);
                    $mediaFile.val('');
                } else {
                    $mediaContent.children('img').css('display', 'initial');
                }
            } else if (Number(mediaTypeCheck) === MEDIA_TYPE_VIDEO) {
                $mediaFile.val('');
                mediaPathCheck = this.youtubeUrlConverter(mediaPathCheck);
                $videoPreviewMain.attr('src', mediaPathCheck);
                $mediaPath.val(mediaPathCheck);
                $mediaContent.html($videoPreviewMain);
            } else {
                $mediaContent.empty();
                $mediaFile.val('');
                 return false;
            }
            $mediaTypeCode.val(mediaTypeCheck);
        },

        imageTypeValidation: function(mediaTypeCheck, imgSrcCheck, mediaPathCheck, mediaFilePath) {
            var fileExtension = '';

            var urlPathChecker = function(path) {
                var ext = '';
                if (path.lastIndexOf('.') === -1) {
                    if (path.indexOf('http') !== -1 || path.indexOf('www.') !== -1) {
                        return true;
                    }
                } else {
                    ext = '';
                    path.substring(path.lastIndexOf('.') + 1);
                    if ($.inArray(ext, ALLOWED_MEDIA_TYPES)) {
                        return true;
                    }
                }
                return false;
            };

            if (Number(mediaTypeCheck) === MEDIA_TYPE_IMAGE) {             // Image
                fileExtension = mediaFilePath.slice(mediaFilePath.lastIndexOf('.'));
                fileExtension = fileExtension.trim();
                fileExtension = fileExtension.toLowerCase();

                if (imgSrcCheck === 'url') {
                    if (urlPathChecker(mediaPathCheck.trim())) {
                        return true;
                    }
                    this.errorModalSetAndShow('올바른 URL을 입력해주세요. 유효하지 않은 URL : \"' + mediaPathCheck+'\"');
                } else if (imgSrcCheck === 'file') {
                    if ($.inArray(fileExtension, ALLOWED_MEDIA_TYPES) >= 0) {
                        return true;
                    }
                    this.errorModalSetAndShow('파일 확장자가 잘못되었습니다. 다시 확인해주세요.');
                    return false;
                }
            }

            return false;
        },

        youtubeUrlConverter: function(mediaPathCheck) {
            var $tempIframe;
            if (mediaPathCheck.indexOf('iframe') > 0) {
                $tempIframe = $(mediaPathCheck);
                mediaPathCheck = $tempIframe.attr('src');
            } else if (mediaPathCheck.indexOf('watch') > 0) {
                mediaPathCheck = 'https://www.youtube.com/embed/' +
                                  mediaPathCheck.slice(mediaPathCheck.lastIndexOf('watch?v=') + 8);
            } else if (mediaPathCheck.indexOf('youtu.be') > 0) {
                mediaPathCheck = mediaPathCheck.replace('youtu.be/', 'www.youtube.com/embed/');
            }
            return mediaPathCheck;
        },

        initializeModal: function() {
            $('.mediaTypeCheck[value="1"]').prop('checked', true);
            $('.mediaTypeCheck[value="2"]').prop('checked', false);

            $('.imgSrcCheck[value="url"]').prop('checked', true);
            $('.imgSrcCheck[value="file"]').prop('checked', false);
            $('.mediaPathCheck').val('').attr('placeholder', 'http://www.oneq.com/img/oneq.png');
            $('.imgSection').css('display', 'flex');
            $('.videoSection').css('display', 'none');
            $('.fileSection').css('display', 'none');
            $('.urlSection').css('display', 'inline');
        },

        addMediaListener: function(ev) {
            var _viewObject = this;              // QuestionCreateView Object
            var target = ev.currentTarget;        // 미디어 추가 버튼을 누른 DOM
            var $target = $(target);
            
            var mediaPath = $(target).siblings('.media_info').children('.mediaPath');
            var mediaFile = $(target).siblings('.media_info').children('.mediaFile');
            var mediaTypeCode = $(target).siblings('.media_info').children('.mediaTypeCode');
            
            if(mediaPath.val() !== '' || mediaFile.val() !== '' || Number(mediaTypeCode.val()) !== 0){
                if(!confirm("이미 추가된 미디어가 있어요. 다른 미디어를 올리시려구요?")){
                    $(target).children('.mediaContent').empty();
                    return false;
                }
            }
            $('#modal').modal({
                onShow: function() {
                    var $file = $(target).siblings('.media_info').children('.mediaFile');
                    var $imagePreviewMain = $('<img style="display:none" ' +
                                                  'class="previewImg" ' +
                                                  'alt="preview section" src="#"/>');

                    $file.unbind('change');
                    $file.on('change', function() {
                        var mediaTypeCheck = $('.mediaTypeCheck:checked').val();
                        var imgSrcCheck = $('.imgSrcCheck:checked').val();
                        var _this = this;
                        var reader;

                        if (Number(mediaTypeCheck) !== 1) {
                            return false;
                        }

                        if (imgSrcCheck !== 'file') {
                            return false;
                        }

                        if ($(this).val() !== '') {
                            reader = new FileReader();
                            reader.onload = function(e) {
                                $imagePreviewMain.attr('src', e.target.result);
                                $(_this).parent().siblings('.media_add')
                                                 .children('.mediaContent').html($imagePreviewMain);
                            };
                            reader.readAsDataURL($(_this).get(0).files[0]);
                        }
                    });

                    $('#fileBtn').unbind('click');
                    $('#fileBtn').on('click', function() {
                        $file.trigger('click');
                    });

                    $('.mediaTypeCheck').unbind('click');
                    $('.mediaTypeCheck').on('click', function() {
                        _viewObject.checkMediaTypeListener(this);
                    });

                    $('.imgSrcCheck').unbind('click');
                    $('.imgSrcCheck').on('click', function() {
                        _viewObject.checkImgSrcListener(this);
                    });


                    $('#media_upload').unbind('click');
                    $('#media_upload').on('click', function() {
                        _viewObject.uploadMediaBtnListener(target);
                    });
                }
            }).modal('show');
            
            this.vibrateMobileDevice();
        },
        
        vibrateMobileDevice: function() {
            if(navigator && navigator.mozVibrate){
                navigator.mozVibrate(1000);
            }
            
            if(navigator && navigator.vibrate){
                navigator.vibrate(1000);
            }
        },

        uploadQuestionListener: function() {
            var _thisViewObj = this;
            var ajaxUrl = '/api/question';

            var questionTitle = $('#questionTitle').val().replace(/\</g, '&lt;').replace(/\>/g, '&gt;');
            var questionContent = $('#questionContent').val().replace(/\</g, '&lt;').replace(/\>/g, '&gt;').replace(/\n/g,'<br>');
            var questionTypeCode = $('input[name="questionTypeCode"]:checked').val();
            var questionMediaTypeCode = $('#questionMediaTypeCode').val() ? $('#questionMediaTypeCode').val() : 0;
            var questionMediaPath = $('#questionMediaPath').val().replace(/\</g, '&lt;').replace(/\>/g, '&gt;');
            var startAt = $('#startAt').val();
            var stopAt = $('#stopAt').val();
            var voteUserCountMax = $('#voteUserCountMax').val() ? $('#voteUserCountMax').val() : 0;
            var questionMediaPathFile = $('#questionMediaPathFile').val();
            var ajaxData = new FormData();
            var isValidItem;

            ajaxData.append('title', questionTitle);
            ajaxData.append('content', questionContent);
            ajaxData.append('questionTypeCode', questionTypeCode);
            ajaxData.append('mediaTypeCode', questionMediaTypeCode);
            ajaxData.append('mediaPath', questionMediaPath);
            if (startAt) {
                ajaxData.append('startAt', startAt);
            }
            if (stopAt) {
                ajaxData.append('stopAt', stopAt);
            }
            if (voteUserCountMax >= 0) {
                ajaxData.append('voteUserCountMax', voteUserCountMax);
            }

            if (questionMediaPathFile !== '') {
                ajaxData.append('questionMediaFile', $('#questionMediaPathFile').get(0).files[0]);
            }

            $('input[name="hashName[]"]').each(function(i) {
                var hashName = $(this).val()
                                      .replace(/\</g, '&lt;')
                                      .replace(/\>/g, '&gt;')
                                      .replace(/\'/g, "\\'")
                                      .replace(/\"/g, '\\"');
                ajaxData.append('hashName[' + i + ']', hashName);
            });

            isValidItem = true;
            $('#itemList .item').each(function(i) {
                var $_this = $(this);
                var itemTitle = $_this.find('.itemTitle').val().replace(/\</g, '&lt;').replace(/\>/g, '&gt;');
                var mediaTypeCode = $_this.find('.mediaTypeCode').val();
                var mediaPath = $_this.find('.mediaPath').val();
                var mediaFile = $_this.find('.mediaFile').val();

                ajaxData.append('itemTitle[' + i + ']', itemTitle);
                ajaxData.append('itemMediaTypeCode[' + i + ']', mediaTypeCode);
                ajaxData.append('itemMediaPath[' + i + ']', mediaPath);

                if (mediaFile !== '') {
                    ajaxData.append('itemMediaFiles[' + i + ']', $_this.find('.mediaFile').get(0).files[0]);
                }

                if (itemTitle === '') {
                    isValidItem = false;
                    return false;
                }
            });

            if (questionTitle.trim() === '') {
                this.errorModalSetAndShow('투표제목이 비어있네요 ! 제목은 필수랍니다. :)');
                return false;
            }

            if (!isValidItem) {
                this.errorModalSetAndShow('투표항목이 비어있네요 ! 채워주시겠어요? :)');
                return false;
            }

            if ($('#itemList .item').length < 2) {
                this.errorModalSetAndShow('항목을 적어도 2개 이상 넣어주셔야 투표가 됩니다!');
                return false;
            }
            
            $.ajax({
                url: ajaxUrl,
                type: 'POST',
                data: ajaxData,
                dataType: 'text',
                processData: false,
                contentType: false,
                beforeSend: function() {
                    $('#overlay').css({
                        'position': 'absolute',
                        'width': $(document).width(),
                        'height': $(document).height(),
                        'z-index': 99999,
                        'background-color': 'grey'
                    }).fadeTo(0, 0.8);
                },
                success: function(data) {
                    var responseData = JSON.parse(data);
                    var resultCode = responseData.header.resultCode;
                    var responseMsg;
                    var responseMsgJson;
                    
                    var errorString = '투표 등록이 실패하였습니다. 다시 시도해주세요.';
                    $('#overlay').remove();

                    if (resultCode === 200) {
                        document.location = '/#v/'+responseData.body.questionId;
                        return true;
                    }else if(resultCode === 406){
                        responseMsg = responseData.header.resultMessage;
                        responseMsgJson = JSON.parse(responseMsg);
                        _thisViewObj.handlingSubmitFails(responseMsgJson);
                    }else{
                        _thisViewObj.errorModalSetAndShow(errorString);
                    }
                    
                    return false;
                }
            });
        },
        
        handlingSubmitFails: function(bodyJson) {
            var ele;
            var errorString = '';
            for (ele in bodyJson) {
                errorString += ' * '+bodyJson[ele]+'<p/>';
            }
            
            if(errorString !== ""){
                this.errorModalSetAndShow(errorString);
            }
        },
        
        errorModalSetAndShow: function(errorString){
            $('#errorModal .errorModal').html(errorString);
            $('#errorModal').modal('show');
        },
        
        checkImgSrcListener: function(e) {
            var _this = e;
            var imgSrcCheck = $(_this).val();

            if (imgSrcCheck === 'url') {
                $('.urlSection').css('display', 'inline');
                $('.fileSection').css('display', 'none');
            } else {
                $('.urlSection').css('display', 'none');
                $('.fileSection').css('display', 'inline');
            }
        },

        checkMediaTypeListener: function(element) {
            var mediaTypeCode = $(element).val();

            if (Number(mediaTypeCode) === 1) { // image
                $('#modal .imgSection').show();
                $('#modal .videoSection').hide();
                $("#modal .urlSrc").prop("checked", true);
                $("#modal .fileSrc").prop("checked", false);
                $('#modal .mediaPathCheck').attr('placeholder', 'ex) http://www.oneq.com/img/oneq.png');
            } else {                  // video
                $('#modal .videoSection').show();
                $('#modal .imgSection').hide();
                $('#modal .fileSection').hide();
                $('#modal .urlSection').show();
                $('#modal .mediaPathCheck').attr('placeholder', 'ex) https://www.youtube.com/watch?v=vg-cWYvtLlk');
            }
        },
        resetDateTimeListener: function(e) {
            $(e.target).parent().siblings('.dateO').val('');
        },

        renderingMediaPreview: function($inputFile, $target) {
            var input = $inputFile.get(0);
            var reader;
            if (input.files && input.files[0]) {
                reader = new FileReader();
                reader.onload = function(e) {
                    $target.append('<img style="position: relative;top: 50%;' +
                                   'transform: translate(0%, -50%);max-height: 90%;" src="' + e.target.result + '"/>');
                };
                reader.readAsDataURL(input.files[0]);
            }
        },
        
        addItemListener: function(e) {
            mediator.publish('addItem');
            this.initialsizeItemPopUp();
            return this;            
        },

        addHashListener: function() {
            var hashName = $('#hashtag_add_text').val();
            //.replace(/\</g,'&lt;').replace(/\>/g,'&gt;').replace(/\'/g,"\\'").replace(/\"/g,'\\"');
            $('#hashtag_add_text').val('');
            mediator.publish('addHash', hashName);
            return this;
        },

        layerListener: function(e) {
            var $el = $(e.target);
            if ($el.hasClass('selectable')) {
                picker1.close();
                picker2.close();
            }
        },

        mediaValidation: function(mediaTypeCode, mediaPath, imgSrc) {
            var fileExtension = '';
            var $tempIframe;

            if (Number(mediaTypeCode) === MEDIA_TYPE_IMAGE) {
                fileExtension = mediaPath.slice(mediaPath.lastIndexOf('.'));
                fileExtension = fileExtension.trim();

                if (imgSrc === MEDIA_SRC_FILE) {
                    if ($.inArray(fileExtension, ALLOWED_MEDIA_TYPES) >= 0) {
                        return true;
                    }
                    return false;
                } else if (imgSrc === MEDIA_SRC_URL) {
                    return true;
                }
            } else if (Number(mediaTypeCode) === MEDIA_TYPE_VIDEO) {
                if (mediaPath.indexOf('iframe') > 0) {
                    $tempIframe = $(mediaPath);
                    mediaPath = $tempIframe.attr('src');
                } else if (mediaPath.indexOf('watch') > 0) {
                    mediaPath = 'https://www.youtube.com/embed/' +
                                 mediaPath.slice(mediaPath.lastIndexOf('watch?v=') + 8);
                } else if (mediaPath.indexOf('youtu.be') > 0) {
                    mediaPath = mediaPath.replace('youtu.be/', 'www.youtube.com/embed/');
                }
                return mediaPath;
            }
        },

        limitItemInput: function(event) {
            var targetString = event.target.value;
            var stringLength = targetString.length;
            var stringLengthByBytes = 0;
            var i, characterToHexadecimalLength;

            for (i = 0; i < stringLength; i = i + 1) {
                characterToHexadecimalLength = escape(targetString.charAt(i)).length;

                if (Number(characterToHexadecimalLength) === 6) {
                    stringLengthByBytes = stringLengthByBytes + 2;
                } else {
                    stringLengthByBytes = stringLengthByBytes + 1;
                }
            }

            if (stringLengthByBytes > 40) {
                event.target.value = targetString.slice(0, -1);
            }
        }
    });

    return QuestionCreateView;
});
