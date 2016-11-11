<!DOCTYPE html>
<head>
    <link rel="stylesheet" href="/server/libs-css/bootstrap-3.3.2.css"/>
    <script type="text/javascript" src="/server/webjars/jquery/jquery.min.js"></script>
</head>
<body>
<div class="container">
    <h2>Custom, Logout</h2>
    <div id="react-logout-container">${content}</div>
    <script type="text/javascript" src="/server/libs/react-15.3.2.js"></script>
    <script type="text/javascript" src="/server/libs/react-dom-15.3.2.js"></script>
    <script type="text/javascript" src="/server/libs/react-dom-server-15.3.2.js"></script>
    <script type="text/javascript" src="/server/components/js/server-logout-form.js"></script>
    <script type="text/javascript">
        $(function () {
            renderClientLogoutForm(${data});
        });
    </script>
</div>
</body>
</html>