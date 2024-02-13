$(document).ready(function() {
   $('#registrationForm').submit(function(event) {
       event.preventDefault();

       let formData = {
           name: $('#name').val(),
           surname: $('#surname').val(),
           birthYear: $('#birthYear').val(),
           email: $('#email').val(),
           password: $('#password').val(),
           // roles: [
           //     {
           //     }
           // ]
       };

       $.ajax({
           type: 'POST',
           url: '/api/auth/registration',
           data: JSON.stringify(formData),
           contentType: 'application/json',
           success: function() {
              // window.location.href = '/auth/login';
               console.log(formData);
           },
           error: function(error) {
               console.error(error);
               console.log(formData);
           }
       });
   });
});