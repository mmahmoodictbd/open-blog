<!DOCTYPE html>
<html lang="en" style="height: 100%">
<head>
    <title>UnloadBrain - Category</title>
    <#include "header.ftl">
</head>

<body>

    <#include "top-nav.ftl">

<div class="container">

    <div class="row mt-5">
        <h2>Categories</h2>
    </div>

    <div class="row mt-5 pt-1">

        <div class="col-md-9">

            <div class="form-group">
                <table class="table">
                    <thead>
                        <tr>
                            <th scope="col">Name</th>
                            <th scope="col">Slug</th>
                            <th scope="col">Parent</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                    <#list categories as category>
                        <tr>
                            <td>${category.name}</td>
                            <td>${category.slug}</td>
                            <td>${(category.parent)!}</td>
                            <td><a href="/admin/categories?id=${category.id}">edit</a></td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>

        </div>

        <div class="col-md-3">

            <form action="/admin/categories" method="post">

                <div class="form-group">
                    <select name="parent" class="form-control">
                        <option>ROOT</option>
                        <#list categories as cat>

                            <#if cat.name == (category.parent)!"">
                                <option value="${cat.name}" selected="selected">${cat.name}</option>
                            <#else>
                                <option value="${cat.name}">${cat.name}</option>
                            </#if>

                        </#list>
                    </select>
                </div>

                <input type="hidden" id="id" name="id" value="${(category.id)!}"/>

                <div class="form-group">
                    <input type="text" class="form-control" id="category-name" name="name"
                           placeholder="Category name" value="${(category.name)!}">
                </div>

                <div class="form-group">
                    <input type="text" class="form-control" id="category-slug" name="slug"
                           placeholder="Category slug" value="${(category.slug)!}">
                </div>

                <div class="form-group">

                    <#if (category.id)??>
                        <button type="submit" class="btn btn-primary btn-sm float-right">Create</button>
                    <#else>
                        <button type="submit" class="btn btn-primary btn-sm float-right">Update</button>
                    </#if>

                </div>

            </form>

        </div>

    </div>
</div>

<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>

</body>

</html>