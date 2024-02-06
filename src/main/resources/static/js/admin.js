$(document).ready(function() {
    let currentUserId;
    let selectedRole;

    const ROLE_MAPPER = {
        'ADMIN' : 1,
        'USER': 2,
    };

    // Navbar
    $.ajax({
        url: "/api/user",
        type: "GET",
        dataType: "json",
        success: function(data) {
            currentUserId = data.id;
            $("#userEmail").text(data.email);
            $("#userRoleNavbar").text(data.roles[0].name.substring(5));
            $("#sidebarRole1").text('ADMIN');
            $("#sidebarRole2").text(data.roles[1].name.substring(5));
        },
        error: function (error) {
            console.log(error);
        }
    });
    // Navbar


    // Tabs
    $('#adminTab').click(function(e) {
        e.preventDefault();

        $('.nav-link').removeClass('active');

        $(this).addClass('active');

        $('#addNewUserCard').hide();
        $('#userTableInfoCard').show();
    });

    $('#addUserTab').click(function(e) {
        e.preventDefault();

        $('.nav-link').removeClass('active');

        $(this).addClass('active');

        $('#userTableInfoCard').hide();
        $('#addNewUserCard').show();
    });
    // Tabs


    // Edit user
    function openEditModal(userId) {
        $.ajax({
            url: '/api/admin/' + userId,
            type: 'GET',
            dataType: 'json',
            success: function (user) {

                $('#edit_id').val(user.id);
                $('#edit_name').val(user.name);
                $('#edit_surname').val(user.surname);
                $('#edit_birthYear').val(user.birthYear);
                $('#edit_email').val(user.email);

                $('#edit_roles').change(function() {
                    selectedRole = $('#edit_roles option:selected').val().toUpperCase();
                });

                $('#edit_user_modal').modal('show');
            },
            error: function (xhr, status, error) {
                console.error('Error fetching user details:', error);
            }
        });
    }

    $(document).on('click', '.edit-button', function () {
        // Get the user ID from the table row where the edit button was clicked
        let userId = $(this).closest('tr').find('td:first').text();
        openEditModal(userId);
    });

    function updateUser(userId, updatedUserData) {
        $.ajax({
            url: '/api/admin/' + userId,
            type: 'PATCH',
            contentType: 'application/json',
            data: JSON.stringify(updatedUserData),
            success: function (response) {
                $('#edit_user_modal').modal('hide');

                if (parseInt(userId) === currentUserId && selectedRole === 'USER') {
                    window.location.href = '/logout';
                } else {
                    getAllUsers();
                }
            },
            error: function (xhr, status, error) {
                console.error('Error updating user:', error);
            }
        });
    }

    $('#edit_user_button').click(function () {
        let userId = $('#edit_id').val();
        let selectedRoleId = ROLE_MAPPER[selectedRole];

        let updatedUserData = {
            name: $('#edit_name').val(),
            surname: $('#edit_surname').val(),
            birthYear: $('#edit_birthYear').val(),
            email: $('#edit_email').val(),
            password: $('#edit_password').val(),
            roles: [
                {
                    id: selectedRoleId,
                    name: 'ROLE_' + selectedRole,
                    authority: 'ROLE_' + selectedRole
                }
            ]
        };

        updateUser(userId, updatedUserData);
    });

    // Edit user


    // Delete user
    function openDeleteModal(userId) {
        $.ajax({
            url: '/api/admin/' + userId,
            type: 'GET',
            dataType: 'json',
            success: function (user) {
              $('#delete_id').val(user.id);
                $('#delete_name').val(user.name);
                $('#delete_surname').val(user.surname);
                $('#delete_birthYear').val(user.birthYear);
                $('#delete_email').val(user.email);

                $('#delete_user_modal').modal('show');
            },
            error: function (error) {
                console.error('Error fetching user details:', error);
            }
        });
    }

    $(document).on('click', '.delete-button', function () {
        let userId = $(this).closest('tr').find('td:first').text();
        openDeleteModal(userId);
    });

    function deleteUser(userId) {
        $.ajax({
            url: '/api/admin/' + userId,
            type: 'DELETE',
            success: function (response) {
                $('#delete_user_modal').modal('hide');
                getAllUsers();
            },
            error: function (error) {
                console.error('Error deleting user:', error);
            }
        });
    }

    $('#delete_user_button').click(function () {
        let userId = $('#delete_id').val();
        deleteUser(userId);
    });
    // Delete user


    // User table
    function getAllUsers() {
        $.ajax({
            url: "/api/admin/allUsers",
            type: "GET",
            dataType: "json",
            success: function (data) {
                let userTableBody = $('#userTableBody');

                userTableBody.empty();

                data.forEach(function (user) {
                    let row = $('<tr></tr>');

                    row.append('<td>' + user.id + '</td>');
                    row.append('<td>' + user.name + '</td>');
                    row.append('<td>' + user.surname + '</td>');
                    row.append('<td>' + user.birthYear + '</td>');
                    row.append('<td>' + user.email + '</td>');
                    row.append('<td>' + '[' + user.roles[0].name.substring(5) + ']' + '</td>');

                    let editButton = $('<button type="button" class="btn btn-info edit-button">Edit</button>');
                    let editBlock = $('<td></td>').append(editButton);
                    row.append(editBlock);


                    let deleteButton = $('<button type="button" class="btn btn-danger delete-button">Delete</button>');
                    let deleteBlock = $('<td></td>').append(deleteButton);
                    row.append(deleteBlock);

                    if (user.id === currentUserId) {
                        row.addClass('table-active');
                    }

                    userTableBody.append(row);

                });
            },
            error: function (error) {
                console.log(error);
            }
        });
    }
    // User table

    // Add new user
    $('#addUser_roles').change(function() {
        selectedRole = $('#addUser_roles option:selected').val().toUpperCase();
    });

    $('#add_user_form').submit(function (e) {
        e.preventDefault();

        let selectedRoleId = ROLE_MAPPER[selectedRole];

        let newUserFormData = {
            name: $('#addUser_name').val(),
            surname: $('#addUser_surname').val(),
            birthYear: $('#addUser_birthYear').val(),
            email: $('#addUser_email').val(),
            password: $('#addUser_password').val(),
            roles: [
                {
                    id: selectedRoleId,
                    name: 'ROLE_' + selectedRole,
                    authority: 'ROLE_' + selectedRole
                }
            ]
        };

        $.ajax({
            url: '/api/admin/add',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(newUserFormData),
            success: function (response) {
                $('.nav-link').removeClass('active');

                $('#adminTab').addClass('active');

                $('#addNewUserCard').hide();
                $('#userTableInfoCard').show();
                getAllUsers();

                $('#addUser_name').val('');
                $('#addUser_surname').val('');
                $('#addUser_birthYear').val('');
                $('#addUser_email').val('');
                $('#addUser_password').val('');
                $('#addUser_roles').val(undefined);
            },
            error: function (error) {
                console.error('Error adding new user:', error);
            }
        });

    });


    // Add new user

    getAllUsers();
});