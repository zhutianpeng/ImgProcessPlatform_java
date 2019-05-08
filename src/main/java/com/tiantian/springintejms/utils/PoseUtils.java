package com.tiantian.springintejms.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
                // draw angle
                for(int j=0;j<CocoConstants.CocoPairsAngle.length;j++){
                    if(!(keySet.contains(String.valueOf(CocoConstants.CocoPairsAngle[j][0])) && keySet.contains(String.valueOf(CocoConstants.CocoPairsAngle[j][1])) && keySet.contains(String.valueOf(CocoConstants.CocoPairsAngle[j][2]))) ){
                        continue;
                    }
                    Point P1 = centerMap.get(CocoConstants.CocoPairsAngle[j][0]);
                    Point PCenter= centerMap.get(CocoConstants.CocoPairsAngle[j][1]);
                    Point P2 = centerMap.get(CocoConstants.CocoPairsAngle[j][2]);
                    String Angle = String.valueOf(OpencvUtils.getAngle(PCenter,P1,P2));
                    Core.putText(image,Angle,PCenter, Core.FONT_ITALIC,0.8, new Scalar(124,252,0),1);
                }

            }
            drawResult = OpencvUtils.MatToBase64(image);
        }
        return drawResult;
    }

    /**
     * This static method inputs 结果pose Json 的 String, 原始图片的宽，高
     * @return ArrayList<HashMap<Integer,PointEntity>> 图片中每个人的每个关节点的位置
     */
    public static JSONArray getPoseData (String poseResultString, String imageContent) {
        //返回的结果
        ArrayList<JSONObject> poseList = new ArrayList<JSONObject>();

        JSONArray poseArray = JSONArray.fromObject(poseResultString);
        if (!poseArray.isEmpty()) {
            Mat image = OpencvUtils.Base64ToMat(imageContent);
            float width = image.width();
            float height = image.height();

            //对每个人的获取
            for (int i = 0; i < poseArray.size(); i++) {
                JSONObject human = poseArray.getJSONObject(i);
                JSONObject bodyParts = JSONObject.fromObject(human.get("body_parts"));

                Set keySet = bodyParts.keySet();
                HashMap<String, JSONObject> centerMap = new HashMap<String, JSONObject>();// 中心点的集合
                // draw point
                for(int j=0; j<CocoConstants.Background; j++){
                    if(!keySet.contains(String.valueOf(j))) {
                        continue;
                    }
                    JSONObject bodyPart = bodyParts.getJSONObject(String.valueOf(j));
                    // 画图
                    float x = Float.parseFloat(JSONArray.fromObject(JSONObject.fromObject(bodyPart.get("x")).get("py/newargs")).get(0).toString());
                    float y = Float.parseFloat(JSONArray.fromObject(JSONObject.fromObject(bodyPart.get("y")).get("py/newargs")).get(0).toString());
                    float confidence = Float.parseFloat(JSONArray.fromObject(JSONObject.fromObject(bodyPart.get("score")).get("py/newargs")).get(0).toString());;

//                    PointEntity center = new PointEntity(x*width+0.5f, y*height+0.5f, confidence);
                    JSONObject centerObject = new JSONObject();
                    centerObject.put("x",x*width+0.5f);
                    centerObject.put("y",y*width+0.5f);
                    centerObject.put("confidence",confidence);

                    centerMap.put(String.valueOf(j), centerObject);
                }
                JSONObject centerMapObject = JSONObject.fromObject(centerMap);
                poseList.add(centerMapObject);
            }
        }

        JSONArray result = JSONArray.fromObject(poseList);
        return result;


    }

}
