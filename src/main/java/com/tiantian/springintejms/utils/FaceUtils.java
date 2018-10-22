package com.tiantian.springintejms.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

/**
 * Created by AndrewKing on 10/18/2018.
 */
public class FaceUtils {
    static{
        String path = OpencvUtils.dllPath;
        System.load(path);
    }

    public static String drawFaces(String faceResultString, String imageResult) {
        Mat image = OpencvUtils.Base64ToMat(imageResult);
        String drawResult = imageResult;
        JSONObject faceObject = JSONObject.fromObject(faceResultString);
        if(faceObject!=null){
            JSONArray faceNames = faceObject.getJSONArray("face_names");
            JSONArray faceLocations = faceObject.getJSONArray("face_locations");

            for(int i=0; i<faceNames.size(); i++){
                String faceName = faceNames.getString(i);
                JSONArray facelocation = JSONArray.fromObject(faceLocations.get(i));
        //draw face
                int top = facelocation.getInt(0)*4;
                int right = facelocation.getInt(1)*4;
                int bottom = facelocation.getInt(2)*4;
                int left = facelocation.getInt(3)*4;

                Core.rectangle(image,new Point(left,top),new Point(right,bottom), new Scalar(0,0,255),2);
                Core.rectangle(image,new Point(left, bottom - 35),new Point(right, bottom), new Scalar(0,255,255));
                Core.putText(image,faceName,new Point(left + 6, bottom - 6), Core.FONT_ITALIC,1.0, new Scalar(255,255,255),1);
            }
            drawResult = OpencvUtils.MatToBase64(image);
        }
        return drawResult;
    }
}
