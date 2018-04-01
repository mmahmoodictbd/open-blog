<!DOCTYPE html>
<html lang="en">

<head>
    <#include "../common/header.ftl">
    <title>Unload Brain</title>
    <meta name="description" content="">
    <meta name="author" content="">
</head>

<body>

    <#include "../common/site-banner.ftl">
    <#include "top-nav.ftl">

    <div class="main-container container">
        <div class="row">

            <div class="col-md-2">
                <#include "sidebar.ftl">
            </div>

            <div class="col-md-8">
                <#include "home-posts.ftl">
            </div>

        </div>
    </div>

    <#include "../common/footer.ftl">
    <#include "scripts.ftl">

</body>

</html>