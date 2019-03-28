<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<div class="content">
    <div class="floated header">
        <div class="ui form">
            <div class="required field">
                <label>투표 제목</label> <input type="text"
                    id="questionTitle" name="questionTitle"
                    placeholder="ex) 오늘 저녁으로 가장 먹고싶은 것은!?"
                    maxlength="80">
            </div>
        </div>
    </div>
    <div class="description">
        <div class="floated meta">
            <div class="ui form">
                <div class="three fields">
                    <div class="field">
                        <label>투표시작일<i style="cursor: pointer"
                            class="remove red icon reset_dt"></i></label> <input
                            class="dateO" id="startAt" name="startAt"
                            type="text" placeholder="ex) 2016-02-01"
                            readonly />
                    </div>
                    <div class="field">
                        <label>투표마감일<i style="cursor: pointer"
                            class="remove red icon reset_dt"></i></label> <input
                            class="dateO" id="stopAt" name="stopAt"
                            type="text" placeholder="ex) 2016-03-01"
                            readonly />
                    </div>
                    <div class="field">
                        <label>최대 참여자수<i style="cursor: pointer"
                            class="remove red icon reset_dt"></i></label> <input
                            id="voteUserCountMax"
                            name="voteUserCountMax" type="number"
                            placeholder="ex) 300명" min="1">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="content">
    <div class="description">
        <div class="ui form">
            <div class="field">
                <label>미디어 추가</label>

                <div id="question_media_add"
                    class="ui fluid button media_add"
                    style="background: transparent; border: 1px black dashed; padding: 0px; height: 300px;">
                    <div class="mediaContent"
                        style="height: 300px; width: 100%;"></div>
                    <span
                        style="font-size: 80px; position: relative; top: -150px;"><i
                        class="upload icon"></i></span>
                </div>
                <div class="media_info question_media_info">
                    <input class="mediaTypeCode" type="hidden"
                        id="questionMediaTypeCode"
                        name="questionMediaTypeCode" value="0" /> <input
                        class="mediaPath" type="hidden"
                        id="questionMediaPath" name="questionMediaPath"
                        value="" maxlength="1000" /> <input class="mediaFile" type="file"
                        id="questionMediaPathFile"
                        name="questionMediaPathFile" />
                </div>
            </div>
        </div>
    </div>
</div>

<div class="content">
    <div class="description">
        <div class="ui form">
            <div class="field">
                <label>투표 설명</label>
                <textarea id="questionContent" name="questionContent"
                    placeholder="ex) 우리가 어떤 민족입니까!!! 밤잠보다 밤참이 많은 민족이지않습니까!"></textarea>
            </div>
        </div>
    </div>
</div>

<div class="content">
    <div class="description">
        <div class="ui form">
            <div class="field">
                <label>해시 태그</label>
            </div>
            <div class="fields">
                <div class="twelve wide field">
                    <input id="hashtag_add_text" type="text"
                        placeholder="해시태그가 없으면 검색되지 않습니다 ex) #점심 #저녁" />
                </div>
                <div class="four wide field">
                    <button type="button" id="hashtag_add_btn"
                        class="ui fluid red button">추가</button>
                </div>
            </div>

            <div id="hashList" class="field"></div>
        </div>
    </div>
</div>


<div class="content">
    <div class="ui form">
        <div class="field">
            <label>투표 종류</label>
        </div>
        <!-- class="hidden" -->
        <div class="inline fields">
            <div class="four wide field">
                <div class="ui radio checkbox">
                    <input type="radio" name="questionTypeCode"
                        value="0" checked="" /> <label>단답형</label>
                </div>
            </div>
            <div class="four wide field">
                <div class="ui radio checkbox">
                    <input type="radio" name="questionTypeCode"
                        value="1" /> <label>복수형</label>
                </div>
            </div>
            <div class="four wide field notServiced">
                <div class="ui radio checkbox" class="ui avatar image">
                    <input type="radio" name="questionTypeCode"
                        value="2" disabled /> <label>순위형</label>
                </div>
            </div>
            <div class="four wide field notServiced">
                <div class="ui radio checkbox" class="ui avatar image">
                    <input type="radio" name="questionTypeCode"
                        value="3" disabled /> <label>VS</label>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="question_create_items" class="ui content">
    <div class="description">
        <div class="ui form">
            <div class="ui field">
                <div class="ui left labeled button" tabindex="0">
                    <span class="ui basic right pointing label">투표 항목</span>
                    <button id="item_add_btn"class="ui red button">추가</button>
                </div>
                
                <div id="itemList"></div>

            </div>
        </div>
    </div>
</div>

<div class="extra content">
    <div class="ui two buttons">
        <button id="question_create_btn" class="ui primary button">투표
            등록</button>
        <button id="question_create_back_btn" class="ui button">취소</button>
    </div>
</div>

<div id="errorModal" class="ui basic modal small">
    <i class="close icon"></i>
    <div class="header">SOMETHING IS WRONG !</div>
    <div class="content errorModal"></div>
</div>


<div id="modal" class="ui modal small">
    <i class="close icon"></i>
    <div class="header">미디어 추가</div>
    <div class="content">
        <div class="ui form media_type">
            <div class="inline fields">
                <label>Media Type</label>
                <div class="field">
                    <div class="ui radio checkbox">
                        <input type="radio" class="mediaTypeCheck"
                            name="mediaTypeCheck" value="1" checked>
                        <label>Image</label>
                    </div>
                </div>
                <div class="field">
                    <div class="ui radio checkbox">
                        <input type="radio" class="mediaTypeCheck"
                            name="mediaTypeCheck" value="2"> <label>Video</label>
                    </div>
                </div>
            </div>
        </div>

        <div class="ui form mediaPath">
            <div class="inline fields imgSection">
                <label>Image Path</label>
                <div class="field">
                    <div class="ui radio checkbox">
                        <input type="radio" class="imgSrcCheck urlSrc"
                            name="imgSrcCheck" value="url" checked /> <label>URL</label>
                    </div>
                </div>
                <div class="field">
                    <div class="ui radio checkbox">
                        <input type="radio" class="imgSrcCheck fileSrc"
                            name="imgSrcCheck" value="file" /> <label>File</label>
                    </div>
                </div>
            </div>


            <div class="videoSection inline fields"
                style="display: none">
                <label>Youtube Path</label>
            </div>


            <div id="mediaPathSection">
                <div class="urlSection">
                    <input type="text" class="mediaPathCheck"
                        name="mediaPathCheck"
                        placeholder="ex) http://www.oneq.com/img/oneq.png" maxlength="1000" />
                </div>
                <div class="fileSection">
                    <button id="fileBtn" class="ui button red">FILE
                        UPLOAD</button>
                </div>
            </div>
        </div>
    </div>
    <div class="actions">
        <div class="ui black deny button">취소</div>
        <div id="media_upload"
            class="ui positive right labeled icon button">
            업로드 <i class="checkmark icon"></i>
        </div>
    </div>
</div>

<div id="layer" class="layer" style="display: none;">
    <div class="calendar-header">
        <a href="#" class="rollover calendar-btn-prev-year">이전년도</a> <a
            href="#" class="rollover calendar-btn-prev-month">이전달</a> <strong
            class="calendar-title"></strong> <a href="#"
            class="rollover calendar-btn-next-month">다음달</a> <a href="#"
            class="rollover calendar-btn-next-year">다음년도</a>
    </div>
    <div class="calendar-body">
        <table cellspacing="0" cellpadding="0">
            <thead>
                <tr>
                    <th class="sun">S</th>
                    <th>M</th>
                    <th>T</th>
                    <th>W</th>
                    <th>T</th>
                    <th>F</th>
                    <th class="sat">S</th>
                </tr>
            </thead>
            <tbody>
                <tr class="calendar-week">
                    <td class="calendar-date"></td>
                    <td class="calendar-date"></td>
                    <td class="calendar-date"></td>
                    <td class="calendar-date"></td>
                    <td class="calendar-date"></td>
                    <td class="calendar-date"></td>
                    <td class="calendar-date"></td>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="calendar-footer">
        <p>
            오늘 <em class="calendar-today"></em>
        </p>
    </div>
</div>