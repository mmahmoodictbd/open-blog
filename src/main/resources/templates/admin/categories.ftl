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
                        <th scope="col">#</th>
                        <th scope="col">First</th>
                        <th scope="col">Last</th>
                        <th scope="col">Handle</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <th scope="row">1</th>
                        <td>Mark</td>
                        <td>Otto</td>
                        <td>@mdo</td>
                    </tr>
                    <tr>
                        <th scope="row">2</th>
                        <td>Jacob</td>
                        <td>Thornton</td>
                        <td>@fat</td>
                    </tr>
                    <tr>
                        <th scope="row">3</th>
                        <td>Larry</td>
                        <td>the Bird</td>
                        <td>@twitter</td>
                    </tr>
                    </tbody>
                </table>
            </div>

        </div>

        <div class="col-md-3">

            <form action="/admin/categories" method="post">

                <div class="form-group">
                    <select class="form-control">
                        <option>Parent category</option>
                    </select>
                </div>

                <input type="hidden" id="id" name="id" value=""/>

                <div class="form-group">
                    <input type="text" class="form-control" id="category-name" name="name" placeholder="Category name">
                </div>

                <div class="form-group">
                    <input type="text" class="form-control" id="category-slug" name="slug" placeholder="Category slug">
                </div>

                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-sm float-right">Create</button>

                    <button type="submit" class="btn btn-primary btn-sm float-right">Update</button>

                </div>

            </form>

        </div>

    </div>
</div>

<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>

</body>

</html>