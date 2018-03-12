package com.ermilitary.CosmoEscape.GameObject;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Collider extends Sprite {
    public float sphere_collider = 0;
    public Vector box_collider = new Vector(0,0,0);

    public void setSphere(float sphere_collider){
        this.sphere_collider = sphere_collider;
    }

    public void setBox(float collider){
        box_collider.set(collider,collider,collider);
    }

    public void setBox(float x, float y){
        box_collider.set(x,y,0);
    }

    public void setBox(Vector collider){
        box_collider.set(collider.x,collider.y,collider.z);
    }

    public boolean isTouchSphere(float x, float y){
        double distance = Vector.Distance(position.x,position.y,x,y);
        if(distance < sphere_collider)
            return true;
        return false;
    }
    public boolean isTouchSphere(Vector touch){
        double distance = Vector.Distance(position.x,position.y,touch.x,touch.y);
        if(distance < sphere_collider)
            return true;
        return false;
    }
    public boolean isTouchBox(Vector touch){
        Vector nDir = Vector.getDirection((Vector.getAngle(touch.minus(position)) - rotation) % 360);
        Vector corecrion = new Vector(position.x + nDir.x*Vector.length(touch.minus(position)), position.y + nDir.y*Vector.length(touch.minus(position)), 0);
        if(corecrion.x < position.x + box_collider.x && corecrion.x > position.x - box_collider.x
                && corecrion.y < position.y + box_collider.y && corecrion.y > position.y - box_collider.y)
            return true;
        return false;
    }
    public boolean isBoxTouchSphere(Collider touch){
        Vector nDir = Vector.getDirection((Vector.getAngle(touch.position.minus(position)) - rotation) % 360);
        Vector corecrion = new Vector(position.x + nDir.x*Vector.length(touch.position.minus(position)), position.y + nDir.y*Vector.length(touch.position.minus(position)), 0);
        if(corecrion.x - touch.sphere_collider < position.x + box_collider.x && corecrion.x + touch.sphere_collider > position.x - box_collider.x
                && corecrion.y - touch.sphere_collider < position.y + box_collider.y && corecrion.y + touch.sphere_collider > position.y - box_collider.y)
            return true;
        return false;
    }
    public boolean isCollision(Collider b){
        if(Vector.Distance(position,b.position) < sphere_collider + b.sphere_collider)
            return true;
        return false;
    }
    public static boolean isCollision(Collider a, Collider b){
        if(Vector.Distance(a.position,b.position) < a.sphere_collider + b.sphere_collider)
            return true;
        return false;
    }
}
