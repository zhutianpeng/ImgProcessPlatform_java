**项目说明**
- 本项目是一个图像处理识别和展示的平台
- 从前端摄像头获取图像，一帧一帧传给后台服务器，中间通过activemq和redis做中间件，server端包含人脸识别和姿态估计两个工程（不完整）的各一份文件（参照）
- redis 做中间件，用的是 pub/sub 模式，注意要在 识别程序中配置 channel 信息
- 局域网下基本实时传输图像，server处理完的图像，client能通过网页的形式访问。时延较少。
<br>

**项目结构** 
暂时没写

**项目结构**
 ![To do...](https://github.com/zhutianpeng/ImgProcessPlatform/raw/master/readme_pic/1.png)

 ![Already done](https://github.com/zhutianpeng/ImgProcessPlatform/raw/master/readme_pic/2.png)
 
 ***安装须知***
 1. pom.xml

```
    <dependency>
            <groupId>org.opencv</groupId>
            <artifactId>opencv</artifactId>
            <version>2.4.13</version>
            <systemPath>"your location"/opencv-2413.jar</systemPath> 
            <scope>system</scope>
    </dependency>
```
2. java/com/tiantian/utils/OpencvUtils
```
//change to your location
public static final  String dllPath = "E:\\opencv_idea\\opencv\\build\\java\\x64\\opencv_java2413.dll";
```