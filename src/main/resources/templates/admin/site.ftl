<!DOCTYPE html>
<html lang="en" style="height: 100%">
<head>
    <title>Update site</title>
    <#include "header.ftl">
    <link href="/webjars/bootstrap-tagsinput/bootstrap-tagsinput.css" rel="stylesheet">
</head>

<body>

    <#include "top-nav.ftl">

<div class="container">

    <div class="row mt-5">
        <h2>Update site info</h2>
    </div>

    <form action="/admin/site" method="post">

        <div class="row mt-5 pt-5">

            <div class="col-md-9">

                <div class="form-group">
                    <label for="name">Site Name</label>
                    <input name="name" type="text" class="form-control" id="name"
                           placeholder="name" value="${(site.name)!}">
                </div>

                <div class="form-group">
                    <label for="description">Site Description</label>
                    <textarea name="description" class="form-control summernote"
                              data-toolbar="full">${(site.description)!}</textarea>
                </div>

                <div class="form-group">
                    <label for="siteUrl">Site URL</label>
                    <input name="siteUrl" type="text" class="form-control" id="siteUrl"
                           placeholder="siteUrl" value="${(site.siteUrl)!}">
                </div>

                <div class="form-group">
                    <label for="homeUrl">Home URL</label>
                    <input name="homeUrl" type="text" class="form-control" id="homeUrl"
                           placeholder="homeUrl" value="${(site.homeUrl)!}">
                </div>

                <div class="form-group">
                    <label for="additionalProperties['keywords']">Keywords</label>
                    <input name="additionalProperties['keywords']" type="text" class="form-control" id="keywords"
                           placeholder="Keywords" value="${(site.additionalProperties['keywords'])!}">
                    <small id="additionalPropertiesKeywordsHelp" class="form-text text-muted">Used in meta tags.</small>
                </div>


            </div>

            <div class="col-md-3">

                <div class="form-group">
                    <button type="submit" class="btn btn-secondary btn-sm">Save</button>
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
            placeholder: 'description',
            height: 100,
            callbacks: {
                onPaste: function (e) {
                    var bufferText = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('Text');
                    e.preventDefault();
                    document.execCommand('insertText', false, bufferText);
                }
            },
            toolbar: [
                ['view', ['fullscreen', 'codeview']],
            ]

        });
    });


</script>

</body>

</html>