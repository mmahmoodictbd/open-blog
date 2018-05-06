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
                    <input name="title" type="text" class="form-control" id="title" placeholder="Title">
                </div>

                <div class="form-group">
                    <textarea name="content" class="form-control summernote" data-toolbar="full"></textarea>
                </div>

            </div>

            <div class="col-md-3">

                <div class="form-group">
                    <button name="action" value="DRAFT" type="submit" class="btn btn-secondary btn-sm">Save draft
                    </button>
                    <!--<button name="action" value="preview" type="submit" class="btn btn-secondary
                    btn-sm">Preview</button>-->
                    <button name="action" value="PUBLISH" type="submit" class="btn btn-primary btn-sm">Publish</button>
                </div>

                <div id="categoryDropdown" class="form-group">
                    <div class="dropdown show">
                        <a id="categoryDropdownBtn" class="btn btn-light btn-sm dropdown-toggle w-100"
                           href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Categories</a>

                        <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                            <a class="dropdown-item" href="#" data-value="programming">Programming</a>
                            <a class="dropdown-item" href="#" data-value="java">-Java</a>
                            <a class="dropdown-item" href="#" data-value="spring-boot">--Spring Boot</a>
                        </div>

                        <input type="hidden" id="categories" name="categories"/>
                    </div>
                </div>

                <div class="form-group">
                    <input name="tags" type="text" class="form-control" value="" data-role="tagsinput"
                           placeholder="Add tags"/>
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

        $('#categoryDropdown .dropdown-menu a').click(function () {
            var selectedOption = $(this).attr('data-value');
            $('#categories').val(selectedOption);
            $('#categoryDropdownBtn').text(selectedOption);
        });

        function uploadImage(image) {
            var data = new FormData();
            data.append("image", image);
            $.ajax({
                url: '/admin/post',
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