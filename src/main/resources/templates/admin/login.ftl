<!DOCTYPE html>
<html lang="en" style="height: 100%">
<head>
    <title>UnloadBrain - Login</title>
    <#include "header.ftl">
</head>

<body style="height: 100%">

    <div class="container h-100">

        <div class="row align-items-center h-100">

            <form role="form" action="/login" method="post" class="col-4 mx-auto">
                <#--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>-->

                <div class="form-group">
                    <label for="email">Username</label>
                    <input name="username" id="username" class="form-control" required autofocus/>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" name="password" id="password" class="form-control" required/>
                </div>

                <div class="form-group">
                    <button type="submit" class="form-control">Signin</button>
                </div>

                <div class="form-group">
                    <#if error.isPresent()>
                        <p>The email or password you have entered is invalid, try again.</p>
                    </#if>
                </div>
            </form>


        </div>
    </div>

</body>
</html>

