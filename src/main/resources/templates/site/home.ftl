<!DOCTYPE html>
<html lang="en">

<head>
  <#include "header.ftl">
</head>

<body>

    <#include "top-nav.ftl">

    <div class="main-container container">
        <div class="row">

            <div class="col-md-2">
                <#include "sidebar.ftl">
            </div>

            <div class="col-md-8">
                <#include "posts.ftl">
            </div>

        </div>
    </div>

    <#include "footer.ftl">
    <#include "scripts.ftl">

</body>

</html>