$(document).ready(function() {
    // Check if the URL contains the 'error' parameter
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('error')) {
        // Show the error message
        $('#errorMessage').show();
    }
});