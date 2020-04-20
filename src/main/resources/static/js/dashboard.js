$("document").ready(function () {
    $("#change_password").click(function () {
        requestPasswordChange($("#old_password").val(), $("#new_password").val());
    });
});

function requestPasswordChange(old_password, new_password) {
    $("#change_password").attr("disabled", true);
    $.ajax({
        url: '/change-password',
        type: "POST",
        data: {
            old_password: old_password,
            new_password: new_password
        },
        dataType: 'JSON',
        success: function(response){
            $('#change_password_modal').modal('hide');
            $("#new_password").val("");
            $("#old_password").val("");
            $("#change_password").removeAttr("disabled");
            if(response.message === "failure") {
                // Incorrect password entered
                $('#incorrect_password_modal').modal('show');
            }
        },
        error: function (xhr, status, error) {
            // Error occured do something
            alert("Error occurred. Please try again.");
            $("#change_password").removeAttr("disabled");
        }
    });
}