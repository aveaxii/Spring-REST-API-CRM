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
        // 1 - Implement delete modal function
        // 2 - implement even on click and passing user id to delete modal
        // 3 - implement delete user function
        // 4 - implement event on click for delete (inside modal) button
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


                    let deleteButton = $('<button type="button" class="btn btn-danger">Delete</button>');
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


    getAllUsers();


    // Sidebar
    // $('#sidebarAdminRole').click(function(e) {
    //     e.preventDefault();
    //
    //     $('.list-group-item-action').removeClass('active');
    //
    //     $(this).addClass('active');
    //
    //     $.ajax({
    //         url: '/admin/admin-content-card-sidebar',
    //         type: 'GET',
    //         dataType: 'html',
    //         success: function(data) {
    //             $('#customContent').html(data);
    //
    //             getAllUsers();
    //         },
    //         error: function() {
    //             console.log('Error loading admin content');
    //         }
    //     });
    // });
    //
    // $('#sidebarUserRole').click(function(e) {
    //     e.preventDefault();
    //
    //     $('.list-group-item-action').removeClass('active');
    //
    //     $(this).addClass('active');
    //
    //     $.ajax({
    //         url: '/admin/user-content-sidebar',
    //         type: 'GET',
    //         dataType : 'html',
    //         success: function(data) {
    //             $('#customContent').html(data);
    //
    //            getUserTableInfo();
    //         },
    //         error: function() {
    //             console.log('Error loading user content');
    //         }
    //     });
    // });
    // Sidebar
});