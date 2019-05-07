package com.tiantian.springintejms.entity.MathEntity;

/**
 * Created by AndrewKing on 4/30/2019.
 */
public class VectorEntity {
    //p1指向p2
    public PointEntity p1;
    public PointEntity p2;

    public VectorEntity(PointEntity p1, PointEntity p2){
        this.p1=p1;
        this.p2=p2;
    }
}