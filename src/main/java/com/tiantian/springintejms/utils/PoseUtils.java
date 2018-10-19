package com.tiantian.springintejms.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.highgui.Highgui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by AndrewKing on 10/15/2018.
 */

public class PoseUtils {

    static{
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String path = OpencvConfig.dllPath;
        System.load(path);
    }

    public static String drawHumans(String poseResultString, String imageContent) {
        JSONArray poseArray = JSONArray.fromObject(poseResultString);
        String drawResult = imageContent;
        if (!poseArray.isEmpty()) {
//      原始图片的信息获取
            Mat image = Base64ToMat(imageContent);

            float width = image.width();
            float height = image.height();
//      对每个人的获取
            for (int i = 0; i < poseArray.size(); i++) {
                JSONObject human = poseArray.getJSONObject(i);
                JSONObject bodyParts = JSONObject.fromObject(human.get("body_parts"));

                Set keySet = bodyParts.keySet();
                List<Point> centers = new ArrayList<Point>(); // 中心点的集合
                // draw point
                for(int j=1; j<=CocoConstants.Background; j++){
                    if(!keySet.contains(j)) {
                        continue;
                    }
                    JSONObject bodyPart = bodyParts.getJSONObject(String.valueOf(j));
                    // 画图
                    float x = (Float)JSONArray.fromObject(JSONObject.fromObject(bodyPart.get("x")).get("py/newargs")).get(0);
                    float y = (Float)JSONArray.fromObject(JSONObject.fromObject(bodyPart.get("y")).get("py/newargs")).get(0);
                    Point center = new Point(x, y);
                    centers.add(center);
                    Core.circle(image, center, 3, CocoConstants.cocoColors.get(j), 3,8,0);
                }
                // draw line
                for(int j=0; j<CocoConstants.CocoPairsRender.length; j++){
                    if ( !(keySet.contains(CocoConstants.CocoPairsRender[j][0]) && keySet.contains(CocoConstants.CocoPairsRender[j][1]))){
                        continue;
                    }
                    Core.line(image,centers.get(CocoConstants.CocoPairsRender[j][0]),centers.get(CocoConstants.CocoPairsRender[j][1]),CocoConstants.cocoColors.get(j),3);
                }
            }
            drawResult = MatToBase64(image);
        }
        return drawResult;
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
