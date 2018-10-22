package com.tiantian.springintejms.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
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

}
