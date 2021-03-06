<!DOCTYPE html>
<html lang="en">

<head>
    <#include "../common/header.ftl">
    <title>${site.name}</title>
    <meta name="description" content="${site.description}">
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

            <#include "disqus-comment.ftl">

        </div>


        <div class="col-md-3">

        </div>

    </div>
</div>

<br><br>

    <#include "../common/footer.ftl">
    <#include "../common/scripts.ftl">
    <#include "../common/google-analytics.ftl">
    <script id="dsq-count-scr" src="//${site.disqusUniqueUrl}/count.js" async></script>

</body>

</html>