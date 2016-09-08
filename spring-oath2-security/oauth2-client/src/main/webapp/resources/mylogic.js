$(document).ready(function() {

    function doRequest(url, type, headers, params) {
        return $.ajax({
            type: type,
            url: url,
            data: params/*,
            headers: headers*/
        }).done(function(data) {
            console.log('non secured POST successful request: ' + JSON.stringify(data));
        }).fail(function(e) {
            console.log('non secured POST failed request: ' + JSON.stringify(e));
        });
    };

    function readCookie(name) {
        var nameEQ = encodeURIComponent(name) + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) === ' ') c = c.substring(1, c.length);
            if (c.indexOf(nameEQ) === 0) return decodeURIComponent(c.substring(nameEQ.length, c.length));
        }
        return null;
    }

    $('#userBtn').click(function(e) {
        e.stopPropagation();
        var xsrfToken = readCookie('XSRF-TOKEN');
        var headers = {'X-XSRF-TOKEN': xsrfToken};
        doRequest('/client/server/user', 'GET'/*, headers*/)
        .done(function(data) {
            $('#userPlaceholder').text(data.name);
        });
        return false;
    });

    $('#helloBtn').click(function(e) {
        e.stopPropagation();
        doRequest('/client/server/hello', 'GET')
        .done(function(data) {
            $('#helloPlaceholder').text(data);
        });
        return false;
    });

    $('#serverBtn').click(function(e) {
        e.stopPropagation();
        var xsrfToken = readCookie('XSRF-TOKEN');
        var headers = {'X-XSRF-TOKEN': xsrfToken};
        doRequest('/client/server/hellouser', 'GET'/*, headers*/)
        .done(function(data) {
            console.log('server request successful');
            $('#serverPlaceholder').text(data);
        });
        return false;
    });

    $('#corsBtn').click(function(e) {
        e.stopPropagation();
        doRequest('http://eprupetw0604:9001/server/cors', 'GET')
        .done(function(data) {
            console.log('server request successful');
            $('#corsPlaceholder').text(data);
        });
        return false;
    });


/*
    $('#logoutBtn').click(function(e) {
        e.stopPropagation();
        var headers = {'X-XSRF-TOKEN': readCookie('XSRF-TOKEN')};
        doRequest('/client/getUserAndToken', 'GET')
        .done(function(data) {
            console.log('Successfully got user\'s token, redirecting to server logout');
            window.location.href = 'http://eprupetw0604:9001/server/customLogout?user='
                + data.user
                + '&token=' + data.token
                + '&urlToReturn=' + 'http://localhost:8001/client/clientLogout';
        });
        return false;
    });
*/
});


