package com.ermilitary.CosmoEscape.Units.Drop;

import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.Game;
import com.ermilitary.CosmoEscape.GameObject.Collider;
import com.ermilitary.CosmoEscape.GameObject.Sprite;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Level.GeoSystem;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Aggrieved extends Unit {
    Sprite avatar_frame;
    Collider avatar;

    public Aggrieved(GeoSystem geoSystem) {
        super(geoSystem);
        avatar_frame = new Sprite();
        avatar_frame.setScale(0.3f);
        avatar_frame.setPosition(Game.ratio - 0.35f, -0.65f, 0);
        avatar = new Collider();
        avatar.setScale(0.2f);
        avatar.setSphere(0.2f);
        avatar.setPosition(avatar_frame.position);

        setSphere(0.03f);
        setScale(0.02f,0.02f,1);
        health = max_health = 1;
        max_m_speed = 0.1f;
        max_r_speed = 200f;

        vis_zone = 0.4f;
    }

    @Override
    public void draw(GL10 gl) {
        if(death){ return; }
        // уступай и не толкай сородичей своих
        notPush();
        // ---движение---
        move();
        super.draw(gl);
    }

    @Override
    public void resLoad() {
        setSprite(geoSistem.textures[45]);
        avatar_frame.setSprite(geoSistem.textures[4]);
        avatar.setSprite(geoSistem.textures[36]);
    }
    @Override
    public void GUIdraw(GL10 gl) {
        avatar_frame.draw(gl);
        avatar.draw(gl);
    }

    @Override
    public int GUIinPut(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if(avatar.isTouchSphere(Vector.prepare_event(event)))
                if (pack != null)
                    return setPack;
                else
                    return next;
            else
                return exit;
        }
        return next;
    }

    @Override
    public void Death() {
        setPack(null);
        delete = 0;
        super.Death();
    }
}