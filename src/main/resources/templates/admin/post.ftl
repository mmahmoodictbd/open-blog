<!DOCTYPE html>
<html lang="en" style="height: 100%">
<head>
    <title>UnloadBrain - Write a post</title>
    <#include "header.ftl">
    <link href="/webjars/bootstrap-tagsinput/bootstrap-tagsinput.css" rel="stylesheet">
</head>

<body>

    <#include "top-nav.ftl">

<div class="container">

    <form action="/admin/post" method="post">

        <div class="row mt-5 pt-5">

            <div class="col-md-9">

                <div class="form-group">
                    <input name="title" type="text" class="form-control" id="title"
                           placeholder="Title" value="${(post.title)!}">
                </div>

                <div class="form-group">
                    <input name="summary" type="text" class="form-control" id="summary"
                           placeholder="Summary" value="${(post.summary)!}">
                </div>

                <div class="form-group">
                    <textarea name="content" class="form-control summernote"
                              data-toolbar="full">${(post.content)!}</textarea>
                </div>

                <input type="hidden" id="id" name="id" value="${(post.id)!}"/>

                <#if (post.status)! == "DRAFT">
                    <input type="hidden" id="status" name="status" value="DRAFT"/>
                <#else>
                    <input type="hidden" id="status" name="status" value="PUBLISHED"/>
                </#if>


            </div>

            <div class="col-md-3">

                <div class="form-group">
                    <select name="categories" class="form-control">
                        <option>ROOT</option>
                        <#list categories as cat>
                            <!-- TODO:: split comma separated post.categories and check with cat.name -->
                            <#if cat.name == (post.categories)!"">
                                <option value="${cat.name}" selected="selected">${cat.name}</option>
                            <#else>
                                <option value="${cat.name}">${cat.name}</option>
                            </#if>
                        </#list>
                    </select>
                </div>

                <div class="form-group">
                    <input name="tags" type="text" class="form-control" value="${(post.tags)!}" data-role="tagsinput"
                           placeholder="Add tags"/>
                </div>


                <div class="form-group">
                    <button name="action" value="DRAFT" type="submit" class="btn btn-secondary btn-sm">Save draft
                    </button>
                    <!--<button name="action" value="preview" type="submit" class="btn btn-secondary
                    btn-sm">Preview</button>-->
                    <button name="action" value="PUBLISH" type="submit" class="btn btn-primary btn-sm">Publish</button>
                </div>

            </div>

        </div>

    </form>
</div>

<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/webjars/bootstrap-tagsinput/bootstrap-tagsinput.min.js"></script>
<script src="/webjars/summernote/dist/summernote-bs4.min.js"></script>

<script>

    $(document).ready(function () {

        $('.summernote').summernote({
            placeholder: 'Happy blogging :-)',
            height: 400,
            callbacks: {
                onImageUpload: function (image) {
                    uploadImage(image[0]);
                },
                onInit: function () {

                    var fileGroup = '<button id="codeblockButton" type="button" ' +
                            'class="note-btn btn btn-light btn-sm"><i class="far fa-file-code"></i></button>';
                    $(fileGroup).prependTo($('.note-codebutton'));


                },
                onPaste: function (e) {
                    var bufferText = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('Text');
                    e.preventDefault();
                    document.execCommand('insertText', false, bufferText);
                }
            },
            toolbar: [
                ['style', ['bold', 'italic', 'underline', 'clear']],
                // ['font', ['fontname']],
                ['fontsize', ['fontsize']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['table', ['table']],
                ['height', ['height']],
                ['insert', ['link', 'picture', 'video', 'hr']],
                ['view', ['fullscreen', 'codeview']],
                ['codebutton', ['codeblock']]
            ],
            buttons: {
                codeblock: CodeBlockButton
            }

        });

        var CodeBlockButton = function (context) {
            var ui = $.summernote.ui;
            var button = ui.button({
                contents: '<i class="fa fa-css3"\/> hello',
                tooltip: 'add code block',
                click: function () {
                    console.log('hello!');
                    context.invoke('editor.insertText', 'hello');
                }
            });
            return button.render();
        };

        $('#codeblockButton').click(function (event) {
            var demoCode = document.createTextNode("Hello World");
            var codeNode = document.createElement('code');
            codeNode.className = 'language-java';
            codeNode.appendChild(demoCode);
            var preNode = document.createElement('pre');
            preNode.appendChild(codeNode);
            preNode.className = 'line-numbers';
            var pNode = document.createElement('p');
            pNode.appendChild(preNode);
            $('.summernote').summernote("editor.insertNode", pNode);
        });

        function uploadImage(image) {
            var data = new FormData();
            data.append("file", image);
            $.ajax({
                url: '/admin/files',
                cache: false,
                contentType: false,
                processData: false,
                data: data,
                type: "post",
                success: function (uploadResponse) {
                    var image = $('<img>').attr('src', '../' + uploadResponse.url);
                    $('.summernote').summernote("insertNode", image[0]);
                },
                error: function (data) {
                    console.log(data);
                }
            });
        }

    });


</script>

</body>

</html>