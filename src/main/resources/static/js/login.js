$(document).ready(function() {
    if (window.location.href.indexOf('?error') !== -1) {
        $('#errorMessage').show();
    }
});