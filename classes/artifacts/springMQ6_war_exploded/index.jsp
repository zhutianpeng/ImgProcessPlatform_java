<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>PoseEstimation</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/stomp.js"></script>

</head>
<body>
<div style="visibility:hidden; width:0; height:0;">
    <canvas id="canvas" width="600" height="600"></canvas>
</div>

<div>
    <p>
        test
    </p>
    <video id="video" autoplay style="display: inline;"></video>
    <img id="target" style="display:inline;"/>
</div>

<!-- Scripts placed at the end of the document so the pages load faster -->

<script type="text/javascript">
    var client, destination, login, passcode;
    url = "ws://10.103.238.165:61614";
    destination = "/queue/video";
    login = "admin";
    passcode = "password";

    client = Stomp.client(url);

    onmessage = function (message) {
        // called every time the client receives a message
        var bytes = new Uint8Array(message.data);
        var data = "";
        var len = bytes.byteLength;
        for (var i = 0; i < len; ++i) {
            data += String.fromCharCode(bytes[i]);
        }
        var img = document.getElementById("target");
        //img.src = "data:image/png;base64,"+window.btoa(data);
        var imageData = JSON.parse(message.body);
        img.src = imageData.image;
        //记录每次连接的时间
        var timestamp = new Date().getTime();
        console.log("end=" + timestamp);
    }

    var error_callback = function (error) {
        // display the error's message header:
        alert(error);
    };

    // the client is notified when it is connected to the server.
    client.connect(login, passcode, function (frame) {
        //client.send(destination, {}, "hello");//发送消息
        userToken = "123";
        client.subscribe("/user/" + userToken + "/video", onmessage);
//     client.subscribe("/queue/video", onmessage) ;
    }, error_callback);


    // var getUserMedia = (navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mediaDevices.getUserMedia || navigator.msGetUserMedia);
    var constraints = {audio: true, video: {width: 600, height: 600}};

    var video = document.querySelector('video');
    var canvas = document.querySelector('canvas');
    var ctx = canvas.getContext('2d');

    // 老的浏览器可能根本没有实现 mediaDevices，所以我们可以先设置一个空的对象
    if (navigator.mediaDevices === undefined) {
        navigator.mediaDevices = {};
    }

    // 一些浏览器部分支持 mediaDevices。我们不能直接给对象设置 getUserMedia
    // 因为这样可能会覆盖已有的属性。这里我们只会在没有getUserMedia属性的时候添加它。
    if (navigator.mediaDevices.getUserMedia === undefined) {
        navigator.mediaDevices.getUserMedia = function (constraints) {

            // 首先，如果有getUserMedia的话，就获得它
            var getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;

            // 一些浏览器根本没实现它 - 那么就返回一个error到promise的reject来保持一个统一的接口
            if (!getUserMedia) {
                return Promise.reject(new Error('getUserMedia is not implemented in this browser'));
            }

            // 否则，为老的navigator.getUserMedia方法包裹一个Promise
            return new Promise(function (resolve, reject) {
                getUserMedia.call(navigator, constraints, resolve, reject);
            });
        }
    }

    navigator.mediaDevices.getUserMedia({
        audio: true,
        video: true
    }).then(function (localMediaStream) {
        // 旧的浏览器可能没有srcObject
        if ("srcObject" in video) {
            video.srcObject = localMediaStream;
        } else {
            // 防止再新的浏览器里使用它，应为它已经不再支持了
            video.src = window.URL.createObjectURL(stream);
        }
        video.onloadedmetadata = function (e) {
            //console.log("Label: " + localMediaStream.label);
            console.log("AudioTracks", localMediaStream.getAudioTracks());
            console.log("VideoTracks", localMediaStream.getVideoTracks());
            video.play();
        };
    }).catch(function (e) {
        console.log('Reeeejected!', e);
    });

    //    function dataURItoBlob(dataURI) {
    //        //将base64 / URLEncoded数据组件转换为保存在字符串中的原始二进制数据
    //        var byteString;
    //        if (dataURI.split(',')[0].indexOf('base64') >= 0)
    //            byteString = atob(dataURI.split(',')[1]);    //将base64解码为字符串
    //        else
    //            byteString = unescape(dataURI.split(',')[1]);
    //
    //        // 分离出mime组件
    //        var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
    //
    //        // 将字符串的字节写入一个类型数组
    //        var ia = new Uint8Array(byteString.length);    //构造8位无符号整数值的类型化数组
    //        for (var i = 0; i < byteString.length; i++)
    //		{
    //            ia[i] = byteString.charCodeAt(i);
    //        }
    //
    //        return new Blob([ia], {type:mimeString});
    //    }

    timer = setInterval(
        function () {
            ctx.drawImage(video, 0, 0, 600, 600);
            var data = canvas.toDataURL('image/jpeg', 1.0);    //将获取到的图像转换为base64编码
            //newblob = dataURItoBlob(data);

            //添加状态判断，当为OPEN时，发送消息
            //var message = {};
            client.send(destination, {'user-token': 123, 'task': 0x01}, data);//发送消息, 0x02 -> 0000 0010
            console.log("data sent")
        }, 100);

</script>

</body>
</html>