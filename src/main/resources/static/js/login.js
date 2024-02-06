// $(document).ready(function() {
//     if (window.location.href.indexOf('?error') == 1) {
//         $('#errorMessage').show();
//     }
// });

$(document).ready(function() {
    // Check if the URL contains the 'error' parameter
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('error')) {
        // Show the error message
        $('#errorMessage').show();
    }
});