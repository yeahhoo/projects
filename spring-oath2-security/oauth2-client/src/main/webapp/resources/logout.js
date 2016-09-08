$(document).ready(function() {

    function submitLogout() {
        document.forms['logoutUserConfirm'].submit();
    };

    function submitByTimeout(logoutInt) {
        setTimeout(submitLogout, logoutInt);
    }

    $('#confirmLogoutBtn').click(function(e) {
        e.stopPropagation();
        submitLogout();
        return false;
    });

    $(function() {
        console.log('Triggered page load listener');
        submitByTimeout(5000);
    });

});