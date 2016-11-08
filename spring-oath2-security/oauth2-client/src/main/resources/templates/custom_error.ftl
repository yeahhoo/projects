<html>
<head>
    <meta charset="utf-8" />
    <title>Demo</title>
    <link rel="stylesheet" type="text/css" href="/client/libs/css/bootstrap-3.3.2.css" />
</head>
<body>
<h1>Oops, something gone wrong</h1>
    <div class="container">
        <div id="react-custom-error-config" style="display: none;">
            <!-- refactor using server-rendering reactJs -->
            <span id="status">${status}</span>
            <span id="error">${error}</span>
            <span id="message">${message}</span>
            <span id="timestamp">${timestamp?datetime}</span>
        </div>
        <div id="react-custom-error-container"></div>
    </div>
    <script type="text/javascript" src="/client/webjars/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/client/libs/js/react-15.3.2.js"></script>
    <script type="text/javascript" src="/client/libs/js/react-dom-15.3.2.js"></script>
    <script type="text/javascript" src="/client/libs/js/babel-core-5.8.34-browser.min.js"></script>
    <script type="text/babel" src="/client/components/js/react-custom-error.js"></script>
</body>
</html>