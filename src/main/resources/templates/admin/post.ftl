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
                    <input name="tags" type="text" class="form-control" value="" data-role="tagsinput"
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
                }
            },
            toolbar: [
                ['style', ['bold', 'italic', 'underline', 'clear']],
                ['font', ['fontname']],
                ['fontsize', ['fontsize']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['table', ['table']],
                ['height', ['height']],
                ['insert', ['link', 'picture', 'video', 'hr']],
                ['view', ['fullscreen', 'codeview']]
            ]
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
                success: function (url) {
                    url = 'www.w3schools.com/howto/img_fjords.jpg'
                    var image = $('<img>').attr('src', 'https://' + url);
                    $('.summernote').summernote("insertNode", image[0]);
                    console.log('hellO!')
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