package com.tiantian.springintejms.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.highgui.Highgui;

public class OpencvUtils {
    public static final  String dllPath = "E:\\opencv_idea\\opencv\\build\\java\\x64\\opencv_java2413.dll";
    static{
        System.load(dllPath);
    }
    public static Mat Base64ToMat(String s){
//      base64转mat，with 'data:image/jpeg;base64,' this head
        byte[] data = Base64.decode(s.split(",")[1]);
        Mat image = Highgui.imdecode(new MatOfByte(data), Highgui.CV_LOAD_IMAGE_UNCHANGED);
        return image;
    }

    public static String MatToBase64(Mat mat){
//     mat转base64, with 'data:image/jpeg;base64,' this head
        MatOfByte matOfByte = new MatOfByte();
        Highgui.imencode(".jpg", mat, matOfByte);
        String dataString = new String(Base64.encode(matOfByte.toArray()));
        dataString = "data:image/jpeg;base64," + dataString;
        return dataString;
    }

    public static int getAngle(Point Px, Point Py, Point Pz) {
//        Px is the center point
        double x1 = Px.x;
        double x2 = Px.y;
        double y1 = Py.x;
        double y2 = Py.y;
        double z1 = Pz.x;
        double z2 = Pz.y;

        //向量的点乘
        double t =(y1-x1)*(z1-x1)+(y2-x2)*(z2-x2);

        // A=向量的点乘/向量的模相乘
        // B=arccos(A)，用反余弦求出弧度
        // result=180*B/π 弧度转角度制
        int result =(int)(180*Math.acos(
                t/Math.sqrt
                        ((Math.abs((y1-x1)*(y1-x1))+Math.abs((y2-x2)*(y2-x2)))
                                *(Math.abs((z1-x1)*(z1-x1))+Math.abs((z2-x2)*(z2-x2)))
                        ))
                /Math.PI);
        //      pi   = 180
        //      x    =  ？
        //====> ?=180*x/pi
        return result;
    }

}
