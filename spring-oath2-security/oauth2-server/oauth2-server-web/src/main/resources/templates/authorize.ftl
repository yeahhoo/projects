<html>
<head>
    <link rel="stylesheet" href="/server/libs-css/bootstrap-3.3.2.css"/>
</head>
<body>
<div class="container">
    <h2>Please Confirm</h2>
    <form id="confirmationForm" name="confirmationForm" action="../oauth/authorize" method="post">
        <input name="user_oauth_approval" value="true" type="hidden" />
        <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <p>Do you authorize "${authorizationRequest.clientId}" at "${authorizationRequest.redirectUri}" to access your protected resources:
            <ul>
                <#list authorizationRequest.scope as scope>
                    <li>
                        <div class="form-group">
                            ${scope} :
                            <input type="radio" name="scope.${scope}" value="true" checked>Approve
                            <input type="radio" name="scope.${scope}" value="false">Deny
                        </div>
                    </li>
                </#list>
            </ul>
        </p>
        <button class="btn btn-primary" type="submit">Approve</button>
    </form>
</div>
</body>
</html>