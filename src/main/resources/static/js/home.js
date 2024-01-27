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
            if (res.success) {
                alert(res.message);
                return;
            } else {
                if (res.message === "Valid error") {
                    alert(res.data.defaultMessage);
                    return;
                }
                alert(res.message);
            }
        }).fail(function(err) {
            console.log(err);
            alert(err.responseText);
        });
    });

    $("#btn-enter-member").click(function() {
        const username = $('#username-input-enter').val();
        const goUrl = window.location.href + "members/" + username;
        window.location.replace(goUrl);
    });


});