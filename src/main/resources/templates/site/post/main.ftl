<!DOCTYPE html>
<html lang="en">

<head>
    <#include "../common/header.ftl">
    <title>Unload Brain</title>
    <meta name="description" content="">
    <meta name="author" content="">
    <link href="/webjars/prism/1.12.2/themes/prism.css" rel="stylesheet">
    <base href="..">
</head>

<body>

    <#include "../common/site-banner.ftl">

<div class="main-container container">

    <div class="row">

        <div class="col-md-9">

            <div class="mb-5">
                <h1>${(post.title)!}</h1>
                <#include "meta.ftl">
            </div>

        ${(post.content)!}

        </div>


        <div class="col-md-3">

        </div>

    </div>
</div>

<br><br>

    <#include "../common/footer.ftl">
    <#include "../common/scripts.ftl">

</body>

</html>