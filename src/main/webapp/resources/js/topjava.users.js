// $(document).ready(function () {
const ajaxUsersUrl = "ajax/admin/users/";

$(function () {
    makeEditable({
            ajaxUrl: ajaxUsersUrl,
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "asc"
                    ]
                ]
            }),
            updateTable: function () {
                $.get(ajaxUsersUrl, updateTable);
            }
        }
    );
});

function enable(chkbox, id) {
        const enabled = chkbox.is(":checked");
        $.ajax({
            url: ajaxUsersUrl + id,
            type: "POST",
            data: "enabled=" + enabled
        }).done(function () {
            chkbox.closest("tr").attr("data-userEnabled", enabled);
            successNoty(enabled ? "Enabled" : "Disabled");
        }).fail(function () {
            $(chkbox).prop("checked", !enabled);
        });
}