<!DOCTYPE html>
<html lang="en">

<head>
    <#include "../common/header.ftl">
</head>

<body>

    <#include "../common/site-banner.ftl">

<div class="main-container container">
    <div class="row">

        <div class="col-md-8">
                <#include "home-posts.ftl">
        </div>

        <div class="col-md-4">
                <#include "sidebar.ftl">
        </div>
    </div>
</div>

    <#include "../common/footer.ftl">
    <#include "../common/scripts.ftl">

</body>

</html>