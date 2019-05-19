package com.tiantian.springintejms.utils;
import com.tiantian.springintejms.entity.MathEntity.VectorEntity;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.highgui.Highgui;

import java.util.Base64;

public class OpencvUtils {
    public static final  String dllPath = "D:\\opencv\\build\\java\\x64\\opencv_java2413.dll";
    static{
        System.load(dllPath);
    }
    public static Mat Base64ToMat(String s){
//      base64转mat，with 'data:image/jpeg;base64,' this head
        byte[] data = Base64.getDecoder().decode(s.split(",")[1]);
        Mat image = Highgui.imdecode(new MatOfByte(data), Highgui.CV_LOAD_IMAGE_UNCHANGED);
        return image;
    }

    public static String MatToBase64(Mat mat){
//     mat转base64, with 'data:image/jpeg;base64,' this head
        MatOfByte matOfByte = new MatOfByte();
        Highgui.imencode(".jpg", mat, matOfByte);
        String dataString = Base64.getEncoder().encodeToString(matOfByte.toArray());
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

    /**
     * This static method inputs three point
     * 两个不相交的向量，通用的方法
     * @return return Cos Similarity
     */
    public static int getCosSimilar(VectorEntity v1, VectorEntity v2){
        float dx1 = v1.p2.x - v1.p1.x;
        float dx2 = v2.p2.x - v2.p1.x;
        float dy1 = v1.p2.y - v1.p1.y;
        float dy2 = v2.p2.y - v2.p1.y;

        //弧度：
        double result = Math.acos(  (dx1*dx2+dy1*dy2)/Math.sqrt(dx1*dx1+dx2*dx2+dy1*dy1+dy2*dy2) );
        //相似度：
        int cosSimilarity = (int)(result/Math.PI);
        return cosSimilarity;
    }


}
