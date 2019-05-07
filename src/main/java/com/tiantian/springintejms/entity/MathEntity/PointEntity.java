package com.tiantian.springintejms.entity.MathEntity;

/**
 * Created by AndrewKing on 4/30/2019.
 */
public class PointEntity {
    public float x;
    public float y;
    public float confidence;
    public PointEntity(float x, float y, float confidence){
        this.x=x;
        this.y=y;
        this.confidence=confidence;
    }
}