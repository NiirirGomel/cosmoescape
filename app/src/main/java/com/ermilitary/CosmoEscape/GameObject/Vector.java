package com.ermilitary.CosmoEscape.GameObject;

import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.Game;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Vector {
    public float x,y,z;
    public Vector(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector(Vector vector){
        x = vector.x;
        y = vector.y;
        z = vector.z;
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public void set(Vector position) {
        x = position.x;
        y = position.y;
        z = position.z;
    }

    public static float Distance(float ax, float ay, float bx, float by){
        return (float)Math.sqrt((bx - ax)*(bx - ax) + (by - ay)*(by - ay));
    }

    public static float Distance(Vector a, Vector b){
        return (float)Math.sqrt((b.x - a.x)*(b.x - a.x) + (b.y - a.y)*(b.y - a.y));
    }

    public static Vector prepare_event(MotionEvent event){
        return new Vector(((event.getX() - Game.width / 2) / Game.width) * Game.ratio * 2,((-event.getY() + Game.height / 2) / Game.height) * 2,0);
    }

    public static float length(Vector vec){
        return (float)Math.sqrt(vec.x * vec.x + vec.y*vec.y);
    }

    public static Vector normalize(Vector vec){
        //Log.v("Vec",vec.x + "x" + vec.y);
        float inv_length = Vector.length(vec);
        if (inv_length == 0)
            return new Vector(0,0,0);
        else
            return new Vector(vec.x/inv_length,vec.y/inv_length,0);
    }
    public Vector minus(Vector position){
        return new Vector(x - position.x, y - position.y, 0);
    }

    public static Vector getDirection(float angle){
        angle = (float)Math.toRadians(angle);
        return new Vector(-(float)Math.sin(angle),(float)Math.cos(angle),0);
//        //----------------------------
//        Log.v("cur_direction",direction.x+"x"+direction.y);
//        int angle = 45;
//        Log.v("angle/radian",""+angle+"/"+Math.toRadians(angle));
//        Vector res = new Vector(0,0,0);
//        Log.v("angle sin/cos",(float)Math.sin(angle)+"/"+(float)Math.cos(angle));
//        Log.v("angle radian sin/cos",Math.sin(Math.toRadians(angle))+"/"+Math.cos(Math.toRadians(angle)));
//        res.x = direction.x*(float)Math.cos(Math.toRadians(angle)) - direction.y*(float)Math.sin(Math.toRadians(angle));
//        res.y = direction.x*(float)Math.sin(Math.toRadians(angle)) + direction.y*(float)Math.cos(Math.toRadians(angle));
//        Log.v("res_direction",res.x+"x"+res.y);
//        //----------------------------
    }

    public  static float getAngle(Vector vector){
        if (vector.x < 0)
            return (float)Math.toDegrees(Math.acos((0 * vector.x + 1 * vector.y) / (Math.sqrt(0 * 0 + 1 * 1) * Math.sqrt(vector.x * vector.x + vector.y * vector.y))));
        else
            return 360 - (float)Math.toDegrees(Math.acos((0 * vector.x + 1 * vector.y) / (Math.sqrt(0 * 0 + 1 * 1) * Math.sqrt(vector.x * vector.x + vector.y * vector.y))));
    }
    public boolean equals(Vector v){
        return (this.x == v.x && this.y == v.y);
    }
}
