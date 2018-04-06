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

    <div class="row mt-5 pt-5">

        <div class="col-md-9">
            <form method="post">

                <div class="form-group">
                    <input type="text" class="form-control" id="title" placeholder="Title">
                </div>

                <div class="form-group">
                    <textarea name="content" class="form-control summernote" data-toolbar="full"></textarea>
                </div>


            </form>
        </div>

        <div class="col-md-3">

            <div class="form-group">
                <button type="button" class="btn btn-secondary btn-sm">Save draft</button>
                <button type="button" class="btn btn-secondary btn-sm">Preview</button>
                <button type="button" class="btn btn-primary btn-sm">Publish</button>
            </div>

            <div class="form-group">
                <div class="dropdown show">
                    <a class="btn btn-light btn-sm dropdown-toggle w-100" href="#" role="button" id="categories"
                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Categories</a>

                    <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                        <a class="dropdown-item" href="#">Action</a>
                        <a class="dropdown-item" href="#">Another action</a>
                        <a class="dropdown-item" href="#">Something else here</a>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <input type="text" class="form-control" value="" data-role="tagsinput" placeholder="Add tags"/>
            </div>
        </div>

    </div>
</div>

<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/webjars/bootstrap-tagsinput/bootstrap-tagsinput.min.js"></script>
<script src="/webjars/summernote/dist/summernote-bs4.min.js"></script>
<script>
    $(document).ready(function () {

        $('.summernote').summernote({
            placeholder: 'Happy blogging :-)',
            height: 400
        });
    });
</script>

</body>

</html>