package com.ermilitary.CosmoEscape.Level;

import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.GameObject.GameObject;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Packs.Pack;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Camera extends GeoSystem {
    public GameObject camera = new GameObject();
    public Pack targetNav;
    public float m_speed = 5;
    public float s_speed = 1;

    public void setCameraTarget(Pack target){
        this.targetNav = target;
    }

    public Camera(){
        //camera.setPosition(1,0,0);
        //camera.setScale(0.5f);
    }

    private void move(){
        if(targetNav != null) {
            if(!targetNav.position.equals(camera.position)) {
                Vector direction = Vector.normalize(targetNav.position.minus(camera.position));
                float move_x = direction.x * m_speed * delayTime / 1000;
                float move_y = direction.y * m_speed * delayTime / 1000;
                if (Vector.Distance(camera.position, targetNav.position) > Vector.length(new Vector(move_x, move_y, 0))) {
                    camera.position.x += move_x;
                    camera.position.y += move_y;
                } else {
                    camera.position.x = targetNav.position.x;
                    camera.position.y = targetNav.position.y;
                }
            }
            if(1/targetNav.vis_zone > camera.scale.x) {
                float scale = camera.scale.x + s_speed * delayTime / 1000;
                if (scale > 1/targetNav.vis_zone) scale = 1/targetNav.vis_zone;
                camera.setScale(scale);
            } else if (1/targetNav.vis_zone < camera.scale.x) {
                float scale = camera.scale.x - s_speed * delayTime / 1000;
                if (scale < 1/targetNav.vis_zone) scale = 1/targetNav.vis_zone;
                camera.setScale(scale);
            }
        }
    }

    public Vector cam_set_event(MotionEvent event){
        Vector prepare = Vector.prepare_event(event);
        prepare.x = (prepare.x / camera.scale.x + camera.position.x);
        prepare.y = (prepare.y / camera.scale.y + camera.position.y);
        return prepare;
    }

    public Vector cam_set_vec(Vector vector){
        return new Vector(vector.x * camera.scale.x - camera.position.x * camera.scale.x, vector.y * camera.scale.x - camera.position.y * camera.scale.y, 0);
    }

    @Override
    public void draw(GL10 gl) {
        // фон
        backGround.draw(gl);

        move();
        gl.glPushMatrix();
        gl.glScalef(camera.scale.x, camera.scale.y, 1);
        gl.glTranslatef(-camera.position.x, -camera.position.y, 0);
        gl.glRotatef(camera.rotation, 0, 0, 1);
        super.draw(gl);
        gl.glPopMatrix();
    }

    @Override
    public void setNav(Pack p) {
        targetNav = p;
    }
    public Pack getNav(){
        return targetNav;
    };

    @Override
    public GameObject getCam() {
        return camera;
    }
}
