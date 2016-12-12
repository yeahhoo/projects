<html>
<head>
    <meta charset="utf-8" />
    <title>Demo</title>
   <link rel="stylesheet" type="text/css" href="/client/libs/css/bootstrap-3.3.2.css" />
   <link rel="stylesheet" type="text/css" href="/client/components/css/navbar-fixed-top.css" />
</head>
<body>
    <div id="react-index-config" style="display: none;">
        <span id="jsonResponse">${jsonResponse}</span>
    </div>
    <div id="react-index-container"></div>
    <#-- libraries -->
    <script type="text/javascript" src="/client/webjars/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/client/libs/js/bootstrap-3.3.2.js"></script>
    <#-- webpack bundle file -->
    <script type="text/javascript" src="/client/components/js/webpack/bundle.js"></script>
</body>
</html>