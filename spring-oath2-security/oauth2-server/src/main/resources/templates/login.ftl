<html>
<head>
    <link rel="stylesheet" href="/server/libs-css/bootstrap-3.3.2.css"/>
</head>
<body>
<#if RequestParameters['auth_error']??>
    <div class="alert alert-danger">
        There was a problem logging in. Please try again.
    </div>
</#if>
<div class="container">
    <h2>Custom, Please Login</h2>
    <form role="form" action="login" method="post">
        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" class="form-control" id="username" name="username"/>
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" class="form-control" id="password" name="password"/>
        </div>
        <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
</div>
</body>
</html>