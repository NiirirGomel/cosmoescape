package com.ermilitary.CosmoEscape.GameObject;


/**
 * Created by Ermilashka on 03.04.2015.
 */
public class GameObject {
    public Vector scale = new Vector(1,1,1);
    public Vector position = new Vector(0,0,0);
    public float rotation = 0;

    public void setPosition(Vector position) {
        this.position.x = position.x;
        this.position.y = position.y;
        this.position.z = position.z;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }
    public void setRotation(float angel){
        rotation = angel;
    }
    public void setScale(float scale){
        this.scale.x = scale;
        this.scale.y = scale;
        this.scale.z = scale;
    }
    public void setScale(float x, float y, float z) {
        this.scale.x = x;
        this.scale.y = y;
        this.scale.z = z;
    }
    public void setScale(Vector scale) {
        this.scale.x = scale.x;
        this.scale.y = scale.y;
        this.scale.z = scale.z;
    }
}
