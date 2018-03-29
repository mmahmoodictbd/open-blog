<!DOCTYPE html>
<html lang="en" style="height: 100%">
<head>
    <meta charset="utf-8">
    <title>UnloadBrain - Write a post</title>

    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="/webjars/font-awesome/on-server/css/fontawesome-all.min.css" rel="stylesheet">
    <link href="/webjars/summernote/dist/summernote-bs4.css" rel="stylesheet">
    <link href="/static/site/base.css" rel="stylesheet">
    <link href="/static/site/admin.css" rel="stylesheet">
</head>

<body>

    <div class="container">

        <div class="row mt-5 pt-5">

            <#--<div class="col-md-2">-->

            <#--</div>-->

            <div class="col-md-10">
                <form method="post">

                    <div class="form-group">
                        <label for="title">Title </label>
                        <input type="text" class="form-input" id="title">
                    </div>

                    <div class="form-group">
                        <textarea name="content" class="form-input summernote" data-toolbar="full"></textarea>
                    </div>

                    <button type="submit" class="btn btn-secondary">Publish</button>
                    <button type="submit" class="btn btn-secondary">Save draft</button>

                </form>
            </div>

        </div>
    </div>

    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>

    <script src="/webjars/summernote/dist/summernote-bs4.min.js"></script>
    <script>
        $(document).ready(function() {

            $('.summernote').summernote({
                placeholder: 'Happy blogging :-)',
                height: 400,
                width: 700
            });
        });
    </script>

</body>

</html>