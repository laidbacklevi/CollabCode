$("document").ready(function () {
    $("#run").click(function compileAndRun() {
        alert("hello");
        $.ajax({
            url: '/compile-and-run',
            type: "GET",
            data: {
                code : "<?php\n" +
                    "echo \"Hello World!\";\n" +
                    "?>",
                language: "php",
                input: "",
            },
            dataType: 'JSON',
            success: function(response){
                console.log(response);
            },
            error: function (xhr, status, error) {
                alert("Result: " + status + " " + error + " " + xhr.status + " " + xhr.statusText)
            }
        });
    });
});