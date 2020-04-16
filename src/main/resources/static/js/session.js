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
        styleActiveLine: true
    });

    // hack
    codemirrorEditor.setSize(null, "calc(100% - 49px)");

    var firepadRef = firebase.database().ref().child(firebase_database_id);
    var firepad = Firepad.fromCodeMirror(firepadRef, codemirrorEditor, {});

    // Output
    var codemirrorOutput = CodeMirror.fromTextArea(document.getElementById("output"), {
        readOnly: true
    });
    codemirrorOutput.setSize(null, "calc(100% - 47px)");

    // Input
    var codemirrorInput = CodeMirror.fromTextArea(document.getElementById("input"));
    codemirrorInput.setSize(null, "calc(100% - 42px)");


    var socket = new SockJS("/session-endpoint");
    var stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        stompClient.subscribe(
            "/session/" + session_id + "/output",
            function (output) {
                // Show output here
                var outputContainer = JSON.parse(output.body);
                // Code ran successfully
                if(outputContainer.output != null)
                    codemirrorOutput.replaceRange(outputContainer.output, {line: Infinity});
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
                    'code' : codemirrorEditor.getValue(),
                    'stdin': codemirrorInput.getValue(),
                    'language': $("#language option:selected").val()
                }
            )
        );
    }

    $("#run").click(function() { compileAndRun(); });

});