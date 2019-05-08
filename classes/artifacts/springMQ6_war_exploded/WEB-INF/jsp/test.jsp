<%--
  Created by IntelliJ IDEA.
  User: Tzh
  Date: 2019/4/24
  Time: 9:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>姿态分析展示平台</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/webGL/TemplateData/favicon.ico">
    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script><!-- 引入vue框架 -->
    <script src="https://cdn.WebRTC-Experiment.com/RecordRTC.js"></script>
    <%--录制实时视频用--%>
    <script src="https://unpkg.com/element-ui/lib/index.js"></script><!-- 引入element组件库 -->
    <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css"><!-- 引入element样式 -->
    <script src="${pageContext.request.contextPath}/newJs/jquery-3.3.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/webGL/TemplateData/UnityProgress.js"></script>
    <script src="${pageContext.request.contextPath}/webGL/Build/UnityLoader.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webGL/TemplateData/style.css">
    <script src="https://cdn.jsdelivr.net/npm/echarts/dist/echarts.min.js"></script>
    <%--引入v-cahrts组件--%>
    <script src="https://cdn.jsdelivr.net/npm/v-charts/lib/index.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/v-charts/lib/style.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/newCss/global.scss">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/newCss/index.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/stomp.js"></script>
    <script src="${pageContext.request.contextPath}/newJs/components/DataBox.js"></script><!-- 引入DataBox组件 -->
    <script src="${pageContext.request.contextPath}/newJs/components/Common.js"></script><!-- 引入组件 -->
    <script src="${pageContext.request.contextPath}/newJs/components/Exercise.js"></script><!-- 用于健身指导功能的全部组件 -->
    <script src="${pageContext.request.contextPath}/newJs/components/Dance.js"></script><!-- 用于动画舞蹈的全部组件 -->

</head>
<style>
    .header {
        width: 100%;
        height: 160px;
        padding: 0 20px;
    }

    .bg-header {
        width: 100%;
        height: 100%;
        background: url(${pageContext.request.contextPath}/img/title.png) no-repeat;
        background-size: 100% 100%;
    }

    .t-title {
        width: 100%;
        height: 50%;
        text-align: center;
        font-size: 2em;
        line-height: 80px;
        color: #fff;
    }

    .data-page {
        background: url(${pageContext.request.contextPath}/img/background.png) no-repeat;
        top: 0;
        right: 0;
        left: 0;
        bottom: 0;
        height: 1100px;
        min-width: 1220px;
        background-size: 100% 100%;
    }

    .data-content {
        padding-top: 20px;
        padding-bottom: 20px;
    }

    .data-main {
        width: calc(100% - 40px);
        margin-bottom: 40px;
        margin-left: 20px;
        height: 720px;
    }

    .main-left {
        width: 24%;
        float: left;
    }

    .main-center {
        float: left;
        width: 52%;
        padding: 0 20px 0 20px;
    }

    .main-right {
        float: left;
        width: 24%;
    }


</style>

<body>
<div class="data-page" id="app">
    <div class="header">
        <div class="bg-header">
            <div class="t-title">Pose Estimation</div>
            <div class="t-title">{{pageSet.title}}</div>
        </div>
    </div>
    <%--<topnav></topnav>--%>
    <div class="data-content">
        <%--<div class="data-time">--%>
        <%--</div>--%>
        <div class="data-main">
            <div class="main-left">
                <component :is="pageSet.left"></component>
                <%--加载左侧组件--%>
            </div>
            <div class="main-center">
                <data-box :title="'实时Unity3D动画'" :dheight="500" :icon="'account'" :boxb="false">
                    <div>
                        <div id="gameContainer" style="width: 100%; height: 100%"></div>
                    </div>
                </data-box>
                <data-box :title="'分区介绍'" :dheight="290" :icon="'account'">
                    <component :is="pageSet.bottom"></component>
                    <%--加载底部组件--%>
                </data-box>
            </div>
            <div class="main-right">
                <data-box :title="''" :dheight="800">
                    <data-box :title="'配置面板'" :dheight="400" :boxb="false">
                        <el-form style="padding: 20px;" label-position="left" label-width="150px">
                            <el-form-item label="切换分区">
                                <el-select v-model="chosenModule">
                                    <el-option v-for="option in moduleOptions" :key="option.value" :label="option.label"
                                               :value="option.value">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="切换角色">
                                <el-select v-model="chosenCharacter">
                                    <el-option v-for="option in characterOptions" :key="option.value"
                                               :label="option.label" :value="option.value">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="切换场景">
                                <el-select v-model="chosenScene">
                                    <el-option v-for="option in sceneOptions" :key="option.value" :label="option.label"
                                               :value="option.value">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item>
                                <el-button :type="button.buttonType" @click="change"><span
                                        v-if="button.enable">禁用脚本</span><span v-if="!button.enable">激活脚本</span>
                                </el-button>
                                <el-button :type="button2.buttonType" @click="changeTrans"><span v-if="button2.enable">中止传输</span><span
                                        v-if="!button2.enable">开始传输</span></el-button>
                                <%--<el-button type="success" @click="sendWS">发送WS测试</el-button>--%>
                            </el-form-item>


                        </el-form>
                    </data-box>
                    <data-box :title="'摄像头实时输出画面'" :dheight="400" :boxb="false">
                        <video style="width: 100%; height: 100%" autoplay></video>
                        <div style="visibility:hidden; width:0; height:0;">
                            <%--<canvas id="canvas" width="800px" height="600px"></canvas>--%>
                            <canvas id="canvas" width="400px" height="300px"></canvas>
                        </div>

                    </data-box>
                </data-box>

                <%--<dright :username="username"></dright>--%>
            </div>
        </div>
    </div>
</div>

<script>


    var destination, login, passcode, userToken;
    url = "ws://10.103.238.165:61614";
    destination = "/queue/video";
    login = "admin";
    passcode = "password";
    userToken = "123";

    var pointMapping = { //关节点映射表，由于识别关节点和Unity关节点不同，因此需要进行转换
        "0": "10",
        "1": "8",
        "2": "14",
        "3": "15",
        "4": "16",
        "5": "11",
        "6": "12",
        "7": "13",
        "8": "1",
        "9": "2",
        "10": "3",
        "11": "4",
        "12": "5",
        "13": "6"
    };
    var client = Stomp.client(url);

    var stompOnMessage = function (message) {
        let jsonData = JSON.parse(message.body); //接收数据JSON示例：{'image':'', 'poseResultParsed':''}
        if (jsonData.poseResultParsed) {
            let poseArray = JSON.parse(jsonData.poseResultParsed); //单帧二维坐标点数据
            // for(let i = 0; i < poseArray.length;i ++){ //TODO 多人场景？
            //
            // }

            let singlePerson = poseArray[0]; //singlePerson是个JSON对象，key为关节点index值，value为横纵坐标以及置信度所组成的JSON对象

            let singleArray = [];
            for (let key in singlePerson) {
                if (pointMapping[key]) { //进行映射之后的关节点index，如果在定义范围内则进行写入（注意14以后的坐标点都没有用到）
                    singleArray.push(pointMapping[key]);
                    singleArray.push(singlePerson[key].x);
                    singleArray.push(singlePerson[key].y);
                }
            }
            console.log(singleArray);
            if (singleArray) { //数据不为空
                app.gameInstance.SendMessage("Philip", "GetPose", singleArray.toString()); //调用Unity内部方法，将姿态数据传入
            }
        }
    };

    var stompOnError = function (error) {
        alert(error);
        console.log("Stomp连接出错！" + error);
    };

    // the client is notified when it is connected to the server.
    client.connect(login, passcode, function (frame) {
        client.subscribe("/user/" + userToken + "/video", stompOnMessage);
    }, stompOnError);


    var app = new Vue({
        el: '#app',
        data: {
            stompInfo: {
                url: "ws://10.103.238.165:61614",
                destination: "/queue/video",
                login: "admin",
                passcode: "password",
                client: ''
            },
            gameInstance: '',
            button: {
                enable: true,
                buttonType: 'danger',
            },
            button2: {
                enable: false,
                buttonType: 'success',
            },
            characterOptions: [{
                label: '男性角色',
                value: 'boy'
            }, {
                label: '女性角色',
                value: 'girl'
            }],
            sceneOptions: [{
                label: '户外场景',
                value: 'outdoor'
            }, {
                label: '室内场景',
                value: 'indoor'
            }],
            moduleOptions: [{
                label: '动画舞蹈',
                value: 'dance'
            }, {
                label: '健身指导',
                value: 'exercise'
            }],
            chosenCharacter: '',
            chosenScene: '',
            chosenModule: '',
            pageSet: {//页面各个部位加载组件
                title: '', //标题
                left: '', //页面左侧
                bottom: '' //页面底部
            },
            pageSetList: {
                'dance': Dance,
                'exercise': Exercise
            },
            sendMsg: {
                userToken: '123',
                image: '',
                task: 1
            },
            confidenceArray: [],
            myTimer: '' //定时器
        },
        mounted: function () {
            this.initPage(); //页面组件初始化
            this.gameInstance = UnityLoader.instantiate("gameContainer", "${pageContext.request.contextPath}/webGL/Build/Receiver2Dv2.json", {onProgress: UnityProgress});

            this.initCamera(); //初始化摄像头
            // this.initStomp(); //Stomp初始化
            // this.initWebSocket(); //初始化WebSocket


        },
        watch: {
            chosenModule: function (newVal) {
                this.pageSet = this.pageSetList[newVal];
            }
        },
        methods: {
            initPage: function () { //页面初始化
                this.chosenCharacter = this.characterOptions[0].value; //初始选择男性角色
                this.chosenScene = this.sceneOptions[0].value; //初始选择户外场景
                this.chosenModule = this.moduleOptions[1].value; //初始选择功能分区
            },
            initStomp: function () { //Stomp初始化
                let _this = this;
                _this.stompInfo.client = Stomp.client(_this.stompInfo.url);
                _this.stompInfo.client.connect(_this.stompInfo.login, _this.stompInfo.passcode, function (frame) {
                    let userToken = "123";
                    _this.stompInfo.client.subscribe("/user/" + userToken + "/video", _this.stompOnMessage());
                }, _this.stompOnError());
            },
            stompOnMessage: function (message) { //Stomp接收到消息回调方法 TODO 计时以检验效率
                let jsonData = JSON.parse(message.body); //接收数据JSON示例：{'image':'', 'poseResultParsed':''}

                let poseArray = jsonData.poseResultParsed; //单帧二维坐标点数据
                // for(let i = 0; i < poseArray.length;i ++){ //TODO 多人场景？
                //
                // }
                let singlePerson = poseArray[0];
                let singleArray = [];
                for (let key in singlePerson) {
                    singleArray.push(key);
                    singleArray.push(singleArray[key].x);
                    singleArray.push(singleArray[key].y);
                }
                console.log(singleArray);

                // if(poseData){ //数据不为空
                //     gameInstance.SendMessage("Philip", "GetPose", singleArray.toString()); //调用Unity内部方法，将姿态数据传入
                // }
            },
            stompOnError: function (error) { //Stomp出错回调方法
                alert(error);
                console.log("Stomp出错！" + error);
            },
            initCamera: function () {
                let _this = this;
                let constraints = {video: true};
                let video = document.querySelector('video');
                navigator.mediaDevices.getUserMedia(constraints)
                    .then(function (mediaStream) {
                        video.srcObject = mediaStream;
                    })
                    .catch(function (err) {
                        console.log(err.name + ": " + err.message);
                    }); // 总是在最后检查错误
            },
            transImage: function (video) {
//                console.log("进入transImage方法");
                let _this = this;
                let canvas = document.querySelector('canvas');
//                let scale = 1; //缩放比例
//                canvas.width = video.videoWidth * scale;
//                canvas.height = video.videoHeight * scale;
                canvas.getContext('2d').drawImage(video, 0, 0, 400,300);
                let image = canvas.toDataURL('image/jpeg');
                if (image != null) {
                    client.send(destination, {'user-token': userToken, 'task': 0x01}, image); //发送消息
                    // _this.sendMsg.image = image; //填充base64编码后的视频帧
                    // this.socket.send(JSON.stringify(_this.sendMsg)); //通过WebSocket发送到后台
                    // console.log(image);
                }
            },
            change: function () { //控制WebGL激活/禁用脚本
                this.gameInstance.SendMessage("Philip", "ChangeState");
                console.log("脚本状态改变！");
                this.button.enable = !this.button.enable;
                if (this.button.enable) {
                    this.button.buttonType = 'danger';

                } else {
                    this.button.buttonType = 'success';
                }
            },
            initWebSocket: function () {
                if (window.WebSocket) {
                    console.log("初始化WebSocket！");
                    // this.socket = new WebSocket('ws://localhost:8080/PoseEstimation/unityWebSocket');
                    this.socket = new WebSocket('ws://localhost:8080/PoseEstimation/webSocket/' + this.sendMsg.userToken); //123为用户ID
                    this.socket.onopen = this.onOpen;
                    this.socket.onmessage = this.onMessage;
                    this.socket.onerror = this.onError;
                    this.socket.onclose = this.onClose;
                } else {
                    console.log("不支持WebSocket！");
                }
            },
            onOpen: function () {
                let _this = this;
                console.log("WebSocket连接成功!");
                setInterval(function () {
                    _this.transImage(document.querySelector('video'));
                }, 50); //50ms发送一次
                // this.socket.binaryType = "arraybuffer"; //设置发送消息类型
                // this.socket.send("Hello!");
            },
            onMessage: function (event) {
                if (event.data) {
                    console.log("接收到来自后台的消息：" + event.data);
                    gameInstance.SendMessage("Philip", "GetPose", event.data); //调用Unity内部方法，将姿态数据传入
                }
            },
            onError: function () {
                console.log("WebSocket连接发生错误！");
            },
            onClose: function () {
                console.log("WebSocket关闭！");
            },
            changeTrans: function () { //按钮调用，用于启动图像传输
                let _this = this;
                _this.button2.enable = !_this.button2.enable;
                if (_this.button2.enable) {
                    _this.button2.buttonType = 'danger';
                    _this.myTimer = setInterval(function () {
                        _this.transImage(document.querySelector('video'));
                    }, 100); //50ms发送一次
                } else {
                    clearInterval(_this.myTimer);
                    _this.button2.buttonType = 'success';
                }
            },


            // recordVideo: function (stream) { //录制实时视频
            //     let _this = this;
            //     let options;
            //     if (typeof MediaRecorder.isTypeSupported == 'function'){//这里涉及到视频的容器以及编解码参数，这个与浏览器有密切的关系
            //         if (MediaRecorder.isTypeSupported('video/webm;codecs=vp9')) {
            //             options = {mimeType: 'video/webm;codecs=h264'};
            //         } else if (MediaRecorder.isTypeSupported('video/webm;codecs=h264')) {
            //             options = {mimeType: 'video/webm;codecs=h264'};
            //         } else if (MediaRecorder.isTypeSupported('video/webm;codecs=vp8')) {
            //             options = {mimeType: 'video/webm;codecs=vp8'};
            //         }
            //         console.log('Using '+options.mimeType);
            //         mediaRecorder = new MediaRecorder(stream, options);
            //     }else{
            //         log('isTypeSupported is not supported, using default codecs for browser');
            //         mediaRecorder = new MediaRecorder(stream);
            //     }
            //
            //     mediaRecorder.ondataavailable = function(event) { //当视频数据准备完成时回调
            //         // this.chunks.push(event.data);
            //         let reader = new FileReader();
            //         reader.addEventListener("loadend", function() { //读取数据结束后的监听方法
            //             let buf = new Uint8Array(reader.result); //reader.result是一个含有视频数据流的Blob对象，进行格式转换
            //             console.log(buf);
            //             if(reader.result.byteLength > 0){ //空数据不进行发送
            //                 _this.socket.send(buf);
            //             }
            //         });
            //         reader.readAsArrayBuffer(event.data);
            //     };
            //     mediaRecorder.onerror = function(e){
            //         console.log('实时视频录制出错：' + e);
            //     };
            //     mediaRecorder.onstart = function(){
            //         console.log('实时视频开始录制：' + mediaRecorder.state);
            //     };
            //     mediaRecorder.start(10); //每10ms记录成一个Blob
            // }

        },
        components: {
            dataBox: DataBox,
            dataBoard: DataBoard, //用于健身指导的数据面板（左）
            record: Record, //用于动画舞蹈的录制面板（左）
            exeIntro: ExeIntro, //健身指导介绍（底部）
            danceIntro: DanceIntro //动画舞蹈介绍（底部）
        }
    })

</script>
</body>
</html>
