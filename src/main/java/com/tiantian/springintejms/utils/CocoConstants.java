package com.tiantian.springintejms.utils;

import org.opencv.core.Scalar;

import java.util.Arrays;
import java.util.List;

/**
 * Created by AndrewKing on 10/17/2018.
 */
public class CocoConstants {
    static {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String path = "E:\\opencv_idea\\opencv\\build\\java\\x64\\opencv_java2413.dll";
        System.load(path);
    }
//       定义一些背景颜色

    private static Scalar s1 = new Scalar(255, 0, 0);
    private static Scalar s2 = new Scalar(255, 85, 0);
    private static Scalar s3 = new Scalar(255, 170, 0);
    private static Scalar s4 = new Scalar(255, 255, 0);
    private static Scalar s5 = new Scalar(170, 255, 0);
    private static Scalar s6 = new Scalar(85, 255, 0);
    private static Scalar s7 = new Scalar(0, 255, 0);
    private static Scalar s8 = new Scalar(0, 255, 85);
    private static Scalar s9 = new Scalar(0, 255, 170);
    private static Scalar s10 = new Scalar(0, 255, 255);
    private static Scalar s11 = new Scalar(0, 170, 255);
    private static Scalar s12 = new Scalar(0, 85, 255);
    private static Scalar s13 = new Scalar(0, 0, 255);
    private static Scalar s14 = new Scalar(85, 0, 255);
    private static Scalar s15 = new Scalar(170, 0, 255);
    private static Scalar s16 = new Scalar(255, 0, 255);
    private static Scalar s17 = new Scalar(255, 0, 170);
    private static Scalar s18 = new Scalar(255, 0, 85);

    public static List<Scalar> cocoColors = Arrays.asList(s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12,s13,s14,s15,s16,s17,s18);

    public static int Background = 18;

    public static int[ ] [ ] CocoPairsRender = {{1, 2},{1, 5},{1, 2}, {1, 5}, {2, 3}, {3, 4}, {5, 6}, {6, 7}, {1, 8}, {8, 9}, {9, 10}, {1, 11},{11, 12}, {12, 13}, {1, 0}, {0, 14}, {14, 16}, {0, 15}, {15, 17}};

}
