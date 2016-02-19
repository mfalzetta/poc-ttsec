var Upsale = Upsale || {};

Upsale.Session = function (aSessionId) {

    var sessionId = aSessionId;

    var videoProperties = {
        width: 400,
        height: 300,
        style: {buttonDisplayMode: "off"}
    };

    var ws;
    var session;
    var publisher;
    var viewer;

    // Callback de abertura de websocket.
    var onOpenCallback;
    // Callback de mensagem recebida via websocket.
    var onMessageCallback;

    this.init = function () {

        console.log("Initializing... ");

        ws = new WebSocket('ws://' + location.host + '/upsale/session');

        // Call back de abertura do websocket.
        ws.onopen = function (evt) {
            console.log('Websocket connection opened.');
            if (onOpenCallback) onOpenCallback();
        };

        // Callback de mensagem recebida do websocket.
        ws.onmessage = function (msg) {
            console.info('Received message: ' + msg.data);
            var json = JSON.parse(msg.data);
            switch (json.type) {
                case 'publisher':
                    publisherResponse(json);
                    break;
                case 'viewer':
                    viewerResponse(json);
                    break;
            }
            if (onMessageCallback) onMessageCallback(json);
        };

        ws.onclose = function () {
            console.log('Websocket connection closed.');
        };

        window.onbeforeunload = function () {
            ws.close();
        }
    };

    this.onOpen = function (callback) {
        onOpenCallback = callback;
    }

    this.onMessage = function (callback) {
        onMessageCallback = callback;
    }

    this.startPublisher = function () {
        this.sendMessage({type: 'publisher'});
    };

    this.startViewer = function () {
        this.sendMessage({type: 'viewer'});
    };

    this.stop = function () {
        if (session) session.disconnect();
        this.sendMessage({type: 'stop'});
    };

    this.pausePublisher = function () {
        if (session) session.unpublish(publisher);
    };

    this.resumePublisher = function () {
        if (session) session.publish(publisher);
    };

    this.sendMessage = function (msg) {
        msg.id = sessionId;
        var json = JSON.stringify(msg);
        console.log('Sending message: ' + json);
        ws.send(json);
    };

    var publisherResponse = function (msg) {
        if (msg.status != 'success') {
            var errorMsg = msg.message ? msg.message : 'Unknow error';
            console.info('Call not accepted for the following reason: ' + errorMsg);
        } else {

            var apiKey = msg.apiKey;
            var externalSessionId = msg.externalSessionId;
            var token = msg.token;

            session = OT.initSession(apiKey, externalSessionId);

            if (OT.checkSystemRequirements() == 1) {
                publisher = OT.initPublisher("video", videoProperties);
                publisher.on({
                    streamCreated: function (event) {
                        console.info("Publisher started streaming.");
                    },
                    streamDestroyed: function (event) {
                        console.info("Publisher stopped streaming. Reason: " + event.reason);
                        event.preventDefault();
                    }
                });

                session.connect(token, function (error) {
                    if (session.capabilities.publish == 1) {
                        session.publish(publisher);
                    } else {
                        console.info("You cannot publish an audio-video stream.");
                    }
                });

            } else {
                console.info("The client does not support WebRTC.");
            }
        }
    }

    var viewerResponse = function (msg) {
        if (msg.status != 'success') {
            var errorMsg = msg.message ? msg.message : 'Unknow error';
            console.info('Call not accepted for the following reason: ' + errorMsg);
        } else {

            var apiKey = msg.apiKey;
            var externalSessionId = msg.externalSessionId;
            var token = msg.token;

            session = OT.initSession(apiKey, externalSessionId);

            session.on({
                streamCreated: function (event) {
                    console.info("StreamCreated");
                    viewer = session.subscribe(event.stream, "video", videoProperties);
//                    viewer.restrictFrameRate(true);
                },
                streamDestroyed: function (event) {
                    console.info("Publisher stopped streaming. Reason: " + event.reason);
                    event.preventDefault();
                }
            });

            session.connect(token, function (error) {
                if (error) {
                    console.log(error.message);
                } else {
                    console.log("Connected to video session.");
                }
            });
        }
    }

};
