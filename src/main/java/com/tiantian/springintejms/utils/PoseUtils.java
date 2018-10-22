package com.tiantian.springintejms.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.*;

/**
 * Created by AndrewKing on 10/15/2018.
 */

public class PoseUtils {

    static{
        String path = OpencvUtils.dllPath;
        System.load(path);
    }

    public static String drawHumans(String poseResultString, String imageContent) {
        JSONArray poseArray = JSONArray.fromObject(poseResultString);
        String drawResult = imageContent;
        if (!poseArray.isEmpty()) {
//      原始图片的信息获取
            Mat image = OpencvUtils.Base64ToMat(imageContent);

            float width = image.width();
            float height = image.height();
//      对每个人的获取
            for (int i = 0; i < poseArray.size(); i++) {
                JSONObject human = poseArray.getJSONObject(i);
                JSONObject bodyParts = JSONObject.fromObject(human.get("body_parts"));

                Set keySet = bodyParts.keySet();
//                List<Point> centers = new ArrayList<Point>(); // 中心点的集合
                Map<Integer,Point> centerMap = new HashMap<Integer, Point>();// 中心点的集合
                // draw point
                for(int j=0; j<CocoConstants.Background; j++){
                    if(!keySet.contains(String.valueOf(j))) {
                        continue;
                    }
                    JSONObject bodyPart = bodyParts.getJSONObject(String.valueOf(j));
                    // 画图
                    float x = Float.parseFloat(JSONArray.fromObject(JSONObject.fromObject(bodyPart.get("x")).get("py/newargs")).get(0).toString());
                    float y = Float.parseFloat(JSONArray.fromObject(JSONObject.fromObject(bodyPart.get("y")).get("py/newargs")).get(0).toString());
                    Point center = new Point(x*width+0.5, y*height+0.5);
//                    centers.add(j,center);
                    centerMap.put(j,center);
                    Core.circle(image, center, 3, CocoConstants.cocoColors.get(j), 3,8,0);
                }
                // draw line
                for(int j=0; j<CocoConstants.CocoPairsRender.length; j++){
                    if ( !(keySet.contains(String.valueOf(CocoConstants.CocoPairsRender[j][0])) && keySet.contains(String.valueOf(CocoConstants.CocoPairsRender[j][1]))) ){
                        continue;
                    }
                    Core.line(image,centerMap.get(CocoConstants.CocoPairsRender[j][0]),centerMap.get(CocoConstants.CocoPairsRender[j][1]),CocoConstants.cocoColors.get(j),3);
                }
            }
            drawResult = OpencvUtils.MatToBase64(image);
        }
        return drawResult;
    }

}
