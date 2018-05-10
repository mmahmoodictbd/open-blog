<!DOCTYPE html>
<html lang="en" style="height: 100%">
<head>
    <title>UnloadBrain - Post List</title>
    <#include "header.ftl">
</head>

<body>

<#include "top-nav.ftl">

<div class="container">

    <div class="row mt-5">
        <h2>Posts</h2>
    </div>

    <div class="row mt-5 pt-1">

        <div class="col-md-12">

            <div class="form-group">
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col" class="col-sm-8">Title</th>
                        <th scope="col" class="col-sm-4">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list posts as post>
                    <tr>
                        <#if (post.status)! == "DRAFT">
                            <td title="${post.id}" class="text-warning">${post.title}</td>
                        <#else>
                            <td title="${post.id}">${post.title}</td>
                        </#if>

                        <td><a href="/admin/post?id=${post.id}&status=${post.status}" target="_blank">edit</a></td>
                    </tr>
                    </#list>

                    </tbody>
                </table>
            </div>

        </div>

    </div>
</div>

<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>

</body>

</html>