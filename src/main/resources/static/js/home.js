$(function() {
    $("#btn-create-member").click(function() {
        let data = {
            username: $("#username-input").val()
        }

        $.ajax({
            url: "/members",
            type: "POST",
            contentType: "application/json;charset=utf-8",
            dataType: "json",
            data: JSON.stringify(data),
        }).done(function(res) {
            console.log(res);
        }).fail(function(err) {
            console.log(err);
            alert(err.responseText);
        });
    });


});