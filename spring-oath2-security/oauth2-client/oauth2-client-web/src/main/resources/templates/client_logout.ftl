<html>
<head>
    <meta charset="utf-8" />
    <title>Demo</title>
    <link rel="stylesheet" type="text/css" href="/client/libs/css/bootstrap-3.3.2.css" />
    <script>
        function submitWithTimeout() {
            console.log('Triggered page load listener');
            setTimeout(function() {
                document.forms['logoutUserConfirm'].submit();
            }, 5000);
        }
    </script>
</head>
<body onload="submitWithTimeout()">
    <h1>Client Logout</h1>
    <div>You've been logged out successfully</div>
    <div>Wait 5 sec to be redirected or press the Submit button</div>
    <form role="form" id="logoutUserConfirm" action="/client/mylogout" method="POST">
        <button id="confirmLogoutBtn" type="submit" class="btn btn-primary">Submit</button>
        <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</body>
</html>