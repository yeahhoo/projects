<html>
    <#include "/head-template.ftl">
<body onload="submitWithTimeout()">
    <h1>Client Logout</h1>
    <div>You've been logged out successfully</div>
    <div>Wait 5 sec to be redirected or press the Submit button</div>
    <form role="form" id="logoutUserConfirm" action="/client/mylogout" method="POST">
        <button id="confirmLogoutBtn" type="submit" class="btn btn-primary">Submit</button>
        <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</body>
<script>
    function submitWithTimeout() {
        console.log('Triggered page load listener');
        setTimeout(function() {
            document.forms['logoutUserConfirm'].submit();
        }, 5000);
    }
</script>
</html>