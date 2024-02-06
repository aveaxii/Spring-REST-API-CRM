$(document).ready(function() {
    $.ajax({
       url: "/api/user",
        type: "GET",
        dataType: "json",
        success: function(data) {
            $("#userEmail").text(data.email);
            $("#userRoleNavbar").text(data.roles[0].name.substring(5));
            $("#sidebarRole1").text(data.roles[0].name.substring(5));

            let userTableBody = $('#userTableBody');

            userTableBody.empty();


                let row = $('<tr></tr>');

                row.append('<td>' + data.id + '</td>');
                row.append('<td>' + data.name + '</td>');
                row.append('<td>' + data.surname + '</td>');
                row.append('<td>' + data.birthYear + '</td>');
                row.append('<td>' + data.email + '</td>');
                row.append('<td>' + '[' + data.roles[0].name.substring(5) + ']' + '</td>');

                row.addClass('table-active');

            userTableBody.append(row);

            if (data.roles[0].name.substring(5) === "ADMIN") {
                let adminRoleSidebar =
                    `<a href="/admin" class="list-group-item list-group-item-action py-3 mt-3 lh-sm rounded-end justify-content-center" aria-current="true" id="sidebarAdminRole">
                    <div class="col-10 mb-1 mx-auto small text-center" bis_skin_checked="1">
                    <span id="sidebarRole1" style="font-weight: bold">ADMIN</span>
                    </div>
                     </a>`;

                $('#sidebarRole1').text('USER');

                $('#customSidebar').append(adminRoleSidebar);
            }
        },
        error: function (error) {
            console.log(error);
        }
    });


});