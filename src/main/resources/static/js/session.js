$("document").ready(function () {

    // Firebase config
    var config = {
        apiKey: 'AIzaSyD0dtv3yXPmrVOypXCqRtw3vPh7pK8-oH4',
        authDomain: "collabcode-4bce8.firebaseapp.com",
        databaseURL: "https://collabcode-4bce8.firebaseio.com"
    };
    firebase.initializeApp(config);

    // Editor
    var codemirrorEditor = CodeMirror.fromTextArea(document.getElementById("editor"), {
        mode: "text/x-java",
        indentWithTabs: true,
        lineNumbers: true,
        styleActiveLine: true,
        smartIndent: true,
        lineWrapping: true,
        matchBrackets : true,
        indentUnit: 4,
        autoCloseBrackets: true,
        autofocus: true
    });
    codemirrorEditor.setSize(null, "calc(100% - 49px)");
    var firepadCodeRef = firebase.database().ref().child(session_id).child("code");
    var firepadCode = Firepad.fromCodeMirror(firepadCodeRef, codemirrorEditor, {});


    // Input
    var codemirrorInput = CodeMirror.fromTextArea(document.getElementById("input"), {
        mode : "text/plain"
    });
    codemirrorInput.setSize(null, "calc(100% - 42px)");
    var firepadInputRef = firebase.database().ref().child(session_id).child("input");
    var firepadInput = Firepad.fromCodeMirror(firepadInputRef, codemirrorInput, {});


    // Language
    var languageRef = firebase.database().ref().child(session_id).child("language");
    languageRef.on('value', function (snapshot) {
        if(snapshot.exists()) {
            var language = snapshot.val();
            // Change dropdown value as well as Codemirror mode
            $("#language").val(language);
            if(language.localeCompare("java")) {
                codemirrorEditor.setOption("mode", "text/x-java");
            } else if(language.localeCompare("cpp")) {
                codemirrorEditor.setOption("mode", "text/x-c++src");
            } else {
                codemirrorEditor.setOption("mode", "text/x-csrc");
            }
        }
    });

    $('select').on('change', function() {
        var language = this.value;
        firebase.database().ref().child(session_id).child("language").set(language);
    });


    // Output
    var codemirrorOutput = CodeMirror.fromTextArea(document.getElementById("output"), {
        mode: "text/plain",
        readOnly: true
    });
    codemirrorOutput.setSize(null, "calc(100% - 47px)");


    var socket = new SockJS("/session-endpoint");
    var stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        stompClient.subscribe(
            "/session/" + session_id + "/live-collaborators",
            function (output) {
                $("#all_collaborators").empty();

                var liveCollaborators = JSON.parse(output.body);
                for(var i = 0; i < liveCollaborators.length; i++) {
                    var liveCollaborator = liveCollaborators[i];
                    $("#all_collaborators").append(
                        '<button class="btn btn-sm btn-outline-dark pl-3 pr-3 ml-2">' +
                        liveCollaborator.firstName + '</button>');
                }

            }
        );

        stompClient.subscribe(
            "/session/" + session_id + "/output",
            function (output) {
                // Show output here
                var outputContainer = JSON.parse(output.body);
                var result = "#" + outputContainer.sourceUserFirstName + " ran code...\n\n";
                if(outputContainer.output != null) {
                    result += outputContainer.output;
                }
                result += "\n\n\n";
                codemirrorOutput.replaceRange(result, {line: Infinity});
                codemirrorOutput.setCursor(codemirrorOutput.lineCount(), 0);
            }
        );
    });


    function compileAndRun() {
        // Send code to server to compile and run
        stompClient.send(
            "/app/session/" + session_id + "/input",
            {},
            JSON.stringify(
                {
                    'sourceUserFirstName' : first_Name,
                    'code' : codemirrorEditor.getValue(),
                    'stdin': codemirrorInput.getValue(),
                    'language': $("#language option:selected").val()
                }
            )
        );
    }

    $("#run").click(function() {compileAndRun(); });

    $("#add_collaborator").click(function () {
        addCollaborator($("#collaborator_email_address").val());
    });

    function addCollaborator(emailAddress) {
        $("#add_collaborator").attr("disabled", true);
        $.ajax({
            url: '/session/' + session_id + '/add-collaborator',
            type: "POST",
            data: {
                email_address: emailAddress
            },
            dataType: 'JSON',
            success: function(response){
                $('#add_collaborator_modal').modal('hide');
                $("#collaborator_email_address").val("");
                $("#add_collaborator").removeAttr("disabled");

                if(response.message === "failure") {
                    // Incorrect password entered
                    $('#invalid_email_modal').modal('show');
                }
            },
            error: function (xhr, status, error) {
                // Error occured do something
                alert("Error occurred. Please try again.");
                $("#add_collaborator").removeAttr("disabled");
            }
        });
    }

    $("#clear").click(function() {
        codemirrorOutput.setValue("");
    });
});