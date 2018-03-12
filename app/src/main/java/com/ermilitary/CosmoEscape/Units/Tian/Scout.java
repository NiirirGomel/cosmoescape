package com.ermilitary.CosmoEscape.Units.Tian;

import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Level.GeoSystem;
import com.ermilitary.CosmoEscape.Packs.Pack;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Scout extends TanUnit {
    public Scout(GeoSystem  gs){
        super(gs);
        setRotation(135f);
        setSphere(0.1f);

        max_tian = 1;
        tian = need_tian = 1;

        setScale(0.1f);
        health = max_health = 450;
        energy = max_energy = 0;

        max_m_speed = 0.4f;
        max_r_speed = 150f;

        vis_zone = 0.5f;
        delete = 0.3f;
    }

    @Override
    public void draw(GL10 gl) {
        if(death){ Death(gl); return; }
        // ***БОЙ***
        if (pack.target_enemy != null){
            // если взят в тарджет то убег от врага

            // уступай и не толкай сородичей своих
            notPush();
            // на путь разведчика вынеси отряд свой
            if (!moving && !rotating && Vector.Distance(position, pack.target_enemy.position) < pack.target_enemy.vis_zone) {
                rot_from(pack.target_enemy.position.minus(position));
                moving = true;
                rotating = true;
            }
            // не улетай далеко от дома своего
            if (!moving && !rotating && Vector.Distance(position, pack.target_enemy.position) > pack.target_enemy.vis_zone + vis_zone) {
                rotating = true;
                moving = true;
                rot_to(pack.target_enemy.position.minus(position));
            }
            // летай вокруг базы с отрядом своим
            if (!moving && !rotating) {
                rotating = true;
                moving = true;
                rot_to(Vector.getAngle(pack.target_enemy.position.minus(position)) + 90);
            }
        }
        // ******

        // ускоряющая абилка
        if (pack.mission == Pack.MISSION_SCOUT || pack.target_enemy != null){
            max_m_speed = 0.8f;
            vis_zone = 1f;
        } else {
            max_m_speed = 0.4f;
            vis_zone = 0.5f;
        }
        super.draw(gl);
    }

    public void Death(GL10 gl) {
        float timer = 1 - delete/0.3f*1;
        explosion.setPosition(position.x+0.02f,position.y-0.01f,0);
        explosion.setScale(0.03f + 0.06f * timer);
        explosion.draw(gl);
        explosion.setPosition(position.x - 0.01f, position.y + 0.05f, 0);
        explosion.setScale(0.02f + 0.04f * timer);
        explosion.draw(gl);
        explosion.setPosition(position.x - 0.03f, position.y - 0.03f, 0);
        explosion.setScale(0.03f + 0.06f * timer);
        explosion.draw(gl);
        airblade.setPosition(position.x+ Vector.getDirection(rotation + 8).x*0.2f*timer,position.y+Vector.getDirection(rotation + 8).y*0.2f*timer,0);
        airblade.setRotation(rotation + 8);
        airblade.setScale(0.1f, 0.03f, 1);
        airblade.draw(gl);
        airblade.setPosition(position.x+Vector.getDirection(rotation + 172).x*0.2f*timer,position.y+Vector.getDirection(rotation + 172).y*0.2f*timer,0);
        airblade.setRotation(rotation + 172);
        airblade.setScale(0.1f ,0.03f ,1);
        airblade.draw(gl);
        delete -= ActiveElement.delayTime / 1000;
    }
    @Override
    public void resLoad() {
        super.resLoad();
        setSprite(geoSistem.textures[13]);
    }
}
