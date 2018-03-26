<!DOCTYPE html>
<html lang="en">

<head>
  <#include "header.ftl">
</head>

<body>

  <#include "top-nav.ftl">

  <div class="main-container container">
    <div class="row">
      <#include "sidebar.ftl">
        <div class="col-md-9">
          <#include "blog-posts.ftl">
        </div>
    </div>
  </div>

  <#include "footer.ftl">
  <#include "scripts.ftl">

</body>

</html>